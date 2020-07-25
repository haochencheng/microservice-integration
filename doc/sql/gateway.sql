# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.23)
# Database: gateway
# Generation Time: 2019-05-08 07:15:43 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table application_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `application_definition`;

CREATE TABLE `application_definition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `application_name` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `application_path` varchar(20) NOT NULL DEFAULT '' COMMENT '应用path',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用，0不启用1启用。默认启用',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `applicagtion_path_index` (`application_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `application_definition` WRITE;
/*!40000 ALTER TABLE `application_definition` DISABLE KEYS */;

INSERT INTO `application_definition` (`id`, `application_name`, `application_path`, `enabled`, `desc`, `create_time`)
VALUES
	(2,'microservice-integration-app','/app',1,NULL,'2019-05-08 13:35:12');

/*!40000 ALTER TABLE `application_definition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table gateway_filter_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_filter_definition`;

CREATE TABLE `gateway_filter_definition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `route_definition_id` varchar(50) NOT NULL DEFAULT '' COMMENT '路由id',
  `filter_definition_args` json NOT NULL COMMENT '路由过滤器信息',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '过滤器名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '启用禁用 0 禁用 1 启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table gateway_predicate_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_predicate_definition`;

CREATE TABLE `gateway_predicate_definition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `route_definition_id` varchar(50) NOT NULL DEFAULT '' COMMENT '路由id',
  `predicate_definition_args` varchar(50) NOT NULL DEFAULT '' COMMENT '路由断言参数',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '断言名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 0 禁用 1启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `gateway_predicate_definition` WRITE;
/*!40000 ALTER TABLE `gateway_predicate_definition` DISABLE KEYS */;

INSERT INTO `gateway_predicate_definition` (`id`, `route_definition_id`, `predicate_definition_args`, `desc`, `name`, `create_time`, `enabled`)
VALUES
	(3,'81abf515-b427-4e9f-a26c-3ebf2331e707','/app/**','','Path','2019-05-08 14:34:49',1);

/*!40000 ALTER TABLE `gateway_predicate_definition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table gateway_route_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_route_definition`;

CREATE TABLE `gateway_route_definition` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `route_definition_uri` varchar(100) NOT NULL DEFAULT '' COMMENT '路由目标服务器url',
  `order` int(5) DEFAULT '0' COMMENT '排序',
  `desc` varchar(100) DEFAULT NULL COMMENT '描述',
  `name` varchar(50) DEFAULT NULL COMMENT '路由名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '启用禁用 0 禁用 1 启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `gateway_route_definition` WRITE;
/*!40000 ALTER TABLE `gateway_route_definition` DISABLE KEYS */;

INSERT INTO `gateway_route_definition` (`id`, `route_definition_uri`, `order`, `desc`, `name`, `create_time`, `enabled`)
VALUES
	('81abf515-b427-4e9f-a26c-3ebf2331e707','lb://microservice-integration-app',0,'','app','2019-05-08 14:34:32',1);

/*!40000 ALTER TABLE `gateway_route_definition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table service_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `service_definition`;

CREATE TABLE `service_definition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `service_path` varchar(50) NOT NULL DEFAULT '' COMMENT '请求路径',
  `desc` varchar(200) DEFAULT NULL,
  `service_method` varchar(10) NOT NULL DEFAULT 'GET' COMMENT '请求方法',
  `need_authorization` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否需要授权 0 不需要 1 需要',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用，0 不启用、1 启用默认启用',
  `application_definition_id` int(11) NOT NULL COMMENT '系统id',
  `service_name` varchar(100) DEFAULT NULL COMMENT '服务名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_path_index` (`service_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `service_definition` WRITE;
/*!40000 ALTER TABLE `service_definition` DISABLE KEYS */;

INSERT INTO `service_definition` (`id`, `service_path`, `desc`, `service_method`, `need_authorization`, `enabled`, `application_definition_id`, `service_name`, `create_time`)
VALUES
	(3,'/list','获取app信息','POST',1,1,2,'获取app信息','2019-05-08 14:10:27'),
	(4,'/info','获取app信息','GET',2,1,2,'获取app信息','2019-05-08 14:10:27'),
	(5,'/error','error','',0,1,2,'error','2019-05-08 14:10:27'),
	(6,'/no','不需要鉴权','POST',0,1,2,'不需要鉴权','2019-05-08 14:39:17');

/*!40000 ALTER TABLE `service_definition` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
