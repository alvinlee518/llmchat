package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.*;
import ai.llmchat.server.repository.entity.DictData;
import ai.llmchat.server.service.DictDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 字典 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/dict")
public class DictDataController {
    private final DictDataService dictDataService;

    public DictDataController(DictDataService dictDataService) {
        this.dictDataService = dictDataService;
    }

    @GetMapping("/list")
    public PageResult<DictData> queryPage(CommonPageParam param) {
        PageData<DictData> pageData = dictDataService.queryPage(param);
        return PageResult.of(pageData);
    }

    @PostMapping("/create")
    public Result<?> create(@RequestBody @Validated DictData param) {
        dictDataService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @PutMapping("/modify")
    public Result<?> modify(@RequestBody @Validated DictData param) {
        dictDataService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @GetMapping("/{id}")
    public Result<DictData> detail(@PathVariable("id") Long id) {
        DictData dto = dictDataService.getById(id);
        return Result.data(dto);
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") List<Long> ids) {
        dictDataService.removeByIds(ids);
        return Result.success();
    }
}
