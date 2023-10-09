package com.doris.nflow.engine.flow.instance.enumerate;

/**
 * @author: xhz
 * @Title: NodeInstanceStatus
 * @Description: 流程实例状态
 * @date: 2022/10/1 12:31
 */
public enum NodeInstanceStatus {
    SUCCESS("success", "处理成功"),
    PROCESSING("processing", "处理中"),
    FAIL("fail", "处理失败"),
    REVOKE("revoke", "处理已撤销"),
    ;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    NodeInstanceStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
