package com.doris.nflow.engine.common.enumerate;

/**
 * @author: xhz
 * @Title: ProcessStatus
 * @Description:
 * @date: 2022/10/8 15:23
 */
public enum ProcessStatus {

    /**
     * 流程状态
     */

    DEFAULT("default", "默认"),
    SUCCESS("success", "已完成"),
    FAILED("failed", "已撤销"),
    ;
    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ProcessStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
