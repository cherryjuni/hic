-- 청구입금 - com.cabis.sc.b.cp.cm
-- CmsRecvGramCC_수취조회

-- 수취조회

-- 거래기관조회
SELECT
	TR_ORG_CD
FROM BUSER.BVAT_TR_ORG_BASE
WHERE RCPT_TP_CD = ? AND BANK_CD = '0'||?
;

SELECT
	*
FROM BUSER.BVAT_TR_ORG_BASE
;

--//가상계좌고객명조회
select rtrim(ltrim(substr(b.cust_nm,1,16)))||'-하이' CUST_NM
from buser.bgmt_comm_dummy d
    left outer join auser.alot_loan_base a
    on 1 = 1
        --        and a.img_acnt_bank_cd = '0'||?
        and a.img_acnt_no = trim(?)
        and a.loan_last_fg = '1'
        and substr(a.loan_stat_cd,1,1) = '2'
    left outer join auser.actt_cust_base b
    on b.cust_no = a.cont_man_no

where d.no = 1
;

select rtrim(ltrim(substr(b.cust_nm,1,16)))||'-하이' CUST_NM
from buser.bgmt_comm_dummy d
    left outer join auser.alot_loan_base a
    on 1 = 1
        and a.img_acnt_bank_cd = '0'||'26'
        and a.img_acnt_no = trim('56201550661748')
        and a.loan_last_fg = '1'
        and substr(a.loan_stat_cd,1,1) = '2'
    left outer join auser.actt_cust_base b
    on b.cust_no = a.cont_man_no
where d.no = 1
;

select *
from buser.bgmt_comm_dummy
;

select img_acnt_no, count(*)
from auser.alot_loan_base a
where loan_last_fg = '1'
and loan_stat_cd = '22'
group by img_acnt_no
having count(*) > 1
;

-- 원거래조회
-- 거래기관코드 / 거래번호
SELECT
	SETL_FG
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE TR_ORG_CD = ? AND TR_NO = ?
;
SELECT
	*
FROM BUSER.BVAT_CMS_IAMT_DESC
;


--CMS원거래취소 UPDATE
--UPDATE BUSER.BVAT_CMS_IAMT_DESC
SET
    ORG_TR_DT = ?,
    ORG_TR_SNUM = ?,
    CNCL_RECV_DT = ?,
    CNCL_RECV_TM =?,
    CNCL_FG = ?,
    CNCL_DT =?,
    LAST_PROC_DT=?,
    LAST_PROC_TM=?,
    LAST_PROC_EMP_NO=?
WHERE
    TR_ORG_CD = ? AND TR_NO = ?
;

--대출정보 조회
SELECT
    CASE WHEN SUBSTR(B.LOAN_STAT_CD,1,1) = '2' 
        THEN LOAN_NO                            
        WHEN SUBSTR(A.LOAN_STAT_CD,1,1) = '3'   
        THEN '344'                            
    ELSE null  END    AS LOAN_NO                  
    ,NVL(B.LOAN_SEQ,'01')               LOAN_SEQ          
    ,NVL(B.MNG_DEPT_CD, A.MNG_DEPT_CD)  MNG_DEPT_CD     
FROM  BUSER.BVAT_UNCNFM_BASE A        
    ,AUSER.ALOT_LOAN_BASE    B                           
WHERE DIV_CD           = ?         /* '1'              */    
    AND IMG_ACNT_NO  = ?         /* :입금내역.계좌번호   */    
    AND IMG_ACNT_BANK_CD = '0'||?    /* :입금내역.은행_코드 */    
    AND LOAN_LAST_FG     = ?         /* '1'              */    ;
;

-- 대출정보조회
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
       , img_acnt_no
       , loan_stat_cd
       , a.*
FROM   AUSER.ALOT_LOAN_BASE a
WHERE                    --                                      대출종료  
       IMG_ACNT_NO      in ('56201554000203', '56201550661748', '56201553000020') --?                  /* :입금내역.계좌번호   */
--       AND IMG_ACNT_BANK_CD = '0'||?         /* :입금내역.은행_코드 */
--       AND LOAN_STAT_CD IN ('22', '29')      /* 대출중인 계좌만         */
       AND LOAN_LAST_FG     = '1'            /* '1'              */
;

select * from BUSER.BVAT_UNCNFM_BASE;

select *
FROM   AUSER.ALOT_LOAN_BASE
WHERE
       IMG_ACNT_NO      = '56201550661748' --?                  /* :입금내역.계좌번호   */
--       AND IMG_ACNT_BANK_CD = '0'||?         /* :입금내역.은행_코드 */
       AND LOAN_STAT_CD IN ('22', '29')      /* 대출중인 계좌만         */
       AND LOAN_LAST_FG     = '1'            /* '1'              */
;

--CMS거래내역 등록
--INSERT INTO BUSER.BVAT_CMS_IAMT_DESC(
	TR_ORG_CD, TR_NO, TR_DT, TR_SNUM, RCPT_TP_CD,
	BANK_CD, TR_AMT, ACNT_NO, DEPO_OWN_NO, DEPO_OWNNM,
	IAMT_RSLT_CD, RECV_DT, RECV_TM, SETL_FG, SETL_NO,
	CNCL_FG, ORG_TR_DT, ORG_TR_SNUM, CNCL_DT, CNCL_RECV_DT,
	CNCL_RECV_TM, CNCL_SETL_FG, CNCL_SETL_NO, LOAN_NO, LOAN_SEQ,
	MNG_DEPT_CD, UN_CNFM_FG, FRST_REG_DT, FRST_REG_TM, FRST_REG_EMP_NO,
	LAST_PROC_DT, LAST_PROC_TM, LAST_PROC_EMP_NO, BANK_BRNCH_CD, TRMN_CD
) VALUES (
	?, ?, ?, ?, ?,
	?, ?, ?, ?, ?,
	?, ?, ?, ?, ?,
	?, ?, ?, ?, ?,
	?, ?, ?, ?, ?,
	?, ?, ?, ?, ?,
	?, ?, ?, ?, ?
)
;

-- 결과코드-명 얻기
SELECT
	CD_DESC_KOR_NM
FROM GUSER.GBCT_COMM_CD_DESC
WHERE CD_KIND_NO = ? AND CD_DESC_NO = ?
;

