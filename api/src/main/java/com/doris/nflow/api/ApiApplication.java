package com.doris.nflow.api;

import com.doris.nflow.engine.verticle.FlowVerticle;
import com.hazelcast.config.Config;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.vertx.core.VertxOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

/**
 * @author: xhz
 * @Title: ApiApplication
 * @Description:
 * @date: 2022/9/29 17:22
 */

@SpringBootApplication(scanBasePackages = {"com.doris.nflow"})
@EnableTransactionManagement
@MapperScan({"com.doris.nflow.engine.flow.definition.mapper","com.doris.nflow.engine.flow.deployment.mapper","com.doris.nflow.engine.flow.instance.mapper"})
public class ApiApplication {


    @Autowired
    private FlowVerticle flowVerticle;
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @PostConstruct
    public void deployVerticle() {
        HazelcastClusterManager mgr = new HazelcastClusterManager();
        Config config = new Config();
        config.setClusterName("datacube");
        mgr.setConfig(config);
        // Configure the Vert.x instance to use the Hazelcast cluster manager
        Single<Vertx> vertxSingle = Vertx.clusteredVertx(new VertxOptions().setClusterManager(mgr));
        Disposable subscribe = vertxSingle.subscribe((vertx, throwable) -> {

            vertx.rxDeployVerticle(flowVerticle).subscribe();
            if (throwable != null) {
                throwable.printStackTrace();
            }
        });
    }
}
