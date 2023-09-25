package com.doris.nflow.engine.processor.model.result;

import lombok.Data;

/**
 * @author: xhz
 * @Title: CreateFlowResult
 * @Description:
 * @date: 2022/10/8 10:11
 */
@Data
public class CreateFlowResult extends CommonResult{
    private String flowModuleCode;
}
