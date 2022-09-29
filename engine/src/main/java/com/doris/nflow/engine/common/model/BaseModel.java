package com.doris.nflow.engine.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: origindoris
 * @Title: BaseModel
 * @Description:
 * @date: 2022/9/29 10:40
 */
@Data
public class BaseModel implements Serializable {
    protected Long id;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 修改时间
     */
    protected Date modifyTime;

    /**
     * 操作人
     */
    protected String operator;


}
