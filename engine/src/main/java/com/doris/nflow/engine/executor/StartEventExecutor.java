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
 * @Title: StartEventExecutor
 * @Description:
 * @date: 2022/10/1 09:11
 */
@Component(NodeTypeConstant.START_EVENT_NODE)
@Slf4j
public class StartEventExecutor extends BaseNodeExecutor {
    @Override
    protected void checkInput(BaseNode baseNode) throws DefinitionException {
        List<String> input = baseNode.getInput();
        if (CollectionUtils.isNotEmpty(input)) {
            recordException(baseNode, ErrorCode.NODE_TOO_MUCH_INPUT);
        }
    }
}
