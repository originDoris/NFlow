package com.doris.nflow.engine.flow.deployment.enumerate;

/**
 * @author: origindoris
 * @Title: FlowDeploymentStatus
 * @Description: 流程发布状态
 * @date: 2022/10/1 12:04
 */
public enum FlowDeploymentStatus {
    //状态(deployed.已部署 offline.已下线)
    DEPLOYED("deployed", "已部署"),
    OFFLINE("offline", "已下线"),
    ;
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    FlowDeploymentStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
