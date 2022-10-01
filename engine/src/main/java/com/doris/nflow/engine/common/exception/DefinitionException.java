package com.doris.nflow.engine.common.exception;

import com.doris.nflow.engine.common.enumerate.ErrorCode;

/**
 * @author: origindoris
 * @Title: DefinitionException
 * @Description: 流程定义异常
 * @date: 2022/10/1 08:48
 */
public class DefinitionException extends NFlowException {

    public DefinitionException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public DefinitionException(ErrorCode errorCode, String detailMsg) {
        super(errorCode, detailMsg);
    }

    public DefinitionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
