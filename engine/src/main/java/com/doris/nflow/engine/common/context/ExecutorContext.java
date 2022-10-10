package com.doris.nflow.engine.common.context;

import com.doris.nflow.engine.executor.RuntimeExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: ExecutorContext
 * @Description:
 * @date: 2022/10/3 08:30
 */
@Service
@Slf4j
public class ExecutorContext {

    @Autowired
    private Map<String, RuntimeExecutor> runtimeExecutorHashMap;


    public RuntimeExecutor getRuntimeExecutor(String type){
        return runtimeExecutorHashMap.get(type);
    }
}
