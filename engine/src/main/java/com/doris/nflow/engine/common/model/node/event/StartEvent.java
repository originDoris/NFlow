package com.doris.nflow.engine.common.model.node.event;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.util.List;

/**
 * @author: xhz
 * @Title: StartEvent
 * @Description: 开始节点
 * @date: 2022/9/29 15:24
 */
@Data
public class StartEvent extends EventNode {

    private List<Param> params;

    @Data
    public static class Param {
        private String name;

        private String type;

        private String desc;
        /**
         * 1 表示是，0 表示否
         */
        private Integer isRequired;
    }

    public List<Param> getParams() {
        return getProperties().get("params") == null ? null : JSON.parseArray(JSON.toJSONString(getProperties().get("params")), Param.class);
    }

    public void setParams(List<Param> params) {
        getProperties().put("params", JSON.toJSONString(params));
    }
}
