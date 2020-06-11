
CREATE DATABASE /*!32312 IF NOT EXISTS*/`raise_funds_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `raise_funds_db`;

/*Table structure for table `sys_approval_ticket` */

DROP TABLE IF EXISTS `sys_approval_ticket`;

CREATE TABLE `sys_approval_ticket` (
  `id` INT(11)  PRIMARY KEY AUTO_INCREMENT,
  `funds_desc_id` INT(11) DEFAULT NULL,
  `approval_status` INT(11) DEFAULT NULL,
  `create_date` DATETIME NOT NULL,
  `update_date` DATETIME NOT NULL,
  `OWNER` INT(11) DEFAULT NULL

) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `sys_approval_ticket` */

/*Table structure for table `sys_operator` */

DROP TABLE IF EXISTS `sys_operator`;

CREATE TABLE `sys_operator` (
  `id` INT(11) PRIMARY KEY AUTO_INCREMENT,
  `NAME` VARCHAR(30) DEFAULT NULL,
  `login_name` VARCHAR(30) DEFAULT NULL,
  `PASSWORD` VARCHAR(20) DEFAULT NULL,
  `priviliage` INT(11) DEFAULT NULL

) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `sys_operator` */

/*Table structure for table `t_raise_funds_desc` */

DROP TABLE IF EXISTS `t_raise_funds_desc`;

CREATE TABLE `t_raise_funds_desc` (
  `id` INT(11) PRIMARY KEY AUTO_INCREMENT,
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
  `received_user_name` VARCHAR(50) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `t_raise_funds_desc` */

INSERT  INTO `t_raise_funds_desc`(`id`,`content`,`title`,`funds_target`,`sponsor_user_id`,`received_user_id`,`create_date`,`funds_counts`,`funds_collected`,`STATUS`,`sponsor_user_name`,`received_user_name`) VALUES (1,'唯小人与女人难养也','帮帮忙',10000,1,2,'2020-06-06 00:00:00',2,300,1,'Michael Jordan','小李'),(2,'世界那么大 我要去看看','谢谢弄',20000,3,4,'2020-06-06 00:00:00',1,100,1,'Kobe Bryant','jordan');

/*Table structure for table `t_token` */

DROP TABLE IF EXISTS `t_token`;

CREATE TABLE `t_token` (
  `id` INT(11) PRIMARY KEY AUTO_INCREMENT,
  `app_id` VARCHAR(32) DEFAULT NULL,
  `access_token` VARCHAR(256) DEFAULT NULL,
  `create_time` DATE DEFAULT NULL,
  `update_time` DATE DEFAULT NULL

) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `t_token` */

INSERT  INTO `t_token`(`id`,`app_id`,`access_token`,`create_time`,`update_time`) VALUES (1,'wx_raiseFunds',NULL,'2020-06-11',NULL);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` INT(11) PRIMARY KEY AUTO_INCREMENT,
  `NAME` VARCHAR(20) DEFAULT NULL,
  `age` INT(11) DEFAULT NULL,
  `wechat_name` VARCHAR(50) DEFAULT NULL,
  `sex` INT(11) DEFAULT NULL,
  `last_login` DATETIME NOT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

INSERT  INTO `t_user`(`id`,`NAME`,`age`,`wechat_name`,`sex`,`last_login`) VALUES (1,'小王',20,'王伯伯',1,'2020-06-06 00:00:00'),(2,'小李',21,'小李飞刀',1,'2020-06-06 00:00:00');
