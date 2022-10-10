package com.doris.nflow.engine.common.constant;

/**
 * @author: origindoris
 * @Title: NodeTypeConstant
 * @Description: 节点类型枚举
 * @date: 2022/10/1 07:13
 */
public class NodeTypeConstant {
    public static final String START_EVENT_NODE = "start";
    public static final String USER_TASK_NODE = "user";
    public static final String END_EVENT_NODE = "end";
    public static final String SERVICE_TASK_NODE = "service";
    public static final String SCRIPT_TASK_NODE = "script";
    public static final String EXCLUSIVE_GATEWAY_NODE = "exclusiveGateway";
    public static final String SEQUENCE_FLOW_NODE = "sequence";
}
