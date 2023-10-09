package com.doris.nflow.engine.common.enumerate;

import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.common.model.node.event.EndEvent;
import com.doris.nflow.engine.common.model.node.event.StartEvent;
import com.doris.nflow.engine.common.model.node.flow.SequenceFlow;
import com.doris.nflow.engine.common.model.node.gateway.ExclusiveGateway;
import com.doris.nflow.engine.common.model.node.task.ScriptTask;
import com.doris.nflow.engine.common.model.node.task.ServiceTask;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: NodeType
 * @Description:
 * @date: 2022/9/29 14:04
 */
public enum NodeType {
    /**
     * 节点类型
     */
    START_EVENT_NODE(NodeTypeConstant.START_EVENT_NODE, "开始节点", StartEvent.class),
    END_EVENT_NODE(NodeTypeConstant.END_EVENT_NODE, "结束节点", EndEvent.class),
    SERVICE_TASK_NODE(NodeTypeConstant.SERVICE_TASK_NODE, "服务节点", ServiceTask.class),
    SCRIPT_TASK_NODE(NodeTypeConstant.SCRIPT_TASK_NODE, "脚本节点", ScriptTask.class),
    EXCLUSIVE_GATEWAY_NODE(NodeTypeConstant.EXCLUSIVE_GATEWAY_NODE, "单一网关/排他网关", ExclusiveGateway.class),
    SEQUENCE_FLOW_NODE(NodeTypeConstant.SEQUENCE_FLOW_NODE, "顺序流", SequenceFlow.class),
    ;

    private String code;

    private String desc;

    private Class<? extends BaseNode> typeClass;

    public Class<? extends BaseNode> getTypeClass() {
        return typeClass;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    NodeType(String code, String desc, Class<? extends BaseNode> typeClass) {
        this.code = code;
        this.desc = desc;
        this.typeClass = typeClass;
    }

    public static Class<? extends BaseNode> getClass(String type) {
        Optional<NodeType> any = Arrays.stream(NodeType.values()).filter(fieldType -> type.equals(fieldType.getCode())).findAny();
        return any.<Class<? extends BaseNode>>map(NodeType::getTypeClass).orElse(null);
    }

}
