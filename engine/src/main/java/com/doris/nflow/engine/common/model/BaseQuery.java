package com.doris.nflow.engine.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: origindoris
 * @Title: BaseQuery
 * @Description:
 * @date: 2022/9/30 11:00
 */
@Data
public class BaseQuery implements Serializable {

    private Integer pageSize;

    private Integer pageNo;

    private String search;

}
