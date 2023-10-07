package com.doris.nflow.engine.flow.instance.model;

import com.doris.nflow.engine.common.model.BaseQuery;
import lombok.Data;

/**
 * @author: xhz
 * @Title: FlowInstanceQuery
 * @Description:
 * @date: 2022/10/1 12:25
 */
@Data
public class FlowInstanceQuery extends BaseQuery {
    private String flowDeployCode;

    private String flowInstanceCode;

    private String flowModuleCode;

    private String status;
    private String search;
}
