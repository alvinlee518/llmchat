package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectGroupNode;
import ai.llmchat.server.api.param.ModelPageParam;
import ai.llmchat.server.api.vo.ModelProviderVO;
import ai.llmchat.server.api.vo.ModelVO;
import ai.llmchat.server.repository.entity.AiModel;
import ai.llmchat.server.service.AiModelService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 模型配置 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-24
 */
@RestController
@RequestMapping("/model")
public class AiModelController {
    private final AiModelService aiModelService;

    public AiModelController(AiModelService aiModelService) {
        this.aiModelService = aiModelService;
    }


    @GetMapping("/list")
    public PageResult<ModelVO> queryPage(ModelPageParam param) {
        PageData<ModelVO> pageData = aiModelService.queryPage(param);
        return PageResult.of(pageData);
    }

    @PostMapping("/create")
    public Result<?> create(@RequestBody @Validated AiModel param) {
        aiModelService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @PutMapping("/modify")
    public Result<?> modify(@RequestBody @Validated AiModel param) {
        aiModelService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @GetMapping("/{id}")
    public Result<AiModel> detail(@PathVariable("id") Long id) {
        AiModel dto = aiModelService.getById(id);
        return Result.data(dto);
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") List<Long> ids) {
        aiModelService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/select_group/{modelType}")
    public Result<List<SelectGroupNode>> selectGroup(@PathVariable("modelType") Integer modelType) {
        List<SelectGroupNode> list = aiModelService.selectGroupOptions(modelType);
        return Result.data(list);
    }

    @GetMapping("/providers")
    public Result<List<ModelProviderVO>> modelProviders() {
        List<ModelProviderVO> providerList = aiModelService.modelProviderList();
        return Result.data(providerList);
    }
}
