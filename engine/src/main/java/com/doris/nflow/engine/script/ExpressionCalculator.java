package com.doris.nflow.engine.script;

import com.doris.nflow.engine.common.exception.ProcessException;

import java.util.Map;

/**
 * @author: origindoris
 * @Title: ExpressionCalculator
 * @Description: 脚本表达式计算器
 * @date: 2022/10/3 13:47
 */
public interface ExpressionCalculator {

    /**
     * 计算器
     * @param expression 表达式
     * @param dataMap 参数
     * @return
     * @throws ProcessException
     */
    Boolean calculate(String expression, Map<String, Object> dataMap) throws ProcessException;
}
