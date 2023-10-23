package com.doris.nflow.engine.socket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: origindoris
 * @Title: FlowWebSocketServer
 * @Description:
 * @date: 2023/10/10 15:06
 */
@Slf4j
@Service
@ServerEndpoint("/websocket/flow/{webSocketKey}")
public class FlowWebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    @Getter
    private static  ConcurrentHashMap<String,FlowWebSocketServer> webSocketSet = new  ConcurrentHashMap<String,FlowWebSocketServer>();


    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String webSocketKey = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("webSocketKey") String webSocketKey) {
        this.session = session;
        webSocketSet.put(webSocketKey, this);
        this.webSocketKey = webSocketKey;
        addOnlineCount();
        try {
            sendMessage("conn_success");
            log.info("有新窗口开始监听:" + webSocketKey + ",当前在线人数为:" + getOnlineCount());
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(webSocketKey);
        subOnlineCount();
        //断开连接情况下，更新主板占用情况为释放
        log.info("释放的webSocketKey为：" + webSocketKey);
        //这里写你 释放的时候，要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + webSocketKey + "的信息:" + message);
        //群发消息
        for (FlowWebSocketServer value : webSocketSet.values()) {
            try {
                value.sendMessage(message);
            } catch (IOException e) {
                log.info("onMessage 出现异常：", e);
            }
        }
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误", error);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String flowModuleCode) throws IOException {
        log.info("推送消息到窗口" + flowModuleCode + "，推送内容:" + message);

        if (StringUtils.isNotBlank(flowModuleCode) && webSocketSet.containsKey(flowModuleCode)) {
            webSocketSet.get(flowModuleCode).sendMessage(message);
        }
        if (StringUtils.isBlank(flowModuleCode)) {
            for (FlowWebSocketServer value : webSocketSet.values()) {
                value.sendMessage(message);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        FlowWebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        FlowWebSocketServer.onlineCount--;
    }

}
