package com.doris.nflow.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: xhz
 * @Title: ApiApplication
 * @Description:
 * @date: 2022/9/29 17:22
 */

@SpringBootApplication(scanBasePackages = {"com.doris.nflow"})
@EnableTransactionManagement
@MapperScan({"com.doris.nflow.engine.flow.definition.mapper","com.doris.nflow.engine.flow.deployment.mapper","com.doris.nflow.engine.flow.instance.mapper","com.doris.nflow.engine.node.instance.mapper"})
public class ApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
