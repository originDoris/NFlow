package com.doris.nflow.engine.common.model.node.task;

import lombok.Data;

import java.util.Map;

/**
 * @author: origindoris
 * @Title: ServiceTask
 * @Description: 服务任务
 * @date: 2022/9/29 15:42
 */
@Data
public class ServiceTask extends TaskNode {

    private String url;

    private String methodType;

    private Map<String, String> headerMap;

    /**
     * 请求超时时间 （毫秒）
     */
    private Long timeout;
}
