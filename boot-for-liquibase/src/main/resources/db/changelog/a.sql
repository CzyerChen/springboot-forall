--liquibase formatted sql

--changeset czy:3
DROP TABLE IF EXISTS `hydp_rdbms_param`;
CREATE TABLE `hydp_rdbms_param` (
  `id` varchar(32) NOT NULL COMMENT '关系数据库数据源ID',
  `name` varchar(255) DEFAULT NULL COMMENT '系数据库数据源名称',
  `media_source_id` varchar(32) DEFAULT NULL COMMENT '数据源机器ID',
  `username` varchar(255) DEFAULT NULL COMMENT '数据源用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '数据源密码',
  `rdbms_type` smallint(6) DEFAULT NULL COMMENT '系数据库数据源类型',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NOW() COMMENT '创建时间',
  `update_time` datetime DEFAULT NOW() COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--changeset czy:4
CREATE TABLE `hydp_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `code` int(11) DEFAULT NULL COMMENT '角色代码',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

