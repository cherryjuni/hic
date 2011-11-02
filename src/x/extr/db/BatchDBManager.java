/** Main system          : x
  * Sub system           : db
  * Classname            : BatchDBManager.java
  * Initial date         : 2005-11-14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ��ġ���α׷��� DB IO�� �����ϴ� class
  *                        ������ DBManager class�� WAS�� ���� DB connection�� ȹ���ϹǷ� ��ġ�ʹ� ���� �ʾ�
  *                        DB connection�� ��� �κи��� overriding�Ͽ� ����, ���� ���α׷����� ȣȯ���� ���� ������ �޼ҵ�� �����Ѵ�
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
	//���� property���� �����;� ��
	public static final String DB2_DRIVER_CLASS 	   = "COM.ibm.db2.jdbc.app.DB2Driver";            	//db2 driver class��
	public static final String DB2_JDBC_URL            = "jdbc:db2:cbtest";       						//jdbc url
	public static final	String DB2_BATCH_DB_USER       = "zuser";                                       //db ������
	public static final	String DB2_BATCH_DB_PASSWORD   = "zuser";                                       //db ���� �н�����	
	
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

	/** DB connection�� ��� �޼ҵ�
	  * Connection �κ��� overriding�Ѵ�
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

	/** DB connection�� ��� �޼ҵ�
	  * connection �κ��� overriding�Ѵ�
	  * @return Connection
	  */
	public Connection getConnection(String dataSource) throws DBException {
		return this.getConnection();
	}

	/**
	 * JNDI context�� ��´�.
	 * DBManager class�� ����ϱ����� dummy�� overriding�Ѵ�
	 * @return Context null
	 */
	public Context getInitialContext() {
		return null;
	}

}