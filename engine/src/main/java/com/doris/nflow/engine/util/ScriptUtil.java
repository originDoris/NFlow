package com.doris.nflow.engine.util;

import com.alibaba.fastjson2.JSON;

import java.util.Map;
import java.util.Objects;

/**
 * @author: xhz
 * @Title: ScriptUtil
 * @Description:
 * @date: 2022/10/10 14:36
 */
public class ScriptUtil {

    public static Map<String, Object> object2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(obj), Map.class);
    }
}
