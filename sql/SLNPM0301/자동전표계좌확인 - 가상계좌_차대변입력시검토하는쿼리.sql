-- AOO085 ��ü�����ڵ�

-- ���������ڵ�
SELECT *                                                                         
FROM GUSER.GBCT_COMM_CD_DESC Z                                                       
WHERE Z.CD_KIND_NO = 'R00012'   /* R00012 */                                                
--     AND Z.CD_DESC_NO = 'W2'
;

-- �������-�츮[R0320]
select * from 
EUSER.EACT_TR_ACCT_DESC
where tr_tp_div_cd ='R0320'
;

-- �������-����[R0030]
select * from 
EUSER.EACT_TR_ACCT_DESC
where tr_tp_div_cd ='R0030'
order by acct_cd, tr_stag_div_cd
;


-- ��꿡�� �߰� �ʿ�
-- ������ �����Ƿ�


select *
from 
(
select * from 
EUSER.EACT_TR_ACCT_DESC
where tr_tp_div_cd ='R0320'
) a,
(
select * from 
EUSER.EACT_TR_ACCT_DESC
where tr_tp_div_cd ='R0030'
) b
where
    a.acct_cd = b.acct_cd
    and a.tr_stag_div_cd = b.tr_stag_div_cd
    and a.dc_div_cd = a.dc_div_cd
    and a.amt_item_div_cd = b.amt_item_div_cd
    and 
;


select
    acct_cd, tr_stag_div_cd, dc_div_cd, amt_item_div_cd
from 
    EUSER.EACT_TR_ACCT_DESC
where tr_tp_div_cd ='R0030'
minus
select
    acct_cd, tr_stag_div_cd, dc_div_cd, amt_item_div_cd
from 
    EUSER.EACT_TR_ACCT_DESC
where tr_tp_div_cd ='R0320'
;
;
