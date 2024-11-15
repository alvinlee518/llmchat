package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.exception.DataExistsException;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.DictData;
import ai.llmchat.server.repository.mapper.DictDataMapper;
import ai.llmchat.server.service.DictDataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 字典 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Override
    public PageData<DictData> queryPage(CommonPageParam param) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, DictData::getStatus, param.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(param.getName()), DictData::getName, param.getName());
        queryWrapper.eq(Optional.ofNullable(param.getParentId()).orElse(-1L) >= 0, DictData::getTypeId, param.getParentId());
        queryWrapper.orderByDesc(DictData::getUpdateAt);
        PageInfo<DictData> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
                .doSelectPageInfo(() -> this.list(queryWrapper));
        List<DictData> list = pageInfo.getList();
        return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), list);
    }

    @Override
    public boolean saveOrUpdate(DictData param) {
        LambdaQueryWrapper<DictData> queryWrapper = Wrappers.<DictData>lambdaQuery()
                .eq(DictData::getTypeId, param.getTypeId())
                .eq(DictData::getCode, param.getCode())
                .ne(DictData::getId, Optional.ofNullable(param.getId()).orElse(0L));
        if (exists(queryWrapper)) {
            throw new DataExistsException("字典编码已存在,请修改后重试!");
        }
        return super.saveOrUpdate(param);
    }
}
