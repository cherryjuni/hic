
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


select * from auser.alot_loan_base
where img_acnt_no = '56201554000203'
;

-- 원거래 조회
SELECT
--	SETL_FG           
    *
FROM BUSER.BVAT_CMS_IAMT_DESC     
WHERE TR_ORG_CD = 'C1004' AND TR_NO = '201108110342711' --?
;

'201112230342713'
'201112230342713'
;


--
SELECT                                                    
    CLAS1                                    DEPT_CD,     
    CLAS2                                    EMP_NO       
FROM GUSER.GBCT_COMM_CD_DESC                              
WHERE CD_KIND_NO = 'R00038' --?   /*R00038*/                         
  AND CD_DESC_NO = '1' --?   /*'1'      */                      
;


-- 원거래 취소
        "UPDATE BUSER.BVAT_CMS_IAMT_DESC   \n" +
        "SET                               \n" +
        "    ORG_TR_DT = ?,                \n" +
        "    ORG_TR_SNUM = ?,              \n" +
        "    CNCL_RECV_DT = ?,             \n" +
        "    CNCL_RECV_TM =?,              \n" +
        "    CNCL_FG = ?,                  \n" +
        "    CNCL_DT =?,                   \n" +
		"    LAST_PROC_DT=?,               \n" +
		"    LAST_PROC_TM=?,               \n" +
		"    LAST_PROC_EMP_NO=?            \n" +
        "WHERE                             \n" +
        "    TR_ORG_CD = ? AND TR_NO = ?   \n";


