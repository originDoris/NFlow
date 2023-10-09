package com.doris.nflow.engine.flow.instance.handler;

import com.alibaba.fastjson2.JSON;
import com.doris.nflow.engine.common.handler.JsonToObjectHandler;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author: xhz
 * @Title: InstanceDataHandler
 * @Description:
 * @date: 2022/9/30 08:34
 */
@MappedTypes(value = InstanceData.class)
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class InstanceDataHandler extends JsonToObjectHandler<List<InstanceData>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<InstanceData> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public List<InstanceData> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String data = rs.getString(columnName);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return JSON.parseArray(data, InstanceData.class);
    }

    @Override
    public List<InstanceData> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String data = rs.getString(columnIndex);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return JSON.parseArray(data, InstanceData.class);
    }

    @Override
    public List<InstanceData> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String data = cs.getString(columnIndex);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return JSON.parseArray(data, InstanceData.class);
    }
}
