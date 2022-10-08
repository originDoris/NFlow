package com.doris.nflow.engine.common.model.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.doris.nflow.engine.common.handler.BaseNodeHandler;
import com.doris.nflow.engine.common.model.node.BaseNode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: origindoris
 * @Title: ModifyFlowParam
 * @Description:
 * @date: 2022/10/8 13:39
 */
@Data
public class ModifyFlowParam extends CommonParam {
    @NotBlank(message = "流程名称不能为空！")
    private String flowName;

    /**
     * 流程模型列表
     */
    @NotEmpty(message = "流程定义模块不能为空！")
    private List<BaseNode> flowModule;


    /**
     * 流程模块代码
     */
    @NotEmpty(message = "流程定义模块代码不能为空！")
    private String flowModuleCode;

    private String remark;
}
