package com.doris.nflow.engine.processor;

import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.NFlowException;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.exception.ReentrantException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.executor.FlowExecutor;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.service.FlowDeploymentService;
import com.doris.nflow.engine.flow.instance.enumerate.FlowInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.FlowInstance;
import com.doris.nflow.engine.flow.instance.service.FlowInstanceService;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.processor.model.flow.FlowInfo;
import com.doris.nflow.engine.processor.model.param.CommitTaskParam;
import com.doris.nflow.engine.processor.model.param.StartProcessorParam;
import com.doris.nflow.engine.processor.model.result.*;
import com.doris.nflow.engine.util.BaseNodeUtil;
import com.doris.nflow.engine.util.InstanceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: RuntimeProcessor
 * @Description:
 * @date: 2022/10/9 09:56
 */
@Component
@Slf4j
@Validated
@Transactional
public class RuntimeProcessor {

    private final FlowDeploymentService flowDeploymentService;


    private final FlowExecutor flowExecutor;



    public RuntimeProcessor(FlowDeploymentService flowDeploymentService,FlowExecutor flowExecutor) {
        this.flowDeploymentService = flowDeploymentService;
        this.flowExecutor = flowExecutor;
    }


    public StartProcessorResult startProcess(@Valid @NotNull(message = "发起流程参数不能为空") StartProcessorParam startProcessParam) {
        RuntimeContext runtimeContext = null;
        try {
            FlowInfo flowInfo = getFlowInfo(startProcessParam);
            runtimeContext = buildRuntimeContext(flowInfo, startProcessParam.getParams());
            flowExecutor.execute(runtimeContext);
            return buildStartProcessResult(runtimeContext);
        } catch (NFlowException e) {
            if (!ErrorCode.isSuccess(e.getErrorCode())) {
                log.warn("startProcess ProcessException.||startProcessParam={}||runtimeContext={}, ",
                        startProcessParam, runtimeContext, e);
            }
            return buildStartProcessResult(runtimeContext, e);
        }
    }

    private StartProcessorResult buildStartProcessResult(RuntimeContext runtimeContext, NFlowException e) {
        StartProcessorResult startProcessResult = new StartProcessorResult();
        BeanUtils.copyProperties(runtimeContext, startProcessResult);
        return (StartProcessorResult) fillRuntimeResult(startProcessResult, runtimeContext, e.getErrorCode(), e.getErrorMsg());
    }

    private StartProcessorResult buildStartProcessResult(RuntimeContext runtimeContext) {
        StartProcessorResult startProcessResult = new StartProcessorResult();
        BeanUtils.copyProperties(runtimeContext, startProcessResult);
        return (StartProcessorResult) fillRuntimeResult(startProcessResult, runtimeContext, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMsg());
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext, Integer errNo, String errMsg) {
        runtimeResult.setErrorCode(errNo);
        runtimeResult.setErrorMsg(errMsg);

        if (runtimeContext != null) {
            runtimeResult.setFlowInstanceCode(runtimeContext.getFlowInstanceCode());
            runtimeResult.setStatus(runtimeContext.getFlowInstanceStatus());
            runtimeResult.setActiveTaskInstance(buildActiveTaskInstance(runtimeContext.getSuspendNodeInstance(), runtimeContext));
            runtimeResult.setParams(InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap()));
        }
        return runtimeResult;
    }

    private NodeInstance buildActiveTaskInstance(NodeInstance nodeInstance, RuntimeContext runtimeContext) {
        NodeInstance activeNodeInstance = new NodeInstance();
        BeanUtils.copyProperties(nodeInstance, activeNodeInstance);
        activeNodeInstance.setNodeCode(nodeInstance.getNodeCode());
        BaseNode baseNode =  runtimeContext.getBaseNodeMap().get(nodeInstance.getNodeCode());
        activeNodeInstance.setNodeName(baseNode.getName());
        activeNodeInstance.setProperties(baseNode.getProperties());

        return activeNodeInstance;
    }

    private FlowInfo getFlowInfo(StartProcessorParam startProcessorParam) throws ProcessException {
        String flowDeployCode = startProcessorParam.getFlowDeployCode();
        return getFlowInfoByFlowDeployCode(flowDeployCode);
    }


    private FlowInfo getFlowInfoByFlowDeployCode(String flowDeployCode) throws ProcessException {
        Optional<FlowDeployment> optional = flowDeploymentService.detail(flowDeployCode);
        if (optional.isEmpty()) {
            throw new ProcessException(ErrorCode.GET_FLOW_DEPLOYMENT_FAILED);
        }
        FlowDeployment flowDeployment = optional.get();
        FlowInfo flowInfo = new FlowInfo();
        BeanUtils.copyProperties(flowDeployment, flowInfo);
        return flowInfo;
    }


    private RuntimeContext buildRuntimeContext(FlowInfo flowInfo, List<InstanceData> params){
        RuntimeContext runtimeContext = new RuntimeContext();
        BeanUtils.copyProperties(flowInfo, runtimeContext);
        runtimeContext.setBaseNodeMap(BaseNodeUtil.getBaseNodeMap(flowInfo.getFlowModule()));
        runtimeContext.setInstanceDataMap(InstanceDataUtil.getInstanceDataMap(params));
        return runtimeContext;
    }
}
