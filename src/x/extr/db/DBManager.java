/** Main system          : x
  * Sub system           : db
  * Classname            : DBManager.java
  * Initial date         : 2005.11.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : DB ó������ ���� Module
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


/** ������ ���̽� ���� ó���� ���� �������� ����� �����ϴ� Class�Դϴ�.
    @author Ȳ��õ
*/
public class DBManager {
	/* ������� ���� */
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

	/** Context ��ü�� ��´�.
	 * @return	Context
	 * @throws javax.naming.NamingException
	*/
	public Context getContext() throws javax.naming.NamingException {
		Context ctx = null;
		return  ctx = new InitialContext();
	}


	/** ���������� �����ϴ� JDBC �� ����Ͽ� Connection �� ��´�.
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


	/** DB_SOURCE�� �ش��ϴ� DB�� �����ϴ� Connection�� ��´�.
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


	/** Connection ��ü�� auto commit�����ʴ� ��쿡 ��������� commit�ϴ� �� ���.
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
	 * Ʈ����� ���н� rollback �ϴ� �� ���.
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
	 * ����Ÿ���̽��� ����� ������ ������ ���� �� ����Ѵ�.
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


	/** DB_SOURCE�� �����Ѵ�.
	 * @param DB_SOURCE
	*/
	public void setDB_SOURCE(String DB_SOURCE) {
		this.db_source = DB_SOURCE;
	}


	/**
	 * JNDI context�� ��´�.
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