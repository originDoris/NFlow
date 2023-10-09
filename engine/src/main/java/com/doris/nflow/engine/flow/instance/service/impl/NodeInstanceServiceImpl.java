package com.doris.nflow.engine.flow.instance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceQuery;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.flow.instance.mapper.NodeInstanceMapper;
import com.doris.nflow.engine.flow.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: NodeInstanceServiceImpl
 * @Description:
 * @date: 2022/9/30 11:09
 */
@Service
@Transactional
@Slf4j
public class NodeInstanceServiceImpl implements NodeInstanceService {

    private final NodeInstanceMapper nodeInstanceMapper;

    public NodeInstanceServiceImpl(NodeInstanceMapper nodeInstanceMapper) {
        this.nodeInstanceMapper = nodeInstanceMapper;
    }

    @Override
    public boolean save(NodeInstance nodeInstance) {
        return nodeInstanceMapper.insert(nodeInstance) > 0;
    }

    @Override
    public boolean modify(NodeInstance nodeInstance) {
        QueryWrapper<NodeInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstance.NODE_INSTANCE_CODE, nodeInstance.getNodeInstanceCode());
        return nodeInstanceMapper.update(nodeInstance, wrapper) > 0;
    }

    @Override
    public boolean remove(String nodeInstanceCode) {
        QueryWrapper<NodeInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstance.NODE_INSTANCE_CODE, nodeInstanceCode);
        return nodeInstanceMapper.delete(wrapper) > 0;
    }

    @Override
    public Optional<NodeInstance> detail(String nodeInstanceCode) {
        QueryWrapper<NodeInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstance.NODE_INSTANCE_CODE,nodeInstanceCode);
        NodeInstance nodeInstance = nodeInstanceMapper.selectOne(wrapper);
        return Optional.ofNullable(nodeInstance);
    }

    @Override
    public List<NodeInstance> queryList(NodeInstanceQuery nodeInstanceQuery) {
        Page<NodeInstance> page = new Page<>(0, -1);
        IPage<NodeInstance> data = nodeInstanceMapper.queryList(page, nodeInstanceQuery);
        return data.getRecords();
    }

    @Override
    public IPage<NodeInstance> queryPage(NodeInstanceQuery nodeInstanceQuery) {
        Page<NodeInstance> page = new Page<>(nodeInstanceQuery.getPageNo(), nodeInstanceQuery.getPageSize());
        return nodeInstanceMapper.queryList(page, nodeInstanceQuery);
    }


    @Override
    public Optional<NodeInstance> detailBySourceInstanceCode(String flowInstanceCode, String sourceInstanceCode, String nodeCode) {
        NodeInstance nodeInstance = nodeInstanceMapper.detailBySourceInstanceCode(flowInstanceCode, sourceInstanceCode, nodeCode);
        return Optional.ofNullable(nodeInstance);
    }


    @Override
    public boolean replace(List<NodeInstance> nodeInstanceList) {
        ArrayList<NodeInstance> saveNodeInstance = new ArrayList<>();
        ArrayList<NodeInstance> modifyNodeInstance = new ArrayList<>();
        for (NodeInstance nodeInstance : nodeInstanceList) {
            if (nodeInstance.getId() == null) {
                saveNodeInstance.add(nodeInstance);
            }else{
                modifyNodeInstance.add(nodeInstance);
            }
        }
        if (!saveNodeInstance.isEmpty()) {
            nodeInstanceMapper.batchSave(saveNodeInstance);
        }
        if (!modifyNodeInstance.isEmpty()) {
            nodeInstanceMapper.batchUpdate(modifyNodeInstance);
        }
        return true;
    }


    @Override
    public List<NodeInstance> queryListByFlowInstanceCode(String flowInstanceCode) {
        QueryWrapper<NodeInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstance.FLOW_INSTANCE_CODE, flowInstanceCode);
        return nodeInstanceMapper.selectList(wrapper);
    }
}
