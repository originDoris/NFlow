package com.doris.nflow.engine.util;

import com.alibaba.fastjson2.*;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.google.common.collect.Lists;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: xhz
 * @Title: BuildModuleUtil
 * @Description:
 * @date: 2022/10/10 16:01
 */
public class BuildModuleUtil {


    public static List<BaseNode> buildNode(String content){
        if (StringUtils.isBlank(content)) {
            return new ArrayList<>();
        }
        JSONObject jsonObject = JSONObject.parseObject(content);
        JSONArray nodeList = jsonObject.getJSONArray("nodes");
        JSONArray edges = jsonObject.getJSONArray("edges");
        Map<String, List<Object>> sourceIdMap = edges.stream().collect(Collectors.groupingBy(o -> {
            JSONObject edge = JSONObject.parseObject(JSON.toJSONString(o));
            return edge.getString("sourceNodeId");
        }));
        Map<String, List<Object>> targetIdMap = edges.stream().collect(Collectors.groupingBy(o -> {
            JSONObject edge = JSONObject.parseObject(JSON.toJSONString(o));
            return edge.getString("targetNodeId");
        }));


        ArrayList<BaseNode> resultNodes = new ArrayList<>();
        nodeList.forEach(o -> {
            JSONObject node = JSONObject.parseObject(JSON.toJSONString(o));
            JSONObject properties = node.getJSONObject("properties");
            BaseNode baseNode = new BaseNode();
            baseNode.setCode(node.getString("id"));
            baseNode.setNodeType(properties.getString("nodeType"));
            baseNode.setName(properties.getString("text"));
            baseNode.setProperties(properties);
            List<Object> source = sourceIdMap.get(baseNode.getCode());
            if (source != null && !source.isEmpty()) {
                ArrayList<String> output = new ArrayList<>();
                for (Object object : source) {
                    output.add(JSON.parseObject(JSON.toJSONString(object)).getString("id"));
                }
                baseNode.setOutput(output);
            }
            List<Object> target = targetIdMap.get(baseNode.getCode());
            if (target != null && !target.isEmpty()) {
                ArrayList<String> input = new ArrayList<>();
                for (Object object : target) {
                    input.add(JSON.parseObject(JSON.toJSONString(object)).getString("id"));
                }
                baseNode.setInput(input);
            }
            resultNodes.add(baseNode);
        });


        for (Object edge : edges) {
            JSONObject node = JSONObject.parseObject(JSON.toJSONString(edge));
            JSONObject properties = node.getJSONObject("properties");
            BaseNode baseNode = new BaseNode();
            baseNode.setCode(node.getString("id"));
            baseNode.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
            baseNode.setName(properties.getString("text"));
            baseNode.setProperties(properties);
            baseNode.setInput(Lists.newArrayList(node.getString("sourceNodeId")));
            baseNode.setOutput(Lists.newArrayList(node.getString("targetNodeId")));
            resultNodes.add(baseNode);
        }

        return resultNodes;

    }

    public static Map<String, Object> parseFlowParam(String content) throws ParamException {
        JSONObject flowData = JSON.parseObject(content);
        JSONArray nodes = flowData.getJSONArray("nodes");
        if (nodes == null || nodes.isEmpty()) {
            throw new ParamException(ErrorCode.PARAM_INVALID.getCode(), "服务编排参数不完整，不存在node信息！");
        }

        Optional<Object> startOptional = nodes.stream().filter(o -> {
            JSONObject properties = JSON.parseObject(JSON.toJSONString(o)).getJSONObject("properties");
            String nodeType = properties.getString("nodeType");
            if (StringUtils.isBlank(nodeType)) {
                try {
                    throw new ParamException(ErrorCode.PARAM_INVALID.getCode(), "服务编排参数不完整，节点类型不能为空！");
                } catch (ParamException e) {
                    throw new RuntimeException(e);
                }
            }
            return "start".equals(nodeType);
        }).findFirst();

        if (startOptional.isEmpty()) {
            throw new ParamException(ErrorCode.PARAM_INVALID.getCode(), "服务编排参数不完整，start节点不存在！");
        }

        JSONObject startNode = JSON.parseObject(JSON.toJSONString(startOptional.get()));

        JSONObject properties = startNode.getJSONObject("properties");
        if (properties == null) {
            return null;
        }
        if (!properties.containsKey("params") && !properties.containsKey("body")) {
            return null;
        }
        if (properties.containsKey("body") ) {
            String body = properties.getString("body");
            return JSON.parseObject(body, new TypeReference<HashMap<String,Object>>(){}, JSONReader.Feature.AllowUnQuotedFieldNames);
        }
        JSONArray params = properties.getJSONArray("params");
        HashMap<String, Object> result = new HashMap<>();
        for (Object param : params) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(param));
            result.put(jsonObject.getString("name"), jsonObject.get("execValue"));
        }
        return result;
    }

}
