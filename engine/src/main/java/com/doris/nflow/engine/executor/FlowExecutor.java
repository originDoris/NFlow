package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.WebSocketMessageConstant;
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
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceDataType;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceLogType;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceLog;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceLogService;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceService;
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

import java.text.MessageFormat;
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
            sendWebSocketMessage(MessageFormat.format(WebSocketMessageConstant.ERROR_FLOW_MESSAGE, e.getErrorMsg()), runtimeContext.getWebSocketKey());
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

        // 发送webSocket消息
        sendWebSocketMessage(WebSocketMessageConstant.INFO_START_MESSAGE, runtimeContext.getWebSocketKey());

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
