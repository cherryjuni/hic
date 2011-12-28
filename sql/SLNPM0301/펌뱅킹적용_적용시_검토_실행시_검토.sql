-- �츮���� ���� ����� Ȯ��
select * from AUSER.ALOT_MO_ACNT_BASE;

-- �츮���� ���� ����� ���
--select * from
--update auser.alot_mo_acnt_base
set bsn_div_cd = '03'
where bsn_div_cd = '93' and bank_cd = '020';
--commit;


-- ���ް��� Ȯ��
--select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' order by pay_no desc;
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '01' order by pay_no desc;

-- ��������
select
--    *
    p.pay_no, p.rcgn_no, p.loan_no, p.loan_seq, p.pay_bsn_div_cd, p.proc_stat_cd, p.cust_nm, p.setl_bank_cd, p.setl_acnt_no
    , d.rqst_dt, pay_stat_cd
    , l.img_acnt_bank_cd, l.img_acnt_no
    , p.loan_pamt
    , decode(p.loan_pamt, d.loan_pamt,      '����', '����-���޻� �ݾ�')         p_loan_pamt
    , decode(p.loan_pamt, i.loan_pamt,      '����', '����-������ �ݾ�')           i_loan_pamt
    , decode(p.loan_pamt, l.loan_pamt,      '����', '����-���±⺻ ����ݾ�')     l_loan_pamt
    , decode(p.loan_pamt, l.loan_pamt_ramt, '����', '����-���±⺻ ����ݾ��ܾ�') l_loan_pamt_ramt
    , decode(p.loan_pamt, l.exec_amt,       '����', '����-���±⺻ ����ݾ�')     l_exec_amt
    , decode(p.loan_pamt, l.aprv_amt,       '����', '����-���±⺻ ���αݾ�')     l_aprv_amt
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
--    not (SETL_BANK_CD = '026' and SETL_ACNT_NO = '110314809090'  and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '�赿��')
--    not (SETL_BANK_CD = '020' and SETL_ACNT_NO = '1002934652109' and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '�赿��')
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
--    not (SETL_BANK_CD = '026' and SETL_ACNT_NO = '110314809090'  and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '�赿��')
--    not (SETL_BANK_CD = '020' and SETL_ACNT_NO = '1002934652109' and DEPO_OWN_NO  = '8401221024416' and DEPO_OWNNM   = '�赿��')
--    and not(LOAN_NO  = :loanNo AND LOAN_SEQ = :loanSeq)
    and tran_proc_dt = '2011-12-27' 
    and proc_stat_cd = '01'
;
--commit;

-- ������Ʈ ��, Ȯ��
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '01' order by pay_no desc;
select * from auser.alot_loan_onln_pay where tran_proc_dt = '2011-12-27' and proc_stat_cd = '09' order by pay_no desc;



-- �츮���� ���� ����� ����
--update auser.alot_mo_acnt_base
set bsn_div_cd = '93'
where bsn_div_cd = '03' and bank_cd = '020';
--commit;

-- ���ް��� ����
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

