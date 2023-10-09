package com.doris.nflow.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.api.server.ServiceFlowServer;
import com.doris.nflow.api.util.BuildModuleUtil;
import com.doris.nflow.engine.common.model.node.BaseNode;
import com.doris.nflow.engine.flow.deployment.model.FlowDeployment;
import com.doris.nflow.engine.flow.deployment.model.FlowDeploymentQuery;
import com.doris.nflow.engine.flow.deployment.service.FlowDeploymentService;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.processor.model.param.CreateFlowParam;
import com.doris.nflow.engine.processor.model.result.CommitTaskResult;
import com.doris.nflow.engine.processor.model.result.DeployFlowResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: xhz
 * @Title: FlowController
 * @Description:
 * @date: 2022/10/10 16:12
 */
@RestController
@RequestMapping("/nflow")
public class FlowController {
    @Autowired
    private ServiceFlowServer serviceFlowServer;

    @Autowired
    private FlowDeploymentService flowDeploymentService;


    @GetMapping("/queryPage")
    public IPage<FlowDeployment> queryPage(@RequestParam(value = "flowModuleCode", required = false) String flowModuleCode,
                                           @RequestParam(value = "status", required = false) String status,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                           @RequestParam(value = "search", required = false) String search) {
        FlowDeploymentQuery flowDeploymentQuery = new FlowDeploymentQuery();
        flowDeploymentQuery.setFlowModuleCode(flowModuleCode);
        flowDeploymentQuery.setStatus(status);
        flowDeploymentQuery.setPageSize(pageSize);
        flowDeploymentQuery.setPageNo(pageNo);
        flowDeploymentQuery.setSearch(search);
        return flowDeploymentService.queryPage(flowDeploymentQuery);
    }


    /**
     * 新增并部署流程
     *
     * @param flowParam
     * @return
     */
    @PostMapping("/addAndDeployFlow")
    public DeployFlowResult addAndDeployFlow(@RequestBody CreateFlowParam flowParam) {
        List<BaseNode> baseNodes = BuildModuleUtil.buildNode(flowParam.getContent());
        flowParam.setFlowModule(baseNodes);
        return serviceFlowServer.addFlow(flowParam);
    }

    @GetMapping("/runServiceFlow")
    public List<InstanceData> runServiceFlow(@RequestParam("flowDeployCode") String flowDeployCode,
                                             @RequestParam("flowModuleCode") String flowModuleCode){
        return serviceFlowServer.run(flowDeployCode, flowModuleCode);
    }
}
