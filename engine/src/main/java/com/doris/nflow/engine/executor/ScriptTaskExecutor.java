package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.task.ScriptTask;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import com.doris.nflow.engine.script.ExpressionCalculator;
import com.doris.nflow.engine.util.InstanceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.doris.nflow.engine.common.constant.NodeTypeConstant.SCRIPT_TASK_NODE;

/**
 * @author: origindoris
 * @Title: ScriptExecutor
 * @Description:
 * @date: 2022/10/10 14:32
 */
@Component(SCRIPT_TASK_NODE)
@Slf4j
public class ScriptTaskExecutor extends RuntimeExecutor{

    protected ScriptTaskExecutor(NodeInstanceService nodeInstanceService, @Lazy ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext, nodeInstanceDataService);
    }

    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        ScriptTask scriptTask = (ScriptTask) runtimeContext.getCurrentNodeModel();
        if (StringUtils.isBlank(scriptTask.getScript())) {
            log.info("脚本任务节点脚本为空！runtimeContext:{}", runtimeContext);
            return;
        }

        String type = scriptTask.getType();
        ExpressionCalculator expressionCalculator = expressionCalculatorContext.getExpressionCalculator(type);
        if (expressionCalculator == null){
            log.error("获取脚本解析器失败！|| runtimeContext:{}", runtimeContext);
            throw new ProcessException(ErrorCode.GET_SCRIPT_CALCULATOR_IS_NULL);
        }
        Map<String, InstanceData> instanceDataMap = runtimeContext.getInstanceDataMap();
        Map<String, Object> dataMap = InstanceDataUtil.parseInstanceDataMap(instanceDataMap);
        Map<String, Object> result = expressionCalculator.executorScript(scriptTask.getScript(), dataMap);
        instanceDataMap.putAll(InstanceDataUtil.parseDataMap2InstanceData(result));
    }


    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataCode(runtimeContext.getInstanceDataCode());
        currentNodeInstance.setStatus(NodeInstanceStatus.SUCCESS.getCode());
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);

        Optional<NodeInstanceData> instanceDataOptional = nodeInstanceDataService.detail(runtimeContext.getInstanceDataCode());
        if (instanceDataOptional.isEmpty()) {
            log.info("节点实例数据不存在！|| runtimeContext:{}", runtimeContext);
            throw new ProcessException(ErrorCode.GET_NODE_INSTANCE_DATA_FAILED);
        }
        NodeInstanceData nodeInstanceData = instanceDataOptional.get();
        nodeInstanceData.setInstanceData(InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap()));
        boolean modifyResult = nodeInstanceDataService.modify(nodeInstanceData);
        if (!modifyResult) {
            log.info("更新节点实例数据失败！|| runtimeContext:{}", runtimeContext);
            throw new ProcessException(ErrorCode.MODIFY_NODE_INSTANCE_DATA_FAILED);
        }
    }
}
