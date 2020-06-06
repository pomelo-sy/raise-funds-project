/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.5.5-10.3.10-MariaDB : Database - raise_funds_db
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

/*Table structure for table `f_raise_funds_desc` */

DROP TABLE IF EXISTS `f_raise_funds_desc`;

CREATE TABLE `f_raise_funds_desc` (
  `id` int(11) NOT NULL,
  `content` varchar(600) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `funds_target` int(11) DEFAULT NULL,
  `sponsor_user_id` int(11) DEFAULT NULL,
  `received_user_id` int(11) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `funds_counts` int(11) DEFAULT NULL,
  `funds_collected` int(11) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `sponsor_user_name` varchar(50) DEFAULT NULL,
  `received_user_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `f_raise_funds_desc` */

insert  into `f_raise_funds_desc`(`id`,`content`,`title`,`funds_target`,`sponsor_user_id`,`received_user_id`,`create_date`,`funds_counts`,`funds_collected`,`STATUS`,`sponsor_user_name`,`received_user_name`) values (1,'唯小人与女人难养也','帮帮忙',10000,1,2,'2020-06-06 00:00:00',2,300,1,'Michael Jordan','小李'),(2,'世界那么大 我要去看看','谢谢弄',20000,3,4,'2020-06-06 00:00:00',1,100,1,'Kobe Bryant','jordan');

/*Table structure for table `sys_approval_ticket` */

DROP TABLE IF EXISTS `sys_approval_ticket`;

CREATE TABLE `sys_approval_ticket` (
  `id` int(11) NOT NULL,
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
  `id` char(10) NOT NULL,
  `NAME` varchar(30) DEFAULT NULL,
  `login_name` varchar(30) DEFAULT NULL,
  `PASSWORD` varchar(20) DEFAULT NULL,
  `priviliage` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_operator` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `NAME` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `wechat_name` varchar(50) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `last_login` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`NAME`,`age`,`wechat_name`,`sex`,`last_login`) values (1,'小王',20,'王伯伯',1,'2020-06-06 00:00:00'),(2,'小李',21,'小李飞刀',1,'2020-06-06 00:00:00');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
