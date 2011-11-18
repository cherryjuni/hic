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
       IMG_ACNT_NO      = '56201550486180'   /* :�Աݳ���.���¹�ȣ  */
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


