package com.doris.nflow.engine.common.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: origindoris
 * @Title: EditFlowParam
 * @Description:
 * @date: 2022/10/8 13:54
 */
@Data
public class EditFlowParam extends CommonParam {

    @NotBlank(message = "流程模块代码不能为空！")
    private String flowModuleCode;
}
