package com.doris.nflow.engine.flow.definition.enumerate;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author: origindoris
 * @Title: FlowDefinitionStatus
 * @Description: 流程定义状态
 * @date: 2022/9/29 16:08
 */
public enum FlowDefinitionStatus implements IEnum<String> {

    /**
     * 流程定义状态
     */
    INIT("init", "初始态"),
    EDIT("edit", "编辑中"),
    OFFLINE("offline", "已下线"),
    ;

    private String code;

    private String desc;

    FlowDefinitionStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
