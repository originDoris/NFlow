package com.doris.nflow.engine.common.model.param;

import com.doris.nflow.engine.common.model.node.BaseNode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: origindoris
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
