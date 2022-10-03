package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.FlowErrorMessageConstant;
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
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author: origindoris
 * @Title: EndEventExecutor
 * @Description:
 * @date: 2022/10/1 09:23
 */
@Component(NodeTypeConstant.END_EVENT_NODE)
@Slf4j
public class EndEventExecutor extends RuntimeExecutor {


    public EndEventExecutor(NodeInstanceService nodeInstanceService, ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext, nodeInstanceDataService);
    }

    @Override
    protected void checkOutput(BaseNode baseNode) throws DefinitionException {
        List<String> output = baseNode.getOutput();
        if (CollectionUtils.isNotEmpty(output)) {
            recordException(baseNode, ErrorCode.NODE_TOO_MUCH_OUTPUT);
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
    protected void doRollback(RuntimeContext runtimeContext) throws ProcessException {
        BaseNode baseNode = runtimeContext.getCurrentNodeModel();
        String nodeName = baseNode.getName();
        log.warn("doRollback: unsupported element type as EndEvent.||flowInstanceId={}||nodeKey={}||nodeName={}||nodeType={}",
                runtimeContext.getFlowInstanceCode(), baseNode.getCode(), nodeName, baseNode.getType());
        throw new ProcessException(ErrorCode.UNSUPPORTED_ELEMENT_TYPE,
                MessageFormat.format(FlowErrorMessageConstant.NODE_INFO_FORMAT, baseNode.getCode(), nodeName, baseNode.getType()));
    }
}
