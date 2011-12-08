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
    A.PROC_STAT_CD    = '01' --'04' --'01'   	
  AND
  A.TRAN_PROC_DT    = '2011-11-28'     	
  AND A.TRAN_RQST_DTTM <= '20111201191510'     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
--  AND DECODE(A.TR_BANK_CD, '088', '026', A.TR_BANK_CD)  = B.BANK_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

SELECT A.TR_BANK_CD, DECODE(nvl(A.TR_BANK_CD,'020'), '088', '026', nvl(A.TR_BANK_CD,'020')) ab
FROM AUSER.ALOT_LOAN_ONLN_PAY A
where
  A.TRAN_PROC_DT    = '2011-11-28'     	
  AND A.TRAN_RQST_DTTM <= '20111201191510'     	
;
--
-- SLNPM0301Dao.updatePayObj(String statusCd, String payNo, Connection con)
--      SLNPM0301Query.getLoanPayUpdateQuery()
--����ǽð� ���޴��) �����ڵ� ����
--UPDATE AUSER.ALOT_LOAN_ONLN_PAY
     SET PROC_STAT_CD = ?  ,
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE PAY_NO       = ?
;
--arg1=02
--arg2=11112800000002

-- SLNPM0301Dao.updateLoanPayDesc(String statusCd, String payNo, Connection con)
--      SLNPM0301Query.getLoanPayDescUpdateSuccQuery()
--      or
--      SLNPM0301Query.getLoanPayDescUpdateFailQuery()
--����ǽð� ���޴��) �����ڵ� ����

--UPDATE AUSER.ALOT_LOAN_PAY_DESC
     SET PAY_STAT_CD  = ?,        				 /* �����ڵ�   */
         PAY_DT       = TO_CHAR(SYSDATE,'YYYY-MM-DD'), 	 /* ��������   */
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE PAY_NO       = ?        				 /* ���޹�ȣ   */
;
--arg1=5 - ���޿Ϸ�
--arg2=11112800000002

-- SLNPM0301Dao.getLoanPayObj(String payNo,Connection con)
--    SLNPM0301Query.getLoanPayObjQuery()
SELECT
	A.PAY_BSN_DIV_CD,
	'' AS CPRT_COM_NO,
	A.CNCL_FG,
	A.RQST_DIV_CD,
	A.SNUM,
	B.LOAN_NO,
	B.LOAN_SEQ,
	B.PRDT_LRGE_CLAS_CD,
	B.PRDT_MID_CLAS_CD,
	B.LOAN_INT_RT,
	B.LOAN_DT,
	B.PRDT_CD,
	B.LOAN_PAMT,
	B.LOAN_TERM,
	B.SETL_DD,
	B.FCNT_DUE_DT,
	B.FCNT_MOPA_CALC_FG,
	B.CUST_TRT_FEE,
	B.CPRT_COM_TRT_FEE,
	B.IMG_ACNT_BANK_CD,
	B.MNG_DEPT_CD,
	B.CPRT_FEE,
   0 AS  PR_FEE,
	B.DUE_METH_CD,
   B.DFEE_RT,
   B.DLY_INT_RT
FROM AUSER.ALOT_LOAN_PAY_DESC A,
     AUSER.ALOT_LOAN_BASE     B
WHERE A.PAY_NO    = '11112800000002' --?
  AND A.LOAN_NO   = B.LOAN_NO
  AND A.LOAN_SEQ  = B.LOAN_SEQ
  AND A.CNCL_FG  != '1'
;
--2011-12-02 14:55:12,  INFO [main](SLNPM0301Dao.java:109) : arg1=11112800000002
;

-- 11112800002	01
select * from AUSER.ALOT_LOAN_PAY_DESC
where pay_no = '11112800000002'
;

-- ������»���
-- �� ������� ���� Ȯ��
SELECT IMG_ACNT_NO FROM
(SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE
WHERE CONT_MAN_NO =(SELECT L.CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE L
WHERE L.LOAN_NO =?
AND L.LOAN_SEQ=?)
AND LOAN_STAT_CD ='22'
AND PRDT_MID_CLAS_CD ='22'
AND IMG_ACNT_NO IS NOT NULL OR IMG_ACNT_NO <> '')
WHERE ROWNUM = 1
;
-- 56201550661748
SELECT IMG_ACNT_NO FROM
(SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE
WHERE CONT_MAN_NO =(SELECT L.CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE L
WHERE L.LOAN_NO ='11112800002'
AND L.LOAN_SEQ='01')
AND LOAN_STAT_CD ='22'
AND PRDT_MID_CLAS_CD ='22'
AND IMG_ACNT_NO IS NOT NULL OR IMG_ACNT_NO <> '')
WHERE ROWNUM = 1
;

--�̻�밡����� ��ȸ
-- 56201550667571
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
--arg1= 0266409069848191

--UPDATE AUSER.ALOT_LOAN_IMG_ACNT_DESC
SET USE_FG = '1'
    ,LAST_PROC_DT = ?
    ,LAST_PROC_TM = ?
    ,LAST_PROC_EMP_NO = ?
WHERE IMG_ACNT_BANK_CD = ?
AND IMG_ACNT_NO = ?
;

--arg3= userEmpNo
--arg4= 026
--arg5= 56201550667571
select *
from AUSER.ALOT_LOAN_IMG_ACNT_DESC
where 
    IMG_ACNT_BANK_CD = '026' -- ���� '026' �츮 '020'
AND IMG_ACNT_NO = '56201550667571'
;

SELECT IMG_ACNT_NO
FROM (
 SELECT IMG_ACNT_NO
 FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
 WHERE IMG_ACNT_BANK_CD = '026'
  AND USE_FG = '0'
  AND DISU_DT IS NULL
)
WHERE ROWNUM = 1
;

-- ������� �̷� �߰�
--INSERT INTO AUSER.ALOT_LOAN_IMG_ACNT_HIST
(IMG_ACNT_BANK_CD
,IMG_ACNT_NO
,CHNG_SNUM
,LOAN_NO
,LOAN_SEQ
,USE_FG
,USE_CMPL_DT
,DISU_DT
,RMRK
,ISRT_DEPT_CD
,FRST_REG_DT
,FRST_REG_TM
,FRST_REG_EMP_NO
,LAST_PROC_DT
,LAST_PROC_TM
,LAST_PROC_EMP_NO)
VALUES
 (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
;
--arg1= 026
--arg1= 56201550667571

select * from AUSER.ALOT_LOAN_IMG_ACNT_HIST
where IMG_ACNT_NO = '56201550667571'
    and IMG_ACNT_BANK_CD = '026'
;


-- ����⺻����
--UPDATE AUSER.ALOT_LOAN_BASE
SET	  IMG_ACNT_BANK_CD = ?
	 ,IMG_ACNT_NO = ?
	 ,LAST_PROC_DT = ?
	 ,LAST_PROC_TM = ?
	 ,LAST_PROC_EMP_NO = ?
WHERE  LOAN_NO = ?
AND    LOAN_SEQ = ?
;

--arg1= 026
--arg2= 56201550667571
--arg6= 11112800002
--arg7= 01

select * from AUSER.ALOT_LOAN_BASE
where
    loan_no = '11112800002'
    and loan_seq = '01'
;




------------------
select
	SUM(PAY_AMT) PAY_AMT_SUM
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11112800000002' --?
AND   PAY_STAT_CD     = '1'
;
--2011-12-02 14:12:10,  INFO [main](SLNPM0301Dao.java:157) : arg1=11112800000002


--��ü���� ���� Table���� ���� �����͸� ����
select * from auser.alot_dfee_rt_desc
--delete from auser.alot_dfee_rt_desc
 where loan_no = '11112800002' --?
   and loan_seq = '01' --?
;
--2011-12-02 15:13:47, DEBUG [main](DfeeRtCrte.java:345) : arg1= 11112800002
--2011-12-02 15:13:48, DEBUG [main](DfeeRtCrte.java:346) : arg2= 01

-- ����⺻��������
-- ����⺻ ����
--UPDATE AUSER.ALOT_LOAN_BASE
     SET PAY_DT        = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_ACCT_DT  = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         INT_STRT_DT   = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_DT       = ?   ,
         LOAN_STAT_CD  = '22',
         DFEE_RT       = ?   ,
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE LOAN_NO       = ?
     AND LOAN_SEQ      = ?
;
--2011-12-02 15:18:15,  INFO [main](SLNPM0301Dao.java:550) : arg1= 2011-12-02
--2011-12-02 15:18:15,  INFO [main](SLNPM0301Dao.java:551) : arg2= 0.0
--2011-12-02 15:18:16,  INFO [main](SLNPM0301Dao.java:552) : arg3= 11112800002
--2011-12-02 15:18:16,  INFO [main](SLNPM0301Dao.java:553) : arg4= 01
--UPDATE AUSER.ALOT_LOAN_BASE
     SET PAY_DT        = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_ACCT_DT  = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         INT_STRT_DT   = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_DT       = '2011-12-02'   ,
         LOAN_STAT_CD  = '22',
         DFEE_RT       = '0.0'   ,
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE LOAN_NO       = '11112800002'
     AND LOAN_SEQ      = '01'
;








SELECT
	SUM(PAY_AMT) PAY_AMT_SUM
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11112800000002' --?
AND   PAY_STAT_CD     = '1'
;

SELECT
	*
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11112800000002' --?
;



