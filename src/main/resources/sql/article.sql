/*
 Navicat Premium Data Transfer

 Source Server         : database
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : campafar

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 31/12/2022 20:09:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `user_id` int(10) NOT NULL,
  `article_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `article_title` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `article_date` char(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `article_price` double(5, 3) NOT NULL,
  `article_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
