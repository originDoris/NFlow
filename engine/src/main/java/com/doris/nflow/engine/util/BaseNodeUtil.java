package com.doris.nflow.engine.util;

import com.doris.nflow.engine.common.constant.NodePropertyConstant;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.model.node.BaseNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: BaseNodeUtil
 * @Description:
 * @date: 2022/10/3 10:50
 */
public class BaseNodeUtil {

    public static String getCondition(BaseNode baseNode){
        Map<String, Object> properties = baseNode.getProperties();
        if (MapUtils.isEmpty(properties)) {
            return null;
        }
        return (String) properties.get(NodePropertyConstant.CONDITION);
    }

    public static Map<String, BaseNode> getBaseNodeMap(List<BaseNode> baseNodes) {
        if (CollectionUtils.isEmpty(baseNodes)) {
            return null;
        }
        HashMap<String, BaseNode> map = new HashMap<>(baseNodes.size());
        for (BaseNode baseNode : baseNodes) {
            map.put(baseNode.getCode(), baseNode);
        }
        return map;
    }

    public static String getConditionType(BaseNode baseNode){
        Map<String, Object> properties = baseNode.getProperties();
        if (MapUtils.isEmpty(properties)) {
            return null;
        }
        return (String) properties.get(NodePropertyConstant.CONDITION_TYPE);
    }

    public static BaseNode getStartNode(Map<String, BaseNode> baseNodeMap){
        if (MapUtils.isEmpty(baseNodeMap)) {
            return null;
        }
        Optional<BaseNode> any = baseNodeMap.values().stream().filter(baseNode -> NodeType.START_EVENT_NODE.getCode().equals(baseNode.getType())).findAny();
        if (any.isEmpty()) {
            return null;
        }
        return any.get();
    }

    public static BaseNode getNodeByCode(Map<String, BaseNode> baseNodeMap, String code) {
        if (MapUtils.isEmpty(baseNodeMap)) {
            return null;
        }
        Optional<BaseNode> any = baseNodeMap.values().stream().filter(baseNode -> code.equals(baseNode.getCode())).findAny();
        if (any.isEmpty()) {
            return null;
        }
        return any.get();
    }


    public static boolean isDefaultCondition(BaseNode flowElement) {
        Map<String, Object> properties = flowElement.getProperties();
        String isDefaultStr = (String) properties.get(NodePropertyConstant.DEFAULT_CONDITION);
        return "true".equalsIgnoreCase(isDefaultStr);
    }

    public static boolean isElementType(String nodeCode, Map<String, BaseNode> baseNodeMap, NodeType nodeType) {
        if (MapUtils.isEmpty(baseNodeMap)) {
            return false;
        }
        Optional<BaseNode> any = baseNodeMap.values().stream().filter(baseNode -> nodeCode.equals(baseNode.getCode())).findAny();
        if (any.isEmpty()) {
            return false;
        }
        BaseNode baseNode = any.get();
        return baseNode.getType().equals(nodeType.getCode());
    }

}
