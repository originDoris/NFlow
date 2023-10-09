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

import java.util.HashMap;
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


    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("1696735719228", "{\"records\":[{\"id\":25,\"createTime\":\"2023-10-07T02:14:20.000+00:00\",\"modifyTime\":\"1969-12-31T16:00:00.000+00:00\",\"operator\":\"\",\"caller\":\"\",\"archive\":false,\"remark\":\"\",\"tenant\":\"\",\"tenantCode\":\"\",\"flowDeployCode\":\"396d9630-64b7-11ee-b375-2e6e8d1d6e86\",\"flowModuleCode\":\"3950bf5f-64b7-11ee-b375-2e6e8d1d6e86\",\"flowName\":\"服务编排流程\",\"status\":\"deployed\",\"content\":null,\"flowModule\":[{\"code\":\"start\",\"name\":\"开始事件\",\"nodeType\":\"start\",\"input\":null,\"output\":[\"sequence1\"],\"properties\":{}},{\"code\":\"sequence1\",\"name\":\"sequence1\",\"nodeType\":\"sequence\",\"input\":[\"start\"],\"output\":[\"serviceTask\"],\"properties\":{}},{\"code\":\"serviceTask\",\"name\":\"获取请求API列表\",\"nodeType\":\"service\",\"input\":[\"sequence1\"],\"output\":[\"sequence2\"],\"properties\":{\"methodnodeType\":\"get\",\"url\":\"http://192.168.1.204:8081/datacube/api/info/list?projectId=0&pageSize=10&pageNo=1\"},\"url\":\"http://192.168.1.204:8081/datacube/api/info/list?projectId=0&pageSize=10&pageNo=1\",\"methodType\":null,\"headerMap\":null,\"timeout\":null},{\"code\":\"sequence2\",\"name\":\"sequence2\",\"nodeType\":\"sequence\",\"input\":[\"serviceTask\"],\"output\":[\"script\"],\"properties\":{}},{\"code\":\"script\",\"name\":\"groovy脚本处理\",\"nodeType\":\"script\",\"input\":[\"sequence2\"],\"output\":[\"sequence3\"],\"properties\":{\"scriptnodeType\":\"groovy\",\"script\":\"def map = new HashMap<String, Object>()\\n        map.put(\\\"size\\\",new groovy.json.JsonSlurper().parseText(serviceTask.get()).data.list.size())\\n        return map\"},\"script\":\"def map = new HashMap<String, Object>()\\n        map.put(\\\"size\\\",new groovy.json.JsonSlurper().parseText(serviceTask.get()).data.list.size())\\n        return map\",\"scriptType\":null},{\"code\":\"sequence3\",\"name\":\"sequence3\",\"nodeType\":\"sequence\",\"input\":[\"script\"],\"output\":[\"end\"],\"properties\":{}},{\"code\":\"end\",\"name\":\"结束\",\"nodeType\":\"end\",\"input\":[\"sequence2\"],\"output\":null,\"properties\":{}}]},{\"id\":41,\"createTime\":\"2023-10-08T03:30:14.000+00:00\",\"modifyTime\":\"1969-12-31T16:00:00.000+00:00\",\"operator\":\"\",\"caller\":\"\",\"archive\":false,\"remark\":\"\",\"tenant\":\"\",\"tenantCode\":\"\",\"flowDeployCode\":\"fe2d55a8-658a-11ee-b40d-664473a5f0a4\",\"flowModuleCode\":\"fe2ae4a7-658a-11ee-b40d-664473a5f0a4\",\"flowName\":\"服务编排1696735840406\",\"status\":\"deployed\",\"content\":\"{\\\"nodes\\\":[{\\\"id\\\":\\\"1696735718488\\\",\\\"type\\\":\\\"rect\\\",\\\"x\\\":380,\\\"y\\\":160,\\\"properties\\\":{\\\"nodeType\\\":\\\"start\\\"},\\\"text\\\":{\\\"x\\\":380,\\\"y\\\":160,\\\"value\\\":\\\"start\\\"}},{\\\"id\\\":\\\"1696735719228\\\",\\\"type\\\":\\\"rect\\\",\\\"x\\\":640,\\\"y\\\":160,\\\"properties\\\":{\\\"nodeType\\\":\\\"service\\\",\\\"id\\\":\\\"1696735719228\\\",\\\"text\\\":\\\"API\\\",\\\"scriptType\\\":\\\"\\\",\\\"script\\\":\\\"\\\",\\\"methodType\\\":\\\"get\\\",\\\"url\\\":\\\"http://192.168.1.204:7002/nflow/queryPage\\\"},\\\"text\\\":{\\\"x\\\":640,\\\"y\\\":160,\\\"value\\\":\\\"API\\\"}},{\\\"id\\\":\\\"1696735720759\\\",\\\"type\\\":\\\"rect\\\",\\\"x\\\":910,\\\"y\\\":160,\\\"properties\\\":{\\\"nodeType\\\":\\\"script\\\",\\\"id\\\":\\\"1696735720759\\\",\\\"text\\\":\\\"插件\\\",\\\"scriptType\\\":\\\"groovy\\\",\\\"script\\\":\\\"def map = new HashMap<String, Object>() map.put(\\\\\\\\\\\\\\\"size\\\\\\\\\\\\\\\",new groovy.json.JsonSlurper().parseText(1696735719228.get()).size()) return map\\\",\\\"methodType\\\":\\\"\\\",\\\"url\\\":\\\"\\\"},\\\"text\\\":{\\\"x\\\":910,\\\"y\\\":160,\\\"value\\\":\\\"插件\\\"}},{\\\"id\\\":\\\"1696735723002\\\",\\\"type\\\":\\\"rect\\\",\\\"x\\\":1150,\\\"y\\\":160,\\\"properties\\\":{\\\"nodeType\\\":\\\"end\\\"},\\\"text\\\":{\\\"x\\\":1150,\\\"y\\\":160,\\\"value\\\":\\\"end\\\"}}],\\\"edges\\\":[{\\\"id\\\":\\\"d69d0af9-725b-45f5-a1ba-d255b6742753\\\",\\\"type\\\":\\\"polyline\\\",\\\"sourceNodeId\\\":\\\"1696735718488\\\",\\\"targetNodeId\\\":\\\"1696735719228\\\",\\\"startPoint\\\":{\\\"x\\\":430,\\\"y\\\":160},\\\"endPoint\\\":{\\\"x\\\":590,\\\"y\\\":160},\\\"properties\\\":{},\\\"pointsList\\\":[{\\\"x\\\":430,\\\"y\\\":160},{\\\"x\\\":590,\\\"y\\\":160}]},{\\\"id\\\":\\\"dc264f26-2f5f-4cb7-a273-313d0ebaaae7\\\",\\\"type\\\":\\\"polyline\\\",\\\"sourceNodeId\\\":\\\"1696735719228\\\",\\\"targetNodeId\\\":\\\"1696735720759\\\",\\\"startPoint\\\":{\\\"x\\\":690,\\\"y\\\":160},\\\"endPoint\\\":{\\\"x\\\":860,\\\"y\\\":160},\\\"properties\\\":{},\\\"pointsList\\\":[{\\\"x\\\":690,\\\"y\\\":160},{\\\"x\\\":860,\\\"y\\\":160}]},{\\\"id\\\":\\\"251fe9d4-b5be-427d-a856-a8f9a5141da3\\\",\\\"type\\\":\\\"polyline\\\",\\\"sourceNodeId\\\":\\\"1696735720759\\\",\\\"targetNodeId\\\":\\\"1696735723002\\\",\\\"startPoint\\\":{\\\"x\\\":960,\\\"y\\\":160},\\\"endPoint\\\":{\\\"x\\\":1100,\\\"y\\\":160},\\\"properties\\\":{},\\\"pointsList\\\":[{\\\"x\\\":960,\\\"y\\\":160},{\\\"x\\\":1100,\\\"y\\\":160}]}]}\",\"flowModule\":[{\"code\":\"1696735718488\",\"name\":null,\"nodeType\":\"start\",\"input\":null,\"output\":[\"d69d0af9-725b-45f5-a1ba-d255b6742753\"],\"properties\":{\"nodeType\":\"start\"}},{\"code\":\"1696735719228\",\"name\":\"API\",\"nodeType\":\"service\",\"input\":[\"d69d0af9-725b-45f5-a1ba-d255b6742753\"],\"output\":[\"dc264f26-2f5f-4cb7-a273-313d0ebaaae7\"],\"properties\":{\"methodType\":\"get\",\"scriptType\":\"\",\"id\":\"1696735719228\",\"text\":\"API\",\"nodeType\":\"service\",\"url\":\"http://192.168.1.204:7002/nflow/queryPage\",\"script\":\"\"},\"url\":\"http://192.168.1.204:7002/nflow/queryPage\",\"methodType\":\"get\",\"headerMap\":null,\"timeout\":null},{\"code\":\"1696735720759\",\"name\":\"插件\",\"nodeType\":\"script\",\"input\":[\"dc264f26-2f5f-4cb7-a273-313d0ebaaae7\"],\"output\":[\"251fe9d4-b5be-427d-a856-a8f9a5141da3\"],\"properties\":{\"methodType\":\"\",\"scriptType\":\"groovy\",\"id\":\"1696735720759\",\"text\":\"插件\",\"nodeType\":\"script\",\"url\":\"\",\"script\":\"return binding.variables\"},\"script\":\"return binding.variables\",\"scriptType\":\"groovy\"},{\"code\":\"1696735723002\",\"name\":null,\"nodeType\":\"end\",\"input\":[\"251fe9d4-b5be-427d-a856-a8f9a5141da3\"],\"output\":null,\"properties\":{\"nodeType\":\"end\"}},{\"code\":\"d69d0af9-725b-45f5-a1ba-d255b6742753\",\"name\":null,\"nodeType\":\"sequence\",\"input\":[\"1696735718488\"],\"output\":[\"1696735719228\"],\"properties\":{}},{\"code\":\"dc264f26-2f5f-4cb7-a273-313d0ebaaae7\",\"name\":null,\"nodeType\":\"sequence\",\"input\":[\"1696735719228\"],\"output\":[\"1696735720759\"],\"properties\":{}},{\"code\":\"251fe9d4-b5be-427d-a856-a8f9a5141da3\",\"name\":null,\"nodeType\":\"sequence\",\"input\":[\"1696735720759\"],\"output\":[\"1696735723002\"],\"properties\":{}}]}],\"total\":0,\"size\":10,\"current\":1,\"orders\":[],\"optimizeCountSql\":true,\"hitCount\":false,\"searchCount\":true,\"pages\":0}");
        Binding binding = createBinding(data);

//


        Script parse = new GroovyShell(binding).parse("def map = new HashMap<String, Object>()\n        map.put(\"size\",new groovy.json.JsonSlurper().parseText(binding.variables.get(\"1696735719228\").get()).records.size())\n        return map");
        Object run = parse.run();
        System.out.println("run = " + run);
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
