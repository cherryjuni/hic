-- PayCnfmProc.java
-- 741 line
-- getLoanPayObjListQuery()
SELECT
	 A.PAY_NO             -- 지급_번호
	,A.RCGN_NO            -- 식별_번호
	,A.LOAN_NO            -- 대출_번호
	,A.LOAN_SEQ           -- 대출_순번
	,A.PAY_BSN_DIV_CD     -- 지급_업무_구분코드
	,A.CUST_NM            -- 고객명
	,A.MNG_DEPT_CD        -- 관리_부서_코드
	,A.SETL_BANK_CD       -- 결제_은행_코드
	,A.SETL_ACNT_NO       -- 결제_계좌번호
	,A.DEPO_OWN_NO        -- 예금주_번호
	,A.DEPO_OWNNM         -- 예금주명
	,A.IAMT_MANMN         -- 입금_인명
	,A.LOAN_PAMT          -- 대출_원금
	,A.TRT_FEE            -- 취급_수수료
	,A.CPRT_FEE           -- 제휴_수수료
	,A.STMP_FEE           -- 인지대
	,A.TRAN_RQST_AMT      -- 이체_요청_금액
	,A.TRAN_RQST_DTTM     -- 이체_요청_일시
	,A.TRAN_PROC_DT       -- 이체_처리_일자
	,A.TR_BANK_CD         -- 거래_은행_코드
	,A.OAMT_FEE           -- 출금_수수료
	,A.GRAM_SEND_DTTM     -- 전문_전송_일시
	,A.GRAM_CHSE_NO       -- 전문_추적_번호
	,A.TR_STRT_TM         -- 거래_개시_시각
	,A.RSPN_CD            -- 응답_코드
	,A.PROC_STAT_CD       -- 처리_상태_코드
	,A.OAMT_AFAMT         -- 출금_후잔액
	,A.MNO_RQST_OBJ_FG    -- 결번_요청_대상_여부
	,A.TRAN_IPSS_GRAM_NO  -- 이체_불능_전문_번호
	,A.VOCH_JNL_LAST_SEQ  -- 전표_분개_최종_순번
	,A.LOAN_CNT           -- 대출_건수
	,B.BANK_CD	     	  -- 모계좌 은행_코드
	,B.MO_ACNT_NO	      -- 모계좌 계좌_번호
   ,C.PL_PRDT_DIV        -- PL상품_구분
FROM AUSER.ALOT_LOAN_ONLN_PAY A,
     AUSER.ALOT_MO_ACNT_BASE  B,
     AUSER.ALOT_LOAN_BASE     C
WHERE A.TRAN_PROC_DT      = '2011-12-18' --?
  AND A.PROC_STAT_CD    = '03' -- 처리상태코드(미응답)
--  AND A.PROC_STAT_CD    = '01' -- 처리상태코드(미처리)
--  AND A.PROC_STAT_CD    = '05' -- 처리상태코드(정상)
  AND A.PAY_BSN_DIV_CD    = B.BSN_DIV_CD                  /*지급업무구분코드 = 업무구분코드 */
  AND DECODE(A.SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020')  = B.BANK_CD
  AND A.LOAN_NO           = C.LOAN_NO(+)
  AND A.LOAN_SEQ          = C.LOAN_SEQ(+)
;
