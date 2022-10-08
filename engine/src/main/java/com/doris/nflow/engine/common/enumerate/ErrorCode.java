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
    MODEL_EMPTY(2002, "Empty model"),
    NODE_TOO_MUCH_INPUT(2100, "too many input"),
    NODE_TOO_MUCH_OUTPUT(2101, "too many output"),
    NODE_KEY_NOT_UNIQUE(2102, "node key not unique"),

    START_NODE_INVALID(2103, "Zero or more than one start node"),
    END_NODE_INVALID(2104, "No end node"),
    GET_INSTANCE_DATA_FAILED(2105, "Get instanceData failed"),

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
    SAVE_FLOW_INSTANCE_FAILED(3007, "Save flowInstance failed"),
    SAVE_NODE_INSTANCE_DATA_FAILED(3008, "Save nodeInstanceData failed"),
    START_NODE_IS_NULL(3009, "start event node is null"),
    NODE_DOES_NOT_EXIST(3010, " node is null"),
    GET_NODE_FAILED(3011, "Get current node failed"),
    ROLLBACK_FAILED(4002, "Rollback task failed"),

    /**
     * 4000 ~ 4999 操作权限异常
     */
    MODIFY_FLOW_STATUS_IS_NOT_EDIT(4000, "flow is not edit"),
    MODIFY_FLOW_STATUS_IS_NOT_INIT(4000, "flow is not init"),

    /**
     * 5000 ~ 5999 业务参数异常
     */
    PARAM_INVALID(5000, "Invalid param"),
    FLOW_INVALID(5001,"invalid flow")
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

    public static boolean isSuccess(Integer code){
        return code >= 1000 && code < 2000;
    }
}
