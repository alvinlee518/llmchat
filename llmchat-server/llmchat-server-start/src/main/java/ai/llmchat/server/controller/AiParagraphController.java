package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.EnabledParam;
import ai.llmchat.server.api.param.ParagraphPageParam;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.service.AiParagraphService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据文档 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@RestController
@RequestMapping("/paragraph")
public class AiParagraphController {
    private final AiParagraphService aiParagraphService;

    public AiParagraphController(AiParagraphService aiParagraphService) {
        this.aiParagraphService = aiParagraphService;
    }

    @GetMapping("/list")
    public PageResult<AiParagraph> queryPage(ParagraphPageParam param) {
        PageData<AiParagraph> pageData = aiParagraphService.queryPage(param);
        return PageResult.of(pageData);
    }

    @PostMapping("/create")
    public Result<?> create(@RequestBody @Validated AiParagraph param) {
        aiParagraphService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @PutMapping("/modify")
    public Result<?> modify(@RequestBody @Validated AiParagraph param) {
        aiParagraphService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @GetMapping("/{id}")
    public Result<AiParagraph> detail(@PathVariable("id") Long id) {
        AiParagraph dto = aiParagraphService.getById(id);
        return Result.data(dto);
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") List<Long> ids) {
        aiParagraphService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping("/enabled")
    public Result<?> enabled(@RequestBody EnabledParam param) {
        aiParagraphService.enabled(param);
        return Result.success();
    }
}
