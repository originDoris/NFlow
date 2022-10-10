package com.doris.nflow.engine.common.model.node.task;

import com.doris.nflow.engine.common.constant.ExpressionTypeConstant;
import lombok.Data;

/**
 * @author: origindoris
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
}
