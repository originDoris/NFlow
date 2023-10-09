package com.doris.nflow.engine.flow.instance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceLog;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceLogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: xhz
 * @Title: NodeInstanceLogMapper
 * @Description:
 * @date: 2022/9/30 08:56
 */
@Mapper
public interface NodeInstanceLogMapper extends BaseMapper<NodeInstanceLog> {

    /**
     * 查询列表
     *
     * @param page                 分页参数
     * @param nodeInstanceLogQuery 节点实例日志查询参数
     * @return
     */
    IPage<NodeInstanceLog> queryList(IPage<NodeInstanceLog> page, NodeInstanceLogQuery nodeInstanceLogQuery);



    boolean batchSave(@Param("list") List<NodeInstanceLog> nodeInstanceLogList);


    boolean batchUpdate(@Param("list") List<NodeInstanceLog> nodeInstanceLogList);
}
