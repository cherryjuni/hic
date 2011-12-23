-- 보조내역등록, 회계단위
--INSERT_SUB_DESC_WITH_ACCT_UNIT
----
--INSERT INTO BUSER.BCLT_CLS_SUB_DESC(
--    ACCT_DT
--    ,TR_DESC_NO
--    ,TR_SNUM
--    ,SUB_SNUM
--    ,ACCT_UNIT_CD
--    ,ACCT_DEPT_CD
--    ,TRAN_TP_CD
--    ,DC_DIV_CD
--    ,ACCT_CD
--    ,VOCH_AMT
--    ,TR_TP_CD
--    ,TR_STAG_CD
--    ,ISRT_DT
--    ,LOAN_NO
--    ,LOAN_SEQ
--    ,PRDT_LRGE_CLAS_CD
--    ,PRDT_MID_CLAS_CD
--    ,OCCR_DEPT_CD
--    ,MNG_DEPT_CD
--    ,BANK_CD
--    ,ACNT_NO
--    ,CPRT_COM_NO
--    ,FRST_REG_DT
--    ,FRST_REG_TM
--    ,FRST_REG_EMP_NO
--    ,LAST_PROC_DT
--    ,LAST_PROC_TM
--    ,LAST_PROC_EMP_NO
--    ,MNG_ACCT_UNIT_CD
--    ,OCCR_ACCT_UNIT_CD
--)
SELECT
    MAX(A.ACCT_DT)
    ,MAX(A.TR_DESC_NO)
    ,A.TR_SNUM
    ,ROW_NUMBER() OVER(ORDER BY A.TR_SNUM) AS ROW_NUM
    ,A.ACCT_UNIT_CD
    ,A.ACCT_DEPT_CD
    ,MAX(A.TRAN_TP_CD)
    ,A.DC_DIV_CD
    ,A.ACCT_CD
    ,SUM(B.AMT
     * CASE
         WHEN OP_DIV_CD = '+' THEN 1
         ELSE -1
       END)    VOCH_AMT
    ,MAX(A.TR_TP_CD)
    ,MAX(A.TR_STAG_CD)
    ,MAX(A.ISRT_DT)
    ,MAX(A.LOAN_NO)
    ,MAX(A.LOAN_SEQ)
    ,MAX(A.PRDT_LRGE_CLAS_CD)
    ,MAX(A.PRDT_MID_CLAS_CD)
    ,MAX(A.OCCR_DEPT_CD)
    ,MAX(A.MNG_DEPT_CD)
    ,MAX(A.BANK_CD)
    ,MAX(A.ACNT_NO)
    ,MAX(A.CPRT_COM_NO)  AS CPRT_COM_NO
    ,TO_CHAR(SYSDATE,'YYYY-MM-DD')        FRST_REG_DT
    ,TO_CHAR(SYSDATE,'HH24MISS')          FRST_REG_TM
    ,CAST( 'SLNPM031' AS CHAR(8))                  FRST_REG_EMP_NO
    ,TO_CHAR(SYSDATE,'YYYY-MM-DD')        LAST_PROC_DT
    ,TO_CHAR(SYSDATE,'HH24MISS')          LAST_PROC_TM
    ,CAST( 'SLNPM031' AS CHAR(8))                LAST_PROC_EMP_NO
    ,MAX(A.MNG_ACCT_UNIT_CD)
    ,MAX(A.OCCR_ACCT_UNIT_CD)
FROM
    ( SELECT
        A.ACCT_DT
        ,A.TR_DESC_NO
        ,A.TR_SNUM
        ,0                                 SUB_SNUM
        ,CASE
           WHEN A.TR_STAG_CD like '0%' THEN
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '2' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '3' THEN A.MNG_ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '4' THEN A.MNG_ACCT_UNIT_CD
               END
           ELSE
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '2' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '3' THEN A.ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '4' THEN A.ACCT_UNIT_CD
               END
        END ACCT_UNIT_CD
        ,CASE
            WHEN B.ACCT_DIV_CD = '1' THEN A.OCCR_DEPT_CD
            WHEN B.ACCT_DIV_CD = '2' THEN A.MNG_DEPT_CD
            WHEN B.ACCT_DIV_CD = '3' THEN A.OCCR_DEPT_CD
            WHEN B.ACCT_DIV_CD = '4' THEN A.MNG_DEPT_CD
        END                             ACCT_DEPT_CD
        ,A.TRAN_TP_CD                  TRAN_TP_CD
        ,B.DC_DIV_CD
        ,B.ACCT_CD
        ,CASE
        WHEN B.CALC_FG = '1' THEN C.AMT_ITEM_DIV_CD
            ELSE B.AMT_ITEM_DIV_CD
        END                                AMT_ITEM_DIV_CD
        ,CASE
        WHEN B.CALC_FG = '1' THEN C.OP_DIV_CD
            ELSE '+'
        END                                OP_DIV_CD
        ,A.TR_TP_CD
        ,A.TR_STAG_CD
        ,A.ISRT_DT
        ,A.LOAN_NO
        ,A.LOAN_SEQ
        ,A.PRDT_LRGE_CLAS_CD
        ,A.PRDT_MID_CLAS_CD
        ,A.OCCR_DEPT_CD
        ,A.MNG_DEPT_CD
        ,A.BANK_CD
        ,A.ACNT_NO
        ,A.CPRT_COM_NO
        ,A.MNG_ACCT_UNIT_CD
        ,A.ACCT_UNIT_CD                    OCCR_ACCT_UNIT_CD
        ,B.ACCT_CLAS_STND_CD
        ,CASE
           WHEN A.TR_STAG_CD like '0%' THEN
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN A.MNG_ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '2' THEN A.MNG_ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '3' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '4' THEN 'W01'
               END
           ELSE
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN A.ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '2' THEN A.ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '3' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '4' THEN 'W01'
               END
        END CNTR_ACCT_UNIT_CD
        FROM BUSER.BCLT_CLS_TR_DESC A
            ,EUSER.EACT_TR_ACCT_DESC B
            LEFT OUTER JOIN EUSER.EACT_TR_CALC_DESC C
            ON C.CALC_TP_CD = B.CALC_TP_CD
        WHERE A.ACCT_DT   = '2011-12-19' /*:전표.회계_일자*/
            AND A.TR_DESC_NO = 27003 /*:전표.거래_내역_번호*/
            AND B.TR_TP_DIV_CD = A.TR_TP_CD
            AND B.TR_STAG_DIV_CD = A.TR_STAG_CD
            AND B.CLAS_CD = (CASE
                        WHEN B.ACCT_CLAS_STND_CD = '01'
                           THEN A.PRDT_LRGE_CLAS_CD
                        WHEN B.ACCT_CLAS_STND_CD = '02'
                           THEN A.PRDT_LRGE_CLAS_CD||A.PRDT_MID_CLAS_CD
                        WHEN B.ACCT_CLAS_STND_CD = '03'
                           THEN CASE A.CPRT_COM_NO
                                WHEN ' ' THEN '*' ELSE
                                        CASE B.CLAS_CD
                                          WHEN ' ' THEN B.CLAS_CD
                                          ELSE A.CPRT_COM_NO
                                        END
                                END
                        WHEN B.ACCT_CLAS_STND_CD = '04'
                           THEN A.ACCT_UNIT_CD
                        WHEN B.ACCT_CLAS_STND_CD = '05'
                           THEN ( SELECT Z.CLAS2 FROM GUSER.GBCT_COMM_CD_DESC Z
                           WHERE Z.CD_KIND_NO = 'R00001'
                               AND Z.CD_DESC_NO = A.DSRC_OCCR_CD)
                        WHEN B.ACCT_CLAS_STND_CD = '06'
                           THEN A.DSRC_ADJ_CD
                        WHEN B.ACCT_CLAS_STND_CD = '07'
                           THEN CASE A.ACNT_NO
                                WHEN ' ' THEN '*' ELSE
                                        CASE B.CLAS_CD
                                          WHEN ' ' THEN B.CLAS_CD
                                          ELSE A.ACNT_NO
                                        END
                                END

                               WHEN B.ACCT_CLAS_STND_CD = '09'
                                  THEN A.PRDT_MID_CLAS_CD

              WHEN (B.ACCT_CLAS_STND_CD = ' '  OR  B.ACCT_CLAS_STND_CD IS NULL)
                      THEN B.CLAS_CD
               END)
        ) A
        ,(
          SELECT B.TR_SNUM
                ,CASE WHEN A.NO = 1 THEN '001'
                      WHEN A.NO = 2 THEN '002'
                      WHEN A.NO = 3 THEN '003'
                      WHEN A.NO = 4 THEN '004'
                      WHEN A.NO = 5 THEN '005'
                      WHEN A.NO = 6 THEN '006'
                      WHEN A.NO = 7 THEN '007'
                      WHEN A.NO = 8 THEN '008'
                      WHEN A.NO = 9 THEN '009'
                      WHEN A.NO = 10 THEN '010'
                      WHEN A.NO = 11 THEN '011'
                      WHEN A.NO = 12 THEN '012'
                      WHEN A.NO = 13 THEN '013'
                      WHEN A.NO = 14 THEN '014'
                      WHEN A.NO = 15 THEN '015'
                      WHEN A.NO = 16 THEN '016'
                      WHEN A.NO = 17 THEN '017'
                      WHEN A.NO = 18 THEN '018'
                      WHEN A.NO = 19 THEN '019'
                      WHEN A.NO = 20 THEN '020'
                      WHEN A.NO = 21 THEN '021'
                      WHEN A.NO = 22 THEN '022'
                      WHEN A.NO = 23 THEN '023'
                      WHEN A.NO = 24 THEN '024'
                      WHEN A.NO = 25 THEN '025'
                      WHEN A.NO = 26 THEN '026'
                      WHEN A.NO = 27 THEN '027'
                      WHEN A.NO = 28 THEN '028'
                      WHEN A.NO = 29 THEN '029'
                      WHEN A.NO = 30 THEN '030'
                      WHEN A.NO = 31 THEN '031'
                      WHEN A.NO = 32 THEN '032'
                      WHEN A.NO = 33 THEN '033'
                      WHEN A.NO = 34 THEN '034'
                      WHEN A.NO = 35 THEN '035'
                      WHEN A.NO = 36 THEN '036'
                      WHEN A.NO = 37 THEN '037'
                      WHEN A.NO = 38 THEN '038'
                      WHEN A.NO = 39 THEN '039'
                      WHEN A.NO = 40 THEN '040'
                      WHEN A.NO = 41 THEN '041'
                      WHEN A.NO = 42 THEN '042'
                      WHEN A.NO = 43 THEN '043'
                      WHEN A.NO = 44 THEN '044'
                      WHEN A.NO = 45 THEN '045'
                      WHEN A.NO = 46 THEN '046'
                      WHEN A.NO = 47 THEN '047'
                      WHEN A.NO = 48 THEN '048'
                      WHEN A.NO = 49 THEN '049'
                      WHEN A.NO = 50 THEN '050'
                      WHEN A.NO = 51 THEN '051'
                      WHEN A.NO = 52 THEN '052'
                      WHEN A.NO = 53 THEN '053'
                      WHEN A.NO = 54 THEN '054'
                      WHEN A.NO = 55 THEN '055'
                      WHEN A.NO = 56 THEN '056'
                      WHEN A.NO = 57 THEN '057'
                      WHEN A.NO = 58 THEN '058'
                      WHEN A.NO = 59 THEN '059'
                      WHEN A.NO = 60 THEN '060'
                      WHEN A.NO = 61 THEN '061'
                      WHEN A.NO = 62 THEN '062'
                      WHEN A.NO = 63 THEN '063'
                      WHEN A.NO = 64 THEN '064'
                      ELSE '999'
                 END    AMT_ITEM_CD
                ,CASE WHEN A.NO = 1 THEN B.SETL_AMT
                      WHEN A.NO = 2 THEN B.DEPO
                      WHEN A.NO = 3 THEN B.CASH
                      WHEN A.NO = 4 THEN B.DSRC_ADJ_AMT
                      WHEN A.NO = 5 THEN B.DSRC_OCCR_AMT
                      WHEN A.NO = 6 THEN B.PAMT
                      WHEN A.NO = 7 THEN B.INT
                      WHEN A.NO = 8 THEN B.DFEE
                      WHEN A.NO = 9 THEN B.ADJ_DFEE
                      WHEN A.NO = 10 THEN B.PDUE_DISC_DFEE
                      WHEN A.NO = 11 THEN B.LEGL_ACTN_COST
                      WHEN A.NO = 12 THEN B.MID_RMBR_FEE
                      WHEN A.NO = 13 THEN B.TRT_FEE_DISC
                      WHEN A.NO = 14 THEN B.ETC_PRFT
                      WHEN A.NO = 15 THEN B.EXMP_PAMT
                      WHEN A.NO = 16 THEN B.EXMP_INT
                      WHEN A.NO = 17 THEN B.EXMP_DFEE
                      WHEN A.NO = 18 THEN B.EXMP_LEGL_ACTN_COST
                      WHEN A.NO = 19 THEN B.EXMP_MID_RMBR_FEE
                      WHEN A.NO = 20 THEN B.CRDT_RCVR_UNRCV_AMT
                      WHEN A.NO = 21 THEN B.PSG_INT
                      WHEN A.NO = 22 THEN B.PRE_RCPT_PAMT
                      WHEN A.NO = 23 THEN B.PRE_RCPT_INT
                      WHEN A.NO = 24 THEN B.PAY_AMT
                      WHEN A.NO = 25 THEN B.RETN_AMT
                      WHEN A.NO = 26 THEN B.CHNG_BF_FEE_COST_TRT_FEE
                      WHEN A.NO = 27 THEN B.CHNG_BF_FEE_PRFT_TRT_FEE
                      WHEN A.NO = 28 THEN B.CHNG_BF_PAMT
                      WHEN A.NO = 29 THEN B.CHNG_BF_DFER_COST_CPRT_FEE
                      WHEN A.NO = 30 THEN B.CHNG_BF_DFER_COST_TRT_FEE
                      WHEN A.NO = 31 THEN B.CHNG_BF_DFER_PRFT_CPRT_FEE
                      WHEN A.NO = 32 THEN B.CHNG_BF_DFER_PRFT_TRT_FEE
                      WHEN A.NO = 33 THEN B.CHNG_BF_INT_COST_CPRT_FEE
                      WHEN A.NO = 34 THEN B.CHNG_BF_INT_PRFT_CPRT_FEE
                      WHEN A.NO = 35 THEN B.CHNG_AF_FEE_COST_TRT_FEE
                      WHEN A.NO = 36 THEN B.CHNG_AF_FEE_PRFT_TRT_FEE
                      WHEN A.NO = 37 THEN B.CHNG_AF_PAMT
                      WHEN A.NO = 38 THEN B.CHNG_AF_DFER_COST_CPRT_FEE
                      WHEN A.NO = 39 THEN B.CHNG_AF_DFER_COST_TRT_FEE
                      WHEN A.NO = 40 THEN B.CHNG_AF_DFER_PRFT_CPRT_FEE
                      WHEN A.NO = 41 THEN B.CHNG_AF_DFER_PRFT_TRT_FEE
                      WHEN A.NO = 42 THEN B.CHNG_AF_INT_COST_CPRT_FEE
                      WHEN A.NO = 43 THEN B.CHNG_AF_INT_PRFT_CPRT_FEE
                      WHEN A.NO = 44 THEN B.FEE_COST_TRT_FEE
                      WHEN A.NO = 45 THEN B.FEE_PRFT_TRT_FEE
                      WHEN A.NO = 46 THEN B.DFER_COST_CPRT_FEE
                      WHEN A.NO = 47 THEN B.DFER_COST_TRT_FEE
                      WHEN A.NO = 48 THEN B.DFER_PRFT_CPRT_FEE
                      WHEN A.NO = 49 THEN B.DFER_PRFT_TRT_FEE
                      WHEN A.NO = 50 THEN B.INT_COST_CPRT_FEE
                      WHEN A.NO = 51 THEN B.INT_PRFT_CPRT_FEE
                      WHEN A.NO = 52 THEN B.NOTA_FEE
                      WHEN A.NO = 53 THEN B.STMP_FEE
                      WHEN A.NO = 54 THEN B.HTAX
                      WHEN A.NO = 55 THEN B.SET_FEE
                      WHEN A.NO = 56 THEN B.ITAX
                      WHEN A.NO = 57 THEN B.ETC_FEE
                      WHEN A.NO = 58 THEN B.DEBTMAN_FEE
                      WHEN A.NO = 59 THEN B.PR_FEE
                      WHEN A.NO = 60 THEN B.TRT_FEE
                      WHEN A.NO = 61 THEN B.BAD_DEBT_APPR_AMT
                      WHEN A.NO = 62 THEN B.BAD_DEBT_DEPR_COST
                      WHEN A.NO = 63 THEN B.DEPS_AMT
                      WHEN A.NO = 64 THEN B.ETC_LOSS
                      ELSE 0
                END                                                       AMT
           FROM BUSER.BGMT_COMM_DUMMY A
                ,BUSER.BCLT_CLS_TR_DESC B
          WHERE A.NO <= (SELECT TO_NUMBER (MAX(CD_DESC_NO))
                           FROM GUSER.GBCT_COMM_CD_DESC Z
                          WHERE Z.CD_KIND_NO = 'R00044')
            AND B.ACCT_DT    = '2011-12-19'  /*:전표.회계일자*/
            AND B.TR_DESC_NO = 27003 /* :전표.거래_내역_번호*/
         ) B
   WHERE B.TR_SNUM = A.TR_SNUM
     AND B.AMT_ITEM_CD = A.AMT_ITEM_DIV_CD
     AND B.AMT != 0
   GROUP BY A.TR_SNUM
            ,A.ACCT_UNIT_CD
            ,A.ACCT_DEPT_CD
            ,A.DC_DIV_CD
            ,A.ACCT_CD
    HAVING SUM(B.AMT
                * CASE
                      WHEN OP_DIV_CD = '+' THEN 1
                      ELSE -1
                  END) != 0
;


SELECT
    MAX(A.ACCT_DT)
    ,MAX(A.TR_DESC_NO)
    ,A.TR_SNUM
    ,ROW_NUMBER() OVER(ORDER BY A.TR_SNUM) AS ROW_NUM
    ,A.ACCT_UNIT_CD
    ,A.ACCT_DEPT_CD
    ,MAX(A.TRAN_TP_CD)
    ,A.DC_DIV_CD
    ,A.ACCT_CD
    ,SUM(B.AMT
     * CASE
         WHEN OP_DIV_CD = '+' THEN 1
         ELSE -1
       END)    VOCH_AMT
    ,MAX(A.TR_TP_CD)
    ,MAX(A.TR_STAG_CD)
    ,MAX(A.ISRT_DT)
    ,MAX(A.LOAN_NO)
    ,MAX(A.LOAN_SEQ)
    ,MAX(A.PRDT_LRGE_CLAS_CD)
    ,MAX(A.PRDT_MID_CLAS_CD)
    ,MAX(A.OCCR_DEPT_CD)
    ,MAX(A.MNG_DEPT_CD)
    ,MAX(A.BANK_CD)
    ,MAX(A.ACNT_NO)
    ,MAX(A.CPRT_COM_NO)  AS CPRT_COM_NO
    ,TO_CHAR(SYSDATE,'YYYY-MM-DD')        FRST_REG_DT
    ,TO_CHAR(SYSDATE,'HH24MISS')          FRST_REG_TM
    ,CAST( 'SLNPM031' AS CHAR(8))                  FRST_REG_EMP_NO
    ,TO_CHAR(SYSDATE,'YYYY-MM-DD')        LAST_PROC_DT
    ,TO_CHAR(SYSDATE,'HH24MISS')          LAST_PROC_TM
    ,CAST( 'SLNPM031' AS CHAR(8))                LAST_PROC_EMP_NO
    ,MAX(A.MNG_ACCT_UNIT_CD)
    ,MAX(A.OCCR_ACCT_UNIT_CD)
FROM
    ( SELECT
        A.ACCT_DT
        ,A.TR_DESC_NO
        ,A.TR_SNUM
        ,0                                 SUB_SNUM
        ,CASE
           WHEN A.TR_STAG_CD like '0%' THEN
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '2' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '3' THEN A.MNG_ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '4' THEN A.MNG_ACCT_UNIT_CD
               END
           ELSE
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '2' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '3' THEN A.ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '4' THEN A.ACCT_UNIT_CD
               END
        END ACCT_UNIT_CD
        ,CASE
            WHEN B.ACCT_DIV_CD = '1' THEN A.OCCR_DEPT_CD
            WHEN B.ACCT_DIV_CD = '2' THEN A.MNG_DEPT_CD
            WHEN B.ACCT_DIV_CD = '3' THEN A.OCCR_DEPT_CD
            WHEN B.ACCT_DIV_CD = '4' THEN A.MNG_DEPT_CD
        END                             ACCT_DEPT_CD
        ,A.TRAN_TP_CD                  TRAN_TP_CD
        ,B.DC_DIV_CD
        ,B.ACCT_CD
        ,CASE
        WHEN B.CALC_FG = '1' THEN C.AMT_ITEM_DIV_CD
            ELSE B.AMT_ITEM_DIV_CD
        END                                AMT_ITEM_DIV_CD
        ,CASE
        WHEN B.CALC_FG = '1' THEN C.OP_DIV_CD
            ELSE '+'
        END                                OP_DIV_CD
        ,A.TR_TP_CD
        ,A.TR_STAG_CD
        ,A.ISRT_DT
        ,A.LOAN_NO
        ,A.LOAN_SEQ
        ,A.PRDT_LRGE_CLAS_CD
        ,A.PRDT_MID_CLAS_CD
        ,A.OCCR_DEPT_CD
        ,A.MNG_DEPT_CD
        ,A.BANK_CD
        ,A.ACNT_NO
        ,A.CPRT_COM_NO
        ,A.MNG_ACCT_UNIT_CD
        ,A.ACCT_UNIT_CD                    OCCR_ACCT_UNIT_CD
        ,B.ACCT_CLAS_STND_CD
        ,CASE
           WHEN A.TR_STAG_CD like '0%' THEN
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN A.MNG_ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '2' THEN A.MNG_ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '3' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '4' THEN 'W01'
               END
           ELSE
               CASE
                   WHEN B.ACCT_DIV_CD = '1' THEN A.ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '2' THEN A.ACCT_UNIT_CD
                   WHEN B.ACCT_DIV_CD = '3' THEN 'W01'
                   WHEN B.ACCT_DIV_CD = '4' THEN 'W01'
               END
        END CNTR_ACCT_UNIT_CD
        FROM BUSER.BCLT_CLS_TR_DESC A
            ,EUSER.EACT_TR_ACCT_DESC B
            LEFT OUTER JOIN EUSER.EACT_TR_CALC_DESC C
            ON C.CALC_TP_CD = B.CALC_TP_CD
        WHERE A.ACCT_DT   = '2011-12-19' /*:전표.회계_일자*/
            AND A.TR_DESC_NO = '26994' /*:전표.거래_내역_번호*/
            AND B.TR_TP_DIV_CD = A.TR_TP_CD
            AND B.TR_STAG_DIV_CD = A.TR_STAG_CD
            AND B.CLAS_CD = (CASE
                        WHEN B.ACCT_CLAS_STND_CD = '01'
                           THEN A.PRDT_LRGE_CLAS_CD
                        WHEN B.ACCT_CLAS_STND_CD = '02'
                           THEN A.PRDT_LRGE_CLAS_CD||A.PRDT_MID_CLAS_CD
                        WHEN B.ACCT_CLAS_STND_CD = '03'
                           THEN CASE A.CPRT_COM_NO
                                WHEN ' ' THEN '*' ELSE
                                        CASE B.CLAS_CD
                                          WHEN ' ' THEN B.CLAS_CD
                                          ELSE A.CPRT_COM_NO
                                        END
                                END
                        WHEN B.ACCT_CLAS_STND_CD = '04'
                           THEN A.ACCT_UNIT_CD
                        WHEN B.ACCT_CLAS_STND_CD = '05'
                           THEN ( SELECT Z.CLAS2 FROM GUSER.GBCT_COMM_CD_DESC Z
                           WHERE Z.CD_KIND_NO = 'R00001'
                               AND Z.CD_DESC_NO = A.DSRC_OCCR_CD)
                        WHEN B.ACCT_CLAS_STND_CD = '06'
                           THEN A.DSRC_ADJ_CD
                        WHEN B.ACCT_CLAS_STND_CD = '07'
                           THEN CASE A.ACNT_NO
                                WHEN ' ' THEN '*' ELSE
                                        CASE B.CLAS_CD
                                          WHEN ' ' THEN B.CLAS_CD
                                          ELSE A.ACNT_NO
                                        END
                                END

                               WHEN B.ACCT_CLAS_STND_CD = '09'
                                  THEN A.PRDT_MID_CLAS_CD

              WHEN (B.ACCT_CLAS_STND_CD = ' '  OR  B.ACCT_CLAS_STND_CD IS NULL)
                      THEN B.CLAS_CD
               END)
        ) A
        ,(
          SELECT B.TR_SNUM
                ,CASE WHEN A.NO = 1 THEN '001'
                      WHEN A.NO = 2 THEN '002'
                      WHEN A.NO = 3 THEN '003'
                      WHEN A.NO = 4 THEN '004'
                      WHEN A.NO = 5 THEN '005'
                      WHEN A.NO = 6 THEN '006'
                      WHEN A.NO = 7 THEN '007'
                      WHEN A.NO = 8 THEN '008'
                      WHEN A.NO = 9 THEN '009'
                      WHEN A.NO = 10 THEN '010'
                      WHEN A.NO = 11 THEN '011'
                      WHEN A.NO = 12 THEN '012'
                      WHEN A.NO = 13 THEN '013'
                      WHEN A.NO = 14 THEN '014'
                      WHEN A.NO = 15 THEN '015'
                      WHEN A.NO = 16 THEN '016'
                      WHEN A.NO = 17 THEN '017'
                      WHEN A.NO = 18 THEN '018'
                      WHEN A.NO = 19 THEN '019'
                      WHEN A.NO = 20 THEN '020'
                      WHEN A.NO = 21 THEN '021'
                      WHEN A.NO = 22 THEN '022'
                      WHEN A.NO = 23 THEN '023'
                      WHEN A.NO = 24 THEN '024'
                      WHEN A.NO = 25 THEN '025'
                      WHEN A.NO = 26 THEN '026'
                      WHEN A.NO = 27 THEN '027'
                      WHEN A.NO = 28 THEN '028'
                      WHEN A.NO = 29 THEN '029'
                      WHEN A.NO = 30 THEN '030'
                      WHEN A.NO = 31 THEN '031'
                      WHEN A.NO = 32 THEN '032'
                      WHEN A.NO = 33 THEN '033'
                      WHEN A.NO = 34 THEN '034'
                      WHEN A.NO = 35 THEN '035'
                      WHEN A.NO = 36 THEN '036'
                      WHEN A.NO = 37 THEN '037'
                      WHEN A.NO = 38 THEN '038'
                      WHEN A.NO = 39 THEN '039'
                      WHEN A.NO = 40 THEN '040'
                      WHEN A.NO = 41 THEN '041'
                      WHEN A.NO = 42 THEN '042'
                      WHEN A.NO = 43 THEN '043'
                      WHEN A.NO = 44 THEN '044'
                      WHEN A.NO = 45 THEN '045'
                      WHEN A.NO = 46 THEN '046'
                      WHEN A.NO = 47 THEN '047'
                      WHEN A.NO = 48 THEN '048'
                      WHEN A.NO = 49 THEN '049'
                      WHEN A.NO = 50 THEN '050'
                      WHEN A.NO = 51 THEN '051'
                      WHEN A.NO = 52 THEN '052'
                      WHEN A.NO = 53 THEN '053'
                      WHEN A.NO = 54 THEN '054'
                      WHEN A.NO = 55 THEN '055'
                      WHEN A.NO = 56 THEN '056'
                      WHEN A.NO = 57 THEN '057'
                      WHEN A.NO = 58 THEN '058'
                      WHEN A.NO = 59 THEN '059'
                      WHEN A.NO = 60 THEN '060'
                      WHEN A.NO = 61 THEN '061'
                      WHEN A.NO = 62 THEN '062'
                      WHEN A.NO = 63 THEN '063'
                      WHEN A.NO = 64 THEN '064'
                      ELSE '999'
                 END    AMT_ITEM_CD
                ,CASE WHEN A.NO = 1 THEN B.SETL_AMT
                      WHEN A.NO = 2 THEN B.DEPO
                      WHEN A.NO = 3 THEN B.CASH
                      WHEN A.NO = 4 THEN B.DSRC_ADJ_AMT
                      WHEN A.NO = 5 THEN B.DSRC_OCCR_AMT
                      WHEN A.NO = 6 THEN B.PAMT
                      WHEN A.NO = 7 THEN B.INT
                      WHEN A.NO = 8 THEN B.DFEE
                      WHEN A.NO = 9 THEN B.ADJ_DFEE
                      WHEN A.NO = 10 THEN B.PDUE_DISC_DFEE
                      WHEN A.NO = 11 THEN B.LEGL_ACTN_COST
                      WHEN A.NO = 12 THEN B.MID_RMBR_FEE
                      WHEN A.NO = 13 THEN B.TRT_FEE_DISC
                      WHEN A.NO = 14 THEN B.ETC_PRFT
                      WHEN A.NO = 15 THEN B.EXMP_PAMT
                      WHEN A.NO = 16 THEN B.EXMP_INT
                      WHEN A.NO = 17 THEN B.EXMP_DFEE
                      WHEN A.NO = 18 THEN B.EXMP_LEGL_ACTN_COST
                      WHEN A.NO = 19 THEN B.EXMP_MID_RMBR_FEE
                      WHEN A.NO = 20 THEN B.CRDT_RCVR_UNRCV_AMT
                      WHEN A.NO = 21 THEN B.PSG_INT
                      WHEN A.NO = 22 THEN B.PRE_RCPT_PAMT
                      WHEN A.NO = 23 THEN B.PRE_RCPT_INT
                      WHEN A.NO = 24 THEN B.PAY_AMT
                      WHEN A.NO = 25 THEN B.RETN_AMT
                      WHEN A.NO = 26 THEN B.CHNG_BF_FEE_COST_TRT_FEE
                      WHEN A.NO = 27 THEN B.CHNG_BF_FEE_PRFT_TRT_FEE
                      WHEN A.NO = 28 THEN B.CHNG_BF_PAMT
                      WHEN A.NO = 29 THEN B.CHNG_BF_DFER_COST_CPRT_FEE
                      WHEN A.NO = 30 THEN B.CHNG_BF_DFER_COST_TRT_FEE
                      WHEN A.NO = 31 THEN B.CHNG_BF_DFER_PRFT_CPRT_FEE
                      WHEN A.NO = 32 THEN B.CHNG_BF_DFER_PRFT_TRT_FEE
                      WHEN A.NO = 33 THEN B.CHNG_BF_INT_COST_CPRT_FEE
                      WHEN A.NO = 34 THEN B.CHNG_BF_INT_PRFT_CPRT_FEE
                      WHEN A.NO = 35 THEN B.CHNG_AF_FEE_COST_TRT_FEE
                      WHEN A.NO = 36 THEN B.CHNG_AF_FEE_PRFT_TRT_FEE
                      WHEN A.NO = 37 THEN B.CHNG_AF_PAMT
                      WHEN A.NO = 38 THEN B.CHNG_AF_DFER_COST_CPRT_FEE
                      WHEN A.NO = 39 THEN B.CHNG_AF_DFER_COST_TRT_FEE
                      WHEN A.NO = 40 THEN B.CHNG_AF_DFER_PRFT_CPRT_FEE
                      WHEN A.NO = 41 THEN B.CHNG_AF_DFER_PRFT_TRT_FEE
                      WHEN A.NO = 42 THEN B.CHNG_AF_INT_COST_CPRT_FEE
                      WHEN A.NO = 43 THEN B.CHNG_AF_INT_PRFT_CPRT_FEE
                      WHEN A.NO = 44 THEN B.FEE_COST_TRT_FEE
                      WHEN A.NO = 45 THEN B.FEE_PRFT_TRT_FEE
                      WHEN A.NO = 46 THEN B.DFER_COST_CPRT_FEE
                      WHEN A.NO = 47 THEN B.DFER_COST_TRT_FEE
                      WHEN A.NO = 48 THEN B.DFER_PRFT_CPRT_FEE
                      WHEN A.NO = 49 THEN B.DFER_PRFT_TRT_FEE
                      WHEN A.NO = 50 THEN B.INT_COST_CPRT_FEE
                      WHEN A.NO = 51 THEN B.INT_PRFT_CPRT_FEE
                      WHEN A.NO = 52 THEN B.NOTA_FEE
                      WHEN A.NO = 53 THEN B.STMP_FEE
                      WHEN A.NO = 54 THEN B.HTAX
                      WHEN A.NO = 55 THEN B.SET_FEE
                      WHEN A.NO = 56 THEN B.ITAX
                      WHEN A.NO = 57 THEN B.ETC_FEE
                      WHEN A.NO = 58 THEN B.DEBTMAN_FEE
                      WHEN A.NO = 59 THEN B.PR_FEE
                      WHEN A.NO = 60 THEN B.TRT_FEE
                      WHEN A.NO = 61 THEN B.BAD_DEBT_APPR_AMT
                      WHEN A.NO = 62 THEN B.BAD_DEBT_DEPR_COST
                      WHEN A.NO = 63 THEN B.DEPS_AMT
                      WHEN A.NO = 64 THEN B.ETC_LOSS
                      ELSE 0
                END                                                       AMT
           FROM BUSER.BGMT_COMM_DUMMY A
                ,BUSER.BCLT_CLS_TR_DESC B
          WHERE A.NO <= (SELECT TO_NUMBER (MAX(CD_DESC_NO))
                           FROM GUSER.GBCT_COMM_CD_DESC Z
                          WHERE Z.CD_KIND_NO = 'R00044')
            AND B.ACCT_DT    = '2011-12-19'  /*:전표.회계일자*/
            AND B.TR_DESC_NO = '26994' /* :전표.거래_내역_번호*/
         ) B
   WHERE B.TR_SNUM = A.TR_SNUM
     AND B.AMT_ITEM_CD = A.AMT_ITEM_DIV_CD
     AND B.AMT != 0
   GROUP BY A.TR_SNUM
            ,A.ACCT_UNIT_CD
            ,A.ACCT_DEPT_CD
            ,A.DC_DIV_CD
            ,A.ACCT_CD
    HAVING SUM(B.AMT
                * CASE
                      WHEN OP_DIV_CD = '+' THEN 1
                      ELSE -1
                  END) != 0
;

-- A ----------------
SELECT
    A.ACCT_DT
    ,A.TR_DESC_NO
    ,A.TR_SNUM
    ,0                                 SUB_SNUM
    ,CASE
       WHEN A.TR_STAG_CD like '0%' THEN
           CASE
               WHEN B.ACCT_DIV_CD = '1' THEN 'W01'
               WHEN B.ACCT_DIV_CD = '2' THEN 'W01'
               WHEN B.ACCT_DIV_CD = '3' THEN A.MNG_ACCT_UNIT_CD
               WHEN B.ACCT_DIV_CD = '4' THEN A.MNG_ACCT_UNIT_CD
           END
       ELSE
           CASE
               WHEN B.ACCT_DIV_CD = '1' THEN 'W01'
               WHEN B.ACCT_DIV_CD = '2' THEN 'W01'
               WHEN B.ACCT_DIV_CD = '3' THEN A.ACCT_UNIT_CD
               WHEN B.ACCT_DIV_CD = '4' THEN A.ACCT_UNIT_CD
           END
    END ACCT_UNIT_CD
    ,CASE
        WHEN B.ACCT_DIV_CD = '1' THEN A.OCCR_DEPT_CD
        WHEN B.ACCT_DIV_CD = '2' THEN A.MNG_DEPT_CD
        WHEN B.ACCT_DIV_CD = '3' THEN A.OCCR_DEPT_CD
        WHEN B.ACCT_DIV_CD = '4' THEN A.MNG_DEPT_CD
    END                             ACCT_DEPT_CD
    ,A.TRAN_TP_CD                  TRAN_TP_CD
    ,B.DC_DIV_CD
    ,B.ACCT_CD
    ,CASE
    WHEN B.CALC_FG = '1' THEN C.AMT_ITEM_DIV_CD
        ELSE B.AMT_ITEM_DIV_CD
    END                                AMT_ITEM_DIV_CD
    ,CASE
    WHEN B.CALC_FG = '1' THEN C.OP_DIV_CD
        ELSE '+'
    END                                OP_DIV_CD
    ,A.TR_TP_CD
    ,A.TR_STAG_CD
    ,A.ISRT_DT
    ,A.LOAN_NO
    ,A.LOAN_SEQ
    ,A.PRDT_LRGE_CLAS_CD
    ,A.PRDT_MID_CLAS_CD
    ,A.OCCR_DEPT_CD
    ,A.MNG_DEPT_CD
    ,A.BANK_CD
    ,A.ACNT_NO
    ,A.CPRT_COM_NO
    ,A.MNG_ACCT_UNIT_CD
    ,A.ACCT_UNIT_CD                    OCCR_ACCT_UNIT_CD
    ,B.ACCT_CLAS_STND_CD
    ,CASE
       WHEN A.TR_STAG_CD like '0%' THEN
           CASE
               WHEN B.ACCT_DIV_CD = '1' THEN A.MNG_ACCT_UNIT_CD
               WHEN B.ACCT_DIV_CD = '2' THEN A.MNG_ACCT_UNIT_CD
               WHEN B.ACCT_DIV_CD = '3' THEN 'W01'
               WHEN B.ACCT_DIV_CD = '4' THEN 'W01'
           END
       ELSE
           CASE
               WHEN B.ACCT_DIV_CD = '1' THEN A.ACCT_UNIT_CD
               WHEN B.ACCT_DIV_CD = '2' THEN A.ACCT_UNIT_CD
               WHEN B.ACCT_DIV_CD = '3' THEN 'W01'
               WHEN B.ACCT_DIV_CD = '4' THEN 'W01'
           END
    END CNTR_ACCT_UNIT_CD
    FROM BUSER.BCLT_CLS_TR_DESC A
        ,EUSER.EACT_TR_ACCT_DESC B
        LEFT OUTER JOIN EUSER.EACT_TR_CALC_DESC C
        ON C.CALC_TP_CD = B.CALC_TP_CD
    WHERE A.ACCT_DT   = '2011-12-19' /*:전표.회계_일자*/
        AND A.TR_DESC_NO = 27003 /*:전표.거래_내역_번호*/
        AND B.TR_TP_DIV_CD = A.TR_TP_CD
        AND B.TR_STAG_DIV_CD = A.TR_STAG_CD
        AND B.CLAS_CD = (CASE
                    WHEN B.ACCT_CLAS_STND_CD = '01'
                       THEN A.PRDT_LRGE_CLAS_CD
                    WHEN B.ACCT_CLAS_STND_CD = '02'
                       THEN A.PRDT_LRGE_CLAS_CD||A.PRDT_MID_CLAS_CD
                    WHEN B.ACCT_CLAS_STND_CD = '03'
                       THEN CASE A.CPRT_COM_NO
                            WHEN ' ' THEN '*' ELSE
                                    CASE B.CLAS_CD
                                      WHEN ' ' THEN B.CLAS_CD
                                      ELSE A.CPRT_COM_NO
                                    END
                            END
                    WHEN B.ACCT_CLAS_STND_CD = '04'
                       THEN A.ACCT_UNIT_CD
                    WHEN B.ACCT_CLAS_STND_CD = '05'
                       THEN ( SELECT Z.CLAS2 FROM GUSER.GBCT_COMM_CD_DESC Z
                       WHERE Z.CD_KIND_NO = 'R00001'
                           AND Z.CD_DESC_NO = A.DSRC_OCCR_CD)
                    WHEN B.ACCT_CLAS_STND_CD = '06'
                       THEN A.DSRC_ADJ_CD
                    WHEN B.ACCT_CLAS_STND_CD = '07'
                       THEN CASE A.ACNT_NO
                            WHEN ' ' THEN '*' ELSE
                                    CASE B.CLAS_CD
                                      WHEN ' ' THEN B.CLAS_CD
                                      ELSE A.ACNT_NO
                                    END
                            END

                           WHEN B.ACCT_CLAS_STND_CD = '09'
                              THEN A.PRDT_MID_CLAS_CD

          WHEN (B.ACCT_CLAS_STND_CD = ' '  OR  B.ACCT_CLAS_STND_CD IS NULL)
                  THEN B.CLAS_CD
           END)
;

-------------------------
-- B --------------------
-------------------------
SELECT B.TR_SNUM
    ,CASE WHEN A.NO = 1 THEN '001'
          WHEN A.NO = 2 THEN '002'
          WHEN A.NO = 3 THEN '003'
          WHEN A.NO = 4 THEN '004'
          WHEN A.NO = 5 THEN '005'
          WHEN A.NO = 6 THEN '006'
          WHEN A.NO = 7 THEN '007'
          WHEN A.NO = 8 THEN '008'
          WHEN A.NO = 9 THEN '009'
          WHEN A.NO = 10 THEN '010'
          WHEN A.NO = 11 THEN '011'
          WHEN A.NO = 12 THEN '012'
          WHEN A.NO = 13 THEN '013'
          WHEN A.NO = 14 THEN '014'
          WHEN A.NO = 15 THEN '015'
          WHEN A.NO = 16 THEN '016'
          WHEN A.NO = 17 THEN '017'
          WHEN A.NO = 18 THEN '018'
          WHEN A.NO = 19 THEN '019'
          WHEN A.NO = 20 THEN '020'
          WHEN A.NO = 21 THEN '021'
          WHEN A.NO = 22 THEN '022'
          WHEN A.NO = 23 THEN '023'
          WHEN A.NO = 24 THEN '024'
          WHEN A.NO = 25 THEN '025'
          WHEN A.NO = 26 THEN '026'
          WHEN A.NO = 27 THEN '027'
          WHEN A.NO = 28 THEN '028'
          WHEN A.NO = 29 THEN '029'
          WHEN A.NO = 30 THEN '030'
          WHEN A.NO = 31 THEN '031'
          WHEN A.NO = 32 THEN '032'
          WHEN A.NO = 33 THEN '033'
          WHEN A.NO = 34 THEN '034'
          WHEN A.NO = 35 THEN '035'
          WHEN A.NO = 36 THEN '036'
          WHEN A.NO = 37 THEN '037'
          WHEN A.NO = 38 THEN '038'
          WHEN A.NO = 39 THEN '039'
          WHEN A.NO = 40 THEN '040'
          WHEN A.NO = 41 THEN '041'
          WHEN A.NO = 42 THEN '042'
          WHEN A.NO = 43 THEN '043'
          WHEN A.NO = 44 THEN '044'
          WHEN A.NO = 45 THEN '045'
          WHEN A.NO = 46 THEN '046'
          WHEN A.NO = 47 THEN '047'
          WHEN A.NO = 48 THEN '048'
          WHEN A.NO = 49 THEN '049'
          WHEN A.NO = 50 THEN '050'
          WHEN A.NO = 51 THEN '051'
          WHEN A.NO = 52 THEN '052'
          WHEN A.NO = 53 THEN '053'
          WHEN A.NO = 54 THEN '054'
          WHEN A.NO = 55 THEN '055'
          WHEN A.NO = 56 THEN '056'
          WHEN A.NO = 57 THEN '057'
          WHEN A.NO = 58 THEN '058'
          WHEN A.NO = 59 THEN '059'
          WHEN A.NO = 60 THEN '060'
          WHEN A.NO = 61 THEN '061'
          WHEN A.NO = 62 THEN '062'
          WHEN A.NO = 63 THEN '063'
          WHEN A.NO = 64 THEN '064'
          ELSE '999'
     END    AMT_ITEM_CD
    ,CASE WHEN A.NO = 1 THEN B.SETL_AMT
          WHEN A.NO = 2 THEN B.DEPO
          WHEN A.NO = 3 THEN B.CASH
          WHEN A.NO = 4 THEN B.DSRC_ADJ_AMT
          WHEN A.NO = 5 THEN B.DSRC_OCCR_AMT
          WHEN A.NO = 6 THEN B.PAMT
          WHEN A.NO = 7 THEN B.INT
          WHEN A.NO = 8 THEN B.DFEE
          WHEN A.NO = 9 THEN B.ADJ_DFEE
          WHEN A.NO = 10 THEN B.PDUE_DISC_DFEE
          WHEN A.NO = 11 THEN B.LEGL_ACTN_COST
          WHEN A.NO = 12 THEN B.MID_RMBR_FEE
          WHEN A.NO = 13 THEN B.TRT_FEE_DISC
          WHEN A.NO = 14 THEN B.ETC_PRFT
          WHEN A.NO = 15 THEN B.EXMP_PAMT
          WHEN A.NO = 16 THEN B.EXMP_INT
          WHEN A.NO = 17 THEN B.EXMP_DFEE
          WHEN A.NO = 18 THEN B.EXMP_LEGL_ACTN_COST
          WHEN A.NO = 19 THEN B.EXMP_MID_RMBR_FEE
          WHEN A.NO = 20 THEN B.CRDT_RCVR_UNRCV_AMT
          WHEN A.NO = 21 THEN B.PSG_INT
          WHEN A.NO = 22 THEN B.PRE_RCPT_PAMT
          WHEN A.NO = 23 THEN B.PRE_RCPT_INT
          WHEN A.NO = 24 THEN B.PAY_AMT
          WHEN A.NO = 25 THEN B.RETN_AMT
          WHEN A.NO = 26 THEN B.CHNG_BF_FEE_COST_TRT_FEE
          WHEN A.NO = 27 THEN B.CHNG_BF_FEE_PRFT_TRT_FEE
          WHEN A.NO = 28 THEN B.CHNG_BF_PAMT
          WHEN A.NO = 29 THEN B.CHNG_BF_DFER_COST_CPRT_FEE
          WHEN A.NO = 30 THEN B.CHNG_BF_DFER_COST_TRT_FEE
          WHEN A.NO = 31 THEN B.CHNG_BF_DFER_PRFT_CPRT_FEE
          WHEN A.NO = 32 THEN B.CHNG_BF_DFER_PRFT_TRT_FEE
          WHEN A.NO = 33 THEN B.CHNG_BF_INT_COST_CPRT_FEE
          WHEN A.NO = 34 THEN B.CHNG_BF_INT_PRFT_CPRT_FEE
          WHEN A.NO = 35 THEN B.CHNG_AF_FEE_COST_TRT_FEE
          WHEN A.NO = 36 THEN B.CHNG_AF_FEE_PRFT_TRT_FEE
          WHEN A.NO = 37 THEN B.CHNG_AF_PAMT
          WHEN A.NO = 38 THEN B.CHNG_AF_DFER_COST_CPRT_FEE
          WHEN A.NO = 39 THEN B.CHNG_AF_DFER_COST_TRT_FEE
          WHEN A.NO = 40 THEN B.CHNG_AF_DFER_PRFT_CPRT_FEE
          WHEN A.NO = 41 THEN B.CHNG_AF_DFER_PRFT_TRT_FEE
          WHEN A.NO = 42 THEN B.CHNG_AF_INT_COST_CPRT_FEE
          WHEN A.NO = 43 THEN B.CHNG_AF_INT_PRFT_CPRT_FEE
          WHEN A.NO = 44 THEN B.FEE_COST_TRT_FEE
          WHEN A.NO = 45 THEN B.FEE_PRFT_TRT_FEE
          WHEN A.NO = 46 THEN B.DFER_COST_CPRT_FEE
          WHEN A.NO = 47 THEN B.DFER_COST_TRT_FEE
          WHEN A.NO = 48 THEN B.DFER_PRFT_CPRT_FEE
          WHEN A.NO = 49 THEN B.DFER_PRFT_TRT_FEE
          WHEN A.NO = 50 THEN B.INT_COST_CPRT_FEE
          WHEN A.NO = 51 THEN B.INT_PRFT_CPRT_FEE
          WHEN A.NO = 52 THEN B.NOTA_FEE
          WHEN A.NO = 53 THEN B.STMP_FEE
          WHEN A.NO = 54 THEN B.HTAX
          WHEN A.NO = 55 THEN B.SET_FEE
          WHEN A.NO = 56 THEN B.ITAX
          WHEN A.NO = 57 THEN B.ETC_FEE
          WHEN A.NO = 58 THEN B.DEBTMAN_FEE
          WHEN A.NO = 59 THEN B.PR_FEE
          WHEN A.NO = 60 THEN B.TRT_FEE
          WHEN A.NO = 61 THEN B.BAD_DEBT_APPR_AMT
          WHEN A.NO = 62 THEN B.BAD_DEBT_DEPR_COST
          WHEN A.NO = 63 THEN B.DEPS_AMT
          WHEN A.NO = 64 THEN B.ETC_LOSS
          ELSE 0
    END                                                       AMT
FROM BUSER.BGMT_COMM_DUMMY A
    ,BUSER.BCLT_CLS_TR_DESC B
WHERE A.NO <= (SELECT TO_NUMBER (MAX(CD_DESC_NO))
               FROM GUSER.GBCT_COMM_CD_DESC Z
              WHERE Z.CD_KIND_NO = 'R00044')
AND B.ACCT_DT    = '2011-12-19'  /*:전표.회계일자*/
AND B.TR_DESC_NO = 27003 /* :전표.거래_내역_번호*/
;

BUSER.BCLT_CLS_TR_DESC A
        ,EUSER.EACT_TR_ACCT_DESC B
        LEFT OUTER JOIN EUSER.EACT_TR_CALC_DESC C
        ON C.CALC_TP_CD = B.CALC_TP_CD
;

-- BCLT_마감_기본
BUSER.BCLT_CLS_BASE
;

-- BCLT_마감_보조_내역
BUSER.BCLT_CLS_SUB_DESC
;

-- EACT_거래_계산_내역
select *
from
EUSER.EACT_TR_CALC_DESC
;


-- BCLT_마감_거래_내역
select *
from BUSER.BCLT_CLS_TR_DESC A
where
    ACCT_DT    = '2011-12-19'
;

-- EACT_거래_계정_내역
select * 
from EUSER.EACT_TR_ACCT_DESC B
where 
--where
--    ACCT_DT    = '2011-12-19'
order by tr_tp_div_cd
;


