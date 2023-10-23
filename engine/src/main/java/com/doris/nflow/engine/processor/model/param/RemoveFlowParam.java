package com.doris.nflow.engine.processor.model.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xhz
 * @Title: RemoveFlowParam
 * @Description:
 * @date: 2023/10/13 09:57
 */
@Data
public class RemoveFlowParam implements Serializable {

    private String flowModuleCode;

    private String flowDeployCode;
}
