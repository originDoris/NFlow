package com.doris.nflow.api.server;

import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.processor.DefinitionProcessor;
import com.doris.nflow.engine.processor.RuntimeProcessor;
import com.doris.nflow.engine.processor.model.param.*;
import com.doris.nflow.engine.processor.model.result.CreateFlowResult;
import com.doris.nflow.engine.processor.model.result.DeployFlowResult;
import com.doris.nflow.engine.processor.model.result.StartProcessorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author: xhz
 * @Title: GradedApprovalServer
 * @Description:
 * @date: 2022/10/10 16:05
 */
@Component
@Slf4j
public class ServiceFlowServer {


    private final DefinitionProcessor definitionProcessor;

    private final RuntimeProcessor runtimeProcessor;

    protected final NodeInstanceDataService nodeInstanceDataService;

    public ServiceFlowServer(DefinitionProcessor definitionProcessor, RuntimeProcessor runtimeProcessor, NodeInstanceDataService nodeInstanceDataService) {
        this.definitionProcessor = definitionProcessor;
        this.runtimeProcessor = runtimeProcessor;
        this.nodeInstanceDataService = nodeInstanceDataService;
    }



    public DeployFlowResult addFlow(CreateFlowParam createFlowParam){
        CreateFlowResult createFlowResult = definitionProcessor.create(createFlowParam);
        DeployFlowParam deployFlowParam = new DeployFlowParam();
        deployFlowParam.setFlowModuleCode(createFlowResult.getFlowModuleCode());
        return definitionProcessor.deploy(deployFlowParam);
    }


    public void modifyFlow(ModifyFlowParam modifyFlowParam){
        String flowModuleCode = modifyFlowParam.getFlowModuleCode();
        definitionProcessor.modify(modifyFlowParam);

    }



    public List<InstanceData> run(String flowDeployCode,String flowModuleCode){
        StartProcessorResult startProcessorResult = startProcessTest(flowDeployCode, flowModuleCode,null);
        return startProcessorResult.getParams();
    }

    public List<InstanceData> run(String flowDeployCode, String flowModuleCode, List<InstanceData> params) {
        StartProcessorResult startProcessorResult = startProcessTest(flowDeployCode, flowModuleCode, params);
        return startProcessorResult.getParams();
    }


    private DeployFlowResult deployFlow(String flowModuleCode){
        DeployFlowParam deployFlowParam = new DeployFlowParam();
        deployFlowParam.setFlowModuleCode(flowModuleCode);
        return definitionProcessor.deploy(deployFlowParam);
    }

    private StartProcessorResult startProcessTest(String flowDeployCode, String flowModuleCode, List<InstanceData> params) {
        StartProcessorParam startProcessorParam = new StartProcessorParam();
        startProcessorParam.setFlowDeployCode(flowDeployCode);
        startProcessorParam.setFlowModuleCode(flowModuleCode);
        startProcessorParam.setParams(params);
        return runtimeProcessor.startProcess(startProcessorParam);
    }

}
