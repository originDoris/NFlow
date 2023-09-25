package com.doris.nflow.engine.processor.model.result;

import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import lombok.Data;

import java.util.List;

/**
 * @author: xhz
 * @Title: RuntimeResult
 * @Description:
 * @date: 2022/10/9 10:02
 */
@Data
public class RuntimeResult extends CommonResult {

    private String flowInstanceCode;

    private String status;

    private NodeInstance activeTaskInstance;

    private List<InstanceData> params;
}
