/** Main system          : x.extr
 * Sub system           : dao
 * Classname            : MessageDAO.java
 * Initial date         : 2005.11.15
 * Author               : 황재천
 * Update date/Modifier : 
 */

package x.extr.dao;

import java.sql.*;
import x.extr.db.*;
import x.extr.exception.*;
import x.extr.log.Log;

/** DB에서 Message를 읽어오는 Class입니다.
    @author 황재천
*/
public class MessageDAO 
{
	String sSystem = "Com";
	String sClassName = "MessageDAO";
	private DBManager dbManager;
	private BatchDBManager bdbManager;

	/**
	 * @ Constructor
	*/
	public MessageDAO() {
		//dbManager = new DBManager ("ORACLE_DATASOURCE_KIIS");
		bdbManager = new BatchDBManager ();
	}
	/**
	 * @ Constructor
	*/
	public MessageDAO(String dataSource) {
		dbManager = new DBManager (dataSource);
	}

	/** DB에 등록된 전체 메시지 목록을 리턴하는 메소드
	  @return ExResultSet
	*/
	public ResultSet queryMsg() {
		StringBuffer selectSql;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		

		selectSql = new StringBuffer("select msg_cd,msg_txt from xt_msg");

		try {
			con = bdbManager.getConnection();
			pstmt = con.prepareStatement(selectSql.toString());
			rs = pstmt.executeQuery();
			//SQLStatement sql = new SQLStatement (selectSql.toString());
			//rs = dbManager.select(sql);
		//} catch (DBException dbe) {
			// Sql ErrorCode를 가지고 핸들링 할 작업이 있으면 아래의 구문을 이용
		//	Log.writeLog(sSystem, sClassName, "queryMsg()", Log.LOG_CRITICAL, dbe.toString());
		//	rs = null;
		} catch (Exception e) {
			System.out.println("Msg DAO Exception --------");
			e.printStackTrace();
			//Log.writeLog(sSystem, sClassName, "queryMsg()", Log.LOG_CRITICAL, "Exception 발생");
			rs = null;
		}

		return rs;
	}

}