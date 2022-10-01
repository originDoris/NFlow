package com.doris.nflow.engine.common.exception;

import com.doris.nflow.engine.common.enumerate.ErrorCode;

/**
 * @author: origindoris
 * @Title: ParamException
 * @Description: 业务参数异常
 * @date: 2022/10/1 10:56
 */
public class ParamException extends NFlowException{
    public ParamException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public ParamException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ParamException(ErrorCode errorCode, String detailMsg) {
        super(errorCode, detailMsg);
    }
}
