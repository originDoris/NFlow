package com.doris.nflow.engine.common.model.param;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author: origindoris
 * @Title: CommonParam
 * @Description:
 * @date: 2022/10/8 10:01
 */
@Data
public class CommonParam implements Serializable {
    private String tenantCode;

    private String caller;
}
