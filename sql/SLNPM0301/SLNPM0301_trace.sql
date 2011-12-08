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
  A.TRAN_PROC_DT    = '2011-11-28'     	
  AND A.TRAN_RQST_DTTM <= '20111201191510'     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
--  AND DECODE(A.TR_BANK_CD, '088', '026', A.TR_BANK_CD)  = B.BANK_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

SELECT A.TR_BANK_CD, DECODE(nvl(A.TR_BANK_CD,'020'), '088', '026', nvl(A.TR_BANK_CD,'020')) ab
FROM AUSER.ALOT_LOAN_ONLN_PAY A
where
  A.TRAN_PROC_DT    = '2011-11-28'     	
  AND A.TRAN_RQST_DTTM <= '20111201191510'     	
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
WHERE A.PAY_NO    = '11112800000002' --?
  AND A.LOAN_NO   = B.LOAN_NO
  AND A.LOAN_SEQ  = B.LOAN_SEQ
  AND A.CNCL_FG  != '1'
;
--2011-12-02 14:55:12,  INFO [main](SLNPM0301Dao.java:109) : arg1=11112800000002
;

-- 11112800002	01
select * from AUSER.ALOT_LOAN_PAY_DESC
where pay_no = '11112800000002'
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
WHERE L.LOAN_NO ='11112800002'
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
    loan_no = '11112800002'
    and loan_seq = '01'
;




------------------
select
	SUM(PAY_AMT) PAY_AMT_SUM
FROM AUSER.ALOT_LOAN_PAY_DESC
WHERE PAY_NO          = '11112800000002' --?
AND   PAY_STAT_CD     = '1'
;
--2011-12-02 14:12:10,  INFO [main](SLNPM0301Dao.java:157) : arg1=11112800000002


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



