package ai.llmchat.server.service.impl;

import ai.llmchat.server.repository.entity.FileDetail;
import ai.llmchat.server.repository.mapper.FileDetailMapper;
import ai.llmchat.server.service.FileDetailService;
import ai.llmchat.server.service.converter.FileDetailConverter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件记录表 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Service
public class FileDetailServiceImpl extends ServiceImpl<FileDetailMapper, FileDetail> implements FileDetailService, FileRecorder {
    private final FileDetailConverter fileDetailConverter;

    public FileDetailServiceImpl(FileDetailConverter fileDetailConverter) {
        this.fileDetailConverter = fileDetailConverter;
    }

    /**
     * 保存文件信息到数据库
     */
    @Override
    public boolean save(FileInfo info) {
        FileDetail fileDetail = fileDetailConverter.dto2do(info);
        boolean isSuc = save(fileDetail);
        if (isSuc) {
            info.setId(fileDetail.getId().toString());
        }
        return isSuc;
    }

    /**
     * 更新文件记录，可以根据文件 ID 或 URL 来更新文件记录，
     * 主要用在手动分片上传文件-完成上传，作用是更新文件信息
     */
    @Override
    public void update(FileInfo info) {
        FileDetail fileDetail = fileDetailConverter.dto2do(info);
        LambdaQueryWrapper<FileDetail> queryWrapper = Wrappers.<FileDetail>lambdaQuery().eq(StringUtils.isNotBlank(info.getUrl()), FileDetail::getUrl, info.getUrl());
        queryWrapper.eq(StringUtils.isNotBlank(info.getId()), FileDetail::getId, info.getId());
        update(fileDetail, queryWrapper);
    }

    /**
     * 根据 url 查询文件信息
     */
    @Override
    public FileInfo getByUrl(String url) {
        FileDetail fileDetail = getOne(Wrappers.<FileDetail>lambdaQuery().eq(FileDetail::getUrl, url));
        return fileDetailConverter.do2dto(fileDetail);
    }

    /**
     * 根据 url 删除文件信息
     */
    @Override
    public boolean delete(String url) {
        remove(Wrappers.<FileDetail>lambdaQuery().eq(FileDetail::getUrl, url));
        return true;
    }

    /**
     * 保存文件分片信息
     *
     * @param filePartInfo 文件分片信息
     */
    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {
    }

    /**
     * 删除文件分片信息
     */
    @Override
    public void deleteFilePartByUploadId(String uploadId) {
    }

    @Override
    public FileInfo findById(Long id) {
        FileDetail fileDetail = getById(id);
        return fileDetailConverter.do2dto(fileDetail);
    }
}
