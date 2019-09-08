/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2019-09-08 23:36:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goodsName` varchar(255) DEFAULT NULL,
  `goodsTitle` varchar(255) DEFAULT NULL,
  `goodsImg` varchar(255) DEFAULT NULL,
  `goodsDetail` varchar(255) DEFAULT NULL,
  `goodsPrice` double(255,0) DEFAULT NULL,
  `goodsStock` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', '篮球鞋', '运动鞋', null, '李宁牌韦德签名室内鞋', '1000', '100');
INSERT INTO `goods` VALUES ('2', '足球鞋', '运动鞋', null, '李宁足球鞋', '500', '100');
INSERT INTO `goods` VALUES ('3', '篮球', '体育用品', null, '安踏篮球', '200', '50');
INSERT INTO `goods` VALUES ('4', '足球', '体育用品', null, '李宁足球', '300', '150');

-- ----------------------------
-- Table structure for mb_user
-- ----------------------------
DROP TABLE IF EXISTS `mb_user`;
CREATE TABLE `mb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mb_user
-- ----------------------------

-- ----------------------------
-- Table structure for ms_good
-- ----------------------------
DROP TABLE IF EXISTS `ms_good`;
CREATE TABLE `ms_good` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goodsId` int(11) DEFAULT NULL,
  `stockCount` int(255) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `miaoshaPrice` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ms_good
-- ----------------------------
INSERT INTO `ms_good` VALUES ('1', '1', '20', '2019-09-01 23:05:53', '2019-09-15 23:06:01', '50');
INSERT INTO `ms_good` VALUES ('2', '2', '20', '2019-09-01 23:06:17', '2019-09-15 23:06:23', '40');
INSERT INTO `ms_good` VALUES ('3', '3', '20', '2019-09-01 23:06:35', '2019-09-15 23:06:38', '30');
INSERT INTO `ms_good` VALUES ('4', '4', '20', '2019-09-01 23:06:47', '2019-09-15 23:06:50', '20');

-- ----------------------------
-- Table structure for ms_order
-- ----------------------------
DROP TABLE IF EXISTS `ms_order`;
CREATE TABLE `ms_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `goodsId` int(11) DEFAULT NULL,
  `deliveryAddrId` int(11) DEFAULT NULL,
  `goodsName` varchar(255) DEFAULT NULL,
  `goodsCount` int(255) DEFAULT NULL,
  `goodsPrice` double DEFAULT NULL,
  `orderChannel` varchar(255) DEFAULT NULL,
  `status` int(255) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `payDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ms_order
-- ----------------------------

-- ----------------------------
-- Table structure for ms_order_ref
-- ----------------------------
DROP TABLE IF EXISTS `ms_order_ref`;
CREATE TABLE `ms_order_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `orderId` int(11) DEFAULT NULL,
  `goodsId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ms_order_ref
-- ----------------------------

-- ----------------------------
-- Table structure for ms_user
-- ----------------------------
DROP TABLE IF EXISTS `ms_user`;
CREATE TABLE `ms_user` (
  `id` varchar(255) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `registerDate` datetime DEFAULT NULL,
  `lastLoginDate` datetime DEFAULT NULL,
  `loginCount` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ms_user
-- ----------------------------
INSERT INTO `ms_user` VALUES ('15914343735', 'corn', 'e5d22cfc746c7da8da84e0a996e0fffa', '1a2b3c4d', '2019-04-28 22:58:28', '2019-04-28 22:58:30', '1');

-- ----------------------------
-- Table structure for ts_user
-- ----------------------------
DROP TABLE IF EXISTS `ts_user`;
CREATE TABLE `ts_user` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ts_user
-- ----------------------------
INSERT INTO `ts_user` VALUES ('1', 'suyiming', '071389565c5023ef76a5f37895e917a6');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'Jone', '18', 'test1@baomidou.com');
INSERT INTO `user` VALUES ('2', 'Jack', '20', 'test2@baomidou.com');
INSERT INTO `user` VALUES ('3', 'Tom', '28', 'test3@baomidou.com');
INSERT INTO `user` VALUES ('4', 'Sandy', '21', 'test4@baomidou.com');
INSERT INTO `user` VALUES ('5', 'Billie', '24', 'test5@baomidou.com');
