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
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.processor.model.flow.FlowInfo;
import com.doris.nflow.engine.processor.model.param.CommitTaskParam;
import com.doris.nflow.engine.processor.model.param.RollbackTaskParam;
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
 * @author: origindoris
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

    private final FlowInstanceService flowInstanceService;

    private final FlowExecutor flowExecutor;



    public RuntimeProcessor(FlowDeploymentService flowDeploymentService, FlowInstanceService flowInstanceService, FlowExecutor flowExecutor) {
        this.flowDeploymentService = flowDeploymentService;
        this.flowInstanceService = flowInstanceService;
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


    public CommitTaskResult commit(CommitTaskParam commitTaskParam) {
        RuntimeContext runtimeContext = null;
        try {
            Optional<FlowInstance> flowInstanceOptional = flowInstanceService.detail(commitTaskParam.getFlowInstanceCode());
            if (flowInstanceOptional.isEmpty()) {
                log.warn("commit failed: cannot find flowInstanceBO.||flowInstanceCode={}", commitTaskParam.getFlowInstanceCode());
                throw new ProcessException(ErrorCode.GET_FLOW_INSTANCE_FAILED);
            }
            FlowInstance flowInstance = flowInstanceOptional.get();

            if (Objects.equals(flowInstance.getStatus(), FlowInstanceStatus.TERMINATION.getCode())) {
                log.warn("commit failed: flowInstance has been completed.||commitTaskParam={}", commitTaskParam);
                throw new ProcessException(ErrorCode.COMMIT_REJECTRD);
            }
            if (Objects.equals(flowInstance.getStatus(), FlowInstanceStatus.COMPLETE.getCode())) {
                log.warn("commit: reentrant process.||commitTaskParam={}", commitTaskParam);
                throw new ReentrantException(ErrorCode.REENTRANT_WARNING);
            }
            String flowDeployCode = flowInstance.getFlowDeployCode();
            FlowInfo flowInfo = getFlowInfoByFlowDeployCode(flowDeployCode);
            runtimeContext = buildRuntimeContext(flowInfo, commitTaskParam.getParams());
            runtimeContext.setFlowInstanceCode(commitTaskParam.getFlowInstanceCode());
            runtimeContext.setFlowInstanceStatus(flowInstance.getStatus());
            NodeInstance suspendNodeInstance = new NodeInstance();
            suspendNodeInstance.setNodeInstanceCode(commitTaskParam.getNodeInstanceCode());
            runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

            flowExecutor.commit(runtimeContext);

            return buildCommitTaskResult(runtimeContext);
        } catch (NFlowException e) {
            if (!ErrorCode.isSuccess(e.getErrorCode())) {
                log.warn("commit ProcessException.||commitTaskParam={}||runtimeContext={}, ", commitTaskParam, runtimeContext, e);
            }
            return buildCommitTaskResult(runtimeContext, e);
        }
    }

    private CommitTaskResult buildCommitTaskResult(RuntimeContext runtimeContext) {
        CommitTaskResult commitTaskResult = new CommitTaskResult();
        return (CommitTaskResult) fillRuntimeResult(commitTaskResult, runtimeContext, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMsg());
    }

    private CommitTaskResult buildCommitTaskResult(RuntimeContext runtimeContext, NFlowException e) {
        CommitTaskResult commitTaskResult = new CommitTaskResult();
        return (CommitTaskResult) fillRuntimeResult(commitTaskResult, runtimeContext, e.getErrorCode(), e.getErrorMsg());
    }

    private Optional<FlowInstance> getFlowInstance(String flowInstanceCode) {
        return flowInstanceService.detail(flowInstanceCode);
    }


    public RollbackTaskResult rollback(@Valid @NotNull(message = "回滚任务参数不能为空！") RollbackTaskParam rollbackTaskParam) {
        RuntimeContext runtimeContext = null;
        try {
            Optional<FlowInstance> flowInstanceOptional = getFlowInstance(rollbackTaskParam.getFlowInstanceCode());
            if (flowInstanceOptional.isEmpty()) {
                log.warn("rollback failed: flowInstance is null.||flowInstanceCode={}", rollbackTaskParam.getFlowInstanceCode());
                throw new ProcessException(ErrorCode.GET_FLOW_INSTANCE_FAILED);
            }

            FlowInstance flowInstance = flowInstanceOptional.get();

            //3.check status
            if (!Objects.equals(flowInstance.getStatus(), FlowInstanceStatus.PROCESSING.getCode())) {
                log.warn("rollback failed: invalid status to rollback.||rollbackTaskParam={}||status={}",
                        rollbackTaskParam, flowInstance.getStatus());
                throw new ProcessException(ErrorCode.ROLLBACK_REJECTRD);
            }
            String flowDeployCode = flowInstance.getFlowDeployCode();

            FlowInfo flowInfo = getFlowInfoByFlowDeployCode(flowDeployCode);

            runtimeContext = buildRollbackContext(rollbackTaskParam, flowInfo, flowInstance.getStatus());

            //6.process
            flowExecutor.rollback(runtimeContext);

            //7.build result
            return buildRollbackTaskResult(runtimeContext);
        } catch (NFlowException e) {
            if (!ErrorCode.isSuccess(e.getErrorCode())) {
                log.warn("rollback ProcessException.||rollbackTaskParam={}||runtimeContext={}, ", rollbackTaskParam, runtimeContext, e);
            }
            return buildRollbackTaskResult(runtimeContext, e);
        }
    }


    private RuntimeContext buildRollbackContext(RollbackTaskParam rollbackTaskParam, FlowInfo flowInfo, String flowInstanceStatus) {
        RuntimeContext runtimeContext = buildRuntimeContext(flowInfo);

        runtimeContext.setFlowInstanceCode(rollbackTaskParam.getFlowInstanceCode());
        runtimeContext.setFlowInstanceStatus(flowInstanceStatus);

        NodeInstance suspendNodeInstance = new NodeInstance();
        suspendNodeInstance.setNodeInstanceCode(rollbackTaskParam.getNodeInstanceCode());
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        return runtimeContext;
    }

    private RuntimeContext buildRuntimeContext(FlowInfo flowInfo) {
        RuntimeContext runtimeContext = new RuntimeContext();
        BeanUtils.copyProperties(flowInfo, runtimeContext);
        runtimeContext.setBaseNodeMap(BaseNodeUtil.getBaseNodeMap(flowInfo.getFlowModule()));
        return runtimeContext;
    }

    private RollbackTaskResult buildRollbackTaskResult(RuntimeContext runtimeContext) {
        RollbackTaskResult rollbackTaskResult = new RollbackTaskResult();
        return (RollbackTaskResult) fillRuntimeResult(rollbackTaskResult, runtimeContext, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMsg());
    }

    private RollbackTaskResult buildRollbackTaskResult(RuntimeContext runtimeContext, NFlowException e) {
        RollbackTaskResult rollbackTaskResult = new RollbackTaskResult();
        return (RollbackTaskResult) fillRuntimeResult(rollbackTaskResult, runtimeContext, e.getErrorCode(), e.getErrorMsg());
    }


    public TerminateResult terminateProcess(String flowInstanceCode) {
        TerminateResult terminateResult;
        try {
            String flowInstanceStatus;

            Optional<FlowInstance> optional = flowInstanceService.detail(flowInstanceCode);
            if (optional.isEmpty()) {
                log.warn("terminate failed: flowInstance is null.||flowInstanceCode={}", flowInstanceCode);
                throw new ProcessException(ErrorCode.GET_FLOW_INSTANCE_FAILED);
            }
            FlowInstance flowInstance = optional.get();
            if (Objects.equals(flowInstance.getStatus(), FlowInstanceStatus.COMPLETE.getCode())) {
                log.warn("terminateProcess: flowInstance is completed.||flowInstanceCode={}", flowInstanceCode);
                flowInstanceStatus = FlowInstanceStatus.COMPLETE.getCode();
            } else {
                flowInstanceService.modifyStatus(FlowInstanceStatus.TERMINATION, flowInstanceCode);
                flowInstanceStatus = FlowInstanceStatus.TERMINATION.getCode();
            }

            terminateResult = new TerminateResult();
            terminateResult.setFlowInstanceCode(flowInstanceCode);
            terminateResult.setStatus(flowInstanceStatus);
        } catch (Exception e) {
            log.error("terminateProcess exception.||flowInstanceCode={}, ", flowInstanceCode, e);
            terminateResult = new TerminateResult();
            terminateResult.setFlowInstanceCode(flowInstanceCode);
            terminateResult.setErrorCode(ErrorCode.SYSTEM_ERROR.getCode());
            terminateResult.setErrorMsg(ErrorCode.SYSTEM_ERROR.getMsg());
        }
        return terminateResult;
    }

}
