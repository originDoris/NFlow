package com.doris.nflow.engine.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.doris.nflow.engine.common.model.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: origindoris
 * @Title: MateDataHandler
 * @Description: 补全数据
 * @date: 2022/9/30 09:56
 */
@Slf4j
@Component
public class CompletionMateDataHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, BaseModel.CREATE_TIME, LocalDateTime.class, LocalDateTime.now());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start modify fill ....");
        this.strictUpdateFill(metaObject, BaseModel.MODIFY_TIME, LocalDateTime.class, LocalDateTime.now());
    }
}
