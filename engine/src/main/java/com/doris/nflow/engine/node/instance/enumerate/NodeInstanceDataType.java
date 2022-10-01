package com.doris.nflow.engine.node.instance.enumerate;

/**
 * @author: origindoris
 * @Title: NodeInstanceDataType
 * @Description: 流程实例状态
 * @date: 2022/10/1 12:31
 */
public enum NodeInstanceDataType {
    //操作类型(init.实例初始化 system.系统执行 system_pull.系统主动获取 source_update.上游更新 commit.任务提交 revoke.任务撤回)
    SYSTEM("system", "系统执行"),
    SYSTEM_PULL("system_pull", "系统主动获取"),
    SOURCE_UPDATE("source_update", "上游更新"),
    INIT("init", "实例初始化"),
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

    NodeInstanceDataType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
