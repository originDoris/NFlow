package com.doris.nflow.engine.node.instance.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceDataType;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: origindoris
 * @Title: NodeInstance
 * @Description: 节点实例
 * @date: 2022/10/1 12:20
 */
@Data
@TableName("instance_data")
public class NodeInstanceData extends BaseModel {

    public static final String NODE_INSTANCE_CODE = "node_instance_code";

    public static final String NODE_INSTANCE_DATA_CODE = "instance_data_code";

    /**
     * 节点实例代码
     */
    @NotBlank(message = "节点实例代码不能为空！")
    private String nodeInstanceCode;

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
     * 流程实例代码
     */
    @NotBlank(message = "流程实例代码不能为空！")
    private String flowInstanceCode;

    /**
     * 流程发布代码
     */
    @NotBlank(message = "流程发布代码不能为空！")
    private String flowDeployCode;

    @NotBlank(message = "流程模块代码不能为空！")
    private String flowModuleCode;

    /**
     * 流程实例状态
     * {@link NodeInstanceStatus}
     */
    @NotBlank(message = "流程状态不能为空！")
    private String status;


    /**
     * 操作类型
     * {@link  NodeInstanceDataType}
     */
    private String type;
}
