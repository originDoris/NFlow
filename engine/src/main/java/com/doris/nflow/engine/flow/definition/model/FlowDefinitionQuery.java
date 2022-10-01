package com.doris.nflow.engine.flow.definition.model;

import com.doris.nflow.engine.common.model.BaseQuery;
import lombok.Data;

/**
 * @author: origindoris
 * @Title: FlowDefinitionQuery
 * @Description: 流程定义参数
 * @date: 2022/9/30 11:08
 */
@Data
public class FlowDefinitionQuery extends BaseQuery {

    private String status;
}
