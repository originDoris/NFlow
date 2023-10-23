package com.doris.nflow.engine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.doris.nflow.engine.common.constant.ExpressionTypeConstant;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.util.GroovyUtil;
import com.doris.nflow.engine.util.ScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xhz
 * @Title: GroovyCalculate
 * @Description:
 * @date: 2022/10/3 13:53
 */
@Service(ExpressionTypeConstant.GROOVY)
@Slf4j
public class GroovyCalculator implements ExpressionCalculator {

    @Override
    public Boolean calculate(String expression, Map<String, Object> dataMap) throws ProcessException {
        Object result = execute(expression, dataMap);
        if (result instanceof Boolean) {
            return  (Boolean) result;
        } else {
            log.warn("the result of expression is not boolean.||expression={}||result={}||dataMap={}",
                    expression, result, JSON.toJSONString(dataMap));
            throw new ProcessException(ErrorCode.GROOVY_CALCULATE_FAILED.getCode(), "expression is not instanceof bool.");
        }
    }

    private Object execute(String script, Map<String, Object> dataMap) throws ProcessException {
        Object result = null;
        try {
            result =  GroovyUtil.execute(script, dataMap);
        } catch (Exception e) {
            log.error("calculate expression failed.||message={}||script={}||dataMap={}, ", e.getMessage(), script, dataMap, e);
            String groovyExFormat = "{0}: expression={1}";
            throw new ProcessException(ErrorCode.GROOVY_CALCULATE_FAILED, MessageFormat.format(groovyExFormat, e.getMessage(), script));
        } finally {
            log.info("calculate expression.||script={}||dataMap={}||result={}", script, JSONObject.toJSONString(dataMap), result);
        }
        return result;
    }

    @Override
    public Map<String, Object> executorScript(String script, Map<String, Object> dataMap) throws ProcessException {
        return ScriptUtil.object2Map(execute(script, dataMap));
    }
}
