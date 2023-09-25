package com.doris.nflow.engine.flow.instance.enumerate;

/**
 * @author: xhz
 * @Title: FlowInstanceStatus
 * @Description: 流程实例状态
 * @date: 2022/10/1 12:31
 */
public enum FlowInstanceStatus {
    //状态(complete.执行完成 processing.执行中 termination.执行终止(强制终止))
    COMPLETE("complete", "执行完成"),
    PROCESSING("processing", "执行中"),
    TERMINATION("termination", "执行终止(强制终止)"),
    ;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    FlowInstanceStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
