package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.exception.ReentrantException;
import com.doris.nflow.engine.common.exception.SuspendException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: RuntimeExecutor
 * @Description:
 * @date: 2022/10/2 10:56
 */
@Slf4j
public abstract class RuntimeExecutor extends BaseNodeExecutor{

    protected final NodeInstanceService nodeInstanceService;

    protected RuntimeExecutor(NodeInstanceService nodeInstanceService) {
        this.nodeInstanceService = nodeInstanceService;
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

    }

    @Override
    public void rollback(RuntimeContext runtimeContext) throws ProcessException {

    }

    @Override
    protected boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException {
        return false;
    }

    @Override
    protected BaseNodeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        return null;
    }

    @Override
    protected BaseNodeExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException {
        return null;
    }
}
