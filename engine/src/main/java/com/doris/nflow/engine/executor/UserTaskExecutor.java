package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.FlowErrorMessageConstant;
import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.exception.SuspendException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

/**
 * @author: origindoris
 * @Title: UserTaskExecutor
 * @Description:
 * @date: 2022/10/3 06:59
 */
@Component(NodeTypeConstant.USER_TASK_NODE)
@Slf4j
public class UserTaskExecutor extends RuntimeExecutor {

    public UserTaskExecutor(NodeInstanceService nodeInstanceService, @Lazy ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext, nodeInstanceDataService);
    }

    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (Objects.equals(currentNodeInstance.getStatus(), NodeInstanceStatus.SUCCESS.getCode())) {
            log.warn("doExecute reentrant: currentNodeInstance is completed.||runtimeContext={}", runtimeContext);
            return;
        }

        if (!Objects.equals(currentNodeInstance.getStatus(), NodeInstanceStatus.PROCESSING.getCode())) {
            currentNodeInstance.setStatus(NodeInstanceStatus.PROCESSING.getCode());
        }
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);

        BaseNode baseNode = runtimeContext.getCurrentNodeModel();
        String nodeName = baseNode.getName();
        String nodeCode = baseNode.getCode();
        log.info("doExecute: userTask to commit.||flowInstanceCode={}||nodeInstanceCode={}||nodeCode={}||nodeName={}",
                runtimeContext.getFlowInstanceCode(), currentNodeInstance.getNodeInstanceCode(), nodeCode, nodeName);
        throw new SuspendException(ErrorCode.COMMIT_SUSPEND, MessageFormat.format(FlowErrorMessageConstant.NODE_INSTANCE_FORMAT,
                nodeCode, nodeName, currentNodeInstance.getNodeInstanceCode()));
    }

    @Override
    protected BaseNodeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        BaseNode currentNodeModel = runtimeContext.getCurrentNodeModel();
        Map<String, BaseNode> baseNodeMap = runtimeContext.getBaseNodeMap();

        BaseNode nextNode;
        if (currentNodeModel.getOutput().size() == 1) {
            nextNode = getUniqueNextNode(currentNodeModel, baseNodeMap);
        } else {
            nextNode = calculateNextNode(currentNodeModel, baseNodeMap, runtimeContext.getInstanceDataMap());
        }
        log.info("getExecuteExecutor.||nextNode={}||runtimeContext={}", nextNode, runtimeContext);
        runtimeContext.setCurrentNodeModel(nextNode);
        return executorContext.getRuntimeExecutor(nextNode.getType());
    }

    @Override
    protected void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceCode = runtimeContext.getFlowInstanceCode();
        NodeInstance suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        String nodeInstanceCode = suspendNodeInstance.getSourceNodeInstanceCode();
        String status = suspendNodeInstance.getStatus();
        BaseNode baseNode = runtimeContext.getCurrentNodeModel();
        String nodeName = baseNode.getName();
        String nodeCode = baseNode.getCode();

        NodeInstance currentNodeInstance = new NodeInstance();
        BeanUtils.copyProperties(suspendNodeInstance, currentNodeInstance);
        runtimeContext.setCurrentNodeInstance(currentNodeInstance);

        //invalid commit node
        if (!suspendNodeInstance.getNodeCode().equals(nodeCode)) {
            log.warn("preCommit: invalid nodeKey to commit.||flowInstanceCode={}||nodeInstanceCode={}||nodeCode={}||nodeName={}",
                    flowInstanceCode, nodeInstanceCode, nodeCode, nodeName);
            throw new ProcessException(ErrorCode.COMMIT_FAILED, MessageFormat.format(FlowErrorMessageConstant.NODE_INSTANCE_FORMAT,
                    baseNode.getCode(), nodeName, currentNodeInstance.getNodeInstanceCode()));
        }

        //reentrant: completed
        if (Objects.equals(status, NodeInstanceStatus.SUCCESS.getCode())) {
            log.warn("preCommit: userTask is completed.||flowInstanceCode={}||nodeInstanceCode={}||nodeCode={}",
                    flowInstanceCode, nodeInstanceCode, nodeCode);
            return;
        }

        //invalid status
        if (!Objects.equals(status, NodeInstanceStatus.PROCESSING.getCode())) {
            log.warn("preCommit: invalid status to commit.||flowInstanceCode={}||status={}||nodeInstanceCode={}||nodeCode={}",
                    flowInstanceCode, status, nodeInstanceCode, nodeCode);
            throw new ProcessException(ErrorCode.COMMIT_FAILED, MessageFormat.format(FlowErrorMessageConstant.NODE_INSTANCE_FORMAT,
                    baseNode.getCode(), nodeName, currentNodeInstance.getNodeInstanceCode()));
        }
    }

    @Override
    protected void postCommit(RuntimeContext runtimeContext) {
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (!Objects.equals(currentNodeInstance.getStatus(), NodeInstanceStatus.SUCCESS.getCode())) {
            currentNodeInstance.setStatus(NodeInstanceStatus.SUCCESS.getCode());
            runtimeContext.getNodeInstanceList().add(currentNodeInstance);
        }
    }

    @Override
    protected void doRollback(RuntimeContext runtimeContext) throws ProcessException {

        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        String status = currentNodeInstance.getStatus();
        currentNodeInstance.setStatus(NodeInstanceStatus.REVOKE.getCode());
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
        if (Objects.equals(status, NodeInstanceStatus.SUCCESS.getCode())) {
            NodeInstance newNodeInstance = new NodeInstance();
            BeanUtils.copyProperties(currentNodeInstance, newNodeInstance);
            newNodeInstance.setId(null);
            String newNodeInstanceId = genId();
            newNodeInstance.setInstanceDataCode(newNodeInstanceId);
            newNodeInstance.setStatus(NodeInstanceStatus.PROCESSING.getCode());
            runtimeContext.setCurrentNodeInstance(newNodeInstance);
            runtimeContext.getNodeInstanceList().add(newNodeInstance);
            throw new SuspendException(ErrorCode.ROLLBACK_SUSPEND, MessageFormat.format(FlowErrorMessageConstant.NODE_INSTANCE_FORMAT,
                    newNodeInstance.getNodeCode(),
                    runtimeContext.getBaseNodeMap().get(newNodeInstance.getNodeCode()),
                    currentNodeInstance.getInstanceDataCode()));
        }
        log.info("doRollback.||currentNodeInstance={}||nodeCode={}", currentNodeInstance, currentNodeInstance.getNodeCode());
    }

}
