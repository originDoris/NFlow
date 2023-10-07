package com.doris.nflow.test.flow;

import com.doris.nflow.api.ApiApplication;
import com.doris.nflow.engine.common.constant.ExpressionTypeConstant;
import com.doris.nflow.engine.common.constant.NodePropertyConstant;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.common.model.node.event.EndEvent;
import com.doris.nflow.engine.common.model.node.event.StartEvent;
import com.doris.nflow.engine.common.model.node.flow.SequenceFlow;
import com.doris.nflow.engine.common.model.node.gateway.GatewayNode;
import com.doris.nflow.engine.common.model.node.task.ScriptTask;
import com.doris.nflow.engine.common.model.node.task.ServiceTask;
import com.doris.nflow.engine.common.model.node.task.UserTask;
import com.doris.nflow.engine.executor.enumerate.HttpMethodType;
import com.doris.nflow.engine.processor.DefinitionProcessor;
import com.doris.nflow.engine.processor.model.param.CreateFlowParam;
import com.doris.nflow.engine.processor.model.param.DeployFlowParam;
import com.doris.nflow.engine.processor.model.result.CreateFlowResult;
import com.doris.nflow.engine.processor.model.result.DeployFlowResult;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: xhz
 * @Title: com.doris.nflow.test.flow.FlowTest
 * @Description:
 * @date: 2022/10/9 14:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class FlowTest {

    @Autowired
    private DefinitionProcessor definitionProcessor;

    @Test
    public void addFlow(){

        CreateFlowParam createFlowParam = new CreateFlowParam();
        createFlowParam.setFlowName("请假流程");
        createFlowParam.setFlowModule(getFlowModule());
        CreateFlowResult createFlowResult = definitionProcessor.create(createFlowParam);
        System.out.println("createFlowResult = " + createFlowResult);
    }

    /**
     * 服务编排的流程
     */
    @Test
    public void serviceFlow(){
        CreateFlowParam createFlowParam = new CreateFlowParam();
        createFlowParam.setFlowName("服务编排流程");
        createFlowParam.setFlowModule(getServiceFlowModule());
        CreateFlowResult createFlowResult = definitionProcessor.create(createFlowParam);
        System.out.println("createFlowResult = " + createFlowResult);
    }
//1f0c8cae-5b63-11ee-9260-a210dca18963
//b906b053-5b62-11ee-8015-a210dca18963
    @Test
    public void deployServiceFlow(){
        DeployFlowParam deployFlowParam = new DeployFlowParam();
        deployFlowParam.setFlowModuleCode("1f0c8cae-5b63-11ee-9260-a210dca18963");
        DeployFlowResult deploy = definitionProcessor.deploy(deployFlowParam);
        System.out.println("deploy = " + deploy);
    }



    private List<BaseNode> getServiceFlowModule(){
        List<BaseNode> baseNodes = new ArrayList<>();
        StartEvent startEvent = new StartEvent();
        startEvent.setCode("start");
        startEvent.setName("开始事件");
        startEvent.setNodeType(NodeType.START_EVENT_NODE.getCode());
        startEvent.setOutput(Lists.newArrayList("sequence1"));

        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setCode("sequence1");
        sequenceFlow.setName("sequence1");
        sequenceFlow.setInput(Lists.newArrayList("start"));
        sequenceFlow.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        sequenceFlow.setOutput(Lists.newArrayList("serviceTask"));

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setCode("serviceTask");
        serviceTask.setInput(Lists.newArrayList("sequence1"));
        serviceTask.setName("获取请求API列表");
        serviceTask.setNodeType(NodeType.SERVICE_TASK_NODE.getCode());
        serviceTask.setOutput(Lists.newArrayList("sequence2"));
        serviceTask.setUrl("http://192.168.1.204:8081/datacube/api/info/list?projectId=0&pageSize=10&pageNo=1");
        serviceTask.setMethodType(HttpMethodType.GET.getCode());

        SequenceFlow sequenceFlow2 = new SequenceFlow();
        sequenceFlow2.setCode("sequence2");
        sequenceFlow2.setName("sequence2");
        sequenceFlow2.setInput(Lists.newArrayList("serviceTask"));
        sequenceFlow2.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        sequenceFlow2.setOutput(Lists.newArrayList("script"));


        ScriptTask scriptTask = new ScriptTask();
        scriptTask.setCode("script");
        scriptTask.setName("groovy脚本处理");
        scriptTask.setNodeType(NodeType.SCRIPT_TASK_NODE.getCode());
        scriptTask.setScript("def map = new HashMap<String, Object>()\n" +
                "        map.put(\"size\",new groovy.json.JsonSlurper().parseText(serviceTask.get()).data.list.size())\n" +
                "        return map");
        scriptTask.setInput(Lists.newArrayList("sequence2"));
        scriptTask.setOutput(Lists.newArrayList("sequence3"));
        scriptTask.setScriptType(ExpressionTypeConstant.GROOVY);


        SequenceFlow sequenceFlow3 = new SequenceFlow();
        sequenceFlow3.setCode("sequence3");
        sequenceFlow3.setName("sequence3");
        sequenceFlow3.setInput(Lists.newArrayList("script"));
        sequenceFlow3.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        sequenceFlow3.setOutput(Lists.newArrayList("end"));

        EndEvent endEvent = new EndEvent();
        endEvent.setCode("end");
        endEvent.setInput(Lists.newArrayList("sequence2"));
        endEvent.setNodeType(NodeType.END_EVENT_NODE.getCode());
        endEvent.setName("结束");

        baseNodes.add(startEvent);
        baseNodes.add(sequenceFlow);
        baseNodes.add(serviceTask);
        baseNodes.add(sequenceFlow2);
        baseNodes.add(scriptTask);
        baseNodes.add(sequenceFlow3);
        baseNodes.add(endEvent);
        return baseNodes;
    }



    @Test
    public void deployFlow(){
        DeployFlowParam deployFlowParam = new DeployFlowParam();
        deployFlowParam.setFlowModuleCode("b01f6e55-4840-11ed-8851-c69bf65f9b74");
        DeployFlowResult deploy = definitionProcessor.deploy(deployFlowParam);
        System.out.println("deploy = " + deploy);
    }

    private List<BaseNode> getFlowModule(){
        List<BaseNode> baseNodes = new ArrayList<>();
        StartEvent startEvent = new StartEvent();
        startEvent.setCode("start");
        startEvent.setName("开始事件");
        startEvent.setNodeType(NodeType.START_EVENT_NODE.getCode());
        startEvent.setOutput(Lists.newArrayList("sequence1"));

        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setCode("sequence1");
        sequenceFlow.setName("sequence1");
        sequenceFlow.setInput(Lists.newArrayList("start"));
        sequenceFlow.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        sequenceFlow.setOutput(Lists.newArrayList("userTask1"));

        UserTask userTask = new UserTask();
        userTask.setCode("userTask1");
        userTask.setInput(Lists.newArrayList("sequence1"));
        userTask.setName("用户提交请假天数");
        userTask.setNodeType(NodeType.USER_TASK_NODE.getCode());
        userTask.setOutput(Lists.newArrayList("sequence2"));

        SequenceFlow sequenceFlow2 = new SequenceFlow();
        sequenceFlow2.setCode("sequence2");
        sequenceFlow2.setName("sequence2");
        sequenceFlow2.setInput(Lists.newArrayList("userTask1"));
        sequenceFlow2.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        sequenceFlow2.setOutput(Lists.newArrayList("gateway1"));


        GatewayNode gatewayNode = new GatewayNode();
        gatewayNode.setCode("gateway1");
        gatewayNode.setName("网关");
        gatewayNode.setNodeType(NodeType.EXCLUSIVE_GATEWAY_NODE.getCode());
        gatewayNode.setInput(Lists.newArrayList("sequence2"));
        gatewayNode.setOutput(Lists.newArrayList("sequence3","sequence4"));


        SequenceFlow sequenceFlow3 = new SequenceFlow();
        sequenceFlow3.setCode("sequence3");
        sequenceFlow3.setName("sequence3");
        sequenceFlow3.setInput(Lists.newArrayList("gateway1"));
        sequenceFlow3.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        HashMap<String, Object> sequenceFlow3Param = new HashMap<>();
        sequenceFlow3Param.put(NodePropertyConstant.CONDITION,"day>3");
        sequenceFlow3.setProperties(sequenceFlow3Param);
        sequenceFlow3.setOutput(Lists.newArrayList("userTask2"));

        SequenceFlow sequenceFlow4 = new SequenceFlow();
        sequenceFlow4.setCode("sequence4");
        sequenceFlow4.setName("sequence4");
        sequenceFlow4.setInput(Lists.newArrayList("gateway1"));
        sequenceFlow4.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        HashMap<String, Object> sequenceFlow4Param = new HashMap<>();
        sequenceFlow4Param.put(NodePropertyConstant.CONDITION,"day<3");
        sequenceFlow4.setProperties(sequenceFlow4Param);
        sequenceFlow4.setOutput(Lists.newArrayList("userTask3"));

        UserTask userTask2 = new UserTask();
        userTask2.setCode("userTask2");
        userTask2.setInput(Lists.newArrayList("sequence3"));
        userTask2.setName("二级领导审批");
        userTask2.setNodeType(NodeType.USER_TASK_NODE.getCode());
        userTask2.setOutput(Lists.newArrayList("sequence5"));

        UserTask userTask3 = new UserTask();
        userTask3.setCode("userTask3");
        userTask3.setInput(Lists.newArrayList("sequence4"));
        userTask3.setName("一级领导审批");
        userTask3.setNodeType(NodeType.USER_TASK_NODE.getCode());
        userTask3.setOutput(Lists.newArrayList("sequence6"));


        SequenceFlow sequenceFlow5 = new SequenceFlow();
        sequenceFlow5.setCode("sequence5");
        sequenceFlow5.setName("sequence5");
        sequenceFlow5.setInput(Lists.newArrayList("userTask2"));
        sequenceFlow5.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        sequenceFlow5.setOutput(Lists.newArrayList("end"));

        SequenceFlow sequenceFlow6 = new SequenceFlow();
        sequenceFlow6.setCode("sequence6");
        sequenceFlow6.setName("sequence6");
        sequenceFlow6.setInput(Lists.newArrayList("userTask3"));
        sequenceFlow6.setNodeType(NodeType.SEQUENCE_FLOW_NODE.getCode());
        sequenceFlow6.setOutput(Lists.newArrayList("end"));

        EndEvent endEvent = new EndEvent();
        endEvent.setCode("end");
        endEvent.setInput(Lists.newArrayList("sequence5", "sequence6"));
        endEvent.setNodeType(NodeType.END_EVENT_NODE.getCode());
        endEvent.setName("结束");

        baseNodes.add(startEvent);
        baseNodes.add(sequenceFlow);
        baseNodes.add(userTask);
        baseNodes.add(sequenceFlow2);
        baseNodes.add(gatewayNode);
        baseNodes.add(sequenceFlow3);
        baseNodes.add(sequenceFlow4);
        baseNodes.add(userTask2);
        baseNodes.add(userTask3);
        baseNodes.add(sequenceFlow5);
        baseNodes.add(sequenceFlow6);
        baseNodes.add(endEvent);
        return baseNodes;
    }
}
