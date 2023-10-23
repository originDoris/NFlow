package com.doris.nflow.engine.common.constant;

/**
 * @author: origindoris
 * @Title: WebSocketMessageConstant
 * @Description:
 * @date: 2023/10/10 16:09
 */
public class WebSocketMessageConstant {

    public static final String INFO_START_MESSAGE = "<info> 服务编排开始执行";
    public static final String INFO_NODE_START_MESSAGE = "<info> 节点名称：{0}，开始执行";
    public static final String INFO_NODE_END_MESSAGE = "<info> 节点名称：{0}，执行成功，输出结果：{1}";
    public static final String ERROR_NODE_MESSAGE = "<error> 节点名称：{0}，执行失败，异常日志：{1}";
    public static final String ERROR_FLOW_MESSAGE = "<error> 服务编排执行异常，异常日志：{0}";
}
