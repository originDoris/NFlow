package com.doris.nflow.api.controller;

import com.doris.nflow.api.server.GradedApprovalServer;
import com.doris.nflow.api.server.ServiceFlowServer;
import com.doris.nflow.engine.common.model.node.BaseNode;
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
    private GradedApprovalServer gradedApprovalServer;

    @Autowired
    private ServiceFlowServer serviceFlowServer;

    @GetMapping("/runGradedApproval")
    public CommitTaskResult runGradedApproval(){
        return gradedApprovalServer.run();
    }


    /**
     * 新增并部署流程
     * @param flowParam
     * @return
     */
    @PostMapping("/addAndDeployFlow")
    public DeployFlowResult addAndDeployFlow(@RequestBody CreateFlowParam flowParam) {
        return serviceFlowServer.addFlow(flowParam);
    }

    @GetMapping("/runServiceFlow")
    public List<InstanceData> runServiceFlow(String flowDeployCode,String flowModuleCode){
        return serviceFlowServer.run(flowDeployCode, flowModuleCode);
    }
}
