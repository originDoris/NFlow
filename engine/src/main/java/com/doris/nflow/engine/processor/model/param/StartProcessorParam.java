package com.doris.nflow.engine.processor.model.param;

import com.doris.nflow.engine.node.instance.model.InstanceData;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author: xhz
 * @Title: StartProcessModel
 * @Description:
 * @date: 2022/10/9 09:58
 */
@Data
public class StartProcessorParam implements Serializable {

    @NotBlank(message = "流程发布代码不能为空！")
    private String flowDeployCode;

    @NotBlank(message = "流程模块代码不能为空！")
    private String flowModuleCode;

    private List<InstanceData> params;
}
