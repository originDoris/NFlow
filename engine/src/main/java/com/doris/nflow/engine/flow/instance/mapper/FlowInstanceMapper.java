package com.doris.nflow.engine.flow.instance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.model.FlowDeploymentQuery;
import com.doris.nflow.engine.flow.instance.model.FlowInstance;
import com.doris.nflow.engine.flow.instance.model.FlowInstanceQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: origindoris
 * @Title: FlowDefinitionMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface FlowInstanceMapper extends BaseMapper<FlowInstance> {

    /**
     * 查询列表
     * @param page 分页参数
     * @param flowInstanceQuery 流程实例查询参数
     * @return
     */
    IPage<FlowInstance> queryList(IPage<FlowInstance> page, FlowInstanceQuery flowInstanceQuery);

}
