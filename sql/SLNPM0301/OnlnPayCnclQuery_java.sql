-- OnlnPayCnclQuery
-- 23 line
-- getOnlnPayObjByGramNoQuery()
SELECT                	
	A.PAY_NO             ,
	A.RCGN_NO            ,
	A.LOAN_NO            ,
	A.LOAN_SEQ           ,
	A.PAY_BSN_DIV_CD     ,
	A.CUST_NM            ,
	A.MNG_DEPT_CD        ,
	A.SETL_BANK_CD       ,
	A.SETL_ACNT_NO       ,
	A.DEPO_OWN_NO        ,
	A.DEPO_OWNNM         ,
	A.IAMT_MANMN         ,
	A.LOAN_PAMT          ,
	A.TRT_FEE            ,
	A.CPRT_FEE           ,
	A.STMP_FEE           ,
	A.TRAN_RQST_AMT      ,
	A.TRAN_RQST_DTTM     ,
	A.TRAN_PROC_DT       ,
	A.TR_BANK_CD         ,
	A.OAMT_FEE           ,
	A.GRAM_SEND_DTTM     ,
	A.GRAM_CHSE_NO       ,
	A.TR_STRT_TM         ,
	A.RSPN_CD            ,
	A.PROC_STAT_CD       ,
	A.OAMT_AFAMT         ,
	A.MNO_RQST_OBJ_FG    ,
	A.TRAN_IPSS_GRAM_NO  ,
	A.VOCH_JNL_LAST_SEQ  ,
	A.LOAN_CNT           ,
	B.BANK_CD	      	 ,
	B.MO_ACNT_NO	      
FROM AUSER.ALOT_LOAN_ONLN_PAY A,       
     AUSER.ALOT_MO_ACNT_BASE  B        
WHERE
--  A.PROC_STAT_CD    = '05'   	 /*처리상태코드(완료)*/
--  AND
  A.TRAN_PROC_DT    = '2011-12-16' --?     	
  AND A.GRAM_CHSE_NO    = '200329020000156' --?     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
  AND DECODE(A.SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = B.BANK_CD 
  ORDER BY A.PAY_NO
;



-- 78
-- getOnlnPayObjByPayNoQuery()
SELECT                	
	A.PAY_NO             ,
	A.RCGN_NO            ,
	A.LOAN_NO            ,
	A.LOAN_SEQ           ,
	A.PAY_BSN_DIV_CD     ,
	A.CUST_NM            ,
	A.MNG_DEPT_CD        ,
	A.SETL_BANK_CD       ,
	A.SETL_ACNT_NO       ,
	A.DEPO_OWN_NO        ,
	A.DEPO_OWNNM         ,
	A.IAMT_MANMN         ,
	A.LOAN_PAMT          ,
	A.TRT_FEE            ,
	A.CPRT_FEE           ,
	A.STMP_FEE           ,
	A.TRAN_RQST_AMT      ,
	A.TRAN_RQST_DTTM     ,
	A.TRAN_PROC_DT       ,
	A.TR_BANK_CD         ,
	A.OAMT_FEE           ,
	A.GRAM_SEND_DTTM     ,
	A.GRAM_CHSE_NO       ,
	A.TR_STRT_TM         ,
	A.RSPN_CD            ,
	A.PROC_STAT_CD       ,
	A.OAMT_AFAMT         ,
	A.MNO_RQST_OBJ_FG    ,
	A.TRAN_IPSS_GRAM_NO  ,
	A.VOCH_JNL_LAST_SEQ  ,
	A.LOAN_CNT           ,
	B.BANK_CD	      	 ,
	B.MO_ACNT_NO	      
FROM AUSER.ALOT_LOAN_ONLN_PAY A,       
     AUSER.ALOT_MO_ACNT_BASE  B       
WHERE
--  A.PROC_STAT_CD    = '05'   	 /*처리상태코드(완료)*/
--  AND
  A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
  AND DECODE(A.SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = B.BANK_CD 
  AND A.TRAN_PROC_DT    = '2011-12-17' --?     	
  AND A.PAY_NO    = '11121600000003' --?     	
  ORDER BY A.PAY_NO
;

