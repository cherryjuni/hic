/** Main system          : x
  * Sub system           : db
  * Classname            : DBManager.java
  * Initial date         : 2005.11.15
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DB 처리관련 공통 Module
*/

package x.extr.db;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import java.text.*;
import java.util.Vector;
import java.sql.Statement;
import x.extr.lib.PropertyManager;
import x.extr.util.FormatData;
import x.extr.db.*;
import x.extr.exception.*;


/** 데이터 베이스 관련 처리에 대한 공통적인 기반을 제공하는 Class입니다.
    @author 황재천
*/
public class DBManager {
	/* 디버깅모드 정의 */
	private boolean DEBUG_VERBOSE = false;

	public Context ctx;
	private String db_source;

	/**
	 * @ Constructor
	*/
	public DBManager() {
		try {
			ctx = getInitialContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @ Constructor
	 * @param DB_SOURCE
	*/
	public DBManager(String DB_SOURCE) {
		this.db_source = DB_SOURCE;
		try {
			ctx = getInitialContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Context 객체를 얻는다.
	 * @return	Context
	 * @throws javax.naming.NamingException
	*/
	public Context getContext() throws javax.naming.NamingException {
		Context ctx = null;
		return  ctx = new InitialContext();
	}


	/** 웹로직에서 제공하는 JDBC 를 사용하여 Connection 을 얻는다.
	 * @return Connection
	 * @throws DBException
	*/
	public Connection getConnection() throws DBException {
		Connection con = null;
		try {
			//PropertyManager pm = PropertyManager.getInstance();
			//javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(pm.getStr(this.db_source));
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup("cabisDS");
			con = ds.getConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return con;
	}


	/** DB_SOURCE에 해당하는 DB에 연결하는 Connection을 얻는다.
	 * @param DB_SOURCE
	 * @return Connection
	 * @throws DBException
	*/
	public Connection getConnection(String DB_SOURCE) throws DBException {
		Connection con = null;
		try {
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(DB_SOURCE);
			con = ds.getConnection();
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}

		return con;
	}


	/** Connection 객체가 auto commit하지않는 경우에 명시적으로 commit하는 데 사용.
	 * @param con    Connection
	 * @throws DBException
	*/
	public void commit(Connection con) throws DBException {
		try {
			con.commit();
		} catch(Exception e) {
			throw new DBException(e.getMessage());
		}
	}


	/**
	 * 트랜잭션 실패시 rollback 하는 데 사용.
	 * @param con   Connection
	 * @throws DBException
	*/
	public void rollback(Connection con) throws DBException {
		try {
			con.rollback();
		} catch(Exception e) {
			throw new DBException(e.getMessage());
		}
	}


	/**
	 * 데이타베이스의 사용을 끝내고 연결을 끊는 데 사용한다.
	 * @param con     Connection
	 * @throws DBException
	*/
	public void close(Connection con) throws DBException {
		try {
			con.close();
		} catch(Exception e) {
			throw new DBException(e.getMessage());
		}
	}


	/** DB_SOURCE를 설정한다.
	 * @param DB_SOURCE
	*/
	public void setDB_SOURCE(String DB_SOURCE) {
		this.db_source = DB_SOURCE;
	}


	/**
	 * JNDI context를 얻는다.
	 * @return Context
	 */
	public Context getInitialContext() {
		Context temp = null;

		try {
		  
		  /*
			PropertyManager pm = PropertyManager.getInstance();
			Properties h = new Properties();
			h.put(Context.INITIAL_CONTEXT_FACTORY, (String)(pm.getProperty("SYS_FACTORY")));
			h.put(Context.PROVIDER_URL, (String)(pm.getProperty("SYS_URL")));
			temp = new InitialContext(h);
			*/
			temp = new InitialContext();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return temp;
	}
}