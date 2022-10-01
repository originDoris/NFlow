package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.FlowErrorMessageConstant;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.DefinitionException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author: origindoris
 * @Title: BaseNodeExecutor
 * @Description: 节点执行器
 * @date: 2022/10/1 07:18
 */
@Slf4j
public abstract class BaseNodeExecutor {

    /**
     * 检查节点输入信息
     *
     * @param baseNode
     */
    protected void checkInput(@NotNull(message = "节点信息不能为空！") BaseNode baseNode) throws DefinitionException {
        List<String> input = baseNode.getInput();
        if (CollectionUtils.isEmpty(input)) {
            String errorMsg = builderErrorMsg(baseNode, ErrorCode.NODE_LACK_INPUT);
            log.warn(errorMsg);
            throw new DefinitionException(ErrorCode.NODE_LACK_INPUT, errorMsg);
        }
    }


    /**
     * 检查节点输出信息
     *
     * @param baseNode
     */
    protected void checkOutput(@NotNull(message = "节点信息不能为空！") BaseNode baseNode) throws DefinitionException {
        List<String> output = baseNode.getOutput();
        if (CollectionUtils.isEmpty(output)) {
            String errorMsg = builderErrorMsg(baseNode, ErrorCode.NODE_LACK_OUTPUT);
            log.warn(errorMsg);
            throw new DefinitionException(ErrorCode.NODE_LACK_OUTPUT, errorMsg);
        }
    }

    /**
     * 记录异常日志，不阻断
     * @param baseNode 节点信息
     * @param errorCode 异常代码
     */
    protected void recordException(BaseNode baseNode, ErrorCode errorCode){
        String errorMsg = builderErrorMsg(baseNode, errorCode);
        log.warn(errorMsg);
    }

    private String builderErrorMsg(BaseNode baseNode, ErrorCode errorCode) {
        String nodeName = baseNode.getName();
        String nodeCode = baseNode.getCode();
        return MessageFormat.format(FlowErrorMessageConstant.NODE_DEFINITION_ERROR_MSG_FORMAT, errorCode, nodeName, nodeCode);
    }









}
