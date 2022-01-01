/*
Navicat MySQL Data Transfer

Source Server         : 192.168.12.10_3306
Source Server Version : 50729
Source Host           : 192.168.12.10:3306
Source Database       : game_king

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-12-01 11:53:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for conf_chapter
-- ----------------------------
DROP TABLE IF EXISTS `conf_chapter`;
CREATE TABLE `conf_chapter` (
  `id` int(11) NOT NULL,
  `chapterId` int(11) DEFAULT '0' COMMENT '出售类型',
  `sectionId` int(11) DEFAULT NULL,
  `maxGold` int(11) DEFAULT '0' COMMENT '最大可获得金币',
  `power` int(11) DEFAULT '0' COMMENT '需要能量',
  `gameTypeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of conf_chapter
-- ----------------------------
INSERT INTO `conf_chapter` VALUES ('1', '1', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('2', '1', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('3', '1', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('4', '1', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('5', '1', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('6', '1', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('7', '1', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('8', '1', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('9', '2', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('10', '2', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('11', '2', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('12', '2', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('13', '2', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('14', '2', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('15', '2', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('16', '2', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('17', '3', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('18', '3', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('19', '3', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('20', '3', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('21', '3', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('22', '3', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('23', '3', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('24', '3', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('25', '4', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('26', '4', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('27', '4', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('28', '4', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('29', '4', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('30', '4', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('31', '4', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('32', '4', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('33', '5', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('34', '5', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('35', '5', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('36', '5', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('37', '5', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('38', '5', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('39', '5', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('40', '5', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('41', '6', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('42', '6', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('43', '6', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('44', '6', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('45', '6', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('46', '6', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('47', '6', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('48', '6', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('49', '7', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('50', '7', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('51', '7', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('52', '7', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('53', '7', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('54', '7', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('55', '7', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('56', '7', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('57', '8', '1', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('58', '8', '2', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('59', '8', '3', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('60', '8', '4', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('61', '8', '5', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('62', '8', '6', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('63', '8', '7', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('64', '8', '8', '5000', '5', '1');
INSERT INTO `conf_chapter` VALUES ('65', '8', '0', '5000', '5', '2');

-- ----------------------------
-- Table structure for conf_game_type
-- ----------------------------
DROP TABLE IF EXISTS `conf_game_type`;
CREATE TABLE `conf_game_type` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of conf_game_type
-- ----------------------------
INSERT INTO `conf_game_type` VALUES ('1', '涛涛大冒险');
INSERT INTO `conf_game_type` VALUES ('2', '涛涛跑酷');

-- ----------------------------
-- Table structure for conf_setting
-- ----------------------------
DROP TABLE IF EXISTS `conf_setting`;
CREATE TABLE `conf_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(20) DEFAULT NULL,
  `platNo` int(11) DEFAULT '0' COMMENT '0.所有平台',
  `value` varchar(64) DEFAULT NULL,
  `desc` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of conf_setting
-- ----------------------------
INSERT INTO `conf_setting` VALUES ('1', 'max_chapter', '0', '{\"chapterId\":4}', null);

-- ----------------------------
-- Table structure for conf_shop
-- ----------------------------
DROP TABLE IF EXISTS `conf_shop`;
CREATE TABLE `conf_shop` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT '0' COMMENT '出售类型',
  `itemId` int(11) DEFAULT NULL,
  `itemName` varchar(32) DEFAULT NULL,
  `itemNum` int(11) DEFAULT NULL,
  `gold` int(11) DEFAULT NULL,
  `coin` int(11) DEFAULT NULL,
  `desc` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of conf_shop
-- ----------------------------
INSERT INTO `conf_shop` VALUES ('1', '1', '1', '慎', '1', '0', '3000', null);
INSERT INTO `conf_shop` VALUES ('2', '1', '2', '艾琳辣', '1', '0', '3000', null);
INSERT INTO `conf_shop` VALUES ('700', '2', '700', '飞镖', '1', '0', '0', '不可购买');
INSERT INTO `conf_shop` VALUES ('701', '2', '701', '魔杖', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('702', '2', '702', '血瓶', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('703', '2', '703', '武器包', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('704', '3', '704', '金币', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('705', '2', '705', '勋章', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('706', '3', '706', '彩虹石', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('707', '2', '707', '磁铁', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('708', '2', '708', '回复HP', '1', '0', '0', null);
INSERT INTO `conf_shop` VALUES ('709', '3', '709', '体力值', '1', '0', '0', null);

-- ----------------------------
-- Table structure for conf_sign
-- ----------------------------
DROP TABLE IF EXISTS `conf_sign`;
CREATE TABLE `conf_sign` (
  `signId` int(11) NOT NULL AUTO_INCREMENT,
  `signDay` int(11) DEFAULT NULL,
  `awardList` varchar(128) DEFAULT NULL,
  `vip` int(11) DEFAULT NULL,
  PRIMARY KEY (`signId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of conf_sign
-- ----------------------------
INSERT INTO `conf_sign` VALUES ('1', '1', '[[704,1000]]', '0');
INSERT INTO `conf_sign` VALUES ('2', '2', '[[703,5]]', null);
INSERT INTO `conf_sign` VALUES ('3', '3', '[[706,100]]', null);
INSERT INTO `conf_sign` VALUES ('4', '4', '[[702,10]]', null);
INSERT INTO `conf_sign` VALUES ('5', '5', '[[704,3000]]', null);
INSERT INTO `conf_sign` VALUES ('6', '6', '[[706,150]]', null);
INSERT INTO `conf_sign` VALUES ('7', '7', '[[706,200]]', null);

-- ----------------------------
-- Table structure for p_bag
-- ----------------------------
DROP TABLE IF EXISTS `p_bag`;
CREATE TABLE `p_bag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `propId` int(11) DEFAULT NULL,
  `propNum` int(11) DEFAULT '0',
  `state` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of p_bag
-- ----------------------------
INSERT INTO `p_bag` VALUES ('1', '1001', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('2', '1002', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('3', '1003', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('4', '1004', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('5', '1005', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('6', '1006', '1', '2', '1');
INSERT INTO `p_bag` VALUES ('7', '1007', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('8', '1008', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('9', '1009', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('10', '1014', '700', '5', '1');
INSERT INTO `p_bag` VALUES ('11', '1015', '700', '5', '1');
INSERT INTO `p_bag` VALUES ('12', '1010', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('13', '1011', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('14', '1018', '700', '5', '1');
INSERT INTO `p_bag` VALUES ('15', '1012', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('16', '1029', '1', '1', '3');
INSERT INTO `p_bag` VALUES ('17', '1013', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('18', '1030', '1', '1', '3');
INSERT INTO `p_bag` VALUES ('19', '1014', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('20', '1015', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('21', '1031', '700', '5', '1');
INSERT INTO `p_bag` VALUES ('22', '1016', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('23', '1017', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('24', '1018', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('30', '1006', '700', '362', '1');
INSERT INTO `p_bag` VALUES ('31', '1019', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('32', '1019', '700', '10', '1');
INSERT INTO `p_bag` VALUES ('35', '1013', '700', '5', '1');
INSERT INTO `p_bag` VALUES ('37', '1010', '700', '5', '1');
INSERT INTO `p_bag` VALUES ('38', '1020', '1', '2', '1');
INSERT INTO `p_bag` VALUES ('39', '1020', '700', '12', '1');
INSERT INTO `p_bag` VALUES ('43', '1021', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('44', '1021', '700', '123', '1');
INSERT INTO `p_bag` VALUES ('48', '1001', '700', '10005', '1');
INSERT INTO `p_bag` VALUES ('51', '1022', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('52', '1022', '700', '10', '1');
INSERT INTO `p_bag` VALUES ('53', '1023', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('54', '1023', '700', '26', '1');
INSERT INTO `p_bag` VALUES ('55', '1024', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('56', '1024', '700', '129', '1');
INSERT INTO `p_bag` VALUES ('58', '1025', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('59', '1025', '700', '141', '1');
INSERT INTO `p_bag` VALUES ('61', '1026', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('62', '1026', '700', '77', '1');
INSERT INTO `p_bag` VALUES ('65', '1025', '701', '12', '1');
INSERT INTO `p_bag` VALUES ('66', '1027', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('67', '1027', '700', '34', '1');
INSERT INTO `p_bag` VALUES ('68', '1028', '1', '2', '3');
INSERT INTO `p_bag` VALUES ('69', '1028', '700', '31', '1');

-- ----------------------------
-- Table structure for p_chapter
-- ----------------------------
DROP TABLE IF EXISTS `p_chapter`;
CREATE TABLE `p_chapter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `combatId` int(11) DEFAULT '0' COMMENT '关卡ID',
  `starLv` int(11) DEFAULT NULL,
  `passTime` int(11) DEFAULT NULL,
  `createTime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of p_chapter
-- ----------------------------

-- ----------------------------
-- Table structure for p_chapter_box
-- ----------------------------
DROP TABLE IF EXISTS `p_chapter_box`;
CREATE TABLE `p_chapter_box` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL,
  `chapterId` int(11) DEFAULT '0' COMMENT '关卡ID',
  `box1` int(11) DEFAULT NULL,
  `box2` int(11) DEFAULT NULL,
  `box3` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of p_chapter_box
-- ----------------------------

-- ----------------------------
-- Table structure for p_sign
-- ----------------------------
DROP TABLE IF EXISTS `p_sign`;
CREATE TABLE `p_sign` (
  `userId` bigint(20) NOT NULL,
  `sign1` int(11) DEFAULT NULL,
  `sign2` int(11) DEFAULT NULL,
  `sign3` int(11) DEFAULT NULL,
  `sign4` int(11) DEFAULT NULL,
  `sign5` int(11) DEFAULT NULL,
  `sign6` int(11) DEFAULT NULL,
  `sign7` int(11) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of p_sign
-- ----------------------------

-- ----------------------------
-- Table structure for p_user
-- ----------------------------
DROP TABLE IF EXISTS `p_user`;
CREATE TABLE `p_user` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `userName` varchar(32) DEFAULT '' COMMENT '角色名称',
  `account` varchar(32) DEFAULT NULL,
  `pwd` varchar(32) DEFAULT NULL,
  `platNo` int(11) DEFAULT '0' COMMENT '0.自测账号 1.微信平台 2.QQ平台',
  `platId` varchar(32) DEFAULT NULL,
  `platProtrait` varchar(64) DEFAULT '' COMMENT '平台头像',
  `platName` varchar(64) DEFAULT NULL,
  `protrait` int(11) DEFAULT '1' COMMENT '1-N游戏内部头像',
  `headFrame` int(11) DEFAULT '1' COMMENT '1-N游戏内部头像框',
  `gold` bigint(20) DEFAULT '0' COMMENT '金币',
  `coin` bigint(20) DEFAULT '0' COMMENT '点券',
  `power` int(11) DEFAULT '0' COMMENT '能量 or 体力',
  `powerTime` int(11) DEFAULT '0',
  `chapter` tinyblob COMMENT '关卡信息',
  `section` blob,
  `prop` blob,
  `ban` int(11) DEFAULT '0' COMMENT '0.正常 1.封号',
  `banMsg` varchar(64) DEFAULT '',
  `banTime` int(11) DEFAULT '0' COMMENT '封禁截止时间',
  `white` int(11) DEFAULT '0' COMMENT '0.常规 1.白名单',
  `ip` varchar(32) DEFAULT NULL,
  `createTime` int(11) DEFAULT NULL COMMENT '0 创建时间',
  `loginTime` int(11) DEFAULT '0' COMMENT '最后一次登录时间',
  `mxwShop` varchar(64) DEFAULT NULL,
  `gameType` int(5) DEFAULT NULL COMMENT '游戏类别',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1032 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of p_user
-- ----------------------------
INSERT INTO `p_user` VALUES ('1001', 'chenyu12', 'chenyu12', '123', '0', '0', null, null, '1', '0', '30000', '30000', '20', '1590483433', null, null, null, '0', null, '0', '0', null, '1588845335', '1588845335', null, '1');
INSERT INTO `p_user` VALUES ('1002', 'aqq1', 'aqq1', '123456', '0', '0', null, null, '1', '0', '30000', '30000', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588845412', '1588845412', null, '1');
INSERT INTO `p_user` VALUES ('1003', 'aqq3', 'aqq3', '123456', '0', '0', null, null, '1', '0', '30000', '30000', '20', '1590483609', null, null, null, '0', null, '0', '0', null, '1588845561', '1588845561', null, '1');
INSERT INTO `p_user` VALUES ('1004', 'aqq4', 'aqq4', '123456', '0', '0', null, null, '1', '0', '30000', '30000', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588845574', '1588845574', null, '1');
INSERT INTO `p_user` VALUES ('1005', 'aqqqqqqqqqqq', 'aqqqqqqqqqqq', '123456', '0', '0', null, null, '2', '0', '28835', '27035', '20', '1590484021', null, null, null, '0', null, '0', '0', null, '1588845582', '1588845582', null, '1');
INSERT INTO `p_user` VALUES ('1006', 'ht', 'ht', '123456', '0', '0', null, null, '1', '0', '1060', '45', '20', '1590484688', null, null, null, '0', null, '0', '0', null, '1588845582', '1588845582', null, '1');
INSERT INTO `p_user` VALUES ('1007', 'byqingyin', 'byqingyin', '123456', '0', '0', null, null, '1', '0', '30000', '30000', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588846224', '1588846224', null, '1');
INSERT INTO `p_user` VALUES ('1008', 'by', 'by', '123456', '0', '0', null, null, '1', '0', '9088', '27103', '20', '1590485664', null, null, null, '0', null, '0', '0', null, '1588846327', '1588846327', null, '1');
INSERT INTO `p_user` VALUES ('1009', 'aqqasdasdddd', 'aqqasdasdddd', '123456', '0', '0', null, null, '1', '0', '28692', '30027', '20', '1590484688', null, null, null, '0', null, '0', '0', null, '1588846656', '1588846656', null, '1');
INSERT INTO `p_user` VALUES ('1010', 'Mc', 'Mc', '123456', '0', '0', null, null, '2', '0', '30525', '27045', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588903209', '1588903209', null, '1');
INSERT INTO `p_user` VALUES ('1011', 'zww', 'zww', '123456', '0', '0', null, null, '1', '0', '30675', '30036', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588903696', '1588903696', null, '1');
INSERT INTO `p_user` VALUES ('1012', 'tongwen', 'tongwen', '123456', '0', '0', null, null, '1', '0', '30000', '30000', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588903918', '1588903918', null, '1');
INSERT INTO `p_user` VALUES ('1013', 'tw', 'tw', '123456', '0', '0', null, null, '2', '0', '28159', '27067', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588904123', '1588904123', null, '1');
INSERT INTO `p_user` VALUES ('1014', '17', '17', '123456', '0', '0', null, null, '1', '0', '26959', '30040', '20', '1590484080', null, null, null, '0', null, '0', '0', null, '1588904839', '1588904839', null, '1');
INSERT INTO `p_user` VALUES ('1015', 'a5104323', 'a5104323', '123456', '0', '0', null, null, '1', '0', '29193', '30048', '20', '1590484296', null, null, null, '0', null, '0', '0', null, '1588904969', '1588904969', null, '1');
INSERT INTO `p_user` VALUES ('1016', 'aqq11', 'aqq11', '123456', '0', '0', null, null, '1', '0', '30000', '30000', '20', '1590484336', null, null, null, '0', null, '0', '0', null, '1588907242', '1588907242', null, '1');
INSERT INTO `p_user` VALUES ('1017', 'z', 'z', '123456', '0', '0', null, null, '1', '0', '30277', '30016', '20', '1590484643', null, null, null, '0', null, '0', '0', null, '1588907366', '1588907366', null, '1');
INSERT INTO `p_user` VALUES ('1018', 'byb', 'byb', '123456', '0', '0', null, null, '1', '0', '30038', '30000', '20', '1590484644', null, null, null, '0', null, '0', '0', null, '1588919328', '1588919328', null, '1');
INSERT INTO `p_user` VALUES ('1019', 'aqqqq', 'aqqqq', '123456', '0', null, null, null, '1', '0', '30000', '30000', '20', '1590484655', null, null, null, '0', null, '0', '0', null, '1589189289', '1589189289', null, '1');
INSERT INTO `p_user` VALUES ('1020', 'h1t', 'h1t', '123456', '0', null, null, null, '2', '0', '30541', '27012', '20', '1590484656', null, null, null, '0', null, '0', '0', null, '1589193423', '1589193423', null, '1');
INSERT INTO `p_user` VALUES ('1021', 'ht1', 'ht1', '123456', '0', null, null, null, '1', '0', '282', '4', '20', '1590484688', null, null, null, '0', null, '0', '0', null, '1589254237', '1589254237', null, '1');
INSERT INTO `p_user` VALUES ('1022', 'aqq', 'aqq', '123456', '0', null, null, null, '1', '0', '30000', '30000', '20', '1590484658', null, null, null, '0', null, '0', '0', null, '1589870693', '1589870693', null, '1');
INSERT INTO `p_user` VALUES ('1023', 'w', 'w', '123456', '0', null, null, null, '1', '0', '30060', '30001', '20', '1590484659', null, null, null, '0', null, '0', '0', null, '1590132881', '1590132881', null, '1');
INSERT INTO `p_user` VALUES ('1024', 'zz', 'zz', '123456', '0', null, null, null, '2', '0', '25485', '27018', '20', '1590484663', null, null, null, '0', null, '0', '0', null, '1590132935', '1590132935', null, '1');
INSERT INTO `p_user` VALUES ('1025', 'zz1', 'zz1', '123456', '0', null, null, null, '1', '0', '22045', '27138', '20', '1590486169', null, null, null, '0', null, '0', '0', null, '1590140169', '1590140169', null, '1');
INSERT INTO `p_user` VALUES ('1026', 'by1', 'by1', '123456', '0', null, null, null, '2', '0', '20946', '27079', '20', '1590484678', null, null, null, '0', null, '0', '0', null, '1590374775', '1590374775', null, '1');
INSERT INTO `p_user` VALUES ('1027', 'zmx', 'zmx', '123456', '0', null, null, null, '1', '0', '30051', '30003', '20', '1590485664', null, null, null, '0', null, '0', '0', null, '1590462242', '1590462242', null, '1');
INSERT INTO `p_user` VALUES ('1028', 'zmx', 'zmx', '123456', '0', null, null, null, '1', '0', '30180', '30000', '20', '1590485145', null, null, null, '0', null, '0', '0', null, '1590462336', '1590462336', null, '2');
INSERT INTO `p_user` VALUES ('1029', 'zz1', 'zz1', '123456', '0', null, null, null, '2', '0', '30000', '27000', '20', '1590486508', null, null, null, '0', null, '0', '0', null, '1590484682', '1590484682', null, '2');
INSERT INTO `p_user` VALUES ('1030', 'by1', 'by1', '123456', '0', null, null, null, '1', '0', '30000', '30000', '20', '1590485110', null, null, null, '0', null, '0', '0', null, '1590484758', '1590484758', null, '2');
INSERT INTO `p_user` VALUES ('1031', 'by', 'by', '123456', '0', null, null, null, '1', '0', '30000', '30000', '20', '1590486695', null, null, null, '0', null, '0', '0', null, '1590485223', '1590485223', null, '2');
