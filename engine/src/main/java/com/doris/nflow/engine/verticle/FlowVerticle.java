package com.doris.nflow.engine.verticle;

import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import com.doris.nflow.engine.processor.DefinitionProcessor;
import com.doris.nflow.engine.processor.RuntimeProcessor;
import com.doris.nflow.engine.processor.model.param.*;
import com.doris.nflow.engine.processor.model.result.CreateFlowResult;
import com.doris.nflow.engine.processor.model.result.DeployFlowResult;
import com.doris.nflow.engine.processor.model.result.StartProcessorResult;
import com.doris.nflow.engine.util.BuildModuleUtil;
import com.doris.nflow.engine.util.InstanceDataUtil;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.MultiMap;
import io.vertx.rxjava3.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author: xhz
 * @Title: FlowVerticle
 * @Description:
 * @date: 2023/10/12 15:08
 */
@Component
@Slf4j
public class FlowVerticle extends AbstractVerticle {


    public static final String SAVE_FLOW_ADDRESS = "save.flow";
    public static final String MODIFY_FLOW_ADDRESS = "modify.flow";
    public static final String REMOVE_FLOW_ADDRESS = "remove.flow";
    public static final String RUN_FLOW_ADDRESS = "run.flow";

    public static final String API_INFO_ADDRESS = "api.info";

    @Resource
    private DefinitionProcessor definitionProcessor;

    @Resource
    private RuntimeProcessor runtimeProcessor;

    @Override
    public Completable rxStart() {
        vertx.eventBus().consumer(SAVE_FLOW_ADDRESS, this::addFlow);
        vertx.eventBus().consumer(MODIFY_FLOW_ADDRESS, this::modifyFlow);
        vertx.eventBus().consumer(REMOVE_FLOW_ADDRESS, this::removeFlow);
        vertx.eventBus().consumer(RUN_FLOW_ADDRESS, this::run);
        return super.rxStart();
    }


    private void addFlow(Message<JsonObject> message) {
        DeployFlowResult deployFlowResult = null;
        try {
            JsonObject body = message.body();
            CreateFlowParam createFlowParam = DatabindCodec.mapper().convertValue(body, CreateFlowParam.class);
            String content = createFlowParam.getContent();
            List<BaseNode> baseNodes = BuildModuleUtil.buildNode(content);
            createFlowParam.setFlowModule(baseNodes);
            deployFlowResult = saveFlow(createFlowParam);
        } catch (Exception e) {
            log.info("新增流程失败,", e);
            message.fail(ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("flowModuleCode", deployFlowResult.getFlowModuleCode());
        map.put("flowDeployCode", deployFlowResult.getFlowDeployCode());
        message.reply(JsonObject.mapFrom(map));
    }

    private void modifyFlow(Message<JsonObject> message){
        try {
            JsonObject body = message.body();
            ModifyFlowParam modifyFlowParam = DatabindCodec.mapper().convertValue(body, ModifyFlowParam.class);
            String content = modifyFlowParam.getContent();
            List<BaseNode> baseNodes = BuildModuleUtil.buildNode(content);
            modifyFlowParam.setFlowModule(baseNodes);
            definitionProcessor.modify(modifyFlowParam);
            definitionProcessor.modifyDeploy(modifyFlowParam);
        } catch (Exception e) {
            log.info("修改流程失败,", e);
            message.fail(ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
            return;
        }
        message.reply(true);
    }

    private void removeFlow(Message<JsonObject> message) {
        JsonObject body = message.body();
        try {
            RemoveFlowParam removeFlowParam = DatabindCodec.mapper().convertValue(body, RemoveFlowParam.class);
            definitionProcessor.removeFlow(removeFlowParam);
        } catch (Exception e) {
            log.info("删除流程失败,", e);
            message.fail(ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
            return;
        }
        message.reply(true);
    }

    /**
     * dataCube 调用运行接口
     */
    private void run(Message<JsonObject> message) {
        JsonObject body = message.body();
        StartProcessorResult startProcessorResult = null;
        try {
            StartProcessorParam startProcessorParam = DatabindCodec.mapper().convertValue(body, StartProcessorParam.class);
            List<InstanceData> data = InstanceDataUtil.paresInstanceData(startProcessorParam.getApiParams());
            startProcessorParam.setParams(data);
            startProcessorResult = runtimeProcessor.startProcess(startProcessorParam);
        } catch (IllegalArgumentException e) {
            log.info("运行流程失败,", e);
            message.fail(ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
            return;
        }
        message.reply(JsonObject.mapFrom(startProcessorResult));
    }


    public CompletableFuture<JsonObject> execApi(String apiCode, Map<String, Object> params) {
        DeliveryOptions deliveryOptions =  new DeliveryOptions();
        deliveryOptions.addHeader("action", "flowExec");
        deliveryOptions.getHeaders().set("action", "flowExec");

        JsonObject entries = new JsonObject();
        entries.put("apiCode", apiCode);
        entries.put("params", params);

        Vertx vertx1 = Vertx.vertx();
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        Future<io.vertx.core.eventbus.Message<JsonObject>> request = vertx1.eventBus().request("api.info.flow.run", entries, deliveryOptions);
        request.onSuccess(res -> {
            future.complete(res.body());
        });
//        Single<JsonObject> single = vertx.eventBus().<JsonObject>rxRequest("api.info.flow.run", entries, deliveryOptions).map(Message::body);
//        return vertx.eventBus().<JsonObject>rxRequest("api.info.flow.run", entries, deliveryOptions).map(Message::body);

//        Disposable subscribe = single.subscribe(future::complete, future::completeExceptionally);
//        future.get();
        return future;
    }

    public DeployFlowResult saveFlow(CreateFlowParam createFlowParam){
        CreateFlowResult createFlowResult = definitionProcessor.create(createFlowParam);
        DeployFlowParam deployFlowParam = new DeployFlowParam();
        deployFlowParam.setFlowModuleCode(createFlowResult.getFlowModuleCode());
        return definitionProcessor.deploy(deployFlowParam);
    }
}
