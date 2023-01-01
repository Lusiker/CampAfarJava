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

 Date: 31/12/2022 20:09:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `user_id` int unsigned NOT NULL,
  `collection_type` tinyint(1) NOT NULL, #此收藏记录的类别
  `target_id` int unsigned NOT NULL, #收藏对象的id，为方便操作统一令文章与问题id等长
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP #收藏时间
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
