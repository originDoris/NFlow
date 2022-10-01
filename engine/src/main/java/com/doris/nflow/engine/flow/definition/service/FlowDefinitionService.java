package com.doris.nflow.engine.flow.definition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.flow.definition.enumerate.FlowDefinitionStatus;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.definition.model.FlowDefinitionQuery;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: FlowDefinitionService
 * @Description:
 * @date: 2022/9/30 08:59
 */
public interface FlowDefinitionService{

    /**
     * 保存流程定义数据
     *
     * @param flowDefinition
     * @return
     */
    boolean save(@Valid @NotNull(message = "流程定义对象不能为空！") FlowDefinition flowDefinition);


    /**
     * 更新流程定义数据
     * @param flowDefinition
     * @return
     */
    boolean modify(@Valid @NotNull(message = "流程定义对象不能为空！") FlowDefinition flowDefinition);


    /**
     * 删除流程定义数据 逻辑删除
     * @param flowModuleCode 流程定义模块代码
     * @return
     */
    boolean remove(@NotBlank(message = "流程定义模块代码不能为空！") String flowModuleCode);


    /**
     * 查询流程定义模块详情
     * @param flowModuleCode 流程定义模块代码
     * @return
     */
    Optional<FlowDefinition> detail(@NotBlank(message = "流程模块代码不能为空！") String flowModuleCode);


    /**
     * 查询流程列表
     * @param flowDefinitionQuery
     * @return
     */
    List<FlowDefinition> queryList(@NotNull(message = "查询参数不能为空！") FlowDefinitionQuery flowDefinitionQuery);


    /**
     * 查询流程列表 分页
     * @param flowDefinitionQuery
     * @return
     */
    IPage<FlowDefinition> queryPage(@NotNull(message = "查询参数不能为空！") FlowDefinitionQuery flowDefinitionQuery);


    /**
     * 更新流程定义状态
     * @param status 状态枚举
     * @param flowModuleCode 流程模块代码
     * @return
     */
    boolean modifyStatus(@NotNull(message = "状态枚举不能为空！") FlowDefinitionStatus status,
                         @NotBlank(message = "流程模块代码不能为空！") String flowModuleCode) throws ParamException;

}
