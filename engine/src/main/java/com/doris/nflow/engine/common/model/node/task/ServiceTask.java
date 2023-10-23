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

    private String apiCode;


    public String getApiCode() {
        return getProperties().get("apiCode") == null ? null : (String) getProperties().get("apiCode");
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
        getProperties().put("apiCode", apiCode);
    }
}
