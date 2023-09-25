package com.doris.nflow.engine.node.instance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.mapper.NodeInstanceMapper;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import com.doris.nflow.engine.node.instance.service.NodeInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean modifyStatus(NodeInstanceStatus status, String nodeInstanceCode) throws ParamException {
        Optional<NodeInstance> detail = detail(nodeInstanceCode);
        if (detail.isEmpty()) {
            throw new ParamException(ErrorCode.PARAM_INVALID,"未查询到流程实例信息，请确认！");
        }
        NodeInstance nodeInstance = detail.get();
        nodeInstance.setStatus(status.getCode());
        return modify(nodeInstance);
    }


    @Override
    public Optional<NodeInstance> detailBySourceInstanceCode(String flowInstanceCode, String sourceInstanceCode, String nodeCode) {
        NodeInstance nodeInstance = nodeInstanceMapper.detailBySourceInstanceCode(flowInstanceCode, sourceInstanceCode, nodeCode);
        return Optional.ofNullable(nodeInstance);
    }


    @Override
    public boolean replace(List<NodeInstance> nodeInstanceList) {
       return nodeInstanceMapper.replace(nodeInstanceList);
    }


    @Override
    public List<NodeInstance> queryListByFlowInstanceCode(String flowInstanceCode) {
        QueryWrapper<NodeInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(NodeInstance.FLOW_INSTANCE_CODE, flowInstanceCode);
        return nodeInstanceMapper.selectList(wrapper);
    }
}
