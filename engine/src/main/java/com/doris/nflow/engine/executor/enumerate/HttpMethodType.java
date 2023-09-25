package com.doris.nflow.engine.executor.enumerate;

/**
 * @author: xhz
 * @Title: HttpMethodType
 * @Description:
 * @date: 2022/10/11 14:24
 */
public enum HttpMethodType {
    /**
     * http 请求方式
     */
    GET("get","get"),
    POST("post", "post"),;
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    HttpMethodType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
