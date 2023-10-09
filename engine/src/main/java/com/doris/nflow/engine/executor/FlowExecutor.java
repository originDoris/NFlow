package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.enumerate.ProcessStatus;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.exception.ReentrantException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.instance.enumerate.FlowInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.FlowInstance;
import com.doris.nflow.engine.flow.instance.service.FlowInstanceService;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceDataType;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceLogType;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstanceLog;
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceLogService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import com.doris.nflow.engine.util.BaseNodeUtil;
import com.doris.nflow.engine.util.IdGenerator;
import com.doris.nflow.engine.util.InstanceDataUtil;
import com.doris.nflow.engine.util.StrongUuidGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author: xhz
 * @Title: FlowExecutor
 * @Description:
 * @date: 2022/10/8 14:24
 */
@Component
@Slf4j
public class FlowExecutor extends BaseNodeExecutor {

    private final FlowInstanceService flowInstanceService;

    private final NodeInstanceDataService nodeInstanceDataService;

    private final ExecutorContext executorContext;

    private final NodeInstanceService nodeInstanceService;

    private final NodeInstanceLogService nodeInstanceLogService;

    private static final IdGenerator idGenerator = new StrongUuidGenerator();

    public FlowExecutor(FlowInstanceService flowInstanceService, NodeInstanceDataService nodeInstanceDataService, ExecutorContext executorContext, NodeInstanceService nodeInstanceService, NodeInstanceLogService nodeInstanceLogService) {
        this.flowInstanceService = flowInstanceService;
        this.nodeInstanceDataService = nodeInstanceDataService;
        this.executorContext = executorContext;
        this.nodeInstanceService = nodeInstanceService;
        this.nodeInstanceLogService = nodeInstanceLogService;
    }

    @Override
    public void execute(RuntimeContext runtimeContext) throws ProcessException {
        String processStatus = ProcessStatus.SUCCESS.getCode();
        try {
            preExecute(runtimeContext);
            doExecute(runtimeContext);
        } catch (ProcessException e) {
            if (!ErrorCode.isSuccess(e.getErrorCode())) {
                processStatus = ProcessStatus.FAILED.getCode();
            }
            throw e;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postExecute(runtimeContext, processStatus);
        }
    }

    private void preExecute(RuntimeContext runtimeContext) throws ProcessException {
        // 保存流程实例
        FlowInstance flowInstance = saveFlowInstance(runtimeContext);

        // 保存实例数据
        String instanceDataCode = saveFlowInstance(runtimeContext, flowInstance);

        // 补全RuntimeContext 找到start节点
        fillExecuteContext(runtimeContext, flowInstance.getFlowInstanceCode(), instanceDataCode);
    }

    private FlowInstance saveFlowInstance(RuntimeContext runtimeContext) throws ProcessException {
        FlowInstance flowInstance = new FlowInstance();
        BeanUtils.copyProperties(runtimeContext, flowInstance);
        flowInstance.setStatus(FlowInstanceStatus.PROCESSING.getCode());
        flowInstance.setFlowInstanceCode(idGenerator.getNextId());
        boolean result = flowInstanceService.save(flowInstance);
        if (result) {
            return flowInstance;
        }
        log.error("save flowInstance failed . flowInstance:{}", flowInstance);
        throw new ProcessException(ErrorCode.SAVE_FLOW_INSTANCE_FAILED);
    }

    private String saveFlowInstance(RuntimeContext runtimeContext, FlowInstance flowInstance) throws ProcessException {
        NodeInstanceData nodeInstanceData = new NodeInstanceData();
        BeanUtils.copyProperties(flowInstance, nodeInstanceData);

        Map<String, InstanceData> instanceDataMap = runtimeContext.getInstanceDataMap();
        if (MapUtils.isEmpty(instanceDataMap)) {
            nodeInstanceData.setInstanceData(null);
        }else{
            Collection<InstanceData> values = instanceDataMap.values();
            nodeInstanceData.setInstanceData(new ArrayList<>(values));
        }
        String nodeInstanceDataCode = idGenerator.getNextId();
        nodeInstanceData.setInstanceDataCode(nodeInstanceDataCode);
        nodeInstanceData.setType(NodeInstanceDataType.INIT.getCode());
        boolean result = nodeInstanceDataService.save(nodeInstanceData);
        if (result) {
            return nodeInstanceDataCode;
        }
        log.error("save nodeInstanceData failed . flowInstance:{}", flowInstance);
        throw new ProcessException(ErrorCode.SAVE_NODE_INSTANCE_DATA_FAILED);
    }


    private void fillExecuteContext(RuntimeContext runtimeContext, String flowInstanceCode, String instanceDataCode) throws ProcessException {
        runtimeContext.setFlowInstanceCode(flowInstanceCode);
        runtimeContext.setInstanceDataCode(instanceDataCode);
        runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.PROCESSING.getCode());
        runtimeContext.setNodeInstanceList(Lists.newArrayList());

        Map<String, BaseNode> baseNodeMap = runtimeContext.getBaseNodeMap();
        BaseNode startNode = BaseNodeUtil.getStartNode(baseNodeMap);
        if (startNode == null) {
            throw new ProcessException(ErrorCode.START_NODE_IS_NULL);
        }
        NodeInstance suspendNodeInstance = new NodeInstance();
        suspendNodeInstance.setNodeCode(startNode.getCode());
        suspendNodeInstance.setStatus(NodeInstanceStatus.PROCESSING.getCode());
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        runtimeContext.setCurrentNodeModel(startNode);
    }


    private void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        BaseNodeExecutor executeExecutor = getExecuteExecutor(runtimeContext);
        while (executeExecutor != null) {
            executeExecutor.execute(runtimeContext);
            executeExecutor = executeExecutor.getExecuteExecutor(runtimeContext);
        }
    }

    private void postExecute(RuntimeContext runtimeContext, String processStatus) throws ProcessException {
        if (Objects.equals(processStatus, ProcessStatus.SUCCESS.getCode())) {
            if (runtimeContext.getCurrentNodeInstance() != null) {
                runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
            }
        }

        saveNodeInstanceList(runtimeContext, NodeInstanceLogType.SYSTEM.getCode());

        if (isCompleted(runtimeContext)) {
            try {
                flowInstanceService.modifyStatus(FlowInstanceStatus.COMPLETE, runtimeContext.getFlowInstanceCode());
            } catch (ParamException e) {
                log.error("更新流程实例状态失败！", e);
                throw new ProcessException(e.getErrorCode(), e.getErrorMsg());
            }

            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETE.getCode());
            log.info("postExecute: flowInstance process completely.||flowInstanceCode={}", runtimeContext.getFlowInstanceCode());
        }
    }

    private void saveNodeInstanceList(RuntimeContext runtimeContext,String nodeInstanceLogType) {

        List<NodeInstance> processNodeList = runtimeContext.getNodeInstanceList();

        if (CollectionUtils.isEmpty(processNodeList)) {
            log.warn("saveNodeInstanceList: processNodeList is empty,||flowInstanceCode={}||nodeInstanceType={}",
                    runtimeContext.getFlowInstanceCode(), nodeInstanceLogType);
            return;
        }

        List<NodeInstance> nodeInstanceList = Lists.newArrayList();
        List<NodeInstanceLog> nodeInstanceLogList = Lists.newArrayList();

        processNodeList.forEach(instance -> {
            NodeInstance nodeInstance = buildNodeInstance(runtimeContext, instance);
            if (nodeInstance != null) {
                nodeInstanceList.add(nodeInstance);

                NodeInstanceLog nodeInstanceLog = new NodeInstanceLog();
                BeanUtils.copyProperties(nodeInstance, nodeInstanceLog);
                nodeInstanceLog.setType(nodeInstanceLogType);
                nodeInstanceLogList.add(nodeInstanceLog);
            }
        });
        nodeInstanceService.replace(nodeInstanceList);
        nodeInstanceLogService.replace(nodeInstanceLogList);
    }

    private NodeInstance buildNodeInstance(RuntimeContext runtimeContext, NodeInstance nodeInstance) {
        if (Objects.equals(runtimeContext.getProcessStatus(), ProcessStatus.FAILED.getCode())) {
            if (nodeInstance.getNodeCode().equals(runtimeContext.getSuspendNodeInstance().getNodeCode())) {
                return null;
            }
            nodeInstance.setStatus(NodeInstanceStatus.FAIL.getCode());
        }

        NodeInstance nodeInstanceResult = new NodeInstance();
        BeanUtils.copyProperties(nodeInstance, nodeInstanceResult);
        nodeInstanceResult.setFlowInstanceCode(runtimeContext.getFlowInstanceCode());
        nodeInstanceResult.setFlowDeployCode(runtimeContext.getFlowDeployCode());
        nodeInstanceResult.setCaller(runtimeContext.getCaller());
        nodeInstanceResult.setArchive(0);
        return nodeInstanceResult;
    }

    @Override
    public void commit(RuntimeContext runtimeContext) throws ProcessException {
        String processStatus = ProcessStatus.SUCCESS.getCode();
        try {
            preCommit(runtimeContext);
            doCommit(runtimeContext);
        } catch (ReentrantException re) {
            //ignore
        } catch (ProcessException pe) {
            if (!ErrorCode.isSuccess(pe.getErrorCode())) {
                processStatus = ProcessStatus.FAILED.getCode();
            }
            throw pe;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postCommit(runtimeContext);
        }
    }


    private void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceCode = runtimeContext.getInstanceDataCode();
        NodeInstance suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        String nodeInstanceCode = suspendNodeInstance.getNodeInstanceCode();

        Optional<NodeInstance> detail = nodeInstanceService.detail(nodeInstanceCode);

        if (detail.isEmpty()) {
            log.warn("preCommit failed: cannot find nodeInstancePO from db.||flowInstanceCode={}||nodeInstanceCode={}",
                    flowInstanceCode, nodeInstanceCode);
            throw new ProcessException(ErrorCode.GET_NODE_INSTANCE_FAILED);
        }
        NodeInstance nodeInstance = detail.get();

        if (isCompleted(runtimeContext)) {
            log.warn("preExecute warning: reentrant process. FlowInstance has been processed completely.||runtimeContext={}", runtimeContext);
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETE.getCode());
            suspendNodeInstance.setId(nodeInstance.getId());
            suspendNodeInstance.setNodeCode(nodeInstance.getNodeCode());
            suspendNodeInstance.setSourceNodeInstanceCode(nodeInstance.getSourceNodeInstanceCode());
            suspendNodeInstance.setSourceNodeCode(nodeInstance.getSourceNodeCode());
            suspendNodeInstance.setInstanceDataCode(nodeInstance.getInstanceDataCode());
            suspendNodeInstance.setStatus(nodeInstance.getStatus());
            throw new ReentrantException(ErrorCode.REENTRANT_WARNING);
        }
        Map<String, InstanceData> instanceDataMap;
        String instanceDataCode = nodeInstance.getInstanceDataCode();
        if (StringUtils.isBlank(instanceDataCode)) {
            instanceDataMap = Maps.newHashMap();
        } else {
            Optional<NodeInstanceData> instanceDataOptional = nodeInstanceDataService.detail(instanceDataCode);
            if (instanceDataOptional.isEmpty()) {
                log.warn("preCommit failed: cannot find instanceDataPO from db." +
                        "||flowInstanceCode={}||instanceDataCode={}", flowInstanceCode, instanceDataCode);
                throw new ProcessException(ErrorCode.GET_INSTANCE_DATA_FAILED);
            }
            NodeInstanceData nodeInstanceData = instanceDataOptional.get();
            instanceDataMap = InstanceDataUtil.getInstanceDataMap(nodeInstanceData.getInstanceData());
        }

        Map<String, InstanceData> commitDataMap = runtimeContext.getInstanceDataMap();
        if (MapUtils.isNotEmpty(commitDataMap)) {
            instanceDataCode = genId();
            instanceDataMap.putAll(commitDataMap);

            NodeInstanceData commitInstanceData = buildCommitInstanceData(runtimeContext,nodeInstanceCode ,
                    nodeInstance.getNodeCode(), instanceDataCode, instanceDataMap);
            nodeInstanceDataService.save(commitInstanceData);
        }

        fillCommitContext(runtimeContext, nodeInstance, instanceDataCode, instanceDataMap);
    }

    private NodeInstanceData buildCommitInstanceData(RuntimeContext runtimeContext, String nodeInstanceCode, String nodeCode,
                                                   String newInstanceDataCode, Map<String, InstanceData> instanceDataMap) {
        NodeInstanceData nodeInstanceData = new NodeInstanceData();
        BeanUtils.copyProperties(runtimeContext, nodeInstanceData);
        nodeInstanceData.setNodeInstanceCode(nodeInstanceCode);
        nodeInstanceData.setNodeCode(nodeCode);
        nodeInstanceData.setType(NodeInstanceLogType.SUBMIT.getCode());
        nodeInstanceData.setCreateTime(new Date());
        nodeInstanceData.setInstanceDataCode(newInstanceDataCode);
        nodeInstanceData.setInstanceData(new ArrayList<>(instanceDataMap.values()));
        return nodeInstanceData;
    }

    private void fillCommitContext(RuntimeContext runtimeContext, NodeInstance nodeInstance, String instanceDataCode,
                                   Map<String, InstanceData> instanceDataMap) throws ProcessException {

        runtimeContext.setInstanceDataCode(instanceDataCode);
        runtimeContext.setInstanceDataMap(instanceDataMap);

        updateSuspendNodeInstanceBO(runtimeContext.getSuspendNodeInstance(), nodeInstance, instanceDataCode);

        setCurrentFlowModel(runtimeContext);

        runtimeContext.setNodeInstanceList(Lists.newArrayList());
    }

    private void updateSuspendNodeInstanceBO(NodeInstance suspendNodeInstance, NodeInstance nodeInstance, String
            instanceDataCode) {
        suspendNodeInstance.setId(nodeInstance.getId());
        suspendNodeInstance.setNodeCode(nodeInstance.getNodeCode());
        suspendNodeInstance.setStatus(nodeInstance.getStatus());
        suspendNodeInstance.setSourceNodeInstanceCode(nodeInstance.getSourceNodeInstanceCode());
        suspendNodeInstance.setSourceNodeCode(nodeInstance.getSourceNodeCode());
        suspendNodeInstance.setInstanceDataCode(instanceDataCode);
    }

    private void setCurrentFlowModel(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstance suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        BaseNode currentNodeModel = BaseNodeUtil.getNodeByCode(runtimeContext.getBaseNodeMap(), suspendNodeInstance.getNodeCode());
        if (currentNodeModel == null) {
            log.warn("setCurrentFlowModel failed: cannot get currentNodeModel.||flowInstance={}||flowDeployCode={}||nodeCode={}",
                    runtimeContext.getFlowInstanceCode(), runtimeContext.getFlowDeployCode(), suspendNodeInstance.getNodeCode());
            throw new ProcessException(ErrorCode.GET_NODE_FAILED);
        }
        runtimeContext.setCurrentNodeModel(currentNodeModel);
    }

    private void doCommit(RuntimeContext runtimeContext) throws ProcessException {
        BaseNodeExecutor baseNodeExecutor = getExecuteExecutor(runtimeContext);
        baseNodeExecutor.commit(runtimeContext);

        baseNodeExecutor = baseNodeExecutor.getExecuteExecutor(runtimeContext);
        while (baseNodeExecutor != null) {
            baseNodeExecutor.execute(runtimeContext);
            baseNodeExecutor = baseNodeExecutor.getExecuteExecutor(runtimeContext);
        }
    }

    private void postCommit(RuntimeContext runtimeContext) throws ProcessException {
        if (Objects.equals(runtimeContext.getProcessStatus(), ProcessStatus.SUCCESS.getCode()) && runtimeContext.getCurrentNodeInstance() != null) {
            runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
        }
        saveNodeInstanceList(runtimeContext, NodeInstanceLogType.SUBMIT.getCode());

        if (isCompleted(runtimeContext)) {
            try {
                flowInstanceService.modifyStatus(FlowInstanceStatus.COMPLETE, runtimeContext.getFlowInstanceCode());
            } catch (ParamException e) {
                log.error("更新流程实例状态失败！", e);
                throw new ProcessException(e.getErrorCode(), e.getErrorMsg());
            }
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETE.getCode());
            log.info("postCommit: flowInstance process completely.||flowInstanceCode={}", runtimeContext.getFlowInstanceCode());
        }
    }

    @Override
    protected boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException {
        if (Objects.equals(runtimeContext.getFlowInstanceStatus(), FlowInstanceStatus.COMPLETE.getCode())) {
            return true;
        }

        NodeInstance suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        if (suspendNodeInstance == null) {
            log.warn("suspendNodeInstance is null.||runtimeContext={}", runtimeContext);
            return false;
        }

        if (!Objects.equals(suspendNodeInstance.getStatus(), NodeInstanceStatus.SUCCESS.getCode())) {
            return false;
        }

        String nodeCode = suspendNodeInstance.getNodeCode();
        Map<String, BaseNode> baseNodeMap = runtimeContext.getBaseNodeMap();
        BaseNode node = BaseNodeUtil.getNodeByCode(baseNodeMap, nodeCode);
        if (node == null) {
            log.error("node does not exist . runtimeContext:{}", runtimeContext);
            throw new ProcessException(ErrorCode.NODE_DOES_NOT_EXIST);
        }
        return Objects.equals(node.getNodeType(), NodeType.END_EVENT_NODE.getCode());
    }

    @Override
    protected BaseNodeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        if (isCompleted(runtimeContext)) {
            return null;
        }
        String type = runtimeContext.getCurrentNodeModel().getNodeType();
        return executorContext.getRuntimeExecutor(type);
    }
}
