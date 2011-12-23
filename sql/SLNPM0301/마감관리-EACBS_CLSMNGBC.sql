-- 마감관리
-- EACBS_CLSMNGBC
-- 6 이체조회
SELECT
	  REPLACE(A.ACCT_DT, '-', '')	AS ACCT_DT
	, A.ACCT_DEPT_CD
	, A.TRAN_TP_CD
	, EUSER.EF_COMM('A00085', A.TRAN_TP_CD)	AS TRAN_TP_NM
	, COUNT(A.TRAN_NO)	AS TRAN_ORG_CNT
	, SUM(A.CLS_CNT)	AS CLS_CNT
	, SUM(A.TRAN_CNT)	AS TRAN_CNT
	, '0'				AS CHK
FROM (
	SELECT
		  ACCT_DT
		, ACCT_DEPT_CD
		, TRAN_TP_CD
		, TRAN_NO
		, (CASE WHEN VOCH_CLS_FG = '1' THEN 1 ELSE 0 END)	AS CLS_CNT	-- 마감건
		, (CASE WHEN TRAN_FG = '1'     THEN 1 ELSE 0 END)	AS TRAN_CNT	-- 이체건
	FROM
		BUSER.BCLT_CLS_BASE
	WHERE
        acct_dt = '2011-12-20'
		--?condition
	) A
GROUP BY
	A.ACCT_DT, A.ACCT_DEPT_CD, A.TRAN_TP_CD
;

	SELECT
		  ACCT_DT
		, ACCT_DEPT_CD
		, TRAN_TP_CD
		, TRAN_NO
		, (CASE WHEN VOCH_CLS_FG = '1' THEN 1 ELSE 0 END)	AS CLS_CNT	-- 마감건
		, (CASE WHEN TRAN_FG = '1'     THEN 1 ELSE 0 END)	AS TRAN_CNT	-- 이체건
	FROM
		BUSER.BCLT_CLS_BASE
	WHERE
        acct_dt = '2011-12-20'
--		?condition
;

select * 
	FROM
		BUSER.BCLT_CLS_BASE
;

EUSER.EF_COMM('A00085', A.TRAN_TP_CD)	AS TRAN_TP_NM
;

-- 이체유형코드
select * from 
where 'A00085'

