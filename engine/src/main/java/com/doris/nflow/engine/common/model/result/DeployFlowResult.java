package com.doris.nflow.engine.common.model.result;

import lombok.Data;

/**
 * @author: origindoris
 * @Title: DeployFlowResult
 * @Description:
 * @date: 2022/10/8 14:04
 */
@Data
public class DeployFlowResult extends CommonResult {

    private String flowModuleCode;

    private String flowDeployCode;
}
