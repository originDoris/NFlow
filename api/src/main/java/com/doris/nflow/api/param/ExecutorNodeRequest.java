package com.doris.nflow.api.param;

import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: origindoris
 * @Title: ExecutorNodeRequest
 * @Description:
 * @date: 2023/10/11 17:29
 */
@Data
public class ExecutorNodeRequest implements Serializable {
    private String flowModuleCode;

    private BaseNode baseNode;

    private List<InstanceData> params;
}
