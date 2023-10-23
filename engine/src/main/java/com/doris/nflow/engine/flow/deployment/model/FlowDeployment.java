package com.doris.nflow.engine.flow.deployment.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.handler.BaseNodeHandler;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.deployment.enumerate.FlowDeploymentStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: xhz
 * @Title: FlowDeployment
 * @Description: 流程发布数据
 * @date: 2022/10/1 10:07
 */
@Data
@TableName(value = "fw_flow_deployment", autoResultMap = true)
public class FlowDeployment extends BaseModel {
    public static final String FLOW_MODULE_CODE = "flow_module_code";
    public static final String FLOW_DEPLOY_CODE = "flow_deploy_code";


    @NotBlank(message = "流程发布代码不能为空！")
    private String flowDeployCode;

    @NotBlank(message = "流程模块代码不能为空！")
    private String flowModuleCode;

    @NotBlank(message = "流程名称不能为空！")
    private String flowName;

    /**
     * 流程发布状态
     * {@link FlowDeploymentStatus}
     */
    private String status;

    private String content;

    @TableField(typeHandler = BaseNodeHandler.class)
    @NotEmpty(message = "流程定义模块不能为空！")
    private List<BaseNode> flowModule;


}
