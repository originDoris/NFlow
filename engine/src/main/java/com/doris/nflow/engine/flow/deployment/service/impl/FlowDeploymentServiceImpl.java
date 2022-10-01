package com.doris.nflow.engine.flow.deployment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.deployment.enumerate.FlowDeploymentStatus;
import com.doris.nflow.engine.flow.deployment.mapper.FlowDeploymentMapper;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.model.FlowDeploymentQuery;
import com.doris.nflow.engine.flow.deployment.service.FlowDeploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: FlowDeploymentServiceImpl
 * @Description:
 * @date: 2022/10/1 11:21
 */
@Service
@Transactional
@Slf4j
public class FlowDeploymentServiceImpl implements FlowDeploymentService {

    private final FlowDeploymentMapper flowDeploymentMapper;

    public FlowDeploymentServiceImpl(FlowDeploymentMapper flowDeploymentMapper) {
        this.flowDeploymentMapper = flowDeploymentMapper;
    }

    @Override
    public boolean save(FlowDeployment flowDeployment) {
        return flowDeploymentMapper.insert(flowDeployment) > 0;
    }

    @Override
    public boolean modify(FlowDeployment flowDeployment) {
        QueryWrapper<FlowDeployment> wrapper = new QueryWrapper<>();
        wrapper.eq(FlowDeployment.FLOW_DEPLOY_CODE, flowDeployment.getFlowDeployCode());
        return flowDeploymentMapper.update(flowDeployment, wrapper) > 0;
    }

    @Override
    public boolean remove(String flowDeployCode) {
        QueryWrapper<FlowDeployment> wrapper = new QueryWrapper<>();
        wrapper.eq(FlowDeployment.FLOW_DEPLOY_CODE, flowDeployCode);
        return flowDeploymentMapper.delete(wrapper) > 0;
    }

    @Override
    public Optional<FlowDeployment> detail(String flowDeployCode) {
        QueryWrapper<FlowDeployment> wrapper = new QueryWrapper<>();
        wrapper.eq(FlowDeployment.FLOW_DEPLOY_CODE, flowDeployCode);
        FlowDeployment flowDeployment = flowDeploymentMapper.selectOne(wrapper);
        return Optional.ofNullable(flowDeployment);
    }

    @Override
    public List<FlowDeployment> queryList(FlowDeploymentQuery flowDeploymentQuery) {
        Page<FlowDeployment> page = new Page<>(0, -1);
        IPage<FlowDeployment> data = flowDeploymentMapper.queryList(page, flowDeploymentQuery);
        return data.getRecords();
    }

    @Override
    public IPage<FlowDeployment> queryPage(FlowDeploymentQuery flowDeploymentQuery) {
        Page<FlowDeployment> page = new Page<>(flowDeploymentQuery.getPageNo(), flowDeploymentQuery.getPageSize());
        return flowDeploymentMapper.queryList(page, flowDeploymentQuery);
    }

    @Override
    public boolean modifyStatus(FlowDeploymentStatus status, String flowDeployCode) throws ParamException {
        Optional<FlowDeployment> detail = detail(flowDeployCode);
        if (detail.isEmpty()) {
            throw new ParamException(ErrorCode.PARAM_INVALID,"未查询到流程发布信息，请确认！");
        }
        FlowDeployment flowDeployment = detail.get();
        flowDeployment.setStatus(status.getCode());
        return modify(flowDeployment);
    }
}
