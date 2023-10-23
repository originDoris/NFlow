package com.doris.nflow.engine.executor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.common.context.ExecutorContext;
import com.doris.nflow.engine.common.context.ExpressionCalculatorContext;
import com.doris.nflow.engine.common.context.RuntimeContext;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ProcessException;
import com.doris.nflow.engine.common.model.node.task.ServiceTask;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceData;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceService;
import com.doris.nflow.engine.util.HttpUtil;
import com.doris.nflow.engine.util.InstanceDataUtil;
import com.doris.nflow.engine.verticle.FlowVerticle;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author: xhz
 * @Title: ServiceTaskExecutor
 * @Description:
 * @date: 2022/10/11 10:27
 */
@Component(NodeTypeConstant.SERVICE_TASK_NODE)
@Slf4j
public class ServiceTaskExecutor extends RuntimeExecutor {

    private final FlowVerticle flowVerticle;

    @Value("${datacube.domain}")
    private String dataCubeDomain;

    public ServiceTaskExecutor(NodeInstanceService nodeInstanceService, @Lazy ExecutorContext executorContext, ExpressionCalculatorContext expressionCalculatorContext, NodeInstanceDataService nodeInstanceDataService, FlowVerticle flowVerticle) {
        super(nodeInstanceService, executorContext, expressionCalculatorContext, nodeInstanceDataService);
        this.flowVerticle = flowVerticle;
    }

    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        super.doExecute(runtimeContext);
        ServiceTask serviceTask = JSON.parseObject(JSON.toJSONString(runtimeContext.getCurrentNodeModel()), ServiceTask.class);
        if (serviceTask.getApiCode() == null) {
            log.info("service task apiCode is null || runtimeContext:{}", runtimeContext);
            throw new ProcessException(ErrorCode.GET_SERVICE_ID_IS_NULL);
        }
        // todo 发起http请求获取结果
        String response = sendRequest(serviceTask.getApiCode(), runtimeContext.getVariables());
        log.info("service task doExecute result :{}", response);

        Map<String, InstanceData> instanceDataMap = runtimeContext.getInstanceDataMap();
        InstanceData instanceData = new InstanceData();
        instanceData.setKey(serviceTask.getCode());
        instanceData.setValue(response);
        instanceDataMap.put(serviceTask.getCode(), instanceData);

        JSONObject jsonObject = JSON.parseObject(response);
        runtimeContext.getVariables().putAll(jsonObject);
    }


    /**
     * 请求接口获取数据
     *
     * @param apiCode
     * @param variables
     * @return
     */
    private String sendRequest(String apiCode, Map<String, Object> variables) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("apiCode", apiCode);
        body.put("params", variables);
        return sendPost(dataCubeDomain + "/api/info/flowExec", body);
    }

    private String sendPost(String url,Map<String, Object> dataMap) {
        HttpRequest.Builder getRequest = HttpRequest.newBuilder().timeout(Duration.ofMillis(30000))
                .uri(URI.create(url));
        getRequest.header("Content-Type", "application/json");
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(JSON.toJSONString(dataMap));
        getRequest.POST(bodyPublisher);
        HttpRequest request = getRequest.build();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        return HttpUtil.send(request, bodyHandler);
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
