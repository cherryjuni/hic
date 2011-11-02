/** Main system          : x.biz
  * Sub system           : 
  * Classname            : FTPInfoManage
  * Initial date         : 2005.11.15
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : FTP 송수신 정보를 디비에 저장하고 패킷을 전송하는
  * 					   테스트를 위한 프로그램이다.
  *                        실제 운영프로그램 작성시에 유용하게 참고할수 있다.
  */


package x.extr.util;

import java.sql.*;
import java.util.*;


import x.extr.db.*;
import x.extr.log.*;
import x.extr.net.*;

/** FTP 정보 관리 클래스
  * @author 황재천
*/
public class FTPInfoManage extends BatchLog {
	private Connection con = null;
	
	
	
	/**
	 * @ Constructor
	*/
	public FTPInfoManage() {
		this("");		
	}
	
	/**
	 * @ Constructor
	*/
	public FTPInfoManage(String dataSource) {
		super();
	}

	/** 배치작업의 시작정보를 스케쥴작업상태테이블에 저장하는 메소드
	  @param jobId   스케쥴작업ID
	  @param deptCd  수행작업코드
	  @param totCnt  예상총작업건수
	  @return 없슴
	*/
	public ArrayList getFTPInfo(String sendDt ) {
		Connection 	conn 	= null;
		Statement 	stmt 	= null;
		ResultSet 	rs 		= null;
		ArrayList   ar      = new ArrayList();
		
		BatchDBManager dbManager = new BatchDBManager();
		
		StringBuffer selectSql = new StringBuffer();
		selectSql.append("select * from fuser.fmgt_ftp_snr_info	\n");
		selectSql.append("where send_dt = '"+sendDt+"'			\n");

		try {			
			conn = dbManager.getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery(selectSql.toString());
			
			while( rs.next() ){
				String[] temp = new String[33];
				temp[0] = rs.getString(1);
				temp[1] = rs.getString(2);
				temp[2] = rs.getString(3);
				temp[3] = rs.getString(4);
				temp[4] = rs.getString(5);
				temp[5] = rs.getString(6);
				temp[6] = rs.getString(7);
				temp[7] = rs.getString(8);
				temp[8] = rs.getString(9);
				temp[9] = rs.getString(10);
				temp[10] = rs.getString(11);
				temp[12] = rs.getString(12);
				temp[13] = rs.getString(13);
				temp[14] = rs.getString(14);
				temp[15] = rs.getString(15);
				temp[16] = rs.getString(16);
				temp[17] = rs.getString(17);
				temp[18] = rs.getString(18);
				temp[19] = rs.getString(19);
				temp[20] = rs.getString(20);
				temp[21] = rs.getString(21);
				temp[22] = rs.getString(22);
				temp[23] = rs.getString(23);
				temp[24] = rs.getString(24);
				temp[25] = rs.getString(25);
				temp[26] = rs.getString(26);
				temp[27] = rs.getString(27);
				temp[28] = rs.getString(28);
				temp[29] = rs.getString(29);
				temp[30] = rs.getString(30);
				temp[31] = rs.getString(31);
				temp[32] = rs.getString(32);
				ar.add(temp);
			}
		} catch (Exception e) {
			return ar;
		} finally {
			try { 
				if(conn != null) conn.close(); 
			} catch(Exception e) {

			}
		}
		
		return ar;
	}
	
	public ArrayList getGramInfo(String sendTp,String extrOrgCd,String bsnDivCd ) {
		Connection 	conn 	= null;
		Statement 	stmt 	= null;
		ResultSet 	rs 		= null;
		ArrayList   ar      = new ArrayList();
		
		BatchDBManager dbManager = new BatchDBManager();
		
		StringBuffer selectSql = new StringBuffer();
		selectSql.append("select * from fuser.fmgt_gram_info	\n");
		selectSql.append("where send_tp    = '"+sendTp+"'		\n");
		selectSql.append("and   extrorg_cd = '"+extrOrgCd+"'	\n");
		selectSql.append("and   bsn_div_cd = '"+bsnDivCd+"'		\n");

		try {			
			conn = dbManager.getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery(selectSql.toString());
			
			while( rs.next() ){
				String[] temp = new String[12];
				temp[0] = rs.getString(1);
				temp[1] = rs.getString(2);
				temp[2] = rs.getString(3);
				temp[3] = rs.getString(4);
				temp[4] = rs.getString(5);
				temp[5] = rs.getString(6);
				temp[6] = rs.getString(7);
				temp[7] = rs.getString(8);
				temp[8] = rs.getString(9);
				temp[9] = rs.getString(10);
				temp[10] = rs.getString(11);
				temp[11] = rs.getString(12);
				ar.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ar;
		} finally {
			try { 
				if(conn != null) conn.close(); 
			} catch(Exception e) {

			}
		}
		
		return ar;
	}	
	
	public ArrayList getScheInfo() {
		Connection 	conn 	= null;
		Statement 	stmt 	= null;
		ResultSet 	rs 		= null;
		ArrayList   sj      = new ArrayList();
		
		BatchDBManager dbManager = new BatchDBManager();
		
		StringBuffer selectSql = new StringBuffer();
		selectSql.append("SELECT * FROM FUSER.FMGT_SCHD_JOB_STAT_INFO	\n");

		try {			
			conn = dbManager.getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery(selectSql.toString());
			
			while( rs.next() ){
				String[] temp = new String[14];
				temp[0] = rs.getString(1);
				temp[1] = rs.getString(2);
				temp[2] = rs.getString(3);
				temp[3] = rs.getString(4);
				temp[4] = rs.getString(5);
				temp[5] = rs.getString(6);
				temp[6] = rs.getString(7);
				temp[7] = rs.getString(8);
				temp[8] = rs.getString(9);
				temp[9] = rs.getString(10);
				temp[10] = rs.getString(11);
				temp[11] = rs.getString(12);
				temp[12] = rs.getString(13);
				temp[13] = rs.getString(14);
				sj.add(temp);
			}
		} catch (Exception e) {
			return sj;
		} finally {
			try { 
				if(conn != null) conn.close(); 
			} catch(Exception e) {

			}
		}
		
		return sj;
	}		
	
	/**
    @return boolean
    */
	public boolean saveFTPInfo(String extrOrgCd, String bsnDivCd, String stndDt, String snrFlag, String filePath, String fileNm, String procTermNo, String termRegMan, String termRegManPswd, int totRecCnt, int fileSize, String sUserId)
	{
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
		querySql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''),			\n");
		// 현재레코드번호(전송일자+대외기관코드 별로  할당)
		querySql.append("   (SELECT COALESCE(MAX(RECNO)+1,1) 								\n");
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
		querySql.append("    0, 0, 	                     			\n");
		// 시작일시초,종료일시초,소요시간
		querySql.append("    '', '', 0.00,                     		\n");
		
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
		catch (Exception e) 
		{
			System.out.println("1111111111111");
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

	/**
    @return boolean
    */
	public boolean saveGramInfo(String extrOrgCd, String bsnDivCd, String gramNm, int itemCnt, int recLth, String sUserId)
	{
		BatchDBManager 		dbManager = new BatchDBManager();		
		Connection 			conn 	  = null;
		PreparedStatement 	pstmt 	  = null;
		ResultSet           rs        = null;

		StringBuffer querySql = new StringBuffer();
		querySql.append("  SELECT * FROM FUSER.FMGT_GRAM_INFO  	 \n");
		querySql.append("  WHERE SEND_TP    = 'B'				 \n");
		querySql.append("  AND   EXTRORG_CD = '"+extrOrgCd+"'	 \n");
		querySql.append("  AND 	 BSN_DIV_CD = '"+bsnDivCd+"'	 \n");
		
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("  INSERT INTO FUSER.FMGT_GRAM_INFO  	 \n");
		insertSql.append("   ( SEND_TP,            				 \n");   // 전송_타입
		insertSql.append("     EXTRORG_CD,                       \n");   // 대외기관_코드
		insertSql.append("     BSN_DIV_CD,                       \n");   // 업무_구분코드
		insertSql.append("     GRAM_NM,                          \n");   // 전문_명
		insertSql.append("     ITEM_CNT,                       	 \n");   // 항목_개수
		insertSql.append("     REC_LTH,                        	 \n");   // 레코드_길이
		insertSql.append("     FRST_REG_DT,                      \n");   // 최초_등록_일자
		insertSql.append("     FRST_REG_TM,                      \n");   // 최초_등록_시각
		insertSql.append("     FRST_REG_EMP_NO,                  \n");   // 최초_등록_사원번호
		insertSql.append("     LAST_PROC_DT,                     \n");   // 최종_처리_일자
		insertSql.append("     LAST_PROC_TM,                     \n");   // 최종_처리_시각
		insertSql.append("     LAST_PROC_EMP_NO                  \n");   // 최종_처리_사원번호
		insertSql.append("   )VALUES(                            \n");
		// 전송타입,대외기관코드,업무구분코드
		insertSql.append("    'B', ?, ?,                     	\n");
		// 전문_명 ,항목_개수     ,레코드_길이 
		insertSql.append("    ?, ?, ?,                    		\n");
		// 최초_등록_일자,최초_등록_시각,최초_등록_사원번호
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?, \n");
		// 최종_처리_일자,최종_처리_시각,최종_처리_사원번호
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?  \n");
		insertSql.append("    )                    				\n");
		
		StringBuffer updateSql = new StringBuffer();
		updateSql.append("  UPDATE FUSER.FMGT_GRAM_INFO  	 	 						\n");
		updateSql.append("  SET GRAM_NM  = ?,                       					\n");   // 전문_명
		updateSql.append("      ITEM_CNT = ?,                       	 				\n");   // 항목_개수
		updateSql.append("      REC_LTH  = ?,                        	 				\n");   // 레코드_길이
		updateSql.append("      LAST_PROC_DT = REPLACE(VARCHAR(CURRENT DATE),'-',''),   \n");   // 최종_처리_일자
		updateSql.append("      LAST_PROC_TM = REPLACE(VARCHAR(CURRENT TIME),'.',''),   \n");   // 최종_처리_시각
		updateSql.append("      LAST_PROC_EMP_NO = ?                 					\n");   // 최종_처리_사원번호
		updateSql.append("  WHERE SEND_TP = 'B'   				 						\n");   // 전송_타입
		updateSql.append("  AND   EXTRORG_CD = '"+extrOrgCd+"'   						\n");   // 대외기관_코드
		updateSql.append("  AND   BSN_DIV_CD = '"+bsnDivCd+"'    						\n");   // 업무_구분코드
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO 테이블에 등록
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(querySql.toString());
			rs    = pstmt.executeQuery();
			if( rs.next() ) {
				pstmt = conn.prepareStatement(updateSql.toString());
				pstmt.setString(1,gramNm);
				pstmt.setInt(   2,itemCnt);
				pstmt.setInt(   3,recLth);
				pstmt.setString(4,sUserId);
				pstmt.executeUpdate();
			}else{
				pstmt = conn.prepareStatement(insertSql.toString());
				pstmt.setString(1,extrOrgCd);
				pstmt.setString(2,bsnDivCd);
				pstmt.setString(3,gramNm);
				pstmt.setInt(   4,itemCnt);
				pstmt.setInt(   5,recLth);
				pstmt.setString(6,sUserId);
				pstmt.setString(7,sUserId);
				pstmt.executeUpdate();
			}
			pstmt.close();
		} 
		catch (Exception e) 
		{
			System.out.println("222222222222222");
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
	
	/**
    @return boolean
    */
	public String saveScheInfo(String accoJobCd, int totProcExptCnt, String sUserId)
	{
		BatchDBManager 		dbManager = new BatchDBManager();		
		Connection 			conn 	  = null;
		PreparedStatement 	pstmt 	  = null;
		ResultSet           rs        = null;
		
		String 				schd_job_id = "";
		
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("  INSERT INTO FUSER.FMGT_SCHD_JOB_STAT_INFO  	\n");
		insertSql.append("   ( SCHD_JOB_ID,           			 \n");   // 스케쥴_작업_ID
		insertSql.append("     ACCO_JOB_CD,                      \n");   // 수행_작업_코드
		insertSql.append("     STRT_TM,                          \n");   // 시작_시간
		insertSql.append("     END_TM,                           \n");   // 종료_시간
		insertSql.append("     TOT_PROC_EXPT_CNT,              	 \n");   // 총_처리_예상_건수
		insertSql.append("     CURR_MSG,                       	 \n");   // 현재_메시지
		insertSql.append("     CURR_STAT,                      	 \n");   // 현재_상황
		insertSql.append("     CURR_PROC_CNT,                  	 \n");   // 현재_처리_건수
		insertSql.append("     FRST_REG_DT,                      \n");   // 최초_등록_일자
		insertSql.append("     FRST_REG_TM,                      \n");   // 최초_등록_시각
		insertSql.append("     FRST_REG_EMP_NO,                  \n");   // 최초_등록_사원번호
		insertSql.append("     LAST_PROC_DT,                     \n");   // 최종_처리_일자
		insertSql.append("     LAST_PROC_TM,                     \n");   // 최종_처리_시각
		insertSql.append("     LAST_PROC_EMP_NO                  \n");   // 최종_처리_사원번호
		insertSql.append("   )VALUES(                            \n");
		// 스케쥴_작업_ID
		insertSql.append("    (SELECT 'SCHE'||guser.gf_lpad_01((INTEGER(MAX(SUBSTR(SCHD_JOB_ID,5,4))) + 1),4,'0') FROM FUSER.FMGT_SCHD_JOB_STAT_INFO ), \n");
		// 수행_작업_코드, 시작_시간, 종료_시간 
		insertSql.append("    ?, '', '',                    		\n");
		// 총_처리_예상_건수, 현재_메시지, 현재_상황 
		insertSql.append("    ?, '', '',0,                    		\n");		
		// 최초_등록_일자,최초_등록_시각,최초_등록_사원번호
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?, \n");
		// 최종_처리_일자,최종_처리_시각,최종_처리_사원번호
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?  \n");
		insertSql.append("    )                    				\n");
		
		StringBuffer selectSql = new StringBuffer();
		selectSql.append("SELECT 'SCHE'||MAX(SUBSTR(SCHD_JOB_ID,5,4)) FROM FUSER.FMGT_SCHD_JOB_STAT_INFO ");
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO 테이블에 등록
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(insertSql.toString());
			pstmt.setString(1,accoJobCd);
			pstmt.setInt(   2,totProcExptCnt);
			pstmt.setString(3,sUserId);
			pstmt.setString(4,sUserId);
			pstmt.executeUpdate();
			pstmt.close();pstmt = null;
			
			pstmt = conn.prepareStatement(selectSql.toString());
			rs = pstmt.executeQuery();
			if( rs.next() ){
				schd_job_id = rs.getString(1);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "NULL";
		} finally {
			try { 
				if(conn != null) conn.close(); 
			} catch(Exception e) {

			}
		}   

		return schd_job_id;
	}		
	
	/**(method) 파일전송실행
    @param String[] jobInfo - 전송정보
	SCHE_JOB_ID		
	SEND_DT			
	SEND_TP			
	EXTRORG_CD		
	BSN_DIV_CD		
	STND_DT			
	SNR_FLAG			
	FILE_PATH		
	FILE_NM			
	TOT_REC_CNT		
	PROC_TRMN_NO		
	TRMN_REG_MAN		
	TRMN_REG_MAN_PSWD
	RESERVED			
    
    @return boolean - boolean
    */
	public boolean transExec(String[] jobInfo) throws Exception
	{
		String scheJobId 	= setStrFillLength(jobInfo[0],  8,1); 	// 스케쥴아이디(아규먼트)
		String sendDt 		= setStrFillLength(jobInfo[1],  8,1);	// 전송일자(시스템일자)
		String extrOrgType	= setStrFillLength(jobInfo[2],  1,1);	// 전문형태(고정)
		String extrOrgCd	= setStrFillLength(jobInfo[3],  2,1);	// 대외기관코드(고정)
		String bsnDivCd	    = setStrFillLength(jobInfo[4],  8,1);	// 거래구분코드(DB)
		String stndDt		= setStrFillLength(jobInfo[5],  8,1);	// 기준일자(시스템일자)
		String snrFlag		= setStrFillLength(jobInfo[6],  1,1);	// 송수신여부(고정)
		String filePath	    = setStrFillLength(jobInfo[7], 50,1);	// 파일경로(DB)
		String fileName	    = setStrFillLength(jobInfo[8], 30,1);	// 파일명(DB)
		String totRecCnt	= setStrFillLength(jobInfo[9],  7,2);	// 데이터 총건수(DB)
		String procTrmnNo	= setStrFillLength(jobInfo[10], 8,1);	// 처리단말번호(DB)
		String trmnRegMan	= setStrFillLength(jobInfo[11], 8,1);	// 단말등록자(DB)
		String trmnRegManPw = setStrFillLength(jobInfo[12],16,1);	// 단말등록자암호(DB)
		String reserved	    = setStrFillLength(jobInfo[13],15,1);	// 예비정보(DB)

		try 
		{ 
			FTPFrame ff = new FTPFrame();
			ff.setDPSStyle(scheJobId,sendDt,extrOrgType,extrOrgCd,bsnDivCd,stndDt,snrFlag,filePath,fileName,totRecCnt,procTrmnNo,trmnRegMan,trmnRegManPw,reserved);
			TCPClient.getInstance().callPDS(ff.toString(),"FTPCLIENT");
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
    /**
     * Fill String.
     * @param String, Length
     */
    public String setStrFillLength(String str,int len, int flag) {
    	String tmp = str;
        int nCnt = tmp.length();

        for (int i = len; i > nCnt; i--) {
        	if( flag == 1)
        		tmp = tmp + " ";
        	else
        		tmp = " " + tmp;
        }
        
        return tmp;
    }	
}
