
-- VochProcDao.java:142
SELECT 						
   A.LOAN_PAMT,				
   A.STMP_FEE,					
   A.PAY_AMT,					
   0 TRT_FEE,	
   0 CPRT_FEE,					
   A.SET_FEE,					
   A.NOTA_FEE,					
   A.ETC_FEE,					
   A.LOAN_NO, 					
   A.LOAN_SEQ,	   				
   '0' RT_FEE_DFER_PROC_FG,		
   '0' CPRT_FEE_DFER_PROC_FG,	
   C.BANK_CD,	        	
   C.MO_ACNT_NO,		
   '0' STMP_FEE_AF_ACQT_FG, 
   A.PRXP_PAMT,		
   A.PRXP_INT		
FROM			
   AUSER.ALOT_LOAN_PAY_DESC A,  
   AUSER.APDT_PRDT_BASE     B,  
   AUSER.ALOT_MO_ACNT_BASE  C   
WHERE A.LOAN_NO  = '11121600004' --?	     
  AND A.LOAN_SEQ = '01' --?	     
  AND A.SNUM     = '01' --?	     
  AND A.PRDT_CD  = B.PRDT_CD    
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD 
  and DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD
;

--2011-12-17 18:27:31, DEBUG [main](VochProcDao.java:143) : arg1= 11121600004
--2011-12-17 18:27:31, DEBUG [main](VochProcDao.java:144) : arg2= 01
--2011-12-17 18:27:31, DEBUG [main](VochProcDao.java:145) : arg3= 01

SELECT  
         A.ACCT_DT
        ,A.ACCT_DEPT_CD AS ACCT_DEPT_CD    
        ,(SELECT Z.DEPT_NM
           FROM DUSER.DHRT_ORG Z
          WHERE Z.DEPT_CD = A.ACCT_DEPT_CD)                    AS ACCT_DEPT_NM
        
        ,A.TR_TP_CD AS TR_TP_CD    
       ,(SELECT Z.CD_DESC_KOR_NM
           FROM GUSER.GBCT_COMM_CD_DESC Z
          WHERE Z.CD_KIND_NO = 'A00068'
            AND Z.CD_DESC_NO = A.TR_TP_CD)                     AS TR_TP_NM    
       
        ,A.BANK_CD AS BANK_CD
        ,(SELECT Z.CD_DESC_KOR_NM
           FROM GUSER.GBCT_COMM_CD_DESC Z
          WHERE Z.CD_KIND_NO = 'A00003'
            AND Z.CD_DESC_NO = A.BANK_CD)                     AS BANK_NM   
        
        ,A.ACNT_NO AS ACNT_NO
        ,COUNT(*) AS CNT
        ,SUM(A.VOCH_AMT) AS AMT 
    FROM BUSER.BCLT_CLS_SUB_DESC A
    WHERE
       acct_dt >= '2011-12-15'
    GROUP BY A.ACCT_DT,A.ACCT_DEPT_CD,A.TR_TP_CD,A.BANK_CD,A.ACNT_NO
    ORDER BY A.ACCT_DEPT_CD
    ;
    
select * from BUSER.BCLT_CLS_SUB_DESC A
where acct_dt = '2011-12-17';

SELECT
 A.CMNG_NO
,A.CMNG_NO1
,A.CMNG_NO2
,A.ACCT_CD
,A.DC_DIV_CD
,A.MNG_NO_DIV_CD
,A.ACCT_DT
,A.ACCT_DEPT_CD
,A.TR_TP_CD
,A.TR_STAG_CD
,A.TRAN_TP_CD
,A.TRAN_NO
,A.MNG_DEPT_CD
,A.VOCH_AMT
,CASE WHEN A.ACCT_CD='23' THEN A.MNG_DEPT_CD WHEN A.ACCT_CD='14' THEN A.MNG_DEPT_CD ELSE A.MNG_NO END MNG_NO
,A.LOAN_NO
,A.LOAN_SEQ
,A.CNT-1 CNT
 FROM
(SELECT
 B.CMNG_NO
,B.CMNG_NO1
,B.CMNG_NO2
,A.ACCT_CD
,'D' DC_DIV_CD
,(SELECT C.MNG_NO_DIV_CD FROM EUSER.EACT_ACCT C WHERE A.ACCT_CD=C.ACCT_CD AND C.ACCT_YY=SUBSTR(?acctDt,1,4)) MNG_NO_DIV_CD
,MAX(A.ACCT_DT) ACCT_DT
,MAX(A.ACCT_DEPT_CD) ACCT_DEPT_CD
,MAX(A.TR_TP_CD) TR_TP_CD
,MAX(A.TR_STAG_CD) TR_STAG_CD
,MAX(A.TRAN_TP_CD) TRAN_TP_CD
,MAX(A.TRAN_NO) TRAN_NO
,MAX(A.MNG_DEPT_CD) MNG_DEPT_CD
,SUM(A.VOCH_AMT) VOCH_AMT
,CASE (SELECT C.MNG_NO_DIV_CD FROM EUSER.EACT_ACCT C WHERE A.ACCT_CD=C.ACCT_CD AND C.ACCT_YY=SUBSTR(?acctDt,1,4))
   WHEN '02' THEN (SELECT C.DEPO_NO FROM EUSER.EFDT_DEPO_BASE C WHERE REPLACE(C.ACNT_NO,'-','')=REPLACE(B.CMNG_NO2,'-',''))
   WHEN '06' THEN B.CMNG_NO1
   WHEN '08' THEN '20'
   ELSE ''
 END MNG_NO
,MAX(B.LOAN_NO) LOAN_NO
,MAX(B.LOAN_SEQ) LOAN_SEQ
,COUNT(B.LOAN_NO) CNT
 FROM BUSER.BCLT_CLS_SUB_DESC A
,(SELECT
 A.TR_DESC_NO
,MAX(CASE WHEN A.DC_DIV_CD='C' AND A.ACCT_CD='23' THEN A.MNG_DEPT_CD ELSE '' END) CMNG_NO
,MAX(CASE WHEN A.DC_DIV_CD='D' AND A.ACCT_CD='14' THEN A.MNG_DEPT_CD ELSE '' END) CMNG_NO1
,MAX(CASE WHEN A.DC_DIV_CD='D' AND A.ACCT_CD='11010101' THEN A.ACNT_NO ELSE '' END) CMNG_NO2
,MAX(A.LOAN_NO) LOAN_NO
,MAX(A.LOAN_SEQ) LOAN_SEQ
FROM BUSER.BCLT_CLS_BASE C,BUSER.BCLT_CLS_SUB_DESC A
WHERE C.ACCT_DT=?acctDt
AND C.ACCT_DEPT_CD=?acctDeptCd
AND C.TRAN_TP_CD=?tranTpCd
AND C.TRAN_NO=?tranNo
AND C.ACCT_UNIT_CD=?acctUnitCd
AND C.ACCT_DT=A.ACCT_DT
AND C.ACCT_DEPT_CD=A.ACCT_DEPT_CD
AND C.TRAN_TP_CD=A.TRAN_TP_CD
AND C.TRAN_NO=A.TRAN_NO
GROUP BY A.TR_DESC_NO) B
WHERE A.TR_DESC_NO=B.TR_DESC_NO
AND A.ACCT_DT=?acctDt
AND A.ACCT_DEPT_CD=?acctDeptCd
AND A.TRAN_TP_CD=?tranTpCd
AND A.TRAN_NO=?tranNo
AND A.ACCT_UNIT_CD=?acctUnitCd
AND A.DC_DIV_CD='D'
GROUP BY B.CMNG_NO,B.CMNG_NO1,B.CMNG_NO2,A.ACCT_CD
UNION 
SELECT B.CMNG_NO,B.CMNG_NO1,B.CMNG_NO2,A.ACCT_CD,'C' DC_DIV_CD
,(SELECT C.MNG_NO_DIV_CD FROM EUSER.EACT_ACCT C WHERE A.ACCT_CD=C.ACCT_CD AND C.ACCT_YY=SUBSTR(?acctDt,1,4)) MNG_NO_DIV_CD
,MAX(A.ACCT_DT) ACCT_DT,MAX(A.ACCT_DEPT_CD) ACCT_DEPT_CD,MAX(A.TR_TP_CD) TR_TP_CD,MAX(A.TR_STAG_CD) TR_STAG_CD
,MAX(A.TRAN_TP_CD) TRAN_TP_CD,MAX(A.TRAN_NO) TRAN_NO,MAX(A.MNG_DEPT_CD) MNG_DEPT_CD,SUM(A.VOCH_AMT) VOCH_AMT
,CASE (SELECT C.MNG_NO_DIV_CD FROM EUSER.EACT_ACCT C WHERE A.ACCT_CD=C.ACCT_CD AND C.ACCT_YY=SUBSTR(?acctDt,1,4))
  WHEN '02' THEN (SELECT C.DEPO_NO FROM EUSER.EFDT_DEPO_BASE C WHERE REPLACE(C.ACNT_NO,'-','')=REPLACE(B.CMNG_NO2,'-',''))
  WHEN '06' THEN B.CMNG_NO1
  WHEN '08' THEN '20'
  ELSE ''
 END MNG_NO
,MAX(B.LOAN_NO) LOAN_NO
,MAX(B.LOAN_SEQ) LOAN_SEQ
,COUNT(B.LOAN_NO) CNT
 FROM BUSER.BCLT_CLS_SUB_DESC A
,(SELECT A.TR_DESC_NO
,MAX(CASE WHEN A.DC_DIV_CD='C' AND A.ACCT_CD='23' THEN A.MNG_DEPT_CD ELSE '' END) CMNG_NO
,MAX(CASE WHEN A.DC_DIV_CD='D' AND A.ACCT_CD='14' THEN A.MNG_DEPT_CD ELSE '' END) CMNG_NO1
,MAX(CASE WHEN A.DC_DIV_CD='D' AND A.ACCT_CD='11010103' THEN A.ACNT_NO ELSE '' END) CMNG_NO2
,MAX(A.LOAN_NO) LOAN_NO
,MAX(A.LOAN_SEQ) LOAN_SEQ
 FROM
 BUSER.BCLT_CLS_BASE C
,BUSER.BCLT_CLS_SUB_DESC A
WHERE C.ACCT_DT=?acctDt
AND C.ACCT_DEPT_CD=?acctDeptCd
AND C.TRAN_TP_CD=?tranTpCd
AND C.TRAN_NO=?tranNo
AND C.ACCT_UNIT_CD=?acctUnitCd
AND C.ACCT_DT=A.ACCT_DT
AND C.ACCT_DEPT_CD=A.ACCT_DEPT_CD
AND C.TRAN_TP_CD=A.TRAN_TP_CD
AND C.TRAN_NO=A.TRAN_NO
GROUP BY A.TR_DESC_NO) B
WHERE A.TR_DESC_NO=B.TR_DESC_NO
AND A.ACCT_DT=?acctDt
AND A.ACCT_DEPT_CD=?acctDeptCd
AND A.TRAN_TP_CD=?tranTpCd
AND A.TRAN_NO=?tranNo
AND A.ACCT_UNIT_CD=?acctUnitCd
AND A.DC_DIV_CD='C'
GROUP BY B.CMNG_NO,B.CMNG_NO1,B.CMNG_NO2,A.ACCT_CD) A
WHERE A.VOCH_AMT!=0
;
