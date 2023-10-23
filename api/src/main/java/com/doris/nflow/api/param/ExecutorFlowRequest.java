package com.doris.nflow.api.param;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * @author: xhz
 * @Title: ExecutorFlowRequest
 * @Description:
 * @date: 2023/10/17 15:10
 */
@Data
public class ExecutorFlowRequest implements Serializable {
    private String flowDeployCode;

    private String flowModuleCode;

    private String content;

    private String webSocketKey;
}
