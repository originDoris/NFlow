package com.doris.nflow.engine.flow.instance.service.impl;

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
import com.doris.nflow.engine.flow.instance.enumerate.FlowInstanceStatus;
import com.doris.nflow.engine.flow.instance.mapper.FlowInstanceMapper;
import com.doris.nflow.engine.flow.instance.model.FlowInstance;
import com.doris.nflow.engine.flow.instance.model.FlowInstanceQuery;
import com.doris.nflow.engine.flow.instance.service.FlowInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: FlowInstanceServiceImpl
 * @Description:
 * @date: 2022/9/30 11:09
 */
@Service
@Transactional
@Slf4j
public class FlowInstanceServiceImpl implements FlowInstanceService {

    private final FlowInstanceMapper flowInstanceMapper;

    public FlowInstanceServiceImpl(FlowInstanceMapper flowInstanceMapper) {
        this.flowInstanceMapper = flowInstanceMapper;
    }

    @Override
    public boolean save(FlowInstance flowInstance) {
        return flowInstanceMapper.insert(flowInstance) > 0;
    }

    @Override
    public boolean modify(FlowInstance flowInstance) {
        QueryWrapper<FlowInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(FlowInstance.FLOW_INSTANCE_CODE, flowInstance.getFlowInstanceCode());
        return flowInstanceMapper.update(flowInstance, wrapper) > 0;
    }

    @Override
    public boolean remove(String flowDeployCode) {
        QueryWrapper<FlowInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(FlowInstance.FLOW_INSTANCE_CODE, flowDeployCode);
        return flowInstanceMapper.delete(wrapper) > 0;
    }

    @Override
    public Optional<FlowInstance> detail(String flowDeployCode) {
        QueryWrapper<FlowInstance> wrapper = new QueryWrapper<>();
        wrapper.eq(FlowInstance.FLOW_INSTANCE_CODE,flowDeployCode);
        FlowInstance flowInstance = flowInstanceMapper.selectOne(wrapper);
        return Optional.ofNullable(flowInstance);
    }

    @Override
    public List<FlowInstance> queryList(FlowInstanceQuery flowInstanceQuery) {
        Page<FlowInstance> page = new Page<>(0, -1);
        IPage<FlowInstance> data = flowInstanceMapper.queryList(page, flowInstanceQuery);
        return data.getRecords();
    }

    @Override
    public IPage<FlowInstance> queryPage(FlowInstanceQuery flowInstanceQuery) {
        Page<FlowInstance> page = new Page<>(flowInstanceQuery.getPageNo(), flowInstanceQuery.getPageSize());
        return flowInstanceMapper.queryList(page, flowInstanceQuery);
    }

    @Override
    public boolean modifyStatus(FlowInstanceStatus status, String flowInstanceCode) throws ParamException {
        Optional<FlowInstance> detail = detail(flowInstanceCode);
        if (detail.isEmpty()) {
            throw new ParamException(ErrorCode.PARAM_INVALID,"未查询到流程实例信息，请确认！");
        }
        FlowInstance flowInstance = detail.get();
        flowInstance.setStatus(status.getCode());
        return modify(flowInstance);
    }
}
