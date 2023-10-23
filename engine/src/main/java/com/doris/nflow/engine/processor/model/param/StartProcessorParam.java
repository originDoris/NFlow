package com.doris.nflow.engine.processor.model.param;

import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.instance.model.InstanceData;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: xhz
 * @Title: StartProcessModel
 * @Description:
 * @date: 2022/10/9 09:58
 */
@Data
public class StartProcessorParam implements Serializable {

    private String flowDeployCode;

    private String flowModuleCode;

    private List<InstanceData> params;

    private Map<String, Object> apiParams;

    private String webSocketKey;

    private List<BaseNode> flowModule;
}
