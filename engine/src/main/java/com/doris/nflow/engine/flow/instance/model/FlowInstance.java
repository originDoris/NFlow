package com.doris.nflow.engine.flow.instance.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.flow.instance.enumerate.FlowInstanceStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: xhz
 * @Title: FlowInstance
 * @Description: 流程实例
 * @date: 2022/10/1 12:20
 */
@Data
@TableName(value = "flow_instance",autoResultMap = true)
public class FlowInstance extends BaseModel {

    public static final String FLOW_INSTANCE_CODE = "flow_instance_code";

    /**
     * 流程实例代码
     */
    @NotBlank(message = "流程实例代码不能为空！")
    private String flowInstanceCode;

    /**
     * 流程发布代码
     */
    @NotBlank(message = "流程发布代码不能为空！")
    private String flowDeployCode;

    /**
     * 流程模块代码
     */
    @NotBlank(message = "流程模块代码不能为空！")
    private String flowModuleCode;

    /**
     * 流程实例状态
     * {@link FlowInstanceStatus}
     */
    @NotBlank(message = "流程状态不能为空！")
    private String status;
}
