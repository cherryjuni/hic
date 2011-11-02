/** Main system          : x.extr
  * Sub system           : util
  * Classname            : ScheMgr.java
  * Initial date         : 2005-11-14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ��ϵ� ������������ �ڵ������Ű�� Class
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
  */
package x.extr.util;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;

import x.extr.db.*;
import x.extr.exception.*;
import x.extr.log.Log;
import x.extr.util.*;

/** ��ϵ� ������������ �ڵ������Ű�� Class
    @author Ȳ��õ
*/

public class ScheMgr 
{
	private String sSystem = "util";
	private String sClassName = "ScheMgr";
	
	private DBManager dbManager;
	private BatchDBManager bdbManager;
	/**
      * @ Constructor for JDBC
      */
	public ScheMgr() {
		bdbManager= new BatchDBManager();
	}

	/**
      * @ Constructor for DataSource
      * @param dataSource           ���콺 �����ͼҽ�
	  */
	public ScheMgr(String dataSource) {
		dbManager = new DBManager(dataSource);
	}

	/** �־��� ������(���� + �ú�)�� ������, ���� ���డ���� �������۾��� �����Ű�� �޼ҵ�
	  @param date             �������۾��� �����ų ��¥
	*/

	//������  �� + ��
	public void getJob(String date) {
		Object[] oa;
		Thread t;

		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  "ScheMgr.getJob Start");
		//������  0 + ����(1,2...7) + �� + �� + �� + ��
		
		Connection 		  con    = null;
		PreparedStatement cpstmt = null;	// Current Job
		PreparedStatement ppstmt = null;	// Previous Job
		ResultSet 		  rsCur  = null;	// Current Job Result Set
		ResultSet 		  rsPrv  = null;	// Previous Job Result Set
		StringBuffer 	  SqlCur = null;	// Current Sql
		StringBuffer 	  SqlPrv = null;	// Previous Sql

		String[] sa = null;

		try {
			con = bdbManager.getConnection(); 	// JDBC ��������
			SqlCur = new StringBuffer("");
			SqlCur.append("SELECT   		                		\n");
			SqlCur.append("         SEND_TP                         \n");
			SqlCur.append("        ,EXTRORG_CD                		\n");
			SqlCur.append("        ,BSN_DIV_CD                 		\n");
			SqlCur.append("        ,SCHD_JOB_DIV                    \n");
			SqlCur.append("        ,SCHD_EXEC_TM               		\n");
			SqlCur.append("        ,SCHD_TP                    		\n");
			SqlCur.append("        ,SCHD_JOB_NM                     \n");
			SqlCur.append("        ,EXEC_CLASS                 		\n");
			SqlCur.append("        ,EXEC_METHOD                		\n");
			SqlCur.append("        ,EXEC_PARM                 		\n");
			SqlCur.append("        ,AFROW_JOB_AUTO_PROC_FG     		\n");
			SqlCur.append("FROM 	FUSER.FMGT_SCHD_JOB_INFO   		\n");
			SqlCur.append("WHERE    SCHD_EXEC_TM	= ?			  	\n");
			SqlCur.append("ORDER BY SEND_TP,	EXTRORG_CD,			\n");
			SqlCur.append("		    BSN_DIV_CD,	SCHD_JOB_DIV ASC	\n");
			SqlCur.append("WITH UR;                					\n");
			cpstmt = con.prepareStatement(SqlCur.toString());
			for(int i=date.length();i<8;i++){
				date = "0" + date;
			}
			//date = "00000000";
			cpstmt.setString(1,date);
			rsCur = cpstmt.executeQuery();
			
			System.out.println("�۾��ð�:"+date);
			
			List jobList = ScheJobConvert.getScheJobList(rsCur);
			
			System.out.println("ó����...............");
			if (jobList.size() == 0) {
				System.out.println("ó���� �۾��� �����ϴ�.");
			} else {
				System.out.println("ó���� ��ġ�Ǽ� : " + jobList.size());
				for(int n=0;n<jobList.size();n++) { 
					// ���������� �����Ѵ�.
					ScheJobEntity jobEntity = (ScheJobEntity)jobList.get(n);
					String sendTp = jobEntity.getSendTp();
					String extrOrgCd = jobEntity.getExtrOrgCd();
					String bsnDivCd = jobEntity.getBsnDivCd();
					String scheJobDiv = jobEntity.getSchdJobDiv();
					SqlPrv = new StringBuffer("");
					SqlPrv.append("SELECT					                                  	\n");
					SqlPrv.append("			E.PREC_SEND_TP                                  	\n");
					SqlPrv.append("		   ,E.PREC_EXTRORG_CD                                  	\n");
					SqlPrv.append("		   ,E.PREC_BSN_DIV_CD                                  	\n");
					SqlPrv.append("		   ,E.PREC_SCHD_JOB_DIV                                 \n");
					SqlPrv.append("	   	   ,E.PREC_CURR_STAT                                    \n");
					SqlPrv.append("FROM (                                                       \n");
					SqlPrv.append("     SELECT 													\n");
					SqlPrv.append("     		A.PREC_SEND_TP                              	\n");
					SqlPrv.append("			   ,A.PREC_EXTRORG_CD								\n");
					SqlPrv.append("			   ,A.PREC_BSN_DIV_CD								\n");
					SqlPrv.append("			   ,A.PREC_SCHD_JOB_DIV								\n");
					SqlPrv.append("																\n");
					SqlPrv.append("																\n");					
					SqlPrv.append("     	   ,A.SEND_TP                               	\n");
					SqlPrv.append("     	   ,A.EXTRORG_CD                               	\n");
					SqlPrv.append("     	   ,A.BSN_DIV_CD                               	\n");
					SqlPrv.append("     	   ,A.SCHD_JOB_DIV                               	\n");
					
					SqlPrv.append("            ,B.CURR_STAT PREC_CURR_STAT                      \n");
					SqlPrv.append("     FROM 	FUSER.FMGT_PREC_SCHD_JOB_INFO A,                \n");
					SqlPrv.append("             FUSER.FMGT_SCHD_JOB_STAT_INFO B                 \n");
					SqlPrv.append("     WHERE 	A.SEND_TP      =  ?                         	\n");
					SqlPrv.append("     AND 	A.EXTRORG_CD   =  ?                         	\n");
					SqlPrv.append("     AND 	A.BSN_DIV_CD   =  ?                         	\n");
					SqlPrv.append("     AND 	A.SCHD_JOB_DIV =  ?                         	\n");
					SqlPrv.append("																\n");
					SqlPrv.append("																\n");
					SqlPrv.append("																\n");
					SqlPrv.append("																\n");
					SqlPrv.append("																\n");
					SqlPrv.append("     AND     A.PREC_SEND_TP 	= B.SEND_TP              		\n");
					SqlPrv.append("     AND     A.PREC_EXTRORG_CD = B.EXTRORG_CD                \n");
					SqlPrv.append("     AND     A.PREC_BSN_DIV_CD = B.BSN_DIV_CD                \n");
					SqlPrv.append("     AND     A.PREC_SCHD_JOB_DIV = B.SCHD_JOB_DIV            \n");
					
					SqlPrv.append(") E                                                    		\n");
					SqlPrv.append("WHERE   E.PREC_CURR_STAT <> 'X'								\n");
					SqlPrv.append("WITH UR;                										\n");
					ppstmt = con.prepareStatement(SqlPrv.toString());
					ppstmt.setString(1,sendTp);
					ppstmt.setString(2,extrOrgCd);
					ppstmt.setString(3,bsnDivCd);
					ppstmt.setString(4,scheJobDiv);
					rsPrv = ppstmt.executeQuery();

					List prevJobList = ScheJobConvert.getPrevScheJobList(rsPrv);
					if (prevJobList.size() == 0 ) {
						System.out.println("[" + sendTp+extrOrgCd+bsnDivCd+scheJobDiv + "]�����۾��� �Ϸ�Ǿ����ϴ�.");
					} else {
						System.out.println("[" + sendTp+extrOrgCd+bsnDivCd+scheJobDiv + "]�̿Ϸ�� �����۾��� �ֽ��ϴ�.");

						for(int m=0;m<prevJobList.size();m++) {
							PrevScheJobEntity prevJobEntity = (PrevScheJobEntity)prevJobList.get(m);
							System.out.println("[" + m + "] : " + prevJobEntity.getPrecSendTp()+prevJobEntity.getPrecExtrOrgCd()+prevJobEntity.getPrecBsnDivCd()+prevJobEntity.getPrecSchdJobDiv() + " , " + prevJobEntity.getPrevCurrStat());
						}
						continue;
					}
					
					if( jobEntity.getExecParm() == null || jobEntity.getExecParm().equals("")){
						sa = FormatData.strSplit(sendTp+","+extrOrgCd+","+bsnDivCd+","+scheJobDiv,",");
					}else{
						sa = FormatData.strSplit(sendTp+","+extrOrgCd+","+bsnDivCd+","+scheJobDiv+","+jobEntity.getExecParm(),",");
					}
					for (int i=0;i<sa.length;i++) {
						if (sa[i].startsWith("%") && sa[i].endsWith("%")) {
							/* �Ķ��Ÿ�� ������̺� �ִ°��� % ��  % ���̷� ������.(��뿹:���н��� Ŭ�����н� ����)
							 * %�� %���̿� �ִ� �ش�Ű�� getConst �� �Ķ��Ÿ�� �Ѱ� ����� �����´�.
							 * �� ����� select const_cd,const_value from x1_const ���⿡ �ִ�.
							 * �Ʒ� �ڵ��� ����� ����� ������Ƽ�κ��� �����´�.
							 */
							
							/* sa[i] = cm.getConst(sa[i].substring(1,sa[i].length()-1)); */
							
							/*
							 * �� �ڵ��� ���κ��� ������ �������Ʈ���� ����ִ� ������Ƽ�� Ű���� �Ѱ� 
							 * �ش� ������� �����´�.
							 */
						}
					}
					
					System.out.println("sa.length:"+sa.length);
					
					/*
					oa = new Object[3];
					oa[0] = rsCur.getString("SCHD_JOB_ID");
					oa[1] = "000";
					oa[2] = sa;
					*/

					oa = new Object[1];
					oa[0] = sa;

					System.out.println("[" + n + "] : " + sendTp+extrOrgCd+bsnDivCd+scheJobDiv + " , " + jobEntity.getSchdJobNm() + " , " + jobEntity.getExecClass() + " , " + jobEntity.getExecMethod() + " , " + jobEntity.getExecParm());
					AsyncCaller ac = new AsyncCaller(jobEntity.getExecClass(),jobEntity.getExecMethod(),oa);
					t = new Thread(ac);
					t.start();
					
					/*
					SyncCaller sc = new SyncCaller();
					sc.call(rsCur.getString("EXEC_CLASS"),rsCur.getString("EXEC_METHOD"),oa);
					*/
					System.out.println("[" + n + "] : " + sendTp+extrOrgCd+bsnDivCd+scheJobDiv + " , " + jobEntity.getSchdJobNm() + " , " + jobEntity.getExecClass() + " , " + jobEntity.getExecMethod() + " , " + jobEntity.getExecParm());
				}
			}
			System.out.println("ó����...............");
		} catch (DBException dbe) {
			dbe.printStackTrace();
			Log.writeLog(sSystem, sClassName, "getJob()", Log.LOG_CRITICAL, dbe.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Log.writeLog(sSystem, sClassName, "getJob()", Log.LOG_CRITICAL, "Exception �߻�");
		}
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  "ScheMgr.getJob Finish");
		return;
	}

	/** �������۾��� ������Ű�� �޼ҵ�
	  @param jobId               �������۾�ID
	  @param execClass           �����ų Ŭ����
	  @param execMethod          �����ų �޼ҵ�
	  @param parm                ���� �Ķ����
	  @return boolean
	*/
	public boolean startJob(String jobId, String execClass,String execMethod,String parm) {
		System.out.println("startJob started....");
		int i;
		Object[] oa;
		Thread t;

		Connection con = null;
		PreparedStatement pstmt = null;
		
		StringBuffer selectSql;
		ResultSet 	 rs  = null;

		try {			
			String sBizDate = "20051115";//ConstManager.getInstance().getConst("X1_BATCHDATE");
			selectSql = new StringBuffer("select e.prv_job_id,e.dpt_cd,e.cstat");
			selectSql.append(" from (  select c.prv_job_id,b.dpt_cd,nvl(b.cur_stat,'N') cstat");
			selectSql.append("              from x1_schejobexecstat b,");
			selectSql.append("                     ( select a.sche_job_id,a.prv_job_id,decode(a.dpt_chk_cls,'0','%%','AAA') dpt_cd");
			selectSql.append("                         from x1_prvschejob a");
			selectSql.append("                        where a.sche_job_id = ? ) c");
			selectSql.append("              where b.exec_date(+) = ?");
			selectSql.append("              and b.sche_job_id(+) = c.prv_job_id");
			selectSql.append("    	        and b.dpt_cd(+) like c.dpt_cd ) e");
			selectSql.append(" where e.cstat <> 'D'");
			pstmt = con.prepareStatement(selectSql.toString());
			pstmt.setString(1,jobId);
			pstmt.setString(2,sBizDate);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				//if (rs.getRowCount() > 0) {
				if( rs.next() ){
					//Log.writeLog(sSystem, sClassName, "startJob()", Log.LOG_DEBUG, "[" + jobId + "]�̿Ϸ�� �����۾��� �ֽ��ϴ�.(" + rs.getRowCount() + ")");
					Log.writeLog(sSystem, sClassName, "startJob()", Log.LOG_DEBUG, "[" + jobId + "]�̿Ϸ�� �����۾��� �ֽ��ϴ�.");
//					System.out.println("[" + jobId + "]�̿Ϸ�� �����۾��� �ֽ��ϴ�.(" + rs.getRowCount() + ")");
					i = 1;
					while (rs.next()) { 
						Log.writeLog(sSystem, sClassName, "startJob()", Log.LOG_DEBUG, "[" + i + "] : " + rs.getString("PRV_JOB_ID") + " , " + rs.getString("DPT_CD") + " , " + rs.getString("CSTAT"));
//						System.out.println("[" + i + "] : " + rs.getString("PRV_JOB_ID") + " , " + rs.getString("DPT_CD") + " , " + rs.getString("CSTAT"));
						i++;
					}
					return false;
				}
			}

			String[] sa = FormatData.strSplit(parm,",");
			//ConstManager cm = ConstManager.getInstance();
			for (i=0;i<sa.length;i++) {
				if (sa[i].startsWith("%") && sa[i].endsWith("%")) {
					//sa[i] = cm.getConst(sa[i].substring(1,sa[i].length()-1));
				}
			}
			oa = new Object[2];
			oa[0] = jobId;
			oa[1] = sa;

			AsyncCaller ac = new AsyncCaller(execClass,execMethod,oa);
			t = new Thread(ac);
			t.start();
		//} catch (DBException dbe) {
		//	Log.writeLog(sSystem, sClassName, "startJob()", Log.LOG_CRITICAL, dbe.toString());
		//	return false;
		} catch (Exception e) {
			Log.writeLog(sSystem, sClassName, "startJob()", Log.LOG_CRITICAL, "Exception �߻�");
			return false;
		}

		return true;
	}

	/** �ڵ������� Thread�� �����Ѵ�.
	  */
	//private void runSche() {
	public void runSche() {
		try {
			new RunSche().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "runSche()", Log.LOG_CRITICAL, "runSche()ó���� �������߻��߽��ϴ�.");
		}
	}

	/** ������ �����ϸ�, 1�д����� �۾������� �������۾��� �����Ű�� ����  Thread
	  */
	class RunSche extends Thread {
		public void run() {
			int actionMinute = -1;
			System.out.println("RunSche Thread Start");
			Calendar c = Calendar.getInstance();
			ScheMgr sm = new ScheMgr();

			try {			
				while (true) {
									
					c.setTime(new java.sql.Date(System.currentTimeMillis()));
					if (actionMinute != c.get(Calendar.MINUTE)){
						sm.getJob(FormatData.numToStr(c.get(Calendar.HOUR_OF_DAY),2,"0") + FormatData.numToStr(c.get(Calendar.MINUTE),2,"0"));
						actionMinute = c.get(Calendar.MINUTE);
						System.out.println("actionMinute ==> "+ actionMinute);	
					} else {
						//System.out.println("now - " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
					}

					Thread.sleep(1000);
				}
			} catch (Exception e) {
				System.out.println("[RunSche Thread] Exception!!!!!!!!!!!");
			}
			System.out.println("RunSche Thread Finish");
		} 
	}
	/** ������ �����ϸ�, 1�д����� �۾������� �������۾��� �����Ű�� ���� Main���
	*/
	public static void main(String[] args) {
		System.out.println("Schedule Server Start-----------------");
		//DB ����
		ScheMgr sm = new ScheMgr();
		sm.runSche();
		System.out.println("Main Finish----------");
	}
}
