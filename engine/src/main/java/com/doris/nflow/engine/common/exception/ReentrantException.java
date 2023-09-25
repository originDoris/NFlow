package com.doris.nflow.engine.common.exception;

import com.doris.nflow.engine.common.enumerate.ErrorCode;

/**
 * @author: xhz
 * @Title: ReentrantException
 * @Description: 重入异常
 * @date: 2022/10/2 08:45
 */
public class ReentrantException extends ProcessException{
    public ReentrantException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public ReentrantException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReentrantException(ErrorCode errorCode, String detailMsg) {
        super(errorCode, detailMsg);
    }
}
