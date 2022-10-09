package com.doris.nflow.engine.processor.model.param;

import com.doris.nflow.engine.node.instance.model.InstanceData;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author: origindoris
 * @Title: CommitTaskParam
 * @Description:
 * @date: 2022/10/9 10:58
 */
@Data
public class CommitTaskParam {

    @NotBlank(message = "流程实例代码不能为空！")
    private String flowInstanceCode;

    @NotBlank(message = "节点实例代码不能为空！")
    private String nodeInstanceCode;

    private List<InstanceData> params;

}
