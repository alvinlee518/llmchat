package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.*;
import ai.llmchat.server.api.vo.AppDatasetVO;
import ai.llmchat.server.api.vo.AppVO;
import ai.llmchat.server.repository.entity.AiApp;
import ai.llmchat.server.service.AiAppDatasetService;
import ai.llmchat.server.service.AiAppService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 应用 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
@RestController
@RequestMapping("/app")
public class AiAppController {

	private final AiAppService aiAppService;

	private final AiAppDatasetService aiAppDatasetService;

	public AiAppController(AiAppService aiAppService, AiAppDatasetService aiAppDatasetService) {
		this.aiAppService = aiAppService;
		this.aiAppDatasetService = aiAppDatasetService;
	}

	@GetMapping("/list")
	public PageResult<AppVO> queryPage(CommonPageParam param) {
		PageData<AppVO> pageData = aiAppService.queryPage(param);
		return PageResult.of(pageData);
	}

	@PostMapping("/create")
	public Result<?> create(@RequestBody @Validated AppParam param) {
		Long rtn = aiAppService.saveOrUpdate(param);
		return Result.data(rtn);
	}

	@PutMapping("/modify")
	public Result<?> modify(@RequestBody @Validated AppParam param) {
		Long rtn = aiAppService.saveOrUpdate(param);
		return Result.data(rtn);
	}

	@GetMapping("/{id}")
	public Result<AppVO> detail(@PathVariable("id") Long id) {
		AppVO rtn = aiAppService.findById(id);
		return Result.data(rtn);
	}

	@DeleteMapping("/{ids}")
	public Result<?> delete(@PathVariable("ids") List<Long> ids) {
		aiAppService.removeByIds(ids);
		return Result.success();
	}

	@GetMapping("/dataset_list")
	public PageResult<AppDatasetVO> datasetList(AppDatasetParam param) {
		PageData<AppDatasetVO> pageData = aiAppDatasetService.queryPage(param);
		return PageResult.of(pageData);
	}

}
