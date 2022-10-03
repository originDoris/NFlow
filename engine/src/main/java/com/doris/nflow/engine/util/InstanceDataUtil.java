package com.doris.nflow.engine.util;

import com.doris.nflow.engine.common.enumerate.DataType;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: InstanceDataUtil
 * @Description:
 * @date: 2022/10/3 10:58
 */
public class InstanceDataUtil {


    public static Map<String, Object> parseInstanceDataMap(Map<String, InstanceData> instanceDataMap) {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return new HashMap<>(0);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        instanceDataMap.forEach((keyName, instanceData) -> {
            dataMap.put(keyName, parseInstanceData(instanceData));
        });
        return dataMap;
    }

    private static Object parseInstanceData(InstanceData instanceData) {
        if (instanceData == null) {
            return null;
        }
        String dataTypeStr = instanceData.getType();
        return DataType.parseData(dataTypeStr, instanceData.getValue());
    }
}
