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

 Date: 31/12/2022 20:09:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `user_id_from` int unsigned NOT NULL, #提问用户uid
  `user_id_to` int unsigned NOT NULL, #回答用户uid
  `question_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, #提问id
  `question_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP, #提问创建于
  `question_title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, #提问标题
  `question_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '', #提问详情
  `question_tag` int(32) unsigned not null default '0', #问题标签，标签最多4个（0~63），通过数字形式存放00000000~63636363，每次取模100得到一个标签
  `question_answered_at` timestamp NULL DEFAULT NULL, #提问回答时间，为null时尚未回答
  `question_view_count` int unsigned not null default '0', #问题浏览次数
  `question_listen_price` decimal(7, 2) NOT NULL default '0.00', #旁听费用， 在回答创建时一并填写
  `question_state` tinyint(2) DEFAULT '0', #问题状态，正常，已删除，审核中
  `question_visible` tinyint(1) DEFAULT '1', #问题可见性
  `answer_detail` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '尚未回答', # 富文本形式的回答
  PRIMARY KEY (`question_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
