

select * from auser.alot_loan_onln_pay
where
    tran_proc_dt = '2011-12-22'
    and pay_no > '11122200000182'
order by
--    last_proc_tm desc
    rcgn_no desc
;

select * from auser.alot_loan_pay_desc
where
    (loan_no, loan_seq) in (select loan_no, loan_seq from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-22' and pay_no > '11122200000182')
;

select * from auser.alot_loan_base
where
    (loan_no, loan_seq) in (select loan_no, loan_seq from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-22' and pay_no > '11122200000182')
;


