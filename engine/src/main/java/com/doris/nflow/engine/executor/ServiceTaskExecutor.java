package com.doris.nflow.engine.executor;

import com.alibaba.fastjson2.JSON;
import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.task.ServiceTask;
import com.doris.nflow.engine.executor.enumerate.HttpMethodType;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import com.doris.nflow.engine.util.HttpUtil;
import com.doris.nflow.engine.util.InstanceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: ServiceTaskExecutor
 * @Description:
 * @date: 2022/10/11 10:27
 */
@Component(NodeTypeConstant.SERVICE_TASK_NODE)
@Slf4j
public class ServiceTaskExecutor extends RuntimeExecutor {

    public static final String FLOW_INSTANCE_MESSAGE = "flowInstanceCode=";

    public static final Long DEFAULT_TIMEOUT = 3000L;

    public static final String FLOW_INSTANCE_CODE = "flowInstanceCode";

    public ServiceTaskExecutor(NodeInstanceService nodeInstanceService, @Lazy ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext, nodeInstanceDataService);
    }

    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        ServiceTask serviceTask = (ServiceTask) runtimeContext.getCurrentNodeModel();
        if (StringUtils.isBlank(serviceTask.getUrl())) {
            log.info("service task url is null || runtimeContext:{}", runtimeContext);
            return;
        }
        String methodType = serviceTask.getMethodType();
        if (StringUtils.isBlank(methodType)) {
            log.info("service task methodType is null || runtimeContext:{}", runtimeContext);
            return;
        }
        String url = serviceTask.getUrl();
        url = appendUrlFlowInstanceCode(url, runtimeContext.getFlowInstanceCode());
        Long timeout = serviceTask.getTimeout() == null ? DEFAULT_TIMEOUT : serviceTask.getTimeout();

        Map<String, InstanceData> instanceDataMap = runtimeContext.getInstanceDataMap();
        String response;
        if (HttpMethodType.GET.getCode().equals(methodType)) {
            response = sendGet(url,timeout);
        }else{
            Map<String, Object> dataMap = InstanceDataUtil.parseInstanceDataMap(instanceDataMap);
            dataMap.put(FLOW_INSTANCE_CODE, runtimeContext.getFlowInstanceCode());
            response = sendPost(url, serviceTask.getHeaderMap(), dataMap,timeout);
        }
        log.info("service task doExecute result :{}", response);

        InstanceData instanceData = new InstanceData();
        instanceData.setKey(serviceTask.getCode());
        instanceData.setValue(response);
        instanceDataMap.put(serviceTask.getCode(), instanceData);
    }


    private String sendGet(String url, Long timeout) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofMillis(timeout)).GET().build();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        return HttpUtil.send(request, bodyHandler);
    }

    private String sendPost(String url, Map<String, String> headerMap,Map<String, Object> dataMap,Long timeout) {
        HttpRequest.Builder getRequest = HttpRequest.newBuilder().timeout(Duration.ofMillis(timeout))
                .uri(URI.create(url));
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            getRequest.header(key, value);
        }
        getRequest.header("Content-Type", "application/json");
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(JSON.toJSONString(dataMap));
        getRequest.POST(bodyPublisher);
        HttpRequest request = getRequest.build();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        return HttpUtil.send(request, bodyHandler);
    }


    private String appendUrlFlowInstanceCode(String url,String flowInstanceCode){
        try {
            return HttpUtil.appendUri(url, FLOW_INSTANCE_MESSAGE + flowInstanceCode);
        } catch (URISyntaxException e) {
            log.error("追加流程实例参数失败！", e);
            return url;
        }
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
