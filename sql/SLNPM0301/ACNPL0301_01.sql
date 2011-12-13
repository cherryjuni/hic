
-- searchHpgOnlnAutoLoanAppl
SELECT CNSL_DT
            ,CNSL_TM
            ,CUST_NO
            ,CASE WHEN TO_NUMBER(SUBSTR(TO_CHAR(SYSDATE,'YYYYMMDD'),5,4)) - TO_NUMBER(SUBSTR(CUST_NO,3,4))  > 0 THEN TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) - TO_NUMBER(DECODE(SUBSTR(CUST_NO,7,1),'1','19','2','19','3','20','4','20')||SUBSTR(CUST_NO,0,2))  - 0
                  WHEN TO_NUMBER(SUBSTR(TO_CHAR(SYSDATE,'YYYYMMDD'),5,4)) - TO_NUMBER(SUBSTR(CUST_NO,3,4)) <= 0 THEN TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) - TO_NUMBER(DECODE(SUBSTR(CUST_NO,7,1),'1','19','2','19','3','20','4','20')||SUBSTR(CUST_NO,0,2))  - 1
             END AS AGE
            ,A.PRDT_CD
            ,APPL_AMT*10000 AS APPL_AMT
            ,ACPT_PATH_CD
            ,RES_KIND_CD
            ,RES_STAT_CD
            ,INMT_PERS_CNT
            ,OWN_DIV_CD
            ,SETL_DD
            ,CUST_NM
            ,SELF_CERT_FG
            ,OFFC_NM
            ,ADDR_DIV_CD_01
            ,ZIP_CODE_01
            ,ADDR_DESC_01
            ,ADDR_SPEC_01
            ,ADDR_DIV_CD_02
            ,ZIP_CODE_02
            ,ADDR_DESC_02
            ,ADDR_SPEC_02
            ,ADDR_DIV_CD_03
            ,ZIP_CODE_03
            ,ADDR_DESC_03
            ,ADDR_SPEC_03
            ,ADDR_DIV_CD_04
            ,ZIP_CODE_04
            ,ADDR_DESC_04
            ,ADDR_SPEC_04
            ,CNTC_PLC_DIV_CD_02
            ,CNTC_PLC_TEL1_02
            ,CNTC_PLC_TEL2_02
            ,CNTC_PLC_TEL3_02
            ,CNTC_PLC_DIV_CD_03
            ,CNTC_PLC_TEL1_03
            ,CNTC_PLC_TEL2_03
            ,CNTC_PLC_TEL3_03
            ,A.JOB_DIV_CD1
            ,A.JOB_DIV_CD2
            ,YY_INCO_AMT*10000 AS YY_INCO_AMT
            ,PAY_BANK_CD
            ,PAY_ACNT_NO
            ,PAY_DEPO_OWN_NM
            ,PAY_DEPO_OWN_FG
            ,NPS_FG
            ,LOAN_STAT_FG
            ,SUBSTR(B.PRDT_CLAS_CD,1,1) AS PRDT_LRGE_CLAS_CD
            ,SUBSTR(B.PRDT_CLAS_CD,2,2) AS PRDT_MID_CLAS_CD
            ,SUBSTR(B.PRDT_CLAS_CD,4,1) AS PRDT_SM_CLAS_CD
            ,B.NORM_RT3
            ,B.DLY_RT3
            ,B.RMBR_FRML_CD
            ,B.MAX_LIM_AMT
 FROM   NUSER.HPG_ONLN_AUTO_LOAN_APPL@HOMEDB1 A
        LEFT OUTER JOIN AUSER.APDT_PRDT_BASE B
        ON A.PRDT_CD = B.PRDT_CD
 WHERE  CNSL_DT = ?
 AND    CNSL_TM = ?
 AND    CUST_NO = ?
 AND    NPS_FG = 'Y'
 ;


SELECT CNSL_DT
            ,CNSL_TM
            ,CUST_NO
            ,CASE WHEN TO_NUMBER(SUBSTR(TO_CHAR(SYSDATE,'YYYYMMDD'),5,4)) - TO_NUMBER(SUBSTR(CUST_NO,3,4))  > 0 THEN TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) - TO_NUMBER(DECODE(SUBSTR(CUST_NO,7,1),'1','19','2','19','3','20','4','20')||SUBSTR(CUST_NO,0,2))  - 0
                  WHEN TO_NUMBER(SUBSTR(TO_CHAR(SYSDATE,'YYYYMMDD'),5,4)) - TO_NUMBER(SUBSTR(CUST_NO,3,4)) <= 0 THEN TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) - TO_NUMBER(DECODE(SUBSTR(CUST_NO,7,1),'1','19','2','19','3','20','4','20')||SUBSTR(CUST_NO,0,2))  - 1
             END AS AGE
            ,A.PRDT_CD
            ,APPL_AMT*10000 AS APPL_AMT
            ,ACPT_PATH_CD
            ,RES_KIND_CD
            ,RES_STAT_CD
            ,INMT_PERS_CNT
            ,OWN_DIV_CD
            ,SETL_DD
            ,CUST_NM
            ,SELF_CERT_FG
            ,OFFC_NM
            ,ADDR_DIV_CD_01
            ,ZIP_CODE_01
            ,ADDR_DESC_01
            ,ADDR_SPEC_01
            ,ADDR_DIV_CD_02
            ,ZIP_CODE_02
            ,ADDR_DESC_02
            ,ADDR_SPEC_02
            ,ADDR_DIV_CD_03
            ,ZIP_CODE_03
            ,ADDR_DESC_03
            ,ADDR_SPEC_03
            ,ADDR_DIV_CD_04
            ,ZIP_CODE_04
            ,ADDR_DESC_04
            ,ADDR_SPEC_04
            ,CNTC_PLC_DIV_CD_02
            ,CNTC_PLC_TEL1_02
            ,CNTC_PLC_TEL2_02
            ,CNTC_PLC_TEL3_02
            ,CNTC_PLC_DIV_CD_03
            ,CNTC_PLC_TEL1_03
            ,CNTC_PLC_TEL2_03
            ,CNTC_PLC_TEL3_03
            ,A.JOB_DIV_CD1
            ,A.JOB_DIV_CD2
            ,YY_INCO_AMT*10000 AS YY_INCO_AMT
            ,PAY_BANK_CD
            ,PAY_ACNT_NO
            ,PAY_DEPO_OWN_NM
            ,PAY_DEPO_OWN_FG
            ,NPS_FG
            ,LOAN_STAT_FG
            ,SUBSTR(B.PRDT_CLAS_CD,1,1) AS PRDT_LRGE_CLAS_CD
            ,SUBSTR(B.PRDT_CLAS_CD,2,2) AS PRDT_MID_CLAS_CD
            ,SUBSTR(B.PRDT_CLAS_CD,4,1) AS PRDT_SM_CLAS_CD
            ,B.NORM_RT3
            ,B.DLY_RT3
            ,B.RMBR_FRML_CD
            ,B.MAX_LIM_AMT
FROM   NUSER.HPG_ONLN_AUTO_LOAN_APPL@HOMEDB1 A
       LEFT OUTER JOIN AUSER.APDT_PRDT_BASE B
       ON A.PRDT_CD = B.PRDT_CD
WHERE  CNSL_DT = '2011-12-09' --'2011-11-11' --?CNSL_DT
AND    CNSL_TM = '074956' --?
AND    CUST_NO = '6508081252016' --?
AND    NPS_FG = 'Y'
;

 
SELECT * FROM NUSER.HPG_ONLN_AUTO_LOAN_APPL@HOMEDB1 A
;

SELECT *
FROM   AUSER.ACNT_EXAM_PRGS_BASE
WHERE  RESI_NO = '6508081252016'
AND    APPL_DT = '2011-12-09' --'2011-11-11'
AND    APPL_TM = '074956'   ;

SELECT COUNT(*) AS CNT
FROM   AUSER.ACNT_EXAM_PRGS_BASE
WHERE  RESI_NO = '6508081252016'
AND    APPL_DT = '2011-11-11'
AND    APPL_TM = '074956'   ;

SELECT COUNT(*) AS CNT
FROM   AUSER.ACNT_EXAM_PRGS_BASE
WHERE  RESI_NO = '"+ acnplObj.getCustNo()       +"'
AND    APPL_DT = '"+ acnplObj.getCnslDt()       +"'
AND    APPL_TM = '"+ acnplObj.getCnslTm()       +"'   ;



SELECT /*+ USE_NL(A B) */
      A.CNSL_NO
     ,A.RESI_NO
     ,A.AGE
     ,A.RJCT_DT
     ,A.RJCT_RSN_CD1
     ,NVL(B.RESI_ZONE_CD,'')
     ,A.CUST_NM
     ,NVL(B.ACPT_PATH_CD,'')
     ,NVL(B.APPL_CMPL_YN,'')
     ,NVL(B.JOB_DIV_CD1,'')
     ,NVL(B.JOB_DIV_CD2,'')
     ,NVL(B.SALR_FORM,'')
     ,NVL(B.YY_INCO_AMT,'')
     ,NVL(C.RES_STAT_CD,'')
     ,NVL(C.RES_KIND_CD,'')
     ,NVL(C.OWN_DIV_CD,'')
     ,NVL(C.INMT_PERS_CNT_CD,'')
     ,NVL(C.HIRE_DT,'')
     ,NVL(C.WORK_BIZ_YYCNT,0)
     ,NVL(C.WORK_BIZ_MM_CNT,0)
     ,NVL(A.AGENT_NO,'')
     ,NVL(C.OUTS_MNG_NO,'')
     ,NVL(A.OUTS_APPL_NO,'')
     ,NVL(D.MEMB_NO,'')
FROM AUSER.ACNT_EXAM_PRGS_BASE A
LEFT OUTER JOIN
     AUSER.ACNT_OUTS_LOAN_APPL_TEMP C
  ON A.OUTS_APPL_NO = C.OUTS_APPL_NO
LEFT OUTER JOIN
     AUSER.ACTT_CUST_BASE D
  ON A.RESI_NO = D.CUST_NO
    ,AUSER.ACNT_CNSL_BASE      B
WHERE A.CNSL_NO = B.CNSL_NO
AND A.EXAM_PRGS_STAT_CD = '01'
AND B.APPL_CMPL_YN      = '1'
AND B.CNSL_PRDT_DIV_CD  = '1'
AND LENGTH(TRIM(A.RESI_NO)) = 13
AND A.CNSL_NO = ?
;

SELECT /*+ USE_NL(A B) */
      A.CNSL_NO
     ,A.RESI_NO
     ,A.AGE
     ,A.RJCT_DT
     ,A.RJCT_RSN_CD1
     ,NVL(B.RESI_ZONE_CD,'')
     ,A.CUST_NM
     ,NVL(B.ACPT_PATH_CD,'')
     ,NVL(B.APPL_CMPL_YN,'')
     ,NVL(B.JOB_DIV_CD1,'')
     ,NVL(B.JOB_DIV_CD2,'')
     ,NVL(B.SALR_FORM,'')
     ,NVL(B.YY_INCO_AMT,'')
     ,NVL(C.RES_STAT_CD,'')
     ,NVL(C.RES_KIND_CD,'')
     ,NVL(C.OWN_DIV_CD,'')
     ,NVL(C.INMT_PERS_CNT_CD,'')
     ,NVL(C.HIRE_DT,'')
     ,NVL(C.WORK_BIZ_YYCNT,0)
     ,NVL(C.WORK_BIZ_MM_CNT,0)
     ,NVL(A.AGENT_NO,'')
     ,NVL(C.OUTS_MNG_NO,'')
     ,NVL(A.OUTS_APPL_NO,'')
     ,NVL(D.MEMB_NO,'')
FROM AUSER.ACNT_EXAM_PRGS_BASE A
LEFT OUTER JOIN
     AUSER.ACNT_OUTS_LOAN_APPL_TEMP C
  ON A.OUTS_APPL_NO = C.OUTS_APPL_NO
LEFT OUTER JOIN
     AUSER.ACTT_CUST_BASE D
  ON A.RESI_NO = D.CUST_NO
    ,AUSER.ACNT_CNSL_BASE      B
WHERE A.CNSL_NO = B.CNSL_NO
AND A.EXAM_PRGS_STAT_CD = '01'
AND B.APPL_CMPL_YN      = '1'
AND B.CNSL_PRDT_DIV_CD  = '1'
AND LENGTH(TRIM(A.RESI_NO)) = 13
AND A.CNSL_NO = '2011121200001' --'6508081252016' --?
;
-- CNSL_NO 2011111100001
-- RESI_NO 6508081252016


-- 기거래정보 조회
SELECT MAX(LOAN_NO||LOAN_SEQ) LOAN_NO
         ,SUM(CASE WHEN LOAN_STAT_CD LIKE '2%' THEN LOAN_PAMT_RAMT ELSE 0 END ) LOAN_PAMT_RAMT
         ,MAX(LOAN_DT)
 FROM AUSER.ALOT_LOAN_BASE
WHERE CONT_MAN_NO = '6508081252016' --?
  AND PRDT_MID_CLAS_CD <> '11'
  AND ( LOAN_STAT_CD LIKE '3%' OR LOAN_STAT_CD LIKE '2%')
GROUP BY CONT_MAN_NO;


select * from AUSER.ALOT_LOAN_BASE
WHERE CONT_MAN_NO = '6508081252016' --?
;

select * 
FROM AUSER.ACNT_EXAM_PRGS_BASE A
where A.CNSL_NO = '2011111100001' --'6508081252016'
;


