package com.doris.nflow.engine.common.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: xhz
 * @Title: BaseModel
 * @Description:
 * @date: 2022/9/29 10:40
 */
@Data
public class BaseModel implements Serializable {

    public static final String CREATE_TIME = "createTime";

    public static final String MODIFY_TIME = "modifyTime";

    @TableId(type = IdType.AUTO)
    protected Integer id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Date modifyTime;

    /**
     * 操作人
     */
    protected String operator;
    /**
     * 调用方
     */
    protected String caller;

    /**
     * 是否归档 true 0 未删除 false 1 已删除
     */
    protected Integer archive;

    /**
     * 备注
     */
    protected String remark;




}
