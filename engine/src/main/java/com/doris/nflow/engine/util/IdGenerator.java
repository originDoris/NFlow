package com.doris.nflow.engine.util;

/**
 * @author: xhz
 * @Title: IdGenerator
 * @Description:
 * @date: 2022/9/30 11:21
 */
public interface IdGenerator {
    /**
     * 获取唯一id
     * @return
     */
    String getNextId();
}
