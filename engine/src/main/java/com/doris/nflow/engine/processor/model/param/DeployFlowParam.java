package com.doris.nflow.engine.processor.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: xhz
 * @Title: DeployFlowParam
 * @Description:
 * @date: 2022/10/8 10:06
 */
@Data
public class DeployFlowParam extends CommonParam {


    /**
     * 流程模型代码
     */
    @NotBlank(message = "流程定义模块代码不能为空！")
    private String flowModuleCode;

}
