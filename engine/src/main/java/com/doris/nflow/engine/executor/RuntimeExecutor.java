package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.exception.DefinitionException;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.exception.ReentrantException;
import com.doris.nflow.engine.common.exception.SuspendException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import com.doris.nflow.engine.util.BaseNodeUtil;
import com.doris.nflow.engine.util.InstanceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: RuntimeExecutor
 * @Description:
 * @date: 2022/10/2 10:56
 */
@Slf4j
@Service
public abstract class RuntimeExecutor extends BaseNodeExecutor{

    protected final NodeInstanceService nodeInstanceService;

    protected final ExecutorContext executorContext;

    protected final ExpressionCalculatorContext expressionCalculatorContext;

    protected final NodeInstanceDataService nodeInstanceDataService;
    protected RuntimeExecutor(NodeInstanceService nodeInstanceService, ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService) {
        this.nodeInstanceService = nodeInstanceService;
        this.executorContext = executorContext;
        this.expressionCalculatorContext = expressionCalculatorContext;
        this.nodeInstanceDataService = nodeInstanceDataService;
    }

    @Override
    protected void checkInput(BaseNode baseNode) throws DefinitionException {
        super.checkInput(baseNode);
    }

    @Override
    protected void checkOutput(BaseNode baseNode) throws DefinitionException {
        super.checkOutput(baseNode);
    }

    public void valid(BaseNode baseNode) throws DefinitionException {
        checkInput(baseNode);
        checkOutput(baseNode);
    }

    @Override
    public void execute(RuntimeContext runtimeContext) throws ProcessException {
        try {
            preExecute(runtimeContext);
            doExecute(runtimeContext);
        } catch (ReentrantException re) {
            log.warn("execute ReentrantException: reentrant execute.||runtimeContext={},", runtimeContext, re);
        } catch (SuspendException se) {
            log.info("execute suspend.||runtimeContext={}", runtimeContext);
            throw se;
        } finally {
            postExecute(runtimeContext);
        }
    }


    /**
     * 预处理context内容
     * 设置流程需要处理的节点实例信息
     *
     * @param runtimeContext 运行台上下文信息
     * @throws ProcessException
     */
    protected void preExecute(RuntimeContext runtimeContext) throws ProcessException {
        // 当前节点实例
        NodeInstance currentNodeInstance = new NodeInstance();

        String flowInstanceCode = runtimeContext.getFlowInstanceCode();
        BaseNode currentNodeModel = runtimeContext.getCurrentNodeModel();
        String nodeCode = currentNodeModel.getCode();
        NodeInstance sourceNodeInstance = runtimeContext.getCurrentNodeInstance();

        String sourceNodeInstanceCode = StringUtils.EMPTY;
        String sourceNodeCode = StringUtils.EMPTY;
        if (sourceNodeInstance != null) {
            Optional<NodeInstance> instanceOptional = nodeInstanceService.detailBySourceInstanceCode(flowInstanceCode, sourceNodeInstance.getSourceNodeInstanceCode(), nodeCode);
            if (instanceOptional.isPresent()) {
                NodeInstance nodeInstance = instanceOptional.get();
                BeanUtils.copyProperties(nodeInstance, currentNodeInstance);
                log.info("preExecute nodeInstance :{}", nodeInstance);
                runtimeContext.setCurrentNodeInstance(currentNodeInstance);
                return;
            }
            sourceNodeInstanceCode = sourceNodeInstance.getNodeInstanceCode();
            sourceNodeCode = sourceNodeInstance.getNodeCode();
        }

        String nodeInstanceCode = genId();
        currentNodeInstance.setNodeInstanceCode(nodeInstanceCode);
        currentNodeInstance.setNodeCode(nodeCode);
        currentNodeInstance.setSourceNodeInstanceCode(sourceNodeInstanceCode);
        currentNodeInstance.setSourceNodeCode(sourceNodeCode);
        currentNodeInstance.setStatus(NodeInstanceStatus.PROCESSING.getCode());
        currentNodeInstance.setInstanceDataCode(StringUtils.defaultString(runtimeContext.getInstanceDataCode(), StringUtils.EMPTY));
        runtimeContext.setCurrentNodeInstance(currentNodeInstance);
    }

    /**
     * 验证context参数
     * @param runtimeContext
     */
    protected void verifyContext(RuntimeContext runtimeContext){
        Assert.notNull(runtimeContext.getFlowInstanceCode(),"流程实例代码不能为空！");
        Assert.notNull(runtimeContext.getCurrentNodeModel(),"当前流程处理节点不能为空！");
        Assert.notNull(runtimeContext.getCurrentNodeModel().getCode(),"当前流程处理节点代码不能为空！");
    }

    /**
     * 执行当前节点的业务
     * @param runtimeContext
     * @throws ProcessException
     */
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {

    }

    /**
     * 节点业务执行完后修改节点状态
     * @param runtimeContext
     * @throws ProcessException
     */
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
    }

    @Override
    public void commit(RuntimeContext runtimeContext) throws ProcessException {
        preCommit(runtimeContext);

        try {
            doCommit(runtimeContext);
        } catch (SuspendException se) {
            log.warn("SuspendException.");
            throw se;
        } finally {
            postCommit(runtimeContext);
        }
    }


    protected void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        log.warn("preCommit: unsupported element type.||flowInstanceCode={}||elementType={}",
                runtimeContext.getFlowInstanceCode(), runtimeContext.getCurrentNodeModel().getNodeType());
        throw new ProcessException(ErrorCode.UNSUPPORTED_ELEMENT_TYPE);
    }

    protected void doCommit(RuntimeContext runtimeContext) throws ProcessException {
    }

    protected void postCommit(RuntimeContext runtimeContext) throws ProcessException {
    }


    @Override
    public void rollback(RuntimeContext runtimeContext) throws ProcessException {
        try {
            preRollback(runtimeContext);
            doRollback(runtimeContext);
        } catch (SuspendException se) {
            log.warn("SuspendException.");
            throw se;
        } catch (ReentrantException re) {
            log.warn("ReentrantException: reentrant rollback.");
        } finally {
            postRollback(runtimeContext);
        }
    }


    protected void preRollback(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceCode = runtimeContext.getFlowInstanceCode();
        String nodeInstanceCode, nodeCode;
        NodeInstance currentNodeInstance;
        if (runtimeContext.getCurrentNodeInstance() == null) {
            currentNodeInstance = runtimeContext.getSuspendNodeInstance();
        } else {
            nodeInstanceCode = runtimeContext.getCurrentNodeInstance().getSourceNodeInstanceCode();
            Optional<NodeInstance> nodeInstanceDetail = nodeInstanceService.detail(nodeInstanceCode);
            if (nodeInstanceDetail.isEmpty()) {
                log.warn("preRollback failed: cannot find currentNodeInstancePO from db."
                        + "||flowInstanceCode={}||nodeInstanceCode={}", flowInstanceCode, nodeInstanceCode);
                throw new ProcessException(ErrorCode.GET_NODE_INSTANCE_FAILED);
            }
            currentNodeInstance = new NodeInstance();
            BeanUtils.copyProperties(currentNodeInstance, currentNodeInstance);

            String currentInstanceDataCode = currentNodeInstance.getInstanceDataCode();
            runtimeContext.setInstanceDataCode(currentInstanceDataCode);
            Optional<NodeInstanceData> nodeInstanceDataOptional = nodeInstanceDataService.detailByFlowInstanceCodeAndInstanceDataCode(flowInstanceCode, currentInstanceDataCode);
            if (nodeInstanceDataOptional.isPresent()) {
                NodeInstanceData nodeInstanceData = nodeInstanceDataOptional.get();
                Map<String, InstanceData> currentInstanceDataMap = InstanceDataUtil.getInstanceDataMap(nodeInstanceData.getInstanceData());
                runtimeContext.setInstanceDataMap(currentInstanceDataMap);
            }
        }
        runtimeContext.setCurrentNodeInstance(currentNodeInstance);

        nodeInstanceCode = currentNodeInstance.getNodeInstanceCode();
        nodeCode = currentNodeInstance.getNodeCode();
        String status = currentNodeInstance.getStatus();
        if (Objects.equals(status, NodeInstanceStatus.REVOKE.getCode())) {
            log.warn("preRollback: reentrant process.||flowInstanceCode={}||nodeInstance={}||nodeCode={}", flowInstanceCode, nodeInstanceCode, nodeCode);
            throw new ReentrantException(ErrorCode.REENTRANT_WARNING);
        }
        log.info("preRollback done.||flowInstanceCode={}||nodeInstance={}||nodeCode={}", flowInstanceCode, nodeInstanceCode, nodeCode);
    }

    /**
     * Common rollback: overwrite it in customized elementExecutor or do nothing
     *
     * @throws Exception
     */
    protected void doRollback(RuntimeContext runtimeContext) throws ProcessException {
    }

    /**
     * Update runtimeContext: update currentNodeInstance.status to DISABLED and add it to nodeInstanceList
     *
     * @throws Exception
     */
    protected void postRollback(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setStatus(NodeInstanceStatus.REVOKE.getCode());
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    /**
     * Get elementExecutor to rollback:
     * Get sourceNodeInstanceId from currentNodeInstance and get sourceElement
     *
     * @return
     * @throws Exception
     */
    @Override
    protected BaseNodeExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceId = runtimeContext.getFlowInstanceCode();
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();

        String sourceNodeInstanceCode = currentNodeInstance.getSourceNodeInstanceCode();
        if (StringUtils.isBlank(sourceNodeInstanceCode)) {
            log.warn("getRollbackExecutor: there's no sourceNodeInstance(startEvent)."
                    + "||flowInstanceCode={}||nodeInstanceCode={}", flowInstanceId, currentNodeInstance.getFlowDeployCode());
            return null;
        }

        // TODO: 2019/12/13 get from cache
        Optional<NodeInstance> detail = nodeInstanceService.detail(sourceNodeInstanceCode);
        if (detail.isEmpty()) {
            log.warn("getRollbackExecutor failed: cannot find sourceNodeInstance from db."
                    + "||flowInstanceCode={}||sourceNodeInstanceCode={}", flowInstanceId, sourceNodeInstanceCode);
            throw new ProcessException(ErrorCode.GET_NODE_INSTANCE_FAILED);
        }
        NodeInstance sourceNodeInstance = detail.get();

        BaseNode sourceNode =runtimeContext.getBaseNodeMap().get(sourceNodeInstance.getNodeCode());

        // TODO: 2019/12/18
        runtimeContext.setCurrentNodeModel(sourceNode);
        return executorContext.getRuntimeExecutor(sourceNode.getNodeType());
    }

    @Override
    protected boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException {
        return false;
    }

    @Override
    protected BaseNodeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        Map<String, BaseNode> baseNodeMap = runtimeContext.getBaseNodeMap();
        BaseNode baseNode = getUniqueNextNode(runtimeContext.getCurrentNodeModel(), baseNodeMap);
        runtimeContext.setCurrentNodeModel(baseNode);
        return executorContext.getRuntimeExecutor(baseNode.getNodeType());
    }

    protected BaseNode getUniqueNextNode(BaseNode currentNode, Map<String, BaseNode> baseNodeMap) {
        List<String> output = currentNode.getOutput();
        String nextNodeKey = output.get(0);
        BaseNode baseNode = baseNodeMap.get(nextNodeKey);
        while (Objects.equals(baseNode.getNodeType(), NodeType.SEQUENCE_FLOW_NODE.getCode())) {
            baseNode = getUniqueNextNode(baseNode, baseNodeMap);
        }
        return baseNode;
    }



    protected BaseNode calculateNextNode(BaseNode currentBaseNode, Map<String, BaseNode> baseNodeMap,
                                         Map<String, InstanceData> instanceDataMap) throws ProcessException {
        BaseNode nextNode = calculateOutgoing(currentBaseNode, baseNodeMap, instanceDataMap);

        while (Objects.equals(nextNode.getNodeType(), NodeType.SEQUENCE_FLOW_NODE.getCode())) {
            nextNode = getUniqueNextNode(nextNode, baseNodeMap);
        }
        return nextNode;
    }

    private BaseNode calculateOutgoing(BaseNode baseNode, Map<String, BaseNode> baseNodeMap,
                                       Map<String, InstanceData> instanceDataMap) throws ProcessException {
        BaseNode defaultNode = null;

        List<String> outputList = baseNode.getOutput();
        for (String key : outputList) {
            BaseNode outputSequenceNode = baseNodeMap.get(key);
            String condition = BaseNodeUtil.getCondition(outputSequenceNode);
            String conditionType = BaseNodeUtil.getConditionType(outputSequenceNode);
            if (StringUtils.isNotBlank(condition) && processCondition(condition, instanceDataMap, conditionType)) {

                return outputSequenceNode;
            }

            if (BaseNodeUtil.isDefaultCondition(outputSequenceNode)) {
                defaultNode = outputSequenceNode;
            }
        }
        if (defaultNode != null) {
            log.info("calculateOutgoing: return defaultBaseNode.||nodeCode={}", baseNode.getCode());
            return defaultNode;
        }

        log.warn("calculateOutgoing failed.||nodeCode={}", baseNode.getCode());
        throw new ProcessException(ErrorCode.GET_OUTGOING_FAILED);
    }

    protected boolean processCondition(String expression, Map<String, InstanceData> instanceDataMap,String type) throws ProcessException {
        Map<String, Object> dataMap = InstanceDataUtil.parseInstanceDataMap(instanceDataMap);
        return expressionCalculatorContext.getExpressionCalculator(type).calculate(expression, dataMap);
    }

}
