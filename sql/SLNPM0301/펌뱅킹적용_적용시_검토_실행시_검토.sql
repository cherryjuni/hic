-- 우리은행 지급 모계좌 확인
select * from AUSER.ALOT_MO_ACNT_BASE;

-- 우리은행 지급 모계좌 등록
--select * from
--update auser.alot_mo_acnt_base
set bsn_div_cd = '03'
where bsn_div_cd = '93' and bank_cd = '020';
--commit;


-- 지급계좌 확인
--select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' order by pay_no desc;
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '01' order by pay_no desc;

-- 검증쿼리
select
--    *
    p.pay_no, p.rcgn_no, p.loan_no, p.loan_seq, p.pay_bsn_div_cd, p.proc_stat_cd, p.cust_nm, p.setl_bank_cd, p.setl_acnt_no
    , d.rqst_dt, pay_stat_cd
    , l.img_acnt_bank_cd, l.img_acnt_no
    , p.loan_pamt
    , decode(p.loan_pamt, d.loan_pamt,      '정상', '오류-지급상세 금액')         p_loan_pamt
    , decode(p.loan_pamt, i.loan_pamt,      '정상', '오류-원리금 금액')           i_loan_pamt
    , decode(p.loan_pamt, l.loan_pamt,      '정상', '오류-계좌기본 대출금액')     l_loan_pamt
    , decode(p.loan_pamt, l.loan_pamt_ramt, '정상', '오류-계좌기본 대출금액잔액') l_loan_pamt_ramt
    , decode(p.loan_pamt, l.exec_amt,       '정상', '오류-계좌기본 실행금액')     l_exec_amt
    , decode(p.loan_pamt, l.aprv_amt,       '정상', '오류-계좌기본 승인금액')     l_aprv_amt
from
    auser.alot_loan_onln_pay p
    , auser.alot_loan_pay_desc d
    , auser.alot_loan_base l
    , auser.alot_loan_piamt i
where
    1 = 1
    and p.loan_no = d.loan_no and p.loan_seq = d.loan_seq
    and p.loan_no = l.loan_no and p.loan_seq = l.loan_seq
    and p.loan_no = i.loan_no and p.loan_seq = i.loan_seq
    and p.tran_proc_dt = '2011-12-27'
    and p.proc_stat_cd = '01'
--    and p.loan_no =  '11122700298' --'11122200078'-- 2 '11122300121' -- 1 '11122300046'
--    and
--    p.pay_no = '11122600000001'
order by pay_no desc
    ;

select * from auser.alot_loan_base where loan_no = '11122700298' --in ( '11122700298', '11122700297', '11122700300', '11122700246');
;

select * from AUSER.ALOT_LOAN_ONLN_PAY
WHERE
    1=1
--    not (SETL_BANK_CD = '026' and SETL_ACNT_NO = '110314809090'  and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    not (SETL_BANK_CD = '020' and SETL_ACNT_NO = '1002934652109' and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    and not (LOAN_NO  = :loanNo AND LOAN_SEQ = :loanSeq)
--    and not (LOAN_NO  = '11122600001' AND LOAN_SEQ = '01')
    and tran_proc_dt = '2011-12-27' 
--    and proc_stat_cd = '01'
;
--update auser.alot_loan_onln_pay set proc_stat_cd = '01' where tran_proc_dt = '2011-12-27' and proc_stat_cd = '02';
--commit;

--update AUSER.ALOT_LOAN_ONLN_PAY
set proc_stat_cd = '09'
WHERE
    1=1 
--    not (SETL_BANK_CD = '026' and SETL_ACNT_NO = '110314809090'  and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    not (SETL_BANK_CD = '020' and SETL_ACNT_NO = '1002934652109' and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '김동오')
--    and not(LOAN_NO  = :loanNo AND LOAN_SEQ = :loanSeq)
    and tran_proc_dt = '2011-12-27' 
    and proc_stat_cd = '01'
;
--commit;

-- 업데이트 후, 확인
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '01' order by pay_no desc;
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '09' order by pay_no desc;



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
    and tran_proc_dt = '2011-12-27' 
    proc_stat_cd = '09'
-- WHERE LOAN_NO  = :loanNo
--   AND LOAN_SEQ = :loanSeq
;
--commit;

