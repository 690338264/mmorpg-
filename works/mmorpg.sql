/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80013
Source Host           : localhost:3306
Source Database       : mmorpg

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2020-08-06 21:53:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bag
-- ----------------------------
DROP TABLE IF EXISTS `bag`;
CREATE TABLE `bag` (
  `volume` int(11) DEFAULT NULL,
  `item` varchar(255) DEFAULT NULL,
  `playerid` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of bag
-- ----------------------------
INSERT INTO `bag` VALUES ('36', '1,1,1,2,3,5,6,6,7,7,8', '1');
INSERT INTO `bag` VALUES ('36', '4', '3');
INSERT INTO `bag` VALUES ('36', null, '7');
INSERT INTO `bag` VALUES ('36', null, '100000002');
INSERT INTO `bag` VALUES ('36', null, '100000003');

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` bigint(20) DEFAULT NULL,
  `name` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `exp` int(11) DEFAULT NULL,
  `loc` int(255) DEFAULT NULL,
  `occupation` int(11) DEFAULT NULL,
  `equip` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `friend` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `roleId` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`roleId`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=100000004 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES ('692', 'lixin', '1', '1', '2', '6,7', '0', null, '1');
INSERT INTO `player` VALUES ('692', 'youx', '1', '1', '1', null, '0', null, '3');
INSERT INTO `player` VALUES ('695', 'qwq', '1', '1', '5', null, '0', null, '7');
INSERT INTO `player` VALUES ('695', 'e', '1', '1', '5', null, '0', null, '100000002');
INSERT INTO `player` VALUES ('696', 'zzz', '1', '1', '6', null, '0', null, '100000003');

-- ----------------------------
-- Table structure for sect
-- ----------------------------
DROP TABLE IF EXISTS `sect`;
CREATE TABLE `sect` (
  `id` int(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `memnum` int(11) NOT NULL,
  `level` int(11) DEFAULT NULL,
  `fame` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sect
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `psw` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=697 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('690', 'catherine', '690338264');
INSERT INTO `user` VALUES ('691', 'jtf', 'sags');
INSERT INTO `user` VALUES ('692', 'you', 'yousi');
INSERT INTO `user` VALUES ('694', 'cath', '789456');
INSERT INTO `user` VALUES ('695', 'yoyo', '123');
INSERT INTO `user` VALUES ('696', 'pop', '123');
