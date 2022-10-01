package com.doris.nflow.engine.node.instance.model;

import com.doris.nflow.engine.common.model.BaseQuery;
import lombok.Data;

/**
 * @author: origindoris
 * @Title: NodeInstanceQuery
 * @Description:
 * @date: 2022/10/1 12:25
 */
@Data
public class NodeInstanceLogQuery extends BaseQuery {

    private String flowInstanceCode;

    private String nodeInstanceCode;

    private String status;

    private String type;
}
