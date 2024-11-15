package ai.llmchat.server.service;

import ai.llmchat.server.repository.entity.FileDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.x.file.storage.core.FileInfo;

/**
 * <p>
 * 文件记录表 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
public interface FileDetailService extends IService<FileDetail> {
    FileInfo findById(Long id);
}
