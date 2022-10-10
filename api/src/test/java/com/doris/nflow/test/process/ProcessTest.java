package com.doris.nflow.test.process;

import com.doris.nflow.api.ApiApplication;
import com.doris.nflow.engine.node.instance.model.InstanceData;
import com.doris.nflow.engine.processor.RuntimeProcessor;
import com.doris.nflow.engine.processor.model.param.CommitTaskParam;
import com.doris.nflow.engine.processor.model.param.StartProcessorParam;
import com.doris.nflow.engine.processor.model.result.CommitTaskResult;
import com.doris.nflow.engine.processor.model.result.StartProcessorResult;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.collections.Lists;

/**
 * @author: origindoris
 * @Title: ProcessTest
 * @Description:
 * @date: 2022/10/10 10:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class ProcessTest {

    @Autowired
    private RuntimeProcessor runtimeProcessor;

    @Test
    public void startProcessTest(){
        StartProcessorParam startProcessorParam = new StartProcessorParam();
        startProcessorParam.setFlowDeployCode("34ca2e55-4842-11ed-9748-3e7b4863f9dc");
        startProcessorParam.setFlowModuleCode("b01f6e55-4840-11ed-8851-c69bf65f9b74");


        StartProcessorResult startProcessorResult = runtimeProcessor.startProcess(startProcessorParam);
        System.out.println("startProcessorResult = " + startProcessorResult);
    }


    @Test
    public void commitProcess(){
        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setFlowInstanceCode("7e2e3be0-4861-11ed-b3bc-7ec94acc885a");
        commitTaskParam.setNodeInstanceCode("20ede653-4862-11ed-b3bc-7ec94acc885a");
        InstanceData instanceData = new InstanceData();
        instanceData.setType("Integer");
        instanceData.setKey("day");
        instanceData.setValue(2);
        commitTaskParam.setParams(Lists.newArrayList(instanceData));
        CommitTaskResult commit = runtimeProcessor.commit(commitTaskParam);
        System.out.println("commit = " + commit);
    }


}
