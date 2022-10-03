package com.doris.nflow.engine.util;

import com.doris.nflow.engine.common.constant.NodePropertyConstant;
import com.doris.nflow.engine.common.model.node.BaseNode;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

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

    public static String getConditionType(BaseNode baseNode){
        Map<String, Object> properties = baseNode.getProperties();
        if (MapUtils.isEmpty(properties)) {
            return null;
        }
        return (String) properties.get(NodePropertyConstant.CONDITION_TYPE);
    }


    public static boolean isDefaultCondition(BaseNode flowElement) {
        Map<String, Object> properties = flowElement.getProperties();
        String isDefaultStr = (String) properties.get(NodePropertyConstant.DEFAULT_CONDITION);
        return "true".equalsIgnoreCase(isDefaultStr);
    }

}
