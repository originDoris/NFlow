package com.doris.nflow.engine.node.instance.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: origindoris
 * @Title: NodeInstance
 * @Description: 节点实例
 * @date: 2022/10/1 12:20
 */
@Data
@TableName("node_instance")
public class NodeInstance extends BaseModel {

    public static final String NODE_INSTANCE_CODE = "node_instance_code";



    /**
     * 节点实例代码
     */
    @NotBlank(message = "节点实例代码不能为空！")
    private String nodeInstanceCode;

    /**
     * 上游节点实例代码
     */
    private String sourceNodeInstanceCode;

    /**
     * 实例数据代码
     */
    private String instanceDataCode;

    /**
     * 节点代码
     */
    @NotBlank(message = "节点代码不能为空！")
    private String nodeCode;

    /**
     * 上游节点代码
     */
    private String sourceNodeCode;

    /**
     * 流程实例代码
     */
    @NotBlank(message = "流程实例代码不能为空！")
    private String flowInstanceCode;

    /**
     * 流程发布代码
     */
    @NotBlank(message = "流程发布代码不能为空！")
    private String flowDeployCode;

    /**
     * 流程实例状态
     * {@link NodeInstanceStatus}
     */
    @NotBlank(message = "流程状态不能为空！")
    private String status;
}
