-----------------------------------------------------------------
--거래기관조회
-----------------------------------------------------------------
SELECT
	TR_ORG_CD
FROM BUSER.BVAT_TR_ORG_BASE
WHERE RCPT_TP_CD = ? AND BANK_CD = '0'||?
;


SELECT
	* --TR_ORG_CD
FROM BUSER.BVAT_TR_ORG_BASE
;
-----------------------------------------------------------------

-- 거래확인
SELECT
	SETL_FG
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE TR_ORG_CD = ? AND TR_NO = ?
;

SELECT                             
	SETL_FG                         
FROM BUSER.BVAT_CMS_IAMT_DESC      
WHERE TR_ORG_CD = 'C1004' AND TR_NO = '201108110342788'  
;

SELECT SETL_FG FROM BUSER.BVAT_CMS_IAMT_DESC WHERE TR_ORG_CD = ? AND TR_NO = ?;
SELECT SETL_FG FROM BUSER.BVAT_CMS_IAMT_DESC WHERE TR_ORG_CD = 'C1003' AND TR_NO = '201111030209851';
--DELETE FROM BUSER.BVAT_CMS_IAMT_DESC WHERE TR_ORG_CD = 'C1003' AND TR_NO = '201111030209851';
--COMMIT;

SELECT                             
	* --SETL_FG                         
FROM BUSER.BVAT_CMS_IAMT_DESC      
WHERE TR_ORG_CD = 'C1004' AND TR_NO = '201108110342788'  
;

-- 결과코드명 얻기
SELECT
   	CD_DESC_KOR_NM
FROM GUSER.GBCT_COMM_CD_DESC
WHERE CD_KIND_NO = 'R00036'
--    AND CD_DESC_NO = '000'
;

SELECT
   	CD_DESC_KOR_NM
FROM GUSER.GBCT_COMM_CD_DESC
WHERE CD_KIND_NO = 'R00036'
    AND CD_DESC_NO = '344'
;

-- 결과코드명 얻기
SELECT
   	* --CD_DESC_KOR_NM
FROM GUSER.GBCT_COMM_CD_DESC
WHERE CD_KIND_NO = 'R00036'
--    AND CD_DESC_NO = '000'
;

--SELECT BUSER.BS_DSRC_UNCNFM_NO.NEXTVAL FROM DUAL
;
