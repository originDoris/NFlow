package com.doris.nflow.engine.node.instance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceData;
import com.doris.nflow.engine.node.instance.model.NodeInstanceDataQuery;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: NodeInstanceDataService
 * @Description:
 * @date: 2022/9/30 08:59
 */
public interface NodeInstanceDataService {

    /**
     * 保存节点实例数据数据
     *
     * @param nodeInstanceData
     * @return
     */
    boolean save(@Valid @NotNull(message = "节点实例数据对象不能为空！") NodeInstanceData nodeInstanceData);


    /**
     * 更新节点实例数据数据
     * @param nodeInstanceData
     * @return
     */
    boolean modify(@Valid @NotNull(message = "节点实例数据对象不能为空！") NodeInstanceData nodeInstanceData);


    /**
     * 删除节点实例数据数据 逻辑删除
     * @param instanceDataCode 节点实例数据代码
     * @return
     */
    boolean remove(@NotBlank(message = "节点实例数据代码不能为空！") String instanceDataCode);


    /**
     * 查询节点实例数据详情
     * @param instanceDataCode 节点实例数据代码
     * @return
     */
    Optional<NodeInstanceData> detail(@NotBlank(message = "节点实例数据代码不能为空！") String instanceDataCode);


    /**
     * 查询节点实例数据列表
     * @param nodeInstanceDataQuery
     * @return
     */
    List<NodeInstanceData> queryList(@NotNull(message = "查询参数不能为空！") NodeInstanceDataQuery nodeInstanceDataQuery);


    /**
     * 查询节点实例数据列表 分页
     * @param nodeInstanceDataQuery
     * @return
     */
    IPage<NodeInstanceData> queryPage(@NotNull(message = "查询参数不能为空！") NodeInstanceDataQuery nodeInstanceDataQuery);


    /**
     * 通过流程实例代码和节点实例数据代码查询
     * @param flowInstanceCode 流程实例代码
     * @param nodeInstanceDataCode 节点实例数据代码
     * @return
     */
    Optional<NodeInstanceData> detailByFlowInstanceCodeAndInstanceDataCode(@NotBlank(message = "流程实例代码不能为空！") String flowInstanceCode,
                                                                 @NotBlank(message = "节点实例数据代码不能为空！") String nodeInstanceDataCode);


}
