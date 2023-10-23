package com.doris.nflow.engine.executor;

import com.alibaba.fastjson2.JSON;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.task.ScriptTask;
import com.doris.nflow.engine.common.model.node.task.ServiceTask;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceData;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceService;
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
 * @author: xhz
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
        super.doExecute(runtimeContext);
        ScriptTask scriptTask = JSON.parseObject(JSON.toJSONString(runtimeContext.getCurrentNodeModel()), ScriptTask.class);

        if (scriptTask.getPluginId() == null) {
            log.info("脚本任务节点插件id为空！runtimeContext:{}", runtimeContext);
            throw new ProcessException(ErrorCode.GET_SCRIPT_PLUGIN_ID_IS_NULL);
        }

        // todo 根据插件ID查询脚本
        String script = getScript(scriptTask.getPluginId(), scriptTask.getScript());

        if (StringUtils.isBlank(scriptTask.getScript())) {
            log.info("脚本任务节点脚本为空！runtimeContext:{}", runtimeContext);
            return;
        }

        String type = scriptTask.getScriptType();
        ExpressionCalculator expressionCalculator = expressionCalculatorContext.getExpressionCalculator(type);
        if (expressionCalculator == null){
            log.error("获取脚本解析器失败！|| runtimeContext:{}", runtimeContext);
            throw new ProcessException(ErrorCode.GET_SCRIPT_CALCULATOR_IS_NULL);
        }
        Map<String, InstanceData> instanceDataMap = runtimeContext.getInstanceDataMap();
        Map<String, Object> result = expressionCalculator.executorScript(script, runtimeContext.getVariables());


        InstanceData instanceData = new InstanceData();
        instanceData.setKey(scriptTask.getCode());
        instanceData.setValue(result);
        instanceDataMap.put(scriptTask.getCode(), instanceData);
        runtimeContext.getVariables().putAll(result);
    }


    /**
     * 根据插件ID获取脚本内容
     * @param pluginId 插件id
     * @param defaultScript 默认脚本内容
     * @return
     */
    private String getScript(Integer pluginId,String defaultScript) {
        return defaultScript;
    }


    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstance currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataCode(runtimeContext.getInstanceDataCode());
        currentNodeInstance.setStatus(NodeInstanceStatus.SUCCESS.getCode());
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);

        Optional<NodeInstanceData> instanceDataOptional = nodeInstanceDataService.detail(runtimeContext.getInstanceDataCode());
        if (instanceDataOptional.isPresent()) {
            NodeInstanceData nodeInstanceData = instanceDataOptional.get();
            nodeInstanceData.setInstanceData(InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap()));
            boolean modifyResult = nodeInstanceDataService.modify(nodeInstanceData);
            if (!modifyResult) {
                log.info("更新节点实例数据失败！|| runtimeContext:{}", runtimeContext);
                throw new ProcessException(ErrorCode.MODIFY_NODE_INSTANCE_DATA_FAILED);
            }
        }
        super.postExecute(runtimeContext);
    }
}
