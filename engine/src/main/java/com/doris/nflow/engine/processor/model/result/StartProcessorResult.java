package com.doris.nflow.engine.processor.model.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xhz
 * @Title: StartProcessorResult
 * @Description:
 * @date: 2022/10/9 10:03
 */
@Data
public class StartProcessorResult extends RuntimeResult {
   private String flowDeployCode;

   private String flowModuleCode;

   private Map<String, Object> result = new HashMap<>();
}
