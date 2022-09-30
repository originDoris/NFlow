package com.doris.nflow.engine.flow.definition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: origindoris
 * @Title: FlowDefinitionMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface FlowDefinitionMapper extends BaseMapper<FlowDefinition> {
}
