
--------------------------------------------------
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
      FROM  AUSER.ALOT_LOAN_BASE
     WHERE
       IMG_ACNT_NO      = ?                      /* :입금내역.계좌번호  */
       AND IMG_ACNT_BANK_CD = '0'||?             /* :입금내역.은행_코드 */
       AND LOAN_LAST_FG     = '1'
       ;
--------------------------------------------------
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
      FROM  AUSER.ALOT_LOAN_BASE
     WHERE
       IMG_ACNT_NO          = '56201551042002'  /* :입금내역.계좌번호  */
       AND IMG_ACNT_BANK_CD = '026'             /* :입금내역.은행_코드 */
       AND LOAN_LAST_FG     = '1'
;
--------------------------------------------------
-- 마지막 코드 여러건 찾기
--------------------------------------------------
select loan_no, count(*)
from auser.alot_loan_base
where loan_last_fg = '1'
group by loan_no
having count(*) > 1
;

select loan_no, loan_seq, loan_last_fg, loan_dt, end_dt
from auser.alot_loan_base
where
    loan_no in (
        select loan_no
        from auser.alot_loan_base
        where loan_last_fg = '1'
        group by loan_no
        having count(*) > 1
    )
    and loan_last_fg = '1'
order by loan_no, loan_seq
;
--------------------------------------------------

select * from (
SELECT  CASE WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' 
    THEN LOAN_NO                            
    WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3'   
      THEN '344'                            
      ELSE null  END    AS LOAN_NO                  
   ,NVL(B.LOAN_SEQ,'01')               LOAN_SEQ    
   ,NVL(B.MNG_DEPT_CD, A.MNG_DEPT_CD)  MNG_DEPT_CD  
  FROM  BUSER.BVAT_UNCNFM_BASE A        
             ,AUSER.ALOT_LOAN_BASE    B       
     WHERE DIV_CD           = 1         /* '1'              */    
       AND TRIM(IMG_ACNT_NO)  = '56201551042002'         /* :입금내역.계좌번호   */  
       --AND IMG_ACNT_BANK_CD = '0'||?    /* :입금내역.은행_코드 */	  
   ) 
--where loan_no is not null	  
    
    --   AND LOAN_LAST_FG     = 1         /* '1'              */
	--  AND LOAN_NO is not null    
;

select * from buser.bvat_uncnfm_base
;

select LOAN_NO, LOAN_SEQ, LOAN_STAT_CD, MNG_DEPT_CD from AUSER.ALOT_LOAN_BASE 
where  TRIM(IMG_ACNT_NO)  = '56201551042002' --'56201555007422'         /* :입금내역.계좌번호   */
;

select * from AUSER.ALOT_LOAN_BASE 
where  TRIM(IMG_ACNT_NO)  = '56201551042002' --'56201555007422'         /* :입금내역.계좌번호   */
;

select rowid, a.* from AUSER.ALOT_LOAN_BASE a
where  TRIM(IMG_ACNT_NO)  = '56201550414684'         /* :입금내역.계좌번호   */
;


CASE WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' 
    THEN LOAN_NO                            
    WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3'   
      THEN '344'                            
      ELSE null  END    AS LOAN_NO
;

select  IMG_ACNT_NO , count(loan_last_fg) from AUSER.ALOT_LOAN_BASE
where LOAN_LAST_FG='1'
group by IMG_ACNT_NO
having count(LOAN_LAST_FG)>=2 
;

select  IMG_ACNT_NO , count(loan_last_fg) from AUSER.ALOT_LOAN_BASE
where LOAN_LAST_FG='1'
--    and SUBSTR(LOAN_STAT_CD,1,1) <> '2'
group by IMG_ACNT_NO
having count(LOAN_LAST_FG)>=2 
;

------------------------------------------- ORG quary
SELECT
    CASE WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' 
        THEN LOAN_NO                            
        WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3'   
        THEN '344'                            
    ELSE null  END    AS LOAN_NO                  
    ,NVL(B.LOAN_SEQ,'01')               LOAN_SEQ          
    ,NVL(B.MNG_DEPT_CD, A.MNG_DEPT_CD)  MNG_DEPT_CD     
FROM  BUSER.BVAT_UNCNFM_BASE A        
    ,AUSER.ALOT_LOAN_BASE    B                           
WHERE DIV_CD           = ?         /* '1'              */    
    AND IMG_ACNT_NO  = ?         /* :입금내역.계좌번호   */    
    AND IMG_ACNT_BANK_CD = '0'||?    /* :입금내역.은행_코드 */    
    AND LOAN_LAST_FG     = ?         /* '1'              */    ;
;
-- 테스트계좌 검토
-- 입금내역 IMG_ACNT_BANK_CD - 026() / 088()
SELECT
    CASE WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' THEN LOAN_NO                            
         WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3' THEN '344'                            
    ELSE null
    END    AS LOAN_NO                  
    ,NVL(B.LOAN_SEQ,'01')               LOAN_SEQ          
    ,NVL(B.MNG_DEPT_CD, A.MNG_DEPT_CD)  MNG_DEPT_CD     
FROM  BUSER.BVAT_UNCNFM_BASE A        
    ,AUSER.ALOT_LOAN_BASE    B                           
WHERE DIV_CD           = '1'         /* '1'              */    
    AND IMG_ACNT_NO  = '56201551042002'         /* :입금내역.계좌번호   */    
    AND IMG_ACNT_BANK_CD = '0'||?    /* :입금내역.은행_코드 */    
    AND LOAN_LAST_FG     = ?         /* '1'              */    ;
;

SELECT *
FROM  BUSER.BVAT_UNCNFM_BASE
;

select LOAN_NO,LOAN_SEQ,LOAN_STAT_CD,IMG_ACNT_BANK_CD,IMG_ACNT_NO,LOAN_LAST_FG
from AUSER.ALOT_LOAN_BASE    B
WHERE IMG_ACNT_NO  = '56201551042002'         /* :입금내역.계좌번호   */
;

select LOAN_NO,LOAN_SEQ,IMG_ACNT_BANK_CD,IMG_ACNT_NO,LOAN_LAST_FG
from AUSER.ALOT_LOAN_BASE    B
where
--    DIV_CD           = '1'         /* '1'              */    
--    AND
    IMG_ACNT_NO  = '56201551042002'         /* :입금내역.계좌번호   */    
;

-- 입금내역.은행_코드 검토
select IMG_ACNT_BANK_CD
from AUSER.ALOT_LOAN_BASE    B
group by IMG_ACNT_BANK_CD
;

---------------------------------------------
SELECT
    CASE
        WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' THEN LOAN_NO                            
        WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3' THEN '344'                            
        ELSE null
    END AS LOAN_NO                  
--   , A.LOAN_STAT_CD, B.LOAN_STAT_CD
   ,NVL(B.LOAN_SEQ,'01')               LOAN_SEQ    
   ,NVL(B.MNG_DEPT_CD, A.MNG_DEPT_CD)  MNG_DEPT_CD  
FROM  BUSER.BVAT_UNCNFM_BASE A        
    ,AUSER.ALOT_LOAN_BASE    B       
     WHERE DIV_CD           = 1         /* '1'              */    
       AND TRIM(IMG_ACNT_NO)  = '56201551042002'         /* :입금내역.계좌번호  운영 / 개발*/  
--       AND TRIM(IMG_ACNT_NO)  = '56201554091224'       /* :입금내역.계좌번호  개발  */  
       --AND IMG_ACNT_BANK_CD = '0'||?    /* :입금내역.은행_코드 */	  
;
----------------------------------------------
select
    LOAN_NO
    , LOAN_SEQ
    , MNG_DEPT_CD
    , LOAN_STAT_CD
    , IMG_ACNT_BANK_CD
--    , a.*
from AUSER.ALOT_LOAN_BASE a
where
    TRIM(IMG_ACNT_NO)  = '56201551042002' --'56201555007422'         /* :입금내역.계좌번호   */
;
----------------------------------------------
select
    LOAN_NO
    , LOAN_SEQ
    , MNG_DEPT_CD
    , LOAN_STAT_CD
    , IMG_ACNT_BANK_CD
--    , a.*
from AUSER.ALOT_LOAN_BASE a;
----------------------------------------------
-- '026' count 143311
-- '088' count 324 - 사용하지 않는 코드 [예전것임 / 종료된 계좌 / 최종 종료일 (2010-03-09)]
----------------------------------------------
select IMG_ACNT_BANK_CD, count(*)
from AUSER.ALOT_LOAN_BASE a
group by IMG_ACNT_BANK_CD
;

select loan_stat_cd, loan_dt, end_dt, a.*
from AUSER.ALOT_LOAN_BASE a
where
    IMG_ACNT_BANK_CD = '088'
--    AND LOAN_SEQ > '01'
--    AND LOAN_LAST_FG = '1'
;

select LOAN_NO, COUNT(*)
from AUSER.ALOT_LOAN_BASE a
where
    IMG_ACNT_BANK_CD = '088'
    AND LOAN_SEQ > '01'
--    AND LOAN_LAST_FG = '1'
GROUP BY LOAN_NO
HAVING COUNT(*) > 1
;
------------------------------------------------
select * from AUSER.ALOT_LOAN_IMG_ACNT_DESC
;

select IMG_ACNT_BANK_CD
from AUSER.ALOT_LOAN_IMG_ACNT_DESC
group by IMG_ACNT_BANK_CD

