-- PayCnfmProc.java
-- 741 line
-- getLoanPayObjListQuery()
SELECT
	 A.PAY_NO             -- ����_��ȣ
	,A.RCGN_NO            -- �ĺ�_��ȣ
	,A.LOAN_NO            -- ����_��ȣ
	,A.LOAN_SEQ           -- ����_����
	,A.PAY_BSN_DIV_CD     -- ����_����_�����ڵ�
	,A.CUST_NM            -- ����
	,A.MNG_DEPT_CD        -- ����_�μ�_�ڵ�
	,A.SETL_BANK_CD       -- ����_����_�ڵ�
	,A.SETL_ACNT_NO       -- ����_���¹�ȣ
	,A.DEPO_OWN_NO        -- ������_��ȣ
	,A.DEPO_OWNNM         -- �����ָ�
	,A.IAMT_MANMN         -- �Ա�_�θ�
	,A.LOAN_PAMT          -- ����_����
	,A.TRT_FEE            -- ���_������
	,A.CPRT_FEE           -- ����_������
	,A.STMP_FEE           -- ������
	,A.TRAN_RQST_AMT      -- ��ü_��û_�ݾ�
	,A.TRAN_RQST_DTTM     -- ��ü_��û_�Ͻ�
	,A.TRAN_PROC_DT       -- ��ü_ó��_����
	,A.TR_BANK_CD         -- �ŷ�_����_�ڵ�
	,A.OAMT_FEE           -- ���_������
	,A.GRAM_SEND_DTTM     -- ����_����_�Ͻ�
	,A.GRAM_CHSE_NO       -- ����_����_��ȣ
	,A.TR_STRT_TM         -- �ŷ�_����_�ð�
	,A.RSPN_CD            -- ����_�ڵ�
	,A.PROC_STAT_CD       -- ó��_����_�ڵ�
	,A.OAMT_AFAMT         -- ���_���ܾ�
	,A.MNO_RQST_OBJ_FG    -- ���_��û_���_����
	,A.TRAN_IPSS_GRAM_NO  -- ��ü_�Ҵ�_����_��ȣ
	,A.VOCH_JNL_LAST_SEQ  -- ��ǥ_�а�_����_����
	,A.LOAN_CNT           -- ����_�Ǽ�
	,B.BANK_CD	     	  -- ����� ����_�ڵ�
	,B.MO_ACNT_NO	      -- ����� ����_��ȣ
   ,C.PL_PRDT_DIV        -- PL��ǰ_����
FROM AUSER.ALOT_LOAN_ONLN_PAY A,
     AUSER.ALOT_MO_ACNT_BASE  B,
     AUSER.ALOT_LOAN_BASE     C
WHERE A.TRAN_PROC_DT      = '2011-12-18' --?
  AND A.PROC_STAT_CD    = '03' -- ó�������ڵ�(������)
--  AND A.PROC_STAT_CD    = '01' -- ó�������ڵ�(��ó��)
--  AND A.PROC_STAT_CD    = '05' -- ó�������ڵ�(����)
  AND A.PAY_BSN_DIV_CD    = B.BSN_DIV_CD                  /*���޾��������ڵ� = ���������ڵ� */
  AND DECODE(A.SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = B.BANK_CD
  AND A.LOAN_NO           = C.LOAN_NO(+)
  AND A.LOAN_SEQ          = C.LOAN_SEQ(+)
;
