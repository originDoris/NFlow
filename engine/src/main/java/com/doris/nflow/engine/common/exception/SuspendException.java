package com.doris.nflow.engine.common.exception;

import com.doris.nflow.engine.common.enumerate.ErrorCode;

/**
 * @author: origindoris
 * @Title: SuspendException
 * @Description: 流程暂停异常
 * @date: 2022/10/2 08:45
 */
public class SuspendException extends ProcessException{
    public SuspendException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public SuspendException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SuspendException(ErrorCode errorCode, String detailMsg) {
        super(errorCode, detailMsg);
    }
}
