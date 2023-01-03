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
  `user_id` int unsigned NOT NULL, #发布者uid
  `article_id` int unsigned NOT NULL AUTO_INCREMENT, #文章id
  `article_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, #文章标题
  `article_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP, #文章创建时间
  `article_tag` int(32) unsigned not null default '0', #文章标签，标签最多4个（0~63），通过数字形式存放00000000~63636363，每次取模100得到一个标签
  `article_is_free` tinyint(1) NOT NULL, #文章是否免费
  `article_price` decimal(7, 2) NOT NULL, #文章标价
  `article_detail` varchar(10000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, #文章正文
  `article_view_count` int unsigned not null default '0', #文章浏览次数
  `article_state` tinyint(2) DEFAULT '0', #文章状态，正常，已删除，审核中
  `article_visible` tinyint(1) DEFAULT '1', #文章可见性
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
