package com.doris.nflow.engine.util;

import com.doris.nflow.engine.common.constant.NodePropertyConstant;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xhz
 * @Title: BaseNodeUtil
 * @Description:
 * @date: 2022/10/3 10:50
 */
@Slf4j
public class GroovyUtil {


    private static final Map<String, Class> SCRIPT_CLASS_CACHE = new ConcurrentHashMap<String, Class>();

    private GroovyUtil() {
    }

    public static Object execute(String expression, Map<String, Object> dataMap) throws Exception {
        if (StringUtils.isBlank(expression)) {
            log.warn("calculate: expression is empty");
            return null;
        }
        try {
            Binding binding = createBinding(dataMap);
            Script shell = createScript(expression, binding);
            Object resultObject = shell.run();
            log.info("calculate.||expression={}||resultObject={}", expression, resultObject);
            return resultObject;
        } catch (MissingPropertyException mpe) {
            log.warn("calculate MissingPropertyException.||expression={}||dataMap={}", expression, dataMap);
            throw new ProcessException(ErrorCode.MISSING_DATA.getCode(), mpe.getMessage());
        }
    }
//    Script script = InvokerHelper.createScript(new GroovyShell(binding).parse("return serviceTask.data.list.size").getClass(),binding);
//script.run();
//


    private static Script createScript(String groovyExpression, Binding binding) {
        Script script;
        if (SCRIPT_CLASS_CACHE.containsKey(groovyExpression)) {
            Class scriptClass = SCRIPT_CLASS_CACHE.get(groovyExpression);
            script = InvokerHelper.createScript(scriptClass, binding);
        } else {
            script = new GroovyShell(binding).parse(groovyExpression);
            SCRIPT_CLASS_CACHE.put(groovyExpression, script.getClass());
        }
        return script;
    }

    private static Binding createBinding(Map<String, Object> infos) {
        Binding binding = new Binding();
        if (!infos.isEmpty()) {
            for (Map.Entry<String, Object> entry : infos.entrySet()) {
                binding.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return binding;
    }

}
