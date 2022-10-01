package com.doris.nflow.engine.flow.definition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.definition.model.FlowDefinitionQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: origindoris
 * @Title: FlowDefinitionMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface FlowDefinitionMapper extends BaseMapper<FlowDefinition> {

    /**
     * 查询列表
     * @param page 分页参数
     * @param flowDefinitionQuery 流程定义查询参数
     * @return
     */
    IPage<FlowDefinition> queryList(IPage<FlowDefinition> page, FlowDefinitionQuery flowDefinitionQuery);

}
