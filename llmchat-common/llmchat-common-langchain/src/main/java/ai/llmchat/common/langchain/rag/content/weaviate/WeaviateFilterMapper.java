package ai.llmchat.common.langchain.rag.content.weaviate;

import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.*;
import dev.langchain4j.store.embedding.filter.logical.And;
import dev.langchain4j.store.embedding.filter.logical.Not;
import dev.langchain4j.store.embedding.filter.logical.Or;
import io.weaviate.client.v1.filters.Operator;
import io.weaviate.client.v1.filters.WhereFilter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class WeaviateFilterMapper {

	public static WhereFilter map(Filter filter, String... metadataKeys) {
		if (filter instanceof IsEqualTo) {
			return mapEqual((IsEqualTo) filter, metadataKeys);
		}
		else if (filter instanceof IsNotEqualTo) {
			return mapNotEqual((IsNotEqualTo) filter, metadataKeys);
		}
		else if (filter instanceof IsGreaterThan) {
			return mapGreaterThan((IsGreaterThan) filter, metadataKeys);
		}
		else if (filter instanceof IsGreaterThanOrEqualTo) {
			return mapGreaterThanOrEqual((IsGreaterThanOrEqualTo) filter, metadataKeys);
		}
		else if (filter instanceof IsLessThan) {
			return mapLessThan((IsLessThan) filter, metadataKeys);
		}
		else if (filter instanceof IsLessThanOrEqualTo) {
			return mapLessThanOrEqual((IsLessThanOrEqualTo) filter, metadataKeys);
		}
		else if (filter instanceof IsIn) {
			return mapIn((IsIn) filter, metadataKeys);
		}
		else if (filter instanceof IsNotIn) {
			return mapNotIn((IsNotIn) filter, metadataKeys);
		}
		else if (filter instanceof And) {
			return mapAnd((And) filter, metadataKeys);
		}
		else if (filter instanceof Not) {
			return mapNot((Not) filter, metadataKeys);
		}
		else if (filter instanceof Or) {
			return mapOr((Or) filter, metadataKeys);
		}
		else {
			throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
		}
	}

	private static WhereFilter mapEqual(IsEqualTo isEqualTo, String... metadataKeys) {
		return buildWhereFilter(Operator.Equal, isEqualTo.key(), isEqualTo.comparisonValue(), metadataKeys);
	}

	private static WhereFilter mapNotEqual(IsNotEqualTo isNotEqualTo, String... metadataKeys) {
		return buildWhereFilter(Operator.NotEqual, isNotEqualTo.key(), isNotEqualTo.comparisonValue(), metadataKeys);
	}

	private static WhereFilter mapGreaterThan(IsGreaterThan isGreaterThan, String... metadataKeys) {
		return buildWhereFilter(Operator.GreaterThan, isGreaterThan.key(), isGreaterThan.comparisonValue(),
				metadataKeys);
	}

	private static WhereFilter mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo,
			String... metadataKeys) {
		return buildWhereFilter(Operator.GreaterThanEqual, isGreaterThanOrEqualTo.key(),
				isGreaterThanOrEqualTo.comparisonValue(), metadataKeys);
	}

	private static WhereFilter mapLessThan(IsLessThan isLessThan, String... metadataKeys) {
		return buildWhereFilter(Operator.LessThan, isLessThan.key(), isLessThan.comparisonValue(), metadataKeys);
	}

	private static WhereFilter mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo, String... metadataKeys) {
		return buildWhereFilter(Operator.LessThanEqual, isLessThanOrEqualTo.key(),
				isLessThanOrEqualTo.comparisonValue(), metadataKeys);
	}

	private static WhereFilter mapIn(IsIn isIn, String... metadataKeys) {
		return buildWhereFilter(Operator.ContainsAny, isIn.key(), isIn.comparisonValues(), metadataKeys);
	}

	private static WhereFilter mapNotIn(IsNotIn isNotIn, String... metadataKeys) {
		WhereFilter inFilter = buildWhereFilter(Operator.ContainsAny, isNotIn.key(),
				isNotIn.comparisonValues().toArray(), metadataKeys);
		return WhereFilter.builder().operator(Operator.Not).operands(inFilter).build();
	}

	private static WhereFilter mapAnd(And and, String... metadataKeys) {
		WhereFilter left = map(and.left(), metadataKeys);
		WhereFilter right = map(and.right(), metadataKeys);
		return WhereFilter.builder().operator(Operator.And).operands(left, right).build();
	}

	private static WhereFilter mapNot(Not not, String... metadataKeys) {
		WhereFilter map = map(not.expression(), metadataKeys);
		return WhereFilter.builder().operator(Operator.Not).operands(map).build();
	}

	private static WhereFilter mapOr(Or or, String... metadataKeys) {
		WhereFilter left = map(or.left(), metadataKeys);
		WhereFilter right = map(or.right(), metadataKeys);
		return WhereFilter.builder().operator(Operator.Or).operands(left, right).build();
	}

	private static WhereFilter buildWhereFilter(String operator, String key, Object value, String... metadataKeys) {
		String path = key;
		boolean isMetadataKey = Arrays.stream(metadataKeys).anyMatch(item -> StringUtils.equalsIgnoreCase(key, item));
		if (isMetadataKey) {
			path = WeaviateContentStore.METADATA_FIELD_PREFIX + key;
		}

		WhereFilter.WhereFilterBuilder builder = WhereFilter.builder().operator(operator).path(path);

		// 使用类型映射处理不同类型的 value
		if (value instanceof Number) {
			handleNumberValue(builder, value, isMetadataKey);
		}
		else if (value instanceof Date) {
			builder.valueDate((Date) value);
		}
		else if (value instanceof LocalDateTime) {
			builder.valueDate(Date.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant()));
		}
		else if (value instanceof Boolean) {
			builder.valueBoolean((Boolean) value);
		}
		else if (value instanceof String) {
			builder.valueText((String) value);
		}
		else if (value instanceof Collection<?>) {
			handleListValue(builder, ((Collection<?>) value).stream().toList(), isMetadataKey);
		}
		else {
			throw new RuntimeException("Unsupported value type: " + value);
		}
		return builder.build();
	}

	// 抽取出数值类型处理逻辑
	private static void handleNumberValue(WhereFilter.WhereFilterBuilder builder, Object value, boolean isMetadataKey) {
		if (isMetadataKey) {
			builder.valueText(value.toString());
		}
		else {
			builder.valueNumber(((Number) value).doubleValue());
		}
	}

	// 抽取出 List 类型的处理逻辑
	private static void handleListValue(WhereFilter.WhereFilterBuilder builder, List<?> list, boolean isMetadataKey) {
		Object firstValue = list.get(0);

		if (firstValue instanceof Number) {
			handleNumberList(builder, list, isMetadataKey);
		}
		else if (firstValue instanceof Date) {
			builder.valueDate(list.toArray(new Date[0]));
		}
		else if (firstValue instanceof LocalDateTime) {
			builder.valueDate(list.stream()
				.map(item -> Date.from(((LocalDateTime) item).atZone(ZoneId.systemDefault()).toInstant()))
				.toArray(Date[]::new));
		}
		else if (firstValue instanceof Boolean) {
			builder.valueBoolean(list.toArray(new Boolean[0]));
		}
		else if (firstValue instanceof String) {
			builder.valueText(list.toArray(new String[0]));
		}
		else {
			throw new RuntimeException("Unsupported list element type: " + firstValue.getClass().getName());
		}
	}

	// 处理 List 数值类型
	private static void handleNumberList(WhereFilter.WhereFilterBuilder builder, List<?> list, boolean isMetadataKey) {
		if (isMetadataKey) {
			builder.valueText(list.stream().map(Object::toString).toArray(String[]::new));
		}
		else {
			builder.valueNumber(list.stream().map(item -> ((Number) item).doubleValue()).toArray(Double[]::new));
		}
	}

}
