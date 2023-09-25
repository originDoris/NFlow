package com.doris.nflow.engine.node.instance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.mapper.NodeInstanceDataMapper;
import com.doris.nflow.engine.node.instance.mapper.NodeInstanceMapper;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstanceDataQuery;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;
import com.doris.nflow.engine.node.instance.service.NodeInstanceDataService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: NodeInstanceDataServiceImpl
 * @Description:
 * @date: 2022/9/30 11:09
 */
@Service
@Transactional
@Slf4j
public class NodeInstanceDataServiceImpl implements NodeInstanceDataService {

    private final NodeInstanceDataMapper nodeInstanceDataMapper;

    public NodeInstanceDataServiceImpl(NodeInstanceDataMapper nodeInstanceDataMapper) {
        this.nodeInstanceDataMapper = nodeInstanceDataMapper;
    }

    @Override
    public boolean save(NodeInstanceData nodeInstanceData) {
        return nodeInstanceDataMapper.insert(nodeInstanceData) > 0;
    }

    @Override
    public boolean modify(NodeInstanceData nodeInstance) {
        QueryWrapper<NodeInstanceData> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstanceData.NODE_INSTANCE_DATA_CODE, nodeInstance.getInstanceDataCode());
        return nodeInstanceDataMapper.update(nodeInstance, wrapper) > 0;
    }

    @Override
    public boolean remove(String instanceDataCode) {
        QueryWrapper<NodeInstanceData> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstanceData.NODE_INSTANCE_DATA_CODE, instanceDataCode);
        return nodeInstanceDataMapper.delete(wrapper) > 0;
    }

    @Override
    public Optional<NodeInstanceData> detail(String instanceDataCode) {
        QueryWrapper<NodeInstanceData> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstanceData.NODE_INSTANCE_DATA_CODE, instanceDataCode);
        NodeInstanceData nodeInstanceData = nodeInstanceDataMapper.selectOne(wrapper);
        return Optional.ofNullable(nodeInstanceData);
    }

    @Override
    public List<NodeInstanceData> queryList(NodeInstanceDataQuery nodeInstanceDataQuery) {
        Page<NodeInstanceData> page = new Page<>(0, -1);
        IPage<NodeInstanceData> data = nodeInstanceDataMapper.queryList(page, nodeInstanceDataQuery);
        return data.getRecords();
    }

    @Override
    public IPage<NodeInstanceData> queryPage(NodeInstanceDataQuery nodeInstanceDataQuery) {
        Page<NodeInstanceData> page = new Page<>(nodeInstanceDataQuery.getPageNo(), nodeInstanceDataQuery.getPageSize());
        return nodeInstanceDataMapper.queryList(page, nodeInstanceDataQuery);
    }

    @Override
    public Optional<NodeInstanceData> detailByFlowInstanceCodeAndInstanceDataCode(String flowInstanceCode, String nodeInstanceDataCode) {
        NodeInstanceData nodeInstanceData = nodeInstanceDataMapper.detailByFlowInstanceCodeAndInstanceDataCode(flowInstanceCode, nodeInstanceDataCode);
        return Optional.ofNullable(nodeInstanceData);
    }
}
