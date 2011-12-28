-- 우리은행 지급 모계좌 등록
select * from auser.alot_mo_acnt_base;
--select * from auser.alot_mo_acnt_base
--update auser.alot_mo_acnt_base
set bsn_div_cd = '03'
where bsn_div_cd = '93' and bank_cd = '020';
--commit;


-- 지급계좌 확인
--select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' order by pay_no desc;
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '01' order by pay_no desc;

select * from AUSER.ALOT_LOAN_ONLN_PAY
WHERE
    1=1
--    and SETL_BANK_CD = '026' and SETL_ACNT_NO = '110314809090'  and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오'
--    and not (SETL_BANK_CD = '026' and SETL_ACNT_NO = '110314809090'  and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    and not (SETL_BANK_CD = '020' and SETL_ACNT_NO = '1002934652109' and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    and not (LOAN_NO  = :loanNo AND LOAN_SEQ = :loanSeq)
--    and not (LOAN_NO  = '11122600001' AND LOAN_SEQ = '01')
    and tran_proc_dt = '2011-12-27' 
    and proc_stat_cd = '01'
;

--update AUSER.ALOT_LOAN_ONLN_PAY
set proc_stat_cd = '09'
WHERE
    1=1 
--    and not (SETL_BANK_CD = '026' and SETL_ACNT_NO = '110314809090'  and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    and not (SETL_BANK_CD = '020' and SETL_ACNT_NO = '1002934652109' and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    and not(LOAN_NO  = :loanNo AND LOAN_SEQ = :loanSeq)
    and tran_proc_dt = '2011-12-27' 
    and proc_stat_cd = '01'
;
--commit;

-- 업데이트 후, 확인
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '01' order by pay_no desc;
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '09' order by pay_no desc;
