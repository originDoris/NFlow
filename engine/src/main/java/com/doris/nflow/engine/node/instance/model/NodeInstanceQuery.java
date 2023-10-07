package com.doris.nflow.engine.node.instance.model;

import com.doris.nflow.engine.common.model.BaseQuery;
import lombok.Data;

/**
 * @author: xhz
 * @Title: NodeInstanceQuery
 * @Description:
 * @date: 2022/10/1 12:25
 */
@Data
public class NodeInstanceQuery extends BaseQuery {
    private String flowDeployCode;

    private String flowInstanceCode;

    private String nodeCode;

    private String nodeInstanceCode;

    private String status;

    private String search;
}
