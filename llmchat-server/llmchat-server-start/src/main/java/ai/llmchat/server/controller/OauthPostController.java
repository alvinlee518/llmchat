package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.OauthPost;
import ai.llmchat.server.service.OauthPostService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 岗位 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/post")
public class OauthPostController {

	private final OauthPostService oauthPostService;

	public OauthPostController(OauthPostService oauthPostService) {
		this.oauthPostService = oauthPostService;
	}

	@GetMapping("/select_options")
	public Result<List<SelectNode>> selectOptions() {
		List<SelectNode> selectNodes = oauthPostService.selectOptions();
		return Result.data(selectNodes);
	}

	@GetMapping("/list")
	public PageResult<OauthPost> queryPage(CommonPageParam param) {
		PageData<OauthPost> pageData = oauthPostService.queryPage(param);
		return PageResult.of(pageData);
	}

	@PostMapping("/create")
	public Result<?> create(@RequestBody @Validated OauthPost param) {
		oauthPostService.saveOrUpdate(param);
		return Result.data(param.getId());
	}

	@PutMapping("/modify")
	public Result<?> modify(@RequestBody @Validated OauthPost param) {
		oauthPostService.saveOrUpdate(param);
		return Result.data(param.getId());
	}

	@GetMapping("/{id}")
	public Result<OauthPost> detail(@PathVariable("id") Long id) {
		OauthPost dto = oauthPostService.getById(id);
		return Result.data(dto);
	}

	@DeleteMapping("/{ids}")
	public Result<?> delete(@PathVariable("ids") List<Long> ids) {
		oauthPostService.removeByIds(ids);
		return Result.success();
	}

}
