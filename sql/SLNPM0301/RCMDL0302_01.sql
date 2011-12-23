
-- 부서코드 가져오기
select * 
FROM GUSER.GBCT_COMM_CD_DESC --@hiplus_link
                WHERE CD_KIND_NO = 'R00038'
;

SELECT                                                            
    TO_CHAR(SYSDATE,'YYYY-MM-DD')   AS PROC_DT,             
    TO_CHAR(SYSDATE,'HH24MISS')  AS PROC_TM,             
    TO_CHAR(SYSDATE-5/24/60,'HH24MISS')  JOB_TM,    //5분을 빼준다.
    CLAS1                    ISRT_DEPT_CD,        
    CLAS2                    ISRT_EMP_NO,         
    MARK_ODR                  FETCH_CNT            
FROM GUSER.GBCT_COMM_CD_DESC                                      
WHERE CD_KIND_NO = '' --?   /*R00038*/                                 
  AND CD_DESC_NO = ?   /*'1'  */
;
SELECT A.LOAN_NO 
 ,A.LOAN_SEQ 
 ,A.IMG_ACNT_NO 
 ,A.LOAN_DT 
 ,A.LOAN_INT_RT 
 FROM AUSER.ALOT_LOAN_BASE A  
 WHERE A.IMG_ACNT_NO =(SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE  
 WHERE LOAN_NO ='11052000051' --?  
 AND LOAN_SEQ='01' )    
 AND A.LOAN_STAT_CD ='22'  
 ORDER BY A.LOAN_INT_RT DESC, A.LOAN_DT
 ;
 
SELECT A.TR_DT                            AS TR_DT		
     , B.CONT_MAN_NO TR_ORG_NM
-- 20110329 거래기관명 주민번호 변경
--     ,(SELECT Z.TR_ORG_NM                             
--        FROM BUSER.BVAT_TR_ORG_BASE Z                 
--       WHERE Z.TR_ORG_CD = A.TR_ORG_CD)   AS TR_ORG_NM
     ,(SELECT Z.CD_DESC_KOR_NM                        
         FROM GUSER.GBCT_COMM_CD_DESC Z               
        WHERE Z.CD_KIND_NO = 'A00003'                 
          AND Z.CD_DESC_NO = A.BANK_CD)   AS BANK_NM 
     ,A.LOAN_NO                           AS LOAN_NO	
     ,A.LOAN_SEQ                          AS LOAN_SEQ
     ,CASE B.SETL_DD
           WHEN '99'  THEN '말일'
           ELSE B.SETL_DD       END       AS SETL_DD	
     ,(SELECT Z.CUST_NM                               
         FROM AUSER.ACTT_CUST_BASE Z                  
        WHERE Z.CUST_NO = B.CONT_MAN_NO)  AS CUST_NM  
     ,A.DEPO_OWNNM          AS DEPO_OWNNM
     ,A.ACNT_NO             AS ACNT_NO
     ,A.TR_AMT              AS TR_AMT	
     ,(SELECT Z.DEPT_NM 
         FROM DUSER.DHRT_ORG Z  
        WHERE Z.DEPT_CD = A.MNG_DEPT_CD)         AS DEPT_NM
     ,E.BND_DIVS_EMP_NO                          AS BND_DIVS_EMP_NO
     ,(SELECT Z.KOR_NAME 
          FROM DUSER.DHRT_EMP_BASE Z
        WHERE Z.EMP_NO = E.BND_DIVS_EMP_NO)      AS BND_DIVS_EMP_NM
     ,(SELECT Z.CLAS_NM
         FROM AUSER.APDT_PRDT_CLAS Z
        WHERE Z.PRDT_CLAS_CD
              = B.PRDT_LRGE_CLAS_CD||B.PRDT_MID_CLAS_CD
          AND Z.CLAS_DIV_CD = '2') AS PRDT_NM
     ,A.RECV_TM       AS RECV_TM
     ,CASE A.CNCL_FG  
        WHEN '0' THEN '무'
        ELSE '유'
      END            AS CNCL_FG 
     ,CASE A.SETL_FG
        WHEN '0' THEN '무'
        ELSE '유'         
      END                      AS SETL_FG 
     ,A.SETL_NO                AS SETL_NO
     ,NVL(C.RCPT_PAMT,0)       AS RCPT_PAMT
     ,NVL(C.RCPT_INT,0)        AS RCPT_INT
     ,NVL(C.RCPT_DLY_INT, 0)   AS DLY_INT
     ,NVL(C.RCPT_LACK_AMT, 0)  AS LACK_AMT
     ,NVL(C.RCPT_DLY_UNRCV_AMT, 0)  AS DLY_UNRCV_AMT
     ,NVL(C.RCPT_ADJ_PAMT, 0)  AS ADJ_PAMT
     ,NVL(C.RCPT_DFEE,0)       AS RCPT_DFEE
     ,NVL(C.LEGL_ACTN_COST,0)  AS LEGL_ACTN_COST
     ,NVL(C.DSRC_OCCR_AMT,0)   AS DSRC_OCCR_AMT
     ,NVL(C.RCPT_AMT,0)        AS RCPT_AMT
     ,(select NVL(sum(Z.mm_pamt+Z.mm_int),0)
       from BUSER.BRCT_RCPT_DESC Z
     where Z.loan_no = A.LOAN_NO
         and Z.loan_seq = A.LOAN_SEQ
         and Z.setl_no =  A.SETL_NO
         and Z.due_dt > A.TR_DT
         and z.cncl_fg = '0')             AS PRE_RCPT_AMT
     ,C.DSRC_NO                           AS DSRC_NO
     ,CASE 
         WHEN D.DSRC_OCCR_AMT != D.DSRC_RAMT THEN '유'
         ELSE '무'
      END                               AS DSRC_ADJ_FG
     ,NVL(B.FLOT_IZE_DIV_CD||B.FLOT_IZE_SEQ, 'W01')   AS ACCT_UNIT_CD
     ,(SELECT CD_DESC_KOR_NM
          FROM GUSER.GBCT_COMM_CD_DESC Z
        WHERE Z.CD_KIND_NO = 'R00043'
             AND Z.CD_DESC_NO =  NVL(B.FLOT_IZE_DIV_CD||B.FLOT_IZE_SEQ, 'W01')) AS ACCT_UNIT_NM
      ,A.BANK_BRNCH_CD       AS BANK_BRNCH_CD
              ,A.TR_SNUM     AS TR_SNUM
      ,A.TRMN_CD             AS TRMN_CD
FROM BUSER.BVAT_CMS_IAMT_DESC A       
LEFT OUTER JOIN AUSER.ALOT_LOAN_BASE B 
      ON B.LOAN_NO = A.LOAN_NO  
     AND B.LOAN_SEQ = A.LOAN_SEQ
LEFT OUTER JOIN BUSER.BRCT_RCPT_BASE C
     ON C.LOAN_NO = A.LOAN_NO
     AND C.LOAN_SEQ = A.LOAN_SEQ
     AND C.SETL_NO = A.SETL_NO
LEFT OUTER JOIN BUSER.BSRT_RCPT_DSRC_BASE D
     ON D.DSRC_NO = C.DSRC_NO
LEFT OUTER JOIN CUSER.CART_BND_CLS_BASE E
     ON E.LOAN_NO = A.LOAN_NO
     AND E.LOAN_SEQ = A.LOAN_SEQ
WHERE
    A.TR_DT = '2011-12-20'
    -- ?condition
ORDER BY A.TR_DT, A.RECV_TM
;

select * FROM BUSER.BVAT_CMS_IAMT_DESC
where
    TR_DT = '2011-12-20'
;

select Z.TR_ORG_CD
         ,Z.TR_NO
         ,Z.LOAN_NO
         ,Z.LOAN_SEQ
         ,Z.ACCT_DT
         ,Z.SETL_DT
         ,Z.BANK_CD
         ,Z.ACNT_NO
         ,Z.SETL_AMT
         ,Z.RCPT_AMT
         ,Z.CASH_AMT
         ,Z.ACCT_UNIT_CD
from(
SELECT  A.TR_ORG_CD       AS TR_ORG_CD
         ,A.TR_NO           AS TR_NO
         ,A.LOAN_NO         AS LOAN_NO
         ,A.LOAN_SEQ        AS LOAN_SEQ
         ,A.TR_DT           AS ACCT_DT
         ,A.TR_DT           AS SETL_DT
         /* ,'1'               AS SETL_DIV_CD */
         ,B.BANK_CD         AS BANK_CD
         ,B.MO_ACNT_NO      AS ACNT_NO
         ,A.TR_AMT          AS SETL_AMT
         ,A.TR_AMT          AS RCPT_AMT
         ,A.TR_AMT          AS CASH_AMT
         ,B.ACCT_UNIT_CD    AS ACCT_UNIT_CD
         /*,:입력_부서_코드   입력_부서_코드     */
         /*,:입력_사원_번호   최초_등록_사원번호 */
    FROM BUSER.BVAT_CMS_IAMT_DESC A
         ,BUSER.BVAT_TR_ORG_BASE B
   WHERE A.RECV_DT   <= ? /* :수신_일자 */
     AND A.RECV_DT || A.RECV_TM   <= (
            CASE '1' WHEN ?
               THEN ?
               ELSE (A.RECV_DT || A.RECV_TM)
            END ) /* :수신_일자||수신_시각 */
     AND A.CNCL_FG   =  ? /* '0'        */
     AND A.SETL_FG   =  ? /* '0'        */
     AND B.TR_ORG_CD =  A.TR_ORG_CD
     ORDER BY A.TR_DT, A.RECV_TM , A.LOAN_NO
) Z where rownum <=10
;

SELECT  A.TR_ORG_CD       AS TR_ORG_CD
     ,A.TR_NO           AS TR_NO
     ,A.LOAN_NO         AS LOAN_NO
     ,A.LOAN_SEQ        AS LOAN_SEQ
     ,A.TR_DT           AS ACCT_DT
     ,A.TR_DT           AS SETL_DT
     /* ,'1'               AS SETL_DIV_CD */
     ,B.BANK_CD         AS BANK_CD
     ,B.MO_ACNT_NO      AS ACNT_NO
     ,A.TR_AMT          AS SETL_AMT
     ,A.TR_AMT          AS RCPT_AMT
     ,A.TR_AMT          AS CASH_AMT
     ,B.ACCT_UNIT_CD    AS ACCT_UNIT_CD
     /*,:입력_부서_코드   입력_부서_코드     */
     /*,:입력_사원_번호   최초_등록_사원번호 */
FROM BUSER.BVAT_CMS_IAMT_DESC A
     ,BUSER.BVAT_TR_ORG_BASE B
WHERE A.RECV_DT   <= ? /* :수신_일자 */
 AND A.RECV_DT || A.RECV_TM   <= (
        CASE '1' WHEN ?
           THEN ?
           ELSE (A.RECV_DT || A.RECV_TM)
        END ) /* :수신_일자||수신_시각 */
 AND A.CNCL_FG   =  ? /* '0'        */
 AND A.SETL_FG   =  ? /* '0'        */
 AND B.TR_ORG_CD =  A.TR_ORG_CD
 ORDER BY A.TR_DT, A.RECV_TM , A.LOAN_NO
;

select * from BUSER.BVAT_TR_ORG_BASE
;

select * 
FROM BUSER.BVAT_CMS_IAMT_DESC A
where
    tr_dt = '2011-12-20'
 AND A.CNCL_FG   =  '0' --? /* '0'        */
 AND A.SETL_FG   =  '0' --? /* '0'        */
;

-- 2 setting
    	"UPDATE BUSER.BVAT_CMS_IAMT_DESC                           \n" +
        "           SET                                         \n" +
        "               SETL_FG = ? /*'2'                     */  \n" +
        "               ,LAST_PROC_DT     = ?                      \n" +
        "               ,LAST_PROC_TM     = ?                      \n" +
        "               ,LAST_PROC_EMP_NO = ?                      \n" +
        "         WHERE TR_ORG_CD = ? /* :거래_기관_코드       */    \n" +
        "           AND TR_NO     = ? /* :거래_번호            */   \n";

--
--
--





