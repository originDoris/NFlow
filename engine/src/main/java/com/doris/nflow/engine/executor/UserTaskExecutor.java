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
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
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

    public UserTaskExecutor(NodeInstanceService nodeInstanceService, ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext);
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
        log.info("doExecute: userTask to commit.||flowInstanceId={}||nodeInstanceId={}||nodeKey={}||nodeName={}",
                runtimeContext.getFlowInstanceCode(), currentNodeInstance.getNodeInstanceCode(), nodeCode, nodeName);
        throw new SuspendException(ErrorCode.COMMIT_SUSPEND, MessageFormat.format(FlowErrorMessageConstant.NODE_INSTANCE_FORMAT,
                nodeCode, nodeName, currentNodeInstance.getNodeInstanceCode()));
    }

}
