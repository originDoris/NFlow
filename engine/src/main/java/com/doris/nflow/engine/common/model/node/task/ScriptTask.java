package com.doris.nflow.engine.common.model.node.task;

import com.doris.nflow.engine.common.constant.ExpressionTypeConstant;
import lombok.Data;

/**
 * @author: xhz
 * @Title: ScriptTask
 * @Description: 脚本任务
 * @date: 2022/9/29 15:43
 */
@Data
public class ScriptTask extends TaskNode{

    /**
     * 脚本
     */
    private String script;

    /**
     * 脚本类型 暂时只支持 groovy
     * 参考{@link  ExpressionTypeConstant}
     */
    private String scriptType;


    /**
     * 插件ID
     */
    private Integer pluginId;



    public String getScript() {
        return getProperties().get("script") == null ? null : (String) getProperties().get("script");
    }

    public void setScript(String script) {
        this.script = script;
        getProperties().put("script", script);
    }

    /**
     * 为空时使用 groovy
     * @return
     */
    public String getScriptType() {
        return getProperties().get("scriptType") == null ? ExpressionTypeConstant.GROOVY : (String) getProperties().get("scriptType");
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
        getProperties().put("scriptType", scriptType);
    }

    public Integer getPluginId() {
        return getProperties().get("pluginId") == null ? null : (Integer) getProperties().get("pluginId");
    }

    public void setPluginId(Integer pluginId) {
        this.pluginId = pluginId;
        getProperties().put("pluginId", pluginId);
    }
}
