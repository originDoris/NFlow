package com.doris.nflow.engine.node.instance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstanceDataQuery;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: xhz
 * @Title: NodeInstanceDataMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface NodeInstanceDataMapper extends BaseMapper<NodeInstanceData> {

    /**
     * 查询列表
     *
     * @param page                  分页参数
     * @param nodeInstanceDataQuery 节点实例数据查询参数
     * @return
     */
    IPage<NodeInstanceData> queryList(IPage<NodeInstanceData> page, NodeInstanceDataQuery nodeInstanceDataQuery);


    NodeInstanceData detailByFlowInstanceCodeAndInstanceDataCode(@Param("flowInstanceCode") String flowInstanceCode, @Param("nodeInstanceDataCode") String nodeInstanceDataCode);
}
