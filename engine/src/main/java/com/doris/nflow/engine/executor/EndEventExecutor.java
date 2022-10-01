package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.DefinitionException;
import com.doris.nflow.engine.common.model.node.BaseNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: origindoris
 * @Title: EndEventExecutor
 * @Description:
 * @date: 2022/10/1 09:23
 */
@Component(NodeTypeConstant.END_EVENT_NODE)
@Slf4j
public class EndEventExecutor extends BaseNodeExecutor {

    @Override
    protected void checkOutput(BaseNode baseNode) throws DefinitionException {
        List<String> output = baseNode.getOutput();
        if (CollectionUtils.isNotEmpty(output)) {
            recordException(baseNode, ErrorCode.NODE_TOO_MUCH_OUTPUT);
        }
    }
}
