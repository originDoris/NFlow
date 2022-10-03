package com.doris.nflow.engine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.doris.nflow.engine.common.constant.ExpressionTypeConstant;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.util.GroovyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: GroveCalulator
 * @Description:
 * @date: 2022/10/3 13:53
 */
@Service(ExpressionTypeConstant.GROOVY)
@Slf4j
public class GroovyCalculator implements ExpressionCalculator {

    @Override
    public Boolean calculate(String expression, Map<String, Object> dataMap) throws ProcessException {
        Object result = null;
        try {
            result = GroovyUtil.execute(expression, dataMap);
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                log.warn("the result of expression is not boolean.||expression={}||result={}||dataMap={}",
                        expression, result, JSON.toJSONString(dataMap));
                throw new ProcessException(ErrorCode.GROOVY_CALCULATE_FAILED.getCode(), "expression is not instanceof bool.");
            }
        } catch (Exception e) {
            log.error("calculate expression failed.||message={}||expression={}||dataMap={}, ", e.getMessage(), expression, dataMap, e);
            String groovyExFormat = "{0}: expression={1}";
            throw new ProcessException(ErrorCode.GROOVY_CALCULATE_FAILED, MessageFormat.format(groovyExFormat, e.getMessage(), expression));
        } finally {
            log.info("calculate expression.||expression={}||dataMap={}||result={}", expression, JSONObject.toJSONString(dataMap), result);
        }
    }
}
