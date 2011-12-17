-- VochProcQuery.java
-- 102 line
-- selectLoanIsrtEx()
SELECT 						
   A.LOAN_PAMT,				 -- 대출원금
   A.STMP_FEE,					 -- 인지대
   A.PAY_AMT,					 -- 지급금액
   0 TRT_FEE,	 -- 취급수수료=고객취급수수료+제휴사취급수수료 --/ 2009.12.09 k.h.y 수정 후 */
   0 CPRT_FEE,					 -- 제휴수수료
   A.SET_FEE,					 -- 설정료
   A.NOTA_FEE,					 -- 공증료
   A.ETC_FEE,					 -- 기타수수료
   A.LOAN_NO, 					 -- 대출번호
   A.LOAN_SEQ,	   				 -- 대출순번
   '0' RT_FEE_DFER_PROC_FG,		 -- 취급수수료이연처리여부 --/ 2009.12.09 k.h.y 수정 후 */
   '0' CPRT_FEE_DFER_PROC_FG,	 -- 제휴수수료이연처리여부
   C.BANK_CD,	        	 -- 은행코드
   C.MO_ACNT_NO,		 -- 계좌번호
   '0' STMP_FEE_AF_ACQT_FG,  --인지대후취여부 --/ 2009.12.09 k.h.y 수정 후 */
   A.PRXP_PAMT,		 -- 계좌번호
   A.PRXP_INT		 -- 계좌번호
FROM			
   AUSER.ALOT_LOAN_PAY_DESC A,   -- 대출지급내역
   AUSER.APDT_PRDT_BASE     B,   -- 상품기본
   AUSER.ALOT_MO_ACNT_BASE  C    -- 모계좌기본
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
   A.LOAN_PAMT,				 -- 대출원금
   A.STMP_FEE,					 -- 인지대
   A.PAY_AMT,					 -- 지급금액
  (A.CUST_TRT_FEE+A.CPRT_COM_TRT_FEE) TRT_FEE,	 -- 취급수수료=고객취급수수료+제휴사취급수수료
   A.CPRT_FEE,					 -- 제휴수수료
   A.SET_FEE,					 -- 설정료
   A.NOTA_FEE,					 -- 공증료
   A.ETC_FEE,					 -- 기타수수료
   A.LOAN_NO, 					 -- 대출번호
   A.LOAN_SEQ,	   				 -- 대출순번
   '0' TRT_FEE_DFER_PROC_FG,		 -- 취급수수료이연처리여부
   '0' CPRT_FEE_DFER_PROC_FG,	 -- 제휴수수료이연처리여부
   C.BANK_CD,		 -- 은행코드
   C.MO_ACNT_NO,		 -- 계좌번호
   A.RIGT_INSU_FEE,		 -- 권원보험료
   A.JUDG_FEE,		 -- 감정료
   A.SET_FEE_BRDN_DIV_CD,	 -- 설정료부담구분코드
   '0' STMP_FEE_AF_ACQT_FG   --인지대후취여부 --/ 2009.12.09 수정 후
FROM			
   AUSER.ALOT_LOAN_PAY_DESC A,   -- 대출지급내역
   AUSER.APDT_PRDT_BASE     B,   -- 상품기본
   AUSER.ALOT_MO_ACNT_BASE  C    -- 모계좌기본
WHERE A.LOAN_NO  = '11121600004'--?	     
  AND A.LOAN_SEQ = '01'--?	     
  AND A.SNUM     = '01'--?	     
  AND A.PRDT_CD  = B.PRDT_CD    
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD 
  AND DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD 
;

-- 189
-- selectPrFeDesc()
-- '01' 신용관리 - 사용안함
-- AUSER.ALOT_PR_FEE_PAY_DESC -> 테이블 없음
SELECT 					
   A.LOAN_NO,				 -- 대출번호
   A.LOAN_SEQ,				 -- 대출순번
   A.PR_FEE,				 -- 판촉수수료
   A.ITAX,					 -- 소득세
   A.HTAX,					 -- 주민세
   C.BANK_CD,		 -- 은행코드
   C.MO_ACNT_NO,		 -- 계좌번호
   A.PR_EMP_RESI_NO		 -- 판촉사원주민번호
FROM					
   AUSER.ALOT_PR_FEE_PAY_DESC A, 	 -- 판촉수수료지급내역
   AUSER.ALOT_MO_ACNT_BASE C    -- 모계좌기본
WHERE A.LOAN_NO  = '11121600004'--?	     
  AND A.LOAN_SEQ = '01'--?	     
  AND A.SNUM     = '01'--?	     
  AND C.BSN_DIV_CD = '01'                     
;

select * from AUSER.ALOT_PR_FEE_PAY_DESC
;

-- 505
-- selectLoanPayPrdtBase2()
-- AUSER.ALOT_DOC_KEEP_SPEC - 테이블 없음
SELECT 					
   A.LOAN_PAMT,			 -- 대출원금
  (A.CUST_TRT_FEE+A.CPRT_COM_TRT_FEE) TRT_FEE, -- 취급수수료(고객+제휴사)
   A.CPRT_FEE,			 -- 제휴수수료
   A.STMP_FEE,			 -- 인지대
   A.SET_FEE,				 -- 설정료
   A.NOTA_FEE,			 -- 공증료
   A.ETC_FEE,				 -- 기타수수료
   A.PAY_AMT,				 -- 지급금액
   A.RQST_DIV_CD,			 -- 요청구분코드
   A.LOAN_NO,				 -- 대출번호
   A.LOAN_SEQ,			 -- 대출순번
   A.SNUM,				 -- 일련번호
   C.BANK_CD,		 -- 은행코드
   C.MO_ACNT_NO,		 -- 계좌번호
   (SELECT STMP_FEE_AF_ACQT_FG FROM AUSER.ALOT_DOC_KEEP_SPEC 
   WHERE LOAN_NO=A.LOAN_NO AND LOAN_SEQ=A.LOAN_SEQ AND CNCL_FG <> '1'ORDER BY SNUM FETCH FIRST ROW ONLY)  --인지대후취여부
FROM					
   AUSER.ALOT_LOAN_PAY_DESC  A,	 -- 대출지급내역
   AUSER.ALOT_MO_ACNT_BASE   C    -- 모계좌기본
WHERE A.CNCL_FG = '0'			 -- 취소여부
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
-- AUSER.ALOT_DOC_KEEP_SPEC -> 테이블 없음
SELECT 					
   A.LOAN_PAMT,			 -- 대출원금
  (A.CUST_TRT_FEE+A.CPRT_COM_TRT_FEE) TRT_FEE, -- 취급수수료(고객+제휴사)
   A.CPRT_FEE,			 -- 제휴수수료
   A.STMP_FEE,			 -- 인지대
   A.SET_FEE,				 -- 설정료
   A.NOTA_FEE,			 -- 공증료
   A.ETC_FEE,				 -- 기타수수료
   A.PAY_AMT,				 -- 지급금액
   A.RQST_DIV_CD,			 -- 요청구분코드
   A.LOAN_NO,				 -- 대출번호
   A.LOAN_SEQ,			 -- 대출순번
   A.SNUM,				 -- 일련번호
   C.BANK_CD,		 -- 은행코드
   C.MO_ACNT_NO,	 -- 계좌번호
   (SELECT STMP_FEE_AF_ACQT_FG FROM AUSER.ALOT_DOC_KEEP_SPEC 
   WHERE LOAN_NO=A.LOAN_NO AND LOAN_SEQ=A.LOAN_SEQ 
   AND CNCL_FG <> '1' ORDER BY SNUM FETCH FIRST ROW ONLY)  --인지대후취여부
FROM			
   AUSER.ALOT_LOAN_PAY_DESC  A,   -- 대출지급내역
   AUSER.ALOT_MO_ACNT_BASE   C    -- 모계좌기본
WHERE A.CNCL_FG = '0'		 -- 취소여부
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
   ,C.BANK_CD	                         -- 은행코드
   ,C.MO_ACNT_NO		         -- 계좌번호
FROM AUSER.ALOT_PR_FEE_PRE_PAY_DESC A  
    ,AUSER.ALOT_MO_ACNT_BASE C         
WHERE A.EMP_RESI_NO = ?                
AND A.SNUM = ?                         
AND C.BSN_DIV_CD  = '13'   		
;

select * from AUSER.ALOT_PR_FEE_PRE_PAY_DESC
;


