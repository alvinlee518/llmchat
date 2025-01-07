package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.exception.ServiceException;
import ai.llmchat.common.langchain.enums.SearchModeEnum;
import ai.llmchat.common.langchain.model.options.EmbeddingModelOptions;
import ai.llmchat.common.langchain.model.options.LanguageModelOptions;
import ai.llmchat.common.langchain.model.options.ScoringModelOptions;
import ai.llmchat.common.langchain.rag.AIAssistantService;
import ai.llmchat.common.langchain.rag.StreamingChatService;
import ai.llmchat.common.langchain.rag.options.*;
import ai.llmchat.common.langchain.rag.output.Citation;
import ai.llmchat.common.langchain.rag.output.Message;
import ai.llmchat.common.langchain.rag.output.Prompt;
import ai.llmchat.common.langchain.rag.output.Segment;
import ai.llmchat.common.langchain.util.LangchainConstants;
import ai.llmchat.common.security.SecurityUtils;
import ai.llmchat.server.api.param.*;
import ai.llmchat.server.api.vo.MessageVO;
import ai.llmchat.server.api.vo.PromptVO;
import ai.llmchat.server.api.vo.UsageVO;
import ai.llmchat.server.repository.dataobject.ModelOptionsDO;
import ai.llmchat.server.repository.dataobject.ParagraphDO;
import ai.llmchat.server.repository.entity.*;
import ai.llmchat.server.repository.mapper.AiChatMapper;
import ai.llmchat.server.service.*;
import ai.llmchat.server.service.converter.AiChatConverter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.langchain4j.data.message.ChatMessageType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 对话 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-11-07
 */
@Service
public class AiChatServiceImpl extends ServiceImpl<AiChatMapper, AiChat> implements AiChatService {

	private final AiAppService aiAppService;

	private final AiModelService aiModelService;

	private final AiDatasetService aiDatasetService;

	private final AiParagraphService aiParagraphService;

	private final StreamingChatService streamingChatService;

	private final AiChatMessageService aiChatMessageService;

	private final AiChatConverter aiChatConverter;

	private final AIAssistantService aiAssistantService;

	public AiChatServiceImpl(AiAppService aiAppService, AiModelService aiModelService,
			AiDatasetService aiDatasetService, AiParagraphService aiParagraphService,
			StreamingChatService streamingChatService, AiChatMessageService aiChatMessageService,
			AiChatConverter aiChatConverter, AIAssistantService aiAssistantService) {
		this.aiAppService = aiAppService;
		this.aiModelService = aiModelService;
		this.aiDatasetService = aiDatasetService;
		this.aiParagraphService = aiParagraphService;
		this.streamingChatService = streamingChatService;
		this.aiChatMessageService = aiChatMessageService;
		this.aiChatConverter = aiChatConverter;
		this.aiAssistantService = aiAssistantService;
	}

	@Override
	public Flux<Message> debugChat(ChatTestingParam param) {
		param.setChatId(validationChatId(param.getChatId(), param.getAppId(), param.getModelId(),
				param.getInstruction(), true));
		List<Long> modelIdList = new ArrayList<>();
		List<AiDataset> datasetList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(param.getDatasets())) {
			datasetList = aiDatasetService
				.list(Wrappers.<AiDataset>lambdaQuery().in(AiDataset::getId, param.getDatasets()));
			modelIdList.addAll(datasetList.stream().map(AiDataset::getEmbedId).toList());
		}
		modelIdList.add(param.getModelId());
		modelIdList.add(param.getRerankId());

		List<AiModel> modelList = aiModelService.list(Wrappers.<AiModel>lambdaQuery().in(AiModel::getId, modelIdList));
		LanguageModelOptions modelOptions = getChatModelOptions(modelList, param.getModelId(), param.getTemperature(),
				param.getMaxTokens());
		AiChatMessage chatMessage = new AiChatMessage();
		chatMessage.setChatId(param.getChatId());
		chatMessage.setUserId(SecurityUtils.getId());
		chatMessage.setInstruction(param.getInstruction());
		aiChatMessageService.save(chatMessage);

		ChatOptions chatOptions = new ChatOptions();
		chatOptions.setId(chatMessage.getId());
		chatOptions.setChatId(param.getChatId());
		chatOptions.setSystemPrompt(param.getPrompt());
		chatOptions.setInstruction(param.getInstruction());
		chatOptions.setModelOptions(modelOptions);
		chatOptions.setMaxMemory(param.getMaxMemory());
		chatOptions.setCitationProvider(getCitationProvider(param.getSourceEnabled()));
		chatOptions.setDoOnComplete(doOnCompleteConsumer(chatMessage.getId()));
		chatOptions.setRetrieval(buildRetrievalOptions(datasetList, modelList, param.getRerankId(),
				param.getRewriteEnabled(), param.getScore(), param.getTopK()));
		return streamingChatService.streamingChat(chatOptions);
	}

	@Override
	public Flux<Message> streamingChat(ChatStramingParam param) {
		AiApp app = aiAppService.getById(param.getAppId());
		if (Optional.ofNullable(app).map(AiApp::getId).orElse(0L) <= 0) {
			throw new ServiceException("app not exists");
		}
		param.setChatId(
				validationChatId(param.getChatId(), param.getAppId(), app.getModelId(), param.getInstruction(), false));
		List<AiDataset> datasetList = aiDatasetService.listByAppId(app.getId());
		List<Long> modelIdList = new ArrayList<>(datasetList.stream().map(AiDataset::getEmbedId).toList());
		modelIdList.add(app.getModelId());
		modelIdList.add(app.getRerankId());

		List<AiModel> modelList = aiModelService.list(Wrappers.<AiModel>lambdaQuery().in(AiModel::getId, modelIdList));
		LanguageModelOptions modelOptions = getChatModelOptions(modelList, app.getModelId(),
				app.getTemperature().doubleValue(), app.getMaxTokens());

		AiChatMessage chatMessage = new AiChatMessage();
		chatMessage.setChatId(param.getChatId());
		chatMessage.setUserId(SecurityUtils.getId());
		chatMessage.setInstruction(param.getInstruction());
		aiChatMessageService.save(chatMessage);

		ChatOptions chatOptions = new ChatOptions();
		chatOptions.setId(chatMessage.getId());
		chatOptions.setChatId(param.getChatId());
		chatOptions.setSystemPrompt(app.getPrompt());
		chatOptions.setInstruction(param.getInstruction());
		chatOptions.setModelOptions(modelOptions);
		chatOptions.setMaxMemory(app.getMaxMemory());
		chatOptions.setCitationProvider(getCitationProvider(app.getSourceEnabled()));
		chatOptions.setDoOnComplete(doOnCompleteConsumer(chatMessage.getId()));
		chatOptions.setRetrieval(buildRetrievalOptions(datasetList, modelList, app.getRerankId(),
				app.getRewriteEnabled(), app.getScore().doubleValue(), app.getTopK()));
		return streamingChatService.streamingChat(chatOptions);
	}

	@Override
	public List<MessageVO> chatMessageById(Long chatId) {
		List<AiChatMessage> list = aiChatMessageService.lambdaQuery()
			.eq(AiChatMessage::getChatId, chatId)
			.eq(AiChatMessage::getUserId, SecurityUtils.getId())
			.orderByAsc(AiChatMessage::getCreateAt)
			.list();
		List<MessageVO> result = new ArrayList<>();
		for (AiChatMessage message : list) {
			MessageVO userMessage = new MessageVO();
			userMessage.setId(message.getId());
			userMessage.setChatId(chatId);
			userMessage.setRole(ChatMessageType.USER.name());
			userMessage.setContent(message.getInstruction());
			userMessage.setCreateAt(message.getCreateAt());
			result.add(userMessage);
			UsageVO usage = new UsageVO();
			usage.setPromptTokens(message.getPromptTokens());
			usage.setCompletionTokens(message.getCompletionTokens());
			usage.setTotalTokens(message.getPromptTokens() + message.getCompletionTokens());
			usage.setDuration(message.getDuration());
			MessageVO aiMessage = new MessageVO();
			aiMessage.setId(message.getId());
			aiMessage.setChatId(chatId);
			aiMessage.setRole(ChatMessageType.AI.name());
			aiMessage.setContent(message.getCompletion());
			aiMessage.setError(Objects.equals(1, message.getFailed()));
			aiMessage.setUsage(usage);
			aiMessage.setCitations(aiChatConverter.citationDo2vo(message.getCitations()));
			aiMessage.setRating(message.getRating());
			aiMessage.setCreateAt(message.getCreateAt());
			result.add(aiMessage);
		}
		return result;
	}

	@Override
	public AppChatVO chatListByAppId(Long appId) {
		AiApp app = aiAppService.getById(appId);
		if (Optional.ofNullable(app).map(AiApp::getId).orElse(0L) <= 0) {
			throw new ServiceException("应用不存在");
		}
		if (app.getModelId() <= 0) {
			throw new ServiceException("请完成应用编排配置后重试");
		}
		LambdaQueryWrapper<AiChat> queryWrapper = Wrappers.<AiChat>lambdaQuery()
			.eq(AiChat::getAppId, app.getId())
			.eq(AiChat::getUserId, SecurityUtils.getId())
			.eq(AiChat::getTesting, BooleanEnum.NO.getCode())
			.eq(AiChat::getStatus, BooleanEnum.YES.getCode())
			.orderByDesc(AiChat::getUpdateAt);
		List<AiChat> list = super.list(queryWrapper);
		AppChatVO appChatVO = new AppChatVO();
		appChatVO.setId(app.getId());
		appChatVO.setName(app.getName());
		appChatVO.setDescription(app.getDescription());
		appChatVO.setPrologue(app.getPrologue());
		appChatVO.setSuggestEnabled(app.getSuggestEnabled());
		appChatVO.setChatList(aiChatConverter.dto2vo(list));
		return appChatVO;
	}

	@Override
	public PromptVO optimize(OptimizeParam param) {
		AiModel model = aiModelService.getById(param.getModelId());
		PromptOptions options = new PromptOptions();
		options.setInstruction(param.getInstruction());
		options.setModelName(model.getModelName());
		options.setModelProvider(model.getModelProvider());
		options.setModelType(model.getModelType());
		options.setBaseUrl(model.getBaseUrl());
		options.setApiKey(model.getApiKey());
		options.setSecretKey(model.getSecretKey());
		Prompt prompt = aiAssistantService.optimizePrompt(options);
		PromptVO result = new PromptVO();
		result.setTaskObjective(prompt.getTaskObjective());
		result.setOutputFormat(prompt.getOutputFormat());
		result.setKeyPoints(prompt.getKeyPoints());
		result.setPrologue(prompt.getPrologue());
		return result;
	}

	@Override
	public List<String> suggested(Long chatId) {
		ModelOptionsDO model = aiModelService.findByChatId(chatId);
		SuggestedOptions options = new SuggestedOptions();
		options.setChatId(chatId);
		options.setModelName(model.getModelName());
		options.setModelProvider(model.getModelProvider());
		options.setModelType(model.getModelType());
		options.setBaseUrl(model.getBaseUrl());
		options.setApiKey(model.getApiKey());
		options.setSecretKey(model.getSecretKey());
		return aiAssistantService.suggestedQuestions(options);
	}

	@Override
	public Long saveOrUpdate(ChatParam param) {
		AiChat chat = aiChatConverter.param2dto(param);
		chat.setUserId(SecurityUtils.getId());
		chat.setStatus(BooleanEnum.YES.getCode());
		baseMapper.insertOrUpdate(chat);
		return chat.getId();
	}

	@Override
	public void rating(RatingParam param) {
		LambdaUpdateWrapper<AiChatMessage> updateWrapper = Wrappers.<AiChatMessage>lambdaUpdate()
			.eq(AiChatMessage::getId, param.getId())
			.set(AiChatMessage::getRating, param.getAction());
		aiChatMessageService.update(updateWrapper);
	}

	private Long validationChatId(Long chatId, Long appId, Long modelId, String instruction, boolean testing) {
		AiChat chat = baseMapper.selectById(chatId);
		if (Optional.ofNullable(chat).map(AiChat::getId).orElse(0L) <= 0) {
			chat = new AiChat();
			chat.setAppId(appId);
			chat.setModelId(modelId);
			chat.setUserId(SecurityUtils.getId());
			chat.setTesting(testing ? BooleanEnum.YES.getCode() : BooleanEnum.NO.getCode());
			chat.setTitle(StringUtils.substring(instruction, 0, 128));
			baseMapper.insert(chat);
		}
		else {
			if (StringUtils.isBlank(chat.getTitle())) {
				chat.setTitle(StringUtils.substring(instruction, 0, 128));
			}
			chat.setAppId(appId);
			chat.setModelId(modelId);
			chat.setUserId(SecurityUtils.getId());
			chat.setTesting(testing ? BooleanEnum.YES.getCode() : BooleanEnum.NO.getCode());
			chat.setStatus(BooleanEnum.YES.getCode());
			chat.setUpdateAt(LocalDateTime.now());
			baseMapper.updateById(chat);
		}
		return chat.getId();
	}

	private Function<List<Long>, List<Citation>> getCitationProvider(Integer sourceEnabled) {
		return idList -> {
			List<Citation> list = new ArrayList<>();
			if (Objects.equals(1, sourceEnabled)) {
				List<ParagraphDO> segmentList = aiParagraphService.queryParagraphByIds(idList);
				if (CollectionUtils.isNotEmpty(segmentList)) {
					segmentList.stream()
						.collect(Collectors.groupingBy(ParagraphDO::getDocId))
						.forEach((aLong, subList) -> {
							ParagraphDO segmentDO = subList.get(0);
							Citation citation = new Citation();
							citation.setId(segmentDO.getDocId());
							citation.setName(segmentDO.getDocName());
							citation.setUrl(segmentDO.getDocUrl());
							citation.setSegments(subList.stream().map(item -> {
								List<String> contents = new ArrayList<>();
								if ((item.getReplyCols() & 1) == 1) {
									contents.add(item.getTitle());
								}
								else if ((item.getReplyCols() & 2) == 2) {
									contents.add(item.getContent());
								}
								Segment segment = new Segment();
								segment.setId(item.getId());
								segment.setIndex(item.getPosition());
								segment.setText(StringUtils.join(contents, System.lineSeparator()));
								return segment;
							}).toList());
							list.add(citation);
						});
				}
			}
			return list;
		};
	}

	private Consumer<Message> doOnCompleteConsumer(Long id) {
		return message -> {
			AiChatMessage chatMessage = new AiChatMessage();
			chatMessage.setId(id);
			chatMessage.setPromptTokens(message.getUsage().getPromptTokens());
			chatMessage.setCompletion(message.getContent());
			chatMessage.setCompletionTokens(message.getUsage().getCompletionTokens());
			chatMessage.setFailed(Optional.ofNullable(message.getError()).orElse(false) ? 1 : 0);
			chatMessage.setDuration(message.getUsage().getDuration());
			chatMessage.setCitations(aiChatConverter.citationDto2do(message.getCitations()));
			aiChatMessageService.updateById(chatMessage);
			List<Long> list = message.getCitations()
				.stream()
				.flatMap(item -> item.getSegments().stream().map(Segment::getId))
				.toList();
			aiParagraphService.incrementHitCount(list);
		};
	}

	private RetrievalOptions buildRetrievalOptions(List<AiDataset> datasetList, List<AiModel> modelList, Long rerankId,
			Integer rewriteEnabled, Double score, Integer topK) {
		if (CollectionUtils.isEmpty(datasetList)) {
			return null;
		}
		List<ContentOptions> contentList = new ArrayList<>();
		for (AiDataset aiDataset : datasetList) {
			ContentOptions contentOptions = new ContentOptions();
			contentOptions.setId(aiDataset.getId());
			contentOptions.setName(aiDataset.getName());
			contentOptions.setDescription(aiDataset.getDescription());
			contentOptions.setSearchMode(aiDataset.getSearchMode());
			contentOptions.setTopK(aiDataset.getTopK());
			contentOptions.setScore(aiDataset.getScore().doubleValue());
			if (!Objects.equals(SearchModeEnum.KEYWORD.getCode(), aiDataset.getSearchMode())) {
				contentOptions.setModelOptions(getEmbeddingModelOptions(aiDataset, modelList));
			}
			contentList.add(contentOptions);
		}
		ScoringModelOptions modelOptions = getScoringModelOptions(rerankId, modelList);
		RetrievalOptions retrievalOptions = new RetrievalOptions();
		retrievalOptions.setRewriteEnabled(Objects.equals(1, rewriteEnabled));
		retrievalOptions.setScore(score);
		retrievalOptions.setTopK(topK);
		retrievalOptions.setModelOptions(modelOptions);
		retrievalOptions.setContents(contentList);
		return retrievalOptions;
	}

	private LanguageModelOptions getChatModelOptions(List<AiModel> modelList, Long modelId, Double temperature,
			Integer maxTokens) {
		AiModel chatModel = modelList.stream()
			.filter(item -> Objects.equals(modelId, item.getId()))
			.findFirst()
			.orElse(null);
		if (Objects.isNull(chatModel)) {
			throw new ServiceException("chat model not exists:" + modelId);
		}
		LanguageModelOptions modelOptions = new LanguageModelOptions();
		modelOptions.setTemperature(temperature);
		modelOptions.setMaxTokens(maxTokens);
		modelOptions.setModelProvider(chatModel.getModelProvider());
		modelOptions.setModelName(chatModel.getModelName());
		modelOptions.setModelType(chatModel.getModelType());
		modelOptions.setBaseUrl(chatModel.getBaseUrl());
		modelOptions.setApiKey(chatModel.getApiKey());
		modelOptions.setSecretKey(chatModel.getSecretKey());
		return modelOptions;
	}

	private ScoringModelOptions getScoringModelOptions(Long rerankId, List<AiModel> modelList) {
		AiModel model = modelList.stream()
			.filter(item -> Objects.equals(rerankId, item.getId()))
			.findFirst()
			.orElse(null);
		if (Objects.isNull(model)) {
			return null;
		}
		ScoringModelOptions modelOptions = new ScoringModelOptions();
		modelOptions.setModelProvider(model.getModelProvider());
		modelOptions.setModelName(model.getModelName());
		modelOptions.setModelType(model.getModelType());
		modelOptions.setBaseUrl(model.getBaseUrl());
		modelOptions.setApiKey(model.getApiKey());
		modelOptions.setSecretKey(model.getSecretKey());
		return modelOptions;
	}

	private EmbeddingModelOptions getEmbeddingModelOptions(AiDataset dataset, List<AiModel> modelList) {
		AiModel model = modelList.stream()
			.filter(item -> Objects.equals(dataset.getEmbedId(), item.getId()))
			.findFirst()
			.orElse(null);
		if (Objects.isNull(model)) {
			throw new ServiceException("embedding model not exists:" + dataset.getEmbedId());
		}
		EmbeddingModelOptions modelOptions = new EmbeddingModelOptions();
		modelOptions.setDimensions(LangchainConstants.DIMENSIONS);
		modelOptions.setModelProvider(model.getModelProvider());
		modelOptions.setModelName(model.getModelName());
		modelOptions.setModelType(model.getModelType());
		modelOptions.setBaseUrl(model.getBaseUrl());
		modelOptions.setApiKey(model.getApiKey());
		modelOptions.setSecretKey(model.getSecretKey());
		return modelOptions;
	}

}
