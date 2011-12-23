SELECT A.CMNG_NO ,
       A.CMNG_NO1 ,
       A.CMNG_NO2 ,
       A.ACCT_CD ,
       A.DC_DIV_CD ,
       A.MNG_NO_DIV_CD ,
       A.ACCT_DT ,
       A.ACCT_DEPT_CD ,
       A.TR_TP_CD ,
       A.TR_STAG_CD ,
       A.TRAN_TP_CD ,
       A.TRAN_NO ,
       A.MNG_DEPT_CD ,
       A.VOCH_AMT ,
       CASE
         WHEN A.ACCT_CD='23' THEN A.MNG_DEPT_CD
         WHEN A.ACCT_CD='14' THEN A.MNG_DEPT_CD
         ELSE A.MNG_NO
       END MNG_NO ,
       A.LOAN_NO ,
       A.LOAN_SEQ ,
       A.CNT-1 CNT
FROM   (SELECT B.CMNG_NO ,
               B.CMNG_NO1 ,
               B.CMNG_NO2 ,
               A.ACCT_CD ,
               'D' DC_DIV_CD ,
               (SELECT C.MNG_NO_DIV_CD
                FROM   EUSER.EACT_ACCT C
                WHERE  A.ACCT_CD=C.ACCT_CD
                AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4)) MNG_NO_DIV_CD ,
               MAX(A.ACCT_DT) ACCT_DT ,
               MAX(A.ACCT_DEPT_CD) ACCT_DEPT_CD ,
               MAX(A.TR_TP_CD) TR_TP_CD ,
               MAX(A.TR_STAG_CD) TR_STAG_CD ,
               MAX(A.TRAN_TP_CD) TRAN_TP_CD ,
               MAX(A.TRAN_NO) TRAN_NO ,
               MAX(A.MNG_DEPT_CD) MNG_DEPT_CD ,
               SUM(A.VOCH_AMT) VOCH_AMT ,
                       CASE (SELECT C.MNG_NO_DIV_CD
                FROM   EUSER.EACT_ACCT C
                WHERE  A.ACCT_CD=C.ACCT_CD
                AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4))
                         WHEN '02' THEN (SELECT C.DEPO_NO
                FROM   EUSER.EFDT_DEPO_BASE C
                WHERE  REPLACE(C.ACNT_NO, '-', '')=REPLACE(B.CMNG_NO2, '-', ''))
                         WHEN '06' THEN B.CMNG_NO1
                         WHEN '08' THEN '20'
                         ELSE ''
                       END MNG_NO ,
               MAX(B.LOAN_NO) LOAN_NO ,
               MAX(B.LOAN_SEQ) LOAN_SEQ ,
               COUNT(B.LOAN_NO) CNT
        FROM   BUSER.BCLT_CLS_SUB_DESC A ,
               (SELECT A.TR_DESC_NO ,
                       MAX(
                                                       CASE
                                                         WHEN A.DC_DIV_CD='C' AND    A.ACCT_CD='23' THEN A.MNG_DEPT_CD
                                                         ELSE ''
                                                       END) CMNG_NO ,
                       MAX(
                                                       CASE
                                                         WHEN A.DC_DIV_CD='D' AND    A.ACCT_CD='14' THEN A.MNG_DEPT_CD
                                                         ELSE ''
                                                       END) CMNG_NO1 ,
                       MAX(
                                                       CASE
                                                         WHEN A.DC_DIV_CD='D' AND    A.ACCT_CD='11010101' THEN A.ACNT_NO
                                                         ELSE ''
                                                       END) CMNG_NO2 ,
                       MAX(A.LOAN_NO) LOAN_NO ,
                       MAX(A.LOAN_SEQ) LOAN_SEQ
                FROM   BUSER.BCLT_CLS_BASE C,
                       BUSER.BCLT_CLS_SUB_DESC A
                WHERE  C.ACCT_DT=?acctDt
                AND    C.ACCT_DEPT_CD=?acctDeptCd
                AND    C.TRAN_TP_CD=?tranTpCd
                AND    C.TRAN_NO=?tranNo
                AND    C.ACCT_UNIT_CD=?acctUnitCd
                AND    C.ACCT_DT=A.ACCT_DT
                AND    C.ACCT_DEPT_CD=A.ACCT_DEPT_CD
                AND    C.TRAN_TP_CD=A.TRAN_TP_CD
                AND    C.TRAN_NO=A.TRAN_NO
                GROUP BY A.TR_DESC_NO) B
        WHERE  A.TR_DESC_NO=B.TR_DESC_NO
        AND    A.ACCT_DT=?acctDt
        AND    A.ACCT_DEPT_CD=?acctDeptCd
        AND    A.TRAN_TP_CD=?tranTpCd
        AND    A.TRAN_NO=?tranNo
        AND    A.ACCT_UNIT_CD=?acctUnitCd
        AND    A.DC_DIV_CD='D'
        GROUP BY B.CMNG_NO, B.CMNG_NO1, B.CMNG_NO2, A.ACCT_CD
        UNION
SELECT B.CMNG_NO,
               B.CMNG_NO1,
               B.CMNG_NO2,
               A.ACCT_CD,
               'C' DC_DIV_CD ,
               (SELECT C.MNG_NO_DIV_CD
                FROM   EUSER.EACT_ACCT C
                WHERE  A.ACCT_CD=C.ACCT_CD
                AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4)) MNG_NO_DIV_CD ,
               MAX(A.ACCT_DT) ACCT_DT,
               MAX(A.ACCT_DEPT_CD) ACCT_DEPT_CD,
               MAX(A.TR_TP_CD) TR_TP_CD,
               MAX(A.TR_STAG_CD) TR_STAG_CD ,
               MAX(A.TRAN_TP_CD) TRAN_TP_CD,
               MAX(A.TRAN_NO) TRAN_NO,
               MAX(A.MNG_DEPT_CD) MNG_DEPT_CD,
               SUM(A.VOCH_AMT) VOCH_AMT ,
                       CASE (SELECT C.MNG_NO_DIV_CD
                FROM   EUSER.EACT_ACCT C
                WHERE  A.ACCT_CD=C.ACCT_CD
                AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4))
                         WHEN '02' THEN (SELECT C.DEPO_NO
                FROM   EUSER.EFDT_DEPO_BASE C
                WHERE  REPLACE(C.ACNT_NO, '-', '')=REPLACE(B.CMNG_NO2, '-', ''))
                         WHEN '06' THEN B.CMNG_NO1
                         WHEN '08' THEN '20'
                         ELSE ''
                       END MNG_NO ,
               MAX(B.LOAN_NO) LOAN_NO ,
               MAX(B.LOAN_SEQ) LOAN_SEQ ,
               COUNT(B.LOAN_NO) CNT
        FROM   BUSER.BCLT_CLS_SUB_DESC A ,
               (SELECT A.TR_DESC_NO ,
                       MAX(
                                                       CASE
                                                         WHEN A.DC_DIV_CD='C'
                        AND    A.ACCT_CD='23' THEN A.MNG_DEPT_CD
                                                         ELSE ''
                                                       END) CMNG_NO ,
                       MAX(
                                                       CASE
                                                         WHEN A.DC_DIV_CD='D'
                        AND    A.ACCT_CD='14' THEN A.MNG_DEPT_CD
                                                         ELSE ''
                                                       END) CMNG_NO1 ,
                       MAX(
                                                       CASE
                                                         WHEN A.DC_DIV_CD='D'
                        AND    A.ACCT_CD='11010103' THEN A.ACNT_NO
                                                         ELSE ''
                                                       END) CMNG_NO2 ,
                       MAX(A.LOAN_NO) LOAN_NO ,
                       MAX(A.LOAN_SEQ) LOAN_SEQ
                FROM   BUSER.BCLT_CLS_BASE C ,
                       BUSER.BCLT_CLS_SUB_DESC A
                WHERE  C.ACCT_DT=?acctDt
                AND    C.ACCT_DEPT_CD=?acctDeptCd
                AND    C.TRAN_TP_CD=?tranTpCd
                AND    C.TRAN_NO=?tranNo
                AND    C.ACCT_UNIT_CD=?acctUnitCd
                AND    C.ACCT_DT=A.ACCT_DT
                AND    C.ACCT_DEPT_CD=A.ACCT_DEPT_CD
                AND    C.TRAN_TP_CD=A.TRAN_TP_CD
                AND    C.TRAN_NO=A.TRAN_NO
                GROUP BY A.TR_DESC_NO) B
        WHERE  A.TR_DESC_NO=B.TR_DESC_NO
        AND    A.ACCT_DT=?acctDt
        AND    A.ACCT_DEPT_CD=?acctDeptCd
        AND    A.TRAN_TP_CD=?tranTpCd
        AND    A.TRAN_NO=?tranNo
        AND    A.ACCT_UNIT_CD=?acctUnitCd
        AND    A.DC_DIV_CD='C'
        GROUP BY B.CMNG_NO, B.CMNG_NO1, B.CMNG_NO2, A.ACCT_CD) A
WHERE  A.VOCH_AMT!=0 ;
 
SELECT B.CMNG_NO ,
       B.CMNG_NO1 ,
       B.CMNG_NO2 ,
       A.ACCT_CD ,
       'D' DC_DIV_CD ,
       (SELECT C.MNG_NO_DIV_CD
        FROM   EUSER.EACT_ACCT C
        WHERE  A.ACCT_CD=C.ACCT_CD
        AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4)) MNG_NO_DIV_CD ,
       MAX(A.ACCT_DT) ACCT_DT ,
       MAX(A.ACCT_DEPT_CD) ACCT_DEPT_CD ,
       MAX(A.TR_TP_CD) TR_TP_CD ,
       MAX(A.TR_STAG_CD) TR_STAG_CD ,
       MAX(A.TRAN_TP_CD) TRAN_TP_CD ,
       MAX(A.TRAN_NO) TRAN_NO ,
       MAX(A.MNG_DEPT_CD) MNG_DEPT_CD ,
       SUM(A.VOCH_AMT) VOCH_AMT ,
       CASE (SELECT C.MNG_NO_DIV_CD
        FROM   EUSER.EACT_ACCT C
        WHERE  A.ACCT_CD=C.ACCT_CD
        AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4))
         WHEN '02' THEN (SELECT C.DEPO_NO
        FROM   EUSER.EFDT_DEPO_BASE C
        WHERE  REPLACE(C.ACNT_NO, '-', '')=REPLACE(B.CMNG_NO2, '-', ''))
         WHEN '06' THEN B.CMNG_NO1
         WHEN '08' THEN '20'
         ELSE ''
       END MNG_NO ,
       MAX(B.LOAN_NO) LOAN_NO ,
       MAX(B.LOAN_SEQ) LOAN_SEQ ,
       COUNT(B.LOAN_NO) CNT
FROM   BUSER.BCLT_CLS_SUB_DESC A ,
       (SELECT A.TR_DESC_NO ,
               MAX(
                                       CASE
                                         WHEN A.DC_DIV_CD='C' AND    A.ACCT_CD='23' THEN A.MNG_DEPT_CD
                                         ELSE ''
                                       END) CMNG_NO ,
               MAX(
                                       CASE
                                         WHEN A.DC_DIV_CD='D'AND    A.ACCT_CD='14' THEN A.MNG_DEPT_CD
                                         ELSE ''
                                       END) CMNG_NO1 ,
               MAX(
                                       CASE
                                         WHEN A.DC_DIV_CD='D'
                AND    A.ACCT_CD='11010101' THEN A.ACNT_NO
                                         ELSE ''
                                       END) CMNG_NO2 ,
               MAX(A.LOAN_NO) LOAN_NO ,
               MAX(A.LOAN_SEQ) LOAN_SEQ
        FROM   BUSER.BCLT_CLS_BASE C,
               BUSER.BCLT_CLS_SUB_DESC A
        WHERE  C.ACCT_DT=?acctDt
        AND    C.ACCT_DEPT_CD=?acctDeptCd
        AND    C.TRAN_TP_CD=?tranTpCd
        AND    C.TRAN_NO=?tranNo
        AND    C.ACCT_UNIT_CD=?acctUnitCd
        AND    C.ACCT_DT=A.ACCT_DT
        AND    C.ACCT_DEPT_CD=A.ACCT_DEPT_CD
        AND    C.TRAN_TP_CD=A.TRAN_TP_CD
        AND    C.TRAN_NO=A.TRAN_NO
        GROUP BY A.TR_DESC_NO) B
WHERE  A.TR_DESC_NO=B.TR_DESC_NO
AND    A.ACCT_DT=?acctDt
AND    A.ACCT_DEPT_CD=?acctDeptCd
AND    A.TRAN_TP_CD=?tranTpCd
AND    A.TRAN_NO=?tranNo
AND    A.ACCT_UNIT_CD=?acctUnitCd
AND    A.DC_DIV_CD='D'
GROUP BY B.CMNG_NO, B.CMNG_NO1, B.CMNG_NO2, A.ACCT_CD
UNION
SELECT B.CMNG_NO,
       B.CMNG_NO1,
       B.CMNG_NO2,
       A.ACCT_CD,
       'C' DC_DIV_CD ,
       (SELECT C.MNG_NO_DIV_CD
        FROM   EUSER.EACT_ACCT C
        WHERE  A.ACCT_CD=C.ACCT_CD
        AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4)) MNG_NO_DIV_CD ,
       MAX(A.ACCT_DT) ACCT_DT,
       MAX(A.ACCT_DEPT_CD) ACCT_DEPT_CD,
       MAX(A.TR_TP_CD) TR_TP_CD,
       MAX(A.TR_STAG_CD) TR_STAG_CD ,
       MAX(A.TRAN_TP_CD) TRAN_TP_CD,
       MAX(A.TRAN_NO) TRAN_NO,
       MAX(A.MNG_DEPT_CD) MNG_DEPT_CD,
       SUM(A.VOCH_AMT) VOCH_AMT ,
       CASE (SELECT C.MNG_NO_DIV_CD
        FROM   EUSER.EACT_ACCT C
        WHERE  A.ACCT_CD=C.ACCT_CD
        AND    C.ACCT_YY=SUBSTR(?acctDt, 1, 4))
         WHEN '02' THEN (SELECT C.DEPO_NO
        FROM   EUSER.EFDT_DEPO_BASE C
        WHERE  REPLACE(C.ACNT_NO, '-', '')=REPLACE(B.CMNG_NO2, '-', ''))
         WHEN '06' THEN B.CMNG_NO1
         WHEN '08' THEN '20'
         ELSE ''
       END MNG_NO ,
       MAX(B.LOAN_NO) LOAN_NO ,
       MAX(B.LOAN_SEQ) LOAN_SEQ ,
       COUNT(B.LOAN_NO) CNT
FROM   BUSER.BCLT_CLS_SUB_DESC A ,
       (SELECT A.TR_DESC_NO ,
               MAX(
                                       CASE
                                         WHEN A.DC_DIV_CD='C' AND    A.ACCT_CD='23' THEN A.MNG_DEPT_CD
                                         ELSE ''
                                       END) CMNG_NO ,
               MAX(
                                       CASE
                                         WHEN A.DC_DIV_CD='D' AND    A.ACCT_CD='14' THEN A.MNG_DEPT_CD
                                         ELSE ''
                                       END) CMNG_NO1 ,
               MAX(
                                       CASE
                                         WHEN A.DC_DIV_CD='D' AND    A.ACCT_CD='11010103' THEN A.ACNT_NO
                                         ELSE ''
                                       END) CMNG_NO2 ,
               MAX(A.LOAN_NO) LOAN_NO ,
               MAX(A.LOAN_SEQ) LOAN_SEQ
        FROM   BUSER.BCLT_CLS_BASE C ,
               BUSER.BCLT_CLS_SUB_DESC A
        WHERE  C.ACCT_DT=?acctDt
        AND    C.ACCT_DEPT_CD=?acctDeptCd
        AND    C.TRAN_TP_CD=?tranTpCd
        AND    C.TRAN_NO=?tranNo
        AND    C.ACCT_UNIT_CD=?acctUnitCd
        AND    C.ACCT_DT=A.ACCT_DT
        AND    C.ACCT_DEPT_CD=A.ACCT_DEPT_CD
        AND    C.TRAN_TP_CD=A.TRAN_TP_CD
        AND    C.TRAN_NO=A.TRAN_NO
        GROUP BY A.TR_DESC_NO) B
WHERE  A.TR_DESC_NO=B.TR_DESC_NO
AND    A.ACCT_DT=?acctDt
AND    A.ACCT_DEPT_CD=?acctDeptCd
AND    A.TRAN_TP_CD=?tranTpCd
AND    A.TRAN_NO=?tranNo
AND    A.ACCT_UNIT_CD=?acctUnitCd
AND    A.DC_DIV_CD='C'
GROUP BY B.CMNG_NO, B.CMNG_NO1, B.CMNG_NO2, A.ACCT_C
;