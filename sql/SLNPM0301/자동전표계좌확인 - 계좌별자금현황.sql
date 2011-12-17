-- 자동전표계좌확인 - 계좌별자금현황
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
        a.acct_dt = '2011-12-15'
        and A.ACCT_DEPT_CD = '330000'
        and A.TR_TP_CD = 'S0030' --'R0030'
    GROUP BY A.ACCT_DT,A.ACCT_DEPT_CD,A.TR_TP_CD,A.BANK_CD,A.ACNT_NO
    ORDER BY A.ACCT_DEPT_CD
;


--addWhere(where, "A.ACCT_DT"  ,DateFormatUtil.format(paramMap.get("acctDt").toString()) , "0");
--최영희님의 말:
--addWhere(where, "A.ACCT_DEPT_CD"   , (String)paramMap.get("acctDeptCd"), "0");
--최영희님의 말:
--addWhere(where, "A.TR_TP_CD"  , (String)paramMap.get("trTpCd"), "0");

--ACCT_DT	ACCT_DEPT_CD	ACCT_DEPT_NM	TR_TP_CD	TR_TP_NM	BANK_CD	BANK_NM	ACNT_NO	CNT	AMT
--2011-12-15	330000	영업팀	R0030	가상계좌	088	통합신한은행	100024062983	4	260000
--2011-12-15	330000	영업팀	S0030	대출지급_개인신용대출	020	우리은행	1002638811651	4	400000


