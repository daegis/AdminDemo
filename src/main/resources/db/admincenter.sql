/*
 Navicat Premium Data Transfer

 Source Server         : 本机mysql
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : localhost:3306
 Source Schema         : admincenter

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 07/01/2019 17:47:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admincenter_authority
-- ----------------------------
DROP TABLE IF EXISTS `admincenter_authority`;
CREATE TABLE `admincenter_authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority_name` varchar(255) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admincenter_authority
-- ----------------------------
BEGIN;
INSERT INTO `admincenter_authority` VALUES (3, 'ROOT', 4, 'glyphicon glyphicon-leaf', 'root');
INSERT INTO `admincenter_authority` VALUES (4, 'operator', 0, 'glyphicon glyphicon-leaf', '操作员');
INSERT INTO `admincenter_authority` VALUES (6, 'c1', 3, 'glyphicon glyphicon-align-left', 'c1');
INSERT INTO `admincenter_authority` VALUES (7, 'c2', 3, 'glyphicon glyphicon-stats', 'c2');
INSERT INTO `admincenter_authority` VALUES (8, 'c3', 3, 'glyphicon glyphicon-stats', 'c3');
INSERT INTO `admincenter_authority` VALUES (9, 'd1', 7, 'glyphicon glyphicon-stats', 'd1');
INSERT INTO `admincenter_authority` VALUES (10, 'd2', 7, 'glyphicon glyphicon-stats', 'd2');
COMMIT;

-- ----------------------------
-- Table structure for admincenter_operator
-- ----------------------------
DROP TABLE IF EXISTS `admincenter_operator`;
CREATE TABLE `admincenter_operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_name` varchar(255) DEFAULT NULL,
  `operator_unique_salt` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `operator_nick_name` varchar(255) DEFAULT NULL,
  `operator_source` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admincenter_operator
-- ----------------------------
BEGIN;
INSERT INTO `admincenter_operator` VALUES (1, 'admin', 'as', '45455868334645784F4349434331765773443032557552785878467843772F4552666B526848654F7148303D', '大闸蟹', 'hiapp');
INSERT INTO `admincenter_operator` VALUES (2, 'user', NULL, NULL, NULL, NULL);
INSERT INTO `admincenter_operator` VALUES (3, 'zzb', '35532B386F5A437364774476486F5352686E4A6F36654172486E55656176644C7A5863302B7548566562383D', '793672763135626E5A633730342F68766145744E4B61532F4158567A592F37642B4D38596341475853686F3D', 'zzb', 'hnair');
COMMIT;

-- ----------------------------
-- Table structure for admincenter_operator_role
-- ----------------------------
DROP TABLE IF EXISTS `admincenter_operator_role`;
CREATE TABLE `admincenter_operator_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admincenter_operator_role
-- ----------------------------
BEGIN;
INSERT INTO `admincenter_operator_role` VALUES (1, 1, 1);
INSERT INTO `admincenter_operator_role` VALUES (2, 3, 3);
COMMIT;

-- ----------------------------
-- Table structure for admincenter_role
-- ----------------------------
DROP TABLE IF EXISTS `admincenter_role`;
CREATE TABLE `admincenter_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admincenter_role
-- ----------------------------
BEGIN;
INSERT INTO `admincenter_role` VALUES (1, 'admin', '超级管理员');
INSERT INTO `admincenter_role` VALUES (3, '角色2', '22222');
COMMIT;

-- ----------------------------
-- Table structure for admincenter_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `admincenter_role_authority`;
CREATE TABLE `admincenter_role_authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `authority_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admincenter_role_authority
-- ----------------------------
BEGIN;
INSERT INTO `admincenter_role_authority` VALUES (3, 2, 9);
INSERT INTO `admincenter_role_authority` VALUES (4, 1, 4);
INSERT INTO `admincenter_role_authority` VALUES (5, 3, 7);
INSERT INTO `admincenter_role_authority` VALUES (6, 3, 9);
INSERT INTO `admincenter_role_authority` VALUES (7, 3, 10);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
