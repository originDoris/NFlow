package com.doris.nflow.api.controller;

import com.doris.nflow.api.server.GradedApprovalServer;
import com.doris.nflow.engine.processor.model.result.CommitTaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: origindoris
 * @Title: FlowController
 * @Description:
 * @date: 2022/10/10 16:12
 */
@RestController
@RequestMapping("/nflow")
public class FlowController {

    @Autowired
    private GradedApprovalServer gradedApprovalServer;

    @GetMapping("/runGradedApproval")
    public CommitTaskResult runGradedApproval(){
        return gradedApprovalServer.run();
    }
}
