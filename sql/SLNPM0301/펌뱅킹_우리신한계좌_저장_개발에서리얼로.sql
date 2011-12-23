-- 
-- 우리은행가상계좌 100개를 테스트계좌로 사용하기 위해 미리 사용플래그를 '1'로 세팅
-- 

-- 사용안한 가상계좌가져오기
SELECT IMG_ACNT_NO              
FROM (                           
    SELECT IMG_ACNT_NO              
    FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC 
    WHERE IMG_ACNT_BANK_CD = ?         
    AND USE_FG = '0'                   
    AND DISU_DT IS NULL                
)                                  
WHERE ROWNUM = 1                   
;

;

select * from AUSER.ALOT_LOAN_IMG_ACNT_DESC
;

select img_acnt_bank_cd, use_fg, count(*) from AUSER.ALOT_LOAN_IMG_ACNT_DESC
group by img_acnt_bank_cd, use_fg;
select img_acnt_bank_cd, use_fg, count(*) from AUSER.ALOT_LOAN_IMG_ACNT_DESC@hiplus_link
group by img_acnt_bank_cd, use_fg;

--RNUM	IMG_ACNT_BANK_CD	IMG_ACNT_NO
--  1	020	26665004518923
--  8	020	26665004518999
--  9	020	26665005118002
--100	020	26665005118912
select rownum rnum, a.* from (
select a.* from AUSER.ALOT_LOAN_IMG_ACNT_DESC a
where img_acnt_bank_cd = '020'
order by img_acnt_bank_cd, img_acnt_no
) a
;

select * from auser.alot_loan_img_acnt_desc
where img_acnt_bank_cd = '020'
    and img_acnt_no > '26665005118912'
    and use_fg = '1'
;


--insert into auser.alot_loan_img_acnt_desc@hiplus_link
select * from auser.alot_loan_img_acnt_desc where img_acnt_bank_cd = '020' order by img_acnt_bank_cd, img_acnt_no
;

--select * from auser.alot_loan_img_acnt_desc@hiplus_link
--update auser.alot_loan_img_acnt_desc@hiplus_link
--set use_fg = '0'
where img_acnt_bank_cd = '020'
    and img_acnt_no > '26665005118912'
    and use_fg = '1'
;

--commit;
--rollback;


-- 리얼과 테스트 테이블 비교
select * from GUSER.GBCT_COMM_CD_DESC@hiplus_link where CD_KIND_NO = 'R00038'
union
select * from GUSER.GBCT_COMM_CD_DESC where CD_KIND_NO = 'R00038'
;


