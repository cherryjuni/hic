
SELECT ACNT_NO, BANK_CD
FROM EUSER.EFDT_DEPO_BASE
WHERE PRPS_DIV_CD = ?  /* '2' */
AND DEPO_STAT_CD <> ?
AND DEPO_NO = 'D0177'            // ������� �����
AND LOC_DIV_CD = ?
;

SELECT *
FROM EUSER.EFDT_DEPO_BASE
;
