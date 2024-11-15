package ai.llmchat.common.mybatis.handler;

import ai.llmchat.common.security.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * MybatisPlus 自动填充配置
 *
 * @author lxw
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        String nickName = SecurityUtils.getNickName();
        fillValue("status", 1, metaObject, false);
        fillValue("createAt", now, metaObject, false);
        fillValue("updateAt", now, metaObject, false);
        fillValue("createBy", nickName, metaObject, false);
        fillValue("updateBy", nickName, metaObject, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fillValue("updateAt", LocalDateTime.now(), metaObject, true);
        fillValue("updateBy", SecurityUtils.getNickName(), metaObject, true);
    }

    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值
     *
     * @param fieldName
     * @param fieldVal
     * @param metaObject
     * @param isCover    是否覆盖原有值,避免更新操作手动入参
     */
    private void fillValue(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
        // 1. 没有 set 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        Object objectValue = metaObject.getValue(fieldName);
        if (!ObjectUtils.isEmpty(objectValue) && !isCover) {
            return;
        }
        // 3. columnName 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
            setFieldValByName(fieldName, fieldVal, metaObject);
        }
    }
}
