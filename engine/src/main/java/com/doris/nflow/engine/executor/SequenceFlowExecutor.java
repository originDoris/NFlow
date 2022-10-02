package com.doris.nflow.engine.executor;

import com.doris.nflow.engine.common.constant.NodeTypeConstant;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: origindoris
 * @Title: SequenceFlowExecutor
 * @Description:
 * @date: 2022/10/2 18:48
 */
@Component(NodeTypeConstant.SEQUENCE_FLOW_NODE)
@Slf4j
public class SequenceFlowExecutor extends RuntimeExecutor {

    public SequenceFlowExecutor(NodeInstanceService nodeInstanceService) {
        super(nodeInstanceService);
    }




}

