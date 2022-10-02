package com.doris.nflow.engine.node.instance.handler;

import com.alibaba.fastjson2.JSON;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.handler.JsonToObjectHandler;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.node.instance.model.InstanceData;
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
 * @Title: InstanceDataHandler
 * @Description:
 * @date: 2022/9/30 08:34
 */
@MappedTypes(value = InstanceData.class)
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class InstanceDataHandler extends JsonToObjectHandler<InstanceData> {

}
