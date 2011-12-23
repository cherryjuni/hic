--AUSER.ALOT_MO_ACNT_BASE
--
--AUSER.ALOT_LOAN_ONLN_PAY
--TRAN_PROC_DT in ( '2011-12-16', '2011-12-20', '2011-12-17')
--
--AUSER.ALOT_LOAN_PAY_DESC
--rqst_dt = '2011-12-16'
--
--BUSER.BCLT_CLS_SUB_DESC
--acct_dt >= '2011-12-16'
--
--EUSER.EFDT_DEPO_BASE
--
--BUSER.BCLT_CLS_BASE
--acct_dt = '2011-12-16'
--
--GUSER.GBCT_COMM_CD_DESC 
--cd_kind_no = 'A00068'  --cd_desc_no = 'S0020' --
--
--GUSER.GSCT_GRAM_CHNL
--
--guser.GSCT_GRAM_BASE
--
--AUSER.ALOT_LOAN_PAY_DESC@hiplus_link





SELECT                	  
	A.PAY_NO             ,-- 지급_번호
	A.RCGN_NO            ,-- 식별_번호
	A.LOAN_NO            ,-- 대출_번호
	A.LOAN_SEQ           ,-- 대출_순번
	A.PAY_BSN_DIV_CD     ,-- 지급_업무_구분코드
	A.CUST_NM            ,-- 고객명
	A.MNG_DEPT_CD        ,-- 관리_부서_코드
	A.SETL_BANK_CD       ,-- 결제_은행_코드
	A.SETL_ACNT_NO       ,-- 결제_계좌번호
	A.DEPO_OWN_NO        ,-- 예금주_번호
	A.DEPO_OWNNM         ,-- 예금주명
	A.IAMT_MANMN         ,-- 입금_인명
	A.LOAN_PAMT          ,-- 대출_원금
	A.TRT_FEE            ,-- 취급_수수료
	A.CPRT_FEE           ,-- 제휴_수수료
	A.STMP_FEE           ,-- 인지대
	A.TRAN_RQST_AMT      ,-- 이체_요청_금액
	A.TRAN_RQST_DTTM     ,-- 이체_요청_일시
	A.TRAN_PROC_DT       ,-- 이체_처리_일자
	A.TR_BANK_CD         ,-- 거래_은행_코드
	A.OAMT_FEE           ,-- 출금_수수료
	A.GRAM_SEND_DTTM     ,-- 전문_전송_일시
	A.GRAM_CHSE_NO       ,-- 전문_추적_번호
	A.TR_STRT_TM         ,-- 거래_개시_시각
	A.RSPN_CD            ,-- 응답_코드
	A.PROC_STAT_CD       ,-- 처리_상태_코드
	A.OAMT_AFAMT         ,-- 출금_후잔액
	A.MNO_RQST_OBJ_FG    ,-- 결번_요청_대상_여부
	A.TRAN_IPSS_GRAM_NO  ,-- 이체_불능_전문_번호
	A.VOCH_JNL_LAST_SEQ  ,-- 전표_분개_최종_순번
	A.LOAN_CNT           ,-- 대출_건수
	B.BANK_CD	      	 ,-- 은행_코드
	B.MO_ACNT_NO	     ,-- 모_계좌번호
   C.PL_PRDT_DIV         -- PL_상품_구분
FROM AUSER.ALOT_LOAN_ONLN_PAY A, -- ALOT_대출_지급_내역      
     AUSER.ALOT_MO_ACNT_BASE  B, -- ALOT_모계좌_기본          
     AUSER.ALOT_LOAN_BASE     C  -- ALOT_대출_기본             
WHERE
    A.PROC_STAT_CD    = '01' --'04' --'01'   	
  AND
  A.TRAN_PROC_DT    = '2011-12-19'     	
  AND A.TRAN_RQST_DTTM <= '20111219162524'     	-- 은행코드 '023' 임 테스트위해 수정
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
--  AND A.SETL_BANK_CD = B.BANK_CD
  AND DECODE(A.SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = B.BANK_CD 
--  and '088' = b.bank_cd
--  and '020' = b.bank_cd
--  AND DECODE(nvl(A.TR_BANK_CD,'020'), '088', '026', '020')  = B.BANK_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

select tr_bank_cd, count(*)
from AUSER.ALOT_LOAN_ONLN_PAY A
group by tr_bank_cd
;

select * from AUSER.ALOT_LOAN_ONLN_PAY A
;

SELECT
    a.setl_bank_cd
    , DECODE(A.SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020')
    , A.TR_BANK_CD
    , DECODE(nvl(A.TR_BANK_CD,'020'), '088', '026', nvl(A.TR_BANK_CD,'020')) ab
FROM AUSER.ALOT_LOAN_ONLN_PAY A
where
  A.TRAN_PROC_DT    = '2011-12-06' --'2011-11-28'     	
  AND A.TRAN_RQST_DTTM <= '20111219191510'     	
;

SELECT
	SUM(PAY_AMT) PAY_AMT_SUM
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11121400000004' --?
AND   PAY_STAT_CD     = '1'
;

select *
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE
    RQST_DT = '2011-12-14'
--    PAY_NO          = '11121400000004' --?
--AND   PAY_STAT_CD     = '1'
;


-- 연체율 정보
SELECT * FROM AUSER.ALOT_DFEE_RT_DESC
WHERE LOAN_NO = '11121600005' --'11121600004' AND LOAN_SEQ = '01'
;

-- 대출원리금
select * from AUSER.ALOT_LOAN_PIAMT
WHERE LOAN_NO = '11121600005' AND LOAN_SEQ = '01'
;


-- 대출금 조회
SELECT 							
   A.PRDT_LRGE_CLAS_CD,			
   A.PRDT_MID_CLAS_CD, 			
   A.LOAN_STAT_CD,	   			
   B.BND_STAT_CD, 	   			
   A.MNG_DEPT_CD,	   			
   A.LOAN_TERM,		   		    
   '' CPRT_COM_NO 		        
FROM					
   AUSER.ALOT_LOAN_BASE      A LEFT OUTER JOIN    CUSER.CART_BND_CLS_BASE   B
   ON  A.LOAN_NO = B.LOAN_NO		
   AND A.LOAN_SEQ = B.LOAN_SEQ      
WHERE  A.LOAN_NO  = '11121600005' --?    			
   AND A.LOAN_SEQ = '01' --?    			
;


--------------------------------------------------------------
--------------------------------------------------------------
--------------------------------------------------------------
-- 전표처리 시작 ---------------------------------------------
--2011-12-18 15:18:42, DEBUG [main](VochProcDao.java:43) :
--공통조회항목 조회 SQL= 
-- VochProcDao.selectCommonItem();
SELECT 							
   A.PRDT_LRGE_CLAS_CD,			
   A.PRDT_MID_CLAS_CD, 			
   A.LOAN_STAT_CD,	   			
   B.BND_STAT_CD, 	   			
   A.MNG_DEPT_CD,	   			
   A.LOAN_TERM,		   		    
   '' CPRT_COM_NO 		        
FROM					
   AUSER.ALOT_LOAN_BASE A
   LEFT OUTER JOIN CUSER.CART_BND_CLS_BASE B
   ON  A.LOAN_NO = B.LOAN_NO		
   AND A.LOAN_SEQ = B.LOAN_SEQ      
WHERE  A.LOAN_NO  = '11121600005' --?    			
   AND A.LOAN_SEQ = '01' --? 
;
--2011-12-18 15:18:45, DEBUG [main](VochProcDao.java:44) : arg1= 11121600004
--2011-12-18 15:18:46, DEBUG [main](VochProcDao.java:45) : arg2= 01

-- 대출지급 - 전표모듈 처리전
-- 정보 읽기
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
WHERE A.LOAN_NO  = '11121600005' --?
  AND A.LOAN_SEQ = '01' --?
  AND A.SNUM     = '01' --?
  AND A.PRDT_CD  = B.PRDT_CD
  AND A.PAY_BSN_DIV_CD  = C.BSN_DIV_CD
  AND DECODE(A.BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = C.BANK_CD
;

--2011-12-18 15:38:39, DEBUG [main](VochProcDao.java:143) : arg1= 11121600004
--2011-12-18 15:38:39, DEBUG [main](VochProcDao.java:144) : arg2= 01
--2011-12-18 15:38:39, DEBUG [main](VochProcDao.java:145) : arg3= 01


-- 예가수 처리
-- 예가수 번호 찾기
-- selectDsrcNo()
SELECT DSRC_NO
FROM BUSER.BSRT_RCPT_DSRC_BASE
WHERE OCCR_LOAN_NO = ?
    AND OCCR_LOAN_SEQ = ?
    AND DSRC_OCCR_CD = ?
    AND DSRC_RAMT = ?
ORDER BY DSRC_NO DESC
FETCH FIRST ROW ONLY
;




select * from CUSER.CART_BND_CLS_BASE
WHERE  LOAN_NO  = '11121600005' --?    			
   AND LOAN_SEQ = '01' --? 
;



--
-- SLNPM0301Dao.updatePayObj(String statusCd, String payNo, Connection con)
--      SLNPM0301Query.getLoanPayUpdateQuery()
--대출실시간 지급대상) 상태코드 수정
--UPDATE AUSER.ALOT_LOAN_ONLN_PAY
     SET PROC_STAT_CD = ?  ,
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE PAY_NO       = ?
;
--arg1=02
--arg2=11112800000002

-- SLNPM0301Dao.updateLoanPayDesc(String statusCd, String payNo, Connection con)
--      SLNPM0301Query.getLoanPayDescUpdateSuccQuery()
--      or
--      SLNPM0301Query.getLoanPayDescUpdateFailQuery()
--대출실시간 지급대상) 상태코드 수정

--UPDATE AUSER.ALOT_LOAN_PAY_DESC
     SET PAY_STAT_CD  = ?,        				 /* 상태코드   */
         PAY_DT       = TO_CHAR(SYSDATE,'YYYY-MM-DD'), 	 /* 지급일자   */
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE PAY_NO       = ?        				 /* 지급번호   */
;
--arg1=5 - 지급완료
--arg2=11112800000002

-- SLNPM0301Dao.getLoanPayObj(String payNo,Connection con)
--    SLNPM0301Query.getLoanPayObjQuery()
SELECT
	A.PAY_BSN_DIV_CD,
	'' AS CPRT_COM_NO,
	A.CNCL_FG,
	A.RQST_DIV_CD,
	A.SNUM,
	B.LOAN_NO,
	B.LOAN_SEQ,
	B.PRDT_LRGE_CLAS_CD,
	B.PRDT_MID_CLAS_CD,
	B.LOAN_INT_RT,
	B.LOAN_DT,
	B.PRDT_CD,
	B.LOAN_PAMT,
	B.LOAN_TERM,
	B.SETL_DD,
	B.FCNT_DUE_DT,
	B.FCNT_MOPA_CALC_FG,
	B.CUST_TRT_FEE,
	B.CPRT_COM_TRT_FEE,
	B.IMG_ACNT_BANK_CD,
	B.MNG_DEPT_CD,
	B.CPRT_FEE,
   0 AS  PR_FEE,
	B.DUE_METH_CD,
   B.DFEE_RT,
   B.DLY_INT_RT
FROM AUSER.ALOT_LOAN_PAY_DESC A,
     AUSER.ALOT_LOAN_BASE     B
WHERE A.PAY_NO    = '11120600000001' --'11112800000002' --?
  AND A.LOAN_NO   = B.LOAN_NO
  AND A.LOAN_SEQ  = B.LOAN_SEQ
  AND A.CNCL_FG  != '1'
;
--2011-12-02 14:55:12,  INFO [main](SLNPM0301Dao.java:109) : arg1=11112800000002
;

-- 11112800002	01
select * from AUSER.ALOT_LOAN_PAY_DESC
where pay_no = '11120600000001' --'11112800000002'
;

-- 가상계좌생성
-- 기 가상계좌 정보 확인
SELECT IMG_ACNT_NO FROM
(SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE
WHERE CONT_MAN_NO =(SELECT L.CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE L
WHERE L.LOAN_NO =?
AND L.LOAN_SEQ=?)
AND LOAN_STAT_CD ='22'
AND PRDT_MID_CLAS_CD ='22'
AND IMG_ACNT_NO IS NOT NULL OR IMG_ACNT_NO <> '')
WHERE ROWNUM = 1
;

-- 56201550661748
SELECT IMG_ACNT_NO FROM
(SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE
WHERE CONT_MAN_NO =(SELECT L.CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE L
WHERE L.LOAN_NO ='11120600001' --'11112800002'
AND L.LOAN_SEQ='01')
AND LOAN_STAT_CD ='22'
AND PRDT_MID_CLAS_CD ='22'
AND IMG_ACNT_NO IS NOT NULL OR IMG_ACNT_NO <> '')
WHERE ROWNUM = 1
;

--미사용가상계좌 조회
-- 56201550667571
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
--arg1= 0266409069848191

SELECT IMG_ACNT_NO
FROM (
 SELECT IMG_ACNT_NO
 FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
 WHERE IMG_ACNT_BANK_CD = '020'
  AND USE_FG = '0'
  AND DISU_DT IS NULL
)
WHERE ROWNUM = 1
;


--UPDATE AUSER.ALOT_LOAN_IMG_ACNT_DESC
SET USE_FG = '1'
    ,LAST_PROC_DT = ?
    ,LAST_PROC_TM = ?
    ,LAST_PROC_EMP_NO = ?
WHERE IMG_ACNT_BANK_CD = ?
AND IMG_ACNT_NO = ?
;

--arg3= userEmpNo
--arg4= 026
--arg5= 56201550667571
select *
from AUSER.ALOT_LOAN_IMG_ACNT_DESC
where 
    IMG_ACNT_BANK_CD = '026' -- 신한 '026' 우리 '020'
AND IMG_ACNT_NO = '56201550667571'
;

SELECT IMG_ACNT_NO
FROM (
 SELECT IMG_ACNT_NO
 FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
 WHERE IMG_ACNT_BANK_CD = '026'
  AND USE_FG = '0'
  AND DISU_DT IS NULL
)
WHERE ROWNUM = 1
;

-- 가상계좌 이력 추가
--INSERT INTO AUSER.ALOT_LOAN_IMG_ACNT_HIST
(IMG_ACNT_BANK_CD
,IMG_ACNT_NO
,CHNG_SNUM
,LOAN_NO
,LOAN_SEQ
,USE_FG
,USE_CMPL_DT
,DISU_DT
,RMRK
,ISRT_DEPT_CD
,FRST_REG_DT
,FRST_REG_TM
,FRST_REG_EMP_NO
,LAST_PROC_DT
,LAST_PROC_TM
,LAST_PROC_EMP_NO)
VALUES
 (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
;
--arg1= 026
--arg1= 56201550667571

select * from AUSER.ALOT_LOAN_IMG_ACNT_HIST
where IMG_ACNT_NO = '56201550667571'
    and IMG_ACNT_BANK_CD = '026'
;


-- 대출기본갱신
--UPDATE AUSER.ALOT_LOAN_BASE
SET	  IMG_ACNT_BANK_CD = ?
	 ,IMG_ACNT_NO = ?
	 ,LAST_PROC_DT = ?
	 ,LAST_PROC_TM = ?
	 ,LAST_PROC_EMP_NO = ?
WHERE  LOAN_NO = ?
AND    LOAN_SEQ = ?
;

--arg1= 026
--arg2= 56201550667571
--arg6= 11112800002
--arg7= 01

select * from AUSER.ALOT_LOAN_BASE
where
    loan_no = '' --'11112800002'
    and loan_seq = '01'
;



-- 대출지급금액합계
------------------
select
	SUM(PAY_AMT) PAY_AMT_SUM
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11120600000001' --'11112800000002' --?
AND   PAY_STAT_CD     = '1'
;
--2011-12-02 14:12:10,  INFO [main](SLNPM0301Dao.java:157) : arg1=11112800000002

select
	*
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11120600000001' --'11112800000002' --?
--AND   PAY_STAT_CD     = '1'
;

-- 대출지급내역, 대출기본 조회
-- getLoanPayObjQuery()
SELECT  						
	A.PAY_BSN_DIV_CD,			
	'' AS CPRT_COM_NO,			
	A.CNCL_FG,					
	A.RQST_DIV_CD,				
	A.SNUM,						
	B.LOAN_NO, 					
	B.LOAN_SEQ, 				
	B.PRDT_LRGE_CLAS_CD, 		
	B.PRDT_MID_CLAS_CD, 		
	B.LOAN_INT_RT,				
	B.LOAN_DT,					
	B.PRDT_CD,					
	B.LOAN_PAMT,				
	B.LOAN_TERM,				
	B.SETL_DD,					
	B.FCNT_DUE_DT,				
	B.FCNT_MOPA_CALC_FG,		
	B.CUST_TRT_FEE,				
	B.CPRT_COM_TRT_FEE,			
	B.IMG_ACNT_BANK_CD,			
	B.MNG_DEPT_CD,				
	B.CPRT_FEE,				    
   0 AS  PR_FEE,               
	B.DUE_METH_CD,				
   B.DFEE_RT,                  
   B.DLY_INT_RT                
FROM AUSER.ALOT_LOAN_PAY_DESC A,   
     AUSER.ALOT_LOAN_BASE     B    
WHERE A.PAY_NO          = '11120600000001' --?  	   	
  AND A.LOAN_NO   = B.LOAN_NO    	
  AND A.LOAN_SEQ  = B.LOAN_SEQ   	
  AND A.CNCL_FG  != '1' 
;

--연체료율 내역 Table에서 기존 데이터를 삭제
select * from auser.alot_dfee_rt_desc
--delete from auser.alot_dfee_rt_desc
 where loan_no = '11112800002' --?
   and loan_seq = '01' --?
;
--2011-12-02 15:13:47, DEBUG [main](DfeeRtCrte.java:345) : arg1= 11112800002
--2011-12-02 15:13:48, DEBUG [main](DfeeRtCrte.java:346) : arg2= 01

-- 대출기본정보수정
-- 대출기본 수정
--UPDATE AUSER.ALOT_LOAN_BASE
     SET PAY_DT        = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_ACCT_DT  = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         INT_STRT_DT   = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_DT       = ?   ,
         LOAN_STAT_CD  = '22',
         DFEE_RT       = ?   ,
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE LOAN_NO       = ?
     AND LOAN_SEQ      = ?
;
--2011-12-02 15:18:15,  INFO [main](SLNPM0301Dao.java:550) : arg1= 2011-12-02
--2011-12-02 15:18:15,  INFO [main](SLNPM0301Dao.java:551) : arg2= 0.0
--2011-12-02 15:18:16,  INFO [main](SLNPM0301Dao.java:552) : arg3= 11112800002
--2011-12-02 15:18:16,  INFO [main](SLNPM0301Dao.java:553) : arg4= 01
--UPDATE AUSER.ALOT_LOAN_BASE
     SET PAY_DT        = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_ACCT_DT  = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         INT_STRT_DT   = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LOAN_DT       = '2011-12-02'   ,
         LOAN_STAT_CD  = '22',
         DFEE_RT       = '0.0'   ,
         LAST_PROC_DT = TO_CHAR(SYSDATE,'YYYY-MM-DD'),
         LAST_PROC_TM = TO_CHAR(SYSDATE,'hh24miss'),
         LAST_PROC_EMP_NO = 'SLNPM031'
   WHERE LOAN_NO       = '11112800002'
     AND LOAN_SEQ      = '01'
;


-- 마감데이터 추가
select * from BUSER.BCLT_CLS_TR_DESC
where acct_dt ='2011-12-18'
;

select * from BUSER.BCLT_CLS_TR_DESC
where acct_dt ='2011-12-19'
;

select * from BUSER.BCLT_CLS_TR_DESC
where acct_dt >= '2011-12-18'
;

SELECT (MAX(CD_DESC_NO))                            
FROM GUSER.GBCT_COMM_CD_DESC Z                           
WHERE Z.CD_KIND_NO = 'R00044'
;
select * from BUSER.BGMT_COMM_DUMMY;


select *
from BUSER.BCLT_CLS_SUB_DESC
where acct_dt >= '2011-12-18'
;

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




select * from AUSER.ASCT_INTN_VOCH
where acct_dt >= '2011-12-18'
;


select * from AUSER.ALOT_LOAN_PAY_DESC
where loan_no = '11121600005'
;

--UPDATE AUSER.ALOT_LOAN_PAY_DESC 	
SET PAY_INTN_VOCH_NO 	= ?		
WHERE LOAN_NO 		= ?		
 AND  LOAN_SEQ		= ?		
 AND  SNUM    		= ?		
;




SELECT
	SUM(PAY_AMT) PAY_AMT_SUM
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11112800000002' --?
AND   PAY_STAT_CD     = '1'
;

SELECT
	*
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11112800000002' --?
;



