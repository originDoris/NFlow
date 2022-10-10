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
     * 流程脚本计算器，用于执行流程中的简单表达式
     * @param expression 表达式
     * @param dataMap 参数
     * @return true / false
     * @throws ProcessException
     */
    Boolean calculate(String expression, Map<String, Object> dataMap) throws ProcessException;


    /**
     * 执行脚本，用于脚本任务
     * @param script 脚本
     * @param dataMap 参数
     * @return key：value map
     * @throws ProcessException
     */
    Map<String, Object> executorScript(String script, Map<String, Object> dataMap) throws ProcessException;
}
