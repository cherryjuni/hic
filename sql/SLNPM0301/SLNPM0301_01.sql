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
WHERE A.PROC_STAT_CD    = '05' --'01'   	
  AND A.TRAN_PROC_DT    = :dt     	
  AND A.TRAN_RQST_DTTM <= :dttm     	
  AND A.PAY_BSN_DIV_CD  = B.BSN_DIV_CD 
  AND A.LOAN_NO = C.LOAN_NO(+) 
  AND A.LOAN_SEQ = C.LOAN_SEQ(+) 
  ORDER BY A.PAY_NO 
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

SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00559'; -- 계약방법코드

SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NM LIKE '%지급처리%'; -- S00045 지급처리업무구분코드
SELECT * FROM GUSER.GBCT_COMM_CD_KIND WHERE CD_KIND_NO LIKE 'S00045';
SELECT * FROM GUSER.GBCT_COMM_CD_DESC WHERE CD_KIND_NO = 'S00045'; -- S00045 지급처리업무구분코드

SELECT PAY_BSN_DIV_CD, COUNT(*) FROM AUSER.ALOT_LOAN_ONLN_PAY
GROUP BY PAY_BSN_DIV_CD;
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
SELECT * FROM AUSER.ACTT_CUST_BASE WHERE CUST_NO IN (SELECT CONT_MAN_NO FROM AUSER.ALOT_LOAN_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01');
-- 고객_상담_안내
SELECT * FROM CUSER.CBCT_CUST_CNSL_GUID WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';
-- 심사_진행_기본
SELECT * FROM AUSER.ACNT_EXAM_PRGS_BASE WHERE LOAN_NO = '11110800003' AND LOAN_SEQ = '01';


----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
-- SENDER SOCKET
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
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
