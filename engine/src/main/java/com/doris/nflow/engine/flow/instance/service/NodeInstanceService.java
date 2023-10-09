package com.doris.nflow.engine.flow.instance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.flow.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.NodeInstance;
import com.doris.nflow.engine.flow.instance.model.NodeInstanceQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: NodeInstanceService
 * @Description:
 * @date: 2022/9/30 08:59
 */
public interface NodeInstanceService{

    /**
     * 保存节点实例数据
     *
     * @param nodeInstance
     * @return
     */
    boolean save(@Valid @NotNull(message = "节点实例对象不能为空！") NodeInstance nodeInstance);


    /**
     * 更新节点实例数据
     * @param nodeInstance
     * @return
     */
    boolean modify(@Valid @NotNull(message = "节点实例对象不能为空！") NodeInstance nodeInstance);


    /**
     * 删除节点实例数据 逻辑删除
     * @param nodeInstanceCode 节点实例代码
     * @return
     */
    boolean remove(@NotBlank(message = "节点实例代码不能为空！") String nodeInstanceCode);


    /**
     * 查询节点实例详情
     * @param nodeInstanceCode 节点实例代码
     * @return
     */
    Optional<NodeInstance> detail(@NotBlank(message = "节点实例代码不能为空！") String nodeInstanceCode);


    /**
     * 查询节点实例列表
     * @param nodeInstanceQuery
     * @return
     */
    List<NodeInstance> queryList(@NotNull(message = "查询参数不能为空！") NodeInstanceQuery nodeInstanceQuery);


    /**
     * 查询节点实例列表 分页
     * @param nodeInstanceQuery
     * @return
     */
    IPage<NodeInstance> queryPage(@NotNull(message = "查询参数不能为空！") NodeInstanceQuery nodeInstanceQuery);



    /**
     * 通过上游节点代码查询节点实例信息
     *
     * @param flowInstanceCode   流程实例代码
     * @param sourceInstanceCode 上游实例代码
     * @param nodeCode           节点代码
     * @return
     */
    Optional<NodeInstance> detailBySourceInstanceCode(String flowInstanceCode, String sourceInstanceCode, String nodeCode);


    /**
     * 保存或更新数据
     * @param nodeInstanceList 节点实例列表
     * @return
     */
    boolean replace(@NotEmpty(message = "节点实例列表不能为空！") List<NodeInstance> nodeInstanceList);


    /**
     * 根据流程实例代码查询节点实例列表
     * @param flowInstanceCode 流程实例代码
     * @return
     */
    List<NodeInstance> queryListByFlowInstanceCode(@NotBlank(message = "流程实例代码不能为空！") String flowInstanceCode);
}
