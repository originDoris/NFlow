package com.doris.nflow.engine.flow.instance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceData;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceDataQuery;
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

}
