package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.DefinitionException;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: StartEventExecutor
 * @Description:
 * @date: 2022/10/1 09:11
 */
@Component(NodeTypeConstant.START_EVENT_NODE)
@Slf4j
public class StartEventExecutor extends RuntimeExecutor {

    public StartEventExecutor(NodeInstanceService nodeInstanceService, ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext);
    }

    @Override
    protected void checkInput(BaseNode baseNode) throws DefinitionException {
        List<String> input = baseNode.getInput();
        if (CollectionUtils.isNotEmpty(input)) {
            recordException(baseNode, ErrorCode.NODE_TOO_MUCH_INPUT);
        }
    }

    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataCode(runtimeContext.getInstanceDataCode());
        currentNodeInstance.setStatus(NodeInstanceStatus.SUCCESS.getCode());
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
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



}
