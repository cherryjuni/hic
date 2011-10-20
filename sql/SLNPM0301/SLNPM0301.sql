
select * from GUSER.GBCT_COMM_CD_DESC;


select *
from GUSER.GBCT_COMM_CD_KIND
where cd_kind_nm like '%지급업무%' 
;

-- 지급배치사용하는코드 'S00045'
-- 03, 06, 10 만 사용중
select *
from GUSER.GBCT_COMM_CD_KIND
where cd_kind_no = 'S00045'
;
select * from GUSER.GBCT_COMM_CD_DESC
where cd_kind_no = 'S00045'
;



select *
from GUSER.GBCT_BSN_DIVS_MAN
;

select *
from GUSER.GSCT_GRAM_BASE
;

select *
from GUSER.GSCT_GRAM_CHNL
order by chnl_cd
;

-- 공통 - 전문실행테스트 에서 사용
select *
from GUSER.GSCT_GRAM_DESC
where GRAM_CD = 'GRAM01'
order by gram_cd, snr_div, snum
;

-- 공통 - 전문실행테스트 에서 사용
select *
from GUSER.GSCT_GRAM_DESC
where GRAM_CD = 'KB0200'
order by gram_cd, snr_div, snum
;

select gram_cd
from GUSER.GSCT_GRAM_DESC
group by gram_cd
;






--- 1. getLoanPayObjListQuery
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
WHERE A.PROC_STAT_CD    = '01'   	 /*처리상태코드(미처리) 				  */
  AND A.TRAN_PROC_DT    = '2011-10-17'
        /*이체처리일자(현재일자:yyyy-MM-dd)     */
  AND A.TRAN_RQST_DTTM <= '20111017122926'
        /*이체요청일시(현재일시:yyyyMMddhhmmss) */
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD
        /*지급업무구분코드 = 업무구분코드  		 */
  AND A.LOAN_NO = C.LOAN_NO(+)
  AND A.LOAN_SEQ = C.LOAN_SEQ(+)
  ORDER BY A.PAY_NO
;

select *
from AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_대출_지급_내역
WHERE A.PROC_STAT_CD    = '01'   	 /*처리상태코드(미처리) 				  */
  AND A.TRAN_PROC_DT    = '2011-10-17'
        /*이체처리일자(현재일자:yyyy-MM-dd)     */
  AND A.TRAN_RQST_DTTM <= '20111017122926'
        /*이체요청일시(현재일시:yyyyMMddhhmmss) */
--  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD
        /*지급업무구분코드 = 업무구분코드  		 */
--  AND A.LOAN_NO = C.LOAN_NO(+)
--  AND A.LOAN_SEQ = C.LOAN_SEQ(+)
  ORDER BY A.PAY_NO
;

select *
from AUSER.ALOT_MO_ACNT_BASE  B  -- ALOT_모계좌_기본
;

select *
from AUSER.ALOT_MO_ACNT_BASE  B  -- ALOT_모계좌_기본
where BSN_DIV_CD in ('03', '04', '05', '06', '07', '09')
;


select *
from AUSER.ALOT_LOAN_BASE     C  -- ALOT_대출_기본
;

SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD'),TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL;

SELECT TO_CHAR(SYSDATE, 'YYYYMMDD'),TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL;


----------------------------------------------
-- GramExecutor.java / SendObj.java / ReceiveObj.java
---

-- SendObj.java gram_cd = 'KB0200'
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG
FROM GUSER.GSCT_GRAM_DESC
WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S'
ORDER BY SNUM ASC
;

-- ReceiveObj.java gram_cd = 'KB0200'
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG
FROM GUSER.GSCT_GRAM_DESC
WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R'
ORDER BY SNUM ASC
;

--SELECT * --KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG
--FROM GUSER.GBCT_COMM_CD_KIND
--WHERE CD_KIND_NO = 'KB0200' --AND SNR_DIV = 'S'
--;

select *
from GUSER.GSCT_GRAM_CHNL
order by chnl_cd
;

