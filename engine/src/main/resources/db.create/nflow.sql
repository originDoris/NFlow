CREATE TABLE `flow_definition`
(
    `id`               bigint unsigned                                              NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `flow_module_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci         DEFAULT '' COMMENT '流程模型id',
    `flow_name`        varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '流程名称',
    `tenant_code`      varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '租户代码',
    `flow_module`      json                                                                  DEFAULT NULL COMMENT '表单定义',
    `status`           varchar(100)                                                 NOT NULL DEFAULT '0' COMMENT '状态(init.初始态 edit.编辑中 offline.已下线)',
    `create_time`      datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '流程创建时间',
    `modify_time`      datetime                                                     NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '流程修改时间',
    `operator`         varchar(32)                                                  NOT NULL DEFAULT '' COMMENT '操作人',
    `remark`           varchar(512)                                                 NOT NULL DEFAULT '' COMMENT '备注',
    `archive`          tinyint                                                      NOT NULL DEFAULT '0' COMMENT '归档状态(0未删除，1删除)',
    `tenant`           varchar(100)                                                 NOT NULL DEFAULT '' COMMENT '租户',
    `caller`           varchar(100)                                                 NOT NULL DEFAULT '' COMMENT '调用方',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_flow_module_id` (`flow_module_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = COMPACT COMMENT ='流程定义表';



CREATE TABLE `flow_deployment`
(
    `id`               bigint unsigned                                               NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `flow_deploy_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程模型部署代码',
    `flow_module_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程模型代码',
    `flow_name`        varchar(64)                                                   NOT NULL DEFAULT '' COMMENT '流程名称',
    `tenant_code`      varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '业务方标识',
    `flow_module`      json                                                                   DEFAULT NULL COMMENT '表单定义',
    `status`           varchar(100)                                                  NOT NULL DEFAULT '0' COMMENT '状态(deployed.已部署 offline.已下线)',
    `create_time`      datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '流程创建时间',
    `modify_time`      datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '流程修改时间',
    `operator`         varchar(32)                                                   NOT NULL DEFAULT '' COMMENT '操作人',
    `remark`           varchar(512)                                                  NOT NULL DEFAULT '' COMMENT '备注',
    `archive`          tinyint                                                       NOT NULL DEFAULT '0' COMMENT '归档状态(0未删除，1删除)',
    `tenant`           varchar(100)                                                  NOT NULL DEFAULT '' COMMENT '租户',
    `caller`           varchar(100)                                                  NOT NULL DEFAULT '' COMMENT '调用方',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_flow_deploy_id` (`flow_deploy_code`),
    KEY `idx_flow_module_id` (`flow_module_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = COMPACT COMMENT ='流程部署表';

CREATE TABLE `flow_instance`
(
    `id`                 bigint unsigned                                               NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `flow_instance_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程执行实例id',
    `flow_deploy_code`   varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程模型部署id',
    `flow_module_code`   varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程模型id',
    `tenant_code`        varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci           DEFAULT '' COMMENT '业务方标识',
    `status`             varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '0' COMMENT '状态(complete.执行完成 processing.执行中 termination.执行终止(强制终止))',
    `create_time`        datetime                                                               DEFAULT '1970-01-01 00:00:00' COMMENT '流程创建时间',
    `modify_time`        datetime                                                               DEFAULT '1970-01-01 00:00:00' COMMENT '流程修改时间',
    `archive`            tinyint                                                                DEFAULT '0' COMMENT '归档状态(0未删除，1删除)',
    `tenant`             varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '租户',
    `caller`             varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '调用方',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_flow_instance_id` (`flow_instance_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 27
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = COMPACT COMMENT ='流程执行实例表';

CREATE TABLE `instance_data`
(
    `id`                 bigint unsigned                                               NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `node_instance_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点执行实例id',
    `flow_instance_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程执行实例id',
    `instance_data_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '实例数据id',
    `flow_deploy_code`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程模型部署id',
    `flow_module_code`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程模型id',
    `node_code`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '节点唯一标识',
    `tenant_code`        varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '业务方标识',
    `instance_data`      json                                                                   DEFAULT NULL COMMENT '数据列表json',
    `type`               varchar(100) COLLATE utf8mb4_unicode_ci                       NOT NULL DEFAULT '0' COMMENT '操作类型(init.实例初始化 system.系统执行 system_pull.系统主动获取 source_update.上游更新 commit.任务提交 revoke.任务撤回)',
    `create_time`        datetime                                                      NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '流程创建时间',
    `archive`            tinyint                                                       NOT NULL DEFAULT '0' COMMENT '归档状态(0未删除，1删除)',
    `tenant`             varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '租户',
    `caller`             varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '调用方',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_instance_data_id` (`instance_data_code`),
    KEY `idx_flow_instance_id` (`flow_instance_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 29
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = COMPACT COMMENT ='实例数据表';



CREATE TABLE `node_instance`
(
    `id`                        bigint unsigned                                               NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `node_instance_code`        varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '节点执行实例code',
    `flow_instance_code`        varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程执行实例code',
    `source_node_instance_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '上一个节点执行实例code',
    `instance_data_code`        varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '实例数据code',
    `flow_deploy_code`          varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程模型部署code',
    `node_code`                 varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  NOT NULL DEFAULT '' COMMENT '节点唯一标识',
    `source_node_code`          varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci           DEFAULT '' COMMENT '上一个流程节点唯一标识',
    `tenant_code`               varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci           DEFAULT '' COMMENT '业务方标识',
    `status`                    varchar(100)                                                  NOT NULL DEFAULT '0' COMMENT '状态(success.处理成功 processing.处理中 fail.处理失败 revoke.处理已撤销)',
    `create_time`               datetime                                                               DEFAULT '1970-01-01 00:00:00' COMMENT '流程创建时间',
    `modify_time`               datetime                                                               DEFAULT '1970-01-01 00:00:00' COMMENT '流程修改时间',
    `archive`                   tinyint                                                                DEFAULT '0' COMMENT '归档状态(0未删除，1删除)',
    `tenant`                    varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '租户',
    `caller`                    varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '调用方',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_node_instance_id` (`node_instance_code`),
    KEY `idx_fiid_sniid_nk` (`flow_instance_code`, `source_node_instance_code`, `node_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 42
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = COMPACT COMMENT ='节点执行实例表';


CREATE TABLE `node_instance_log`
(
    `id`                 bigint unsigned                                               NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `node_instance_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '节点执行实例code',
    `flow_instance_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '流程执行实例code',
    `instance_data_code` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '实例数据code',
    `node_code`          varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  NOT NULL DEFAULT '' COMMENT '节点唯一标识',
    `tenant_code`        varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci           DEFAULT '' COMMENT '业务方标识',
    `type`               varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '0' COMMENT '操作类型(system.系统执行 submit.任务提交 revoke.任务撤销)',
    `status`             varchar(100)                                                  NOT NULL DEFAULT '0' COMMENT '状态(success.处理成功 processing.处理中 fail.处理失败 revoke.处理已撤销)',
    `create_time`        datetime                                                               DEFAULT '1970-01-01 00:00:00' COMMENT '流程创建时间',
    `archive`            tinyint                                                       NOT NULL DEFAULT '0' COMMENT '归档状态(0未删除，1删除)',
    `tenant`             varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '租户',
    `caller`             varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT '' COMMENT '调用方',
    `modify_time`        datetime                                                               DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 42
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = COMPACT COMMENT ='节点执行记录表';