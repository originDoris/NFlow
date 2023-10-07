package com.doris.nflow.engine.flow.deployment.model;

import com.doris.nflow.engine.common.model.BaseQuery;
import lombok.Data;

/**
 * @author: xhz
 * @Title: FlowDeploymentQuery
 * @Description:
 * @date: 2022/10/1 10:26
 */
@Data
public class FlowDeploymentQuery extends BaseQuery {

    private String flowModuleCode;


    private String status;

    private String search;
}
