package com.doris.nflow.engine.common.context;

import com.doris.nflow.engine.executor.RuntimeExecutor;
import com.doris.nflow.engine.script.ExpressionCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author: origindoris
 * @Title: ExecutorContext
 * @Description:
 * @date: 2022/10/3 08:30
 */
@Service
@Slf4j
public class ExpressionCalculatorContext {

    @Autowired
    private HashMap<String, ExpressionCalculator> expressionCalculatorHashMap;


    public ExpressionCalculator getExpressionCalculator(String type){
        return expressionCalculatorHashMap.get(type);
    }
}
