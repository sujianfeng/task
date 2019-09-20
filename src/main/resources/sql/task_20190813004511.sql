/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : task

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 13/08/2019 00:45:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_black_mobile
-- ----------------------------
DROP TABLE IF EXISTS `t_black_mobile`;
CREATE TABLE `t_black_mobile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imsi` varchar(64) DEFAULT NULL COMMENT 'imsi',
  `imei` varchar(64) DEFAULT NULL COMMENT 'imei',
  `mobile` varchar(32) DEFAULT NULL COMMENT 'mobile',
  `status` int(1) DEFAULT NULL COMMENT '是否为黑名单 1 是  0 否',
  `module_id` int(11) DEFAULT NULL COMMENT '指定模块id',
  `moduleName` varchar(32) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL COMMENT '指定任务id',
  `taskName` varchar(32) DEFAULT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imsi` varchar(32) DEFAULT NULL COMMENT 'imsi',
  `imei` varchar(32) DEFAULT NULL COMMENT 'imei',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_dynamic_file
-- ----------------------------
DROP TABLE IF EXISTS `t_dynamic_file`;
CREATE TABLE `t_dynamic_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fileName` varchar(64) DEFAULT NULL COMMENT '动态文件名称',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_dynamic_file
-- ----------------------------
BEGIN;
INSERT INTO `t_dynamic_file` VALUES (1, '通用动态文件1', '2019-08-07 17:02:19', '2019-08-11 17:46:35');
COMMIT;

-- ----------------------------
-- Table structure for t_file_version
-- ----------------------------
DROP TABLE IF EXISTS `t_file_version`;
CREATE TABLE `t_file_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_id` int(11) DEFAULT NULL COMMENT '动态文件ID',
  `version` varchar(32) DEFAULT NULL COMMENT '文件版本',
  `desc` varchar(255) DEFAULT NULL COMMENT '版本描述',
  `url` varchar(255) DEFAULT NULL COMMENT '下载链接',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_file_version
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_module
-- ----------------------------
DROP TABLE IF EXISTS `t_module`;
CREATE TABLE `t_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `moduleName` varchar(32) DEFAULT NULL COMMENT '模块名称',
  `status` int(1) DEFAULT NULL COMMENT '是否开启 1 开启  0关闭',
  `file_id` int(11) DEFAULT NULL COMMENT '动态文件id',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_module
-- ----------------------------
BEGIN;
INSERT INTO `t_module` VALUES (1, 'sms', 1, 1, '2019-08-06 23:43:39', '2019-08-10 23:18:46');
INSERT INTO `t_module` VALUES (2, 'ivr', 1, 1, '2019-08-06 23:43:39', '2019-08-10 00:29:58');
COMMIT;

-- ----------------------------
-- Table structure for t_module_bind
-- ----------------------------
DROP TABLE IF EXISTS `t_module_bind`;
CREATE TABLE `t_module_bind` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `module_id` int(11) DEFAULT NULL COMMENT '模块id',
  `bind` int(1) DEFAULT NULL COMMENT '是否绑定 1 是  0  否',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_module_bind
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `status` int(1) DEFAULT NULL COMMENT '是否可用 1 可用 0 不可用',
  `desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `roleLevel` int(1) DEFAULT NULL COMMENT '角色级别 1 超管 2 模块管理员',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role` VALUES (1, '系统管理员', 1, '超管', 1, '2019-08-06 23:43:39', '2019-08-10 00:29:58');
INSERT INTO `t_role` VALUES (2, '模块管理员', 1, '模块管理员', 2, '2019-08-06 23:43:39', '2019-08-10 00:29:58');
COMMIT;

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskName` varchar(64) DEFAULT NULL COMMENT '任务名称',
  `module_id` int(11) DEFAULT NULL COMMENT '所属模块ID',
  `status` int(1) DEFAULT NULL COMMENT '开启状态 1 开启 0 关闭',
  `startDate` date DEFAULT NULL COMMENT '开始日期',
  `endDate` date DEFAULT NULL COMMENT '结束日期',
  `startTime` time DEFAULT NULL COMMENT '开始时间',
  `endTime` time DEFAULT NULL COMMENT '结束时间',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `period` int(1) DEFAULT NULL COMMENT '周期任务时间 0 无限制  15表示15小时一个周期 ',
  `periodUnit` int(1) DEFAULT NULL COMMENT '周期单位 1 天  2  小时',
  `total` int(11) DEFAULT NULL COMMENT '任务总数限制',
  `detail` varchar(10240) DEFAULT NULL COMMENT '任务详情，json格式',
  `random` int(4) DEFAULT NULL COMMENT '随机概率0-100之间',
  `allowArea` varchar(1024) DEFAULT NULL COMMENT '允许地区',
  `limitArea` varchar(1024) DEFAULT NULL COMMENT '禁止地区',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `update_user` int(11) DEFAULT NULL COMMENT '更新用户',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_task_complete
-- ----------------------------
DROP TABLE IF EXISTS `t_task_complete`;
CREATE TABLE `t_task_complete` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imsi` varchar(32) DEFAULT NULL COMMENT 'imsi',
  `imei` varchar(32) DEFAULT NULL COMMENT 'imei',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `module_id` int(11) DEFAULT NULL COMMENT '请求模块id',
  `moduleName` varchar(64) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL COMMENT '请求任务id',
  `taskName` varchar(64) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_task_complete
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_task_limit
-- ----------------------------
DROP TABLE IF EXISTS `t_task_limit`;
CREATE TABLE `t_task_limit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL COMMENT '任务ID',
  `period` int(1) DEFAULT NULL COMMENT '周期任务时间 0 无限制  15表示15小时一个周期 ',
  `periodUnit` int(1) DEFAULT NULL COMMENT '周期单位 1 小时  2天',
  `startTime` datetime DEFAULT NULL COMMENT '周期开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '周期结束时间',
  `total` int(11) DEFAULT NULL COMMENT '任务阈值',
  `curCount` int(11) DEFAULT NULL COMMENT '当前执行次数',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_task_request
-- ----------------------------
DROP TABLE IF EXISTS `t_task_request`;
CREATE TABLE `t_task_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imsi` varchar(32) DEFAULT NULL COMMENT 'imsi',
  `imei` varchar(32) DEFAULT NULL COMMENT 'imei',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `module_id` int(11) DEFAULT NULL COMMENT '请求模块id',
  `moduleName` varchar(64) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL COMMENT '请求任务id',
  `taskName` varchar(64) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_task_request
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `roleid` int(11) DEFAULT NULL COMMENT '角色id',
  `status` int(1) DEFAULT NULL COMMENT '是否可用 1 可用 0 不可用',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES (1, 'admin', 'admin123456', 1, 1, '2019-08-06 23:43:39', '2019-08-06 23:43:39');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
