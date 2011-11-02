/** Main system          : x.extr
  * Sub system           : dao
  * Classname            : BatchDAO.java
  * Initial date         : 2005.11.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : Batch���μ����� ���� �⺻ Ŭ����
  */

/** ����
  * 1. DB connection�� was �� ���� ������ ���� �и���
  */

package x.extr.dao;

import java.sql.*;
import java.io.*;

import x.extr.db.*;
import x.extr.lib.*;
import x.extr.log.*;
import x.extr.util.*;

/** Batch���μ����� ���� �⺻ Class�Դϴ�.
  * @author Ȳ��õ
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

	/** ��ġ�۾��� ���������� �������۾��������̺� �����ϴ� �޼ҵ�
	  @param jobId   �������۾�ID
	  @param deptCd  �����۾��ڵ�
	  @param totCnt  �������۾��Ǽ�
	  @return ����
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
		selectSql.append("		CHAR(                                  	");// ������_�۾�_����
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
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-',''),	");// ������_�۾�_����
		insertSql.append(" ?, 				");// ������_�۾�_����
		insertSql.append(" ?,				");// ����_Ÿ��
		insertSql.append(" ?,				");// ��ܱ��_�ڵ�
		insertSql.append(" ?,				");// ����_����_�ڵ�
		insertSql.append(" ?,				");// ������_�۾�_����(���ϻ�����ġ,���۹�ġ)
		insertSql.append(" 'FTPS',			");// �����۾��ڵ�
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-','')||REPLACE(VARCHAR(CURRENT TIME),'.',''),");// ����_�ð�
		insertSql.append(" '',				");// ����_�ð�
		insertSql.append(" ?,				");// ��_ó��_����_�Ǽ�
		insertSql.append(" '',				");// ����_�޽���
		insertSql.append(" 'S',				");// ����_��Ȳ
		insertSql.append(" 0,				");// ����_ó��_�Ǽ�
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-',''),");	//����_���_����
		insertSql.append(" REPLACE(VARCHAR(CURRENT TIME),'.',''),");	//����_���_�ð�
		insertSql.append(" 'SYSTEMOP',		");							//����_���_�����ȣ
		insertSql.append(" REPLACE(VARCHAR(CURRENT DATE),'-',''),");	//����_ó��_����
		insertSql.append(" REPLACE(VARCHAR(CURRENT TIME),'.',''),");	//����_ó��_�ð�
		insertSql.append(" 'SYSTEMOP')		");							//����_ó��_�����ȣ

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

	/** ��ġ�۾��� ���������� �������۾��������̺� �����ϴ� �޼ҵ� 
	  @param jobId   �������۾�ID
	  @param deptCd  �μ��ڵ�
	  @param curCnt  �����۾��Ǽ�
	  @return ExResultSet
	*/
	public void workingJob(String sendTp,String extrOrgCd,String bsnDivCd,String schdJobDiv,String currMsg,int currProcCnt) {
		StringBuffer updateSql;
		Connection conn = null;
		PreparedStatement pstmt = null;
		BatchDBManager dbManager = new BatchDBManager();
		
		updateSql = new StringBuffer();
		updateSql.append("UPDATE FUSER.FMGT_SCHD_JOB_STAT_INFO 				");
		updateSql.append("SET	CURR_MSG      = ?,							");// ����_�޽���
		updateSql.append("		CURR_STAT     = 'W',						");// ����_��Ȳ
		updateSql.append("		CURR_PROC_CNT = ?,							");// ����_ó��_�Ǽ�
		updateSql.append("		LAST_PROC_DT  = REPLACE(VARCHAR(CURRENT DATE),'-',''),");//����_ó��_����
		updateSql.append("		LAST_PROC_TM  = REPLACE(VARCHAR(CURRENT TIME),'.',''),");//����_ó��_�ð�
		updateSql.append("		LAST_PROC_EMP_NO  = 'SYSTEMOP'				");//����_ó��_�����ȣ
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

	/** ��ġ�۾��� ���������� �������۾��������̺� �����ϴ� �޼ҵ�
	  @param jobId   �������۾�ID
	  @param deptCd  �μ��ڵ�
	  @param state   �������
	  @param msg     ����󼼸޽���
	  @return void
	*/
	public void endJob(String sendTp,String extrOrgCd,String bsnDivCd,String schdJobDiv,String currStat,String currMsg) {
		StringBuffer updateSql;
		Connection conn = null;
		PreparedStatement pstmt = null;
		BatchDBManager dbManager = new BatchDBManager();

		updateSql = new StringBuffer();
		updateSql.append("UPDATE FUSER.FMGT_SCHD_JOB_STAT_INFO 					");
		updateSql.append("SET	END_TM = REPLACE(VARCHAR(CURRENT TIME),'.',''),	");// ����_�ð�
		updateSql.append("		CURR_STAT = ?,									");// ����_��Ȳ
		updateSql.append("		CURR_MSG  = ? 									");// ����_�޽���
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
	
	/** [����] PDS FTP�������� ��� �޼ҵ�
    @param String extrOrgCd   - ��ܱ���ڵ�
    @param String bsnDivCd    - ���������ڵ�
    @param String stndDt      - ��������
    @param String snrFlag     - �ۼ����÷���
    @param String filePath    - �����н�
    @param String fileNm      - ���ϸ�
    @param String procTermNo  - ó���ܸ���ȣ
    @param String termRegMan  - �ܸ������
    @param String termRegManPswd  - �ܸ�����ھ�ȣ
    @param int    totRecCnt   - �ѷ��ڵ尳��(��ó������Ǽ�)
    @param int    fileSize    - ����ũ��
    @param String sUserId     - ����ھ��̵�
    @return void              - boolean (����/����)
    */
	/* History
	 �� �Լ��� FTPInfoManage ���� �����Ͽ� �ۼ��ߴ�.
	 ���� ���� �ִ� setPdsFtpSend �Լ��� �׽�Ʈ�� ���� ���·� FTPInfoManage �� �����Ͽ�  �׽�Ʈ �غ��� ������ ���� �����߰�,
	 �̸� �ٽ� �� ���α׷��� �ٽ� �����Ͽ� ����Ѵ�. ���� setPdsFtpSend �Լ��� �Ʒ��� �ִ�.
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
		
		System.out.println("�ϰ��ۼ��� SQL:"+countSql.toString());
		System.out.println("EXTRORG_CD:"+extrOrgCd);
		System.out.println("BSN_DIV_CD:"+bsnDivCd);
		System.out.println("STND_DT:"+stndDt);
		
		StringBuffer querySql = new StringBuffer();
		querySql.append("  INSERT INTO FUSER.FMGT_FTP_SNR_INFO  \n");
		querySql.append("   ( SEND_DT,            				\n");   // ��������
		querySql.append("     RECNO,                            \n");   // ���ڵ��ȣ
		querySql.append("     SEND_TP,                          \n");   // ����_Ÿ��
		querySql.append("     EXTRORG_CD,                       \n");   // ��ܱ��_�ڵ�
		querySql.append("     BSN_DIV_CD,                       \n");   // ����_�����ڵ�
		querySql.append("     STND_DT,                          \n");   // ����_����
		querySql.append("     SNR_JOB_FL,                       \n");   // �ۼ���_�۾�_FLAG
		querySql.append("     FILE_PATH,                        \n");   // ����_�н�
		querySql.append("     FILE_NM,                          \n");   // ����_��
		querySql.append("     PROC_TRMN_NO,                     \n");   // ó��_�ܸ���ȣ
		querySql.append("     TRMN_REG_MAN,                     \n");   // �ܸ�_�����
		querySql.append("     TRMN_REG_MAN_PSWD,                \n");   // �ܸ�_�����_��ȣ
		querySql.append("     SEQPER_REC_CNT,                   \n");   // SEQ��_���ڵ�_��
		querySql.append("     TOT_REC_CNT,                      \n");   // ��_���ڵ�_����
		querySql.append("     FILE_SIZE,                        \n");   // ����_ũ��
//		querySql.append("     SCS_REC_CNT,                      \n");   // ����_���ڵ�_����2006.03.07 ����
//		querySql.append("     FAIL_REC_CNT,                     \n");   // ����_���ڵ�_����2006.03.07 ����
		querySql.append("     STRT_DTTM_SCND,                   \n");   // ����_�Ͻ���
		querySql.append("     END_DTTM_SCND,                    \n");   // ����_�Ͻ���
//		querySql.append("     NEED_TM,                          \n");   // �ҿ�_�ð� 2006.03.07 ����
		querySql.append("     RSPN_CD,                          \n");   // ����_�ڵ�
		querySql.append("     JOB_STAT,                         \n");   // �۾�_����
		querySql.append("     HDCP_DETL_CD,                     \n");   // ���_����_�ڵ�
		querySql.append("     LAST_BLCK_NO,                     \n");   // ����_���_No
		querySql.append("     LAST_SNUM,                        \n");   // ����_�Ϸù�ȣ
		querySql.append("     LAST_REC_NO,                      \n");   // ����_���ڵ�_��ȣ
		querySql.append("     TRLR_INFO,                        \n");   // Ʈ���Ϸ�_����
		querySql.append("     FRST_REG_DT,                      \n");   // ����_���_����
		querySql.append("     FRST_REG_TM,                      \n");   // ����_���_�ð�
		querySql.append("     FRST_REG_EMP_NO,                  \n");   // ����_���_�����ȣ
		querySql.append("     LAST_PROC_DT,                     \n");   // ����_ó��_����
		querySql.append("     LAST_PROC_TM,                     \n");   // ����_ó��_�ð�
		querySql.append("     LAST_PROC_EMP_NO                  \n");   // ����_ó��_�����ȣ
		querySql.append("   )VALUES(                            \n");
		// ��������(�ý������� �ڵ��Ҵ�)
		querySql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''),						\n");
		// ���緹�ڵ��ȣ(��������+��ܱ���ڵ� ����  �Ҵ�)
		querySql.append("   (SELECT COALESCE(MAX(RECNO)+1,1) 							\n");
		querySql.append("	 FROM FUSER.FMGT_FTP_SNR_INFO								\n");
		querySql.append("    WHERE SEND_DT = REPLACE(VARCHAR(CURRENT DATE),'-','')		\n");
		querySql.append("    AND   EXTRORG_CD = ?),										\n");
		// ����Ÿ��,��ܱ���ڵ�,���������ڵ�
		querySql.append("    'B' , ?, ?,                     		\n");
		// ��������,�ۼ����÷���,�����н�,���ϸ� 
		querySql.append("    ?, ?, ?, ?,                    		\n");
		// ó���ܸ���ȣ,�ܸ������,�ܸ�����ھ�ȣ
		querySql.append("    ?, ?, ?,                      			\n");
		// SEQ��_���ڵ�_��,�ѷ��ڵ尳��,����ũ��
		querySql.append("    0, ?, ?,                     			\n");
		// �������ڵ尳��,���з��ڵ尳��
		//querySql.append("    0, 0, 	                     			\n");
		// �����Ͻ���,�����Ͻ���,�ҿ�ð�
		querySql.append("    '', '', 	                     		\n");
		// �����ڵ�,�۾�����,��ּ����ڵ�
		querySql.append("    '', '', '',                     		\n");
		// ������NO,�����Ϸù�ȣ,�������ڵ��ȣ
		querySql.append("    0, 0, 0,                     			\n");
		// Ʈ���Ϸ�_����,����_���_����,����_���_�ð�,����_���_�����ȣ
		querySql.append("    '', REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?, \n");
		// ����_ó��_����,����_ó��_�ð�,����_ó��_�����ȣ
		querySql.append("    '', '', '' )                          	\n");
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO ���̺� ���
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
	
	/** [����] PDS FTP��������set �޼ҵ�
    @param String extrOrgCd   - ��ܱ���ڵ�
    @param String bsnDivCd    - ���������ڵ�
    @param String stndDt      - ��������
    @param String filePath    - �����н�
    @param String fileNm      - ���ϸ�
    @param String procTermNo  - ó���ܸ���ȣ
    @param String termRegMan  - �ܸ������
    @param String termRegManPswd  - �ܸ�����ھ�ȣ
    @param int    totRecCnt   - �ѷ��ڵ尳��
    @param int    fileSize    - ����ũ��
    @param String sUserId     - ����ھ��̵�
    @return void              - void
    */	
	/* History
	 * �� �Լ��� ���� BatchDAO �� �ִ���.
	 * 
	public void setPdsFtpSend(String extrOrgCd, String bsnDivCd, String stndDt, String filePath, String fileNm, String procTermNo, String termRegMan, String termRegManPswd, int totRecCnt, int fileSize, String sUserId) throws Exception
	{
		System.out.println("(method)setPdsFtpSend() is started.");
		BatchDBManager 		dbManager = new BatchDBManager();		
		Connection 			conn 	  = null;
		PreparedStatement 	pstmt 	  = null;

		StringBuffer querySql = new StringBuffer();
		querySql.append("  INSERT INTO FUSER.FMGT_FTP_SNR_INFO  \n");
		querySql.append("   ( SEND_DT,            				\n");   // ��������
		querySql.append("     RECNO,                            \n");   // ���ڵ��ȣ
		querySql.append("     SEND_TP,                          \n");   // ����_Ÿ��
		querySql.append("     EXTRORG_CD,                       \n");   // ��ܱ��_�ڵ�
		querySql.append("     BSN_DIV_CD,                       \n");   // ����_�����ڵ�
		querySql.append("     STND_DT,                          \n");   // ����_����
		querySql.append("     SNR_JOB_FL,                       \n");   // �ۼ���_�۾�_FLAG
		querySql.append("     FILE_PATH,                        \n");   // ����_�н�
		querySql.append("     FILE_NM,                          \n");   // ����_��
		querySql.append("     PROC_TRMN_NO,                     \n");   // ó��_�ܸ���ȣ
		querySql.append("     TRMN_REG_MAN,                     \n");   // �ܸ�_�����
		querySql.append("     TRMN_REG_MAN_PSWD,                \n");   // �ܸ�_�����_��ȣ
		querySql.append("     SEQPER_REC_CNT,                   \n");   // SEQ��_���ڵ�_��
		querySql.append("     TOT_REC_CNT,                      \n");   // ��_���ڵ�_����
		querySql.append("     FILE_SIZE,                        \n");   // ����_ũ��
		querySql.append("     SCS_REC_CNT,                      \n");   // ����_���ڵ�_����
		querySql.append("     FAIL_REC_CNT,                     \n");   // ����_���ڵ�_����
		querySql.append("     STRT_DTTM_SCND,                   \n");   // ����_�Ͻ���
		querySql.append("     END_DTTM_SCND,                    \n");   // ����_�Ͻ���
		querySql.append("     NEED_TM,                          \n");   // �ҿ�_�ð�
		querySql.append("     RSPN_CD,                          \n");   // ����_�ڵ�
		querySql.append("     JOB_STAT,                         \n");   // �۾�_����
		querySql.append("     HDCP_DETL_CD,                     \n");   // ���_����_�ڵ�
		querySql.append("     LAST_BLCK_NO,                     \n");   // ����_���_No
		querySql.append("     LAST_SNUM,                        \n");   // ����_�Ϸù�ȣ
		querySql.append("     LAST_REC_NO,                      \n");   // ����_���ڵ�_��ȣ
		querySql.append("     TRLR_INFO,                        \n");   // Ʈ���Ϸ�_����
		querySql.append("     FRST_REG_DT,                      \n");   // ����_���_����
		querySql.append("     FRST_REG_TM,                      \n");   // ����_���_�ð�
		querySql.append("     FRST_REG_EMP_NO,                  \n");   // ����_���_�����ȣ
		querySql.append("     LAST_PROC_DT,                     \n");   // ����_ó��_����
		querySql.append("     LAST_PROC_TM,                     \n");   // ����_ó��_�ð�
		querySql.append("     LAST_PROC_EMP_NO                  \n");   // ����_ó��_�����ȣ
		querySql.append("   )VALUES(                            \n");
		// ��������(�ý������� �ڵ��Ҵ�)
		querySql.append("    VALUES (REPLACE(VARCHAR(CURRENT DATE),'-','')),			\n");
		// ���緹�ڵ��ȣ(��������+��ܱ���ڵ� ����  �Ҵ�)
		querySql.append("   (SELECT COALESCE(MAX(RECNO),1) 								\n");
		querySql.append("	 FROM FUSER.FMGT_FTP_SNR_INFO								\n");
		querySql.append("    WHERE SEND_DT = REPLACE(VARCHAR(CURRENT DATE),'-','')		\n");
		querySql.append("    AND   EXTRORG_CD = ?),										\n");
		// ����Ÿ��,��ܱ���ڵ�,���������ڵ�
		querySql.append("    'B' , ?, ?,                     		\n");
		// ��������,�ۼ����÷���,�����н�,���ϸ� 
		querySql.append("    ?, 'S', ?, ?,                    		\n");
		// ó���ܸ���ȣ,�ܸ������,�ܸ�����ھ�ȣ
		querySql.append("    ?, ?, ?,                      			\n");
		// SEQ��_���ڵ�_��,�ѷ��ڵ尳��,����ũ��
		querySql.append("    0, ?, ?,                     			\n");
		// �������ڵ尳��,���з��ڵ尳��
		querySql.append("    0, 0, 	                     			\n");
		// �����Ͻ���,�����Ͻ���,�ҿ�ð�
		querySql.append("    '', '', '',                     		\n");
		
		// �����ڵ�,�۾�����,��ּ����ڵ�
		querySql.append("    '', '', '',                     		\n");
		
		// ������NO,�����Ϸù�ȣ,�������ڵ��ȣ
		querySql.append("    0, 0, 0,                     			\n");
		
		// Ʈ���Ϸ�_����,����_���_����,����_���_�ð�,����_���_�����ȣ
		querySql.append("    '', VALUES (REPLACE(VARCHAR(CURRENT DATE),'-','')), VALUES (REPLACE(VARCHAR(CURRENT TIME),'-','')), ?, \n");
		// ����_ó��_����,����_ó��_�ð�,����_ó��_�����ȣ
		querySql.append("    '', '', '' )                          	\n");
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO ���̺� ���
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
