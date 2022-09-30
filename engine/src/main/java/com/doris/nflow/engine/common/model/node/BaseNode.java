package com.doris.nflow.engine.common.model.node;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: FlowNode
 * @Description:
 * @date: 2022/9/29 13:55
 */
@Data
public class BaseNode implements Serializable {

    public static final String NODE_TYPE = "type";

    /**
     * 节点唯一代码
     */
    private String code;

    /**
     * 节点类型
     */
    private String type;

    /**
     * 上游节点
     */
    private List<String> input;

    /**
     * 下游节点
     */
    private List<String> output;

    /**
     * 节点属性
     */
    private Map<String, Object> properties;
}
