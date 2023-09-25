package com.doris.nflow.engine.flow.deployment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.definition.model.FlowDefinitionQuery;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.model.FlowDeploymentQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: xhz
 * @Title: FlowDefinitionMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface FlowDeploymentMapper extends BaseMapper<FlowDeployment> {

    /**
     * 查询列表
     * @param page 分页参数
     * @param flowDeploymentQuery 流程部署查询参数
     * @return
     */
    IPage<FlowDeployment> queryList(IPage<FlowDeployment> page, FlowDeploymentQuery flowDeploymentQuery);

}
