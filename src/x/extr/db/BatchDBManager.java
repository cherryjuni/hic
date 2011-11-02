/** Main system          : x
  * Sub system           : db
  * Classname            : BatchDBManager.java
  * Initial date         : 2005-11-14
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : 배치프로그램의 DB IO를 지원하는 class
  *                        기존의 DBManager class는 WAS를 통해 DB connection을 획득하므로 배치와는 맞지 않아
  *                        DB connection을 얻는 부분만을 overriding하여 구현, 기존 프로그램과의 호환성을 위해 나머지 메소드는 존속한다
  * Version information  : v 1.0
  * 
  */

package x.extr.db;

import java.sql.*;
import javax.naming.*;

import x.extr.lib.*;
import x.extr.db.*;
import x.extr.dao.*;
import x.extr.exception.*;

public class BatchDBManager extends DBManager {
	//차후 property에서 가져와야 함
	public static final String DB2_DRIVER_CLASS 	   = "COM.ibm.db2.jdbc.app.DB2Driver";            	//db2 driver class명
	public static final String DB2_JDBC_URL            = "jdbc:db2:cbtest";       						//jdbc url
	public static final	String DB2_BATCH_DB_USER       = "zuser";                                       //db 유저명
	public static final	String DB2_BATCH_DB_PASSWORD   = "zuser";                                       //db 유저 패스워드	
	
	/** constructor
	  * default constructor
	  */
	public BatchDBManager() {
		super();
	}

	/** constructor
	  * dummy constructor
	  */
	public BatchDBManager(String dataSource) {
		super();	
	}

	/** DB connection을 얻는 메소드
	  * Connection 부분을 overriding한다
	  * @return Connection
	  */
	public Connection getConnection() throws DBException {	
		Connection con = null;

		try {
			Class.forName(DB2_DRIVER_CLASS);
			con = DriverManager.getConnection(DB2_JDBC_URL, DB2_BATCH_DB_USER, DB2_BATCH_DB_PASSWORD);
			return con;
		} catch(ClassNotFoundException e) {
			throw new DBException(e.toString());
		} catch(SQLException e) {
			throw new DBException(String.valueOf(e.getErrorCode()), "Getting connection failed!\n" + e.toString());
		} catch(Exception e) {
			throw new DBException("Getting connection failed!\n" + toString());
		}
	}

	/** DB connection을 얻는 메소드
	  * connection 부분을 overriding한다
	  * @return Connection
	  */
	public Connection getConnection(String dataSource) throws DBException {
		return this.getConnection();
	}

	/**
	 * JNDI context를 얻는다.
	 * DBManager class를 사용하기위해 dummy로 overriding한다
	 * @return Context null
	 */
	public Context getInitialContext() {
		return null;
	}

}