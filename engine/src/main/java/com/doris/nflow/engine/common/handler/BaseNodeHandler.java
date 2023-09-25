package com.doris.nflow.engine.common.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.model.node.BaseNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: xhz
 * @Title: BaseNodeHandler
 * @Description:
 * @date: 2022/9/30 08:34
 */
@MappedTypes(value = BaseNode.class)
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class BaseNodeHandler extends JsonToObjectHandler<List<BaseNode>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<BaseNode> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public List<BaseNode> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String data = rs.getString(columnName);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        List<BaseNode> list = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(data);
        for (Object o : jsonArray) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(o));
            String nodeType = jsonObject.getString(BaseNode.NODE_TYPE);
            Class<? extends BaseNode> aClass = NodeType.getClass(nodeType);
            BaseNode baseNode = JSON.parseObject(jsonObject.toJSONString(), aClass);
            list.add(baseNode);
        }
        return list;
    }

    @Override
    public List<BaseNode> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String data = rs.getString(columnIndex);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        List<BaseNode> list = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(data);
        for (Object o : jsonArray) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(o));
            String nodeType = jsonObject.getString(BaseNode.NODE_TYPE);
            Class<? extends BaseNode> aClass = NodeType.getClass(nodeType);
            BaseNode baseNode = JSON.parseObject(jsonObject.toJSONString(), aClass);
            list.add(baseNode);
        }
        return list;
    }

    @Override
    public List<BaseNode> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String data = cs.getString(columnIndex);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        List<BaseNode> list = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(data);
        for (Object o : jsonArray) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(o));
            String nodeType = jsonObject.getString(BaseNode.NODE_TYPE);
            Class<? extends BaseNode> aClass = NodeType.getClass(nodeType);
            BaseNode baseNode = JSON.parseObject(jsonObject.toJSONString(), aClass);
            list.add(baseNode);
        }
        return list;
    }
}
