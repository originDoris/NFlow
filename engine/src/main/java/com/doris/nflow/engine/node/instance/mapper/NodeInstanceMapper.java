package com.doris.nflow.engine.node.instance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: origindoris
 * @Title: NodeInstanceMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface NodeInstanceMapper extends BaseMapper<NodeInstance> {

    /**
     * 查询列表
     * @param page 分页参数
     * @param nodeInstanceQuery 节点实例查询参数
     * @return
     */
    IPage<NodeInstance> queryList(IPage<NodeInstance> page, NodeInstanceQuery nodeInstanceQuery);

}
