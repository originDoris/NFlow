package com.doris.nflow.engine.common.context;

import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: RuntimeContext
 * @Description: 流程运行上下文
 * @date: 2022/10/2 08:46
 */
@Data
public class RuntimeContext implements Serializable {

    /**
     * 流程发布code
     */
    private String flowDeployCode;

    /**
     * 流程模块代码
     */
    private String flowModuleCode;

    private String tenantCode;

    private String caller;

    /**
     * 流程module 节点map key：nodeCode value ： baseNode
     */
    private Map<String, BaseNode> baseNodeMap;

    /**
     * 流程实例代码
     */
    private String flowInstanceCode;

    /**
     * 流程实例状态
     */
    private int flowInstanceStatus;

    private NodeInstance suspendNodeInstance;

    private List<NodeInstance> nodeInstanceList;


    /**
     * 流程当前处理节点
     */
    private BaseNode currentNodeModel;

    /**
     * 流程当前节点实例
     */
    private NodeInstance currentNodeInstance;

    /**
     * 流程实例数据代码
     */
    private String instanceDataCode;
    /**
     * 实例数据map
     */
    private Map<String, InstanceData> instanceDataMap;

    /**
     * 流程状态
     */
    private int processStatus;
}
