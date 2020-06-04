/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/6/4 22:23:48                            */
/*==============================================================*/





DROP TABLE IF EXISTS F_Raise_Funds_Desc;



DROP TABLE IF EXISTS Sys_Approval_Ticket;


DROP TABLE IF EXISTS Sys_operator;


DROP TABLE IF EXISTS USER;

/*==============================================================*/
/* Table: F_Raise_Funds_Desc                                    */
/*==============================================================*/
CREATE TABLE F_Raise_Funds_Desc
(
   id                   INT NOT NULL,
   content              VARCHAR(600),
   title                VARCHAR(100),
   funds_target         INT,
   sponsor_user_id      INT,
   received_user_id     INT,
   create_date          DATETIME NOT NULL ,
   funds_counts         INT,
   funds_collected      INT,
   STATUS               INT
);

ALTER TABLE F_Raise_Funds_Desc
   ADD PRIMARY KEY (id);

/*==============================================================*/
/* Table: Sys_Approval_Ticket                                   */
/*==============================================================*/
CREATE TABLE Sys_Approval_Ticket
(
   id                   INT NOT NULL,
   funds_desc_id        INT,
   approval_status      INT,
   create_date          DATETIME NOT NULL ,
   update_date          DATETIME NOT NULL ,
   OWNER                INT
);

ALTER TABLE Sys_Approval_Ticket
   ADD PRIMARY KEY (id);

/*==============================================================*/
/* Table: Sys_operator                                          */
/*==============================================================*/
CREATE TABLE Sys_operator
(
   id                   CHAR(10) NOT NULL,
   NAME                 VARCHAR(30),
   login_name           VARCHAR(30),
   PASSWORD             VARCHAR(20),
   priviliage           INT
);

ALTER TABLE Sys_operator
   ADD PRIMARY KEY (id);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
CREATE TABLE USER
(
   id                   INT NOT NULL,
   NAME                 VARCHAR(20),
   age                  INT,
   wechat_name          VARCHAR(50),
   sex                  INT,
   last_login           DATETIME NOT NULL
);

ALTER TABLE USER
   ADD PRIMARY KEY (id);

