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
WHERE A.PROC_STAT_CD    = '01' --'05' --'01'   	
  AND A.TRAN_PROC_DT    = :dt     	
  AND A.TRAN_RQST_DTTM <= :dttm     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
  AND DECODE(A.PAY_BSN_DIV_CD, '03', DECODE(A.SETL_BANK_CD, '021', '088', '026', '088', '088', '088', '020'), '088')  = B.BANK_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;


select * from GUSER.GSCT_SQL_INFO
;

select * from GUSER.GSCT_PRGM
;

--TRAN_RQST_DTTM
--20111108095409
--20111108103642
--20111108101641
--20111108103956
--20111108103512
select *
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_대출_지급_내역
where tran_proc_dt >= :dt --'2011-09-10'
  AND A.TRAN_RQST_DTTM <= :dttm     	
;

---------------------------------------------------------------------------------
---------------------------------------------------------------------------------
select TRAN_PROC_DT,TRAN_RQST_DTTM,LOAN_NO,LOAN_SEQ,PROC_STAT_CD,PAY_BSN_DIV_CD,PAY_NO
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_대출_지급_내역
where tran_proc_dt >= :dt --'2011-09-10'
--  AND A.TRAN_RQST_DTTM <= :dttm     	
order by pay_no
;

select *
FROM AUSER.ALOT_MO_ACNT_BASE B -- ALOT_모계좌_기본          
order by BSN_DIV_CD
;

select *
FROM AUSER.ALOT_LOAN_BASE     C  -- ALOT_대출_기본             
;
---------------------------------------------------------------------------------
---------------------------------------------------------------------------------
select *
FROM AUSER.ALOT_LOAN_ONLN_PAY A, -- ALOT_대출_지급_내역      
--     AUSER.ALOT_MO_ACNT_BASE  B, -- ALOT_모계좌_기본          
     AUSER.ALOT_LOAN_BASE     C  -- ALOT_대출_기본             
WHERE 
--      A.PROC_STAT_CD    = '01'   	
--  AND 
A.TRAN_PROC_DT    = :dt     	
--  AND A.TRAN_RQST_DTTM <= :dttm     	
--  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

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
--    A.PROC_STAT_CD    = '01'   	
--  AND 
  A.TRAN_PROC_DT    = :dt     	
  AND A.TRAN_RQST_DTTM <= :dttm     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
;

select PAY_BSN_DIV_CD, count(*)
FROM AUSER.ALOT_LOAN_ONLN_PAY
group by PAY_BSN_DIV_CD
;
---------------------------------------------------------------------------------
---------------------------------------------------------------------------------
select count(*)
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_대출_지급_내역      
;

select TRAN_PROC_DT,count(*)
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_대출_지급_내역      
where tran_proc_dt > '2011-09-10'
group by TRAN_PROC_DT
;

select *
FROM AUSER.ALOT_LOAN_ONLN_PAY A -- ALOT_대출_지급_내역      
where tran_proc_dt = '2011-11-07'
;


----------------------------------------------------------------------------------
-- 미사용가상계좌
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

SELECT *
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC 
WHERE USE_FG = '0'                   
;

SELECT IMG_ACNT_BANK_CD, USE_FG, count(*)
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
group by IMG_ACNT_BANK_CD, USE_FG
--WHERE USE_FG = '0'
;

select count(*)
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
;

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
--//거래기관조회
SELECT
	TR_ORG_CD
FROM BUSER.BVAT_TR_ORG_BASE
WHERE RCPT_TP_CD = ? AND BANK_CD = '0'||?
;

-- C1 - 수납유형코드 (가상수납)
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '088';
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '020';
SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD;
-- C2 - 수납유형코드 (즉시출금)
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C2' AND BANK_CD = '088';

SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD
;
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------

SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559'; -- 계약방법코드

SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NM LIKE '%지급처리%'; -- S00045 지급처리업무구분코드
SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NO LIKE 'S00045';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00045'; -- S00045 지급처리업무구분코드

SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NO = 'A00003'; -- 지급모계좌에서 사용하는 은행코드
;
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'A00003'; -- A00003 지급모계좌에서 사용하는 은행코드

SELECT PAY_BSN_DIV_CD, COUNT(*) FROM AUSER.ALOT_LOAN_ONLN_PAY
GROUP BY PAY_BSN_DIV_CD;
-- 원거래조회
SELECT SETL_FG FROM BUSER.BVAT_CMS_IAMT_DESC WHERE TR_ORG_CD = ? AND TR_NO = ?;
SELECT SETL_FG FROM BUSER.BVAT_CMS_IAMT_DESC WHERE TR_ORG_CD = 'C1003' AND TR_NO = '201111030209851';

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- 실시간 지급 대상 건수 조회
-- 대출_지급_내역
SELECT * FROM AUSER.ALOT_LOAN_ONLN_PAY  WHERE PROC_STAT_CD = '05' AND TRAN_PROC_DT = '2011-11-08' AND TRAN_RQST_DTTM <= '20111108095409';
-- 모계좌_기본
SELECT * FROM AUSER.ALOT_MO_ACNT_BASE   WHERE BSN_DIV_CD IN ('03', '10') ORDER BY BSN_DIV_CD; -- '03' 지급계좌 '10' 회계지급처리계좌
-- 대출_기본
SELECT * FROM AUSER.ALOT_LOAN_BASE      WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 대출실시간_지급대상 - 상태코드수정
-- 대출지급내역
SELECT * FROM AUSER.ALOT_LOAN_PAY_DESC  WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 연체료_율_내역
SELECT * FROM AUSER.ALOT_DFEE_RT_DESC   WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 대출월리금
SELECT * FROM AUSER.ALOT_LOAN_PIAMT     WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 대출_관계_자
SELECT * FROM AUSER.ALOT_LOAN_REL_MAN   WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 대출실납입자
SELECT * FROM AUSER.ALOT_LOAN_RL_DUE_MAN WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 대출_가상_계좌_상세
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC WHERE IMG_ACNT_BANK_CD = '026' AND IMG_ACNT_NO = '56201550755471';
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC WHERE (IMG_ACNT_BANK_CD, IMG_ACNT_NO) IN (SELECT IMG_ACNT_BANK_CD,IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC WHERE USE_FG = '0';
-- 대출_가상_계좌_이력
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_HIST WHERE IMG_ACNT_BANK_CD = '026' AND IMG_ACNT_NO = '56201550755471';
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_HIST WHERE (IMG_ACNT_BANK_CD, IMG_ACNT_NO) IN (SELECT IMG_ACNT_BANK_CD,IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- 채권_마감_기본
SELECT * FROM CUSER.CART_BND_CLS_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 상품_기본
SELECT * FROM AUSER.APDT_PRDT_BASE WHERE PRDT_CD = 'LOQ11';
SELECT * FROM AUSER.APDT_PRDT_BASE WHERE PRDT_CD IN (SELECT PRDT_CD FROM AUSER.ALOT_LOAN_PAY_DESC  WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- 내부_전표
SELECT * FROM AUSER.ASCT_INTN_VOCH WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 계약_서류_도착_관리_원장
SELECT * FROM AUSER.ALDT_CONT_DOC_ARRL_MNG_MST WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 발송_관리_정보
SELECT * FROM AUSER.ALDT_DLVR_MNG_INFO WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- 고객_주소
SELECT * FROM AUSER.ACTT_CUST_ADDR WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- 공통_코드_내역
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- 계약방법코드
-- 고객_연락처
SELECT * FROM AUSER.ACTT_CUST_CNTC_PLC WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
SELECT * FROM AUSER.ACTT_CUST_CNTC_PLC WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01') AND CNTC_PLC_DIV_CD = '01';
-- 고객_기본
SELECT * FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11112800002' AND LOAN_SEQ = '01');
-- 19 고객_상담_안내
SELECT * FROM CUSER.CBCT_CUST_CNSL_GUID WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 20 심사_진행_기본
SELECT * FROM AUSER.ACNT_EXAM_PRGS_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 21 대출_실납입자
SELECT * FROM AUSER.ALOT_LOAN_RL_DUE_MAN WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 22 채번_기본
SELECT * FROM GUSER.GBCT_GVNO_BASE ORDER BY CLAS_KIND_NO;
-- 23 거래_은행_상세
SELECT * FROM BUSER.BVAT_TR_BANK_DESC WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '088' ORDER BY RCPT_TP_CD, BANK_SNUM;
SELECT * FROM BUSER.BVAT_TR_BANK_DESC ORDER BY RCPT_TP_CD, BANK_SNUM;

-- - C1 - 가상계좌 / 88 신한은행 (23 / 26)
-- 24 거래_기관_기본
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '088';
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '026';
SELECT TR_ORG_CD FROM BUSER.BVAT_TR_ORG_BASE WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '020';
SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD;

-- 공통_코드_내역 - 은행
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'A00003' order by CD_DESC_NO;
-- 공통_코드_내역 - 은행
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'R00012' order by CD_DESC_NO;

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- 공통_코드_내역 - 주소
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- 계약방법코드
-- 공통_코드_내역 - 청구입금배치부서 가져오기
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'R00038';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- 계약방법코드
-- 공통_코드_내역 - 오류코드
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'R00036';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_DESC_NO = (SELECT DLVR_PLC_DIV_CD FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01')) AND CD_KIND_NO = 'S00559'; -- 계약방법코드

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- SENDER SOCKET
SELECT C.CHNL_IP, C.CHNL_PORT, B.HDR_LTH FROM GUSER.GSCT_GRAM_CHNL C, GUSER.GSCT_GRAM_BASE B WHERE C.CHNL_CD = B.CHNL_CD AND B.GRAM_CD = ?;
SELECT C.CHNL_IP, C.CHNL_PORT, B.HDR_LTH FROM GUSER.GSCT_GRAM_CHNL C, GUSER.GSCT_GRAM_BASE B WHERE C.CHNL_CD = B.CHNL_CD AND B.GRAM_CD = 'KB0200';
SELECT * FROM GUSER.GSCT_GRAM_CHNL ORDER BY CHNL_CD;
SELECT * FROM GUSER.GSCT_GRAM_BASE ORDER BY GRAM_CD;
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S' ORDER BY SNUM ASC;
SELECT SNUM, SNR_DIV, HDR_FG, KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S' ORDER BY SNUM ASC;
SELECT * FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'S';
SELECT KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R' ORDER BY SNUM ASC;
SELECT SNUM, SNR_DIV, HDR_FG, KOR_ITEM_NM, ENG_ITEM_NM, ATT, LTH, ITER_FG FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R' ORDER BY SNUM ASC;
SELECT * FROM GUSER.GSCT_GRAM_DESC WHERE GRAM_CD = 'KB0200' AND SNR_DIV = 'R';


-- 전문 로그 기록 테이블
-- 지급배치 - 'KB0200'
SELECT COUNT(*) FROM GUSER.GSCT_GRAM_LOG
;
SELECT GRAM_CD FROM GUSER.GSCT_GRAM_LOG
GROUP BY GRAM_CD
;
SELECT distinct GRAM_CD FROM GUSER.GSCT_GRAM_LOG
WHERE GRAM_CD IS NOT NULL
--GROUP BY GRAM_CD
;

SELECT * FROM GUSER.GSCT_GRAM_BASE ORDER BY GRAM_CD;

select * from guser.gsct_gram_log
;

select * from guser.gsct_gram_log
where EXEC_DT = '20111122'
order by EXEC_TM
;
--delete from guser.gsct_gram_log where exec_dt = '20111122';
--commit;

select * from guser.gsct_gram_log
where EXEC_DT = '20111124'
order by EXEC_TM
;
select * from guser.gsct_gram_log
where EXEC_DT = '20111130' and gram_cd = 'KB0200'
order by EXEC_TM
;

select * from guser.gsct_gram_log
where gram_cd = 'KB0200'
;

select * from guser.gsct_gram_log
where gram_cd = 'KB6013'
;

select * from guser.gsct_gram_log
where gram_cd = 'KB4100'
;

select GRAM_CD, MIN(EXEC_DT), MAX(EXEC_DT) from guser.gsct_gram_log
where gram_cd = 'KB4100'
GROUP BY GRAM_CD
;

select GRAM_CD, MIN(EXEC_DT), MAX(EXEC_DT) from guser.gsct_gram_log
--where gram_cd = 'KB4100'
GROUP BY GRAM_CD
;

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC where USE_FG = '0'
;

SELECT * FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC where USE_FG = '0' and IMG_ACNT_BANK_CD = '020'
order by IMG_ACNT_BANK_CD,IMG_ACNT_NO
;

SELECT COUNT(*) FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
;

select * from auser.ALOT_LOAN_IMG_ACNT_DESC@hiplus_link
;

select COUNT(*) from auser.ALOT_LOAN_IMG_ACNT_DESC@hiplus_link
;

--INSERT INTO AUSER.ALOT_LOAN_IMG_ACNT_DESC
--select * from auser.ALOT_LOAN_IMG_ACNT_DESC@hiplus_link
--;

--COMMIT;
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------




-- DDL Script for TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC. Orange for ORACLE.
-- Generated on 2011/11/17 20:51:30 by ZUSER@CABISDEV

--CREATE TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC 
--(
--	IMG_ACNT_BANK_CD   CHAR (3) DEFAULT '' NOT NULL,
--	IMG_ACNT_NO        VARCHAR2 (20) DEFAULT '' NOT NULL,
--	USE_FG             CHAR (1) DEFAULT '0',
--	USE_CMPL_DT        CHAR (10) DEFAULT '',
--	DISU_DT            CHAR (10) DEFAULT '',
--	RMRK               VARCHAR2 (100) DEFAULT '',
--	FRST_REG_DT        CHAR (10) DEFAULT '',
--	FRST_REG_TM        CHAR (6) DEFAULT '',
--	FRST_REG_EMP_NO    VARCHAR2 (8) DEFAULT '',
--	LAST_PROC_DT       CHAR (10) DEFAULT '',
--	LAST_PROC_TM       CHAR (6) DEFAULT '',
--	LAST_PROC_EMP_NO   VARCHAR2 (8) DEFAULT ''
--)
--TABLESPACE AUSER
--PCTFREE 10
--INITRANS 1
--MAXTRANS 255
--STORAGE
--(
--	INITIAL 131072
--	NEXT 1048576
--	MINEXTENTS 1
--	MAXEXTENTS UNLIMITED
--	BUFFER_POOL DEFAULT
--)
--LOGGING
--DISABLE ROW MOVEMENT ;
--
--GRANT DELETE ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--GRANT INSERT ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--GRANT SELECT ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--GRANT UPDATE ON AUSER.ALOT_LOAN_IMG_ACNT_DESC TO ZUSER;
--
--COMMENT ON TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC IS 'ALOT_대출_가상_계좌_내역' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.DISU_DT IS '폐기_일자' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.FRST_REG_DT IS '최초_등록_일자' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.FRST_REG_EMP_NO IS '최초_등록_사원번호' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.FRST_REG_TM IS '최초_등록_시각' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.IMG_ACNT_BANK_CD IS '가상_계좌_은행_코드' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.IMG_ACNT_NO IS '가상_계좌번호' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.LAST_PROC_DT IS '최종_처리_일자' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.LAST_PROC_EMP_NO IS '최종_처리_사원번호' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.LAST_PROC_TM IS '최종_처리_시각' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.RMRK IS '비고' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.USE_CMPL_DT IS '사용_완료_일자' ;
--
--COMMENT ON COLUMN AUSER.ALOT_LOAN_IMG_ACNT_DESC.USE_FG IS '사용_여부' ;
--
--CREATE INDEX AUSER.ACNTLOANIMG_A02 
--ON AUSER.ALOT_LOAN_IMG_ACNT_DESC
--(
--	IMG_ACNT_BANK_CD ASC,
--	USE_FG ASC
--)
--TABLESPACE AUSER
--PCTFREE 10
--INITRANS 2
--MAXTRANS 255
--STORAGE
--(
--	INITIAL 65536
--	NEXT 1048576
--	MINEXTENTS 1
--	MAXEXTENTS UNLIMITED
--	BUFFER_POOL DEFAULT
--)
--LOGGING
--NOCOMPRESS
--NOPARALLEL ;
--
--ALTER TABLE AUSER.ALOT_LOAN_IMG_ACNT_DESC ADD(
--	PRIMARY KEY (IMG_ACNT_BANK_CD, IMG_ACNT_NO));
--;


---
-- 신한(구)조흥 - 021
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='021'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '021' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- 우리(구)상업 022
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='022'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '022' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- 우리은행－（구）한일 024
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='024'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '024' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- 통합신한은행 088
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='088'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '088' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- (구)조흥 021
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='021'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

-- 우리은행 020
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='020'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '020' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;

-- (구)신한 026
SELECT
    A.BANK_CD BANK_CD,
    GUSER.GF_COMMCD_NM_01( 'A00003', A.BANK_CD) BANK_NM,
    RCPT_TP_CD,
    GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM,
    STRT_DUE_DT,
    END_DUE_DT,
    LAST_PROC_EMP_NO,
    (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME,
    BANK_SNUM
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_SNUM  = ( 
SELECT  
MAX(Z.BANK_SNUM)  
FROM BUSER.BVAT_TR_BANK_DESC Z 
WHERE Z.RCPT_TP_CD = A.RCPT_TP_CD 
AND Z.BANK_CD = A.BANK_CD )
AND A.BANK_CD ='026'
ORDER BY A.RCPT_TP_CD, A.BANK_CD
;

SELECT
  A.BANK_SNUM BANK_SNUM,
  A.BANK_CD BANK_CD,
  A.RCPT_TP_CD RCPT_TP_CD,
  GUSER.GF_COMMCD_NM_01('R00012',A.RCPT_TP_CD) RCPT_TP_NM,
  STRT_DUE_DT,
  END_DUE_DT,
  LAST_PROC_EMP_NO,
  (SELECT KOR_NAME FROM DUSER.DHRT_EMP_BASE
    WHERE EMP_NO = A.LAST_PROC_EMP_NO) KOR_NAME
FROM BUSER.BVAT_TR_BANK_DESC A
WHERE A.BANK_CD  = '026' AND A.RCPT_TP_CD = 'A1'
ORDER BY A.BANK_SNUM DESC
;


-- 검증 쿼리들
SELECT * FROM BUSER.BVAT_TR_BANK_DESC ORDER BY RCPT_TP_CD, BANK_SNUM;
select * FROM BUSER.BVAT_TR_BANK_DESC               order by RCPT_TP_CD,BANK_CD,BANK_SNUM;
select * FROM BUSER.BVAT_TR_BANK_DESC@hiplus_link   order by RCPT_TP_CD,BANK_CD,BANK_SNUM;
SELECT * FROM BUSER.BVAT_TR_ORG_BASE ORDER BY TR_ORG_CD
;

SELECT TR_ORG_CD,TR_ORG_NM,ACCT_UNIT_CD,RCPT_TP_CD,CYCL_CD,BANK_CD,MO_ACNT_NO FROM BUSER.BVAT_TR_ORG_BASE
minus
SELECT TR_ORG_CD,TR_ORG_NM,ACCT_UNIT_CD,RCPT_TP_CD,CYCL_CD,BANK_CD,MO_ACNT_NO FROM BUSER.BVAT_TR_ORG_BASE@HIPLUS_LINK
;

SELECT
    l.TR_ORG_CD, l.TR_ORG_NM, l.ACCT_UNIT_CD, l.RCPT_TP_CD, l.CYCL_CD, l.BANK_CD, l.MO_ACNT_NO, r.MO_ACNT_NO
FROM
    BUSER.BVAT_TR_ORG_BASE l, BUSER.BVAT_TR_ORG_BASE@hiplus_link r
where
        l.TR_ORG_CD    = r.TR_ORG_CD
    and l.TR_ORG_NM    = r.TR_ORG_NM
    and l.ACCT_UNIT_CD = r.ACCT_UNIT_CD
    and l.RCPT_TP_CD   = r.RCPT_TP_CD
    and l.CYCL_CD      = r.CYCL_CD
    and l.BANK_CD      = r.BANK_CD
    and l.MO_ACNT_NO   = r.MO_ACNT_NO
;

select l.RCPT_TP_CD, l.BANK_CD, l.BANK_SNUM
from BUSER.BVAT_TR_BANK_DESC l, BUSER.BVAT_TR_BANK_DESC@hiplus_link r
where
        l.RCPT_TP_CD = r.RCPT_TP_CD
    and l.BANK_CD    = r.BANK_CD
    and l.BANK_SNUM  = r.BANK_SNUM
;

SELECT * FROM BUSER.BVAT_TR_BANK_DESC order by rcpt_tp_cd, bank_cd, bank_snum;
SELECT * FROM BUSER.BVAT_TR_ORG_BASE order by TR_ORG_CD, bank_cd, MO_ACNT_NO;

SELECT RCPT_TP_CD,BANK_CD,BANK_SNUM FROM BUSER.BVAT_TR_BANK_DESC
minus
select RCPT_TP_CD,BANK_CD,BANK_SNUM FROM BUSER.BVAT_TR_BANK_DESC@hiplus_link;

select RCPT_TP_CD,  GUSER.GF_COMMCD_NM_01( 'R00012', RCPT_TP_CD) RCPT_TP_NM
FROM BUSER.BVAT_TR_BANK_DESC
;

--GUSER.GF_COMMCD_NM_01( 'R00012', A.RCPT_TP_CD) RCPT_TP_NM
--;

SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NO = 'R00012'; -- R00012 수납유형코드
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'R00012';

--서비스계좌 : 리얼타임지급서비스 (1005101927669)
--             가상계좌집금서비스 (1005301927675)


select rtrim(ltrim(substr(b.cust_nm,1,16)))||'-하이' CUST_NM
  from buser.bgmt_comm_dummy d
       left outer join auser.alot_loan_base a
       on 1 = 1
        and a.img_acnt_bank_cd = '0'||?
       and a.img_acnt_no = trim(?)
       and a.loan_last_fg = '1'
       and substr(a.loan_stat_cd,1,1) = '2'
       left outer join auser.actt_cust_base b
      on b.cust_no = a.cont_man_no
 where d.no = 1
 ;

select rtrim(ltrim(substr(b.cust_nm,1,16)))||'-하이' CUST_NM
  from buser.bgmt_comm_dummy d
       left outer join auser.alot_loan_base a
       on 1 = 1
        and a.img_acnt_bank_cd = '0'||'20'
       and a.img_acnt_no = trim('26665004518964') -- 26665004518923 / 26665004518931 / 26665004518940 / 26665004518956 / 26665004518964
       and a.loan_last_fg = '1'
       and substr(a.loan_stat_cd,1,1) = '2'
       left outer join auser.actt_cust_base b
      on b.cust_no = a.cont_man_no
 where d.no = 1
 ;


SELECT
	*
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE tr_dt > '2011-11-01'; --AND TR_NO = ?  ;
--WHERE TR_ORG_CD = 'C1004' and tr_dt = '2011-11-30'; --AND TR_NO = ?  ;

;

select * from EUSER.EFDT_DEPO_TRIAL
;

desc EUSER.EFDT_DEPO_TRIAL;

select * from EUSER.EFDT_DEPO_LDGR ;

select * from EUSER.EFDT_DEPO_BASE;

-- 가상계좌 입금
SELECT
	*
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE tr_dt = '2011-12-26'; --AND TR_NO = ?  ;
--WHERE TR_ORG_CD = 'C1004' and tr_dt = '2011-11-30'; --AND TR_NO = ?  ;
;



SELECT IMG_ACNT_NO FROM 	
(
SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE 
WHERE CONT_MAN_NO =(SELECT L.CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE L 
WHERE L.LOAN_NO ='11122300001' --? 
AND L.LOAN_SEQ='01' --?
) 
AND LOAN_STAT_CD ='22' 
AND PRDT_MID_CLAS_CD ='22' 
AND IMG_ACNT_NO IS NOT NULL OR IMG_ACNT_NO <> '') 
WHERE ROWNUM = 1                   
;

SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE 
WHERE CONT_MAN_NO =(
SELECT L.CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE L 
WHERE L.LOAN_NO ='11122300001' --? 
AND L.LOAN_SEQ='01' --?
)
;

SELECT L.CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE L 
WHERE L.LOAN_NO ='11122300001' --? 
AND L.LOAN_SEQ='01' --?
;

-- 가상계좌 1개
SELECT IMG_ACNT_NO FROM AUSER.ALOT_LOAN_BASE 
WHERE CONT_MAN_NO = '6409061683112'
;


------------------------------
-- 추가대출 / 재대출(RE) / 등 검토
-- 08061800030 01 - 종료계좌
SELECT * FROM AUSER.ALOT_LOAN_BASE 
WHERE CONT_MAN_NO = '6501039607681' --'6409061683112'
;

select * from auser.alot_loan_base
where
    loan_stat_cd like '31'
;

select * from auser.alog
