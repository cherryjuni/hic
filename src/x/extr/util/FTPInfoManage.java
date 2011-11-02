/** Main system          : x.biz
  * Sub system           : 
  * Classname            : FTPInfoManage
  * Initial date         : 2005.11.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : FTP �ۼ��� ������ ��� �����ϰ� ��Ŷ�� �����ϴ�
  * 					   �׽�Ʈ�� ���� ���α׷��̴�.
  *                        ���� ����α׷� �ۼ��ÿ� �����ϰ� �����Ҽ� �ִ�.
  */


package x.extr.util;

import java.sql.*;
import java.util.*;


import x.extr.db.*;
import x.extr.log.*;
import x.extr.net.*;

/** FTP ���� ���� Ŭ����
  * @author Ȳ��õ
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

	/** ��ġ�۾��� ���������� �������۾��������̺� �����ϴ� �޼ҵ�
	  @param jobId   �������۾�ID
	  @param deptCd  �����۾��ڵ�
	  @param totCnt  �������۾��Ǽ�
	  @return ����
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
		querySql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''),			\n");
		// ���緹�ڵ��ȣ(��������+��ܱ���ڵ� ����  �Ҵ�)
		querySql.append("   (SELECT COALESCE(MAX(RECNO)+1,1) 								\n");
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
		querySql.append("    0, 0, 	                     			\n");
		// �����Ͻ���,�����Ͻ���,�ҿ�ð�
		querySql.append("    '', '', 0.00,                     		\n");
		
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
		insertSql.append("   ( SEND_TP,            				 \n");   // ����_Ÿ��
		insertSql.append("     EXTRORG_CD,                       \n");   // ��ܱ��_�ڵ�
		insertSql.append("     BSN_DIV_CD,                       \n");   // ����_�����ڵ�
		insertSql.append("     GRAM_NM,                          \n");   // ����_��
		insertSql.append("     ITEM_CNT,                       	 \n");   // �׸�_����
		insertSql.append("     REC_LTH,                        	 \n");   // ���ڵ�_����
		insertSql.append("     FRST_REG_DT,                      \n");   // ����_���_����
		insertSql.append("     FRST_REG_TM,                      \n");   // ����_���_�ð�
		insertSql.append("     FRST_REG_EMP_NO,                  \n");   // ����_���_�����ȣ
		insertSql.append("     LAST_PROC_DT,                     \n");   // ����_ó��_����
		insertSql.append("     LAST_PROC_TM,                     \n");   // ����_ó��_�ð�
		insertSql.append("     LAST_PROC_EMP_NO                  \n");   // ����_ó��_�����ȣ
		insertSql.append("   )VALUES(                            \n");
		// ����Ÿ��,��ܱ���ڵ�,���������ڵ�
		insertSql.append("    'B', ?, ?,                     	\n");
		// ����_�� ,�׸�_����     ,���ڵ�_���� 
		insertSql.append("    ?, ?, ?,                    		\n");
		// ����_���_����,����_���_�ð�,����_���_�����ȣ
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?, \n");
		// ����_ó��_����,����_ó��_�ð�,����_ó��_�����ȣ
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?  \n");
		insertSql.append("    )                    				\n");
		
		StringBuffer updateSql = new StringBuffer();
		updateSql.append("  UPDATE FUSER.FMGT_GRAM_INFO  	 	 						\n");
		updateSql.append("  SET GRAM_NM  = ?,                       					\n");   // ����_��
		updateSql.append("      ITEM_CNT = ?,                       	 				\n");   // �׸�_����
		updateSql.append("      REC_LTH  = ?,                        	 				\n");   // ���ڵ�_����
		updateSql.append("      LAST_PROC_DT = REPLACE(VARCHAR(CURRENT DATE),'-',''),   \n");   // ����_ó��_����
		updateSql.append("      LAST_PROC_TM = REPLACE(VARCHAR(CURRENT TIME),'.',''),   \n");   // ����_ó��_�ð�
		updateSql.append("      LAST_PROC_EMP_NO = ?                 					\n");   // ����_ó��_�����ȣ
		updateSql.append("  WHERE SEND_TP = 'B'   				 						\n");   // ����_Ÿ��
		updateSql.append("  AND   EXTRORG_CD = '"+extrOrgCd+"'   						\n");   // ��ܱ��_�ڵ�
		updateSql.append("  AND   BSN_DIV_CD = '"+bsnDivCd+"'    						\n");   // ����_�����ڵ�
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO ���̺� ���
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
		insertSql.append("   ( SCHD_JOB_ID,           			 \n");   // ������_�۾�_ID
		insertSql.append("     ACCO_JOB_CD,                      \n");   // ����_�۾�_�ڵ�
		insertSql.append("     STRT_TM,                          \n");   // ����_�ð�
		insertSql.append("     END_TM,                           \n");   // ����_�ð�
		insertSql.append("     TOT_PROC_EXPT_CNT,              	 \n");   // ��_ó��_����_�Ǽ�
		insertSql.append("     CURR_MSG,                       	 \n");   // ����_�޽���
		insertSql.append("     CURR_STAT,                      	 \n");   // ����_��Ȳ
		insertSql.append("     CURR_PROC_CNT,                  	 \n");   // ����_ó��_�Ǽ�
		insertSql.append("     FRST_REG_DT,                      \n");   // ����_���_����
		insertSql.append("     FRST_REG_TM,                      \n");   // ����_���_�ð�
		insertSql.append("     FRST_REG_EMP_NO,                  \n");   // ����_���_�����ȣ
		insertSql.append("     LAST_PROC_DT,                     \n");   // ����_ó��_����
		insertSql.append("     LAST_PROC_TM,                     \n");   // ����_ó��_�ð�
		insertSql.append("     LAST_PROC_EMP_NO                  \n");   // ����_ó��_�����ȣ
		insertSql.append("   )VALUES(                            \n");
		// ������_�۾�_ID
		insertSql.append("    (SELECT 'SCHE'||guser.gf_lpad_01((INTEGER(MAX(SUBSTR(SCHD_JOB_ID,5,4))) + 1),4,'0') FROM FUSER.FMGT_SCHD_JOB_STAT_INFO ), \n");
		// ����_�۾�_�ڵ�, ����_�ð�, ����_�ð� 
		insertSql.append("    ?, '', '',                    		\n");
		// ��_ó��_����_�Ǽ�, ����_�޽���, ����_��Ȳ 
		insertSql.append("    ?, '', '',0,                    		\n");		
		// ����_���_����,����_���_�ð�,����_���_�����ȣ
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?, \n");
		// ����_ó��_����,����_ó��_�ð�,����_ó��_�����ȣ
		insertSql.append("    REPLACE(VARCHAR(CURRENT DATE),'-',''), REPLACE(VARCHAR(CURRENT TIME),'.',''), ?  \n");
		insertSql.append("    )                    				\n");
		
		StringBuffer selectSql = new StringBuffer();
		selectSql.append("SELECT 'SCHE'||MAX(SUBSTR(SCHD_JOB_ID,5,4)) FROM FUSER.FMGT_SCHD_JOB_STAT_INFO ");
		
		try  
		{   
			// PDS XT_FTP_SNR_INFO ���̺� ���
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
	
	/**(method) �������۽���
    @param String[] jobInfo - ��������
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
		String scheJobId 	= setStrFillLength(jobInfo[0],  8,1); 	// ��������̵�(�ƱԸ�Ʈ)
		String sendDt 		= setStrFillLength(jobInfo[1],  8,1);	// ��������(�ý�������)
		String extrOrgType	= setStrFillLength(jobInfo[2],  1,1);	// ��������(����)
		String extrOrgCd	= setStrFillLength(jobInfo[3],  2,1);	// ��ܱ���ڵ�(����)
		String bsnDivCd	    = setStrFillLength(jobInfo[4],  8,1);	// �ŷ������ڵ�(DB)
		String stndDt		= setStrFillLength(jobInfo[5],  8,1);	// ��������(�ý�������)
		String snrFlag		= setStrFillLength(jobInfo[6],  1,1);	// �ۼ��ſ���(����)
		String filePath	    = setStrFillLength(jobInfo[7], 50,1);	// ���ϰ��(DB)
		String fileName	    = setStrFillLength(jobInfo[8], 30,1);	// ���ϸ�(DB)
		String totRecCnt	= setStrFillLength(jobInfo[9],  7,2);	// ������ �ѰǼ�(DB)
		String procTrmnNo	= setStrFillLength(jobInfo[10], 8,1);	// ó���ܸ���ȣ(DB)
		String trmnRegMan	= setStrFillLength(jobInfo[11], 8,1);	// �ܸ������(DB)
		String trmnRegManPw = setStrFillLength(jobInfo[12],16,1);	// �ܸ�����ھ�ȣ(DB)
		String reserved	    = setStrFillLength(jobInfo[13],15,1);	// ��������(DB)

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
