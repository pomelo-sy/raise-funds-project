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

/*Table structure for table `sys_approval_ticket` */

DROP TABLE IF EXISTS `sys_approval_ticket`;

CREATE TABLE `sys_approval_ticket` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `funds_desc_id` INT(11) DEFAULT NULL,
  `approval_status` INT(11) DEFAULT NULL,
  `create_date` DATETIME NOT NULL,
  `update_date` DATETIME NOT NULL,
  `OWNER` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `sys_approval_ticket` */

/*Table structure for table `sys_operator` */

DROP TABLE IF EXISTS `sys_operator`;

CREATE TABLE `sys_operator` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(30) DEFAULT NULL,
  `login_name` VARCHAR(30) DEFAULT NULL,
  `PASSWORD` VARCHAR(20) DEFAULT NULL,
  `priviliage` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `sys_operator` */

/*Table structure for table `t_raise_funds_desc` */

DROP TABLE IF EXISTS `t_raise_funds_desc`;

CREATE TABLE `t_raise_funds_desc` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(600) DEFAULT NULL,
  `title` VARCHAR(100) DEFAULT NULL,
  `funds_target` INT(11) DEFAULT NULL,
  `sponsor_user_id` INT(11) DEFAULT NULL,
  `received_user_id` INT(11) DEFAULT NULL,
  `create_date` DATETIME NOT NULL,
  `funds_counts` INT(11) DEFAULT NULL,
  `funds_collected` INT(11) DEFAULT NULL,
  `STATUS` INT(11) DEFAULT NULL,
  `sponsor_user_name` VARCHAR(50) DEFAULT NULL,
  `received_user_name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_raise_funds_desc` */

INSERT  INTO `t_raise_funds_desc`(`id`,`content`,`title`,`funds_target`,`sponsor_user_id`,`received_user_id`,`create_date`,`funds_counts`,`funds_collected`,`STATUS`,`sponsor_user_name`,`received_user_name`) VALUES (1,'唯小人与女人难养也','帮帮忙',10000,1,2,'2020-06-06 00:00:00',2,300,1,'Michael Jordan','小李'),(2,'世界那么大 我要去看看','谢谢弄',20000,3,4,'2020-06-06 00:00:00',1,100,1,'Kobe Bryant','jordan');

/*Table structure for table `t_token` */

DROP TABLE IF EXISTS `t_token`;

CREATE TABLE `t_token` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `app_id` VARCHAR(32) DEFAULT NULL,
  `token` VARCHAR(256) DEFAULT NULL,
  `create_time` DATE DEFAULT NULL,
  `update_time` DATE DEFAULT NULL,
  `token_type` VARCHAR(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_token` */

INSERT  INTO `t_token`(`id`,`app_id`,`token`,`create_time`,`update_time`,`token_type`) VALUES (1,'wx_raiseFunds','34_4Cth6AZj4ZSsWOwU4Y4kpZQ4ZFmmRCfXmBRIoloWcjlhm7nj9YRVnWKwpvfiLJQ5nDHFWxohp3n_KDo4CzvvHtpB_t4i--NmkdbafEVFXW62rB4bkXLLel9DU1RSnQwcA3EhAiJ75Rq3EN7xSTBjAJAVWE','2020-06-11','2020-06-12','1'),(2,'wx_raiseFunds','kgt8ON7yVITDhtdwci0qeX0ZS-JLRnt3Aiw_yJGjvGyD80ZxVNGLKYa135rlIyKm4gVi9Tc9FCzykMUE52TonQ','2020-06-11','2020-06-12','2');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(20) DEFAULT NULL,
  `age` INT(11) DEFAULT NULL,
  `wechat_name` VARCHAR(50) DEFAULT NULL,
  `sex` INT(11) DEFAULT NULL,
  `last_login` DATETIME NOT NULL,
  `city` VARCHAR(64) DEFAULT NULL,
  `country` VARCHAR(64) DEFAULT NULL,
  `head_img_url` VARCHAR(256) DEFAULT NULL,
  `union_id` VARCHAR(256) DEFAULT NULL,
  `open_id` VARCHAR(64) DEFAULT NULL,
  `nick_name` VARCHAR(128) DEFAULT NULL,
  `province` VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

INSERT  INTO `t_user`(`id`,`NAME`,`age`,`wechat_name`,`sex`,`last_login`,`city`,`country`,`head_img_url`,`union_id`,`open_id`,`nick_name`,`province`) VALUES (7,NULL,NULL,NULL,1,'2020-06-12 01:45:49','普陀','中国','http://thirdwx.qlogo.cn/mmopen/PDXc7rMdmLewiccVOaYeCQibWmicsEn9LeqPfDZibIkjn5RiaExqq0hCIiacohsVI39zZLYq2xjjtKMOaOXFSibmygkO71CjDr3iaeaW/132',NULL,'oCLaowcmAeHb3-wOJ9ZkoJwZdn6Y','撮鳊鱼','上海');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
