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
public class NodeInstanceDataQuery extends BaseQuery {

    private String flowInstanceCode;

    private String nodeInstanceCode;

    private String flowModuleCode;

    private String instanceDataCode;

    private String type;
    private String search;
}
