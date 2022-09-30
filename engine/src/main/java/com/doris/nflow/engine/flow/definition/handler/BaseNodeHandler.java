package com.doris.nflow.engine.flow.definition.handler;

import com.alibaba.fastjson2.JSON;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.handler.JsonToObjectHandler;
import com.doris.nflow.engine.common.model.node.BaseNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: origindoris
 * @Title: BaseNodeHandler
 * @Description:
 * @date: 2022/9/30 08:34
 */
@MappedTypes(value = BaseNode.class)
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class BaseNodeHandler extends JsonToObjectHandler<BaseNode> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseNode parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public BaseNode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String fieldType = rs.getString(BaseNode.NODE_TYPE);
        String data = rs.getString(columnName);
        if (StringUtils.isBlank(fieldType) || StringUtils.isBlank(data)) {
            return null;
        }
        Class<? extends BaseNode> aClass = NodeType.getClass(fieldType);
        return JSON.parseObject(data, aClass);
    }

    @Override
    public BaseNode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String fieldType = rs.getString(BaseNode.NODE_TYPE);
        String data = rs.getString(columnIndex);
        if (StringUtils.isBlank(fieldType) || StringUtils.isBlank(data)) {
            return null;
        }
        Class<? extends BaseNode> aClass = NodeType.getClass(fieldType);
        return JSON.parseObject(data, aClass);
    }

    @Override
    public BaseNode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String data = cs.getString(columnIndex);
        String fieldType = cs.getString(BaseNode.NODE_TYPE);
        if (StringUtils.isBlank(fieldType) || StringUtils.isBlank(data)) {
            return null;
        }
        Class<? extends BaseNode> aClass = NodeType.getClass(fieldType);
        return JSON.parseObject(data, aClass);
    }
}
