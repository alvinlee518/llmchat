package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectNode;
import ai.llmchat.server.api.param.*;
import ai.llmchat.server.repository.entity.OauthRole;
import ai.llmchat.server.repository.entity.OauthUser;
import ai.llmchat.server.service.OauthRoleService;
import ai.llmchat.server.service.OauthUserRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/role")
public class OauthRoleController {

	private final OauthRoleService oauthRoleService;

	private final OauthUserRoleService oauthUserRoleService;

	public OauthRoleController(OauthRoleService oauthRoleService, OauthUserRoleService oauthUserRoleService) {
		this.oauthRoleService = oauthRoleService;
		this.oauthUserRoleService = oauthUserRoleService;
	}

	@GetMapping("/list")
	public PageResult<OauthRole> queryPage(CommonPageParam param) {
		PageData<OauthRole> pageData = oauthRoleService.queryPage(param);
		return PageResult.of(pageData);
	}

	@PostMapping("/create")
	public Result<?> create(@RequestBody @Validated OauthRole param) {
		oauthRoleService.saveOrUpdate(param);
		return Result.data(param.getId());
	}

	@PutMapping("/modify")
	public Result<?> modify(@RequestBody @Validated OauthRole param) {
		oauthRoleService.saveOrUpdate(param);
		return Result.data(param.getId());
	}

	@GetMapping("/{id}")
	public Result<OauthRole> detail(@PathVariable("id") Long id) {
		OauthRole dto = oauthRoleService.getById(id);
		return Result.data(dto);
	}

	@DeleteMapping("/{ids}")
	public Result<?> delete(@PathVariable("ids") List<Long> ids) {
		oauthRoleService.removeByIds(ids);
		return Result.success();
	}

	@GetMapping("/data_scope/{id}")
	public Result<DataScopeParam> dataScope(@PathVariable("id") Long id) {
		DataScopeParam param = oauthRoleService.dataScopeById(id);
		return Result.data(param);
	}

	@PostMapping("/data_scope")
	public Result<?> dataScope(@RequestBody DataScopeParam param) {
		oauthRoleService.modifyDataScope(param);
		return Result.success();
	}

	@GetMapping("/menu_scope/{id}")
	public Result<MenuScopeParam> menuScope(@PathVariable("id") Long id) {
		MenuScopeParam param = oauthRoleService.menuScopeById(id);
		return Result.data(param);
	}

	@PostMapping("/menu_scope")
	public Result<?> menuScope(@RequestBody MenuScopeParam param) {
		oauthRoleService.modifyMenuScope(param);
		return Result.success();
	}

	@GetMapping("/user_scope")
	public PageResult<OauthUser> userRoleList(UserRolePageParam param) {
		PageData<OauthUser> pageData = oauthUserRoleService.userRoleList(param);
		return PageResult.of(pageData);
	}

	@PostMapping("/allocated")
	public Result<?> allocated(@RequestBody UserRoleParam param) {
		oauthUserRoleService.allocated(param);
		return Result.success();
	}

	@PostMapping("/unallocated")
	public Result<?> unallocated(@RequestBody UserRoleParam param) {
		oauthUserRoleService.unallocated(param);
		return Result.success();
	}

	@GetMapping("/select_options")
	public Result<List<SelectNode>> selectOptions() {
		List<SelectNode> selectNodes = oauthRoleService.selectOptions();
		return Result.data(selectNodes);
	}

}
