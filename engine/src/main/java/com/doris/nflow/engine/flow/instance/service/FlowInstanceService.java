package com.doris.nflow.engine.flow.instance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.flow.definition.enumerate.FlowDefinitionStatus;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.definition.model.FlowDefinitionQuery;
import com.doris.nflow.engine.flow.instance.enumerate.FlowInstanceStatus;
import com.doris.nflow.engine.flow.instance.model.FlowInstance;
import com.doris.nflow.engine.flow.instance.model.FlowInstanceQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: FlowInstanceService
 * @Description:
 * @date: 2022/9/30 08:59
 */
public interface FlowInstanceService{

    /**
     * 保存流程实例数据
     *
     * @param flowInstance
     * @return
     */
    boolean save(@Valid @NotNull(message = "流程实例对象不能为空！") FlowInstance flowInstance);


    /**
     * 更新流程实例数据
     * @param flowInstance
     * @return
     */
    boolean modify(@Valid @NotNull(message = "流程实例对象不能为空！") FlowInstance flowInstance);


    /**
     * 删除流程实例数据 逻辑删除
     * @param flowInstanceCode 流程实例代码
     * @return
     */
    boolean remove(@NotBlank(message = "流程实例代码不能为空！") String flowInstanceCode);


    /**
     * 查询流程实例详情
     * @param flowInstanceCode 流程实例代码
     * @return
     */
    Optional<FlowInstance> detail(@NotBlank(message = "流程实例代码不能为空！") String flowInstanceCode);


    /**
     * 查询流程实例列表
     * @param flowInstanceQuery
     * @return
     */
    List<FlowInstance> queryList(@NotNull(message = "查询参数不能为空！") FlowInstanceQuery flowInstanceQuery);


    /**
     * 查询流程实例列表 分页
     * @param flowInstanceQuery
     * @return
     */
    IPage<FlowInstance> queryPage(@NotNull(message = "查询参数不能为空！") FlowInstanceQuery flowInstanceQuery);


    /**
     * 更新流程实例状态
     * @param status 状态枚举
     * @param flowInstanceCode 流程实例代码
     * @return
     */
    boolean modifyStatus(@NotNull(message = "状态枚举不能为空！") FlowInstanceStatus status,
                         @NotBlank(message = "流程实例代码不能为空！") String flowInstanceCode) throws ParamException;

}
