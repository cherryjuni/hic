-- VochProcQuery.java
-- 102 line
-- selectLoanIsrtEx()
SELECT 						
   A.LOAN_PAMT,				 -- �������
   A.STMP_FEE,					 -- ������
   A.PAY_AMT,					 -- ���ޱݾ�
   0 TRT_FEE,	 -- ��޼�����=����޼�����+���޻���޼����� --/ 2009.12.09 k.h.y ���� �� */
   0 CPRT_FEE,					 -- ���޼�����
   A.SET_FEE,					 -- ������
   A.NOTA_FEE,					 -- ������
   A.ETC_FEE,					 -- ��Ÿ������
   A.LOAN_NO, 					 -- �����ȣ
   A.LOAN_SEQ,	   				 -- �������
   '0' RT_FEE_DFER_PROC_FG,		 -- ��޼������̿�ó������ --/ 2009.12.09 k.h.y ���� �� */
   '0' CPRT_FEE_DFER_PROC_FG,	 -- ���޼������̿�ó������
   C.BANK_CD,	        	 -- �����ڵ�
   C.MO_ACNT_NO,		 -- ���¹�ȣ
   '0' STMP_FEE_AF_ACQT_FG,  --���������뿩�� --/ 2009.12.09 k.h.y ���� �� */
   A.PRXP_PAMT,		 -- ���¹�ȣ
   A.PRXP_INT		 -- ���¹�ȣ
FROM			
   AUSER.ALOT_LOAN_PAY_DESC A,   -- �������޳���
   AUSER.APDT_PRDT_BASE     B,   -- ��ǰ�⺻
   AUSER.ALOT_MO_ACNT_BASE  C    -- ����±⺻
WHERE A.LOAN_NO  = '11121600004'--?	     
  AND A.LOAN_SEQ = '01'--?	     
  AND A.SNUM     = '01'--?	     
  AND A.PRDT_CD  = B.PRDT_CD    
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD 
  AND DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD 
;

-- 157 line
-- selectMoggLoan()
SELECT 						
   A.LOAN_PAMT,				 -- �������
   A.STMP_FEE,					 -- ������
   A.PAY_AMT,					 -- ���ޱݾ�
  (A.CUST_TRT_FEE+A.CPRT_COM_TRT_FEE) TRT_FEE,	 -- ��޼�����=����޼�����+���޻���޼�����
   A.CPRT_FEE,					 -- ���޼�����
   A.SET_FEE,					 -- ������
   A.NOTA_FEE,					 -- ������
   A.ETC_FEE,					 -- ��Ÿ������
   A.LOAN_NO, 					 -- �����ȣ
   A.LOAN_SEQ,	   				 -- �������
   '0' TRT_FEE_DFER_PROC_FG,		 -- ��޼������̿�ó������
   '0' CPRT_FEE_DFER_PROC_FG,	 -- ���޼������̿�ó������
   C.BANK_CD,		 -- �����ڵ�
   C.MO_ACNT_NO,		 -- ���¹�ȣ
   A.RIGT_INSU_FEE,		 -- �ǿ������
   A.JUDG_FEE,		 -- ������
   A.SET_FEE_BRDN_DIV_CD,	 -- ������δ㱸���ڵ�
   '0' STMP_FEE_AF_ACQT_FG   --���������뿩�� --/ 2009.12.09 ���� ��
FROM			
   AUSER.ALOT_LOAN_PAY_DESC A,   -- �������޳���
   AUSER.APDT_PRDT_BASE     B,   -- ��ǰ�⺻
   AUSER.ALOT_MO_ACNT_BASE  C    -- ����±⺻
WHERE A.LOAN_NO  = '11121600004'--?	     
  AND A.LOAN_SEQ = '01'--?	     
  AND A.SNUM     = '01'--?	     
  AND A.PRDT_CD  = B.PRDT_CD    
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD 
  AND DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD 
;

-- 189
-- selectPrFeDesc()
-- '01' �ſ���� - ������
-- AUSER.ALOT_PR_FEE_PAY_DESC -> ���̺� ����
SELECT 					
   A.LOAN_NO,				 -- �����ȣ
   A.LOAN_SEQ,				 -- �������
   A.PR_FEE,				 -- ���˼�����
   A.ITAX,					 -- �ҵ漼
   A.HTAX,					 -- �ֹμ�
   C.BANK_CD,		 -- �����ڵ�
   C.MO_ACNT_NO,		 -- ���¹�ȣ
   A.PR_EMP_RESI_NO		 -- ���˻���ֹι�ȣ
FROM					
   AUSER.ALOT_PR_FEE_PAY_DESC A, 	 -- ���˼��������޳���
   AUSER.ALOT_MO_ACNT_BASE C    -- ����±⺻
WHERE A.LOAN_NO  = '11121600004'--?	     
  AND A.LOAN_SEQ = '01'--?	     
  AND A.SNUM     = '01'--?	     
  AND C.BSN_DIV_CD = '01'                     
;

select * from AUSER.ALOT_PR_FEE_PAY_DESC
;

-- 505
-- selectLoanPayPrdtBase2()
-- AUSER.ALOT_DOC_KEEP_SPEC - ���̺� ����
SELECT 					
   A.LOAN_PAMT,			 -- �������
  (A.CUST_TRT_FEE+A.CPRT_COM_TRT_FEE) TRT_FEE, -- ��޼�����(��+���޻�)
   A.CPRT_FEE,			 -- ���޼�����
   A.STMP_FEE,			 -- ������
   A.SET_FEE,				 -- ������
   A.NOTA_FEE,			 -- ������
   A.ETC_FEE,				 -- ��Ÿ������
   A.PAY_AMT,				 -- ���ޱݾ�
   A.RQST_DIV_CD,			 -- ��û�����ڵ�
   A.LOAN_NO,				 -- �����ȣ
   A.LOAN_SEQ,			 -- �������
   A.SNUM,				 -- �Ϸù�ȣ
   C.BANK_CD,		 -- �����ڵ�
   C.MO_ACNT_NO,		 -- ���¹�ȣ
   (SELECT STMP_FEE_AF_ACQT_FG FROM AUSER.ALOT_DOC_KEEP_SPEC 
   WHERE LOAN_NO=A.LOAN_NO AND LOAN_SEQ=A.LOAN_SEQ AND CNCL_FG <> '1'ORDER BY SNUM FETCH FIRST ROW ONLY)  --���������뿩��
FROM					
   AUSER.ALOT_LOAN_PAY_DESC  A,	 -- �������޳���
   AUSER.ALOT_MO_ACNT_BASE   C    -- ����±⺻
WHERE A.CNCL_FG = '0'			 -- ��ҿ���
  AND A.LOAN_NO  = '11121600004'--?	     
  AND A.LOAN_SEQ = '01'--?	     
  AND A.SNUM     = '01'--?	     
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD 
  AND DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD 
;

select * from AUSER.ALOT_DOC_KEEP_SPEC
;

-- 547
-- selectLoanPayPrdtBase3()
-- AUSER.ALOT_DOC_KEEP_SPEC -> ���̺� ����
SELECT 					
   A.LOAN_PAMT,			 -- �������
  (A.CUST_TRT_FEE+A.CPRT_COM_TRT_FEE) TRT_FEE, -- ��޼�����(��+���޻�)
   A.CPRT_FEE,			 -- ���޼�����
   A.STMP_FEE,			 -- ������
   A.SET_FEE,				 -- ������
   A.NOTA_FEE,			 -- ������
   A.ETC_FEE,				 -- ��Ÿ������
   A.PAY_AMT,				 -- ���ޱݾ�
   A.RQST_DIV_CD,			 -- ��û�����ڵ�
   A.LOAN_NO,				 -- �����ȣ
   A.LOAN_SEQ,			 -- �������
   A.SNUM,				 -- �Ϸù�ȣ
   C.BANK_CD,		 -- �����ڵ�
   C.MO_ACNT_NO,	 -- ���¹�ȣ
   (SELECT STMP_FEE_AF_ACQT_FG FROM AUSER.ALOT_DOC_KEEP_SPEC 
   WHERE LOAN_NO=A.LOAN_NO AND LOAN_SEQ=A.LOAN_SEQ 
   AND CNCL_FG <> '1' ORDER BY SNUM FETCH FIRST ROW ONLY)  --���������뿩��
FROM			
   AUSER.ALOT_LOAN_PAY_DESC  A,   -- �������޳���
   AUSER.ALOT_MO_ACNT_BASE   C    -- ����±⺻
WHERE A.CNCL_FG = '0'		 -- ��ҿ���
  AND A.LOAN_NO  = '11121600004'--?	     
  AND A.LOAN_SEQ = '01'--?	     
  AND A.SNUM     = '01'--?	     
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD 
  AND DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD 
;

-- 730
-- selectPrFeePrePay()
SELECT A.EMP_RESI_NO			
   ,A.PAY_AMT                          
   ,C.BANK_CD	                         -- �����ڵ�
   ,C.MO_ACNT_NO		         -- ���¹�ȣ
FROM AUSER.ALOT_PR_FEE_PRE_PAY_DESC A  
    ,AUSER.ALOT_MO_ACNT_BASE C         
WHERE A.EMP_RESI_NO = ?                
AND A.SNUM = ?                         
AND C.BSN_DIV_CD  = '13'   		
;

select * from AUSER.ALOT_PR_FEE_PRE_PAY_DESC
;


