package com.doris.nflow.api.controller;

import com.doris.nflow.api.param.ExecutorFlowRequest;
import com.doris.nflow.api.param.ExecutorNodeRequest;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.executor.RuntimeExecutor;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.processor.RuntimeProcessor;
import com.doris.nflow.engine.processor.model.param.StartProcessorParam;
import com.doris.nflow.engine.processor.model.result.StartProcessorResult;
import com.doris.nflow.engine.util.BuildModuleUtil;
import com.doris.nflow.engine.util.InstanceDataUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: ExecutorController
 * @Description:
 * @date: 2023/10/11 16:30
 */
@RestController
@RequestMapping("/executor")
public class ExecutorController {


    @Autowired
    private RuntimeProcessor runtimeProcessor;
    @PostMapping("/executorNode")
    public StartProcessorResult executorNode(@RequestBody ExecutorNodeRequest executorNodeRequest){
        return runtimeProcessor.executorBaseNode(executorNodeRequest.getBaseNode(), executorNodeRequest.getFlowModuleCode(), executorNodeRequest.getParams());
    }



    @PostMapping("/executorFlow")
    public StartProcessorResult executor(@RequestBody ExecutorFlowRequest request) throws ParamException {
        Map<String, Object> param = BuildModuleUtil.parseFlowParam(request.getContent());
        List<BaseNode> baseNodes = BuildModuleUtil.buildNode(request.getContent());
        StartProcessorParam startProcessorParam = new StartProcessorParam();
        startProcessorParam.setParams(InstanceDataUtil.paresInstanceData(param));
        startProcessorParam.setFlowDeployCode(request.getFlowDeployCode());
        startProcessorParam.setFlowModuleCode(request.getFlowModuleCode());
        startProcessorParam.setFlowModule(baseNodes);
        startProcessorParam.setWebSocketKey(request.getWebSocketKey());
        return runtimeProcessor.startProcess(startProcessorParam);
    }


}
