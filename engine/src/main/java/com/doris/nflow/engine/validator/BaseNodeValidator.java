package com.doris.nflow.engine.validator;

import com.doris.nflow.engine.common.constant.FlowErrorMessageConstant;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.enumerate.NodeType;
import com.doris.nflow.engine.common.exception.DefinitionException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.executor.RuntimeExecutor;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author: xhz
 * @Title: BaseNodeValidator
 * @Description: 流程节点参数验证器
 * @date: 2022/10/1 07:09
 */
@Slf4j
@Component
public class BaseNodeValidator {

    private final ExecutorContext executorContext;

    public BaseNodeValidator(ExecutorContext executorContext) {
        this.executorContext = executorContext;
    }

    public void validate(List<BaseNode> flowModule) throws DefinitionException {
        if (CollectionUtils.isEmpty(flowModule)) {
            log.warn("message={}", ErrorCode.MODEL_EMPTY.getMsg());
            throw new DefinitionException(ErrorCode.MODEL_EMPTY);
        }

        // key:code value:baseNode
        Map<String, BaseNode> baseNodeMap = Maps.newHashMap();

        for (BaseNode baseNode : flowModule) {
            if (baseNodeMap.containsKey(baseNode.getCode())) {
                String baseNodeName = baseNode.getName();
                String baseNodeCode = baseNode.getCode();
                String exceptionMsg = MessageFormat.format(FlowErrorMessageConstant.MODEL_DEFINITION_ERROR_MSG_FORMAT,
                        ErrorCode.NODE_KEY_NOT_UNIQUE, baseNodeName, baseNodeCode);
                log.warn(exceptionMsg);
                throw new DefinitionException(ErrorCode.NODE_KEY_NOT_UNIQUE, exceptionMsg);
            }
            Assert.notNull(baseNode.getNodeType(),"节点类型不能为空！");
            baseNodeMap.put(baseNode.getCode(), baseNode);
        }

        int startCount = 0;
        int endCount = 0;
        for (BaseNode baseNode : baseNodeMap.values()) {
            RuntimeExecutor runtimeExecutor = executorContext.getRuntimeExecutor(baseNode.getNodeType());
            runtimeExecutor.valid(baseNode);
            if (NodeType.START_EVENT_NODE.getCode().equals(baseNode.getNodeType())) {
                startCount++;
            }
            if (NodeType.END_EVENT_NODE.getCode().equals(baseNode.getNodeType())) {
                endCount++;
            }
        }

        if (startCount != 1) {
            log.warn("message={}||startEventCount={}", ErrorCode.START_NODE_INVALID.getMsg(), startCount);
            throw new DefinitionException(ErrorCode.START_NODE_INVALID);
        }

        if (endCount < 1) {
            log.warn("message={}", ErrorCode.END_NODE_INVALID.getMsg());
            throw new DefinitionException(ErrorCode.END_NODE_INVALID);
        }

    }
}
