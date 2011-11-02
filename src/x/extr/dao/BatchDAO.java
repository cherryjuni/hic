/** Main system          : x.extr
  * Sub system           : dao
  * Classname            : BatchDAO.java
  * Initial date         : 2005.11.15
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : Batch프로세스를 위한 기본 클래스
  */

/** 변경
  * 1. DB connection을 was 를 통해 얻어오던 것을 분리함
  */

package x.extr.dao;

import java.sql.*;
import java.io.*;

import x.extr.db.*;
import x.extr.lib.*;
import x.extr.log.*;
import x.extr.util.*;

/** Batch프로세스를 위한 기본 Class입니다.
  * @author 황재천
*/
public class BatchDAO extends BatchLog {
	String sSystem = "dao";
	String sClassName = "BatchDAO";
	
	//Batch process flag
	public static final String BATCH_START   = "W";
	public static final String BATCH_SUCCESS = "D";
	public static final String BATCH_RUN     = "R";
	public static final String BATCH_ERROR   = "E";
	
	private Connection con = null;
	
	
	
	/**
	 * @ Constructor
	*/
	public BatchDAO() {
		this("");		
	}
	
	/**
	 * @ Constructor
	*/
	public BatchDAO(String dataSource) {
		super();
	}

	/** 배치작업의 시작정보를 스케쥴작업상태테이블에 저장하는 메소드
	  @param jobId   스케쥴작업ID
	  @param deptCd  수행작업코드
	  @param totCnt  예상총작업건수
	  @return 없슴
	*/
	public String startJob(String sendTp,String extrOrgCd,String bsnDivCd,String schdJobDiv,int totProcExptCnt ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String seq = "";

		System.out.println("----->"+sendTp+":"+sendTp.length());
		System.out.println("----->"+extrOrgCd+":"+extrOrgCd.length());
		System.out.println("----->"+bsnDivCd+":"+bsnDivCd.length());
		System.out.println("----->"+schdJobDiv+":"+schdJobDiv.length());

		BatchDBManager dbManager = new BatchDBManager();
		StringBuffer selectSql = new StringBuffer();
		selectSql.append("  SELECT 										");
		selectSql.append("		CHAR(                                  	");// 스케쥴_작업_순번
		selectSql.append("  		GUSER.GF_LPAD_01(                	");
		selectSql.append("     			MAX(INT(SCHD_JOB_SEQ))+1        ");
		selectSql.append("	 		,2,'0')                     		");
		selectSql.append("  	)                                		");
		selectSql.append("  	MAXSEQ                     				");
		selectSql.append("  FROM FUSER.FMGT_SCHD_JOB_STAT_INFO	 		");
		selectSql.append("  WHERE SCHD_JOB_DT  = REPLACE(VARCHAR(CURRENT DATE),'-','')	");
		selectSql.append("  AND   SEND_TP      = ? 	 					");
		selectSql.append("  AND   EXTRORG_CD   = ?	 					");
		selectSql.append("  AND   BSN_DIV_CD   = ?	 					");
		selectSql.append("  AND   SCHD_JOB_DIV = ?	 WITH UR			");

		StringBuffer insertSql = new StringBuffer();
		insertSql.append("INSERT INTO FUSER.FMGT_SCHD_JOB_STAT_INFO (	");
		insertSql.append("SCHD_JOB_DT,		");
		insertSql.append("SCHD_JOB_SEQ,		");
		insertSql.append("SEND_TP,			");
		insertSql.append("EXTRORG_CD,		");
		insertSql.append("BSN_DIV_CD,		");
		insertSql.append("SCHD_JOB_DIV,		");
		insertSql.append("ACCO_JOB_CD,		");
		insertSql.append("STRT_TM,			");
		insertSql.append("END_TM,			");
		insertSql.append("TOT_PROC_EXPT_CNT,");
		insertSql.append("CURR_MSG,			");
		insertSql.append("CURR_STAT,		");
		insertSql.append("CURR_PROC_CNT,	");
		insertSql.append("FRST_REG_DT,		");
		insertSql.append("FRST_REG_TM,		");
		insertSql.append("FRST_REG_EMP_NO,	");
		insertSql.append("LAST_PROC_DT,		");
		insertSql.append("LAST_PROC_TM,		");
		insertSql.append("LAST_PROC_EMP_NO	");
		insertSql.append(")VALUES (			");
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-',''),	");// 스케쥴_작업_일자
		insertSql.append(" ?, 				");// 스케쥴_작업_순번
		insertSql.append(" ?,				");// 전송_타입
		insertSql.append(" ?,				");// 대외기관_코드
		insertSql.append(" ?,				");// 업무_구분_코드
		insertSql.append(" ?,				");// 스케쥴_작업_구분(파일생성배치,전송배치)
		insertSql.append(" 'FTPS',			");// 수행작업코드
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-','')||REPLACE(VARCHAR(CURRENT TIME),'.',''),");// 시작_시간
		insertSql.append(" '',				");// 종료_시간
		insertSql.append(" ?,				");// 총_처리_예상_건수
		insertSql.append(" '',				");// 현재_메시지
		insertSql.append(" 'S',				");// 현재_상황
		insertSql.append(" 0,				");// 현재_처리_건수
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-',''),");	//최초_등록_일자
		insertSql.append(" REPLACE(VARCHAR(CURRENT TIME),'.',''),");	//최초_등록_시각
		insertSql.append(" 'SYSTEMOP',		");							//최초_등록_사원번호
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-',''),");	//최종_처리_일자
		insertSql.append(" REPLACE(VARCHAR(CURRENT TIME),'.',''),");	//최종_처리_시각
		insertSql.append(" 'SYSTEMOP')		");							//최종_처리_사원번호

		try {			
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(selectSql.toString()); 
			pstmt.setString(1, sendTp);
			pstmt.setString(2, extrOrgCd);
			pstmt.setString(3, bsnDivCd);
			pstmt.setString(4, schdJobDiv);
			rs = pstmt.executeQuery();

			if( rs.next() ) {
				seq = rs.getString("MAXSEQ");
				if( seq == null ) {
					seq = "01";
				}
			}else{
				seq = "01";
			}
			seq = seq.trim();

			pstmt.close();pstmt = null;
			pstmt = conn.prepareStatement(insertSql.toString()); 
			pstmt.setString(1, seq);
			pstmt.setString(2, sendTp);
			pstmt.setString(3, extrOrgCd);
			pstmt.setString(4, bsnDivCd);
			pstmt.setString(5, schdJobDiv);
			pstmt.setInt(6, totProcExptCnt);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			try { 
				if(conn != null) conn.close(); 
			} catch(Exception e) {
				System.out.println(e.toString()); 
				return "";
			}
		}
		
		return seq;
	}

	/** 배치작업의 진행정보를 스케쥴작업상태테이블에 저장하는 메소드 
	  @param jobId   스케쥴작업ID
	  @param deptCd  부서코드
	  @param curCnt  현재작업건수
	  @return ExResultSet
	*/
	public void workingJob(String sendTp,String extrOrgCd,String bsnDivCd,String schdJobDiv,String currMsg,int currProcCnt) {
		StringBuffer updateSql;
		Connection conn = null;
		PreparedStatement pstmt = null;
		BatchDBManager dbManager = new BatchDBManager();
		
		updateSql = new StringBuffer();
		updateSql.append("UPDATE FUSER.FMGT_SCHD_JOB_STAT_INFO 				");
		updateSql.append("SET	CURR_MSG      = ?,							");// 현재_메시지
		updateSql.append("		CURR_STAT     = 'W',						");// 현재_상황
		updateSql.append("		CURR_PROC_CNT = ?,							");// 현재_처리_건수
		updateSql.append("		LAST_PROC_DT  = REPLACE(VARCHAR(CURRENT DATE),'-',''),");//최종_처리_일자
		updateSql.append("		LAST_PROC_TM  = REPLACE(VARCHAR(CURRENT TIME),'.',''),");//최종_처리_시각
		updateSql.append("		LAST_PROC_EMP_NO  = 'SYSTEMOP'				");//최종_처리_사원번호
		updateSql.append("WHERE SEND_TP      = ?								");
		updateSql.append("AND   EXTRORG_CD   = ?								");
		updateSql.append("AND   BSN_DIV_CD   = ?								");
		updateSql.append("AND   SCHD_JOB_DIV = ?                                ");

		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(updateSql.toString());
			pstmt.setString(1, currMsg);
			pstmt.setInt(2, currProcCnt);
			pstmt.setString(3, sendTp);
			pstmt.setString(4, extrOrgCd);
			pstmt.setString(5, bsnDivCd);
			pstmt.setString(6, schdJobDiv);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close(); } catch(Exception e) {System.out.println(e.toString()); return;}
		}
	}

	/** 배치작업의 종료정보를 스케쥴작업상태테이블에 저장하는 메소드
	  @param jobId   스케쥴작업ID
	  @param deptCd  부서코드
	  @param state   종료상태
	  @param msg     종료상세메시지
	  @return void
	*/
	public void endJob(String sendTp,String extrOrgCd,String bsnDivCd,String schdJobDiv,String currStat,String currMsg) {
		StringBuffer updateSql;
		Connection conn = null;
		PreparedStatement pstmt = null;
		BatchDBManager dbManager = new BatchDBManager();

		updateSql = new StringBuffer();
		updateSql.append("UPDATE FUSER.FMGT_SCHD_JOB_STAT_INFO 					");
		updateSql.append("SET	END_TM = REPLACE(VARCHAR(CURRENT TIME),'.',''),	");// 종료_시간
		updateSql.append("		CURR_STAT = ?,									");// 현재_상황
		updateSql.append("		CURR_MSG  = ? 									");// 현재_메시지
		updateSql.append("WHERE SEND_TP      = ?								");
		updateSql.append("AND   EXTRORG_CD   = ?								");
		updateSql.append("AND   BSN_DIV_CD   = ?								");
		updateSql.append("AND   SCHD_JOB_DIV = ?                                ");
System.out.println("----->"+sendTp+":"+sendTp.length());
System.out.println("----->"+extrOrgCd+":"+extrOrgCd.length());
System.out.println("----->"+bsnDivCd+":"+bsnDivCd.length());
System.out.println("----->"+schdJobDiv+":"+schdJobDiv.length());
System.out.println("----->"+currStat+":"+currStat.length());
System.out.println("----->"+currMsg+":"+currMsg.length());
		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(updateSql.toString());
			pstmt.setString(1, currStat);
			pstmt.setString(2, currMsg);
			pstmt.setString(3, sendTp);
			pstmt.setString(4, extrOrgCd);
			pstmt.setString(5, bsnDivCd);
			pstmt.setString(6, schdJobDiv);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close(); } catch(Exception e) {return;}
		}
	}
	
	/** [전송] PDS FTP전송정보 등록 메소드
    @param String extrOrgCd   - 대외기관코드
    @param String bsnDivCd    - 업무구분코드
    @param String stndDt      - 기준일자
    @param String snrFlag     - 송수신플래그
    @param String filePath    - 파일패스
    @param String fileNm      - 파일명
    @param String procTermNo  - 처리단말번호
    @param String termRegMan  - 단말등록자
    @param String termRegManPswd  - 단말등록자암호
    @param int    totRecCnt   - 총레코드개수(총처리예상건수)
    @param int    fileSize    - 파일크기
    @param String sUserId     - 사용자아이디
    @return void              - boolean (성공/여부)
    */
	/* History
	 이 함수는 FTPInfoManage 에서 복사하여 작성했다.
	 원래 여기 있던 setPdsFtpSend 함수를 테스트는 안한 상태로 FTPInfoManage 에 복사하여  테스트 해보니 오류가 생겨 수정했고,
	 이를 다시 이 프로그램에 다시 복사하여 사용한다. 원본 setPdsFtpSend 함수는 아래에 있다.
    */  
	public boolean saveFTPInfo(String extrOrgCd, String bsnDivCd, String stndDt, String snrFlag, String filePath, String fileNm, String procTermNo, String termRegMan, String termRegManPswd, int totRecCnt, int fileSize, String sUserId)
	{
		BatchDBManager 		dbManager = new BatchDBManager();		
		Connection 			conn 	  = null;
		PreparedStatement 	pstmt 	  = null;

		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT COUNT(*) FROM FUSER.FMGT_FTP_SNR_INFO 			\n");
		countSql.append("WHERE SEND_DT = REPLACE(VARCHAR(CURRENT DATE),'-','') 	\n");
		countSql.append("  AND SEND_TP = 'B'									\n");
		countSql.append("  AND EXTRORG_CD = ? 									\n");
		countSql.append("  AND BSN_DIV_CD = ? 									\n");
		countSql.append("  AND STND_DT    = ? 									\n");
		countSql.append("  AND SNR_JOB_FL = ?									\n");
		countSql.append("WITH UR 												\n");
		
		System.out.println("일괄송수신 SQL:"+countSql.toString());
		System.out.println("EXTRORG_CD:"+extrOrgCd);
		System.out.println("BSN_DIV_CD:"+bsnDivCd);
		System.out.println("STND_DT:"+stndDt);
		
		StringBuffer querySql = new StringBuffer();
		querySql.append("  INSERT INTO FUSER.FMGT_FTP_SNR_INFO  \n");
		querySql.append("   ( SEND_DT,            				\n");   // 전송일자
		querySql.append("     RECNO,                            \n");   // 레코드번호
		querySql.append("     SEND_TP,                          \n");   // 전송_타입
		querySql.append("     EXTRORG_CD,                       \n");   // 대외기관_코드
		querySql.append("     BSN_DIV_CD,                       \n");   // 업무_구분코드
		querySql.append("     STND_DT,                          \n");   // 기준_일자
		querySql.append("     SNR_JOB_FL,                       \n");   // 송수신_작업_FLAG
		querySql.append("     FILE_PATH,                        \n");   // 파일_패스
		querySql.append("     FILE_NM,                          \n");   // 파일_명
		querySql.append("     PROC_TRMN_NO,                     \n");   // 처리_단말번호
		querySql.append("     TRMN_REG_MAN,                     \n");   // 단말_등록자
		querySql.append("     TRMN_REG_MAN_PSWD,                \n");   // 단말_등록자_암호
		querySql.append("     SEQPER_REC_CNT,                   \n");   // SEQ당_레코드_수
		querySql.append("     TOT_REC_CNT,                      \n");   // 총_레코드_개수
		querySql.append("     FILE_SIZE,                        \n");   // 파일_크기
//		querySql.append("     SCS_REC_CNT,                      \n");   // 성공_레코드_개수2006.03.07 삭제
//		querySql.append("     FAIL_REC_CNT,                     \n");   // 실패_레코드_개수2006.03.07 삭제
		querySql.append("     STRT_DTTM_SCND,                   \n");   // 시작_일시초
		querySql.append("     END_DTTM_SCND,                    \n");   // 종료_일시초
//		querySql.append("     NEED_TM,                          \n");   // 소요_시간 2006.03.07 삭제
		querySql.append("     RSPN_CD,                          \n");   // 응답_코드
		querySql.append("     JOB_STAT,                         \n");   // 작업_상태
		querySql.append("     HDCP_DETL_CD,                     \n");   // 장애_세부_코드
		querySql.append("     LAST_BLCK_NO,                     \n");   // 최종_블록_No
		querySql.append("     LAST_SNUM,                        \n");   // 최종_일련번호
		querySql.append("     LAST_REC_NO,                      \n");   // 최종_레코드_번호
		querySql.append("     TRLR_INFO,                        \n");   // 트레일러_정보
		querySql.append("     FRST_REG_DT,                      \n");   // 최초_등록_일자
		querySql.append("     FRST_REG_TM,                      \n");   // 최초_등록_시각
		querySql.append("     FRST_REG_EMP_NO,                  \n");   // 최초_등록_사원번호
		querySql.append("     LAST_PROC_DT,                     \n");   // 최종_처리_일자
		querySql.append("     LAST_PROC_TM,                     \n");   // 최종_처리_시각
		querySql.append("     LAST_PROC_EMP_NO                  \n");   // 최종_처리_사원번호
		querySql.append("   )VALUES(                            \n");
		// 전송일자(시스템일자 자동할당)
		querySql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''),						\n");
		// 현재레코드번호(전송일자+대외기관코드 별로  할당)
		querySql.append("   (SELECT COALESCE(MAX(RECNO)+1,1) 							\n");
		querySql.append("	 FROM FUSER.FMGT_FTP_SNR_INFO								\n");
		querySql.append("    WHERE SEND_DT = REPLACE(VARCHAR(CURRENT DATE),'-','')		\n");
		querySql.append("    AND   EXTRORG_CD = ?),										\n");
		// 전송타입,대외기관코드,업무구분코드
		querySql.append("    'B' , ?, ?,                     		\n");
		// 기준일자,송수신플래그,파일패스,파일명 
		querySql.append("    ?, ?, ?, ?,                    		\n");
		// 처리단말번호,단말등록자,단말등록자암호
		querySql.append("    ?, ?, ?,                      			\n");
		// SEQ당_레코드_수,총레코드개수,파일크기
		querySql.append("    0, ?, ?,                     			\n");
		// 성공레코드개수,실패레코드개수
		//querySql.append("    0, 0, 	                     			\n");
		// 시작일시초,종료일시초,소요시간
		querySql.append("    '', '', 	                     		\n");
		// 응답코드,작업상태,장애세부코드
		querySql.append("    '', '', '',                     		\n");
		// 최종블럭NO,최종일련번호,최종레코드번호
		querySql.append("    0, 0, 0,                     			\n");
		// 트레일러_정보,최초_등록_일자,최초_등록_시각,최초_등록_사원번호
		querySql.append("    '', REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?, \n");
		// 최종_처리_일자,최종_처리_시각,최종_처리_사원번호
		querySql.append("    '', '', '' )                          	\n");
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO 테이블에 등록
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(countSql.toString());
			pstmt.setString(1,extrOrgCd);
			pstmt.setString(2,bsnDivCd);
			pstmt.setString(3,stndDt);
			pstmt.setString(4,snrFlag);
			ResultSet rs = pstmt.executeQuery();
			int rCnt = 0;
			if( rs.next() ) {
				rCnt = rs.getInt(1);
			}
			pstmt.close();
			pstmt = null;
			
			if( rCnt == 0 ) {
				pstmt = conn.prepareStatement(querySql.toString());
				pstmt.setString(1,extrOrgCd);
				pstmt.setString(2,extrOrgCd);
				pstmt.setString(3,bsnDivCd);
				pstmt.setString(4,stndDt);
				pstmt.setString(5,snrFlag);
				pstmt.setString(6,filePath);
				pstmt.setString(7,fileNm);
				pstmt.setString(8,procTermNo);
				pstmt.setString(9,termRegMan);
				pstmt.setString(10,termRegManPswd);
				pstmt.setInt   (11,totRecCnt);
				pstmt.setInt   (12,fileSize);
				pstmt.setString(13,sUserId);
				pstmt.executeUpdate();
				pstmt.close();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		} finally {
			try { 
				if(conn != null) conn.close(); 
			} catch(Exception e) {

			}
		}  

		return true;
	}
	
	/** [전송] PDS FTP전송정보set 메소드
    @param String extrOrgCd   - 대외기관코드
    @param String bsnDivCd    - 업무구분코드
    @param String stndDt      - 기준일자
    @param String filePath    - 파일패스
    @param String fileNm      - 파일명
    @param String procTermNo  - 처리단말번호
    @param String termRegMan  - 단말등록자
    @param String termRegManPswd  - 단말등록자암호
    @param int    totRecCnt   - 총레코드개수
    @param int    fileSize    - 파일크기
    @param String sUserId     - 사용자아이디
    @return void              - void
    */	
	/* History
	 * 이 함수는 원래 BatchDAO 에 있던거.
	 * 
	public void setPdsFtpSend(String extrOrgCd, String bsnDivCd, String stndDt, String filePath, String fileNm, String procTermNo, String termRegMan, String termRegManPswd, int totRecCnt, int fileSize, String sUserId) throws Exception
	{
		System.out.println("(method)setPdsFtpSend() is started.");
		BatchDBManager 		dbManager = new BatchDBManager();		
		Connection 			conn 	  = null;
		PreparedStatement 	pstmt 	  = null;

		StringBuffer querySql = new StringBuffer();
		querySql.append("  INSERT INTO FUSER.FMGT_FTP_SNR_INFO  \n");
		querySql.append("   ( SEND_DT,            				\n");   // 전송일자
		querySql.append("     RECNO,                            \n");   // 레코드번호
		querySql.append("     SEND_TP,                          \n");   // 전송_타입
		querySql.append("     EXTRORG_CD,                       \n");   // 대외기관_코드
		querySql.append("     BSN_DIV_CD,                       \n");   // 업무_구분코드
		querySql.append("     STND_DT,                          \n");   // 기준_일자
		querySql.append("     SNR_JOB_FL,                       \n");   // 송수신_작업_FLAG
		querySql.append("     FILE_PATH,                        \n");   // 파일_패스
		querySql.append("     FILE_NM,                          \n");   // 파일_명
		querySql.append("     PROC_TRMN_NO,                     \n");   // 처리_단말번호
		querySql.append("     TRMN_REG_MAN,                     \n");   // 단말_등록자
		querySql.append("     TRMN_REG_MAN_PSWD,                \n");   // 단말_등록자_암호
		querySql.append("     SEQPER_REC_CNT,                   \n");   // SEQ당_레코드_수
		querySql.append("     TOT_REC_CNT,                      \n");   // 총_레코드_개수
		querySql.append("     FILE_SIZE,                        \n");   // 파일_크기
		querySql.append("     SCS_REC_CNT,                      \n");   // 성공_레코드_개수
		querySql.append("     FAIL_REC_CNT,                     \n");   // 실패_레코드_개수
		querySql.append("     STRT_DTTM_SCND,                   \n");   // 시작_일시초
		querySql.append("     END_DTTM_SCND,                    \n");   // 종료_일시초
		querySql.append("     NEED_TM,                          \n");   // 소요_시간
		querySql.append("     RSPN_CD,                          \n");   // 응답_코드
		querySql.append("     JOB_STAT,                         \n");   // 작업_상태
		querySql.append("     HDCP_DETL_CD,                     \n");   // 장애_세부_코드
		querySql.append("     LAST_BLCK_NO,                     \n");   // 최종_블록_No
		querySql.append("     LAST_SNUM,                        \n");   // 최종_일련번호
		querySql.append("     LAST_REC_NO,                      \n");   // 최종_레코드_번호
		querySql.append("     TRLR_INFO,                        \n");   // 트레일러_정보
		querySql.append("     FRST_REG_DT,                      \n");   // 최초_등록_일자
		querySql.append("     FRST_REG_TM,                      \n");   // 최초_등록_시각
		querySql.append("     FRST_REG_EMP_NO,                  \n");   // 최초_등록_사원번호
		querySql.append("     LAST_PROC_DT,                     \n");   // 최종_처리_일자
		querySql.append("     LAST_PROC_TM,                     \n");   // 최종_처리_시각
		querySql.append("     LAST_PROC_EMP_NO                  \n");   // 최종_처리_사원번호
		querySql.append("   )VALUES(                            \n");
		// 전송일자(시스템일자 자동할당)
		querySql.append("    VALUES (REPLACE(VARCHAR(CURRENT DATE),'-','')),			\n");
		// 현재레코드번호(전송일자+대외기관코드 별로  할당)
		querySql.append("   (SELECT COALESCE(MAX(RECNO),1) 								\n");
		querySql.append("	 FROM FUSER.FMGT_FTP_SNR_INFO								\n");
		querySql.append("    WHERE SEND_DT = REPLACE(VARCHAR(CURRENT DATE),'-','')		\n");
		querySql.append("    AND   EXTRORG_CD = ?),										\n");
		// 전송타입,대외기관코드,업무구분코드
		querySql.append("    'B' , ?, ?,                     		\n");
		// 기준일자,송수신플래그,파일패스,파일명 
		querySql.append("    ?, 'S', ?, ?,                    		\n");
		// 처리단말번호,단말등록자,단말등록자암호
		querySql.append("    ?, ?, ?,                      			\n");
		// SEQ당_레코드_수,총레코드개수,파일크기
		querySql.append("    0, ?, ?,                     			\n");
		// 성공레코드개수,실패레코드개수
		querySql.append("    0, 0, 	                     			\n");
		// 시작일시초,종료일시초,소요시간
		querySql.append("    '', '', '',                     		\n");
		
		// 응답코드,작업상태,장애세부코드
		querySql.append("    '', '', '',                     		\n");
		
		// 최종블럭NO,최종일련번호,최종레코드번호
		querySql.append("    0, 0, 0,                     			\n");
		
		// 트레일러_정보,최초_등록_일자,최초_등록_시각,최초_등록_사원번호
		querySql.append("    '', VALUES (REPLACE(VARCHAR(CURRENT DATE),'-','')), VALUES (REPLACE(VARCHAR(CURRENT TIME),'-','')), ?, \n");
		// 최종_처리_일자,최종_처리_시각,최종_처리_사원번호
		querySql.append("    '', '', '' )                          	\n");
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO 테이블에 등록
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(querySql.toString());
			pstmt.setString(1,extrOrgCd);
			pstmt.setString(2,extrOrgCd);
			pstmt.setString(3,bsnDivCd);
			pstmt.setString(4,stndDt);
			pstmt.setString(5,filePath);
			pstmt.setString(6,fileNm);
			pstmt.setString(7,procTermNo);
			pstmt.setString(8,termRegMan);
			pstmt.setString(9,termRegManPswd);
			pstmt.setInt   (10,totRecCnt);
			pstmt.setInt   (11,fileSize);
			pstmt.setString(12,sUserId);
			pstmt.executeUpdate();
			pstmt.close(); 
		} 
		catch (Exception e) 
		{
			writeLog(sSystem, sClassName, "setPdsFtpSend()", Log.LOG_CRITICAL, e.toString());
			throw e;
		}   
		System.out.println("(method)setPdsFtpSend() is ended.");
		return;
	}
	*/

}
