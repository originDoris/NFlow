package com.doris.nflow.api.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

}
