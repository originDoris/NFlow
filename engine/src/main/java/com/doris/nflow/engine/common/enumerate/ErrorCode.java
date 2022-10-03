package com.doris.nflow.engine.common.enumerate;

/**
 * @author: origindoris
 * @Title: ErrorCode
 * @Description: 错误代码
 * @date: 2022/10/1 07:55
 */
public enum ErrorCode {
    /**
     * 1000 ~ 1999 非阻断性错误码
     */
    SUCCESS(1000, "Success"),
    REENTRANT_WARNING(1001, "Reentrant warning"),
    COMMIT_SUSPEND(1002, "Commit task suspend"),
    ROLLBACK_SUSPEND(1003, "Rollback task suspend"),

    /**
     * 2000 ~ 2999 流程定义异常
     */
    NODE_LACK_INPUT(2000, "node lock input"),
    NODE_LACK_OUTPUT(2001, "node lock output"),
    NODE_TOO_MUCH_INPUT(2100, "too many input"),
    NODE_TOO_MUCH_OUTPUT(2101, "too many output"),

    /**
     * 3000 ~ 3999 流程执行异常
     */
    GET_OUTGOING_FAILED(3000, "Get outgoing failed"),
    MISSING_DATA(3001, "Miss data"),
    GROOVY_CALCULATE_FAILED(3002, "Groovy calculate failed"),
    GET_NODE_INSTANCE_FAILED(3003, "Get nodeInstance failed"),
    NO_USER_TASK_TO_ROLLBACK(3004, "No userTask to rollback"),
    UNSUPPORTED_ELEMENT_TYPE(3005, "Unsupported element type"),
    COMMIT_FAILED(3006, "Commit task failed"),

    /**
     * 5000 ~ 5999 业务参数异常
     */
    PARAM_INVALID(5000, "Invalid param"),
    ;
    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
