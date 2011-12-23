
--create table alot_mo_acnt_base_real_111221 as
--select * from auser.alot_mo_acnt_base@hiplus_link
;


--create table AUSER.ALOT_MO_ACNT_BASE_20111221 as
--select * from AUSER.ALOT_MO_ACNT_BASE
--;

--select * from alot_mo_acnt_base_real_111221;

--ALTER TABLE ALOT_MO_ACNT_BASE_real_111221 ADD(
--	CONSTRAINT ACNTT_BASE_PK PRIMARY KEY (BSN_DIV_CD, BANK_CD, MO_ACNT_NO));

--alter table alot_mo_acnt_base_REAL_111221 drop CONSTRAINT SYS_C0021617;

--select constraint_name
--from user_constraints
--where table_name = 'auser.alot_mo_acnt_base'
--;


-- ��������̺� �����̸Ӹ�Ű ���� ����
--alter table auser.alot_mo_acnt_base drop CONSTRAINT ALOT_MO_ACNT_BASE_PK;
--ALTER TABLE auser.ALOT_MO_ACNT_BASE ADD(CONSTRAINT ALOT_MO_ACNT_BASE_PK PRIMARY KEY (BSN_DIV_CD, BANK_CD));

-- ���� �츮���� ���� �߰� ��, ���� �� ��.
--alter table auser.alot_mo_acnt_base drop CONSTRAINT ALOT_MO_ACNT_BASE_PK;
--ALTER TABLE auser.ALOT_MO_ACNT_BASE ADD(CONSTRAINT ALOT_MO_ACNT_BASE_PK PRIMARY KEY (BSN_DIV_CD, BANK_CD, MO_ACNT_NO));

--ALTER TABLE TABLE�� DROP CONSTRAINT PK��;

-- PK �����ϱ�
--Alter TABLE ���̺��̸� drop primary key cascade
 
--PK�߰� �ϱ�
--ALTER TABLE ���̺��̸� ADD CONSTRAINT �ε��� �̸� PRIMARY KEY(field1, field2)

-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------

--CREATE TABLE alot_mo_acnt_base_20111205 AS (select * from AUSER.ALOT_MO_ACNT_BASE);
--select * from alot_mo_acnt_base_20111205;

--drop table auser.ALOT_MO_ACNT_BASE;

--insert into AUSER.ALOT_MO_ACNT_BASE
--select * from alot_mo_acnt_base_20111205 order by BSN_DIV_CD, BANK_CD, MO_ACNT_NO;
--
--commit;



-- DDL Script for TABLE AUSER.ALOT_MO_ACNT_BASE. Orange for ORACLE.
-- Generated on 2011/12/05 14:32:47 by ZUSER@CABISDEV

CREATE TABLE AUSER.ALOT_MO_ACNT_BASE 
(
	BSN_DIV_CD         CHAR (2) DEFAULT '' NOT NULL,
	BANK_CD            CHAR (3) DEFAULT '' NOT NULL,
	MO_ACNT_NO         VARCHAR2 (20) DEFAULT '' NOT NULL,
	RMRK               VARCHAR2 (100) DEFAULT '',
	FRST_REG_DT        CHAR (10) DEFAULT '',
	FRST_REG_TM        CHAR (6) DEFAULT '',
	FRST_REG_EMP_NO    VARCHAR2 (8) DEFAULT '',
	LAST_PROC_DT       CHAR (10) DEFAULT '',
	LAST_PROC_TM       CHAR (6) DEFAULT '',
	LAST_PROC_EMP_NO   VARCHAR2 (8) DEFAULT ''
)
TABLESPACE AUSER
PCTFREE 10
INITRANS 1
MAXTRANS 255
STORAGE
(
	INITIAL 65536
	NEXT 1048576
	MINEXTENTS 1
	MAXEXTENTS UNLIMITED
	BUFFER_POOL DEFAULT
)
LOGGING
DISABLE ROW MOVEMENT ;

GRANT DELETE ON AUSER.ALOT_MO_ACNT_BASE TO ZUSER;
GRANT INSERT ON AUSER.ALOT_MO_ACNT_BASE TO ZUSER;
GRANT SELECT ON AUSER.ALOT_MO_ACNT_BASE TO ZUSER;
GRANT UPDATE ON AUSER.ALOT_MO_ACNT_BASE TO ZUSER;

COMMENT ON TABLE AUSER.ALOT_MO_ACNT_BASE IS 'ALOT_�����_�⺻' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.BANK_CD IS '����_�ڵ�' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.BSN_DIV_CD IS '����_�����ڵ�' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.FRST_REG_DT IS '����_���_����' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.FRST_REG_EMP_NO IS '����_���_�����ȣ' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.FRST_REG_TM IS '����_���_�ð�' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.LAST_PROC_DT IS '����_ó��_����' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.LAST_PROC_EMP_NO IS '����_ó��_�����ȣ' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.LAST_PROC_TM IS '����_ó��_�ð�' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.MO_ACNT_NO IS '��_���¹�ȣ' ;

COMMENT ON COLUMN AUSER.ALOT_MO_ACNT_BASE.RMRK IS '���' ;

CREATE UNIQUE INDEX AUSER.ACNTT_BASE_PK 
ON AUSER.ALOT_MO_ACNT_BASE
(
	BSN_DIV_CD ASC
	, BANK_CD ASC
	, MO_ACNT_NO ASC
)
TABLESPACE AUSER
PCTFREE 10
INITRANS 2
MAXTRANS 255
STORAGE
(
	INITIAL 65536
	NEXT 1048576
	MINEXTENTS 1
	MAXEXTENTS UNLIMITED
	BUFFER_POOL DEFAULT
)
LOGGING
NOCOMPRESS
NOPARALLEL ;

--ALTER TABLE AUSER.ALOT_MO_ACNT_BASE ADD(PRIMARY KEY (BSN_DIV_CD, BANK_CD, MO_ACNT_NO));
ALTER TABLE AUSER.ALOT_MO_ACNT_BASE ADD(CONSTRAINT ALOT_MO_ACNT_BASE_PK PRIMARY KEY (BSN_DIV_CD, BANK_CD));

