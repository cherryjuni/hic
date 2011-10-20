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

    -- SQLCODE, SQLSTATE, 반환용 변수 선언
    SQLCODE                   INT            := 0   ;    -- SQLCODE 변수
    SQLSTATE                  CHAR(5)        := '0' ;    -- SQLSTATE 변수
    V_RETURN                  INT            := 0   ;    -- SP 실행 상태 변수
    T_CURRENT_DT              CHAR(10)       := ''   ; -- 현재일자
    T_CURRENT_TM              CHAR(8)        := ''   ; -- 현재시간

    -- 변수 선언..
    T_RMBR_DIV_CD             CHAR(1)        :=  '' ; --상환구분 코드 1:급여 2:상여
    T_AMT_B01                 DECIMAL        :=  0.0; --사우회비 총액
    T_MAX_SEQ                 INT            :=  0  ; --사우회 내역에 최종 SEQ
    T_MATE_AMT_RAMT           DECIMAL        :=  0.0; --사우회비 잔액 총액
    T_CLS_FG                  CHAR(1)        :=  '' ;--
    T_CNT                     INT            :=  0  ;--
    T_EMP_NO                  CHAR(8)        :=  '' ; --사번
    T_EMP_NM                  VARCHAR2(20)   :=  '' ; --성명
    T_PAMT_RAMT               DECIMAL        :=  0.0; --상환잔액
    T_RMBR_PAMT               DECIMAL        :=  0.0; --상환총액
    T_MSG                     VARCHAR2(100)  :=  '' ; --메세지


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
 *USE CASE 명 : 급상여확정처리
 *내 용       : 급상여확정처리(회차관리, 마감작업등..).
 *******************************************************************************/


BEGIN

    -- 현재일자, 시간 세팅
    SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD')
    INTO T_CURRENT_DT
    FROM DUAL;
        
    SELECT TO_CHAR(SYSDATE,'HH24MISS')
    INTO T_CURRENT_TM
    FROM DUAL;   

    O_MSG := '';--
    -- 확정월 상환 내역을 저장할 임시 테이블.
    /*    create GLOBAL TEMPORARY table DUSER.HT_EMP_PAY_LEND
        (
                EMP_NO                CHAR(8)        NOT NULL  , -- 사번
                SALR_PAY_DIV_CD       CHAR(1)        NOT NULL  , -- 급상여 작업구분
                LEND_KIND_CD          CHAR(1)        NOT NULL  , -- 대여종류코드
                LEND_SEQ              DECIMAL(1)     NOT NULL  ,  -- 대여순번
                INT_RT		          DECIMAL(6,2)   NOT NULL  ,  -- 이자율
                PRI_CNT               DECIMAL(3)     NOT NULL  ,  -- 급여회수
                INT_AMT               DECIMAL(13)    NOT NULL  ,  -- 대여금 이자금액
                PRI_AMT               DECIMAL(13)    NOT NULL     -- 대여금 원금금액
        )
        ON COMMIT  DELETE ROWS  

        COMMIT;
     */

	-- 추가공제에서 공제한 대여금을 가져와서 임시테이블에 저장함.
	-- 주택구입자금 원금(B23), 주택구입자금 이자(B24), 주택전세자금 원금(25)
	-- 주택전세자금 이자(B26), 생활안정자금 원금(B27), 생활안정자금 이자(28)
	-- 퇴직금 담보대출 이자(B32), 퇴직금 담보대출 원금은 퇴직시에만 처리됨.
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

    -- 초기값 설정
    IF (I_SALR_DIV = 'P') THEN
         T_RMBR_DIV_CD := '1';--
    ELSIF (I_SALR_DIV = 'S') THEN
         T_RMBR_DIV_CD := '2';--
    ELSE
         T_RMBR_DIV_CD := '';--
    END IF;--



/***************************************************************************************/

    SELECT COUNT(*)            -- 종료구분 1:정상종로, 0:비정상종료(마감미완료)
    INTO T_CNT
    FROM DUSER.DPRT_SALR_PAY
    WHERE PAY_DT          = I_PAY_DT
    AND   SALR_PAY_DIV_CD = I_SALR_DIV ;--

	IF (T_CNT <= 0) THEN
         O_MSG := '0.급여작업이 되어 있지 않습니다. [SQLCODE: ' || TO_CHAR(SQLCODE) || ']';--
		RETURN;--
	END IF;--

    -- 1. 마감여부 확인
    SELECT COUNT(CLS_FG)            -- 종료구분 1:정상종로, 0:비정상종료(마감미완료)
    INTO   T_CLS_FG
    FROM DUSER.DPRT_SALR_CLS
    WHERE PAY_DT          = I_PAY_DT
    AND   SALR_PAY_DIV_CD = I_SALR_DIV
    --AND   EMP_NO          = I_EMP_NO
    AND   JOB_DIV_CD      = '1'    -- 작업구분
    AND   PROC_DIV_CD     = '*';  -- 처리구분 1:정상처리 0:비정상처리

    -- 2. 재마감 불가 처리
    IF (T_CLS_FG = '1') THEN
         O_MSG := '1.이미 마감되었습니다. [SQLCODE: ' || TO_CHAR(SQLCODE) || ']';--
         -- 2010.06.15 재마감일때 마감데이타 입력할 필요없음. 주석처리
         --DUSER.DP_DSLSM08_11('1','',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
        RETURN;--
    END IF;--

    -- 1. 마감처리 작업내역 삭제
    DELETE FROM DUSER.DPRT_SALR_CLS
    WHERE  PAY_DT          = I_PAY_DT
    AND    SALR_PAY_DIV_CD = I_SALR_DIV
    --AND    EMP_NO          = I_EMP_NO
    AND    JOB_DIV_CD      = '1';--


    -- 4. 기존 상환 내역 반영 삭제(임직원 전세, 임직원 주택, 생활안정자금, 퇴직금담보)
    DELETE FROM DUSER.DPRT_EMP_LEND_AMT_RMBR
    WHERE  RMBR_DT     = I_PAY_DT
    AND    RMBR_DIV_CD = T_RMBR_DIV_CD;--

    IF (SQLCODE NOT IN (00, 100)) THEN
         O_MSG := '4.임직원 상환내역 삭제오류[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
         DUSER.DP_DSLSM08_11('1','4',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
        RETURN;--
    END IF;--


    -- 5. 기존 상환 내역 반영 삭제(사우회 대부금)
    DELETE FROM DUSER.DPRT_MATE_LEND_AMT_RMBR
     WHERE RMBR_DT     = I_PAY_DT
       AND RMBR_DIV_CD = T_RMBR_DIV_CD;--

    IF (SQLCODE NOT IN (00, 100)) THEN
         O_MSG := '5.사우회 상환내역 삭제오류[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
         DUSER.DP_DSLSM08_11('1','5',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
        RETURN;--
    END IF;--

    -- 6. 기존 상환 내역 반영 삭제(법정채무금)
    DELETE FROM DUSER.DPRT_DEBT_DEDU_DESC
     WHERE DEDU_DT     = I_PAY_DT
     AND   DEDU_DIV_CD = T_RMBR_DIV_CD;--

    IF (SQLCODE NOT IN (00, 100)) THEN
         O_MSG := '6.법정채무금 상환내역 삭제오류[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
         DUSER.DP_DSLSM08_11('1','6',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
	RETURN;--
    END IF;--


    -- 사우회비 내역 입력을 위한 기초정보 조회
        SELECT COALESCE(MAX(SEQ),0) AS SEQ
        INTO   T_MAX_SEQ
        FROM   DUSER.DPRT_MATE_AMT_DESC
        WHERE  IO_DT = I_PAY_DT ;--

        SELECT A.MATE_AMT_RAMT -- 기존 잔액 조회
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



    -- 공제금액을 읽어 상환 내역 처리
    -- - 상환은 한 대출유형에 한건만 발생하는것으로 가정 - 법정채무금은 추후 발생시 적용
    FOR CUR1 IN CUR_1 LOOP
        IF (T_EMP_NO <> CUR1.EMP_NO) THEN
            SELECT KOR_NAME INTO T_EMP_NM
            FROM   DUSER.DHRT_EMP_BASE
            WHERE  EMP_NO = CUR1.EMP_NO;--
        END IF;--
        IF (CUR1.SALR_CD = 'B01') THEN    -- 사우회비 내역 수정
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
                     O_MSG := '8.사우회비 내역 수정 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] 사번 :' || CUR1.EMP_NO;--
                     DUSER.DP_DSLSM08_11('1','8',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                    RETURN;--
                END IF;--
            END IF;--

             T_AMT_B01 := T_AMT_B01 + CUR1.AMT;--

        ELSIF (CUR1.SALR_CD = 'B11') THEN    -- 사우회 대부금 상환내역 적용
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
                 O_MSG := '9.사우회 대출원장 수정 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] 사번 :' || CUR1.EMP_NO;--
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
                 O_MSG := 'A.사우회 대출내역 수정 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] 사번 :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','A',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--

      -- 사우회비 내역에 사우회대여금 상환내역 입력
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
             VALUES(I_PAY_DT       ,T_MAX_SEQ      ,'11'              -- 입출항목코드 : 사우회비
                   ,'1'            ,SUBSTR(I_PAY_DT,1,7)||'사우회대여금상환('||T_EMP_NM||')'
                   ,CUR1.AMT
                   ,T_MATE_AMT_RAMT
                   ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID
                   ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID);--

            IF (SQLCODE <> 00) THEN
                 O_MSG := 'B.사우회 내역 입력 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] 사번 :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','B',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--

        ELSIF (CUR1.SALR_CD = 'B12') THEN    -- 사우회 대부금 상환내역 적용
           UPDATE DUSER.DPRT_MATE_LEND_AMT A
               SET PAY_INT           = PAY_INT  + CUR1.AMT
                  ,LAST_PROC_DT      = T_CURRENT_DT
                  ,LAST_PROC_TM      = T_CURRENT_TM
                  ,LAST_PROC_EMP_NO  = I_USER_ID
             WHERE EMP_NO            = CUR1.EMP_NO
               AND SEQ               = TO_NUMBER(SUBSTR(CUR1.RMRK,3,6));--

            IF (SQLCODE <> 00) THEN
                 O_MSG := 'C.사우회 대출원장 수정 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] 사번 :' || CUR1.EMP_NO;--
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
                 O_MSG := 'D.사우회 대출내역 수정 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] 사번 :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','D',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--
        /*
            --사우회대여금 이자를 사우회 관리에 입력
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
                 O_MSG := 'J.사우회비 내역 수정 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] 사번 :' || CUR1.EMP_NO;--
                 DUSER.DP_DSLSM08_11('1','J',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
                RETURN;--
            END IF;--
        */
        END IF;--
    END LOOP;--



    -- 직원대여금 관련 처리.(주택전세자금, 주택구입자금, 생활안정자금, 퇴직금 담보대출)

	-- 실공제 금액과 추가공제 대상액을 확인.
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
         O_MSG := 'F. [전산실문의]공제한 금액과 추가공제금액의 값이 틀림니다.';--
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
	         O_MSG := 'G.임직원 대출내역 수정 오류[SQLCODE :' || TO_CHAR(SQLCODE) || '] ';--
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
	         O_MSG := 'H.임직원 대출상환내역 작성 오류[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
	        DUSER.DP_DSLSM08_11('1','H',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
	        RETURN;--
	    END IF;--
	END IF;--


    IF (T_AMT_B01 > 0) THEN   --  사우회비 내역에 등록
         T_MAX_SEQ       := T_MAX_SEQ + 1;--
         T_MATE_AMT_RAMT := T_MATE_AMT_RAMT + T_AMT_B01;--

        INSERT
          INTO DUSER.DPRT_MATE_AMT_DESC
              (IO_DT          ,SEQ            ,IO_ITEM_CD
              ,IO_DIV_CD      ,PRPS_DESC      ,IAMT
              ,MATE_AMT_RAMT
              ,FRST_REG_DT    ,FRST_REG_TM    ,FRST_REG_EMP_NO
              ,LAST_PROC_DT   ,LAST_PROC_TM   ,LAST_PROC_EMP_NO)
        VALUES(I_PAY_DT       ,T_MAX_SEQ      ,'01'              -- 입출항목코드 : 사우회비
              ,'1'            ,SUBSTR(I_PAY_DT,1,7)||'사우회비'
              ,T_AMT_B01
              ,T_MATE_AMT_RAMT
              ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID
              ,T_CURRENT_DT   ,T_CURRENT_TM   ,I_USER_ID);--

        IF (SQLCODE <> 00) THEN
             O_MSG := 'I.사우회비 내역 입력 오류[SQLCODE :' || TO_CHAR(SQLCODE) || ']';--
                DUSER.DP_DSLSM08_11('1','I',I_SALR_DIV,I_PAY_DT,'0','',O_MSG, I_USER_ID, I_EMP_NO ,T_MSG);--
            RETURN;--
        END IF;--
    END IF;--

    -- 문제없이 프로세스 진행시에 마감처리.
     O_MSG := '[급여구분 :' || I_SALR_DIV || '] 마감 완료';--
    DUSER.DP_DSLSM08_11('1', '*', I_SALR_DIV, I_PAY_DT, '1','', O_MSG, I_USER_ID, I_EMP_NO , O_MSG);--
END;