package com.doris.nflow.engine.flow.definition.service.impl;

import com.doris.nflow.engine.flow.definition.mapper.FlowDefinitionMapper;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.definition.model.FlowDefinitionQuery;
import com.doris.nflow.engine.flow.definition.service.FlowDefinitionService;
import com.doris.nflow.engine.util.IdGenerator;
import com.doris.nflow.engine.util.StrongUuidGenerator;
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


    public static final IdGenerator idGenerator = new StrongUuidGenerator();
    private final FlowDefinitionMapper flowDefinitionMapper;

    public FlowDefinitionServiceImpl(FlowDefinitionMapper flowDefinitionMapper) {
        this.flowDefinitionMapper = flowDefinitionMapper;
    }

    @Override
    public boolean save(FlowDefinition flowDefinition) {
        return false;
    }

    @Override
    public boolean modify(FlowDefinition flowDefinition) {
        return false;
    }

    @Override
    public boolean remove(String flowModuleCode) {
        return false;
    }

    @Override
    public Optional<FlowDefinition> detail(String flowModuleCode) {
        return Optional.empty();
    }

    @Override
    public List<FlowDefinition> queryList(FlowDefinitionQuery flowDefinitionQuery) {
        return null;
    }
}
