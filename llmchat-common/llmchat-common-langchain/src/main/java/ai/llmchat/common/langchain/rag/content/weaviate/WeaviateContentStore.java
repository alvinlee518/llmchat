package ai.llmchat.common.langchain.rag.content.weaviate;

import ai.llmchat.common.langchain.rag.content.ContentSearchOptions;
import ai.llmchat.common.langchain.rag.content.ContentStore;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.filter.Filter;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.base.WeaviateErrorMessage;
import io.weaviate.client.v1.batch.api.ObjectsBatcher;
import io.weaviate.client.v1.batch.model.BatchDeleteResponse;
import io.weaviate.client.v1.batch.model.ObjectGetResponse;
import io.weaviate.client.v1.batch.model.ObjectsGetResponseAO2Result;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import io.weaviate.client.v1.filters.WhereFilter;
import io.weaviate.client.v1.graphql.model.GraphQLError;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.*;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;
import io.weaviate.client.v1.schema.model.DataType;
import io.weaviate.client.v1.schema.model.Property;
import io.weaviate.client.v1.schema.model.Tokenization;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class WeaviateContentStore implements ContentStore, InitializingBean {
    public static final String METADATA_FIELD_PREFIX = "meta_";
    private static final String CONTENT_FIELD_NAME = "content";
    private static final String METADATA_FIELD_NAME = "metadata";
    private static final String ADDITIONAL_FIELD_NAME = "_additional";
    private static final String ADDITIONAL_ID_FIELD_NAME = "id";
    private static final String ADDITIONAL_CERTAINTY_FIELD_NAME = "certainty";
    private static final String ADDITIONAL_SCORE_FIELD_NAME = "score";
    private static final String ADDITIONAL_VECTOR_FIELD_NAME = "vector";

    private final WeaviateClient client;
    private final String consistencyLevel;
    private final String className;
    private final Collection<String> metadataKeys;
    private final Field[] searchFields;

    public WeaviateContentStore(WeaviateClient client, String consistencyLevel, String className, Collection<String> metadataKeys) {
        this.client = client;
        this.consistencyLevel = Utils.getOrDefault(consistencyLevel, ConsistencyLevel.QUORUM);
        this.className = Utils.getOrDefault(className, "LLMChat_Embedding");
        this.metadataKeys = Utils.getOrDefault(metadataKeys, Collections.emptyList());
        this.searchFields = buildSearchFields();
    }

    @Override
    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ValidationUtils.ensureTrue(ids.size() == embeddings.size(), "ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue(embedded == null || embeddings.size() == embedded.size(), "embeddings size is not equal to embedded size");
        List<WeaviateObject> objects = new ArrayList<>();
        for (int i = 0; i < embeddings.size(); i++) {
            objects.add(toWeaviateObject(ids.get(i), embeddings.get(i), CollectionUtils.isEmpty(embedded) ? null : embedded.get(i)));
        }
        try (ObjectsBatcher objectsBatcher = this.client.batch().objectsBatcher()) {
            Result<ObjectGetResponse[]> result = objectsBatcher
                    .withObjects(objects.toArray(new WeaviateObject[0]))
                    .withConsistencyLevel(this.consistencyLevel)
                    .run();

            if (result.hasErrors()) {
                throw new RuntimeException("failed to add documents because: \n" + result.getError().getMessages().stream().map(WeaviateErrorMessage::getMessage).collect(Collectors.joining(System.lineSeparator())));
            }
            List<String> errorMessages = new ArrayList<>();
            if (result.getResult() != null) {
                for (var r : result.getResult()) {
                    if (r.getResult() != null && r.getResult().getErrors() != null) {
                        var error = r.getResult().getErrors();
                        errorMessages.add(error.getError().stream().map(ObjectsGetResponseAO2Result.ErrorItem::getMessage).collect(Collectors.joining(System.lineSeparator())));
                    }
                }
            }

            if (!CollectionUtils.isEmpty(errorMessages)) {
                throw new RuntimeException("failed to add documents because: \n" + errorMessages);
            }
        }
    }

    @Override
    public void removeAll(Filter filter) {
        WhereFilter whereFilter = Objects.isNull(filter) ? null : WeaviateFilterMapper.map(filter, this.metadataKeys.toArray(new String[0]));
        Result<BatchDeleteResponse> result = this.client.batch()
                .objectsBatchDeleter()
                .withClassName(this.className)
                .withConsistencyLevel(this.consistencyLevel)
                .withWhere(whereFilter)
                .run();
        if (result.hasErrors()) {
            throw new RuntimeException("failed to delete documents because: \n" + result.getError().getMessages().stream().map(WeaviateErrorMessage::getMessage).collect(Collectors.joining(System.lineSeparator())));
        }
    }

    @Override
    public List<EmbeddingMatch<TextSegment>> keywordSearch(ContentSearchOptions options) {
        WhereFilter whereFilter = WeaviateFilterMapper.map(options.getFilter(), this.metadataKeys.toArray(new String[0]));
        GetBuilder builder = GetBuilder.builder()
                .className(this.className)
                .limit(options.getMaxResults())
                .withWhereFilter(WhereArgument.builder().filter(whereFilter).build())
                .withBm25Filter(Bm25Argument.builder()
                        .properties(new String[]{CONTENT_FIELD_NAME})
                        .query(options.getKeyword()).build())
                .fields(Fields.builder().fields(this.searchFields).build())
                .build();
        String graphQLQuery = builder.buildQuery();

        return doSearch(graphQLQuery);
    }

    @Override
    public List<EmbeddingMatch<TextSegment>> similaritySearch(ContentSearchOptions options) {
        WhereFilter whereFilter = WeaviateFilterMapper.map(options.getFilter(), this.metadataKeys.toArray(new String[0]));
        GetBuilder getBuilder = GetBuilder.builder()
                .className(this.className)
                .limit(options.getMaxResults())
                .withNearVectorFilter(
                        NearVectorArgument.builder()
                                .vector(options.getEmbedding().vectorAsList().toArray(new Float[0]))
                                .certainty(Optional.ofNullable(options.getMinScore()).orElse(0d).floatValue())
                                .build()
                )
                .withWhereFilter(WhereArgument.builder().filter(whereFilter).build())
                .fields(Fields.builder().fields(this.searchFields).build())
                .build();

        String graphQLQuery = getBuilder.buildQuery();

        return doSearch(graphQLQuery);
    }

    @Override
    public List<EmbeddingMatch<TextSegment>> hybridSearch(ContentSearchOptions options) {
        WhereFilter whereFilter = WeaviateFilterMapper.map(options.getFilter(), this.metadataKeys.toArray(new String[0]));
        GetBuilder getBuilder = GetBuilder.builder()
                .className(this.className)
                .limit(options.getMaxResults())
                .withHybridFilter(HybridArgument.builder()
                        .properties(new String[]{CONTENT_FIELD_NAME})
                        .query(options.getKeyword())
                        .vector(options.getEmbedding().vectorAsList().toArray(new Float[0]))
                        .alpha(0.75f)
                        .fusionType(FusionType.RELATIVE_SCORE)
                        .build())
                .withWhereFilter(WhereArgument.builder().filter(whereFilter).build())
                .fields(Fields.builder().fields(this.searchFields).build())
                .build();
        String graphQLQuery = getBuilder.buildQuery();

        return doSearch(graphQLQuery);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("initializing weaviate schema for className: {}", this.className);
        Result<Boolean> exists = this.client.schema().exists().withClassName(this.className).run();
        if (Optional.ofNullable(exists).map(Result::getResult).orElse(false)) {
            log.info("className: {} already exists", this.className);
            return;
        }

        Property content = Property.builder()
                .indexFilterable(true)
                .indexSearchable(true)
                .tokenization(Tokenization.WORD)
                .dataType(List.of(DataType.TEXT))
                .name(CONTENT_FIELD_NAME)
                .build();
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(content);
        for (String key : metadataKeys) {
            Property meta = Property.builder()
                    .indexFilterable(true)
                    .indexSearchable(true)
                    .dataType(List.of(DataType.TEXT))
                    .name(METADATA_FIELD_PREFIX + key)
                    .build();
            propertyList.add(meta);
        }
        WeaviateClass embeddingClass = WeaviateClass.builder().className(this.className).properties(propertyList).build();
        Result<Boolean> result = this.client.schema().classCreator().withClass(embeddingClass).run();
        if (result.hasErrors()) {
            throw new RuntimeException("failed to create schema because: \n" + result.getError().getMessages().stream().map(WeaviateErrorMessage::getMessage).collect(Collectors.joining(System.lineSeparator())));
        }
    }

    private Field[] buildSearchFields() {
        List<Field> fields = new ArrayList<>(metadataKeys.stream().map(item -> Field.builder().name(METADATA_FIELD_PREFIX + item).build()).toList());
        fields.add(Field.builder().name(CONTENT_FIELD_NAME).build());
        fields.add(Field.builder()
                .name(ADDITIONAL_FIELD_NAME)
                .fields(
                        Field.builder().name(ADDITIONAL_ID_FIELD_NAME).build(),
                        Field.builder().name(ADDITIONAL_VECTOR_FIELD_NAME).build(),
                        Field.builder().name(ADDITIONAL_CERTAINTY_FIELD_NAME).build(),
                        Field.builder().name(ADDITIONAL_SCORE_FIELD_NAME).build()
                ).build());
        return fields.toArray(new Field[0]);
    }

    private WeaviateObject toWeaviateObject(String id, Embedding embedding, TextSegment segment) {
        Map<String, Object> metadata = new HashMap<>();
        String content = StringUtils.EMPTY;
        if (Objects.nonNull(segment)) {
            metadata = segment.metadata().toMap();
            content = segment.text();
        }
        Map<String, Object> fields = new HashMap<>();
        fields.put(CONTENT_FIELD_NAME, content);
        fields.put(METADATA_FIELD_NAME, metadata);
        for (String key : metadataKeys) {
            fields.put(METADATA_FIELD_PREFIX + key, metadata.getOrDefault(key, -1).toString());
        }
        return WeaviateObject.builder()
                .className(this.className)
                .id(id)
                .vector(embedding.vectorAsList().toArray(new Float[0]))
                .properties(fields)
                .creationTimeUnix(System.currentTimeMillis())
                .build();
    }


    @SuppressWarnings("unchecked")
    private List<EmbeddingMatch<TextSegment>> doSearch(String graphQLQuery) {
        log.info("execute graphql query:{}", graphQLQuery);
        Result<GraphQLResponse> result = this.client
                .graphQL()
                .raw()
                .withQuery(graphQLQuery)
                .run();
        if (result.hasErrors()) {
            throw new RuntimeException("failed to search document because: \n" + result.getError().getMessages().stream().map(WeaviateErrorMessage::getMessage).collect(Collectors.joining(System.lineSeparator())));
        }

        GraphQLError[] errors = result.getResult().getErrors();
        if (errors != null && errors.length > 0) {
            throw new RuntimeException("failed to search document because: \n" + Arrays.stream(errors).map(GraphQLError::getMessage).collect(Collectors.joining(System.lineSeparator())));
        }

        Optional<Map.Entry<String, Map<?, ?>>> resGetPart = ((Map<String, Map<?, ?>>) result.getResult().getData()).entrySet().stream().findFirst();
        if (resGetPart.isEmpty()) {
            return List.of();
        }

        Optional<?> resItemsPart = resGetPart.get().getValue().entrySet().stream().findFirst();
        if (resItemsPart.isEmpty()) {
            return List.of();
        }

        List<Map<String, ?>> resItems = ((Map.Entry<String, List<Map<String, ?>>>) resItemsPart.get()).getValue();

        return resItems.stream().map(this::toEmbeddingMatch).toList();
    }

    @SuppressWarnings("unchecked")
    private EmbeddingMatch<TextSegment> toEmbeddingMatch(Map<String, ?> item) {
        Map<String, ?> additional = (Map<String, ?>) item.get(ADDITIONAL_FIELD_NAME);
        final Metadata metadata = new Metadata();
        for (String metadataKey : metadataKeys) {
            metadata.add(metadataKey, item.getOrDefault(METADATA_FIELD_PREFIX + metadataKey, null));
        }
        String text = (String) item.get(CONTENT_FIELD_NAME);
        String score = (String) additional.get(ADDITIONAL_SCORE_FIELD_NAME);
        return new EmbeddingMatch<>(
                Optional.ofNullable((Double) additional.get(ADDITIONAL_CERTAINTY_FIELD_NAME)).orElse(Double.parseDouble(score)),
                (String) additional.get(ADDITIONAL_ID_FIELD_NAME),
                Embedding.from(((List<Double>) additional.get(ADDITIONAL_VECTOR_FIELD_NAME)).stream().map(Double::floatValue).toList()),
                StringUtils.isBlank(text) ? null : TextSegment.from(text, metadata)
        );
    }
}
