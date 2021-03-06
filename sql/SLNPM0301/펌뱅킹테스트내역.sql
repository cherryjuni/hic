
/**************************************************************
2011-12-26 펌뱅킹 운영 테스트 가상 시나리오 프로세스

2011-12-23 승인의뢰 상태에서 고객거절 건을 대상으로 작업 진행 후 원복 예정
           원복 후 입금 은행 코드 변경 하여 다시 진행 가능

cust_nm  = '이정우'
cnsl_no  = '2011122300241'
cust_no  = '8106111036619'
appl_dt  = '2011-12-23'
agent_no = 'HP0042'
loan_no  = '11122300046'
loan_seq = '01'
**************************************************************/

/**************************************************************
    고객 정보 백업
**************************************************************/
-- 1. 상담기본 백업
SELECT *
  FROM AUSER.ACNT_CNSL_BASE
 WHERE CNSL_NO = :cnslNo
;

-- 2. 고객연락처 핸드폰 번호 백업
SELECT *
  FROM AUSER.ACTT_CUST_CNTC_PLC
 WHERE CUST_NO = :custNo
   AND CNTC_PLC_DIV_CD = '03'
;

-- 3. 심사진행기본 백업
SELECT * 
  FROM AUSER.ACNT_EXAM_PRGS_BASE
 WHERE CNSL_NO = :cnslNo
;

-- 심사결과 백업 후 삭제 pass
--SELECT * 
--  FROM AUSER.ACNT_CNSL_EXAM_RSLT
-- WHERE LOAN_NO  = :loanNo
--   AND LOAN_SEQ = :loanSeq
--;
--DELETE FROM AUSER.ACNT_CNSL_EXAM_RSLT
-- WHERE LOAN_NO  = :loanNo
--   AND LOAN_SEQ = :loanSeq
--;

-- 4. 심사승인단계 백업
SELECT * 
  FROM AUSER.AJAT_EXAM_APRV_STAG
 WHERE LOAN_NO  = :loanNo 
   AND LOAN_SEQ = :loanSeq
;

-- 5. 심사승인기본 백업
SELECT * 
  FROM AUSER.AJAT_EXAM_APRV_BASE
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;

-- 6. 대출원장 백업
SELECT * 
  FROM AUSER.ALOT_LOAN_BASE
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;

-- 7. 대출관계자 백업
SELECT * 
  FROM AUSER.ALOT_LOAN_REL_MAN
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;
--DELETE FROM AUSER.ALOT_LOAN_REL_MAN
-- WHERE LOAN_NO  = :loanNo
--   AND LOAN_SEQ = :loanSeq
--;

/**************************************************************
    지급배치 전 단계 업무 처리
**************************************************************/

-- 1. 중계 전송을 방지하기 위한 AGENT_NO 초기화
UPDATE AUSER.ACNT_CNSL_BASE
   SET AGENT_NO = NULL
 WHERE CNSL_NO = :cnslNo
;

-- 2. SMS 오발송을 방지하기 위해 고객연락처 핸드폰 번호 변경
UPDATE AUSER.ACTT_CUST_CNTC_PLC
   SET CNTC_PLC_TEL1 = '010'
     , CNTC_PLC_TEL2 = '2894'
     , CNTC_PLC_TEL3 = '4279'
 WHERE CUST_NO = :custNo
   AND CNTC_PLC_DIV_CD = '03'
;

-- 3. 심사진행기본 수정
UPDATE AUSER.ACNT_EXAM_PRGS_BASE 
   SET EXAM_PRGS_STAT_CD = '02'
     , AGENT_NO          = NULL
 WHERE CNSL_NO = :cnslNo
;

-- 4. 대출기본 수정
UPDATE AUSER.ALOT_LOAN_BASE 
   SET AGENT_NO = NULL
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;

-- 5. 심사승인단계 수정
UPDATE AUSER.AJAT_EXAM_APRV_STAG
   SET JST_EMP_NO = '200099'
 WHERE LOAN_NO    = :loanNo 
   AND LOAN_SEQ   = :loanSeq
   AND JST_MAN_CD = '20'
;
-- 6. 하이플러스 승인관리에서 가승인 처리

-- 7. 심사승인기본 녹취정보 수정
UPDATE AUSER.AJAT_EXAM_APRV_BASE 
   SET JST_VOICE_CNFM_FG = '1'
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;

-- 8. 하이플러스 승인관리 대출지급 처리

-- 9. 입금실행 후 지급배치 수행 전 지급계좌 정보 변경
UPDATE AUSER.ALOT_LOAN_ONLN_PAY
   SET SETL_BANK_CD = '026'
     , SETL_ACNT_NO = '110314809090'
     , DEPO_OWN_NO  = '8401221024416'
     , DEPO_OWNNM   = '김동오'
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;
UPDATE AUSER.ALOT_LOAN_PAY_DESC
   SET BANK_CD     = '026'
     , ACNT_NO     = '110314809090'
     , DEPO_OWN_NO = '8401221024416'
     , PAY_PLCNM   = '김동오'
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
   AND SNUM     = '01'
;

-- 9. 지급배치 수행

/**************************************************************
    데이타 정합성 체크 - 각 업무 담당자
**************************************************************/

/**************************************************************
    데이타 복원
**************************************************************/

-- 1. 백업된 원장 정보 삭제

-- 2. 백업된 데이타 등록

-- 3. 심사결과내역 삭제 처리
DELETE FROM AUSER.AJAT_EXAM_RSLT_DESC
 WHERE LOAN_NO  = :loanNo 
   AND LOAN_SEQ = :loanSeq
;

-- 4. 대출 실시간지급 삭제 처리
DELETE FROM AUSER.ALOT_LOAN_ONLN_PAY
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;

-- 5. 대출지급내역 삭제 처리
DELETE FROM AUSER.ALOT_LOAN_PAY_DESC
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;
 
-- 6. 마감보조내역 삭제 처리
DELETE FROM BUSER.BCLT_CLS_SUB_DESC
 WHERE LOAN_NO  = :loanNo
   AND LOAN_SEQ = :loanSeq
;

