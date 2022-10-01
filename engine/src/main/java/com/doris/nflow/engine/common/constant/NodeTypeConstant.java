package com.doris.nflow.engine.common.constant;

/**
 * @author: origindoris
 * @Title: NodeTypeConstant
 * @Description: 节点类型枚举
 * @date: 2022/10/1 07:13
 */
public class NodeTypeConstant {
//       START_EVENT_NODE("start", "开始节点", StartEvent.class),
//    END_EVENT_NODE("end", "结束节点", EndEvent.class),
//    USER_TASK_NODE("user", "用户节点", UserTask.class),
//    SERVICE_TASK_NODE("service", "服务节点", ServiceTask.class),
//    SCRIPT_TASK_NODE("script", "脚本节点", ScriptTask.class),
//    EXCLUSIVE_GATEWAY_NODE("exclusiveGateway", "单一网关/排他网关", ExclusiveGateway.class),
//    SEQUENCE_FLOW_NODE("sequence", "顺序流", SequenceFlow.class),

    public static final String START_EVENT_NODE = "start";
    public static final String USER_TASK_NODE = "user";
    public static final String END_EVENT_NODE = "end";
    public static final String SERVICE_TASK_NODE = "service";
    public static final String SCRIPT_TASK_NODE = "script";
    public static final String EXCLUSIVE_GATEWAY_NODE = "exclusiveGateway";
    public static final String SEQUENCE_FLOW_NODE = "sequence";
}
