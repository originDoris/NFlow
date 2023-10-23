package com.doris.nflow.engine.common.context;

import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xhz
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
    private String flowInstanceStatus;

    private NodeInstance suspendNodeInstance;

    private List<NodeInstance> nodeInstanceList = new ArrayList<>();


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
    private Map<String, InstanceData> instanceDataMap = new HashMap<>();


    /**
     * 运行过程中的变量
     */
    private Map<String, Object> variables = new HashMap<>();

    /**
     * 流程状态
     */
    private String processStatus;

    private String webSocketKey;
}
