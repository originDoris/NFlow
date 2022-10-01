package com.doris.nflow.engine.common.exception;

import com.doris.nflow.engine.common.enumerate.ErrorCode;

/**
 * @author: origindoris
 * @Title: DeploymentException
 * @Description: 流程发布异常
 * @date: 2022/10/1 10:44
 */
public class DeploymentException extends NFlowException{
    public DeploymentException(Integer errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public DeploymentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DeploymentException(ErrorCode errorCode, String detailMsg) {
        super(errorCode, detailMsg);
    }
}
