
select * from auser.alot_mo_acnt_base
;



select * from auser.alot_loan_onln_pay
where tran_proc_dt = '2011-12-23'
;

select * from auser.alot_loan_pay_desc
where rqst_dt = '2011-12-23'
;

select * from auser.alot_loan_base
where ( loan_no, loan_seq)  in (select loan_no, loan_seq from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-23')
;

select
--    *
    p.pay_no, p.rcgn_no, p.loan_no, p.loan_seq, p.pay_bsn_div_cd, p.proc_stat_cd, p.cust_nm, p.setl_bank_cd, p.setl_acnt_no
    , d.rqst_dt, pay_stat_cd
    , l.img_acnt_bank_cd, l.img_acnt_no
from
    auser.alot_loan_onln_pay p
    , auser.alot_loan_pay_desc d
    , auser.alot_loan_base l
where
    p.loan_no = d.loan_no and p.loan_seq = d.loan_seq
    and p.loan_no = l.loan_no and p.loan_seq = l.loan_seq
    and
    p.tran_proc_dt = '2011-12-23';
    
    

select p.proc_stat_cd, p.*
from auser.alot_loan_onln_pay p
where
    tran_proc_dt = '2011-12-23'
    and
    pay_no = '11122300000001'
;

--select p.proc_stat_cd, p.*
update auser.alot_loan_onln_pay p
set proc_stat_cd = '01'
--from auser.alot_loan_onln_pay p
where
    tran_proc_dt = '2011-12-23'
    and
    pay_no = '11122300000001'
;

select d.pay_stat_cd, d.*
from auser.alot_loan_pay_desc d
--update auser.alot_loan_pay_desc p
--set pay_stat_cd = '1'
where
(loan_no, loan_seq) in
(
select loan_no, loan_seq
from auser.alot_loan_onln_pay
where
    tran_proc_dt = '2011-12-23'
    and
    pay_no = '11122300000001'
);

--commit;

