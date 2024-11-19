package ai.llmchat.server.controller;

import ai.llmchat.common.core.util.ExcelUtil;
import ai.llmchat.common.core.wrapper.PageResult;
import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.DocumentPageParam;
import ai.llmchat.server.api.param.DocumentParam;
import ai.llmchat.server.api.param.FileParam;
import ai.llmchat.server.api.vo.DocumentItemVO;
import ai.llmchat.server.api.vo.ParagraphExportVO;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.service.AiDocumentService;
import ai.llmchat.server.service.AiParagraphService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.math.NumberUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/document")
public class AiDocumentController {
    private final AiDocumentService aiDocumentService;
    private final AiParagraphService aiParagraphService;
    private final FileStorageService fileStorageService;

    public AiDocumentController(AiDocumentService aiDocumentService, AiParagraphService aiParagraphService, FileStorageService fileStorageService) {
        this.aiDocumentService = aiDocumentService;
        this.aiParagraphService = aiParagraphService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/list")
    public PageResult<DocumentItemVO> queryPage(DocumentPageParam param) {
        PageData<DocumentItemVO> pageData = aiDocumentService.queryPage(param);
        return PageResult.of(pageData);
    }

    @PostMapping("/create")
    public Result<?> create(DocumentParam param, @RequestParam("files") List<MultipartFile> fileList) {
        List<FileParam> fileParamList = fileList.stream().map(item -> {
            FileInfo fileInfo = fileStorageService.of(item).upload();
            long aLong = NumberUtils.toLong(fileInfo.getId(), 0);
            return new FileParam(aLong, fileInfo.getOriginalFilename());
        }).toList();
        aiDocumentService.saveOrUpdate(param, fileParamList);
        return Result.success();
    }

    @PostMapping("/modify")
    public Result<?> modify(@RequestBody DocumentParam param) {
        aiDocumentService.saveOrUpdate(param);
        return Result.success();
    }

    @PutMapping("/reindex/{id}")
    public Result<?> reindex(@PathVariable("id") Long id) {
        aiParagraphService.reindexByDocId(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<AiDocument> detail(@PathVariable("id") Long id) {
        AiDocument dto = aiDocumentService.getById(id);
        return Result.data(dto);
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") List<Long> ids) {
        aiDocumentService.removeByIds(ids);
        return Result.success();
    }

    @SneakyThrows
    @GetMapping("/export/{id}")
    public void exportAll(HttpServletResponse response, @PathVariable("id") Long id) {
        List<ParagraphExportVO> paragraphExportVOS = aiParagraphService.exportListByDocId(id);
        ExcelUtil.export2Web(response,
                String.valueOf(System.currentTimeMillis()),
                "sheet1",
                ParagraphExportVO.class,
                paragraphExportVOS
        );
    }
}
