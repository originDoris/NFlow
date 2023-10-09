package com.doris.nflow.engine.flow.deployment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.deployment.enumerate.FlowDeploymentStatus;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.model.FlowDeploymentQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: FlowDeploymentService
 * @Description:
 * @date: 2022/10/1 11:21
 */
public interface FlowDeploymentService {

    /**
     * 保存流程发布数据
     *
     * @param flowDeployment
     * @return
     */
    boolean save(@Valid @NotNull(message = "流程发布对象不能为空！") FlowDeployment flowDeployment);


    /**
     * 更新流程发布数据
     * @param flowDeployment
     * @return
     */
    boolean modify(@Valid @NotNull(message = "流程发布对象不能为空！") FlowDeployment flowDeployment);


    /**
     * 删除流程发布数据 逻辑删除
     * @param flowDeployCode 流程发布代码
     * @return
     */
    boolean remove(@NotBlank(message = "流程发布代码不能为空！") String flowDeployCode);


    /**
     * 查询流程发布模块详情
     * @param flowDeployCode 流程发布代码
     * @return
     */
    Optional<FlowDeployment> detail(@NotBlank(message = "流程发布代码不能为空！") String flowDeployCode);


    /**
     * 查询流程发布列表
     * @param flowDeploymentQuery
     * @return
     */
    List<FlowDeployment> queryList(@NotNull(message = "查询参数不能为空！") FlowDeploymentQuery flowDeploymentQuery);


    /**
     * 查询流程发布列表 分页
     * @param flowDeploymentQuery
     * @return
     */
    IPage<FlowDeployment> queryPage(@NotNull(message = "查询参数不能为空！") FlowDeploymentQuery flowDeploymentQuery);



}
