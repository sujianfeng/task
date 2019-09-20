/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : task

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-08-08 17:59:42
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
  `createTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
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
  `fileName` varchar(64) DEFAULT NULL COMMENT '动态文件名称',
  `createTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_dynamic_file
-- ----------------------------
INSERT INTO `t_dynamic_file` VALUES ('1', '通用动态文件', '2019-08-07 17:02:19', '2019-08-07 17:02:20');
INSERT INTO `t_dynamic_file` VALUES ('2', '测试动态文件1', '2019-08-07 17:23:12', '2019-08-07 17:23:13');

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
  `createTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_file_version
-- ----------------------------
INSERT INTO `t_file_version` VALUES ('1', '1', '1.0.0', 'test', 'http://www.baidu.com', null, null);
INSERT INTO `t_file_version` VALUES ('2', '1', '1.0.1', 'test1', 'http://www.baidu.com2', '2019-08-08 11:45:25', '2019-08-08 11:45:26');

-- ----------------------------
-- Table structure for t_module
-- ----------------------------
DROP TABLE IF EXISTS `t_module`;
CREATE TABLE `t_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `moduleName` varchar(32) DEFAULT NULL COMMENT '模块名称',
  `status` int(1) DEFAULT NULL COMMENT '是否开启 1 开启  0关闭',
  `file_id` int(11) DEFAULT NULL COMMENT '动态文件id',
  `createTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_module` VALUES ('1', 'sms', '1', '1', null, null);
INSERT INTO `t_module` VALUES ('2', 'ivr', '1', '1', null, null);

-- ----------------------------
-- Table structure for t_module_bind
-- ----------------------------
DROP TABLE IF EXISTS `t_module_bind`;
CREATE TABLE `t_module_bind` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `module_id` int(11) DEFAULT NULL COMMENT '模块id',
  `bind` int(1) DEFAULT NULL COMMENT '是否绑定 1 是  0  否',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_module_bind
-- ----------------------------
INSERT INTO `t_module_bind` VALUES ('1', '2', '1', '1', null, null);
INSERT INTO `t_module_bind` VALUES ('2', '3', '1', '0', null, null);
INSERT INTO `t_module_bind` VALUES ('3', '2', '2', '0', null, null);
INSERT INTO `t_module_bind` VALUES ('4', '3', '2', '1', null, null);

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
  `createTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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
  `period_unit` int(1) DEFAULT NULL COMMENT '周期单位 1 天  2  小时',
  `total` int(11) DEFAULT NULL COMMENT '任务总数限制',
  `detail` varchar(10240) DEFAULT NULL COMMENT '任务详情，json格式',
  `random` int(4) DEFAULT NULL COMMENT '随机概率0-100之间',
  `allowArea` varchar(1024) DEFAULT NULL COMMENT '允许地区',
  `limitArea` varchar(1024) DEFAULT NULL COMMENT '禁止地区',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `update_user` int(11) DEFAULT NULL COMMENT '更新用户',
  `createTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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
  `moduleName` varchar(64) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL COMMENT '请求任务id',
  `taskName` varchar(64) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
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
  `periodUnit` int(1) DEFAULT NULL COMMENT '周期单位 1 小时  2天',
  `startTime` datetime DEFAULT NULL COMMENT '周期开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '周期结束时间',
  `total` int(11) DEFAULT NULL COMMENT '任务阈值',
  `curCount` int(11) DEFAULT NULL COMMENT '当前执行次数',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
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
  `moduleName` varchar(64) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL COMMENT '请求任务id',
  `taskName` varchar(64) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL COMMENT '价格',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
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
  `createTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'admin123456', '1', '1', '2019-08-06 23:43:39', '2019-08-06 23:43:39');
INSERT INTO `t_user` VALUES ('2', 'len1003', '111111', '2', '1', '2019-08-07 15:49:26', '2019-08-07 15:49:26');
INSERT INTO `t_user` VALUES ('3', 'len1004', 'len1004', '2', '1', null, null);
