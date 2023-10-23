package com.doris.nflow.engine.processor;

import com.doris.nflow.engine.common.constant.WebSocketMessageConstant;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.NFlowException;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.executor.FlowExecutor;
import com.doris.nflow.engine.executor.RuntimeExecutor;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.service.FlowDeploymentService;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.processor.model.flow.FlowInfo;
import com.doris.nflow.engine.processor.model.param.StartProcessorParam;
import com.doris.nflow.engine.processor.model.result.*;
import com.doris.nflow.engine.util.BaseNodeUtil;
import com.doris.nflow.engine.util.InstanceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
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

    private final ExecutorContext executorContext;



    public RuntimeProcessor(FlowDeploymentService flowDeploymentService, FlowExecutor flowExecutor, ExecutorContext executorContext) {
        this.flowDeploymentService = flowDeploymentService;
        this.flowExecutor = flowExecutor;
        this.executorContext = executorContext;
    }


    public StartProcessorResult executorBaseNode(BaseNode baseNode, String flowModuleCode, List<InstanceData> params){
        String nodeType = baseNode.getNodeType();
        RuntimeContext runtimeContext = new RuntimeContext();
        runtimeContext.setCurrentNodeModel(baseNode);
        runtimeContext.setFlowModuleCode(flowModuleCode);
        runtimeContext.setVariables(InstanceDataUtil.parseVariables(params));
        RuntimeExecutor runtimeExecutor = executorContext.getRuntimeExecutor(nodeType);
        try {
            runtimeExecutor.execute(runtimeContext);
            return buildStartProcessResult(runtimeContext);
        } catch (ProcessException e) {
            log.info("执行单个节点出现异常:", e);
            throw new RuntimeException(e);
        }
    }


    public StartProcessorResult startProcess(@Valid @NotNull(message = "发起流程参数不能为空") StartProcessorParam startProcessParam) {
        RuntimeContext runtimeContext = null;
        try {
            FlowInfo flowInfo = getFlowInfo(startProcessParam);
            runtimeContext = buildRuntimeContext(flowInfo, startProcessParam.getParams(), startProcessParam.getWebSocketKey());
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
        startProcessResult.setResult(runtimeContext.getVariables());
        return (StartProcessorResult) fillRuntimeResult(startProcessResult, runtimeContext, e.getErrorCode(), e.getErrorMsg());
    }

    private StartProcessorResult buildStartProcessResult(RuntimeContext runtimeContext) {
        StartProcessorResult startProcessResult = new StartProcessorResult();
        BeanUtils.copyProperties(runtimeContext, startProcessResult);
        startProcessResult.setResult(runtimeContext.getVariables());
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

        if (StringUtils.isBlank(flowDeployCode)) {
            FlowInfo flowInfo = new FlowInfo();
            flowInfo.setFlowDeployCode(startProcessorParam.getWebSocketKey());
            flowInfo.setFlowModuleCode(startProcessorParam.getWebSocketKey());
            flowInfo.setFlowModule(startProcessorParam.getFlowModule());
            return flowInfo;
        }
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


    private RuntimeContext buildRuntimeContext(FlowInfo flowInfo, List<InstanceData> params,String webSocketKey){
        RuntimeContext runtimeContext = new RuntimeContext();
        BeanUtils.copyProperties(flowInfo, runtimeContext);
        runtimeContext.setBaseNodeMap(BaseNodeUtil.getBaseNodeMap(flowInfo.getFlowModule()));
        runtimeContext.setInstanceDataMap(InstanceDataUtil.getInstanceDataMap(params));
        runtimeContext.setVariables(InstanceDataUtil.parseVariables(params));
        runtimeContext.setWebSocketKey(webSocketKey);
        return runtimeContext;
    }
}
