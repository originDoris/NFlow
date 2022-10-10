package com.doris.nflow.engine.util;

import com.doris.nflow.engine.common.enumerate.DataType;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: InstanceDataUtil
 * @Description:
 * @date: 2022/10/3 10:58
 */
public class InstanceDataUtil {

    public static Map<String, InstanceData> getInstanceDataMap(List<InstanceData> instanceDataList) {
        if (CollectionUtils.isEmpty(instanceDataList)) {
            return new HashMap<>(1);
        }
        Map<String, InstanceData> instanceDataMap = Maps.newHashMap();
        instanceDataList.forEach(instanceData -> {
            instanceDataMap.put(instanceData.getKey(), instanceData);
        });
        return instanceDataMap;
    }


    public static Map<String,InstanceData> parseDataMap2InstanceData(Map<String, Object> dataMap){
        if (MapUtils.isEmpty(dataMap)) {
            return new HashMap<>(1);
        }
        HashMap<String, InstanceData> result = new HashMap<>();

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            InstanceData instanceData = new InstanceData();
            instanceData.setValue(value);
            instanceData.setKey(key);
            // todo 类型推断
            instanceData.setType("object");
            result.put(key, instanceData);
        }
        return result;
    }

    public static List<InstanceData> getInstanceDataList(Map<String, InstanceData> instanceDataMap){
        return new ArrayList<>(instanceDataMap.values());
    }

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
