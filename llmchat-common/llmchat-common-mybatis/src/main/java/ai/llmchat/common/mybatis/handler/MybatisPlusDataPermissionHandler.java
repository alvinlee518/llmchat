package ai.llmchat.common.mybatis.handler;

import ai.llmchat.common.mybatis.annotation.DataScope;
import ai.llmchat.common.mybatis.util.DataScopeUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

import java.util.Objects;

public class MybatisPlusDataPermissionHandler implements MultiDataPermissionHandler {
    @Override
    public Expression getSqlSegment(Table table, Expression where, String statementId) {
        DataScope dataScope = DataScopeUtils.getDataScope(statementId);
        if (Objects.isNull(dataScope)) {
            return null;
        }
        return null;
    }
}
