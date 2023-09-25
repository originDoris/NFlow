package com.doris.nflow.engine.node.instance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.mapper.NodeInstanceLogMapper;
import com.doris.nflow.engine.node.instance.mapper.NodeInstanceMapper;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceLog;
import com.doris.nflow.engine.node.instance.model.NodeInstanceLogQuery;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;
import com.doris.nflow.engine.node.instance.service.NodeInstanceLogService;
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
public class NodeInstanceLogServiceImpl implements NodeInstanceLogService {

    private final NodeInstanceLogMapper nodeInstanceLogMapper;

    public NodeInstanceLogServiceImpl(NodeInstanceLogMapper nodeInstanceLogMapper) {
        this.nodeInstanceLogMapper = nodeInstanceLogMapper;
    }

    @Override
    public boolean save(NodeInstanceLog nodeInstanceLog) {
        return nodeInstanceLogMapper.insert(nodeInstanceLog) > 0;
    }

    @Override
    public boolean modify(NodeInstanceLog nodeInstanceLog) {
        return nodeInstanceLogMapper.updateById(nodeInstanceLog) > 0;
    }

    @Override
    public boolean remove(Long id) {
        return nodeInstanceLogMapper.deleteById(id) > 0;
    }

    @Override
    public Optional<NodeInstanceLog> detail(Long id) {
        NodeInstanceLog nodeInstanceLog = nodeInstanceLogMapper.selectById(id);
        return Optional.ofNullable(nodeInstanceLog);
    }

    @Override
    public List<NodeInstanceLog> queryList(NodeInstanceLogQuery nodeInstanceLogQuery) {
        Page<NodeInstanceLog> page = new Page<>(0, -1);
        IPage<NodeInstanceLog> data = nodeInstanceLogMapper.queryList(page, nodeInstanceLogQuery);
        return data.getRecords();
    }

    @Override
    public IPage<NodeInstanceLog> queryPage(NodeInstanceLogQuery nodeInstanceLogQuery) {
        Page<NodeInstanceLog> page = new Page<>(nodeInstanceLogQuery.getPageNo(), nodeInstanceLogQuery.getPageSize());
        return nodeInstanceLogMapper.queryList(page, nodeInstanceLogQuery);
    }

    @Override
    public boolean replace(List<NodeInstanceLog> nodeInstanceLogs) {
        return nodeInstanceLogMapper.replace(nodeInstanceLogs);
    }
}
