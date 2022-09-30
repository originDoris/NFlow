package com.doris.nflow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: origindoris
 * @Title: ApiApplication
 * @Description:
 * @date: 2022/9/29 17:22
 */

@SpringBootApplication(scanBasePackages = {"com.doris.nflow"})
@EnableTransactionManagement
//@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
