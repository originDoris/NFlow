package com.doris.nflow.engine.processor.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: xhz
 * @Title: RollbackTaskParam
 * @Description:
 * @date: 2022/10/9 11:35
 */
@Data
public class RollbackTaskParam implements Serializable {
    @NotBlank(message = "流程实例代码不能为空！")
    private String flowInstanceCode;

    @NotBlank(message = "节点实例代码不能为空！")
    private String nodeInstanceCode;
}
