package com.doris.nflow.engine.node.instance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceLog;
import com.doris.nflow.engine.node.instance.model.NodeInstanceLogQuery;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: origindoris
 * @Title: NodeInstanceLogMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface NodeInstanceLogMapper extends BaseMapper<NodeInstanceLog> {

    /**
     * 查询列表
     * @param page 分页参数
     * @param nodeInstanceLogQuery 节点实例日志查询参数
     * @return
     */
    IPage<NodeInstanceLog> queryList(IPage<NodeInstanceLog> page, NodeInstanceLogQuery nodeInstanceLogQuery);

}
