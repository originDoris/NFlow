/*
 Navicat Premium Data Transfer

 Source Server         : 204 pg
 Source Server Type    : PostgreSQL
 Source Server Version : 150004 (150004)
 Source Host           : 192.168.1.204:5432
 Source Catalog        : flow
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 150004 (150004)
 File Encoding         : 65001

 Date: 09/10/2023 14:02:29
*/


-- ----------------------------
-- Sequence structure for flow_definition_id
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."flow_definition_id";
CREATE SEQUENCE "public"."flow_definition_id"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."flow_definition_id" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for flow_deployment_id
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."flow_deployment_id";
CREATE SEQUENCE "public"."flow_deployment_id"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."flow_deployment_id" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for flow_instance_id
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."flow_instance_id";
CREATE SEQUENCE "public"."flow_instance_id"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."flow_instance_id" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for instance_data_id
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."instance_data_id";
CREATE SEQUENCE "public"."instance_data_id"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."instance_data_id" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for node_instance_id
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."node_instance_id";
CREATE SEQUENCE "public"."node_instance_id"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."node_instance_id" OWNER TO "postgres";

-- ----------------------------
-- Sequence structure for node_instance_log_id
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."node_instance_log_id";
CREATE SEQUENCE "public"."node_instance_log_id"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;
ALTER SEQUENCE "public"."node_instance_log_id" OWNER TO "postgres";

-- ----------------------------
-- Table structure for flow_definition
-- ----------------------------
DROP TABLE IF EXISTS "public"."flow_definition";
CREATE TABLE "public"."flow_definition" (
                                            "id" numeric(20,0) NOT NULL DEFAULT nextval('flow_definition_id'::regclass),
                                            "flow_module_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                            "flow_name" varchar(64) COLLATE "pg_catalog"."default",
                                            "flow_module" text COLLATE "pg_catalog"."default",
                                            "status" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                            "create_time" timestamp(6) NOT NULL,
                                            "modify_time" timestamp(6),
                                            "operator" varchar(32) COLLATE "pg_catalog"."default",
                                            "remark" varchar(512) COLLATE "pg_catalog"."default",
                                            "archive" int2 NOT NULL DEFAULT 0,
                                            "caller" varchar(100) COLLATE "pg_catalog"."default",
                                            "content" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."flow_definition" OWNER TO "postgres";
COMMENT ON COLUMN "public"."flow_definition"."id" IS '自增主键';
COMMENT ON COLUMN "public"."flow_definition"."flow_module_code" IS '流程模型id';
COMMENT ON COLUMN "public"."flow_definition"."flow_name" IS '流程名称';
COMMENT ON COLUMN "public"."flow_definition"."flow_module" IS '表单定义';
COMMENT ON COLUMN "public"."flow_definition"."status" IS '状态(init.初始态 edit.编辑中 offline.已下线)';
COMMENT ON COLUMN "public"."flow_definition"."create_time" IS '流程创建时间';
COMMENT ON COLUMN "public"."flow_definition"."modify_time" IS '流程修改时间';
COMMENT ON COLUMN "public"."flow_definition"."operator" IS '操作人';
COMMENT ON COLUMN "public"."flow_definition"."remark" IS '备注';
COMMENT ON COLUMN "public"."flow_definition"."archive" IS '归档状态(0未删除，1删除)';
COMMENT ON COLUMN "public"."flow_definition"."caller" IS '调用方';
COMMENT ON COLUMN "public"."flow_definition"."content" IS '保存前端结构';
COMMENT ON TABLE "public"."flow_definition" IS '流程定义表';

-- ----------------------------
-- Table structure for flow_deployment
-- ----------------------------
DROP TABLE IF EXISTS "public"."flow_deployment";
CREATE TABLE "public"."flow_deployment" (
                                            "id" numeric(20,0) NOT NULL DEFAULT nextval('flow_deployment_id'::regclass),
                                            "flow_deploy_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                            "flow_module_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                            "flow_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                            "flow_module" text COLLATE "pg_catalog"."default",
                                            "status" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                            "create_time" timestamp(6) NOT NULL,
                                            "modify_time" timestamp(6),
                                            "operator" varchar(32) COLLATE "pg_catalog"."default",
                                            "remark" varchar(512) COLLATE "pg_catalog"."default",
                                            "archive" int2 NOT NULL DEFAULT 0,
                                            "caller" varchar(100) COLLATE "pg_catalog"."default",
                                            "content" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."flow_deployment" OWNER TO "postgres";
COMMENT ON COLUMN "public"."flow_deployment"."id" IS '自增主键';
COMMENT ON COLUMN "public"."flow_deployment"."flow_deploy_code" IS '流程模型部署代码';
COMMENT ON COLUMN "public"."flow_deployment"."flow_module_code" IS '流程模型代码';
COMMENT ON COLUMN "public"."flow_deployment"."flow_name" IS '流程名称';
COMMENT ON COLUMN "public"."flow_deployment"."flow_module" IS '表单定义';
COMMENT ON COLUMN "public"."flow_deployment"."status" IS '状态(deployed.已部署 offline.已下线)';
COMMENT ON COLUMN "public"."flow_deployment"."create_time" IS '流程创建时间';
COMMENT ON COLUMN "public"."flow_deployment"."modify_time" IS '流程修改时间';
COMMENT ON COLUMN "public"."flow_deployment"."operator" IS '操作人';
COMMENT ON COLUMN "public"."flow_deployment"."remark" IS '备注';
COMMENT ON COLUMN "public"."flow_deployment"."archive" IS '归档状态(0未删除，1删除)';
COMMENT ON COLUMN "public"."flow_deployment"."caller" IS '调用方';
COMMENT ON COLUMN "public"."flow_deployment"."content" IS '保存前端结构';
COMMENT ON TABLE "public"."flow_deployment" IS '流程部署表';

-- ----------------------------
-- Table structure for flow_instance
-- ----------------------------
DROP TABLE IF EXISTS "public"."flow_instance";
CREATE TABLE "public"."flow_instance" (
                                          "id" numeric(20,0) NOT NULL DEFAULT nextval('flow_instance_id'::regclass),
                                          "flow_instance_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "flow_deploy_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "flow_module_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "status" varchar(100) COLLATE "pg_catalog"."default",
                                          "create_time" timestamp(6),
                                          "modify_time" timestamp(6),
                                          "archive" int2 DEFAULT 0,
                                          "caller" varchar(100) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."flow_instance" OWNER TO "postgres";
COMMENT ON COLUMN "public"."flow_instance"."id" IS '自增主键';
COMMENT ON COLUMN "public"."flow_instance"."flow_instance_code" IS '流程执行实例id';
COMMENT ON COLUMN "public"."flow_instance"."flow_deploy_code" IS '流程模型部署id';
COMMENT ON COLUMN "public"."flow_instance"."flow_module_code" IS '流程模型id';
COMMENT ON COLUMN "public"."flow_instance"."status" IS '状态(complete.执行完成 processing.执行中 termination.执行终止(强制终止))';
COMMENT ON COLUMN "public"."flow_instance"."create_time" IS '流程创建时间';
COMMENT ON COLUMN "public"."flow_instance"."modify_time" IS '流程修改时间';
COMMENT ON COLUMN "public"."flow_instance"."archive" IS '归档状态(0未删除，1删除)';
COMMENT ON COLUMN "public"."flow_instance"."caller" IS '调用方';
COMMENT ON TABLE "public"."flow_instance" IS '流程执行实例表';

-- ----------------------------
-- Table structure for instance_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."instance_data";
CREATE TABLE "public"."instance_data" (
                                          "id" numeric(20,0) NOT NULL DEFAULT nextval('instance_data_id'::regclass),
                                          "node_instance_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
                                          "flow_instance_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "instance_data_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "flow_deploy_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "flow_module_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "node_code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
                                          "instance_data" text COLLATE "pg_catalog"."default",
                                          "type" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                          "create_time" timestamp(6) NOT NULL,
                                          "archive" int2 NOT NULL DEFAULT 0,
                                          "caller" varchar(100) COLLATE "pg_catalog"."default",
                                          "modify_time" timestamp(6)
)
;
ALTER TABLE "public"."instance_data" OWNER TO "postgres";
COMMENT ON COLUMN "public"."instance_data"."id" IS '自增主键';
COMMENT ON COLUMN "public"."instance_data"."node_instance_code" IS '节点执行实例id';
COMMENT ON COLUMN "public"."instance_data"."flow_instance_code" IS '流程执行实例id';
COMMENT ON COLUMN "public"."instance_data"."instance_data_code" IS '实例数据id';
COMMENT ON COLUMN "public"."instance_data"."flow_deploy_code" IS '流程模型部署id';
COMMENT ON COLUMN "public"."instance_data"."flow_module_code" IS '流程模型id';
COMMENT ON COLUMN "public"."instance_data"."node_code" IS '节点唯一标识';
COMMENT ON COLUMN "public"."instance_data"."instance_data" IS '数据列表json';
COMMENT ON COLUMN "public"."instance_data"."type" IS '操作类型(init.实例初始化 system.系统执行 system_pull.系统主动获取 source_update.上游更新 commit.任务提交 revoke.任务撤回)';
COMMENT ON COLUMN "public"."instance_data"."create_time" IS '流程创建时间';
COMMENT ON COLUMN "public"."instance_data"."archive" IS '归档状态(0未删除，1删除)';
COMMENT ON COLUMN "public"."instance_data"."caller" IS '调用方';
COMMENT ON TABLE "public"."instance_data" IS '实例数据表';

-- ----------------------------
-- Table structure for node_instance
-- ----------------------------
DROP TABLE IF EXISTS "public"."node_instance";
CREATE TABLE "public"."node_instance" (
                                          "id" numeric(20,0) NOT NULL DEFAULT nextval('node_instance_id'::regclass),
                                          "node_instance_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "flow_instance_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "source_node_instance_code" varchar(128) COLLATE "pg_catalog"."default",
                                          "instance_data_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "flow_deploy_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                          "node_code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                          "source_node_code" varchar(64) COLLATE "pg_catalog"."default",
                                          "status" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                          "create_time" timestamp(6),
                                          "modify_time" timestamp(6),
                                          "archive" int2 DEFAULT 0,
                                          "caller" varchar(100) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."node_instance" OWNER TO "postgres";
COMMENT ON COLUMN "public"."node_instance"."id" IS '自增主键';
COMMENT ON COLUMN "public"."node_instance"."node_instance_code" IS '节点执行实例code';
COMMENT ON COLUMN "public"."node_instance"."flow_instance_code" IS '流程执行实例code';
COMMENT ON COLUMN "public"."node_instance"."source_node_instance_code" IS '上一个节点执行实例code';
COMMENT ON COLUMN "public"."node_instance"."instance_data_code" IS '实例数据code';
COMMENT ON COLUMN "public"."node_instance"."flow_deploy_code" IS '流程模型部署code';
COMMENT ON COLUMN "public"."node_instance"."node_code" IS '节点唯一标识';
COMMENT ON COLUMN "public"."node_instance"."source_node_code" IS '上一个流程节点唯一标识';
COMMENT ON COLUMN "public"."node_instance"."status" IS '状态(success.处理成功 processing.处理中 fail.处理失败 revoke.处理已撤销)';
COMMENT ON COLUMN "public"."node_instance"."create_time" IS '流程创建时间';
COMMENT ON COLUMN "public"."node_instance"."modify_time" IS '流程修改时间';
COMMENT ON COLUMN "public"."node_instance"."archive" IS '归档状态(0未删除，1删除)';
COMMENT ON COLUMN "public"."node_instance"."caller" IS '调用方';
COMMENT ON TABLE "public"."node_instance" IS '节点执行实例表';

-- ----------------------------
-- Table structure for node_instance_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."node_instance_log";
CREATE TABLE "public"."node_instance_log" (
                                              "id" numeric(20,0) NOT NULL DEFAULT nextval('node_instance_log_id'::regclass),
                                              "node_instance_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                              "flow_instance_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                              "instance_data_code" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                              "node_code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                              "type" varchar(100) COLLATE "pg_catalog"."default",
                                              "status" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                              "create_time" timestamp(6),
                                              "archive" int2 NOT NULL DEFAULT 0,
                                              "caller" varchar(100) COLLATE "pg_catalog"."default",
                                              "modify_time" timestamp(6)
)
;
ALTER TABLE "public"."node_instance_log" OWNER TO "postgres";
COMMENT ON COLUMN "public"."node_instance_log"."id" IS '自增主键';
COMMENT ON COLUMN "public"."node_instance_log"."node_instance_code" IS '节点执行实例code';
COMMENT ON COLUMN "public"."node_instance_log"."flow_instance_code" IS '流程执行实例code';
COMMENT ON COLUMN "public"."node_instance_log"."instance_data_code" IS '实例数据code';
COMMENT ON COLUMN "public"."node_instance_log"."node_code" IS '节点唯一标识';
COMMENT ON COLUMN "public"."node_instance_log"."type" IS '操作类型(system.系统执行 submit.任务提交 revoke.任务撤销)';
COMMENT ON COLUMN "public"."node_instance_log"."status" IS '状态(success.处理成功 processing.处理中 fail.处理失败 revoke.处理已撤销)';
COMMENT ON COLUMN "public"."node_instance_log"."create_time" IS '流程创建时间';
COMMENT ON COLUMN "public"."node_instance_log"."archive" IS '归档状态(0未删除，1删除)';
COMMENT ON COLUMN "public"."node_instance_log"."caller" IS '调用方';
COMMENT ON COLUMN "public"."node_instance_log"."modify_time" IS '修改时间';
COMMENT ON TABLE "public"."node_instance_log" IS '节点执行记录表';

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."flow_definition_id"', 14, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."flow_deployment_id"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."flow_instance_id"', 15, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."instance_data_id"', 15, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."node_instance_id"', 20, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"public"."node_instance_log_id"', 20, true);

-- ----------------------------
-- Indexes structure for table flow_definition
-- ----------------------------
CREATE UNIQUE INDEX "uniq_flow_module_id" ON "public"."flow_definition" USING btree (
    "flow_module_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table flow_definition
-- ----------------------------
ALTER TABLE "public"."flow_definition" ADD CONSTRAINT "flow_definition_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table flow_deployment
-- ----------------------------
CREATE INDEX "idx_flow_module_id" ON "public"."flow_deployment" USING btree (
    "flow_module_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );
CREATE UNIQUE INDEX "uniq_flow_deploy_id" ON "public"."flow_deployment" USING btree (
    "flow_deploy_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table flow_deployment
-- ----------------------------
ALTER TABLE "public"."flow_deployment" ADD CONSTRAINT "_copy_5" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table flow_instance
-- ----------------------------
CREATE UNIQUE INDEX "uniq_flow_instance_id" ON "public"."flow_instance" USING btree (
    "flow_instance_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table flow_instance
-- ----------------------------
ALTER TABLE "public"."flow_instance" ADD CONSTRAINT "_copy_4" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table instance_data
-- ----------------------------
CREATE INDEX "idx_flow_instance_id" ON "public"."instance_data" USING btree (
    "flow_instance_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );
CREATE UNIQUE INDEX "uniq_instance_data_id" ON "public"."instance_data" USING btree (
    "instance_data_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table instance_data
-- ----------------------------
ALTER TABLE "public"."instance_data" ADD CONSTRAINT "_copy_3" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table node_instance
-- ----------------------------
CREATE INDEX "idx_fiid_sniid_nk" ON "public"."node_instance" USING btree (
    "flow_instance_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
    "source_node_instance_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
    "node_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );
CREATE UNIQUE INDEX "uniq_node_instance_id" ON "public"."node_instance" USING btree (
    "node_instance_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table node_instance
-- ----------------------------
ALTER TABLE "public"."node_instance" ADD CONSTRAINT "_copy_2" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table node_instance_log
-- ----------------------------
ALTER TABLE "public"."node_instance_log" ADD CONSTRAINT "_copy_1" PRIMARY KEY ("id");
