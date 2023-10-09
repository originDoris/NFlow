package com.doris.nflow.engine.flow.instance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: xhz
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


    /**
     * 通过上游节点代码查询节点实例信息
     *
     * @param flowInstanceCode   流程实例代码
     * @param sourceInstanceCode 上游实例代码
     * @param nodeCode           节点代码
     * @return
     */
    NodeInstance detailBySourceInstanceCode(@Param("flowInstanceCode") String flowInstanceCode,
                                            @Param("sourceInstanceCode") String sourceInstanceCode,
                                            @Param("nodeCode") String nodeCode);




    /**
     * 批量新增
     * @param instances
     * @return
     */
    boolean batchSave(@Param("list") List<NodeInstance> instances);


    boolean batchUpdate(@Param("list") List<NodeInstance> instances);
}
