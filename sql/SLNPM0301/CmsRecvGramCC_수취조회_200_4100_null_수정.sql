;
--------------------------------------------------

SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD ,loan_stat_cd
      FROM  AUSER.ALOT_LOAN_BASE
     WHERE
       IMG_ACNT_NO      = '56201550189380'  /* :�Աݳ���.���¹�ȣ  */
       AND IMG_ACNT_BANK_CD = '0'||'26'     /* :�Աݳ���.����_�ڵ� */
       AND LOAN_STAT_CD >= '22'             /* ���� ���� ���� */
       AND LOAN_LAST_FG     = '1'
       order by loan_stat_cd asc
       ;

-------------
CONT_MAN_NO
;
select * from auser.alot_loan_base
where cont_man_no in ('7612252489710'
,'8005172074630'
,'8111202850026'
,'8107221155725'
,'56201551009387' )
;
--56201550189380
select * from auser.alot_loan_base
where loan_no in ('10100700091' )
;

--10100700091 01 (����) -> 11042500085 01 (����)
-------------------------------------
-- �� - ������ ����
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
      FROM  AUSER.ALOT_LOAN_BASE
     WHERE
       IMG_ACNT_NO          = '56201551042002'  /* :�Աݳ���.���¹�ȣ  */
       AND IMG_ACNT_BANK_CD = '026'             /* :�Աݳ���.����_�ڵ� */
       AND LOAN_LAST_FG     = '1'
;
-- �� - �������� ����
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
      FROM  AUSER.ALOT_LOAN_BASE
     WHERE
       IMG_ACNT_NO          = '56201551042002'  /* :�Աݳ���.���¹�ȣ  */
       AND IMG_ACNT_BANK_CD = '026'             /* :�Աݳ���.����_�ڵ� */
       AND LOAN_LAST_FG     = '1'
;

-- 
-- �� - ������� ���� ã��
-- LOAN_NO    IMG_ACNT_NO
--08060400007 56201554000203
--08060500002 56201554000240
--08060400010 56201554000237
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,IMG_ACNT_NO
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
      FROM  AUSER.ALOT_LOAN_BASE
     WHERE
--       IMG_ACNT_NO      = ?                      /* :�Աݳ���.���¹�ȣ  */
--       AND IMG_ACNT_BANK_CD = '0'||?             /* :�Աݳ���.����_�ڵ� */
--       AND 
       LOAN_LAST_FG     = '1'
       AND LOAN_STAT_CD IN ('22', '29')             /* ���� ���� ���� */
       ;
       
-- �� - �������� ���� ã��
--LOAN_NO     IMG_ACNT_NO
--08052900006 56201553000020
--08053000003 56201552000032
--08053000001 56201554000025
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,IMG_ACNT_NO
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
      FROM  AUSER.ALOT_LOAN_BASE
     WHERE
--       IMG_ACNT_NO      = ?                      /* :�Աݳ���.���¹�ȣ  */
--       AND IMG_ACNT_BANK_CD = '0'||?             /* :�Աݳ���.����_�ڵ� */
--       AND 
       LOAN_LAST_FG     = '1'
       AND LOAN_STAT_CD NOT IN ('22', '29')             /* ���� ���� ���� */
       ;

-- ������ ������ Ȯ��
-- LOAN_NO    IMG_ACNT_NO
--08060400007 56201554000203
--08060500002 56201554000240
--08060400010 56201554000237
select loan_no, count(*)
from auser.alot_loan_base
--where LOAN_NO IN ('08060400007', '08060500002', '08060400010') AND loan_last_fg = '1'
where IMG_ACNT_NO IN ('56201554000203', '56201554000240', '56201554000237') AND loan_last_fg = '1'
group by loan_no
--having count(*) > 1
;
-- �������� ������ Ȯ��
--LOAN_NO     IMG_ACNT_NO
--08052900006 56201553000020
--08053000003 56201552000032
--08053000001 56201554000025
select loan_no, count(*)
from auser.alot_loan_base
--where LOAN_NO IN ('08052900006', '08053000003', '08053000001') AND loan_last_fg = '1'
where IMG_ACNT_NO IN ('56201553000020', '56201552000032', '56201554000025') AND loan_last_fg = '1'
group by loan_no
--having count(*) > 1
;


select IMG_ACNT_NO,loan_stat_cd from auser.alot_loan_base
where LOAN_NO in ('11022500201'
,'11030900110'
,'11051200193' )

--------------------------------------------------
-- ������ �ڵ� ������ ã��
--------------------------------------------------
;
select cont_man_no , count(*)
from auser.alot_loan_base
where loan_last_fg = '1' and loan_stat_cd in ('22','31','32' )
group by cont_man_no
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
SELECT  CASE WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' THEN LOAN_NO                            
             WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3' THEN '344'                            
             ELSE null
        END    AS LOAN_NO                  
   ,NVL(B.LOAN_SEQ,'01')               LOAN_SEQ    
   ,NVL(B.MNG_DEPT_CD, A.MNG_DEPT_CD)  MNG_DEPT_CD  
  FROM  BUSER.BVAT_UNCNFM_BASE A        
             ,AUSER.ALOT_LOAN_BASE    B       
     WHERE DIV_CD           = 1         /* '1'              */    
       AND TRIM(IMG_ACNT_NO)  = '56201551042002'         /* :�Աݳ���.���¹�ȣ   */  
       --AND IMG_ACNT_BANK_CD = '0'||?    /* :�Աݳ���.����_�ڵ� */	  
   ) 
--where loan_no is not null	  
    
    --   AND LOAN_LAST_FG     = 1         /* '1'              */
	--  AND LOAN_NO is not null    
;

select * from buser.bvat_uncnfm_base
;

select LOAN_NO, LOAN_SEQ, LOAN_STAT_CD, MNG_DEPT_CD from AUSER.ALOT_LOAN_BASE 
where  TRIM(IMG_ACNT_NO)  = '56201551042002' --'56201555007422'         /* :�Աݳ���.���¹�ȣ   */
;

select * from AUSER.ALOT_LOAN_BASE 
where  TRIM(IMG_ACNT_NO)  = '56201551042002' --'56201555007422'         /* :�Աݳ���.���¹�ȣ   */
;

select rowid, a.* from AUSER.ALOT_LOAN_BASE a
where  TRIM(IMG_ACNT_NO)  = '56201550414684'         /* :�Աݳ���.���¹�ȣ   */
;


CASE WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' THEN LOAN_NO                            
     WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3' THEN '344'                            
     ELSE null
END    AS LOAN_NO
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
    AND IMG_ACNT_NO  = ?         /* :�Աݳ���.���¹�ȣ   */    
    AND IMG_ACNT_BANK_CD = '0'||?    /* :�Աݳ���.����_�ڵ� */    
    AND LOAN_LAST_FG     = ?         /* '1'              */    ;
;
-- �׽�Ʈ���� ����
-- �Աݳ��� IMG_ACNT_BANK_CD - 026() / 088()
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
    AND IMG_ACNT_NO  = '56201551042002'         /* :�Աݳ���.���¹�ȣ   */    
    AND IMG_ACNT_BANK_CD = '0'||?    /* :�Աݳ���.����_�ڵ� */    
    AND LOAN_LAST_FG     = ?         /* '1'              */    ;
;

SELECT *
FROM  BUSER.BVAT_UNCNFM_BASE
;

select LOAN_NO,LOAN_SEQ,LOAN_STAT_CD,IMG_ACNT_BANK_CD,IMG_ACNT_NO,LOAN_LAST_FG
from AUSER.ALOT_LOAN_BASE    B
WHERE IMG_ACNT_NO  = '56201551042002'         /* :�Աݳ���.���¹�ȣ   */
;

select LOAN_NO,LOAN_SEQ,IMG_ACNT_BANK_CD,IMG_ACNT_NO,LOAN_LAST_FG
from AUSER.ALOT_LOAN_BASE    B
where
--    DIV_CD           = '1'         /* '1'              */    
--    AND
    IMG_ACNT_NO  = '56201551042002'         /* :�Աݳ���.���¹�ȣ   */    
;

-- �Աݳ���.����_�ڵ� ����
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
       AND TRIM(IMG_ACNT_NO)  = '56201551042002'         /* :�Աݳ���.���¹�ȣ  � / ����*/  
--       AND TRIM(IMG_ACNT_NO)  = '56201554091224'       /* :�Աݳ���.���¹�ȣ  ����  */  
       --AND IMG_ACNT_BANK_CD = '0'||?    /* :�Աݳ���.����_�ڵ� */	  
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
    TRIM(IMG_ACNT_NO)  = '56201551042002' --'56201555007422'         /* :�Աݳ���.���¹�ȣ   */
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
-- '088' count 324 - ������� �ʴ� �ڵ� [�������� / ����� ���� / ���� ������ (2010-03-09)]
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

