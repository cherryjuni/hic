
select * from GUSER.GBCT_COMM_CD_DESC;


select *
from GUSER.GBCT_COMM_CD_KIND
where cd_kind_nm like '%���޾���%' 
;

-- ���޹�ġ����ϴ��ڵ� 'S00045'
-- 03, 06, 10 �� �����
select *
from GUSER.GBCT_COMM_CD_KIND
where cd_kind_no = 'S00045'
;
select * from GUSER.GBCT_COMM_CD_DESC
where cd_kind_no = 'S00045'
;



select *
from GUSER.GBCT_BSN_DIVS_MAN
;

select *
from GUSER.GSCT_GRAM_BASE
;

select *
from GUSER.GSCT_GRAM_CHNL
order by chnl_cd
;

-- ���� - ���������׽�Ʈ ���� ���
select *
from GUSER.GSCT_GRAM_DESC
where GRAM_CD = 'GRAM01'
order by gram_cd, snr_div, snum
;

-- ���� - ���������׽�Ʈ ���� ���
select *
from GUSER.GSCT_GRAM_DESC
where GRAM_CD = 'KB0200'
order by gram_cd, snr_div, snum
;

select gram_cd
from GUSER.GSCT_GRAM_DESC
group by gram_cd
;






--- 1. getLoanPayObjListQuery
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
WHERE A.PROC_STAT_CD    = '01'   	 /*ó�������ڵ�(��ó��) 				  */
  AND A.TRAN_PROC_DT    = '2011-10-17'
        /*��üó������(��������:yyyy-MM-dd)     */
  AND A.TRAN_RQST_DTTM <= '20111017122926'
        /*��ü��û�Ͻ�(�����Ͻ�:yyyyMMddhhmmss) */
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD
        /*���޾��������ڵ� = ���������ڵ�  		 */
  AND A.LOAN_NO = C.LOAN_NO(+)
  AND A.LOAN_SEQ = C.LOAN_SEQ(+)
  ORDER BY A.PAY_NO
;

select *
from AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_����_����_����
WHERE A.PROC_STAT_CD    = '01'   	 /*ó�������ڵ�(��ó��) 				  */
  AND A.TRAN_PROC_DT    = '2011-10-17'
        /*��üó������(��������:yyyy-MM-dd)     */
  AND A.TRAN_RQST_DTTM <= '20111017122926'
        /*��ü��û�Ͻ�(�����Ͻ�:yyyyMMddhhmmss) */
--  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD
        /*���޾��������ڵ� = ���������ڵ�  		 */
--  AND A.LOAN_NO = C.LOAN_NO(+)
--  AND A.LOAN_SEQ = C.LOAN_SEQ(+)
  ORDER BY A.PAY_NO
;

select *
from AUSER.ALOT_MO_ACNT_BASE  B  -- ALOT_�����_�⺻
;

select *
from AUSER.ALOT_MO_ACNT_BASE  B  -- ALOT_�����_�⺻
where BSN_DIV_CD in ('03', '04', '05', '06', '07', '09')
;


select *
from AUSER.ALOT_LOAN_BASE     C  -- ALOT_����_�⺻
;

SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD'),TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL;

SELECT TO_CHAR(SYSDATE, 'YYYYMMDD'),TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL;


----------------------------------------------
-- GramExecutor.java / SendObj.java / ReceiveObj.java
---

-- SendObj.java gram_cd = 'KB0200'
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG
FROM GUSER.GSCT_GRAM_DESC
WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S'
ORDER BY SNUM ASC
;

-- ReceiveObj.java gram_cd = 'KB0200'
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG
FROM GUSER.GSCT_GRAM_DESC
WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R'
ORDER BY SNUM ASC
;

--SELECT * --KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG
--FROM GUSER.GBCT_COMM_CD_KIND
--WHERE CD_KIND_NO = 'KB0200' --AND SNR_DIV = 'S'
--;

select *
from GUSER.GSCT_GRAM_CHNL
order by chnl_cd
;

