/** Main system          : foundation
  * Sub system           : util
  * Classname            : ScheMgrSA.java
  * Initial date         : 2005-11-15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ��ϵ� ������������ �ڵ������Ű�� Class
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
  */
package x.extr.util;

import java.sql.*;
import java.util.*;
import x.extr.db.*;
//import x.extr.lib.ConstManager;
import x.extr.exception.*;
import x.extr.log.Log;
import x.extr.util.*;

/** ��ϵ� ������������ �ڵ������Ű�� Class
    @author Ȳ��õ
*/
public class ScheMgrSA 
{
	String sSystem = "X1";
	String sClassName = "ScheMgrSA";
	//private DBManager dbManager;
	static private Connection con = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;

	/** �־��� ������(���� + �ú�)�� ������, ���� ���డ���� �������۾��� �����Ű�� �޼ҵ�
	  @param date             �������۾��� �����ų ��¥
	*/
	//������  �� + ��
	private void init() throws Exception{
		BDBManager dbmanager = new BDBManager();
		con = dbmanager.getConnection();
		if (con == null){
		  System.out.println("DB�� �������� ���߽��ϴ�.");  
		}		
	}

	public void getJob(String date) {
		String sBizDate = null;
		StringBuffer selectSql;
		String sScheJobId = null;
		//int i,j;
		Object[] oa;
		Thread t;

		System.out.println("ACTION : " + date);//������  0 + ����(1,2...7) + �� + �� + �� + ��
		String[] sa = null;		
		try {			
			//��ġ ���ڸ� �����´�.
			sBizDate = getConstValue("X1_BATCHDATE");
			//�������� ���� ��� ���� ó���� �������.
			System.out.println("BIZDATE is [" + sBizDate + "]");
			//�����ؾ��� ��� ������������ ��ȸ�Ѵ�.
			selectSql = new StringBuffer("");
			selectSql.append("SELECT   C.SCHE_JOB_ID SCHE_JOB_ID                                                                                 \n");
			selectSql.append("        ,C.SCHE_JOB_NM                                                                                             \n");
			selectSql.append("        ,C.EXEC_CLASS                                                                                              \n");
			selectSql.append("        ,C.EXEC_METHOD                                                                                             \n");
			selectSql.append("        ,C.EXEC_PARM                                                                                               \n");
			selectSql.append("FROM 	(                                                                                                            \n");
			selectSql.append("		SELECT 	NVL(B.CUR_STAT,'R') CSTAT                                                                            \n");
			selectSql.append("			,A.SCHE_JOB_ID                                                                                           \n");
			selectSql.append("			,A.SCHE_JOB_NM                                                                                           \n");
			selectSql.append("			,A.EXEC_CLASS                                                                                            \n");
			selectSql.append("			,A.EXEC_METHOD                                                                                           \n");
			selectSql.append("			,NVL(A.EXEC_PARM,'') EXEC_PARM                                                                           \n");
			selectSql.append("		FROM 	(                                                                                                    \n");
			selectSql.append("			SELECT	 D.TYPE                                                                                          \n");
			selectSql.append("					,D.*                                                                                             \n");
			selectSql.append("			FROM (                                                                                                   \n");
			selectSql.append("					SELECT 	SUBSTR(E.SCHE_INFO,1,2) TYPE                                                             \n");
			selectSql.append("							,E.SCHE_JOB_ID                                                                      	 \n");
			selectSql.append("							,E.SCHE_JOB_NM                                                                      	 \n");
			selectSql.append("							,E.EXEC_CLASS                                                                       	 \n");
			selectSql.append("							,E.EXEC_METHOD                                                                      	 \n");
			selectSql.append("							,E.SCHE_GRP                                                                         	 \n");
			selectSql.append("							,E.SCHE_KND                                                                         	 \n");
			selectSql.append("							,E.NEXT_JOB_CLS                                                                     	 \n");
			selectSql.append("							,E.SCHE_INFO                                                                        	 \n");
			selectSql.append("							,DECODE(F.SCHE_GRP ,NULL ,E.EXEC_PARM,F.EXEC_PARM) EXEC_PARM                        	 \n");
			selectSql.append("					FROM 	X1_SCHEJOBINFO E, X1_SCHEDATE F                                                          \n");
			selectSql.append("					WHERE	E.SCHE_GRP = F.SCHE_GRP(+)                                                               \n");
			selectSql.append("					AND		F.SCHE_DATE(+) = ?                                                                       \n");
			selectSql.append("			) D                                                                                                      \n");
			selectSql.append("			WHERE ( D.TYPE = '01' )                                                                                  \n");
			selectSql.append("			OR 	  ( D.TYPE = '02' 	AND (SELECT BIZ_DT_CLS FROM X1_BIZDT WHERE BIZ_DT = ?) IN ('00','01','02') ) 	 \n");
			selectSql.append("			OR 	  ( D.TYPE = '03' 	AND ? IN (SELECT SCHE_DATE FROM X1_SCHEDATE WHERE SCHE_GRP = D.SCHE_GRP) )   	 \n");
			selectSql.append("		) A,X1_SCHEJOBEXECSTAT B                                                                                     \n");
			selectSql.append("		WHERE   (SUBSTR(A.SCHE_INFO,3,4) <= ? OR (SUBSTR(A.SCHE_INFO,3,4) = '____' AND A.NEXT_JOB_CLS = 'Y'))        \n");
			selectSql.append("		AND 	A.SCHE_KND = '1'                                                                                     \n");
			selectSql.append("		AND 	? = B.EXEC_DATE(+)   --X1_BATCHDATE                                                                  \n");
			selectSql.append("		AND 	A.SCHE_JOB_ID = B.SCHE_JOB_ID(+)                                                                     \n");
			selectSql.append("		AND 	B.DPT_CD(+) = '000'                                                                                  \n");
			selectSql.append(")C                                                                                                                 \n");
			selectSql.append("WHERE C.CSTAT IN ('R')																							 \n");
			pstmt2 = con.prepareStatement(selectSql.toString());
			pstmt2.setString(1,sBizDate);
			pstmt2.setString(2,sBizDate);
			pstmt2.setString(3,sBizDate);
			pstmt2.setString(4,date.substring(0,4));
			pstmt2.setString(5,sBizDate);
			
			rs2 = pstmt2.executeQuery();		
			//�۾��� ����� �ִ� �� ���ο� ���� ó��.
			StringBuffer selectSqlPrv = new StringBuffer("");

			/*�������� üũ ���� ����(2004.02.03)
			//��������������, ���� ����Ǿ�� �ϴ� �������� �ƴ� ��� �������Ǵ�󿡼� �����ϵ��� �Ѵ�.
			selectSqlPrv.append("SELECT	E.PRV_JOB_ID                                                    \n");
			selectSqlPrv.append("				,E.DPT_CD                                                       \n");
			selectSqlPrv.append("				,E.CSTAT                                                        \n");
			selectSqlPrv.append("FROM 		(                                                             \n");
			selectSqlPrv.append("				SELECT 	C.PRV_JOB_ID                                            \n");
			selectSqlPrv.append("								,B.DPT_CD,NVL(B.CUR_STAT,'N') CSTAT                     \n");
			selectSqlPrv.append("        FROM 		X1_SCHEJOBEXECSTAT B,                                 \n");
			selectSqlPrv.append("        				(                                                       \n");
			selectSqlPrv.append("        				SELECT 	A.SCHE_JOB_ID                                   \n");
			selectSqlPrv.append("        								,A.PRV_JOB_ID                                   \n");
			selectSqlPrv.append("        								,DECODE(A.DPT_CHK_CLS,'0','%%','AAA') DPT_CD    \n");
			selectSqlPrv.append("                FROM 		X1_PRVSCHEJOB A                               \n");
			selectSqlPrv.append("                WHERE 	A.SCHE_JOB_ID =  ?                             \n");
			selectSqlPrv.append("                ) C                                                    \n");
			selectSqlPrv.append("        WHERE 	B.EXEC_DATE(+) =  ?                                    \n");
			selectSqlPrv.append("        AND 		B.SCHE_JOB_ID(+) = C.PRV_JOB_ID                         \n");
			selectSqlPrv.append("    		AND 		B.DPT_CD(+) LIKE C.DPT_CD                               \n");
			selectSqlPrv.append("    		) E                                                             \n");
			selectSqlPrv.append("WHERE 	E.CSTAT <> 'D'																									\n");
			*/
			selectSqlPrv.append("SELECT 	PRV.PRV_JOB_ID                                                                                                          \n");
			selectSqlPrv.append("FROM (                                                                                                                             \n");
			selectSqlPrv.append("			SELECT                                                                                                                  \n");
			selectSqlPrv.append("					 E.PRV_JOB_ID                                                                                                   \n");
			selectSqlPrv.append("					,E.CSTAT                                                                                                        \n");
			selectSqlPrv.append("			FROM 	(                                                                                                           	\n");
			selectSqlPrv.append("					SELECT 	C.PRV_JOB_ID                                                                                            \n");
			selectSqlPrv.append("							,B.DPT_CD                                                                                               \n");
			selectSqlPrv.append("							,NVL(B.CUR_STAT,'N') CSTAT                                                                              \n");
			selectSqlPrv.append("        			FROM 	X1_SCHEJOBEXECSTAT B,                                                                                   \n");
			selectSqlPrv.append("        					(                                                                                                       \n");
			selectSqlPrv.append("        					SELECT 	 A.SCHE_JOB_ID                                                                                  \n");
			selectSqlPrv.append("        							,A.PRV_JOB_ID                                                                                   \n");
			selectSqlPrv.append("        							,DECODE(A.DPT_CHK_CLS,'0','%%','AAA') DPT_CD                                                    \n");
			selectSqlPrv.append("           				FROM 	X1_PRVSCHEJOB A                                                                                 \n");
			selectSqlPrv.append("           				WHERE 	A.SCHE_JOB_ID =  ? --������ID                                                                    \n");
			selectSqlPrv.append("           				) C                                                                                                     \n");
			selectSqlPrv.append("        			WHERE 	B.EXEC_DATE(+) =  ?  --��������                                                                                                     						\n");
			selectSqlPrv.append("        			AND 		B.SCHE_JOB_ID(+) = C.PRV_JOB_ID                                                                     \n");
			selectSqlPrv.append("    				AND 		B.DPT_CD(+) LIKE C.DPT_CD                                                                           \n");
			selectSqlPrv.append("    		) E                                                                                                                     \n");
			selectSqlPrv.append("		    WHERE 	E.CSTAT <> 'D' ) PRV                                                                                            \n");
			selectSqlPrv.append("WHERE 	PRV.PRV_JOB_ID IN(                                                                                                          \n");
			selectSqlPrv.append("  			SELECT	A.SCHE_JOB_ID                                                                                                   \n");
			selectSqlPrv.append("  			FROM 	(                                                                                                               \n");
			selectSqlPrv.append("  					SELECT	D.TYPE                                                                                                  \n");
		    selectSqlPrv.append("  							,D.*                                                                                                    \n");
		    selectSqlPrv.append("  					FROM (                                                                                                          \n");
		    selectSqlPrv.append("  							SELECT 	SUBSTR(E.SCHE_INFO,1,2) TYPE                                                                    \n");
		    selectSqlPrv.append("  									,E.SCHE_JOB_ID                                                                                  \n");
		    selectSqlPrv.append("  									,E.SCHE_JOB_NM                                                                                  \n");
		    selectSqlPrv.append("  									,E.SCHE_GRP                                                                                     \n");
		    selectSqlPrv.append("  									,E.NEXT_JOB_CLS                                                                                 \n");
		    selectSqlPrv.append("  									,E.SCHE_INFO                                                                                    \n");
		    selectSqlPrv.append("  									,DECODE(F.SCHE_GRP ,NULL ,E.EXEC_PARM,F.EXEC_PARM) EXEC_PARM                                    \n");
		    selectSqlPrv.append("  							FROM    X1_SCHEJOBINFO E, X1_SCHEDATE F                                                             	\n");
		    selectSqlPrv.append("  							WHERE	E.SCHE_GRP = F.SCHE_GRP(+)                                                                    	\n");
		    selectSqlPrv.append("  							AND		F.SCHE_DATE(+) = ? --��������                                                                               								\n");
		    selectSqlPrv.append("  					) D                                                                                                             \n");
		    selectSqlPrv.append("  			WHERE ( D.TYPE = '01' )                                                                                                 \n");
		    selectSqlPrv.append("  			OR 		( D.TYPE = '02' 	AND (SELECT BIZ_DT_CLS FROM X1_BIZDT WHERE BIZ_DT = ?) IN ('00','01','02') ) --��������             \n");
		    selectSqlPrv.append("  			OR 		( D.TYPE = '03' 	AND ? IN (SELECT SCHE_DATE FROM X1_SCHEDATE WHERE SCHE_GRP = D.SCHE_GRP) )   --��������             \n");
		    selectSqlPrv.append("  			) A                                                                                                                     \n");
		    selectSqlPrv.append("  			WHERE	  (SUBSTR(A.SCHE_INFO,3,4) <= '2500' OR (SUBSTR(A.SCHE_INFO,3,4) = '____' AND A.NEXT_JOB_CLS = 'Y'))            \n");
		    selectSqlPrv.append(")                                                                               													\n");

			pstmt3 = con.prepareStatement(selectSqlPrv.toString());
			while(rs2.next()){
				sScheJobId = rs2.getString("SCHE_JOB_ID");
				pstmt3.setString(1,sScheJobId);
				pstmt3.setString(2,sBizDate);
				pstmt3.setString(3,sBizDate);				
				pstmt3.setString(4,sBizDate);
				pstmt3.setString(5,sBizDate);
				rs3 = pstmt3.executeQuery();
				//*************************************************************************************//
				//�����۾��� �������� �ʴ� ��� �ش� ��ġ�۾��� �����Ų��
				//�����۾��� �����ϴ��� ���� ����Ǿ�� �ϴ� �������� �ƴ� ���� �������ǿ��� �����Ѵ�.
				//*************************************************************************************//
				if(!rs3.next()){
					System.out.println(sScheJobId + "�� �����մϴ�.");
					sa = FormatData.strSplit((String)rs2.getString("EXEC_PARM"),",");
					if (sa != null){ 
						for (int i=0;i<sa.length;i++) {
							if (sa[i].startsWith("%") && sa[i].endsWith("%")) {
								sa[i] = getConstValue(sa[i].substring(1,sa[i].length()-1));
							}  
						}
					}else{
						System.out.println("This Job have not Parameter....");
					}
					oa = new Object[3];
					oa[0] = (String)rs2.getString("SCHE_JOB_ID");
					oa[1] = "000";
					oa[2] = sa;
					String className = rs2.getString("EXEC_CLASS");
					String execMethod = rs2.getString("EXEC_METHOD");
					System.out.println("====================================================");
					System.out.println("className[" + className + "]" + "execMethod[" + execMethod + "]");

					try{
						AsyncCaller ac = new AsyncCaller(rs2.getString("EXEC_CLASS"),rs2.getString("EXEC_METHOD"),oa);
						t = new Thread(ac);
						t.start();
					}catch(Exception e){
						System.out.println("Error" + e.toString());  
					}
/////////////////////////////////////////////////////////////////////////////////
					System.out.println(rs2.getString("SCHE_JOB_ID") + " , " + rs2.getString("SCHE_JOB_NM") + " , " + rs2.getString("EXEC_CLASS") + " , " + rs2.getString("EXEC_METHOD") + " , " + rs2.getString("EXEC_PARM"));
				}
				//���������� ���������� ���� ����Ǿ�� �ϴ� ���� �ƴѰ��� ���������� �����Ǿ��,
				//���������ʹ� ������� �����ϵ��� �Ѵ�.
				else{
					System.out.println(sScheJobId + " has PRV_JOB");
					StringBuffer prvExptSB = new StringBuffer("");
				}  
			}//end of while
		} catch (DBException dbe) {
			System.out.println(dbe.toString());
			Log.writeLog(sSystem, sClassName, "getJob()", Log.LOG_CRITICAL, dbe.toString());
			dbe.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.toString());
			Log.writeLog(sSystem, sClassName, "getJob()", Log.LOG_CRITICAL, "Exception �߻�");
		}finally {
			if ( rs2 != null ) try {rs2.close();}catch(Exception e){}
			if ( pstmt2 != null ) try {pstmt2.close();}catch(Exception e){} 
			if ( rs3 != null ) try {rs3.close();}catch(Exception e){}
			if ( pstmt3 != null ) try {pstmt3.close();}catch(Exception e){} 
		}
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  "ScheMgrSA.getJob Finish");
		return;
	}

	/** �ڵ������� Thread�� �����Ѵ�.
	  */
	private void runSche() {
		try {
			new RunSche().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "runSche()", Log.LOG_CRITICAL, "runSche()ó���� �������߻��߽��ϴ�.");
			System.out.println(e.toString());
		}
	}

	/** ������ �����ϸ�, 1�д����� �۾������� �������۾��� �����Ű�� ����  Thread
	  */
	class RunSche extends Thread {
		public void run() {
			int actionMinute = -1;
			System.out.println("RunSche Thread Start");
			Calendar c = Calendar.getInstance();
			ScheMgrSA sm = new ScheMgrSA();
			try {
				String sa[] = null;
				Object oa[] = {"KCX10630", "000", sa};
				SyncCaller ac = new SyncCaller();
				ac.callString("kiis.biz.kc.x1.batch.CManageConst","run",oa);	
				System.out.println("Const updated.......");				
				while (true) {
					c.setTime(new java.sql.Date(System.currentTimeMillis()));
					if (actionMinute != c.get(Calendar.MINUTE)){
					  	try{
					  		sm.getJob(FormatData.numToStr(c.get(Calendar.HOUR_OF_DAY),2,"0") + FormatData.numToStr(c.get(Calendar.MINUTE),2,"0"));
					  	}catch(Exception e){
					  		System.out.println(e.toString());  
					  	}
					  	actionMinute = c.get(Calendar.MINUTE);
					}else {
					  	System.out.println("now - " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
					}
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				System.out.println("[RunSche Thread] Exception!!!!!!!!!!!");
				e.printStackTrace();
			}
			System.out.println("RunSche Thread Finish");
		}
	}

	private String getConstValue(String sConstCd) throws Exception{

    ResultSet rs = null;
    PreparedStatement pstmt0 = null;
    String sConstValue = null;

    try{      
    	pstmt0 = con.prepareStatement("SELECT CONST_VALUE FROM X1_CONST WHERE CONST_CD = ?");
    	pstmt0.setString(1,sConstCd);
  		rs = pstmt0.executeQuery();
  		while(rs.next()){
  			sConstValue = rs.getString("CONST_VALUE");  
  		}
  	}finally {
        if ( rs != null ) try {rs.close();}catch(Exception e){}
        if ( pstmt0 != null ) try {pstmt0.close();}catch(Exception e){} 
    }
    return sConstValue;  
}

	/** ������ �����ϸ�, 1�д����� �۾������� �������۾��� �����Ű�� ���� Main���
	*/
	public static void main(String[] args) {
		//DB ����
		ScheMgrSA sm = new ScheMgrSA();

		try{
			sm.init();
		}catch(Exception e){
			System.out.println(e.toString());  
		}
		sm.runSche();
		System.out.println("Started Scheduler :" + new java.sql.Timestamp(System.currentTimeMillis()));
	}
}