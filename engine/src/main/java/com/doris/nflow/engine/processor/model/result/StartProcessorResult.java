package com.doris.nflow.engine.processor.model.result;

import lombok.Data;

/**
 * @author: origindoris
 * @Title: StartProcessorResult
 * @Description:
 * @date: 2022/10/9 10:03
 */
@Data
public class StartProcessorResult extends RuntimeResult {
   private String flowDeployCode;

   private String flowModuleCode;
}
