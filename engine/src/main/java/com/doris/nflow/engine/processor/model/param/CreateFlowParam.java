package com.doris.nflow.engine.processor.model.param;

import com.doris.nflow.engine.common.model.node.BaseNode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: xhz
 * @Title: CreateFlowParam
 * @Description:
 * @date: 2022/10/8 10:06
 */
@Data
public class CreateFlowParam extends CommonParam {

    @NotBlank(message = "流程名称不能为空！")
    private String flowName;

    /**
     * 流程模型列表
     */
    @NotEmpty(message = "流程定义模块不能为空！")
    private List<BaseNode> flowModule;


    private String remark;

    private String content;

}
