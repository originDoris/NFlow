package com.doris.nflow.engine.flow.definition.model;

import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.common.model.node.BaseNode;
import lombok.Data;

import java.util.List;

/**
 * @author: origindoris
 * @Title: FlowDefinition
 * @Description:
 * @date: 2022/9/29 13:51
 */
@Data
public class FlowDefinition extends BaseModel {

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
     * 是否归档 true 已删除 false 已删除
     */
    private Boolean archive;

    /**
     * 备注
     */
    private String remark;

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 流程模型列表
     */
    private List<BaseNode> flowModule;

    /**
     * 流程模型代码
     */
    private String flowModuleCode;

    /**
     * 流程状态
     * {@link com.doris.nflow.engine.flow.definition.enumerate.FlowDefinitionStatus}
     */
    private String status;



}
