package com.doris.nflow.engine.processor.model.flow;

import com.doris.nflow.engine.common.model.node.BaseNode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: origindoris
 * @Title: FlowInfo
 * @Description:
 * @date: 2022/10/9 10:07
 */
@Data
public class FlowInfo implements Serializable {

    private String flowModuleCode;

    private String flowDeployCode;

    private String tenantCode;

    private String caller;

    private List<BaseNode> flowModule;
}
