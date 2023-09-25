package com.doris.nflow.engine.common.exception;

import com.doris.nflow.engine.common.enumerate.ErrorCode;
import lombok.Data;

import java.text.MessageFormat;

/**
 * @author: xhz
 * @Title: NFlowException
 * @Description:
 * @date: 2022/10/1 07:53
 */
@Data
public class NFlowException extends Exception {

    private static final String ERROR_MSG_FORMAT = "{0}({1})";
    private Integer errorCode;

    private String errorMsg;


    public NFlowException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public NFlowException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMsg();
    }

    public NFlowException(ErrorCode errorCode, String detailMsg) {
        super(errorCode.getMsg());
        String errMsg = MessageFormat.format(ERROR_MSG_FORMAT, errorCode.getMsg(), detailMsg);
        this.errorCode = errorCode.getCode();
        this.errorMsg = errMsg;
    }




}
