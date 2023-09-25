package com.doris.nflow.engine.processor.model.result;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: xhz
 * @Title: CommonResult
 * @Description:
 * @date: 2022/10/8 09:59
 */
@Data
public class CommonResult implements Serializable {
    protected Integer errorCode;
    protected String errorMsg;
    protected boolean result;
}
