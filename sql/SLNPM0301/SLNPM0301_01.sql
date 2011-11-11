SELECT                	  
	A.PAY_NO             ,-- ����_��ȣ
	A.RCGN_NO            ,-- �ĺ�_��ȣ
	A.LOAN_NO            ,-- ����_��ȣ
	A.LOAN_SEQ           ,-- ����_����
	A.PAY_BSN_DIV_CD     ,-- ����_����_�����ڵ�
	A.CUST_NM            ,-- ������
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
	A.CUST_NM            ,-- ������
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
-- ����_�ּ�
SELECT * FROM AUSER.ACTT_CUST_ADDR WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- ����_�ڵ�_����
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- ������ڵ�
-- ����_����ó
SELECT * FROM AUSER.ACTT_CUST_CNTC_PLC WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
SELECT * FROM AUSER.ACTT_CUST_CNTC_PLC WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01') AND CNTC_PLC_DIV_CD = '01';
-- ����_�⺻
SELECT * FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- ����_���_�ȳ�
SELECT * FROM CUSER.CBCT_CUST_CNSL_GUID WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- �ɻ�_����_�⺻
SELECT * FROM AUSER.ACNT_EXAM_PRGS_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';


----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- SENDER SOCKET
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
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------