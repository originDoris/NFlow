package com.doris.nflow.engine.processor.model.result;

import lombok.Data;

/**
 * @author: xhz
 * @Title: DeployFlowResult
 * @Description:
 * @date: 2022/10/8 14:04
 */
@Data
public class DeployFlowResult extends CommonResult {

    private String flowModuleCode;

    private String flowDeployCode;

    private String content;
}
