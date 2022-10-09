package com.doris.nflow.engine.processor;

import com.doris.nflow.engine.common.enumerate.ErrorCode;
import com.doris.nflow.engine.common.exception.DefinitionException;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.processor.model.param.CreateFlowParam;
import com.doris.nflow.engine.processor.model.param.DeployFlowParam;
import com.doris.nflow.engine.processor.model.param.EditFlowParam;
import com.doris.nflow.engine.processor.model.param.ModifyFlowParam;
import com.doris.nflow.engine.processor.model.result.CreateFlowResult;
import com.doris.nflow.engine.processor.model.result.DeployFlowResult;
import com.doris.nflow.engine.processor.model.result.EditFlowResult;
import com.doris.nflow.engine.processor.model.result.ModifyFlowResult;
import com.doris.nflow.engine.flow.definition.enumerate.FlowDefinitionStatus;
import com.doris.nflow.engine.flow.definition.model.FlowDefinition;
import com.doris.nflow.engine.flow.definition.service.FlowDefinitionService;
import com.doris.nflow.engine.flow.deployment.enumerate.FlowDeploymentStatus;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.service.FlowDeploymentService;
import com.doris.nflow.engine.util.IdGenerator;
import com.doris.nflow.engine.util.StrongUuidGenerator;
import com.doris.nflow.engine.validator.BaseNodeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author: origindoris
 * @Title: DefinitionProcessor
 * @Description:
 * @date: 2022/10/8 09:57
 */
@Component
@Validated
@Slf4j
public class DefinitionProcessor {

    private static final IdGenerator idGenerator = new StrongUuidGenerator();

    private final FlowDefinitionService flowDefinitionService;

    private final FlowDeploymentService flowDeploymentService;

    private final BaseNodeValidator baseNodeValidator;

    public DefinitionProcessor(FlowDefinitionService flowDefinitionService, FlowDeploymentService flowDeploymentService, BaseNodeValidator baseNodeValidator) {
        this.flowDefinitionService = flowDefinitionService;
        this.flowDeploymentService = flowDeploymentService;
        this.baseNodeValidator = baseNodeValidator;
    }


    /**
     * 创建流程
     * @param createFlowParam 创建流程参数
     * @return
     */
    public CreateFlowResult create(@Valid @NotNull(message = "创建流程参数不能为空！") CreateFlowParam createFlowParam){
        CreateFlowResult createFlowResult = new CreateFlowResult();
        String flowModuleCode = null;
        boolean result = false;
        try {
            FlowDefinition flowDefinition = new FlowDefinition();
            BeanUtils.copyProperties(createFlowParam, flowDefinition);
            flowDefinition.setStatus(FlowDefinitionStatus.INIT.getCode());
            flowModuleCode = idGenerator.getNextId();
            flowDefinition.setFlowModuleCode(flowModuleCode);
            baseNodeValidator.validate(flowDefinition.getFlowModule());

            result = flowDefinitionService.save(flowDefinition);
        } catch (DefinitionException e) {
            log.error("创建流程出现异常：", e);
            createFlowResult.setErrorCode(e.getErrorCode());
            createFlowResult.setErrorMsg(e.getErrorMsg());
        }
        createFlowResult.setResult(result);
        createFlowResult.setFlowModuleCode(flowModuleCode);
        return createFlowResult;
    }


    /**
     * 开启编辑流程状态
     * @param editFlowParam
     * @return
     */
    public EditFlowResult editFlow(@Valid @NotNull(message = "编辑流程状态参数不能为空！") EditFlowParam editFlowParam) {
        EditFlowResult editFlowResult = new EditFlowResult();
        boolean result = false;
        try {
            Optional<FlowDefinition> detail = flowDefinitionService.detail(editFlowParam.getFlowModuleCode());
            if (detail.isEmpty()) {
                throw new ParamException(ErrorCode.FLOW_INVALID, "未查询到流程信息请确认！");
            }
            result = flowDefinitionService.modifyStatus(FlowDefinitionStatus.EDIT, editFlowParam.getFlowModuleCode());
        } catch (ParamException e) {
            log.error("编辑流程状态出现异常：", e);
            editFlowResult.setErrorCode(e.getErrorCode());
            editFlowResult.setErrorMsg(e.getErrorMsg());
        }
        editFlowResult.setResult(result);
        return editFlowResult;
    }

    /**
     * 更新流程信息
     *
     * @param modifyFlowParam
     * @return
     */
    public ModifyFlowResult modify(@Valid @NotNull(message = "修改流程参数不能为空！") ModifyFlowParam modifyFlowParam) {
        ModifyFlowResult modifyFlowResult = new ModifyFlowResult();
        boolean result = false;
        try {
            FlowDefinition flowDefinition = new FlowDefinition();
            BeanUtils.copyProperties(modifyFlowParam, flowDefinition);
            Optional<FlowDefinition> detail = flowDefinitionService.detail(flowDefinition.getFlowModuleCode());
            if (detail.isEmpty()) {
                throw new ParamException(ErrorCode.FLOW_INVALID, "未查询到流程信息请确认！");
            }
            FlowDefinition definition = detail.get();
            if (!FlowDefinitionStatus.EDIT.getCode().equals(definition.getStatus())) {
                throw new ParamException(ErrorCode.MODIFY_FLOW_STATUS_IS_NOT_EDIT, "该流程不是开启编辑状态不能修改！");
            }

            flowDefinition.setStatus(FlowDefinitionStatus.INIT.getCode());
            baseNodeValidator.validate(flowDefinition.getFlowModule());

            result = flowDefinitionService.modify(flowDefinition);
        } catch (DefinitionException | ParamException e) {
            log.error("更新流程出现异常：", e);
            modifyFlowResult.setErrorCode(e.getErrorCode());
            modifyFlowResult.setErrorMsg(e.getErrorMsg());
        }
        modifyFlowResult.setResult(result);
        return modifyFlowResult;
    }


    /**
     * 发布流程信息
     *
     * @param deployFlowParam
     * @return
     */
    public DeployFlowResult deploy(@Valid @NotNull(message = "发布流程参数不能为空！") DeployFlowParam deployFlowParam) {
        DeployFlowResult deployFlowResult = new DeployFlowResult();
        boolean result = false;
        try {
            String flowModuleCode = deployFlowParam.getFlowModuleCode();
            Optional<FlowDefinition> detail = flowDefinitionService.detail(flowModuleCode);
            if (detail.isEmpty()) {
                throw new ParamException(ErrorCode.FLOW_INVALID, "未查询到流程信息请确认！");
            }
            FlowDefinition definition = detail.get();
            if (!FlowDefinitionStatus.INIT.getCode().equals(definition.getStatus())) {
                throw new ParamException(ErrorCode.MODIFY_FLOW_STATUS_IS_NOT_INIT, "该流程不是初始状态不能发布！");
            }

            FlowDeployment flowDeployment = new FlowDeployment();
            BeanUtils.copyProperties(definition, flowDeployment);
            String flowDeployCode = idGenerator.getNextId();
            flowDeployment.setFlowDeployCode(flowDeployCode);
            flowDeployment.setStatus(FlowDeploymentStatus.DEPLOYED.getCode());
            result = flowDeploymentService.save(flowDeployment);
        } catch (ParamException e) {
            log.error("发布流程出现异常：", e);
            deployFlowResult.setErrorCode(e.getErrorCode());
            deployFlowResult.setErrorMsg(e.getErrorMsg());
        }
        deployFlowResult.setResult(result);
        return deployFlowResult;
    }




}
