package com.doris.nflow.engine.common.model.node.task;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

import java.util.Map;

/**
 * @author: xhz
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


    public String getUrl() {
        return (String) getProperties().get("url");
    }

    public void setUrl(String url) {
        getProperties().put("url", url);
    }

    public String getMethodType() {
        return getProperties().get("methodType") == null ? null : (String) getProperties().get("methodType");
    }

    public void setMethodType(String methodType) {
        getProperties().put("methodType", methodType);
    }

    public Map<String, String> getHeaderMap() {
        return getProperties().get("headerMap") == null ? null : (Map<String, String>) getProperties().get("headerMap");
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        getProperties().put("headerMap", headerMap);
    }

    public Long getTimeout() {
        return getProperties().get("timeout") == null ? null : (Long) getProperties().get("timeout");
    }

    public void setTimeout(Long timeout) {
        getProperties().put("timeout", timeout);
    }
}
