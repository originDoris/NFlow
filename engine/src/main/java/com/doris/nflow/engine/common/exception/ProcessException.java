package com.doris.nflow.engine.common.exception;

import com.doris.nflow.engine.common.enumerate.ErrorCode;

/**
 * @author: xhz
 * @Title: ProcessException
 * @Description: 流程执行异常
 * @date: 2022/10/2 08:40
 */
public class ProcessException extends NFlowException{

    public ProcessException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public ProcessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProcessException(ErrorCode errorCode, String detailMsg) {
        super(errorCode, detailMsg);
    }
}
