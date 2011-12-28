
select tr_bank_cd, count(*) from auser.alot_loan_onln_pay
where
    tran_proc_dt < '2011-12-28' and (tr_bank_cd != '088' or tr_bank_cd is null)
--    tr_bank_cd
group by tr_bank_cd
;

-- 2011-12-28 전 수정
--update auser.alot_loan_onln_pay
set tr_bank_cd = '088'
where
    tran_proc_dt < '2011-12-28' and (tr_bank_cd != '088' or tr_bank_cd is null)
;

-- 2011-12-28일 이후 적용 수정
--update auser.alot_loan_onln_pay
set tr_bank_cd = DECODE(PAY_BSN_DIV_CD, '03', DECODE(SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020'), '088')
where
    tran_proc_dt = '2011-12-28'
;

--commit;

--BANK_CD	COUNT(*)
--020	143
--088	19
-- count 검증 쿼리
select tr_bank_cd, count(*) from auser.alot_loan_onln_pay
where
    tran_proc_dt = '2011-12-28'
group by tr_bank_cd --DECODE(PAY_BSN_DIV_CD, '03', DECODE(SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020'), '088')
;

-- 사전 검증
select bank_cd, count(*) from 
(
select DECODE(PAY_BSN_DIV_CD, '03', DECODE(SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020'), '088') bank_cd
from auser.alot_loan_onln_pay
where
    tran_proc_dt = '2011-12-28'
    )
    
group by bank_cd --DECODE(PAY_BSN_DIV_CD, '03', DECODE(SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020'), '088')
;

