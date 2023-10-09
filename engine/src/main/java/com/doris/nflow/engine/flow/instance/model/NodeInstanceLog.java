package com.doris.nflow.engine.flow.instance.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceLogType;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: xhz
 * @Title: NodeInstanceLog
 * @Description: 节点实例
 * @date: 2022/10/1 12:20
 */
@Data
@TableName(value = "node_instance_log",autoResultMap = true)
public class NodeInstanceLog extends BaseModel {

    public static final String NODE_INSTANCE_CODE = "node_instance_code";

    /**
     * 节点实例代码
     */
    @NotBlank(message = "节点实例代码不能为空！")
    private String nodeInstanceCode;

    /**
     * 操作类型
     * {@link NodeInstanceLogType}
     */
    @NotBlank(message = "操作类型不能为空！")
    private String type;

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
     * 流程实例状态
     * {@link NodeInstanceStatus}
     */
    @NotBlank(message = "流程状态不能为空！")
    private String status;
}
