package com.doris.nflow.engine.node.instance.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xhz
 * @Title: InstanceData
 * @Description: 实例数据
 * @date: 2022/10/2 17:42
 */
@Data
public class InstanceData implements Serializable {

    private String key;

    private String type;

    private Object value;
}
