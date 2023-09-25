package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.FlowErrorMessageConstant;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.DefinitionException;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.util.IdGenerator;
import com.doris.nflow.engine.util.StrongUuidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author: xhz
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



    private static final IdGenerator idGenerator = new StrongUuidGenerator();


    protected String genId() {
        return idGenerator.getNextId();
    }

    /**
     * 执行流程
     * @param runtimeContext
     * @throws ProcessException
     */
    public abstract void execute(RuntimeContext runtimeContext) throws ProcessException;

    /**
     * 提交流程
     * @param runtimeContext
     * @throws ProcessException
     */
    public abstract void commit(RuntimeContext runtimeContext) throws ProcessException;

    /**
     * 回滚流程
     * @param runtimeContext
     * @throws ProcessException
     */
    public abstract void rollback(RuntimeContext runtimeContext) throws ProcessException;

    /**
     * 流程是否已完成
     * @param runtimeContext
     * @return
     * @throws ProcessException
     */
    protected abstract boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException;

    /**
     * 获取执行顺序流程的执行器
     * @param runtimeContext
     * @return
     * @throws ProcessException
     */
    protected abstract BaseNodeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException;

    /**
     * 获取执行回滚流程的执行器
     * @param runtimeContext
     * @return
     * @throws ProcessException
     */
    protected abstract BaseNodeExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException;





}
