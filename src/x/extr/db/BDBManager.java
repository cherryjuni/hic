/** Main system          : x
  * Sub system           : db
  * Classname            : BDBManager.java
  * Initial date         : 2005.10.07
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : 배치전용 DBManager
  */
package x.extr.db;

import java.sql.*;
import javax.naming.*;
import java.text.*;
import java.sql.Statement;
import x.extr.lib.*;
import x.extr.db.*;
import x.extr.dao.*;
import x.extr.lib.PropertyManager;

public class BDBManager {
	/* DEBUG FINDING VERBOSE */
	private boolean DEBUG_VERBOSE = false;

	/** 
	  * default constructor
	  */
	public BDBManager()  {}

	/** (method) Get Connection Object.
     * @param  void void
	 * @return Connection
     * @throws Exception
     */
	public Connection getConnection() throws Exception 
	{	
		Connection con = null;
		PropertyManager pm = PropertyManager.getInstance();
		
		try 
		{
			//CONNECTION OBJECT를 얻기위한 변수
			String sODC = pm.getStr("ORACLE_DRIVER_CLASS");  
			String sJU  = pm.getStr("JDBC_URL");             
			String sBDU = pm.getStr("BATCH_DB_USER");        
			String sBDP = pm.getStr("BATCH_DB_PASSWORD");    
			System.out.println("**********************************************");
			System.out.println("**********************************************");
			System.out.println("*   sODC  : [" + sODC + "]");
			System.out.println("*   sJU   : [" + sJU + "]");
			System.out.println("*   sBDU  : [" + sBDU + "]");
			System.out.println("*   sBDP  : [" + sBDP + "]");
			System.out.println("**********************************************");
			System.out.println("**********************************************");

			//getting class
			Class.forName(sODC);				
			//DB connection
			con = DriverManager.getConnection(sJU, sBDU, sBDP);
			con.setAutoCommit(false);
		} 
		catch(SQLException sqle) 
		{
			throw sqle;
		} 
		catch(Exception e) 
		{
			throw e;
		}
		return con;
	}
}