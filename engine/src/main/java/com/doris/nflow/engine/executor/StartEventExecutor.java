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
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author: origindoris
 * @Title: StartEventExecutor
 * @Description:
 * @date: 2022/10/1 09:11
 */
@Component(NodeTypeConstant.START_EVENT_NODE)
@Slf4j
public class StartEventExecutor extends RuntimeExecutor {

    public StartEventExecutor(NodeInstanceService nodeInstanceService, @Lazy ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext, nodeInstanceDataService);
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
    protected void preRollback(RuntimeContext runtimeContext) throws ProcessException {
        runtimeContext.setCurrentNodeInstance(runtimeContext.getSuspendNodeInstance());
        runtimeContext.setNodeInstanceList(Collections.emptyList());

        log.warn("postRollback: reset runtimeContext.||flowInstanceCode={}||nodeCode={}||nodeType={}",
                runtimeContext.getFlowInstanceCode(), runtimeContext.getCurrentNodeModel().getCode(), runtimeContext.getCurrentNodeModel().getType());
        throw new ProcessException(ErrorCode.NO_USER_TASK_TO_ROLLBACK, "It's a startEvent.");
    }



}
