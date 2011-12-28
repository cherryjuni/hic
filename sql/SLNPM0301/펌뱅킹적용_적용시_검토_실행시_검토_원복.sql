-- 우리은행 지급 모계좌 원복
--update auser.alot_mo_acnt_base
set bsn_div_cd = '93'
where bsn_div_cd = '03' and bank_cd = '020';
--commit;

-- 지급계좌 원복
--update 
--AUSER.ALOT_LOAN_ONLN_PAY
set proc_stat_cd = '01'
where
    1=1
    and tran_proc_dt = '2011-12-26' 
    proc_stat_cd = '09'
-- WHERE LOAN_NO  = :loanNo
--   AND LOAN_SEQ = :loanSeq
;
--commit;