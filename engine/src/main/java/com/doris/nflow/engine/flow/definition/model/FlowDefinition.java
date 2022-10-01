package com.doris.nflow.engine.flow.definition.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.definition.enumerate.FlowDefinitionStatus;
import com.doris.nflow.engine.flow.definition.handler.BaseNodeHandler;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: origindoris
 * @Title: FlowDefinition
 * @Description:
 * @date: 2022/9/29 13:51
 */
@Data
@TableName("flow_definition")
public class FlowDefinition extends BaseModel {

    public static final String FLOW_MODULE_CODE = "flow_module_code";

    /**
     * 租户名称
     */
    private String tenant;

    /**
     * 租户代码
     */
    private String tenantCode;

    /**
     * 调用方
     */
    private String caller;

    /**
     * 是否归档 true 0 未删除 false 1 已删除
     */
    @TableLogic
    private Boolean archive;

    /**
     * 备注
     */
    private String remark;

    /**
     * 流程名称
     */
    @NotBlank(message = "流程名称不能为空！")
    private String flowName;

    /**
     * 流程模型列表
     */
    @TableField(typeHandler = BaseNodeHandler.class)
    @NotEmpty(message = "流程定义模块不能为空！")
    private List<BaseNode> flowModule;

    /**
     * 流程模型代码
     */
    @NotBlank(message = "流程模块代码不能为空！")
    private String flowModuleCode;

    /**
     * 流程状态
     * {@link com.doris.nflow.engine.flow.definition.enumerate.FlowDefinitionStatus}
     */
    @NotNull(message = "流程状态不能为空！")
    private FlowDefinitionStatus status;



}
