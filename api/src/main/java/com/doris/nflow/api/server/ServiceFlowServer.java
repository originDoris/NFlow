package com.doris.nflow.api.server;

import com.doris.nflow.api.util.BuildModuleUtil;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.processor.DefinitionProcessor;
import com.doris.nflow.engine.processor.RuntimeProcessor;
import com.doris.nflow.engine.processor.model.param.CreateFlowParam;
import com.doris.nflow.engine.processor.model.param.DeployFlowParam;
import com.doris.nflow.engine.processor.model.param.StartProcessorParam;
import com.doris.nflow.engine.processor.model.result.CommitTaskResult;
import com.doris.nflow.engine.processor.model.result.CreateFlowResult;
import com.doris.nflow.engine.processor.model.result.DeployFlowResult;
import com.doris.nflow.engine.processor.model.result.StartProcessorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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


    public List<InstanceData> run(){
        CreateFlowResult createFlowResult = addFlow();

        DeployFlowResult deployFlowResult = deployFlow(createFlowResult.getFlowModuleCode());

        StartProcessorResult startProcessorResult = startProcessTest(deployFlowResult.getFlowDeployCode(), deployFlowResult.getFlowModuleCode());

        return startProcessorResult.getParams();
    }


    public DeployFlowResult addFlow(CreateFlowParam createFlowParam){
        CreateFlowResult createFlowResult = definitionProcessor.create(createFlowParam);
        DeployFlowParam deployFlowParam = new DeployFlowParam();
        deployFlowParam.setFlowModuleCode(createFlowResult.getFlowModuleCode());
        return definitionProcessor.deploy(deployFlowParam);
    }



    public List<InstanceData> run(String flowDeployCode,String flowModuleCode){
        StartProcessorResult startProcessorResult = startProcessTest(flowDeployCode, flowModuleCode);
        return startProcessorResult.getParams();
    }

    private CreateFlowResult addFlow(){

        CreateFlowParam createFlowParam = new CreateFlowParam();
        createFlowParam.setFlowName("服务编排");
        createFlowParam.setFlowModule(BuildModuleUtil.getServiceFlowModule());
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

}
