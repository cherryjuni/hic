--- ������� �Ա�
--- 20032903 -
--- 0200 1100
--- 1
-- in 'C1" �������
-- 26 - ���� / 20 - �츮
-- out 'C1004'
SELECT
  TR_ORG_CD
FROM BUSER.BVAT_TR_ORG_BASE
WHERE RCPT_TP_CD = 'C1' AND BANK_CD = '0'||'26'
;

--- 2 
-- in �Լ� ���ο��� 'R00038' '1'
-- out 
-- DEPT_CD	EMP_NO
-- 000038	System
SELECT
    CLAS1 DEPT_CD,
    CLAS2 EMP_NO
FROM GUSER.GBCT_COMM_CD_DESC
WHERE CD_KIND_NO = 'R00038'
  AND CD_DESC_NO = '1'
;

----------------------------------------------------
--�Ա�
--//���ŷ� ��ȸ
--private static final String SEARCH_ORIG_TR =
--in TrOrgCd C1004 - Trno 201111030209662
SELECT
	SETL_FG
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE TR_ORG_CD = ? AND TR_NO = ?
;

SELECT
	SETL_FG
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE TR_ORG_CD = 'C1004' AND TR_NO = '201111030209662'
;

--//�������� ��ȸ
--private static final String SEARCH_LOAN_FOR_CMS =
-- in acnt='56201550486180'  / bankcd='26'
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
FROM   AUSER.ALOT_LOAN_BASE
WHERE
       IMG_ACNT_NO      = ?                  /* :�Աݳ���.���¹�ȣ  */
       AND IMG_ACNT_BANK_CD = '0'||?         /* :�Աݳ���.����_�ڵ� */
       AND LOAN_STAT_CD IN ('22', '29')      /* �������� ���¸�     */
       AND LOAN_LAST_FG     = '1'            /* '1'                 */
;

-- Test
--LOAN_NO	LOAN_SEQ	MNG_DEPT_CD
--11063000218	01	344000
-- Real
--LOAN_NO	LOAN_SEQ	MNG_DEPT_CD
--11063000218	01	344000
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
FROM   AUSER.ALOT_LOAN_BASE
WHERE
       IMG_ACNT_NO      = '56201550207156' --'56201550486180'   /* :�Աݳ���.���¹�ȣ  */
       AND IMG_ACNT_BANK_CD = '0'||'26'      /* :�Աݳ���.����_�ڵ� */
       AND LOAN_STAT_CD IN ('22', '29')      /* �������� ���¸�     */
       AND LOAN_LAST_FG     = '1'            /* '1'                 */
;

-------------------------------------------------
-------------------------------------------------
-------------------------------------------------
-- ���� �� Ȯ��
-------------------------------------------------
-- test
--insert into BUSER.BVAT_CMS_IAMT_DESC
SELECT
	*
--;
--delete
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE
    TR_ORG_CD = 'C1004'
    and TR_DT = '2011-11-03'
    AND TR_NO = '201111030209662'
;
--commit;
-------------------------------------------------
-------------------------------------------------
-------------------------------------------------

-- ���ŷ���ȸ
SELECT                             
	SETL_FG                         
FROM BUSER.BVAT_CMS_IAMT_DESC      
WHERE TR_ORG_CD = ? AND TR_NO = ?  
;

SELECT                             
	SETL_FG                         
FROM BUSER.BVAT_CMS_IAMT_DESC      
WHERE TR_ORG_CD = 'C1004' AND TR_NO in ('201111030209662', '201111030209851')
;

SELECT                             
 *                         
FROM BUSER.BVAT_CMS_IAMT_DESC      
WHERE TR_ORG_CD = 'C1004' AND TR_NO in ('201111030209662', '201111030209851')
;

-- ����������ȸ
SELECT  NVL(LOAN_NO,' ')       LOAN_NO       
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ      
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD   
FROM   AUSER.ALOT_LOAN_BASE                  
WHERE                                        
       IMG_ACNT_NO      = ?                  /* :�Աݳ���.���¹�ȣ   */    
       AND IMG_ACNT_BANK_CD = '0'||?         /* :�Աݳ���.����_�ڵ� */    
       AND LOAN_STAT_CD IN ('22', '29')      /* �������� ���¸�         */    
       AND LOAN_LAST_FG     = '1'            /* '1'              */   
;
SELECT  NVL(LOAN_NO,' ')       LOAN_NO       
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ      
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD   
FROM   AUSER.ALOT_LOAN_BASE                  
WHERE                                        
       IMG_ACNT_NO      = '56201550207156'                  /* :�Աݳ���.���¹�ȣ   */    
       AND IMG_ACNT_BANK_CD = '0'||'26'         /* :�Աݳ���.����_�ڵ� */    
       AND LOAN_STAT_CD IN ('22', '29')      /* �������� ���¸�         */    
       AND LOAN_LAST_FG     = '1'            /* '1'              */   
;

-- ���� �� Ȯ��
-------------------------------------------------
-- test
--insert into BUSER.BVAT_CMS_IAMT_DESC
SELECT
	*
--;
--delete
FROM BUSER.BVAT_CMS_IAMT_DESC
WHERE
    TR_ORG_CD = 'C1004'
    and TR_DT = '2011-11-03'
    AND TR_NO = '201111030209851' --'201111030209662'
;
--commit;

--------------------------------
SELECT  NVL(LOAN_NO,' ')       LOAN_NO
       ,NVL(LOAN_SEQ,'01')     LOAN_SEQ
       ,NVL(MNG_DEPT_CD, ' ')  MNG_DEPT_CD
FROM   AUSER.ALOT_LOAN_BASE
WHERE
       IMG_ACNT_NO      = '56201550207156' --'56201550486180'   /* :�Աݳ���.���¹�ȣ  */
       AND IMG_ACNT_BANK_CD = '0'||'26'      /* :�Աݳ���.����_�ڵ� */
       AND LOAN_STAT_CD IN ('22', '29')      /* �������� ���¸�     */
       AND LOAN_LAST_FG     = '1'            /* '1'                 */
;

SELECT *
FROM   AUSER.ALOT_LOAN_BASE
WHERE
    LOAN_DT > '2011-04-01'
    AND LOAN_SEQ = '01'
    AND LOAN_STAT_CD IN ('22', '29')      /* �������� ���¸�     */
    AND LOAN_LAST_FG     = '1'            /* '1'                 */
ORDER BY LOAN_NO
;

SELECT LOAN_NO,LOAN_SEQ,IMG_ACNT_BANK_CD,IMG_ACNT_NO,LOAN_DT, LOAN_STAT_CD
FROM   AUSER.ALOT_LOAN_BASE
WHERE
    LOAN_DT > '2011-04-01'
    AND LOAN_SEQ = '01'
    AND LOAN_STAT_CD IN ('22', '29')      /* �������� ���¸�     */
    AND LOAN_LAST_FG     = '1'            /* '1'                 */
ORDER BY LOAN_NO
;

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


SELECT * --IMG_ACNT_NO
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
WHERE IMG_ACNT_BANK_CD = '020'
AND USE_FG = '0'
ORDER BY IMG_ACNT_NO
;

SELECT * --IMG_ACNT_NO
FROM AUSER.ALOT_LOAN_IMG_ACNT_DESC
WHERE IMG_ACNT_NO <= '26665005118912'
;

SELECT * FROM
--UPDATE
AUSER.ALOT_LOAN_IMG_ACNT_DESC
--SET USE_FG = '1'
WHERE IMG_ACNT_NO <= '26665005118912'
;
--COMMIT;