-- Create table
create table IOM_SERVER_ADAPTATION
(
  ID          NUMBER(9) PRIMARY KEY,
  SERVER_CODE VARCHAR2(32) not null,
  SERVER_SQL  VARCHAR2(1024),
  SERVER_TYPE VARCHAR2(32) not null,
  CLAZZ_NAME  VARCHAR2(64),
  PARAM_CODE  VARCHAR2(32),
  STATE       NUMBER(1) not null,
  CREATE_DATE DATE default SYSDATE,
  STATE_DATE  DATE
)
-- Add comments to the table 
comment on table IOM_SERVER_ADAPTATION
  is '适配服务表';
-- Add comments to the columns 
comment on column IOM_SERVER_ADAPTATION.ID
  is '主键ID';
comment on column IOM_SERVER_ADAPTATION.SERVER_CODE
  is '服务编码';
comment on column IOM_SERVER_ADAPTATION.SERVER_SQL
  is '执行sql';
comment on column IOM_SERVER_ADAPTATION.SERVER_TYPE
  is '执行方法（sql）类型，SELECT,INSERT,UPDATE,DELETE';
comment on column IOM_SERVER_ADAPTATION.CLAZZ_NAME
  is '定制化Java类名';
comment on column IOM_SERVER_ADAPTATION.PARAM_CODE
  is '参数对应编码';
comment on column IOM_SERVER_ADAPTATION.STATE
  is '状态，0在用1废弃';
comment on column IOM_SERVER_ADAPTATION.CREATE_DATE
  is '创建时间';
comment on column IOM_SERVER_ADAPTATION.STATE_DATE
  is '状态时间';
-- CREATE SEQUENCE
CREATE SEQUENCE ADAPTATION_ID_SEQ
MINVALUE 1
MAXVALUE 99999999999999999999
START WITH 1
INCREMENT BY 1
CACHE 10;
----------------------------------------------------------------------
-- Create table
create table IOM_SERVER_ADAPTATION_PARAM
(
  ID          NUMBER(9) PRIMARY KEY,
  PARAM_CODE  VARCHAR2(32) not null,
  PAGE_CODE   VARCHAR2(32) not null,
  DB_CODE     VARCHAR2(32) not null,
  PARAM_SQL   VARCHAR2(1024),
  STATE       NUMBER(1) not null,
  CREATE_DATE DATE default SYSDATE,
  STATE_DATE  DATE,
  PARAM_SIGN  VARCHAR2(32) not null
)
-- Add comments to the table 
comment on table IOM_SERVER_ADAPTATION_PARAM
  is '适配服务参数表';
-- Add comments to the columns 
comment on column IOM_SERVER_ADAPTATION_PARAM.ID
  is '主键';
comment on column IOM_SERVER_ADAPTATION_PARAM.PARAM_CODE
  is '参数对应编码';
comment on column IOM_SERVER_ADAPTATION_PARAM.PAGE_CODE
  is '页面字段key';
comment on column IOM_SERVER_ADAPTATION_PARAM.DB_CODE
  is 'sql字段key';
comment on column IOM_SERVER_ADAPTATION_PARAM.PARAM_SQL
  is '参数sql';
comment on column IOM_SERVER_ADAPTATION_PARAM.STATE
  is '状态，0在用1废弃';
comment on column IOM_SERVER_ADAPTATION_PARAM.CREATE_DATE
  is '创建时间';
comment on column IOM_SERVER_ADAPTATION_PARAM.STATE_DATE
  is '状态时间';
comment on column IOM_SERVER_ADAPTATION_PARAM.PARAM_SIGN
  is '连接符（参数位置）INSERT,SET,WHERE';
-- CREATE SEQUENCE
CREATE SEQUENCE ADAPTATION_PARAM_ID_SEQ
MINVALUE 1
MAXVALUE 99999999999999999999
START WITH 1
INCREMENT BY 1
CACHE 10;