CREATE OR REPLACE PROCEDURE DUSER.DP_DSLSM08_01
 (
     I_WORK_DIV  IN   CHAR,
     I_PROC_DIV  IN   CHAR,
     I_SALR_DIV  IN   CHAR,
     I_PAY_DT    IN   CHAR,
     I_USER_ID   IN   CHAR,
     I_EMP_NO    IN   CHAR,
     O_MSG       OUT  VARCHAR2
 )
 
IS

    -- SQLCODE, SQLSTATE, ��ȯ�� ���� ����
    SQLCODE                   INT            := 0   ;    -- SQLCODE ����
    SQLSTATE                  CHAR(5)        := '0' ;    -- SQLSTATE ����
    V_RETURN                  INT            := 0   ;    -- SP ���� ���� ����
    T_CURRENT_DT              CHAR(10)       := ''   ; -- ��������
    T_CURRENT_TM              CHAR(8)        := ''   ; -- ����ð�

    -- ���� ����..
    T_RMBR_DIV_CD             CHAR(1)        :=  '' ; --��ȯ���� �ڵ� 1:�޿� 2:��
    T_AMT_B01                 DECIMAL        :=  0.0; --���ȸ�� �Ѿ�
    T_MAX_SEQ                 INT            :=  0  ; --���ȸ ������ ���� SEQ
    T_MATE_AMT_RAMT           DECIMAL        :=  0.0; --���ȸ�� �ܾ� �Ѿ�
    T_CLS_FG                  CHAR(1)        :=  '' ;--
    T_CNT                     INT            :=  0  ;--
    T_EMP_NO                  CHAR(8)        :=  '' ; --���
    T_EMP_NM                  VARCHAR2(20)   :=  '' ; --����
    T_PAMT_RAMT               DECIMAL        :=  0.0; --��ȯ�ܾ�
    T_RMBR_PAMT               DECIMAL        :=  0.0; --��ȯ�Ѿ�
    T_MSG                     VARCHAR2(100)  :=  '' ; --�޼���


    CURSOR CUR_1 IS
         SELECT A.EMP_NO
               ,A.SALR_CD
               ,A.RMRK
               ,A.AMT
         FROM DUSER.DPRT_SALR_PAY_DESC A
         WHERE A.PAY_DT          = I_PAY_DT
         AND   A.SALR_PAY_DIV_CD = I_SALR_DIV
         --AND   A.SALR_CD         IN ('B01', 'B11', 'B12', 'B22', 'B23', 'B24', 'B25', 'B26', 'B27', 'B28', 'B32')
         AND   A.SALR_CD LIKE 'B%' 
         ORDER BY A.EMP_NO, A.SALR_CD;


  /*******************************************************************************
 *USE CASE �� : �޻�Ȯ��ó��
 *�� ��       : �޻�Ȯ��ó��(ȸ������, �����۾���..).
 *******************************************************************************/


BEGIN

    -- ��������, �ð� ����
    SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD')
    INTO T_CURRENT_DT
    FROM DUAL;
        
    SELECT TO_CHAR(SYSDATE,'HH24MISS')
    INTO T_CURRENT_TM
    FROM DUAL;   

    O_MSG := '';--
    -- Ȯ���� ��ȯ ������ ������ �ӽ� ���̺�.
    /*    create GLOBAL TEMPORARY table DUSER.HT_EMP_PAY_LEND
        (
                EMP_NO                CHAR(8)        NOT NULL  , -- ���
                SALR_PAY_DIV_CD       CHAR(1)        NOT NULL  , -- �޻� �۾�����
                LEND_KIND_CD          CHAR(1)        NOT NULL  , -- �뿩�����ڵ�
                LEND_SEQ              DECIMAL(1)     NOT NULL  ,  -- �뿩����
                INT_RT		          DECIMAL(6,2)   NOT NULL  ,  -- ������
                PRI_CNT               DECIMAL(3)     NOT NULL  ,  -- �޿�ȸ��
                INT_AMT               DECIMAL(13)    NOT NULL  ,  -- �뿩�� ���ڱݾ�
                PRI_AMT               DECIMAL(13)    NOT NULL     -- �뿩�� ���ݱݾ�
        )
        ON COMMIT  DELETE ROWS  

        COMMIT;
     */

	-- �߰��������� ������ �뿩���� �����ͼ� �ӽ����̺� ������.
	-- ���ñ����ڱ� ����(B23), ���ñ����ڱ� ����(B24), ���������ڱ� ����(25)
	-- ���������ڱ� ����(B26), ��Ȱ�����ڱ� ����(B27), ��Ȱ�����ڱ� ����(28)
	-- ������ �㺸���� ����(B32), ������ �㺸���� ������ �����ÿ��� ó����.
	INSERT INTO DUSER.HT_EMP_PAY_LEND
		 SELECT A.EMP_NO, A.SALR_PAY_DIV_CD,
		        SUBSTR(A.RMRK,1,1) AS LEND_KIND_CD,
		        TO_NUMBER(SUBSTR(A.RMRK,3,6)) AS LEND_SEQ,
				COALESCE(B.INT_RT,0),
		        MAX(CASE WHEN A.SALR_CD IN ('B23', 'B25', 'B27') THEN 1 ELSE 0 END) AS PRI_CNT,
		        SUM(CASE WHEN A.SALR_CD IN ('B24', 'B26', 'B28', 'B32') THEN A.AMT ELSE 0 END) AS INT_AMT,
		        SUM(CASE WHEN A.SALR_CD IN ('B23', 'B25', 'B27') THEN A.AMT ELSE 0 END) AS PRI_AMT
		   FROM DUSER.DPRT_ADD_PAY_DEDU A
			    LEFT OUTER JOIN DUSER.DPRT_RMBR_PLAN_DESC B
			      ON A.EMP_NO = B.EMP_NO
				 AND B.LEND_KIND_CD = SUBSTR(A.RMRK,1,1)
				 AND B.LEND_SEQ = TO_NUMBER(SUBSTR(A.RMRK,3,6))
				 AND B.DUE_DT = A.YYMM || '-21'
		  WHERE A.YYMM = SUBSTR(I_PAY_DT,1,7) AND SUBSTR(I_PAY_DT,9,2) = '21' AND A.SALR_PAY_DIV_CD = I_SALR_DIV
		    AND A.SALR_CD IN ('B23', 'B24', 'B25', 'B26', 'B27', 'B28', 'B32')
		  GROUP BY A.EMP_NO,  A.SALR_PAY_DIV_CD, SUBSTR(A.RMRK,1,1), SUBSTR(A.RMRK,3,6), B.INT_RT;--

    -- �ʱⰪ ����
    IF (I_SALR_DIV = 'P') THEN
         T_RMBR_DIV_CD := '1';--
    ELSIF (I_SALR_DIV = 'S') THEN
         T_RMBR_DIV_CD := '2';--
    ELSE
         T_RMBR_DIV_CD := '';--
    END IF;--



/***************************************************************************************/

    SELECT COUNT(*)            -- ���ᱸ�� 1:��������, 0:����������(�����̿Ϸ�)
    INTO T_CNT
    FROM DUSER.DPRT_SALR_PAY
    WHERE PAY_DT          = I_PAY_DT
    AND   SALR_PAY_DIV_CD = I_SALR_DIV ;--

	IF (T_CNT <= 0) THEN
         O_MSG := '0.�޿��۾��� �Ǿ� ���� �ʽ��ϴ�. [SQLCODE: ' || TO_CHAR(SQLCODE) || ']';--
		RETURN;--
	END IF;--

    -- 1. �������� Ȯ��
    SELECT COUNT(CLS_FG)            -- ���ᱸ�� 1:��������, 0:����������(�����̿Ϸ�)
    INTO   T_CLS_FG
    FROM DUSER.DPRT_SALR_CLS
    WHERE PAY_DT          = I_PAY_DT
    AND   SALR_PAY_DIV_CD = I_SALR_DIV
    --AND   EMP_NO          = I_EMP_NO
    AND   JOB_DIV_CD      = '1'    -- �۾�����
    AND   PROC_DIV_CD     = '*';  -- ó������ 1:����ó�� 0:������ó��

    -- 2. �縶�� �Ұ� ó��
    IF (T_CLS_FG = '1') THEN
         O_MSG := '1.�̹� �����Ǿ����ϴ�. [SQLCODE: ' || TO_CHAR(SQLCODE) || ']';--
         -- 2010.06.15 �縶���϶� ��������Ÿ �Է��� �ʿ����. �ּ�ó��
         --DUSER.DP_DSLSM08_11('1','',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
        RETURN;--
    END IF;--

    -- 1. ����ó�� �۾����� ����
    DELETE FROM DUSER.DPRT_SALR_CLS
    WHERE  PAY_DT          = I_PAY_DT
    AND    SALR_PAY_DIV_CD = I_SALR_DIV
    --AND    EMP_NO          = I_EMP_NO
    AND    JOB_DIV_CD      = '1';--


    -- 4. ���� ��ȯ ���� �ݿ� ����(������ ����, ������ ����, ��Ȱ�����ڱ�, �����ݴ㺸)
    DELETE FROM DUSER.DPRT_EMP_LEND_AMT_RMBR
    WHERE  RMBR_DT     = I_PAY_DT
    AND    RMBR_DIV_CD = T_RMBR_DIV_CD;--

    IF (SQLCODE NOT IN (00, 100)) THEN
         O_MSG := '4.������ ��ȯ���� ��������[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
         DUSER.DP_DSLSM08_11('1','4',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
        RETURN;--
    END IF;--


    -- 5. ���� ��ȯ ���� �ݿ� ����(���ȸ ��α�)
    DELETE FROM DUSER.DPRT_MATE_LEND_AMT_RMBR
     WHERE RMBR_DT     = I_PAY_DT
       AND RMBR_DIV_CD = T_RMBR_DIV_CD;--

    IF (SQLCODE NOT IN (00, 100)) THEN
         O_MSG := '5.���ȸ ��ȯ���� ��������[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
         DUSER.DP_DSLSM08_11('1','5',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
        RETURN;--
    END IF;--

    -- 6. ���� ��ȯ ���� �ݿ� ����(����ä����)
    DELETE FROM DUSER.DPRT_DEBT_DEDU_DESC
     WHERE DEDU_DT     = I_PAY_DT
     AND   DEDU_DIV_CD = T_RMBR_DIV_CD;--

    IF (SQLCODE NOT IN (00, 100)) THEN
         O_MSG := '6.����ä���� ��ȯ���� ��������[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
         DUSER.DP_DSLSM08_11('1','6',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
	RETURN;--
    END IF;--


    -- ���ȸ�� ���� �Է��� ���� �������� ��ȸ
        SELECT COALESCE(MAX(SEQ),0) AS SEQ
        INTO   T_MAX_SEQ
        FROM   DUSER.DPRT_MATE_AMT_DESC
        WHERE  IO_DT = I_PAY_DT ;--

        SELECT A.MATE_AMT_RAMT -- ���� �ܾ� ��ȸ
        INTO   T_MATE_AMT_RAMT
        FROM   DUSER.DPRT_MATE_AMT_DESC A,
              (SELECT C.IO_DT, MAX(C.SEQ) AS SEQ
               FROM DUSER.DPRT_MATE_AMT_DESC C,
                   (SELECT MAX(IO_DT) AS IO_DT
                    FROM DUSER.DPRT_MATE_AMT_DESC ) D
                    WHERE C.IO_DT=D.IO_DT
                    GROUP BY C.IO_DT) B
        WHERE A.IO_DT = B.IO_DT
        AND   A.SEQ   = B.SEQ;--



    -- �����ݾ��� �о� ��ȯ ���� ó��
    -- - ��ȯ�� �� ���������� �ѰǸ� �߻��ϴ°����� ���� - ����ä������ ���� �߻��� ����
    FOR CUR1 IN CUR_1 LOOP
        IF (T_EMP_NO <> CUR1.EMP_NO) THEN
            SELECT KOR_NAME INTO T_EMP_NM
            FROM   DUSER.DHRT_EMP_BASE
            WHERE  EMP_NO = CUR1.EMP_NO;--
        END IF;--
        IF (CUR1.SALR_CD = 'B01') THEN    -- ���ȸ�� ���� ����
            UPDATE DUSER.DPRT_MATE
               SET MATE_AMT_PAY_TCNT = MATE_AMT_PAY_TCNT + 1
                  ,LAST_PROC_DT      = T_CURRENT_DT
                  ,LAST_PROC_TM      = T_CURRENT_TM
                  ,LAST_PROC_EMP_NO  = I_USER_ID
             WHERE EMP_NO            = CUR1.EMP_NO;--

            IF (SQLCODE <> 00) THEN
                INSERT INTO DUSER.DPRT_MATE
                      (EMP_NO
                      ,ENTR_DT
                      ,MATE_AMT_PAY_TCNT
                      ,FRST_REG_DT
                      ,FRST_REG_TM
                      ,FRST_REG_EMP_NO
                      ,LAST_PROC_DT
                      ,LAST_PROC_TM
                      ,LAST_PROC_EMP_NO)
                VALUES(CUR1.EMP_NO
                      ,I_PAY_DT
                      ,1
                      ,T_CURRENT_DT
                      ,T_CURRENT_TM
                      ,I_USER_ID
                      ,T_CURRENT_DT
                      ,T_CURRENT_TM
                      ,I_USER_ID);--
                IF (SQLCODE <> 00) THEN
                     O_MSG := '8.���ȸ�� ���� ���� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ��� :' || CUR1.EMP_NO;--
                     DUSER.DP_DSLSM08_11('1','8',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                    RETURN;--
                END IF;--
            END IF;--

             T_AMT_B01 := T_AMT_B01 + CUR1.AMT;--

        ELSIF (CUR1.SALR_CD = 'B11') THEN    -- ���ȸ ��α� ��ȯ���� ����
            SELECT RMBR_PAMT ,PAMT_RAMT INTO T_RMBR_PAMT, T_PAMT_RAMT
              FROM DUSER.DPRT_MATE_LEND_AMT
             WHERE EMP_NO            = CUR1.EMP_NO
               AND SEQ               = TO_NUMBER(SUBSTR(CUR1.RMRK,3,6));--

              T_RMBR_PAMT := T_RMBR_PAMT + CUR1.AMT;--
              T_PAMT_RAMT := T_PAMT_RAMT - CUR1.AMT;--

            UPDATE DUSER.DPRT_MATE_LEND_AMT A
               SET RMBR_TCNT         = RMBR_TCNT + 1
                  ,RMBR_PAMT         = T_RMBR_PAMT
                  ,PAMT_RAMT         = T_PAMT_RAMT
                  ,EXPR_KIND_CD      = CASE WHEN PAMT_RAMT - CUR1.AMT = 0 THEN '20'
                                                                          ELSE '10'
                                       END
                  ,MATE_AMT_PAY_TCNT = (SELECT MATE_AMT_PAY_TCNT FROM DUSER.DPRT_MATE B WHERE A.EMP_NO = B.EMP_NO)
                  ,MATE_AMT          = (SELECT PAY_AMT           FROM DUSER.DPRT_MATE B WHERE A.EMP_NO = B.EMP_NO)
                  ,LAST_PROC_DT      = T_CURRENT_DT
                  ,LAST_PROC_TM      = T_CURRENT_TM
                  ,LAST_PROC_EMP_NO  = I_USER_ID
             WHERE EMP_NO            = CUR1.EMP_NO
               AND SEQ               = TO_NUMBER(SUBSTR(CUR1.RMRK,3,6));--

            IF (SQLCODE <> 00) THEN
                 O_MSG := '9.���ȸ ������� ���� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ��� :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','9',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--

            INSERT
              INTO DUSER.DPRT_MATE_LEND_AMT_RMBR
                  (EMP_NO            ,SEQ                   ,RMBR_DT
                  ,RMBR_PAMT         ,TMP_RMBR_PAMT         ,RMBR_DIV_CD
                  ,RMBR_TAMT         ,PAMT_RAMT
                  ,FRST_REG_DT       ,FRST_REG_TM           ,FRST_REG_EMP_NO
                  ,LAST_PROC_DT      ,LAST_PROC_TM          ,LAST_PROC_EMP_NO)
            VALUES(CUR1.EMP_NO       ,TO_NUMBER(SUBSTR(CUR1.RMRK,3,6)) ,I_PAY_DT
                  ,CUR1.AMT          ,0                     ,T_RMBR_DIV_CD
                  ,T_RMBR_PAMT       ,T_PAMT_RAMT
                  ,T_CURRENT_DT      ,T_CURRENT_TM          ,I_USER_ID
                  ,T_CURRENT_DT      ,T_CURRENT_TM          ,I_USER_ID);--

            IF (SQLCODE <> 00) THEN
                 O_MSG := 'A.���ȸ ���⳻�� ���� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ��� :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','A',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--

      -- ���ȸ�� ������ ���ȸ�뿩�� ��ȯ���� �Է�
             T_MAX_SEQ       := T_MAX_SEQ + 1;--
             T_MATE_AMT_RAMT := T_MATE_AMT_RAMT + CUR1.AMT;--

            INSERT
              INTO DUSER.DPRT_MATE_AMT_DESC
                  (IO_DT          ,SEQ            ,IO_ITEM_CD
                   ,IO_DIV_CD      ,PRPS_DESC
                   ,IAMT
                   ,MATE_AMT_RAMT
                   ,FRST_REG_DT    ,FRST_REG_TM    ,FRST_REG_EMP_NO
                   ,LAST_PROC_DT   ,LAST_PROC_TM   ,LAST_PROC_EMP_NO)
             VALUES(I_PAY_DT       ,T_MAX_SEQ      ,'11'              -- �����׸��ڵ� : ���ȸ��
                   ,'1'            ,SUBSTR(I_PAY_DT,1,7)||'���ȸ�뿩�ݻ�ȯ('||T_EMP_NM||')'
                   ,CUR1.AMT
                   ,T_MATE_AMT_RAMT
                   ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID
                   ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID);--

            IF (SQLCODE <> 00) THEN
                 O_MSG := 'B.���ȸ ���� �Է� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ��� :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','B',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--

        ELSIF (CUR1.SALR_CD = 'B12') THEN    -- ���ȸ ��α� ��ȯ���� ����
           UPDATE DUSER.DPRT_MATE_LEND_AMT A
               SET PAY_INT           = PAY_INT  + CUR1.AMT
                  ,LAST_PROC_DT      = T_CURRENT_DT
                  ,LAST_PROC_TM      = T_CURRENT_TM
                  ,LAST_PROC_EMP_NO  = I_USER_ID
             WHERE EMP_NO            = CUR1.EMP_NO
               AND SEQ               = TO_NUMBER(SUBSTR(CUR1.RMRK,3,6));--

            IF (SQLCODE <> 00) THEN
                 O_MSG := 'C.���ȸ ������� ���� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ��� :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','C',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--

            UPDATE DUSER.DPRT_MATE_LEND_AMT_RMBR
               SET RMBR_INT          = RMBR_INT + CUR1.AMT
                  ,LAST_PROC_DT      = T_CURRENT_DT
                  ,LAST_PROC_TM      = T_CURRENT_TM
                  ,LAST_PROC_EMP_NO  = I_USER_ID
             WHERE EMP_NO            = CUR1.EMP_NO
               AND SEQ               = TO_NUMBER(SUBSTR(CUR1.RMRK,3,6))
               AND RMBR_DT           = I_PAY_DT;--

            IF (SQLCODE <> 00) THEN
                 O_MSG := 'D.���ȸ ���⳻�� ���� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ��� :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','D',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--
        /*
            --���ȸ�뿩�� ���ڸ� ���ȸ ������ �Է�
            UPDATE DUSER.DPRT_MATE_AMT_DESC
               SET IAMT              = IAMT + CUR1.AMT
                  ,MATE_AMT_RAMT     = MATE_AMT_RAMT + CUR1.AMT
                  ,LAST_PROC_DT      = T_CURRENT_DT
                  ,LAST_PROC_TM      = T_CURRENT_TM
                  ,LAST_PROC_EMP_NO  = I_USER_ID
             WHERE EMP_NO            = CUR1.EMP_NO
               AND SEQ               = T_MAX_SEQ
               AND IO_ITEM_CD        = '11'
               AND IO_DIV_CD         = '1';--

            IF (SQLCODE <> 00) THEN
                 O_MSG := 'J.���ȸ�� ���� ���� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ��� :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','J',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--
        */
        END IF;--
    END LOOP;--



    -- �����뿩�� ���� ó��.(���������ڱ�, ���ñ����ڱ�, ��Ȱ�����ڱ�, ������ �㺸����)

	-- �ǰ��� �ݾװ� �߰����� ������ Ȯ��.
	SELECT COUNT(*) INTO T_CNT
	  FROM DUSER.DPRT_SALR_PAY_DESC A,
     	   (
      		SELECT A.EMP_NO, A.SALR_PAY_DIV_CD, A.SALR_CD, SUM(A.AMT) AS AMT
         	  FROM DUSER.DPRT_ADD_PAY_DEDU A
       		 WHERE A.YYMM = SUBSTR(I_PAY_DT,1,7) AND A.SALR_PAY_DIV_CD = I_SALR_DIV
       	     GROUP BY A.EMP_NO, A.SALR_PAY_DIV_CD, A.SALR_CD
	 	   ) B
	 WHERE A.EMP_NO = B.EMP_NO AND A.SALR_CD = B.SALR_CD
	   AND A.SALR_CD IN ('B23', 'B24', 'B25', 'B26', 'B27', 'B28', 'B32')
       AND A.PAY_DT = I_PAY_DT AND A.AMT <> B.AMT;--
    IF (T_CNT > 0) THEN
         O_MSG := 'F. [����ǹ���]������ �ݾװ� �߰������ݾ��� ���� Ʋ���ϴ�.';--
         DUSER.DP_DSLSM08_11('1','F',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
        RETURN;--
    END IF;--

	SELECT COUNT(*) INTO T_CNT FROM DUSER.HT_EMP_PAY_LEND;--
	IF (T_CNT > 0) THEN
		UPDATE DUSER.DPRT_EMP_LEND_AMT A SET (PAMT_RMBR_TCNT,  PAMT_RMBR_AMT, PAMT_RAMT, PAY_INT,
			  	  						      SALR_TOT_TCNT, SALR_TOT_PAMT, BONS_TOT_TCNT, BONS_TOT_PAMT,
										      RMBR_EXPR_DIV_CD, LAST_PROC_DT, LAST_PROC_TM, LAST_PROC_EMP_NO) =
	                                         (SELECT A.PAMT_RMBR_TCNT + 1,
										  		  	 A.PAMT_RMBR_AMT + PRI_AMT, A.PAMT_RAMT - PRI_AMT,
													 A.PAY_INT + INT_AMT,
							  	  					 A.SALR_TOT_TCNT + CASE WHEN PRI_AMT <> 0 AND SALR_PAY_DIV_CD = 'P' THEN 1 ELSE 0 END,
													 A.SALR_TOT_PAMT + CASE WHEN PRI_AMT <> 0 AND  SALR_PAY_DIV_CD = 'P' THEN PRI_AMT ELSE 0 END,
													 A.BONS_TOT_TCNT + CASE WHEN PRI_AMT <> 0 AND  SALR_PAY_DIV_CD = 'B' THEN 1 ELSE 0 END,
													 A.BONS_TOT_PAMT + CASE WHEN PRI_AMT <> 0 AND  SALR_PAY_DIV_CD = 'B' THEN PRI_AMT ELSE 0 END,
													 CASE WHEN A.PAMT_RAMT - PRI_AMT > 0 THEN '10' ELSE '20' END,
													 T_CURRENT_DT, T_CURRENT_TM, I_USER_ID
										        FROM DUSER.HT_EMP_PAY_LEND B
										       WHERE A.EMP_NO = B.EMP_NO AND A.LEND_KIND_CD = B.LEND_KIND_CD AND A.LEND_SEQ = B.LEND_SEQ
										     )
		WHERE EXISTS (SELECT '1'  FROM DUSER.HT_EMP_PAY_LEND B
				       WHERE A.EMP_NO = B.EMP_NO AND A.LEND_KIND_CD = B.LEND_KIND_CD
				         AND A.LEND_SEQ = B.LEND_SEQ);--
	    IF (SQLCODE <> 00) THEN
	         O_MSG := 'G.������ ���⳻�� ���� ����[SQLCODE :' || TO_CHAR(SQLCODE) || '] ';--
	        DUSER.DP_DSLSM08_11('1','G',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
	        RETURN;--
	    END IF;--

	    INSERT
	      INTO DUSER.DPRT_EMP_LEND_AMT_RMBR
	          (EMP_NO                ,LEND_KIND_CD           ,LEND_SEQ
	          ,RMBR_DT               ,RMBR_PAMT              ,TMP_RMBR_PAMT
	          ,RMBR_DIV_CD           ,RMBR_TAMT              ,PAMT_RAMT
	          ,RMBR_INT			     ,RMBR_INT_RT
	          ,FRST_REG_DT           ,FRST_REG_TM            ,FRST_REG_EMP_NO
	          ,LAST_PROC_DT          ,LAST_PROC_TM           ,LAST_PROC_EMP_NO)
	   SELECT A.EMP_NO               ,A.LEND_KIND_CD         ,A.LEND_SEQ
	          ,I_PAY_DT              ,A.PRI_AMT              ,0
	          ,T_RMBR_DIV_CD         ,B.PAMT_RMBR_AMT        ,B.PAMT_RAMT
	          ,A.INT_AMT             ,A.INT_RT
	          ,T_CURRENT_DT          ,T_CURRENT_TM           ,I_USER_ID
	          ,T_CURRENT_DT          ,T_CURRENT_TM           ,I_USER_ID
	     FROM DUSER.HT_EMP_PAY_LEND A, DUSER.DPRT_EMP_LEND_AMT B
	    WHERE A.EMP_NO = B.EMP_NO AND A.LEND_KIND_CD = B.LEND_KIND_CD
	      AND A.LEND_SEQ = B.LEND_SEQ;--
	    IF (SQLCODE <> 00) THEN
	         O_MSG := 'H.������ �����ȯ���� �ۼ� ����[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
	        DUSER.DP_DSLSM08_11('1','H',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
	        RETURN;--
	    END IF;--
	END IF;--


    IF (T_AMT_B01 > 0) THEN   --  ���ȸ�� ������ ���
         T_MAX_SEQ       := T_MAX_SEQ + 1;--
         T_MATE_AMT_RAMT := T_MATE_AMT_RAMT + T_AMT_B01;--

        INSERT
          INTO DUSER.DPRT_MATE_AMT_DESC
              (IO_DT          ,SEQ            ,IO_ITEM_CD
              ,IO_DIV_CD      ,PRPS_DESC      ,IAMT
              ,MATE_AMT_RAMT
              ,FRST_REG_DT    ,FRST_REG_TM    ,FRST_REG_EMP_NO
              ,LAST_PROC_DT   ,LAST_PROC_TM   ,LAST_PROC_EMP_NO)
        VALUES(I_PAY_DT       ,T_MAX_SEQ      ,'01'              -- �����׸��ڵ� : ���ȸ��
              ,'1'            ,SUBSTR(I_PAY_DT,1,7)||'���ȸ��'
              ,T_AMT_B01
              ,T_MATE_AMT_RAMT
              ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID
              ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID);--

        IF (SQLCODE <> 00) THEN
             O_MSG := 'I.���ȸ�� ���� �Է� ����[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
                DUSER.DP_DSLSM08_11('1','I',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
            RETURN;--
        END IF;--
    END IF;--

    -- �������� ���μ��� ����ÿ� ����ó��.
     O_MSG := '[�޿����� :' || I_SALR_DIV || '] ���� �Ϸ�';--
    DUSER.DP_DSLSM08_11('1', '*', I_SALR_DIV, I_PAY_DT, '1','', O_MSG, I_USER_ID, I_EMP_NO , O_MSG);--
END;