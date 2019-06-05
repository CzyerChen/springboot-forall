-- ----------------------------
-- Table structure for t_people
-- ----------------------------
DROP TABLE IF EXISTS `t_people`;
CREATE TABLE `t_people` (
  `pid` int(11) NOT NULL,
  `pname` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_people
-- ----------------------------
INSERT INTO `t_people` VALUES ('1', 'claire', '1', '1111111111');


-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL,
  `person_name` varchar(50) NOT NULL,
  `sex` char(20) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `person_id` (`person_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES ('1', '1', 'lily', 'f', 'china', 'zhongguo.beijing');
INSERT INTO `person` VALUES ('2', '2', 'lily', 'm', 'china', 'zhongguo.beijing');
INSERT INTO `person` VALUES ('3', '3', 'Aidrow', 'f', 'Russia', 'zhongguo.tianjin');
INSERT INTO `person` VALUES ('4', '4', 'chen', 'm', 'USA', 'zhongguo.beijing');
INSERT INTO `person` VALUES ('5', '5', 'karlys', 'f', 'china', 'zhongguo.shanghai');
INSERT INTO `person` VALUES ('6', '6', 'claire', 'm', 'UK', 'zhongguo.hebei');
