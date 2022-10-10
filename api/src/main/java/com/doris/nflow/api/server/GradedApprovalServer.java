package com.doris.nflow.api.server;

import com.doris.nflow.api.util.BuildModuleUtil;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.processor.DefinitionProcessor;
import com.doris.nflow.engine.processor.RuntimeProcessor;
import com.doris.nflow.engine.processor.model.param.CommitTaskParam;
import com.doris.nflow.engine.processor.model.param.CreateFlowParam;
import com.doris.nflow.engine.processor.model.param.DeployFlowParam;
import com.doris.nflow.engine.processor.model.param.StartProcessorParam;
import com.doris.nflow.engine.processor.model.result.CommitTaskResult;
import com.doris.nflow.engine.processor.model.result.CreateFlowResult;
import com.doris.nflow.engine.processor.model.result.DeployFlowResult;
import com.doris.nflow.engine.processor.model.result.StartProcessorResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: origindoris
 * @Title: GradedApprovalServer
 * @Description:
 * @date: 2022/10/10 16:05
 */
@Component
@Slf4j
public class GradedApprovalServer {


    private final DefinitionProcessor definitionProcessor;

    private final RuntimeProcessor runtimeProcessor;

    public GradedApprovalServer(DefinitionProcessor definitionProcessor, RuntimeProcessor runtimeProcessor) {
        this.definitionProcessor = definitionProcessor;
        this.runtimeProcessor = runtimeProcessor;
    }


    public CommitTaskResult run(){
        CreateFlowResult createFlowResult = addFlow();

        DeployFlowResult deployFlowResult = deployFlow(createFlowResult.getFlowModuleCode());

        StartProcessorResult startProcessorResult = startProcessTest(deployFlowResult.getFlowDeployCode(), deployFlowResult.getFlowModuleCode());

        return commitProcess(startProcessorResult.getFlowInstanceCode(), startProcessorResult.getActiveTaskInstance().getNodeInstanceCode());
    }



    private CreateFlowResult addFlow(){

        CreateFlowParam createFlowParam = new CreateFlowParam();
        createFlowParam.setFlowName("请假流程");
        createFlowParam.setFlowModule(BuildModuleUtil.getGradedApproval());
        return definitionProcessor.create(createFlowParam);
    }

    private DeployFlowResult deployFlow(String flowModuleCode){
        DeployFlowParam deployFlowParam = new DeployFlowParam();
        deployFlowParam.setFlowModuleCode(flowModuleCode);
        return definitionProcessor.deploy(deployFlowParam);
    }

    private StartProcessorResult startProcessTest(String flowDeployCode,String flowModuleCode){
        StartProcessorParam startProcessorParam = new StartProcessorParam();
        startProcessorParam.setFlowDeployCode(flowDeployCode);
        startProcessorParam.setFlowModuleCode(flowModuleCode);
        return runtimeProcessor.startProcess(startProcessorParam);
    }


    private CommitTaskResult commitProcess(String flowInstanceCode, String nodeInstanceCode) {
        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setFlowInstanceCode(flowInstanceCode);
        commitTaskParam.setNodeInstanceCode(nodeInstanceCode);
        InstanceData instanceData = new InstanceData();
        instanceData.setType("Integer");
        instanceData.setKey("day");
        instanceData.setValue(2);
        commitTaskParam.setParams(Lists.newArrayList(instanceData));
        return runtimeProcessor.commit(commitTaskParam);
    }
}
