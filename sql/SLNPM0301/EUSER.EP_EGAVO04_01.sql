-- DDL Script for PROCEDURE EUSER.EP_EGAVO04_01. Orange for ORACLE.
-- Generated on 2011/12/16 19:59:12 by ZUSER@CABISDEV

CREATE OR REPLACE PROCEDURE EUSER.EP_EGAVO04_01 (        
      I_VOCH_NO        IN    VARCHAR2        
    , I_ACCT_DT        IN    VARCHAR2        
    , I_EMP_NO        IN    VARCHAR2        
    , O_ERR_MSG        OUT    VARCHAR2        
)        
/******************************************************************************        
 *파일명        : EUSER.EP_EGAVO04_01        
 *버전        :        
 *작성일        :        
 *작성자        : 하리라        
 *USECASE명    : 전표결재(AGAVO04)        
 *내 용        : 전표번호에 따른 전표확정처리 프로세스(본지점전표 포함)        
*******************************************************************************/        
        
IS        
    V_CURR_DATE            VARCHAR2(10)    := TO_CHAR(SYSDATE, 'YYYY-MM-DD');    -- 현재일자        
    V_CURR_TIME            VARCHAR2(6)        := TO_CHAR(SYSDATE, 'HH24MISS');    -- 현재시각        
    V_SYS_DATE            VARCHAR2(8)        := TO_CHAR(SYSDATE, 'YYYYMMDD');    -- 채번을 위한 시스템일자        
        
    -- AT_전표_기본 체크용        
    V_VOCH_STAT_CD        VARCHAR2(2)        DEFAULT '';        
    V_JST_STAT_CD        VARCHAR2(2)        DEFAULT '';        -- 전표결재상태코드        
    V_MAKE_EMP_NO        VARCHAR2(8)        DEFAULT '';        -- 작성자 사원번호        
    V_MNBR_DIV_CD        CHAR(1)            DEFAULT '';        -- 본사여부        
    V_ACCT_DEPT_CD        VARCHAR2(6)        DEFAULT '';        -- 회계부서코드        
    V_ORG_VOCH_NO        VARCHAR2(12)    DEFAULT '';        -- 원전표번호 - 취소전표인지 구분하기 위함        
    V_TR_TP_CD            VARCHAR2(8)        DEFAULT '';        -- 거래유형        
        
    -- AT_전표_기본 저장용        
    V_VOCH_ACCT_DT        CHAR(10)        DEFAULT '';        
        
    -- AT_지급_계획 저장용        
    V_PAY_NO            VARCHAR(12)        DEFAULT '';        
    V_CAUS_VOCH_NO        VARCHAR2(12)    DEFAULT '';        
    V_CAUS_VOCH_SNUM    DECIMAL(5);        
    V_CAUS_ACCT_CD        VARCHAR2(10)    DEFAULT '';        
    V_CAUS_CUR_DIV_CD    VARCHAR2(3)        DEFAULT '';        
    V_CAUS_EXRT            DECIMAL(9, 2)    DEFAULT 0.0;        
    V_CAUS_FCUR_AMT        DECIMAL(13, 2)    DEFAULT 0.0;        
    V_CAUS_WCUR_AMT        DECIMAL(18)        DEFAULT 0;        
    V_CAUS_RMK            VARCHAR2(40)    DEFAULT '';        
    V_PAY_OBJ_DIV_CD    CHAR(1)            DEFAULT '';        
    V_PAYPLC_CD            VARCHAR2(13)    DEFAULT '';        
    V_PAY_RQST_DEPT_CD    VARCHAR2(6)        DEFAULT '';        
    V_PAY_RQST_DT        VARCHAR2(10)    DEFAULT '';        
    V_PAY_RQST_METH_CD    VARCHAR2(2)        DEFAULT '';        
    V_PAY_STAT_CD        VARCHAR2(2)        DEFAULT '';        
    V_GIRO_FG            CHAR(1)            DEFAULT '0';        
    V_PAY_PROC_FG        CHAR(1)            DEFAULT '0';        
    V_PAY_DEPO_NO        VARCHAR2(5)        DEFAULT '';        -- 지급예금번호        
    TMP_ACCT_CD            VARCHAR2(10)    DEFAULT '';        -- 관리부서의 회계부서        
    V_TMP_MNG_NO        VARCHAR2(16)    DEFAULT '';        -- 넘어온 관리번호체크        
    V_CARD_NO            VARCHAR2(16)    DEFAULT '';        -- 증빙에 등록된 카드번호       
    V_CARD_INCO_DCD        CHAR(1)            DEFAULT '';        -- 카드 개인/법인 구분코드       
        
    -- AT_총계정원장,AT_거래처_원장,AT_예금_원장 저장용        
    V_LOC_DIV_CD        VARCHAR2(3)        DEFAULT '';        
    V_ACCT_DT            VARCHAR2(10)    DEFAULT '';        
    V_DEPT_CD            VARCHAR2(6)        DEFAULT '';        
    V_ACCT_CD            VARCHAR2(10)    DEFAULT '';        
    V_CLAC_ADJU_FG        CHAR(1)            DEFAULT '0';        
    V_BUSI_CD            VARCHAR2(13)    DEFAULT '';        
    V_DEPO_NO            VARCHAR2(5)        DEFAULT '';        
    V_DR_AMT            DECIMAL(18)        DEFAULT 0;        
    V_CR_AMT            DECIMAL(18)        DEFAULT 0;        
    V_DR_CNT            INTEGER            DEFAULT 0;        
    V_CR_CNT            INTEGER            DEFAULT 0;        
        
    -- AT_총계정원장,AT_거래처_원장,AT_예금_원장 기등록여부 체크용        
    V_TACCT_LDGR_FG        CHAR(1)            DEFAULT '0';        
    V_BUSI_LDGR_FG        CHAR(1)            DEFAULT '0';        
    V_DEPO_LDGR_FG        CHAR(1)            DEFAULT '0';        
    V_PROF_FG            CHAR(1)            DEFAULT '0';        
        
    -- 마감여부 체크 카운트        
    V_TMP_CNT            DECIMAL(5)        := 0;        
        
    -- User Exception        
    SQLCODE                INTEGER            DEFAULT 0;        
    CHK_ERR_FG            CHAR(1)            DEFAULT '0';    -- 에러여부 체크용 플래그        
    USR_DEF_ERR            EXCEPTION;        
    ERR_MSG                VARCHAR2(100)    DEFAULT '';        
        
    CURSOR CUR_CONFIRM3 IS (        
        SELECT        
            C.LOC_DIV_CD        
            , C.ACCT_DT        
            , C.ACCT_DEPT_CD AS DEPT_CD        
            , C.ACCT_CD        
            , C.CLAC_VOCH_FG AS CLAC_ADJU_FG        
            , C.BUSI_CD        
            , C.DEPO_NO        
            , SUM(C.DR_AMT) DR_AMT        
            , SUM(C.CR_AMT) CR_AMT        
            , SUM(C.DR_CNT) DR_CNT        
            , SUM(C.CR_CNT) CR_CNT        
        FROM (        
            SELECT        
                A.LOC_DIV_CD        
                , A.ACCT_DT        
                , A.ACCT_DEPT_CD        
                , B.ACCT_CD        
                , A.CLAC_VOCH_FG        
                , CASE  WHEN (    SELECT    BUSI_MNG_FG        
                                FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4)        
                                        AND RAMT_ACCT_FG = '1') = '1'        
                        THEN B.MNG_BUSI_CD        
                        ELSE ''        
                        END        AS BUSI_CD        
                , CASE  WHEN (    SELECT    MNG_NO_DIV_CD        
                                FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD        
                                        AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4) ) = '02'        
                        THEN (    SELECT    MNG_NO        
                                FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4) )        
                        ELSE ''        
                        END        AS DEPO_NO        
                , CASE WHEN B.DC_DIV_CD = 'D' THEN B.WCUR_AMT ELSE 0 END    AS DR_AMT        
                , CASE WHEN B.DC_DIV_CD = 'C' THEN B.WCUR_AMT ELSE 0 END    AS CR_AMT        
                , CASE WHEN B.DC_DIV_CD = 'D' THEN 1 ELSE 0 END                AS DR_CNT        
                , CASE WHEN B.DC_DIV_CD = 'C' THEN 1 ELSE 0 END                AS CR_CNT        
            FROM        
                EUSER.EACT_VOCH_BASE A        
                , EUSER.EACT_VOCH_DESC B        
            WHERE        
                A.VOCH_NO IN (    SELECT    I_VOCH_NO        
                                FROM    DUAL        
        
                                UNION        
        
                                SELECT    B.VOCH_NO        
                                FROM    EUSER.EACT_VOCH_BASE C        
                                        , EUSER.EACT_MNBR_VOCH B        
                                WHERE    C.TR_TP_DIV_CD = 'A0016'        
                                        AND C.MNBR_VOCH_DIV_CD = '3'        
                                        AND B.CNTR_VOCH_NO = I_VOCH_NO        
                                        AND C.VOCH_NO = I_VOCH_NO        
        
                                UNION        
        
                                SELECT    B.CNTR_VOCH_NO        
                                FROM    EUSER.EACT_VOCH_BASE C        
                                        , EUSER.EACT_MNBR_VOCH B        
                                WHERE    (C.TR_TP_DIV_CD <> 'A0016' OR C.MNBR_VOCH_DIV_CD <> '3')        
                                        AND B.VOCH_NO = I_VOCH_NO        
                                        AND C.VOCH_NO = I_VOCH_NO        
                                )        
                AND A.VOCH_NO = B.VOCH_NO        
            ) C        
        GROUP BY        
            C.LOC_DIV_CD, C.ACCT_DT, C.ACCT_DEPT_CD, C.ACCT_CD, C.CLAC_VOCH_FG, C.BUSI_CD, C.DEPO_NO        
        );        
        
    -- 지급관련        
    CURSOR CUR_CONFIRM1 IS (        
        SELECT        
            A.LOC_DIV_CD        
            , A.VOCH_NO        
            , B.VOCH_SNUM        
            , B.ACCT_CD        
            , B.CUR_DIV_CD        
            , B.EXRT        
            , B.FCUR_AMT        
            , CASE  WHEN (    SELECT    1        
                            FROM    EUSER.ETXT_WTAX        
                            WHERE    VOCH_NO = I_VOCH_NO AND DEL_FG = '0' AND SUBM_FG = '0') = 1        
                    THEN B.WCUR_AMT - (    SELECT    NVL(CTAX, 0) + NVL(ITAX, 0) + NVL(HTAX, 0) + NVL(FTAX, 0)        
                                        FROM    EUSER.ETXT_WTAX        
                                        WHERE    VOCH_NO = I_VOCH_NO )        
                    ELSE B.WCUR_AMT        
                    END        AS WCUR_AMT        
            , DECODE(B.MNG_NO, '40', NVL(EUSER.EF_BUSI(B.MNG_BUSI_CD), B.RMK), B.RMK) AS RMK        
            , CASE WHEN C.PAY_OBJ_DIV_CD IS NULL   
                   THEN NVL2(B.MNG_BUSI_CD,'1','2')   
                   ELSE C.PAY_OBJ_DIV_CD  
                   END         AS PAY_OBJ_DIV_CD  
            , CASE WHEN C.PAY_OBJ_DIV_CD IS NULL   
                   THEN NVL(B.MNG_BUSI_CD, B.MNG_EMP_NO)  
                   ELSE DECODE(C.PAY_OBJ_DIV_CD, '1', C.BUSI_CD, C.USE_EMP_NO)  
                   END         AS PAYPLC_CD  
            --, C.PAY_OBJ_DIV_CD        
            --, DECODE(C.PAY_OBJ_DIV_CD, '1', C.BUSI_CD, C.USE_EMP_NO)    AS PAYPLC_CD                   
            , B.MNG_DEPT_CD        AS PAY_RQST_DEPT_CD        
            , B.MNG_DT            AS PAY_RQST_DT        
            , DECODE(B.MNG_NO, '50', '20', B.MNG_NO)    AS PAY_RQST_METH_CD    -- 지급요청 : 지로/인출 -> 제예금        
            , '20'    AS PAY_STAT_CD        
            , DECODE(B.MNG_NO, '50', '1', '0')            AS GIRO_FG        
            , '0'    AS PAY_PROC_FG        
            , B.MNG_NO        
        FROM        
            EUSER.EACT_VOCH_BASE A        
                LEFT OUTER JOIN EUSER.EACT_VOCH_DESC B        
                    ON  A.VOCH_NO = B.VOCH_NO        
                    AND (B.ACCT_CD LIKE '2101%' OR B.ACCT_CD LIKE '2104%')    -- 미지급금/미지급비용 계정        
                    AND B.DC_DIV_CD = 'C'        
                    AND (B.CAUS_VOCH_NO IS NULL OR LENGTH(TRIM(B.CAUS_VOCH_NO)) != 12)        
                LEFT OUTER JOIN (        
                    -- 증빙       
                    SELECT        
                        C1.VOCH_NO        
                        , C1.PROF_KIND_CD        
                        , C1.PROF_NO        
                        , C1.PAY_OBJ_DIV_CD        
                        , CASE  WHEN PROF_KIND_CD = '03' AND PAY_OBJ_DIV_CD = '1' THEN '2138615419'      
                                ELSE C1.BUSI_CD      
                                END        AS BUSI_CD      
                        , C1.USE_EMP_NO        
                    FROM        
                        EUSER.EACT_PROF_BASE C1        
                        , EUSER.EACT_PROF_DESC C2        
                    WHERE        
                        VOCH_NO = I_VOCH_NO        
                        AND C1.PROF_NO = C2.PROF_NO        
                    GROUP BY        
                        C1.VOCH_NO, C1.PROF_KIND_CD, C1.PROF_NO        
                        , C1.PAY_OBJ_DIV_CD, C1.BUSI_CD, C1.USE_EMP_NO        
                            
                    UNION ALL        
                    -- 직원경비        
                    SELECT        
                          C1.VOCH_NO        
                        , C1.PROF_KIND_CD        
                        , C1.PROF_NO        
                        , '2'    AS PAY_OBJ_DIV_CD        
                        , ''    AS BUSI_CD        
                        , C2.USE_EMP_NO        
                    FROM        
                        EUSER.EACT_EMP_EXPN_BASE C1        
                        , EUSER.EACT_EMP_EXPN_DESC C2        
                    WHERE        
                        VOCH_NO = I_VOCH_NO        
                        AND C1.PROF_NO = C2.PROF_NO        
                    ) C        
                    ON A.VOCH_NO = C.VOCH_NO      
                    AND B.PROF_NO = C.PROF_NO     
        WHERE        
            A.VOCH_NO = I_VOCH_NO        
            AND (A.ORG_VOCH_NO IS NULL OR LENGTH(TRIM(A.ORG_VOCH_NO)) != 12)        
    );        
        
    CURSOR CUR_CONFIRM2 IS (        
        SELECT        
            C.LOC_DIV_CD        
            , C.ACCT_DT        
            , C.ACCT_DEPT_CD AS DEPT_CD        
            , C.ACCT_CD        
            , C.CLAC_VOCH_FG AS CLAC_ADJU_FG        
            , C.BUSI_CD        
            , C.DEPO_NO        
            , SUM(C.DR_AMT)    DR_AMT        
            , SUM(C.CR_AMT)    CR_AMT        
            , SUM(C.DR_CNT)    DR_CNT        
            , SUM(C.CR_CNT)    CR_CNT        
        FROM (        
            SELECT        
                A.LOC_DIV_CD, A.ACCT_DT, A.ACCT_DEPT_CD, B.ACCT_CD, A.CLAC_VOCH_FG        
                , CASE  WHEN (    SELECT    BUSI_MNG_FG        
                                FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD        
                                        AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4)        
                                        AND RAMT_ACCT_FG = '1') = '1'        
                        THEN B.MNG_BUSI_CD        
                        ELSE ''        
                        END     AS BUSI_CD        
                , CASE  WHEN (    SELECT    MNG_NO_DIV_CD        
                                FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD        
                                        AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4) ) = '02'    -- 예금번호        
                        THEN (    SELECT    MNG_NO        
                                FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD        
                                        AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4) )        
                         ELSE ''        
                         END     AS DEPO_NO        
                , CASE WHEN B.DC_DIV_CD = 'D' THEN B.WCUR_AMT ELSE 0 END    AS DR_AMT        
                , CASE WHEN B.DC_DIV_CD = 'C' THEN B.WCUR_AMT ELSE 0 END    AS CR_AMT        
                , CASE WHEN B.DC_DIV_CD = 'D' THEN 1 ELSE 0 END                AS DR_CNT        
                , CASE WHEN B.DC_DIV_CD = 'C' THEN 1 ELSE 0 END                AS CR_CNT        
            FROM        
                EUSER.EACT_VOCH_BASE A        
                , EUSER.EACT_VOCH_DESC B        
            WHERE        
                A.VOCH_NO IN (    SELECT    I_VOCH_NO        
                                FROM    DUAL        
        
                                UNION        
        
                                SELECT    B.VOCH_NO        
                                FROM    EUSER.EACT_VOCH_BASE C        
                                        , EUSER.EACT_MNBR_VOCH B        
                                WHERE    C.TR_TP_DIV_CD = 'A0016'        
                                        AND C.MNBR_VOCH_DIV_CD = '3'        
                                        AND B.CNTR_VOCH_NO = I_VOCH_NO        
                                        AND C.VOCH_NO = I_VOCH_NO        
        
                                UNION        
        
                                SELECT    B.CNTR_VOCH_NO        
                                FROM    EUSER.EACT_VOCH_BASE C        
                                        , EUSER.EACT_MNBR_VOCH B        
                                WHERE    (C.TR_TP_DIV_CD <> 'A0016' OR C.MNBR_VOCH_DIV_CD <> '3')        
                                        AND B.VOCH_NO = I_VOCH_NO        
                                        AND C.VOCH_NO = I_VOCH_NO        
                                )        
                AND A.VOCH_NO = B.VOCH_NO        
            ) C        
        GROUP BY        
            C.LOC_DIV_CD, C.ACCT_DT, C.ACCT_DEPT_CD, C.ACCT_CD, C.CLAC_VOCH_FG, C.BUSI_CD, C.DEPO_NO        
    );        
        
    -- 총계정원장        
    CURSOR CUR_CONFIRM4 IS (        
        SELECT        
            C.LOC_DIV_CD        
            , C.ACCT_DT        
            , C.ACCT_DEPT_CD    AS DEPT_CD        
            , C.ACCT_CD        
            , C.CLAC_VOCH_FG    AS CLAC_ADJU_FG        
            , C.BUSI_CD        
            , C.DEPO_NO        
            , SUM(C.DR_AMT)        AS DR_AMT        
            , SUM(C.CR_AMT)        AS CR_AMT        
            , SUM(C.DR_CNT)        AS DR_CNT        
            , SUM(C.CR_CNT)        AS CR_CNT        
        FROM (        
              SELECT        
                  A.LOC_DIV_CD        
                  , A.ACCT_DT        
                  , A.ACCT_DEPT_CD        
                  , B.ACCT_CD        
                  , A.CLAC_VOCH_FG        
                  , CASE  WHEN (    SELECT    BUSI_MNG_FG        
                                  FROM    EUSER.EACT_ACCT        
                                  WHERE    ACCT_CD = B.ACCT_CD        
                                        AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4)        
                                        AND RAMT_ACCT_FG = '1'        
                                ) = '1'        
                        THEN B.MNG_BUSI_CD        
                        ELSE ''        
                        END        AS BUSI_CD        
                , CASE  WHEN (    SELECT    MNG_NO_DIV_CD        
                            FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD        
                                        AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4)        
                                ) = '02'        
                        THEN (    SELECT    MNG_NO        
                                FROM    EUSER.EACT_ACCT        
                                WHERE    ACCT_CD = B.ACCT_CD        
                                        AND ACCT_YY = SUBSTR(A.VOCH_NO, 1, 4))        
                        ELSE ''        
                        END        AS DEPO_NO        
                , CASE WHEN B.DC_DIV_CD = 'D' THEN B.WCUR_AMT ELSE 0 END    AS DR_AMT        
                , CASE WHEN B.DC_DIV_CD = 'C' THEN B.WCUR_AMT ELSE 0 END    AS CR_AMT        
                , CASE WHEN B.DC_DIV_CD = 'D' THEN 1 ELSE 0 END                 AS DR_CNT        
                , CASE WHEN B.DC_DIV_CD = 'C' THEN 1 ELSE 0 END                AS CR_CNT        
            FROM        
                EUSER.EACT_VOCH_BASE A        
                , EUSER.EACT_VOCH_DESC B        
            WHERE        
                A.VOCH_NO IN (    SELECT    I_VOCH_NO        
                                FROM    DUAL        
        
                                UNION        
        
                                SELECT    B.VOCH_NO        
                                FROM    EUSER.EACT_VOCH_BASE C        
                                        , EUSER.EACT_MNBR_VOCH B        
                                WHERE        
                                    C.TR_TP_DIV_CD = 'A0016'        
                                    AND C.MNBR_VOCH_DIV_CD = '3'        
                                    AND B.CNTR_VOCH_NO = I_VOCH_NO        
                                    AND C.VOCH_NO = I_VOCH_NO        
        
                                UNION        
        
                                SELECT    B.CNTR_VOCH_NO        
                                FROM    EUSER.EACT_VOCH_BASE C        
                                        , EUSER.EACT_MNBR_VOCH B        
                                WHERE        
                                    (C.TR_TP_DIV_CD <> 'A0016' OR C.MNBR_VOCH_DIV_CD <> '3')        
                                    AND B.VOCH_NO = I_VOCH_NO        
                                    AND C.VOCH_NO = I_VOCH_NO        
                                )        
                AND A.VOCH_NO = B.VOCH_NO        
            ) C        
        GROUP BY        
            C.LOC_DIV_CD, C.ACCT_DT, C.ACCT_DEPT_CD, C.ACCT_CD, C.CLAC_VOCH_FG, C.BUSI_CD, C.DEPO_NO        
    );        
        
        
BEGIN        
        
    -- 1. AT_전표_기본 조회 - 기확정여부 체크        
    SELECT        
        VOCH_STAT_CD        
        , MAKE_EMP_NO        
        , ACCT_DT        
        , ORG_VOCH_NO        
        , TR_TP_DIV_CD || TR_STAG_DIV_CD        
        , PROF_FG        
    INTO        
        V_VOCH_STAT_CD        
        , V_MAKE_EMP_NO        
        , V_VOCH_ACCT_DT        
        , V_ORG_VOCH_NO        
        , V_TR_TP_CD        
        , V_PROF_FG        
    FROM        
        EUSER.EACT_VOCH_BASE        
    WHERE        
        VOCH_NO = I_VOCH_NO;        
        
    IF(SQLCODE < 0) THEN        
        ERR_MSG  := '대상 전표번호 조회 중 문제가 발생하였습니다.';        
        CHK_ERR_FG := '1';        
        RAISE USR_DEF_ERR;        
    END IF;        
        
    IF(V_VOCH_STAT_CD = '20') THEN        
        ERR_MSG  := '이미 확정처리가 된 전표입니다.';        
        CHK_ERR_FG := '1';        
        RAISE USR_DEF_ERR;        
    END IF;        
        
        
    IF(CHK_ERR_FG = '0') THEN        
        -- 2. AT_전표_기본 저장 - 대상이 되는 본지점전표들 모두 전표상태코드 = '확정', 회계일자 셋팅        
        
        -- 결산전표 등은 회계일자를 외부에서 파라미터로 지정한 값을 셋팅한다.        
        -- 회계일자 입력 받은일자로 그냥두기로함 2006-05-26        
        -- 결재단계 : 본사 회계팀 또는 본부는 1차 결재까지(20), 본사면서 회계가 아니면 2차결재까지(40)        
        --    -> (하이캐피탈)결재단계변경 : 모든 부서 결재단계는 2차결재까지..        
        SELECT        
            B.MNBR_DIV_CD        
            , B.DEPT_CD        
        INTO        
            V_MNBR_DIV_CD        
            , V_ACCT_DEPT_CD        
        FROM        
            DUSER.DHRT_EMP_BASE A        
            , DUSER.DHRT_ORG B        
        WHERE        
            A.EMP_NO = TRIM(V_MAKE_EMP_NO)        
            AND A.DEPT_CD = B.DEPT_CD;        
        
        IF(SQLCODE < 0) THEN        
            ERR_MSG  := '작성자 상태확인 중 오류 발생';        
            CHK_ERR_FG := '1';        
            RAISE USR_DEF_ERR;        
        END IF;        
        
        /* [2010-02-10] (하이캐피탈:결재단계 변경) 모두 2차결재완료로..  */      
        V_JST_STAT_CD := '40';        
        /*         
        IF V_MNBR_DIV_CD = '2' OR V_ACCT_DEPT_CD = '230000' THEN        
            V_JST_STAT_CD := '20';        
        ELSE        
            V_JST_STAT_CD := '40';        
        END IF;        
        */        
        
        -----------------------------------------------------------------------------        
        -- 업무마감 체크 2006-06-21        
        IF SUBSTR(V_TR_TP_CD, 1, 5) != 'A0099' THEN        
        
            -- 자기 부서 또는 상대부서가 업무마감이면 작업 안되요...        
            SELECT        
                COUNT(*)        
            INTO        
                V_TMP_CNT        
            FROM        
                EUSER.EACT_VOCH_BASE A        
                , EUSER.EACT_FINC_CLS B        
            WHERE        
                A.ACCT_DEPT_CD = B.DEPT_CD        
                AND A.ACCT_DT = B.ACCT_DT        
--                AND B.BSN_CLS_FG = '1'        
                AND B.ADMN_CLS_FG = '1'                
                AND A.VOCH_NO IN (    SELECT    I_VOCH_NO        
                                    FROM    DUAL        
        
                                    UNION        
        
                                    SELECT    B.VOCH_NO        
                                    FROM    EUSER.EACT_VOCH_BASE C        
                                            , EUSER.EACT_MNBR_VOCH B        
                                    WHERE    C.TR_TP_DIV_CD = 'A0016'        
                                            AND C.MNBR_VOCH_DIV_CD = '3'        
                                            AND B.CNTR_VOCH_NO = I_VOCH_NO        
                                            AND C.VOCH_NO = I_VOCH_NO        
        
                                    UNION        
        
                                    SELECT    B.CNTR_VOCH_NO        
                                    FROM    EUSER.EACT_VOCH_BASE C        
                                            , EUSER.EACT_MNBR_VOCH B        
                                    WHERE    (C.TR_TP_DIV_CD <> 'A0016' OR C.MNBR_VOCH_DIV_CD <> '3')        
                                            AND B.VOCH_NO = I_VOCH_NO        
                                            AND C.VOCH_NO = I_VOCH_NO    )        
            ;        
        
            IF (SQLCODE < 0) THEN        
--                ERR_MSG  := '업무마감 체크 중 문제가 발생하였습니다.';        
                ERR_MSG  := '일반관리마감 체크 중 문제가 발생하였습니다.';                
                CHK_ERR_FG := '1';        
                RAISE USR_DEF_ERR;        
            END IF;        
        
            IF(V_TMP_CNT >= 1) THEN        
--                ERR_MSG  := '업무마감이라 작업할 수 없습니다.';        
                ERR_MSG  := '일반관리마감이라 작업할 수 없습니다.';
                CHK_ERR_FG := '1';        
                RAISE USR_DEF_ERR;        
            END IF;        
        
        END IF;        
        --------------------------- END OF 업무마감 체크        
        
        
        UPDATE        
            EUSER.EACT_VOCH_BASE        
        SET        
            VOCH_STAT_CD       = '20'        
            , JST_STAT_CD      = V_JST_STAT_CD        
            , LAST_PROC_DT     = V_CURR_DATE        
            , LAST_PROC_TM     = V_CURR_TIME        
            , LAST_PROC_EMP_NO = I_EMP_NO        
        WHERE        
            VOCH_NO IN (SELECT    I_VOCH_NO    FROM DUAL        
        
                        UNION        
        
                        SELECT    B.VOCH_NO        
                        FROM    EUSER.EACT_VOCH_BASE C        
                                , EUSER.EACT_MNBR_VOCH B        
                        WHERE    C.TR_TP_DIV_CD = 'A0016'        
                                AND C.MNBR_VOCH_DIV_CD = '3'        
                                AND B.CNTR_VOCH_NO = I_VOCH_NO        
                                AND C.VOCH_NO = I_VOCH_NO        
        
                        UNION        
        
                        SELECT    B.CNTR_VOCH_NO        
                        FROM    EUSER.EACT_VOCH_BASE C        
                                , EUSER.EACT_MNBR_VOCH B        
                        WHERE    (C.TR_TP_DIV_CD <> 'A0016' OR C.MNBR_VOCH_DIV_CD <> '3')        
                                AND B.VOCH_NO = I_VOCH_NO        
                                AND C.VOCH_NO = I_VOCH_NO    )        
        ;        
        
        IF (SQLCODE < 0) THEN        
            ERR_MSG  := '전표상태코드 확정처리 중 문제가 발생하였습니다.';        
            CHK_ERR_FG := '1';        
            RAISE USR_DEF_ERR;        
        END IF;        
        
        IF V_PROF_FG IS NOT NULL AND V_PROF_FG = '1' THEN        
            UPDATE    EUSER.EACT_PROF_BASE        
            SET        PROF_STAT_CD = '20'        
            WHERE    VOCH_NO = I_VOCH_NO;        
        
            UPDATE    EUSER.EACT_EMP_EXPN_BASE        
            SET        PROF_STAT_CD = '20'        
            WHERE    VOCH_NO = I_VOCH_NO;        
        END IF;        
        
    END IF;        
        
    ----------------------------------------------------------        
    -- 거래유형 : 결산조정        
    IF V_TR_TP_CD LIKE 'A0099%' THEN        
        
        FOR CONFIRM3 IN CUR_CONFIRM3 LOOP        
        
          -- 총계정원장 기등록여부 체크        
            SELECT        
                CASE WHEN COUNT(*) = 0 THEN '0' ELSE '1' END     TACCT_LDGR_FG        
            INTO        
                V_TACCT_LDGR_FG        
            FROM        
                EUSER.EACT_TACCT_LDGR        
            WHERE        
                LOC_DIV_CD  = CONFIRM3.LOC_DIV_CD        
                AND ACCT_DT = CONFIRM3.ACCT_DT        
                AND DEPT_CD = CONFIRM3.DEPT_CD        
                AND ACCT_CD = CONFIRM3.ACCT_CD        
                AND CLAC_ADJU_FG = CONFIRM3.CLAC_ADJU_FG        
            ;        
        
        
            IF(CHK_ERR_FG = '0') THEN        
                IF(SQLCODE < 0) THEN        
                    ERR_MSG  := '총계정원장 기등록여부 체크 중 문제가 발생하였습니다.';        
                    CHK_ERR_FG := '1';        
                    RAISE USR_DEF_ERR;        
                END IF;        
            END IF;        
        
            IF (V_TACCT_LDGR_FG = '0') THEN        
                INSERT INTO EUSER.EACT_TACCT_LDGR (        
                    LOC_DIV_CD, ACCT_DT, DEPT_CD, ACCT_CD, CLAC_ADJU_FG, DR_AMT        
                    , CR_AMT, DR_CNT, CR_CNT, FRST_REG_DT, FRST_REG_TM, FRST_REG_EMP_NO        
                    , LAST_PROC_DT, LAST_PROC_TM, LAST_PROC_EMP_NO        
                ) VALUES (        
                    CONFIRM3.LOC_DIV_CD, CONFIRM3.ACCT_DT, CONFIRM3.DEPT_CD, CONFIRM3.ACCT_CD        
                    , CONFIRM3.CLAC_ADJU_FG, CONFIRM3.DR_AMT, CONFIRM3.CR_AMT, CONFIRM3.DR_CNT        
                    , CONFIRM3.CR_CNT, V_CURR_DATE, V_CURR_TIME, I_EMP_NO, V_CURR_DATE, V_CURR_TIME, I_EMP_NO        
                );        
        
                IF (CHK_ERR_FG = '0') THEN        
                    IF (SQLCODE < 0) THEN        
                        ERR_MSG    := 'AT_총계정원장 저장(INSERT) 중 문제가 발생하였습니다.';        
                        CHK_ERR_FG := '1';        
                        RAISE USR_DEF_ERR;        
                    END IF;        
                END IF;        
        
            ELSE        
                UPDATE        
                    EUSER.EACT_TACCT_LDGR        
                SET        
                      DR_AMT = DR_AMT + CONFIRM3.DR_AMT        
                    , CR_AMT = CR_AMT + CONFIRM3.CR_AMT        
                    , DR_CNT = DR_CNT + CONFIRM3.DR_CNT        
                    , CR_CNT = CR_CNT + CONFIRM3.CR_CNT        
                WHERE        
                    LOC_DIV_CD  = CONFIRM3.LOC_DIV_CD        
                    AND ACCT_DT = CONFIRM3.ACCT_DT        
                    AND DEPT_CD = CONFIRM3.DEPT_CD        
                    AND ACCT_CD = CONFIRM3.ACCT_CD        
                    AND CLAC_ADJU_FG = CONFIRM3.CLAC_ADJU_FG;        
        
                IF(CHK_ERR_FG = '0') THEN        
                    IF(SQLCODE < 0) THEN        
                        ERR_MSG    := 'AT_총계정원장 저장(UPDATE) 중 문제가 발생하였습니다.';        
                        CHK_ERR_FG := '1';        
                        RAISE USR_DEF_ERR;        
                    END IF;        
                END IF;        
        
            END IF;        
        
        END LOOP;        
        
    -- 취소전표가 아닌 전표확정이면        
    ELSIF (V_ORG_VOCH_NO IS NULL OR LENGTH(TRIM(V_ORG_VOCH_NO)) != 12) THEN        
        
    ----------------------------------------------------------        
    DBMS_OUTPUT.PUT_LINE('전표 확정');        
        IF (CHK_ERR_FG = '0') THEN        
        
            -- 3. AT_지급_계획 저장 - 미지급 발생전표(거래유형이 '정산'이 아니고 원전표번호 없는 대변의 미지급계정)        
            FOR CONFIRM1 IN CUR_CONFIRM1 LOOP        
        
                -- 지급요청수단코드 (50:지로/인출)        
                IF (CONFIRM1.MNG_NO = '50') THEN        
                    -- 지급요청부서(CONFIRM1.PAY_RQST_DEPT_CD)의 회계부서코드 가져오기        
                    SELECT    ACCT_DEPT_CD        
                    INTO    TMP_ACCT_CD        
                    FROM    DUSER.DHRT_ORG        
                    WHERE    DEPT_CD = CONFIRM1.PAY_RQST_DEPT_CD;        
 
                    -- 2010.08.31 지로/인출인 경우도 예금번호를 운영계좌에서 가져오도록 수정. 
                    -- 예금기본에서 용도구분(2:운영/수납)인것 예금번호 가져오기        
--                    SELECT    DEPO_NO        
--                    INTO    V_PAY_DEPO_NO        
--                    FROM    EUSER.EFDT_DEPO_BASE        
--                    WHERE    MNG_DEPT_CD = TMP_ACCT_CD        
--                            AND PRPS_DIV_CD = '2'        
--                            AND DEPO_STAT_CD = '10'        
--                            AND LOC_DIV_CD = CONFIRM1.LOC_DIV_CD;        
 
                    SELECT    CD_DESC_NO        
                    INTO    V_PAY_DEPO_NO        
                    FROM    GUSER.GBCT_COMM_CD_DESC        
                    WHERE    CD_KIND_NO = 'A00073';   
                     
                    V_PAY_RQST_METH_CD := '20'; 
        
                -- 제예금        
                ELSIF CONFIRM1.PAY_RQST_METH_CD = '20' THEN        
                    SELECT    CD_DESC_NO        
                    INTO    V_PAY_DEPO_NO        
                    FROM    GUSER.GBCT_COMM_CD_DESC        
                    WHERE    CD_KIND_NO = 'A00073';        
        
                    V_PAY_RQST_METH_CD := '20';        
        
                -- 미지급금-법인카드이면        
                ELSIF CONFIRM1.ACCT_CD = '210102' THEN        
        
                    SELECT    SETL_DEPO_NO       
                            , INDV_CORP_DIV_CD       
                    INTO    V_PAY_DEPO_NO       
                            , V_CARD_INCO_DCD       
                    FROM    EUSER.EACT_CARD_BASE        
                    WHERE    CARD_NO = CONFIRM1.MNG_NO        
                            AND SEQ = (    SELECT    MAX(SEQ)        
                                        FROM    EUSER.EACT_CARD_BASE        
                                        WHERE    CARD_NO = CONFIRM1.MNG_NO);        
                           
                    IF V_CARD_INCO_DCD = '2' THEN    -- 개인카드인경우 제예금       
                        V_PAY_RQST_METH_CD := '20';       
                    ELSE       
                        V_PAY_RQST_METH_CD := '40';       
                    END IF;       
        
                ELSE        
                    V_PAY_RQST_METH_CD := CONFIRM1.PAY_RQST_METH_CD;        
        
                END IF;        
        
                -- 지급번호 채번        
                GUSER.GP_GVNO_01('PAY', REPLACE(I_ACCT_DT, '-', ''), '', '', '', '', '', I_EMP_NO, V_PAY_NO, ERR_MSG);        
        
                IF(CHK_ERR_FG = '0') THEN        
                    IF(SQLCODE < 0 OR LENGTH(TRIM(V_PAY_NO)) = 0) THEN        
                        ERR_MSG  := '지급번호 채번 중 문제가 발생하였습니다.';        
                        CHK_ERR_FG := '1';        
                        RAISE USR_DEF_ERR;        
                    END IF;        
                END IF;      
        
                INSERT INTO EUSER.EACT_PAY_PLAN (        
                    PAY_NO, CAUS_VOCH_NO, CAUS_VOCH_SNUM, CAUS_ACCT_CD, CAUS_CUR_DIV_CD        
                    , CAUS_EXRT, CAUS_FCUR_AMT, CAUS_WCUR_AMT, CAUS_RMK, PAY_OBJ_DIV_CD      
                    , PAYPLC_CD, PAY_RQST_DEPT_CD, PAY_RQST_DT, PAY_RQST_METH_CD      
                    , PAY_STAT_CD, GIRO_FG, PAY_PROC_METH_CD, PAY_DEPO_NO, PAY_PROC_FG      
                    , PAY_DT, FRST_REG_DT, FRST_REG_TM, FRST_REG_EMP_NO, LAST_PROC_DT, LAST_PROC_TM, LAST_PROC_EMP_NO        
                ) VALUES (        
                    V_PAY_NO, CONFIRM1.VOCH_NO, CONFIRM1.VOCH_SNUM, CONFIRM1.ACCT_CD, CONFIRM1.CUR_DIV_CD        
                    , CONFIRM1.EXRT, CONFIRM1.FCUR_AMT, CONFIRM1.WCUR_AMT, CONFIRM1.RMK, CONFIRM1.PAY_OBJ_DIV_CD      
                    , CONFIRM1.PAYPLC_CD, CONFIRM1.PAY_RQST_DEPT_CD, CONFIRM1.PAY_RQST_DT, V_PAY_RQST_METH_CD        
                    , CONFIRM1.PAY_STAT_CD, CONFIRM1.GIRO_FG, V_PAY_RQST_METH_CD, V_PAY_DEPO_NO, CONFIRM1.PAY_PROC_FG        
                    , CONFIRM1.PAY_RQST_DT, V_CURR_DATE, V_CURR_TIME, I_EMP_NO, V_CURR_DATE, V_CURR_TIME, I_EMP_NO        
                );        
        
                IF(CHK_ERR_FG = '0') THEN        
                    IF(SQLCODE < 0) THEN        
                        ERR_MSG  := '지급계획 저장 중 문제가 발생하였습니다. '        
                            || '[전표번호:' || CONFIRM1.VOCH_NO || ', 전표일련번호:' || TO_CHAR(CONFIRM1.VOCH_SNUM) || ']';        
                        CHK_ERR_FG := '1';        
                        RAISE USR_DEF_ERR;        
                    END IF;        
                END IF;        
        
            END LOOP;        
        
        END IF;        
        
        IF(CHK_ERR_FG = '0') THEN        
            -- 6~8. 총계정, 거래처, 예금원장 저장        
        
            FOR CONFIRM2 IN CUR_CONFIRM2 LOOP        
        
                -- 총계정원장 기등록여부 체크        
                SELECT        
                    CASE WHEN COUNT(*) = 0 THEN '0' ELSE '1' END    TACCT_LDGR_FG        
                INTO        
                    V_TACCT_LDGR_FG        
                FROM        
                    EUSER.EACT_TACCT_LDGR        
                WHERE        
                    LOC_DIV_CD  = CONFIRM2.LOC_DIV_CD        
                    AND ACCT_DT = CONFIRM2.ACCT_DT        
                    AND DEPT_CD = CONFIRM2.DEPT_CD        
                    AND ACCT_CD = CONFIRM2.ACCT_CD        
                    AND CLAC_ADJU_FG = CONFIRM2.CLAC_ADJU_FG;        
        
                IF(CHK_ERR_FG = '0') THEN        
                    IF(SQLCODE < 0) THEN        
                        ERR_MSG  := '총계정원장 기등록여부 체크 중 문제가 발생하였습니다.';        
                        CHK_ERR_FG := '1';        
                        RAISE USR_DEF_ERR;        
                    END IF;        
                END IF;        
        
                -- 6. AT_총계정원장 저장(INSERT 또는 UPDATE)        
                IF(V_TACCT_LDGR_FG = '0') THEN        
                    INSERT INTO EUSER.EACT_TACCT_LDGR (        
                        LOC_DIV_CD, ACCT_DT, DEPT_CD, ACCT_CD, CLAC_ADJU_FG, DR_AMT, CR_AMT        
                        , DR_CNT, CR_CNT, FRST_REG_DT, FRST_REG_TM, FRST_REG_EMP_NO, LAST_PROC_DT        
                        , LAST_PROC_TM, LAST_PROC_EMP_NO        
                    ) VALUES (        
                        CONFIRM2.LOC_DIV_CD, CONFIRM2.ACCT_DT, CONFIRM2.DEPT_CD, CONFIRM2.ACCT_CD        
                        , CONFIRM2.CLAC_ADJU_FG, CONFIRM2.DR_AMT, CONFIRM2.CR_AMT, CONFIRM2.DR_CNT        
                        , CONFIRM2.CR_CNT, V_CURR_DATE, V_CURR_TIME, I_EMP_NO, V_CURR_DATE, V_CURR_TIME, I_EMP_NO        
                    );        
        
                    IF(CHK_ERR_FG = '0') THEN        
                        IF(SQLCODE < 0) THEN        
                            ERR_MSG  := 'AT_총계정원장 저장(INSERT) 중 문제가 발생하였습니다.';        
                            CHK_ERR_FG := '1';        
                            RAISE USR_DEF_ERR;        
                        END IF;        
                    END IF;        
        
                ELSE        
                    UPDATE        
                        EUSER.EACT_TACCT_LDGR        
                    SET        
                        DR_AMT = DR_AMT + CONFIRM2.DR_AMT        
                        , CR_AMT = CR_AMT + CONFIRM2.CR_AMT        
                        , DR_CNT = DR_CNT + CONFIRM2.DR_CNT        
                        , CR_CNT = CR_CNT + CONFIRM2.CR_CNT        
                    WHERE        
                        LOC_DIV_CD  = CONFIRM2.LOC_DIV_CD        
                        AND ACCT_DT = CONFIRM2.ACCT_DT        
                        AND DEPT_CD = CONFIRM2.DEPT_CD        
                        AND ACCT_CD = CONFIRM2.ACCT_CD        
                        AND CLAC_ADJU_FG = CONFIRM2.CLAC_ADJU_FG;        
        
                    IF(CHK_ERR_FG = '0') THEN        
                        IF(SQLCODE < 0) THEN        
                            ERR_MSG  := 'AT_총계정원장 저장(UPDATE) 중 문제가 발생하였습니다.';        
                            CHK_ERR_FG := '1';        
                            RAISE USR_DEF_ERR;        
                        END IF;        
                    END IF;        
        
                END IF;        
        
                -- 7. AT_거래처원장 저장 대상        
                IF LENGTH(TRIM(CONFIRM2.BUSI_CD)) > 0 THEN        
        
                    -- 거래처원장 기등록여부 체크        
                    SELECT        
                        CASE WHEN COUNT(*) = 0 THEN '0' ELSE '1' END    AS BUSI_LDGR_FG        
                    INTO        
                        V_BUSI_LDGR_FG        
                    FROM        
                        EUSER.EACT_BUSI_LDGR        
                    WHERE        
                        LOC_DIV_CD = CONFIRM2.LOC_DIV_CD        
                        AND ACCT_DT = CONFIRM2.ACCT_DT        
                        AND DEPT_CD = CONFIRM2.DEPT_CD        
                        AND ACCT_CD = CONFIRM2.ACCT_CD        
                        AND BUSI_CD = CONFIRM2.BUSI_CD        
                        AND CLAC_ADJU_FG = CONFIRM2.CLAC_ADJU_FG;        
        
                    IF(CHK_ERR_FG = '0') THEN        
                        IF(SQLCODE < 0) THEN        
                            ERR_MSG  := '거래처원장 기등록여부 체크 중 문제가 발생하였습니다.';        
                            CHK_ERR_FG := '1';        
                            RAISE USR_DEF_ERR;        
                        END IF;        
                    END IF;        
        
                    -- AT_거래처원장 저장(INSERT 또는 UPDATE)        
                    IF(V_BUSI_LDGR_FG = '0') THEN        
                        INSERT INTO EUSER.EACT_BUSI_LDGR (        
                            LOC_DIV_CD, ACCT_DT, DEPT_CD, ACCT_CD, BUSI_CD, CLAC_ADJU_FG, DR_AMT, CR_AMT        
                            , FRST_REG_DT, FRST_REG_TM, FRST_REG_EMP_NO, LAST_PROC_DT, LAST_PROC_TM, LAST_PROC_EMP_NO        
                        ) VALUES (        
                            CONFIRM2.LOC_DIV_CD, CONFIRM2.ACCT_DT, CONFIRM2.DEPT_CD, CONFIRM2.ACCT_CD        
                            , CONFIRM2.BUSI_CD, CONFIRM2.CLAC_ADJU_FG, CONFIRM2.DR_AMT, CONFIRM2.CR_AMT        
                            , V_CURR_DATE, V_CURR_TIME, I_EMP_NO, V_CURR_DATE, V_CURR_TIME, I_EMP_NO        
                        );        
        
                        IF(CHK_ERR_FG = '0') THEN        
                            IF(SQLCODE < 0) THEN        
                                ERR_MSG  := 'AT_거래처원장 저장(INSERT) 중 문제가 발생하였습니다.';        
                                CHK_ERR_FG := '1';        
                                RAISE USR_DEF_ERR;        
                            END IF;        
                        END IF;        
        
                    ELSE        
                        UPDATE        
                            EUSER.EACT_BUSI_LDGR        
                        SET        
                            DR_AMT = DR_AMT + CONFIRM2.DR_AMT        
                            , CR_AMT = CR_AMT + CONFIRM2.CR_AMT        
                        WHERE        
                            LOC_DIV_CD = CONFIRM2.LOC_DIV_CD        
                            AND ACCT_DT = CONFIRM2.ACCT_DT        
                            AND DEPT_CD = CONFIRM2.DEPT_CD        
                            AND ACCT_CD = CONFIRM2.ACCT_CD        
                            AND BUSI_CD = CONFIRM2.BUSI_CD        
                            AND CLAC_ADJU_FG = CONFIRM2.CLAC_ADJU_FG;        
        
                        IF(CHK_ERR_FG = '0') THEN        
                            IF(SQLCODE < 0) THEN        
                                ERR_MSG  := 'AT_거래처원장 저장(UPDATE) 중 문제가 발생하였습니다.';        
                                CHK_ERR_FG := '1';        
                                RAISE USR_DEF_ERR;        
                            END IF;        
                        END IF;        
        
                    END IF;        
        
                END IF;        
        
                -- 8. AT_예금원장 저장 대상        
                IF LENGTH(TRIM(CONFIRM2.DEPO_NO)) > 0 THEN        
        
                    -- 예금원장 기등록여부 체크        
                    SELECT        
                        CASE WHEN COUNT(*) = 0 THEN '0' ELSE '1' END DEPO_LDGR_FG        
                    INTO        
                        V_DEPO_LDGR_FG        
                    FROM        
                        EUSER.EFDT_DEPO_LDGR        
                    WHERE        
                        LOC_DIV_CD = CONFIRM2.LOC_DIV_CD        
                        AND ACCT_DT = CONFIRM2.ACCT_DT        
                        AND DEPT_CD = CONFIRM2.DEPT_CD        
                        AND DEPO_NO = CONFIRM2.DEPO_NO        
                        AND CLAC_ADJU_FG = CONFIRM2.CLAC_ADJU_FG;        
        
                    IF(CHK_ERR_FG = '0') THEN        
                        IF(SQLCODE < 0) THEN        
                            ERR_MSG  := '예금원장 기등록여부 체크 중 문제가 발생하였습니다.';        
                            CHK_ERR_FG := '1';        
                            RAISE USR_DEF_ERR;        
                        END IF;        
                    END IF;        
        
                    -- AT_예금원장 저장(INSERT 또는 UPDATE)        
                    IF(V_DEPO_LDGR_FG = '0') THEN        
        
                        INSERT INTO EUSER.EFDT_DEPO_LDGR (        
                            LOC_DIV_CD, ACCT_DT, DEPT_CD, DEPO_NO, CLAC_ADJU_FG, DR_AMT, CR_AMT        
                            , FRST_REG_DT, FRST_REG_TM, FRST_REG_EMP_NO        
                            , LAST_PROC_DT, LAST_PROC_TM, LAST_PROC_EMP_NO        
                        ) VALUES (        
                            CONFIRM2.LOC_DIV_CD, CONFIRM2.ACCT_DT, CONFIRM2.DEPT_CD, CONFIRM2.DEPO_NO        
                            , CONFIRM2.CLAC_ADJU_FG, CONFIRM2.DR_AMT, CONFIRM2.CR_AMT, V_CURR_DATE        
                            , V_CURR_TIME, I_EMP_NO, V_CURR_DATE, V_CURR_TIME, I_EMP_NO        
                        );        
        
                        IF(CHK_ERR_FG = '0') THEN        
                            IF(SQLCODE < 0) THEN        
                                ERR_MSG  := 'AT_예금원장 저장(INSERT) 중 문제가 발생하였습니다.';        
                                CHK_ERR_FG := '1';        
                                RAISE USR_DEF_ERR;        
                            END IF;        
                        END IF;        
        
                    ELSE        
                        UPDATE        
                            EUSER.EFDT_DEPO_LDGR        
                        SET        
                            DR_AMT = DR_AMT + CONFIRM2.DR_AMT        
                            , CR_AMT = CR_AMT + CONFIRM2.CR_AMT        
                        WHERE        
                            LOC_DIV_CD = CONFIRM2.LOC_DIV_CD        
                            AND ACCT_DT = CONFIRM2.ACCT_DT        
                            AND DEPT_CD = CONFIRM2.DEPT_CD        
                            AND DEPO_NO = CONFIRM2.DEPO_NO        
                            AND CLAC_ADJU_FG = CONFIRM2.CLAC_ADJU_FG        
                        ;        
        
                        IF(CHK_ERR_FG = '0') THEN        
                            IF(SQLCODE < 0) THEN        
                                ERR_MSG  := 'AT_예금원장 저장(UPDATE) 중 문제가 발생하였습니다.';        
                                CHK_ERR_FG := '1';        
                                RAISE USR_DEF_ERR;        
                            END IF;        
                        END IF;        
        
                    END IF;        
        
                END IF;        
        
            END LOOP;        
        
        END IF;        
        
    -- 취소전표 확정이면        
    ELSE        
        -- 원장 복구        
        EUSER.EP_EGAVO07_01(I_VOCH_NO, I_EMP_NO, ERR_MSG);        
        
        IF(SQLCODE < 0) THEN        
            CHK_ERR_FG := '1';        
            ERR_MSG  := '취소전표에 대한 원장 복구중 에러발생';        
            RAISE USR_DEF_ERR;        
        END IF;        
        
        -- 총계정원장 CUR_CONFIRM4        
        
        FOR CONFIRM4 IN CUR_CONFIRM4 LOOP        
        
            -- 총계정원장 기등록여부 체크        
            SELECT        
                CASE WHEN COUNT(*) = 0 THEN '0' ELSE '1' END TACCT_LDGR_FG        
            INTO        
                V_TACCT_LDGR_FG        
            FROM EUSER.EACT_TACCT_LDGR        
            WHERE        
                LOC_DIV_CD = CONFIRM4.LOC_DIV_CD        
                AND ACCT_DT = CONFIRM4.ACCT_DT        
                AND DEPT_CD = CONFIRM4.DEPT_CD        
                AND ACCT_CD = CONFIRM4.ACCT_CD        
                AND CLAC_ADJU_FG = CONFIRM4.CLAC_ADJU_FG;        
        
            IF(CHK_ERR_FG = '0') THEN        
                IF(SQLCODE < 0) THEN        
                    ERR_MSG  := '총계정원장 기등록여부 체크 중 문제가 발생하였습니다.';        
                    CHK_ERR_FG := '1';        
                    RAISE USR_DEF_ERR;        
                END IF;        
            END IF;        
        
            -- AT_총계정원장 저장(INSERT 또는 UPDATE)        
            IF(V_TACCT_LDGR_FG = '0') THEN        
                INSERT INTO EUSER.EACT_TACCT_LDGR (        
                    LOC_DIV_CD, ACCT_DT, DEPT_CD, ACCT_CD, CLAC_ADJU_FG, DR_AMT, CR_AMT, DR_CNT, CR_CNT        
                    , FRST_REG_DT, FRST_REG_TM, FRST_REG_EMP_NO, LAST_PROC_DT, LAST_PROC_TM, LAST_PROC_EMP_NO        
                )        
                VALUES (        
                    CONFIRM4.LOC_DIV_CD, CONFIRM4.ACCT_DT, CONFIRM4.DEPT_CD        
                    , CONFIRM4.ACCT_CD, CONFIRM4.CLAC_ADJU_FG, CONFIRM4.DR_AMT        
                    , CONFIRM4.CR_AMT, CONFIRM4.DR_CNT, CONFIRM4.CR_CNT, V_CURR_DATE        
                    , V_CURR_TIME, I_EMP_NO, V_CURR_DATE, V_CURR_TIME, I_EMP_NO        
                );        
        
                IF(CHK_ERR_FG = '0') THEN        
                    IF(SQLCODE < 0) THEN        
                        ERR_MSG  := 'AT_총계정원장 저장(INSERT) 중 문제가 발생하였습니다.';        
                        CHK_ERR_FG := '1';        
                        RAISE USR_DEF_ERR;        
                    END IF;        
                END IF;        
        
            ELSE        
                UPDATE        
                    EUSER.EACT_TACCT_LDGR        
                SET        
                    DR_AMT   = DR_AMT + CONFIRM4.DR_AMT        
                    , CR_AMT = CR_AMT + CONFIRM4.CR_AMT        
                    , DR_CNT = DR_CNT + CONFIRM4.DR_CNT        
                    , CR_CNT = CR_CNT + CONFIRM4.CR_CNT        
                WHERE        
                    LOC_DIV_CD = CONFIRM4.LOC_DIV_CD        
                    AND ACCT_DT = CONFIRM4.ACCT_DT        
                    AND DEPT_CD = CONFIRM4.DEPT_CD        
                    AND ACCT_CD = CONFIRM4.ACCT_CD        
                    AND CLAC_ADJU_FG = CONFIRM4.CLAC_ADJU_FG;--        
        
                IF ( CHK_ERR_FG = '0' ) THEN        
                    IF ( SQLCODE < 0 ) THEN        
                        ERR_MSG  := 'AT_총계정원장 저장(UPDATE) 중 문제가 발생하였습니다.';        
                        CHK_ERR_FG := '1' ;        
                        RAISE USR_DEF_ERR;        
                    END IF;        
                END IF;        
        
            END IF;        
        
            -- 예금원장복구        
            UPDATE        
                EUSER.EFDT_DEPO_LDGR        
            SET        
                DR_AMT = DR_AMT + CONFIRM4.DR_AMT        
                , CR_AMT = CR_AMT + CONFIRM4.CR_AMT        
            WHERE        
                LOC_DIV_CD = CONFIRM4.LOC_DIV_CD        
                AND ACCT_DT = CONFIRM4.ACCT_DT        
                AND DEPT_CD = CONFIRM4.DEPT_CD        
                AND DEPO_NO = CONFIRM4.DEPO_NO        
                AND CLAC_ADJU_FG = CONFIRM4.CLAC_ADJU_FG;        
        
            IF(CHK_ERR_FG = '0') THEN        
                IF(SQLCODE < 0) THEN        
                    ERR_MSG  := 'AT_예금원장 저장(UPDATE) 중 문제가 발생하였습니다.';        
                    CHK_ERR_FG := '1';        
                    RAISE USR_DEF_ERR;        
                END IF;        
            END IF;        
        
        END LOOP;        
        
    END IF;        
        
    IF(CHK_ERR_FG = '1') THEN        
        RAISE USR_DEF_ERR;        
    END IF;        
        
    ERR_MSG := 'OK';        
        
    EXCEPTION        
        WHEN USR_DEF_ERR THEN        
            O_ERR_MSG := ERR_MSG;        
            ROLLBACK;        
        
END;
/

GRANT EXECUTE ON EUSER.EP_EGAVO04_01 TO ZUSER;