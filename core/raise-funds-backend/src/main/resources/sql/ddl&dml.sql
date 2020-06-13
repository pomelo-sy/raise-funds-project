/*
SQLyog 企业版 - MySQL GUI v8.14
MySQL - 5.5.65-MariaDB : Database - raise_funds_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`raise_funds_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `raise_funds_db`;

/*Table structure for table `sys_approval_ticket` */

DROP TABLE IF EXISTS `sys_approval_ticket`;

CREATE TABLE `sys_approval_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `funds_desc_id` int(11) DEFAULT NULL,
  `approval_status` int(11) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `OWNER` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_approval_ticket` */

/*Table structure for table `sys_operator` */

DROP TABLE IF EXISTS `sys_operator`;

CREATE TABLE `sys_operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) DEFAULT NULL,
  `login_name` varchar(30) DEFAULT NULL,
  `PASSWORD` varchar(20) DEFAULT NULL,
  `priviliage` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_operator` */

/*Table structure for table `t_raise_funds_desc` */

DROP TABLE IF EXISTS `t_raise_funds_desc`;

CREATE TABLE `t_raise_funds_desc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(600) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `funds_target` decimal(18,2) DEFAULT NULL,
  `sponsor_user_id` int(11) DEFAULT NULL,
  `received_user_id` int(11) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `funds_counts` int(11) DEFAULT NULL,
  `funds_collected` int(11) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `sponsor_user_name` varchar(50) DEFAULT NULL,
  `received_user_name` varchar(50) DEFAULT NULL,
  `open_id` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `t_raise_funds_desc` */

insert  into `t_raise_funds_desc`(`id`,`content`,`title`,`funds_target`,`sponsor_user_id`,`received_user_id`,`create_date`,`funds_counts`,`funds_collected`,`STATUS`,`sponsor_user_name`,`received_user_name`,`open_id`) values (1,'唯小人与女人难养也','帮帮忙','10000.00',1,2,'2020-06-06 00:00:00',2,300,1,'Michael Jordan','小李',NULL),(2,'世界那么大 我要去看看','谢谢弄','20000.00',3,4,'2020-06-06 00:00:00',1,100,1,'Kobe Bryant','jordan',NULL),(3,'222','111','555.00',NULL,NULL,'2020-06-13 10:22:46',NULL,NULL,0,'333',NULL,'oCLaowcmAeHb3-wOJ9ZkoJwZdn6Y'),(4,'222','111','555.00',NULL,NULL,'2020-06-13 10:23:19',NULL,NULL,0,'333',NULL,'oCLaowcmAeHb3-wOJ9ZkoJwZdn6Y'),(5,'222','111','555.00',NULL,NULL,'2020-06-13 10:45:00',NULL,NULL,0,'333',NULL,'oCLaowcmAeHb3-wOJ9ZkoJwZdn6Y'),(6,'222','1111','55.00',NULL,NULL,'2020-06-13 10:50:21',NULL,NULL,0,'333',NULL,'oCLaowcmAeHb3-wOJ9ZkoJwZdn6Y');

/*Table structure for table `t_token` */

DROP TABLE IF EXISTS `t_token`;

CREATE TABLE `t_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(32) DEFAULT NULL,
  `token` varchar(256) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `token_type` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_token` */

insert  into `t_token`(`id`,`app_id`,`token`,`create_time`,`update_time`,`token_type`) values (1,'wx_raiseFunds','34_BdnsYGrDjeNaj4LexFS78NSJPlD-yX4h4gHLdqZxDMaT3mvXEhpb0owLDgbP37p70B-4FCDgFtToPdNXMrI39BqavZQep2WvWq83VTTy1Ib3k8DrN5BEZRwAR3nJYzjfO6ByEevm0TYP8hxXTZFdAGALRX','2020-06-11 00:00:00','2020-06-13 10:50:12','1'),(2,'wx_raiseFunds','kgt8ON7yVITDhtdwci0qeX0ZS-JLRnt3Aiw_yJGjvGxdQzDOse4OH3Sip0ZVGgDLeGsUtmWxP3F_joQw7BYIiA','2020-06-11 00:00:00','2020-06-13 10:50:12','2');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `wechat_name` varchar(50) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `last_login` datetime NOT NULL,
  `city` varchar(64) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  `head_img_url` varchar(256) DEFAULT NULL,
  `union_id` varchar(256) DEFAULT NULL,
  `open_id` varchar(128) DEFAULT NULL,
  `nick_name` varchar(128) DEFAULT NULL,
  `province` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`NAME`,`age`,`wechat_name`,`sex`,`last_login`,`city`,`country`,`head_img_url`,`union_id`,`open_id`,`nick_name`,`province`) values (7,NULL,NULL,NULL,1,'2020-06-12 01:45:49','普陀','中国','http://thirdwx.qlogo.cn/mmopen/PDXc7rMdmLewiccVOaYeCQibWmicsEn9LeqPfDZibIkjn5RiaExqq0hCIiacohsVI39zZLYq2xjjtKMOaOXFSibmygkO71CjDr3iaeaW/132',NULL,'oCLaowcmAeHb3-wOJ9ZkoJwZdn6Y','撮鳊鱼','上海');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
