SELECT                	  
	A.PAY_NO             ,-- ����_��ȣ
	A.RCGN_NO            ,-- �ĺ�_��ȣ
	A.LOAN_NO            ,-- ����_��ȣ
	A.LOAN_SEQ           ,-- ����_����
	A.PAY_BSN_DIV_CD     ,-- ����_����_�����ڵ�
	A.CUST_NM            ,-- ����
	A.MNG_DEPT_CD        ,-- ����_�μ�_�ڵ�
	A.SETL_BANK_CD       ,-- ����_����_�ڵ�
	A.SETL_ACNT_NO       ,-- ����_���¹�ȣ
	A.DEPO_OWN_NO        ,-- ������_��ȣ
	A.DEPO_OWNNM         ,-- �����ָ�
	A.IAMT_MANMN         ,-- �Ա�_�θ�
	A.LOAN_PAMT          ,-- ����_����
	A.TRT_FEE            ,-- ���_������
	A.CPRT_FEE           ,-- ����_������
	A.STMP_FEE           ,-- ������
	A.TRAN_RQST_AMT      ,-- ��ü_��û_�ݾ�
	A.TRAN_RQST_DTTM     ,-- ��ü_��û_�Ͻ�
	A.TRAN_PROC_DT       ,-- ��ü_ó��_����
	A.TR_BANK_CD         ,-- �ŷ�_����_�ڵ�
	A.OAMT_FEE           ,-- ���_������
	A.GRAM_SEND_DTTM     ,-- ����_����_�Ͻ�
	A.GRAM_CHSE_NO       ,-- ����_����_��ȣ
	A.TR_STRT_TM         ,-- �ŷ�_����_�ð�
	A.RSPN_CD            ,-- ����_�ڵ�
	A.PROC_STAT_CD       ,-- ó��_����_�ڵ�
	A.OAMT_AFAMT         ,-- ���_���ܾ�
	A.MNO_RQST_OBJ_FG    ,-- ���_��û_���_����
	A.TRAN_IPSS_GRAM_NO  ,-- ��ü_�Ҵ�_����_��ȣ
	A.VOCH_JNL_LAST_SEQ  ,-- ��ǥ_�а�_����_����
	A.LOAN_CNT           ,-- ����_�Ǽ�
	B.BANK_CD	      	 ,-- ����_�ڵ�
	B.MO_ACNT_NO	     ,-- ��_���¹�ȣ
   C.PL_PRDT_DIV         -- PL_��ǰ_����
FROM AUSER.ALOT_LOAN_ONLN_PAY A, -- ALOT_����_����_����      
     AUSER.ALOT_MO_ACNT_BASE  B, -- ALOT_�����_�⺻          
     AUSER.ALOT_LOAN_BASE     C  -- ALOT_����_�⺻             
WHERE A.PROC_STAT_CD    = '05' --'01'   	
  AND A.TRAN_PROC_DT    = :dt     	
  AND A.TRAN_RQST_DTTM <= :dttm     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

--TRAN_RQST_DTTM
--20111108095409
--20111108103642
--20111108101641
--20111108103956
--20111108103512
select *
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_����_����_����
where tran_proc_dt >= :dt --'2011-09-10'
  AND A.TRAN_RQST_DTTM <= :dttm     	
;

---------------------------------------------------------------------------------
---------------------------------------------------------------------------------
select TRAN_PROC_DT,TRAN_RQST_DTTM,LOAN_NO,LOAN_SEQ,PROC_STAT_CD,PAY_BSN_DIV_CD,PAY_NO
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_����_����_����
where tran_proc_dt >= :dt --'2011-09-10'
--  AND A.TRAN_RQST_DTTM <= :dttm     	
order by pay_no
;

select *
FROM AUSER.ALOT_MO_ACNT_BASE B -- ALOT_�����_�⺻          
order by BSN_DIV_CD
;

select *
FROM AUSER.ALOT_LOAN_BASE     C  -- ALOT_����_�⺻             
;
---------------------------------------------------------------------------------
---------------------------------------------------------------------------------
select *
FROM AUSER.ALOT_LOAN_ONLN_PAY A, -- ALOT_����_����_����      
--     AUSER.ALOT_MO_ACNT_BASE  B, -- ALOT_�����_�⺻          
     AUSER.ALOT_LOAN_BASE     C  -- ALOT_����_�⺻             
WHERE 
--      A.PROC_STAT_CD    = '01'   	
--  AND 
A.TRAN_PROC_DT    = :dt     	
--  AND A.TRAN_RQST_DTTM <= :dttm     	
--  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

SELECT                	  
	A.PAY_NO             ,-- ����_��ȣ
	A.RCGN_NO            ,-- �ĺ�_��ȣ
	A.LOAN_NO            ,-- ����_��ȣ
	A.LOAN_SEQ           ,-- ����_����
	A.PAY_BSN_DIV_CD     ,-- ����_����_�����ڵ�
	A.CUST_NM            ,-- ����
	A.MNG_DEPT_CD        ,-- ����_�μ�_�ڵ�
	A.SETL_BANK_CD       ,-- ����_����_�ڵ�
	A.SETL_ACNT_NO       ,-- ����_���¹�ȣ
	A.DEPO_OWN_NO        ,-- ������_��ȣ
	A.DEPO_OWNNM         ,-- �����ָ�
	A.IAMT_MANMN         ,-- �Ա�_�θ�
	A.LOAN_PAMT          ,-- ����_����
	A.TRT_FEE            ,-- ���_������
	A.CPRT_FEE           ,-- ����_������
	A.STMP_FEE           ,-- ������
	A.TRAN_RQST_AMT      ,-- ��ü_��û_�ݾ�
	A.TRAN_RQST_DTTM     ,-- ��ü_��û_�Ͻ�
	A.TRAN_PROC_DT       ,-- ��ü_ó��_����
	A.TR_BANK_CD         ,-- �ŷ�_����_�ڵ�
	A.OAMT_FEE           ,-- ���_������
	A.GRAM_SEND_DTTM     ,-- ����_����_�Ͻ�
	A.GRAM_CHSE_NO       ,-- ����_����_��ȣ
	A.TR_STRT_TM         ,-- �ŷ�_����_�ð�
	A.RSPN_CD            ,-- ����_�ڵ�
	A.PROC_STAT_CD       ,-- ó��_����_�ڵ�
	A.OAMT_AFAMT         ,-- ���_���ܾ�
	A.MNO_RQST_OBJ_FG    ,-- ���_��û_���_����
	A.TRAN_IPSS_GRAM_NO  ,-- ��ü_�Ҵ�_����_��ȣ
	A.VOCH_JNL_LAST_SEQ  ,-- ��ǥ_�а�_����_����
	A.LOAN_CNT           ,-- ����_�Ǽ�
	B.BANK_CD	      	 ,-- ����_�ڵ�
	B.MO_ACNT_NO	     ,-- ��_���¹�ȣ
   C.PL_PRDT_DIV         -- PL_��ǰ_����
FROM AUSER.ALOT_LOAN_ONLN_PAY A, -- ALOT_����_����_����      
     AUSER.ALOT_MO_ACNT_BASE  B, -- ALOT_�����_�⺻          
     AUSER.ALOT_LOAN_BASE     C  -- ALOT_����_�⺻             
WHERE
--    A.PROC_STAT_CD    = '01'   	
--  AND 
  A.TRAN_PROC_DT    = :dt     	
  AND A.TRAN_RQST_DTTM <= :dttm     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

select PAY_BSN_DIV_CD, count(*)
FROM AUSER.ALOT_LOAN_ONLN_PAY
group by PAY_BSN_DIV_CD
;
---------------------------------------------------------------------------------
---------------------------------------------------------------------------------
select count(*)
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_����_����_����      
;

select TRAN_PROC_DT,count(*)
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_����_����_����      
where tran_proc_dt > '2011-09-10'
group by TRAN_PROC_DT
;

select *
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_����_����_����      
where tran_proc_dt = '2011-11-07'
;


----------------------------------------------------------------------------------
-- �̻�밡�����
SELECT IMG_ACNT_NO              
FROM (                           
SELECT IMG_ACNT_NO              
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC 
WHERE IMG_ACNT_BANK_CD = ?         
AND USE_FG = '0'                   
AND DISU_DT IS NULL                
)                                  
WHERE ROWNUM = 1                   
;

SELECT *
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC 
WHERE USE_FG = '0'                   
;

SELECT IMG_ACNT_BANK_CD, USE_FG, count(*)
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
group by IMG_ACNT_BANK_CD, USE_FG
--WHERE USE_FG = '0'
;

select count(*)
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
;

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
--//�ŷ������ȸ
SELECT
	TR_ORG_CD
FROM BUSER.BVAT_TR_ORG_BASE
WHERE RCPT_TP_CD = ? AND BANK_CD = '0'||?
;

-- C1 - ���������ڵ� (�������)
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '088';

SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD
;
-- C2 - ���������ڵ� (������)
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C2' AND BANK_CD = '088';

SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD
;
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------

SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559'; -- ������ڵ�

SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NM LIKE '%����ó��%'; -- S00045 ����ó�����������ڵ�
SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NO LIKE 'S00045';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00045'; -- S00045 ����ó�����������ڵ�

SELECT PAY_BSN_DIV_CD, COUNT(*) FROM AUSER.ALOT_LOAN_ONLN_PAY
GROUP BY PAY_BSN_DIV_CD;
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- �ǽð� ���� ��� �Ǽ� ��ȸ
-- ����_����_����
SELECT * FROM AUSER.ALOT_LOAN_ONLN_PAY  WHERE PROC_STAT_CD = '05' AND TRAN_PROC_DT = '2011-11-08' AND TRAN_RQST_DTTM <= '20111108095409';
-- �����_�⺻
SELECT * FROM AUSER.ALOT_MO_ACNT_BASE   WHERE BSN_DIV_CD IN ('03', '10') ORDER BY BSN_DIV_CD; -- '03' ���ް��� '10' ȸ������ó������
-- ����_�⺻
SELECT * FROM AUSER.ALOT_LOAN_BASE      WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ����ǽð�_���޴�� - �����ڵ����
-- �������޳���
SELECT * FROM AUSER.ALOT_LOAN_PAY_DESC  WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ��ü��_��_����
SELECT * FROM AUSER.ALOT_DFEE_RT_DESC   WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ���������
SELECT * FROM AUSER.ALOT_LOAN_PIAMT     WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ����_����_��
SELECT * FROM AUSER.ALOT_LOAN_REL_MAN   WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ����ǳ�����
SELECT * FROM AUSER.ALOT_LOAN_RL_DUE_MAN WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ����_����_����_��
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC WHERE IMG_ACNT_BANK_CD = '026' AND IMG_ACNT_NO = '56201550755471';
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC WHERE (IMG_ACNT_BANK_CD, IMG_ACNT_NO) IN (SELECT IMG_ACNT_BANK_CD,IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC WHERE USE_FG = '0';
-- ����_����_����_�̷�
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_HIST WHERE IMG_ACNT_BANK_CD = '026' AND IMG_ACNT_NO = '56201550755471';
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_HIST WHERE (IMG_ACNT_BANK_CD, IMG_ACNT_NO) IN (SELECT IMG_ACNT_BANK_CD,IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- ä��_����_�⺻
SELECT * FROM CUSER.CART_BND_CLS_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ��ǰ_�⺻
SELECT * FROM AUSER.APDT_PRDT_BASE WHERE PRDT_CD = 'LOQ11';
SELECT * FROM AUSER.APDT_PRDT_BASE WHERE PRDT_CD IN (SELECT PRDT_CD FROM AUSER.ALOT_LOAN_PAY_DESC  WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- ����_��ǥ
SELECT * FROM AUSER.ASCT_INTN_VOCH WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- ���_����_����_����_����
SELECT * FROM AUSER.ALDT_CONT_DOC_ARRL_MNG_MST WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- �߼�_����_����
SELECT * FROM AUSER.ALDT_DLVR_MNG_INFO WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- ��_�ּ�
SELECT * FROM AUSER.ACTT_CUST_ADDR WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- ����_�ڵ�_����
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- ������ڵ�
-- ��_����ó
SELECT * FROM AUSER.ACTT_CUST_CNTC_PLC WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
SELECT * FROM AUSER.ACTT_CUST_CNTC_PLC WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01') AND CNTC_PLC_DIV_CD = '01';
-- ��_�⺻
SELECT * FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- 19 ��_���_�ȳ�
SELECT * FROM CUSER.CBCT_CUST_CNSL_GUID WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 20 �ɻ�_����_�⺻
SELECT * FROM AUSER.ACNT_EXAM_PRGS_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 21 ����_�ǳ�����
SELECT * FROM AUSER.ALOT_LOAN_RL_DUE_MAN WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 22 ä��_�⺻
SELECT * FROM GUSER.GBCT_GVNO_BASE ORDER BY CLAS_KIND_NO;
-- 23 �ŷ�_����_��
SELECT * FROM BUSER.BVAT_TR_BANK_DESC WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '088' ORDER BY RCPT_TP_CD, BANK_SNUM;
SELECT * FROM BUSER.BVAT_TR_BANK_DESC ORDER BY RCPT_TP_CD, BANK_SNUM;

-- - C1 - ������� / 88 �������� (23 / 26)
-- 24 �ŷ�_���_�⺻
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '088';
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '026';
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '020';
SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD;

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- ����_�ڵ�_���� - �ּ�
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- ������ڵ�
-- ����_�ڵ�_���� - û���Աݹ�ġ�μ� ��������
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'R00038';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- ������ڵ�
-- ����_�ڵ�_���� - �����ڵ�
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'R00036';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- ������ڵ�

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- SENDER SOCKET
SELECT C.CHNL_IP, C.CHNL_PORT, B.HDR_LTH FROM GUSER.GSCT_GRAM_CHNL C, GUSER.GSCT_GRAM_BASE B WHERE C.CHNL_CD = B.CHNL_CD AND B.GRAM_CD = ?;
SELECT C.CHNL_IP, C.CHNL_PORT, B.HDR_LTH FROM GUSER.GSCT_GRAM_CHNL C, GUSER.GSCT_GRAM_BASE B WHERE C.CHNL_CD = B.CHNL_CD AND B.GRAM_CD = 'KB0200';
SELECT * FROM GUSER.GSCT_GRAM_CHNL ORDER BY CHNL_CD;
SELECT * FROM GUSER.GSCT_GRAM_BASE ORDER BY GRAM_CD;
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S' ORDER BY SNUM ASC;
SELECT SNUM, SNR_DIV, HDR_FG, KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S' ORDER BY SNUM ASC;
SELECT * FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S';
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R' ORDER BY SNUM ASC;
SELECT SNUM, SNR_DIV, HDR_FG, KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R' ORDER BY SNUM ASC;
SELECT * FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R';


-- ���� �α� ��� ���̺�
-- ���޹�ġ - 'KB0200'
SELECT COUNT(*) FROM GUSER.GSCT_GRAM_LOG
;
SELECT GRAM_CD FROM GUSER.GSCT_GRAM_LOG
GROUP BY GRAM_CD
;
SELECT distinct GRAM_CD FROM GUSER.GSCT_GRAM_LOG
WHERE GRAM_CD IS NOT NULL
--GROUP BY GRAM_CD
;

SELECT * FROM GUSER.GSCT_GRAM_BASE ORDER BY GRAM_CD;

select * from guser.gsct_gram_log
;

select * from guser.gsct_gram_log
where EXEC_DT = '20111122'
order by EXEC_TM
;
--delete from guser.gsct_gram_log where exec_dt = '20111122';
--commit;

select * from guser.gsct_gram_log
where EXEC_DT = '20111124'
order by EXEC_TM
;

select * from guser.gsct_gram_log
where gram_cd = 'KB6013'
;

select * from guser.gsct_gram_log
where gram_cd = 'KB4100'
;

select GRAM_CD, MIN(EXEC_DT), MAX(EXEC_DT) from guser.gsct_gram_log
where gram_cd = 'KB4100'
GROUP BY GRAM_CD
;

select GRAM_CD, MIN(EXEC_DT), MAX(EXEC_DT) from guser.gsct_gram_log
--where gram_cd = 'KB4100'
GROUP BY GRAM_CD
;

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC where USE_FG = '0'
;

SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC where USE_FG = '0' and IMG_ACNT_BANK_CD = '020'
order by IMG_ACNT_BANK_CD,IMG_ACNT_NO
;

SELECT COUNT(*) FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
;

select * from auser.ALOT_LOAN_IMG_ACNT_DESC@hiplus_link
;

select COUNT(*) from auser.ALOT_LOAN_IMG_ACNT_DESC@hiplus_link
;

--INSERT INTO AUSER.ALOT_LOAN_IMG_ACNT_DESC
--select * from auser.ALOT_LOAN_IMG_ACNT_DESC@hiplus_link
--;

--COMMIT;
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------




-- DDL Script for TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC. Orange for ORACLE.
-- Generated on 2011/11/17 20:51:30 by ZUSER@CABISDEV

--CREATE TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC 
--(
--	IMG_ACNT_BANK_CD   CHAR (3) DEFAULT '' NOT NULL,
--	IMG_ACNT_NO        VARCHAR2 (20) DEFAULT '' NOT NULL,
--	USE_FG             CHAR (1) DEFAULT '0',
--	USE_CMPL_DT        CHAR (10) DEFAULT '',
--	DISU_DT            CHAR (10) DEFAULT '',
--	RMRK               VARCHAR2 (100) DEFAULT '',
--	FRST_REG_DT        CHAR (10) DEFAULT '',
--	FRST_REG_TM        CHAR (6) DEFAULT '',
--	FRST_REG_EMP_NO    VARCHAR2 (8) DEFAULT '',
--	LAST_PROC_DT       CHAR (10) DEFAULT '',
--	LAST_PROC_TM       CHAR (6) DEFAULT '',
--	LAST_PROC_EMP_NO   VARCHAR2 (8) DEFAULT ''
--)
--TABLESPACE AUSER
--PCTFREE 10
--INITRANS 1
--MAXTRANS 255
--STORAGE
--(
--	INITIAL 131072
--	NEXT 1048576
--	MINEXTENTS 1
--	MAXEXTENTS UNLIMITED
--	BUFFER_POOL DEFAULT
--)
--LOGGING
--DISABLE ROW MOVEMENT ;
--
--GRANT DELETE ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--GRANT INSERT ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--GRANT SELECT ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--GRANT UPDATE ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--
--COMMENT ON TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC IS 'ALOT_����_����_����_����' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.DISU_DT IS '���_����' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.FRST_REG_DT IS '����_���_����' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.FRST_REG_EMP_NO IS '����_���_�����ȣ' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.FRST_REG_TM IS '����_���_�ð�' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.IMG_ACNT_BANK_CD IS '����_����_����_�ڵ�' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.IMG_ACNT_NO IS '����_���¹�ȣ' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.LAST_PROC_DT IS '����_ó��_����' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.LAST_PROC_EMP_NO IS '����_ó��_�����ȣ' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.LAST_PROC_TM IS '����_ó��_�ð�' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.RMRK IS '���' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.USE_CMPL_DT IS '���_�Ϸ�_����' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.USE_FG IS '���_����' ;
--
--CREATE INDEX AUSER.ACNTLOANIMG_A02 
--ON AUSER.ALOT_LOAN_IMG_ACNT_DESC
--(
--	IMG_ACNT_BANK_CD ASC,
--	USE_FG ASC
--)
--TABLESPACE AUSER
--PCTFREE 10
--INITRANS 2
--MAXTRANS 255
--STORAGE
--(
--	INITIAL 65536
--	NEXT 1048576
--	MINEXTENTS 1
--	MAXEXTENTS UNLIMITED
--	BUFFER_POOL DEFAULT
--)
--LOGGING
--NOCOMPRESS
--NOPARALLEL ;
--
--ALTER TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC ADD(
--	PRIMARY KEY (IMG_ACNT_BANK_CD, IMG_ACNT_NO));
--;


---
-- ����(��)���� - 021
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='021'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '021' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- �츮(��)��� 022
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='022'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '022' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- �츮���࣭���������� 024
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='024'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '024' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- ���ս������� 088
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='088'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '088' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- (��)���� 021
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='021'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

-- �츮���� 020
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='020'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '020' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- (��)���� 026
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='026'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '026' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;


-- ���� ������
SELECT * FROM BUSER.BVAT_TR_BANK_DESC ORDER BY RCPT_TP_CD, BANK_SNUM;
select * FROM BUSER.BVAT_TR_BANK_DESC               order by RCPT_TP_CD,BANK_CD,BANK_SNUM;
select * FROM BUSER.BVAT_TR_BANK_DESC@hiplus_link   order by RCPT_TP_CD,BANK_CD,BANK_SNUM;
SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD
;

SELECT TR_ORG_CD,TR_ORG_NM,ACCT_UNIT_CD,RCPT_TP_CD,CYCL_CD,BANK_CD,MO_ACNT_NO FROM BUSER.BVAT_TR_ORG_BASE
minus
SELECT TR_ORG_CD,TR_ORG_NM,ACCT_UNIT_CD,RCPT_TP_CD,CYCL_CD,BANK_CD,MO_ACNT_NO FROM BUSER.BVAT_TR_ORG_BASE@HIPLUS_LINK
;

SELECT
    l.TR_ORG_CD, l.TR_ORG_NM, l.ACCT_UNIT_CD, l.RCPT_TP_CD, l.CYCL_CD, l.BANK_CD, l.MO_ACNT_NO, r.MO_ACNT_NO
FROM
    BUSER.BVAT_TR_ORG_BASE l, BUSER.BVAT_TR_ORG_BASE@hiplus_link r
where
        l.TR_ORG_CD    = r.TR_ORG_CD
    and l.TR_ORG_NM    = r.TR_ORG_NM
    and l.ACCT_UNIT_CD = r.ACCT_UNIT_CD
    and l.RCPT_TP_CD   = r.RCPT_TP_CD
    and l.CYCL_CD      = r.CYCL_CD
    and l.BANK_CD      = r.BANK_CD
    and l.MO_ACNT_NO   = r.MO_ACNT_NO
;

select l.RCPT_TP_CD, l.BANK_CD, l.BANK_SNUM
from BUSER.BVAT_TR_BANK_DESC l, BUSER.BVAT_TR_BANK_DESC@hiplus_link r
where
        l.RCPT_TP_CD = r.RCPT_TP_CD
    and l.BANK_CD    = r.BANK_CD
    and l.BANK_SNUM  = r.BANK_SNUM
;

SELECT * FROM BUSER.BVAT_TR_BANK_DESC order by rcpt_tp_cd, bank_cd, bank_snum;
SELECT * FROM BUSER.BVAT_TR_ORG_BASE order by TR_ORG_CD, bank_cd, MO_ACNT_NO;

SELECT RCPT_TP_CD,BANK_CD,BANK_SNUM FROM BUSER.BVAT_TR_BANK_DESC
minus
select RCPT_TP_CD,BANK_CD,BANK_SNUM FROM BUSER.BVAT_TR_BANK_DESC@hiplus_link;

select RCPT_TP_CD,  GUSER.GF_COMMCD_NM_01( 'R00012', RCPT_TP_CD) RCPT_TP_NM
FROM BUSER.BVAT_TR_BANK_DESC
;

--GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM
--;

SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NO = 'R00012'; -- R00012 ���������ڵ�
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'R00012';

--���񽺰��� : ����Ÿ�����޼��� (1005101927669)
--             ����������ݼ��� (1005301927675)




