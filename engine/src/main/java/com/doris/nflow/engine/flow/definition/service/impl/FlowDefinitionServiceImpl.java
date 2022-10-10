package com.doris.nflow.engine.flow.definition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.flow.definition.enumerate.FlowDefinitionStatus;
import com.doris.nflow.engine.flow.definition.mapper.FlowDefinitionMapper;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.definition.model.FlowDefinitionQuery;
import com.doris.nflow.engine.flow.definition.service.FlowDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: FlowDefinitionServiceImpl
 * @Description:
 * @date: 2022/9/30 11:09
 */
@Service
@Transactional
@Slf4j
public class FlowDefinitionServiceImpl implements FlowDefinitionService {

    private final FlowDefinitionMapper flowDefinitionMapper;

    public FlowDefinitionServiceImpl(FlowDefinitionMapper flowDefinitionMapper) {
        this.flowDefinitionMapper = flowDefinitionMapper;
    }

    @Override
    public boolean save(FlowDefinition flowDefinition) {
        return flowDefinitionMapper.insert(flowDefinition) > 0;
    }

    @Override
    public boolean modify(FlowDefinition flowDefinition) {
        QueryWrapper<FlowDefinition> wrapper = new QueryWrapper<FlowDefinition>();
        wrapper.eq(FlowDefinition.FLOW_MODULE_CODE, flowDefinition.getFlowModuleCode());
        return flowDefinitionMapper.update(flowDefinition, wrapper) > 0;
    }

    @Override
    public boolean remove(String flowModuleCode) {
        QueryWrapper<FlowDefinition> wrapper = new QueryWrapper<FlowDefinition>();
        wrapper.eq(FlowDefinition.FLOW_MODULE_CODE, flowModuleCode);
        return flowDefinitionMapper.delete(wrapper) > 0;
    }

    @Override
    public Optional<FlowDefinition> detail(String flowModuleCode) {
        QueryWrapper<FlowDefinition> wrapper = new QueryWrapper<FlowDefinition>();
        wrapper.eq(FlowDefinition.FLOW_MODULE_CODE, flowModuleCode);
        FlowDefinition flowDefinition = flowDefinitionMapper.selectOne(wrapper);
        return Optional.ofNullable(flowDefinition);
    }

    @Override
    public List<FlowDefinition> queryList(FlowDefinitionQuery flowDefinitionQuery) {
        Page<FlowDefinition> page = new Page<>(0, -1);
        IPage<FlowDefinition> data = flowDefinitionMapper.queryList(page, flowDefinitionQuery);
        return data.getRecords();
    }

    @Override
    public IPage<FlowDefinition> queryPage(FlowDefinitionQuery flowDefinitionQuery) {
        Page<FlowDefinition> page = new Page<>(flowDefinitionQuery.getPageNo(), flowDefinitionQuery.getPageSize());
        return flowDefinitionMapper.queryList(page, flowDefinitionQuery);
    }


    @Override
    public boolean modifyStatus(FlowDefinitionStatus status, String flowModuleCode) throws ParamException {
        Optional<FlowDefinition> detail = detail(flowModuleCode);
        if (detail.isEmpty()) {
            throw new ParamException(ErrorCode.PARAM_INVALID,"未查询到流程定义信息，请确认！");
        }
        FlowDefinition flowDefinition = detail.get();
        flowDefinition.setStatus(status.getCode());
        return modify(flowDefinition);
    }
}
