package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.*;
import ai.llmchat.server.api.vo.DatasetVO;
import ai.llmchat.server.api.vo.HitTestingVO;
import ai.llmchat.server.repository.entity.AiDataset;
import ai.llmchat.server.service.AiDatasetService;
import ai.llmchat.server.service.AiDocumentService;
import org.apache.commons.lang3.math.NumberUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 数据集 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@RestController
@RequestMapping("/dataset")
public class AiDatasetController {
    private final AiDatasetService aiDatasetService;
    private final FileStorageService fileStorageService;
    private final AiDocumentService aiDocumentService;

    public AiDatasetController(AiDatasetService aiDatasetService, FileStorageService fileStorageService, AiDocumentService aiDocumentService) {
        this.aiDatasetService = aiDatasetService;
        this.fileStorageService = fileStorageService;
        this.aiDocumentService = aiDocumentService;
    }

    @GetMapping("/list")
    public PageResult<DatasetVO> queryPage(CommonPageParam param) {
        PageData<DatasetVO> pageData = aiDatasetService.queryPage(param);
        return PageResult.of(pageData);
    }

    @PostMapping("/create")
    public Result<?> create(DatasetParam datasetParam, DocumentParam documentParam, @RequestParam("files") List<MultipartFile> fileList) {
        List<FileParam> fileParamList = fileList.stream().map(item -> {
            FileInfo fileInfo = fileStorageService.of(item).upload();
            long aLong = NumberUtils.toLong(fileInfo.getId(), 0);
            return new FileParam(aLong, fileInfo.getOriginalFilename());
        }).toList();
        Long datasetId = aiDatasetService.saveOrUpdate(datasetParam);
        documentParam.setDatasetId(datasetId);
        aiDocumentService.saveOrUpdate(documentParam, fileParamList);
        return Result.data(datasetId);
    }

    @PutMapping("/modify")
    public Result<?> modify(@RequestBody @Validated DatasetParam param) {
        aiDatasetService.saveOrUpdate(param);
        return Result.data(param.getId());
    }

    @GetMapping("/{id}")
    public Result<AiDataset> detail(@PathVariable("id") Long id) {
        AiDataset dto = aiDatasetService.getById(id);
        return Result.data(dto);
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") List<Long> ids) {
        aiDatasetService.removeByIds(ids);
        return Result.success();
    }

    @PostMapping("/hit-testing")
    public Result<List<HitTestingVO>> hitTesting(@RequestBody HitTestingParam param) {
        List<HitTestingVO> list = aiDatasetService.hitTesting(param);
        return Result.data(list);
    }
}
