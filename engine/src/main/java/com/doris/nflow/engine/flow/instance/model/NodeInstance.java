package com.doris.nflow.engine.flow.instance.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author: xhz
 * @Title: NodeInstance
 * @Description: 节点实例
 * @date: 2022/10/1 12:20
 */
@Data
@TableName(value = "fw_node_instance",autoResultMap = true)
public class NodeInstance extends BaseModel {

    public static final String NODE_INSTANCE_CODE = "node_instance_code";

    public static final String FLOW_INSTANCE_CODE = "flow_instance_code";

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

    @TableField(exist = false)
    private String nodeName;

    @TableField(exist = false)
    private Map<String, Object> properties;

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

    @TableField(exist = false)
    private String operator;

    @TableField(exist = false)
    private String remark;
}
