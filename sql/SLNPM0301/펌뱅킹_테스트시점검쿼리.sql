
select * from auser.alot_mo_acnt_base;

select * from auser.alot_mo_acnt_base where bsn_div_cd = '03';
select * from auser.alot_mo_acnt_base where bsn_div_cd = '03' and bank_cd = '020' and mo_acnt_no = '1002638811651';


select * from auser.alot_mo_acnt_base;
select * from auser.alot_mo_acnt_base where bsn_div_cd = '03';
select * from auser.alot_mo_acnt_base where bsn_div_cd = '93' and bank_cd = '020';

-- 우리은행 지급 모계좌 등록
select * from
--update
auser.alot_mo_acnt_base
--set bsn_div_cd = '03'
where bsn_div_cd = '93' and bank_cd = '020';
--commit;

-- 우리은행 지급 모계좌 원복
select * from
--update
auser.alot_mo_acnt_base
--set bsn_div_cd = '93'
where bsn_div_cd = '03' and bank_cd = '020';
--commit;

-- 11122300001 11122300002 11122300003 11122300005 11122300006 11122300007
select * from auser.alot_loan_onln_pay
where tran_proc_dt = '2011-12-23'
;


select * from auser.alot_loan_onln_pay --@hiplus_link
where tran_proc_dt = '2011-12-27'
    and tr_bank_cd = '23'
;

select tr_bank_cd, count(*) from auser.alot_loan_onln_pay --@hiplus_link
--where tran_proc_dt = '2011-12-27'
group by tr_bank_cd
;

select * from auser.alot_loan_pay_desc --@hiplus_link
where rqst_dt = '2011-12-27'
;

select * from auser.alot_loan_base
where ( loan_no, loan_seq)  in (select loan_no, loan_seq from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27')
;

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
--    and p.proc_stat_cd = '01'
--    and p.loan_no =  '11122200078'-- 2 '11122300121' -- 1 '11122300046'
--    and
--    p.pay_no = '11122600000001'
order by pay_no desc
;


select * from BUSER.BVAT_TR_BANK_DESC; --@hiplus_link;


select
    p.proc_stat_cd, p.*
from auser.alot_loan_onln_pay p
--update 
--    auser.alot_loan_onln_pay p
--set proc_stat_cd = '01'
where
    1 =1 
    and
    tran_proc_dt = '2011-12-27'
    and proc_stat_cd = '01'
--    and
--    pay_no = '11122600000001'  --우리
--    pay_no = '11122600000004'  --신한
--group by
--    p.proc_stat_cd
;
--commit;

select p.proc_stat_cd, count(*)
from auser.alot_loan_onln_pay p
where
    tran_proc_dt = '2011-12-27'
--    and
--    pay_no = '11122600000001'
group by
    p.proc_stat_cd
;

    

select p.proc_stat_cd, p.*
from auser.alot_loan_onln_pay p
where
    tran_proc_dt = '2011-12-27'
    and
--    pay_no = '11122600000001' -- 우리
    pay_no = '11122600000004' -- 신한
;

update auser.alot_loan_onln_pay p
set proc_stat_cd = '01'
--select p.proc_stat_cd, p.*
--from auser.alot_loan_onln_pay p
where
    tran_proc_dt = '2011-12-27'
    and
    pay_no = '11122600000001' -- 우리
--    pay_no = '11122600000004' -- 신한
;

--commit;

select * from auser.alot_loan_pay_desc d;

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
    1 =1
    and tran_proc_dt = '2011-12-27'
--    and pay_no = '11122600000001'
);

--commit;

-- 가상계좌로 찾기
-- 신한 - 56~~~
-- 우리 - 26~~~
select * from auser.alot_loan_base
where img_acnt_no = '56201554000203'
;

select * from auser.alot_loan_base
where img_acnt_no in ('26665004518931', '26665004518923', '26665004518972', '56201550661748')
;
select * from auser.alot_loan_base where cont_man_no = '6304271841416'
;

select * from auser.alot_loan_base where cont_man_no = '5801280008051'
;

select * from auser.actt_cust_base
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

;

-- 가상계좌 입금
SELECT * FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE tr_dt = '2011-12-27' --AND TR_NO = ?  ;
--WHERE TR_ORG_CD = 'C1004' and tr_dt = '2011-11-30'; --AND TR_NO = ?  ;
order by tr_no
;

SELECT CNCL_FG, count(*) FROM BUSER.BVAT_CMS_IAMT_DESC --
group by CNCL_FG
;

SELECT * FROM BUSER.BVAT_CMS_IAMT_DESC --
where CNCL_FG = '1'
;
