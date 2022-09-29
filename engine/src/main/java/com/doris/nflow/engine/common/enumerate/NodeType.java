package com.doris.nflow.engine.common.enumerate;

/**
 * @author: origindoris
 * @Title: NodeType
 * @Description:
 * @date: 2022/9/29 14:04
 */
public enum NodeType {
    /**
     * 节点类型
     */
    START_EVENT_NODE("start","开始节点"),
    END_EVENT_NODE("end","结束节点"),
    USER_TASK_NODE("user","用户节点"),
    SERVICE_TASK_NODE("service","服务节点"),
    SCRIPT_TASK_NODE("script","脚本节点"),
    EXCLUSIVE_GATEWAY_NODE("exclusiveGateway","单一网关/排他网关"),
    SEQUENCE_FLOW_NODE("sequence","顺序流");

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    NodeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
