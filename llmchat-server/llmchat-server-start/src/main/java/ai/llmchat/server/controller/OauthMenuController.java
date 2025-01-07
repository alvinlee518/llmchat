package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.TreeNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.OauthMenu;
import ai.llmchat.server.service.OauthMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/menu")
public class OauthMenuController {

	private final OauthMenuService oauthMenuService;

	public OauthMenuController(OauthMenuService oauthMenuService) {
		this.oauthMenuService = oauthMenuService;
	}

	@GetMapping("/tree/{pid}")
	public Result<List<TreeNode>> treeData(@PathVariable("pid") Long pid) {
		List<TreeNode> list = oauthMenuService.treeData(pid);
		return Result.data(list);
	}

	@GetMapping("/list")
	public PageResult<OauthMenu> queryPage(CommonPageParam param) {
		PageData<OauthMenu> pageData = oauthMenuService.queryPage(param);
		return PageResult.of(pageData);
	}

	@PostMapping("/create")
	public Result<?> create(@RequestBody @Validated OauthMenu param) {
		oauthMenuService.saveOrUpdate(param);
		return Result.data(param.getId());
	}

	@PutMapping("/modify")
	public Result<?> modify(@RequestBody @Validated OauthMenu param) {
		oauthMenuService.saveOrUpdate(param);
		return Result.data(param.getId());
	}

	@GetMapping("/{id}")
	public Result<OauthMenu> detail(@PathVariable("id") Long id) {
		OauthMenu dto = oauthMenuService.getById(id);
		return Result.data(dto);
	}

	@DeleteMapping("/{ids}")
	public Result<?> delete(@PathVariable("ids") List<Long> ids) {
		oauthMenuService.removeByIds(ids);
		return Result.success();
	}

}
