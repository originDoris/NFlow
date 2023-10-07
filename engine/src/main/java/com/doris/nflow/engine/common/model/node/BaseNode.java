package com.doris.nflow.engine.common.model.node;

import com.alibaba.fastjson2.annotation.JSONType;
import com.doris.nflow.engine.common.model.node.task.ScriptTask;
import com.doris.nflow.engine.common.model.node.task.ServiceTask;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xhz
 * @Title: FlowNode
 * @Description:
 * @date: 2022/9/29 13:55
 */
@Data
public class BaseNode implements Serializable {

    public static final String NODE_TYPE = "nodeType";

    /**
     * 节点唯一代码
     */
    private String code;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点类型
     */
    private String nodeType;

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
    private Map<String, Object> properties = new HashMap<>();
}
