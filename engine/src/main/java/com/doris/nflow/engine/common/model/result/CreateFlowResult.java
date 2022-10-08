package com.doris.nflow.engine.common.model.result;

import lombok.Builder;
import lombok.Data;

/**
 * @author: origindoris
 * @Title: CreateFlowResult
 * @Description:
 * @date: 2022/10/8 10:11
 */
@Data
public class CreateFlowResult extends CommonResult{
    private String flowModuleCode;
}
