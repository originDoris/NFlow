package com.doris.nflow.engine.util;

import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xhz
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


    public static List<InstanceData> getInstanceDataList(Map<String, InstanceData> instanceDataMap){
        return new ArrayList<>(instanceDataMap.values());
    }

    public static Map<String, Object> parseInstanceDataMap(Map<String, InstanceData> instanceDataMap) {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return new HashMap<>(0);
        }
        Map<String, Object> dataMap = Maps.newHashMap();
        instanceDataMap.forEach((keyName, instanceData) -> {
            dataMap.put(keyName, instanceData.getValue());
        });
        return dataMap;
    }
}
