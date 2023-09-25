package com.doris.nflow.engine.node.instance.enumerate;

/**
 * @author: xhz
 * @Title: NodeInstanceLogType
 * @Description: 流程实例状态
 * @date: 2022/10/1 12:31
 */
public enum NodeInstanceLogType {
    //操作类型(system.系统执行 submit.任务提交 revoke.任务撤销)
    SYSTEM("system", "系统执行"),
    SUBMIT("submit", "任务提交"),
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

    NodeInstanceLogType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
