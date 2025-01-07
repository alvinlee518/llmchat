package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.OauthUserParam;
import ai.llmchat.server.api.param.OauthUserPageParam;
import ai.llmchat.server.api.vo.OauthUserVO;
import ai.llmchat.server.service.OauthUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class OauthUserController {

	private final OauthUserService oauthUserService;

	public OauthUserController(OauthUserService oauthUserService) {
		this.oauthUserService = oauthUserService;
	}

	@GetMapping("/list")
	public PageResult<OauthUserVO> queryPage(OauthUserPageParam param) {
		PageData<OauthUserVO> pageData = oauthUserService.queryPage(param);
		return PageResult.of(pageData);
	}

	@PostMapping("/create")
	public Result<?> create(@RequestBody @Validated OauthUserParam param) {
		Long id = oauthUserService.saveOrUpdate(param);
		return Result.data(id);
	}

	@PutMapping("/modify")
	public Result<?> modify(@RequestBody @Validated OauthUserParam param) {
		Long id = oauthUserService.saveOrUpdate(param);
		return Result.data(id);
	}

	@GetMapping("/{id}")
	public Result<OauthUserVO> detail(@PathVariable("id") Long id) {
		OauthUserVO data = oauthUserService.selectById(id);
		return Result.data(data);
	}

	@DeleteMapping("/{ids}")
	public Result<?> delete(@PathVariable("ids") List<Long> ids) {
		oauthUserService.removeByIds(ids);
		return Result.success();
	}

}
