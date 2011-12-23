select * from euser.efdt_depo_base
;
-- 가상모계좌 대출 계좌 선택
--  1. 신한은행 - D0177
--  2. 우리은행 - D0203 - 0###
-- 추후, 우리은행에 가상모계좌를 여러개 사용시
--       다른곳에 정의 우선
--       1. 가상계좌의 번호의 범위에 따라 가상모계좌를 선택
--       2. 가상계좌와 가상모계좌를 정의한 테이블에서 가져오도록 개발
--        2.1 가상계좌테이블에 가상모계좌 추가
--        2.2 가상계좌테이블 + 가상계좌모계좌테이블생성
SELECT ACNT_NO, BANK_CD               
		  FROM EUSER.EFDT_DEPO_BASE           
		   WHERE PRPS_DIV_CD = '2' --?  /* '2' */     
		   AND DEPO_STAT_CD <> '90' --?              
		   AND DEPO_NO = 'D0177'             -- 가상계좌 모계좌
		   AND LOC_DIV_CD = 'W01' --?                 
           ;
           


SELECT * FROM EUSER.EACT_TR_ACCT_DESC
;

SELECT * FROM BUSER.BCLT_CLS_SUB_DESC
;

SELECT	B.VOCH_NO
	,'2'	AS MNBR_VOCH_DIV_CD
FROM	EUSER.EACT_VOCH_BASE A
	, EUSER.EACT_MNBR_VOCH B
WHERE
--    A.VOCH_NO = '' --?vochNo
--	AND 
    A.VOCH_NO = B.CNTR_VOCH_NO
;


SELECT * FROM EUSER.EACT_MNBR_VOCH
;

SELECT * FROM EUSER.EACT_VOCH_BASE
where voch_no = '201112190003'
;

SELECT * FROM EUSER.EACT_VOCH_DESC
where voch_no = '201112190003'
;

SELECT * FROM EUSER.EACT_VOCH_DESC
where voch_no = '2011121800002'
;

SELECT        
    VOCH_STAT_CD        
    , MAKE_EMP_NO        
    , ACCT_DT        
    , ORG_VOCH_NO        
    , TR_TP_DIV_CD || TR_STAG_DIV_CD        
    , PROF_FG        
--INTO        
--    V_VOCH_STAT_CD        
--    , V_MAKE_EMP_NO        
--    , V_VOCH_ACCT_DT        
--    , V_ORG_VOCH_NO        
--    , V_TR_TP_CD        
--    , V_PROF_FG        
FROM        
    EUSER.EACT_VOCH_BASE        
WHERE        
    VOCH_NO = '201112190003';
    ;



--        FROM BUSER.BCLT_CLS_TR_DESC A
--            ,EUSER.EACT_TR_ACCT_DESC B
--            LEFT OUTER JOIN EUSER.EACT_TR_CALC_DESC C

SELECT * FROM AUSER.ALOT_LOAN_PAY_DESC
where PAY_INTN_VOCH_NO = '2011121800002'
;

SELECT * FROM EUSER.EACT_TR_ACCT_DESC
where PAY_INTN_VOCH_NO = '2011121800002'
;


