package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.TreeNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.OauthDept;
import ai.llmchat.server.service.OauthDeptService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/dept")
public class OauthDeptController {
    private final OauthDeptService oauthDeptService;

    public OauthDeptController(OauthDeptService oauthDeptService) {
        this.oauthDeptService = oauthDeptService;
    }

    @GetMapping("/tree/{pid}")
    public Result<List<TreeNode>> treeData(@PathVariable("pid") Long pid) {
        List<TreeNode> list = oauthDeptService.treeData(pid);
        return Result.data(list);
    }

    @GetMapping("/list")
    public PageResult<OauthDept> queryPage(CommonPageParam param) {
        PageData<OauthDept> pageData = oauthDeptService.queryPage(param);
        return PageResult.of(pageData);
    }

    @PostMapping("/create")
    public Result<?> create(@RequestBody @Validated OauthDept param) {
        oauthDeptService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @PutMapping("/modify")
    public Result<?> modify(@RequestBody @Validated OauthDept param) {
        oauthDeptService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @GetMapping("/{id}")
    public Result<OauthDept> detail(@PathVariable("id") Long id) {
        OauthDept dto = oauthDeptService.getById(id);
        return Result.data(dto);
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") List<Long> ids) {
        oauthDeptService.removeByIds(ids);
        return Result.success();
    }
}
