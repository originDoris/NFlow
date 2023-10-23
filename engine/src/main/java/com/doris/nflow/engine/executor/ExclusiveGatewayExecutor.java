package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author: xhz
 * @Title: ExclusiveGatewayExecutor
 * @Description:
 * @date: 2022/10/1 09:23
 */
@Component(NodeTypeConstant.EXCLUSIVE_GATEWAY_NODE)
@Slf4j
public class ExclusiveGatewayExecutor extends RuntimeExecutor {


    public ExclusiveGatewayExecutor(NodeInstanceService nodeInstanceService, @Lazy ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext, nodeInstanceDataService);
    }

    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataCode(runtimeContext.getInstanceDataCode());
        currentNodeInstance.setStatus(NodeInstanceStatus.SUCCESS.getCode());
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
        super.postExecute(runtimeContext);
    }

    @Override
    protected BaseNodeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        BaseNode nextNode = calculateNextNode(runtimeContext.getCurrentNodeModel(),
                runtimeContext.getBaseNodeMap(), runtimeContext.getInstanceDataMap());

        runtimeContext.setCurrentNodeModel(nextNode);
        return executorContext.getRuntimeExecutor(nextNode.getNodeType());
    }

}
