/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : task

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-08-06 16:28:19
*/

SET FOREIGN_KEY_CHECKS=0;

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
  `task_id` int(11) DEFAULT NULL COMMENT '指定任务id',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_black_mobile
-- ----------------------------

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imsi` varchar(32) DEFAULT NULL COMMENT 'imsi',
  `imei` varchar(32) DEFAULT NULL COMMENT 'imei',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_device
-- ----------------------------

-- ----------------------------
-- Table structure for t_dynamic_file
-- ----------------------------
DROP TABLE IF EXISTS `t_dynamic_file`;
CREATE TABLE `t_dynamic_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(64) DEFAULT NULL COMMENT '动态文件名称',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_dynamic_file
-- ----------------------------

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
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_file_version
-- ----------------------------

-- ----------------------------
-- Table structure for t_module
-- ----------------------------
DROP TABLE IF EXISTS `t_module`;
CREATE TABLE `t_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module_name` varchar(32) DEFAULT NULL COMMENT '模块名称',
  `status` int(1) DEFAULT NULL COMMENT '是否开启 1 开启  0关闭',
  `file_id` int(11) DEFAULT NULL COMMENT '动态文件id',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_module
-- ----------------------------

-- ----------------------------
-- Table structure for t_module_bind
-- ----------------------------
DROP TABLE IF EXISTS `t_module_bind`;
CREATE TABLE `t_module_bind` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `module_id` int(11) DEFAULT NULL COMMENT '模块id',
  `bind` int(1) DEFAULT NULL COMMENT '是否绑定 1 是  0  否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_module_bind
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `status` int(1) DEFAULT NULL COMMENT '是否可用 1 可用 0 不可用',
  `desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `role_level` int(1) DEFAULT NULL COMMENT '角色级别 1 超管 2 模块管理员',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '系统管理员', '1', '超管', '1', null, null);
INSERT INTO `t_role` VALUES ('2', '模块管理员', '1', '模块管理员', '2', null, null);

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(64) DEFAULT NULL COMMENT '任务名称',
  `module_id` int(11) DEFAULT NULL COMMENT '所属模块ID',
  `status` int(1) DEFAULT NULL COMMENT '开启状态 1 开启 0 关闭',
  `startDate` date DEFAULT NULL COMMENT '开始日期',
  `endDate` date DEFAULT NULL COMMENT '结束日期',
  `startTime` time DEFAULT NULL COMMENT '开始时间',
  `endTime` time DEFAULT NULL COMMENT '结束时间',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `period` int(1) DEFAULT NULL COMMENT '周期任务时间 0 无限制  15表示15小时一个周期 ',
  `period_unit` int(1) DEFAULT NULL COMMENT '周期单位 1 天  2  小时',
  `total` int(11) DEFAULT NULL COMMENT '任务总数限制',
  `detail` varchar(10240) DEFAULT NULL COMMENT '任务详情，json格式',
  `random` int(4) DEFAULT NULL COMMENT '随机概率0-100之间',
  `allow_area` varchar(1024) DEFAULT NULL COMMENT '允许地区',
  `limit_area` varchar(1024) DEFAULT NULL COMMENT '禁止地区',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `update_user` int(11) DEFAULT NULL COMMENT '更新用户',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_task
-- ----------------------------

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
  `module_name` varchar(64) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL COMMENT '请求任务id',
  `task_name` varchar(64) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_task_complete
-- ----------------------------

-- ----------------------------
-- Table structure for t_task_limit
-- ----------------------------
DROP TABLE IF EXISTS `t_task_limit`;
CREATE TABLE `t_task_limit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL COMMENT '任务ID',
  `period` int(1) DEFAULT NULL COMMENT '周期任务时间 0 无限制  15表示15小时一个周期 ',
  `period_unit` int(1) DEFAULT NULL COMMENT '周期单位 1 小时  2天',
  `start_time` datetime DEFAULT NULL COMMENT '周期开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '周期结束时间',
  `total` int(11) DEFAULT NULL COMMENT '任务阈值',
  `cur_count` int(11) DEFAULT NULL COMMENT '当前执行次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_task_limit
-- ----------------------------

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
  `module_name` varchar(64) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL COMMENT '请求任务id',
  `task_name` varchar(64) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_task_request
-- ----------------------------

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
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'admin', '1', '1', '2019-08-05 23:27:30', '2019-08-05 23:27:36');
