package com.doris.nflow.engine.flow.instance.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doris.nflow.engine.common.model.BaseModel;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceDataType;
import com.doris.nflow.engine.flow.instance.handler.InstanceDataHandler;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author: xhz
 * @Title: NodeInstance
 * @Description: 节点实例
 * @date: 2022/10/1 12:20
 */
@Data
@TableName(value = "instance_data", autoResultMap = true)
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


    @TableField(typeHandler = InstanceDataHandler.class)
    private List<InstanceData> instanceData;


    /**
     * 操作类型
     * {@link  NodeInstanceDataType}
     */
    private String type;
}
