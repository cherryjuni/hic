/** Main system          : foundation
  * Sub system           : lib
  * Classname            : BatchUtility.java
  * Initial date         : 2005-11-14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ��ƿ��Ƽ Ŭ����
  * Version information  : v 1.0
  * 
  */

package x.extr.lib;

import java.sql.*;
import java.lang.reflect.*;
import java.util.Calendar;
import x.extr.dao.*;
import x.extr.util.*;
import x.extr.db.*;
import x.extr.exception.*;


public final class BatchUtility {
	/** private constructor, cannot be instantiated
	  */
	private BatchUtility() {
	}
	
	/** DB���� �ڵ忡 ���� ������� �������� �޼ҵ�
	  * @param constCode String ��� �ڵ�
	  * @return String   ����ڵ忡 �����ϴ� ��
	  */
	public static String getConst(String constCode) throws DBException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String constValue = null;
		StringBuffer sql = new StringBuffer();

		BatchDBManager dbManager = new BatchDBManager();
		sql.append("SELECT CONST_VALUE FROM X1_CONST WHERE CONST_CD = ? ");
		try {
			conn = dbManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, constCode);
			rs = pstmt.executeQuery();
			constValue = rs.next() ? rs.getString(1) : "";
		} catch(SQLException e) {
			throw new DBException(e.toString());
		} finally {
			try { if(conn != null) conn.close(); pstmt.close(); } catch(Exception e) { System.out.println(e.toString()); }
		}

		return constValue;
	}	

	/** null fix utility
	  */
	public static void fixNull(Object o) {
		Field field[] = o.getClass().getFields();
		//System.out.println("field num = " + field.length);

		try {	
			for(int idx=0; idx<field.length; idx++) {
				if(field[idx].get(o) == null && field[idx].getType().getName().equals("java.lang.String")) {
					//System.out.println("fix null : " + idx + "  " + field[idx].getName());
					field[idx].set(o, "");
				}
				//else System.out.println("value is " + idx + "  " + String.valueOf(field[idx].get(o)));
			}
		} catch(Exception e) {}
	}

	/** print DM class
	  */
	public static void printDM(Object o) {
		Field field[] = o.getClass().getFields();

		try {
			for(int idx=0; idx<field.length; idx++) System.out.println((idx+1) + " : " + field[idx].getName() + " = [" + String.valueOf(field[idx].get(o)) + "]");
		} catch(Exception e) { System.out.println(e.toString()); }
	}	

	//��¥���� utility ================================================================================================

	//=================================================================================================================

	/** �Է¹��� ��¥�� ���� ���ϴ� �����ϼ������� �������� ���ϴ� �޼ҵ�
	  @param from      ���� ��¥
	  @param date      ��� �ϼ�
	  @param mode      �����ϱ���(���࿵���ϱ����̸� true, ĳ��Ż�����ϱ����̸�  false)
	  @return String   
	*/
	public static String calcBizDtUsingBizCount(String from, int bizDate, boolean mode) {
		StringBuffer selectSql;
		PreparedStatement sql = null;
		ResultSet rs = null;
		String sMode = "";

		Connection con = null;

		BatchDBManager dbManager = new BatchDBManager();

		if (mode) {
			sMode = "'00','01'";
		} else {
			sMode = "'00','01','02'";
		}


		if ( bizDate >= 0 ) {
			selectSql = new StringBuffer("");
			selectSql.append("SELECT 	MAX(BIZ_DT) BIZDT                                 \n");
			selectSql.append("FROM  (                                                   \n");
			selectSql.append("		SELECT 	BIZ_DT                                        \n");
			selectSql.append("		FROM 	  X1_BIZDT                                      \n");
			selectSql.append("		WHERE 	BIZ_DT  > ?                                   \n");
			selectSql.append("		AND 	BIZ_DT_CLS = '00'                               \n");
			if (mode){
				selectSql.append("		AND 	TO_CHAR(TO_DATE(BIZ_DT,'YYYYMMDD'),'D') <> '7'  \n");
			}
			selectSql.append("		ORDER BY 1  )                                         \n");
			selectSql.append("WHERE ROWNUM <= ?                                         \n");
		} else {
			selectSql = new StringBuffer("");
			selectSql.append("SELECT MIN(BIZ_DT) BIZDT FROM (                                 \n");
			selectSql.append("SELECT BIZ_DT FROM X1_BIZDT                                \n");
			selectSql.append("WHERE BIZ_DT  < ?                                          \n");
			selectSql.append("AND BIZ_DT_CLS = '00'                                      \n");
			if (mode){
				selectSql.append("AND TO_CHAR(TO_DATE(BIZ_DT,'YYYYMMDD'),'D') <> '7'         \n");
			}
			selectSql.append("ORDER BY 1 DESC)                                           \n");
			selectSql.append("WHERE ROWNUM <= ?                                          \n");
		}

		try {
			con = dbManager.getConnection();
			sql = con.prepareStatement(selectSql.toString());

			int idx=0;
			
			sql.setString(++idx, from);
			sql.setInt(++idx, Math.abs(bizDate));
			rs = sql.executeQuery();
			return (rs.next()) ? rs.getString("BIZDT") : null;
		} catch (DBException dbe) {
			System.out.println(dbe.toString());			
			return null;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		} finally {
			try { 
				if(rs != null) rs.close();
				if(sql != null) sql.close();
				if(con != null) con.close();
			} catch(Exception e) { System.out.println(e.toString()); }
		}
	}

	/** �Է¹��� ��¥�� ���� ���ϴ� �ϼ������� �������� ���ϴ� �޼ҵ�
	  @param from      ���� ��¥
	  @param yaer      ��� ���
	  @param month     ��� ����
	  @param date      ��� �ϼ�
	  @return String   
	*/
	public static String calcBizDt(String from, int year, int month, int date) {
		String to = DateUtils.calcDate(from,year,month,date);
		StringBuffer selectSql;
		PreparedStatement sql = null;
		ResultSet rs = null;
		Connection con = null;

		BatchDBManager dbManager = new BatchDBManager();
		
		selectSql = new StringBuffer("select min(biz_dt) BIZDT from x1_bizdt ");
		selectSql.append("where biz_dt >= ? and biz_dt_cls like '0%'");

		try {

			con = dbManager.getConnection();
			sql = con.prepareStatement(selectSql.toString());
			sql.setString(1, to);
			rs = sql.executeQuery();

			return (rs.next()) ? rs.getString("BIZDT") : null;
		} catch (DBException dbe) {
			System.out.println(dbe.toString());			
			return null;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		} finally {
			try { 
				if(rs != null) rs.close();
				if(sql != null) sql.close();
				if(con != null) con.close();
			} catch(Exception e) { System.out.println(e.toString()); }
		}
	}

	/** From Date�� to Date���� �ϼ��� ���
	  @param from      ������(YYYYMMDD)
	  @param yaer      ������(YYYYMMDD)
	  @return String   
	*/
	public static String getDaysBetween(String from, String to ) {
		StringBuffer selectSql;
		PreparedStatement sql = null;
		ResultSet rs = null;

		Connection con = null;
		BatchDBManager dbManager = new BatchDBManager();
		selectSql = new StringBuffer("");
		selectSql.append("select (to_date(?,'YYYYMMDD') - to_date(?,'YYYYMMDD')) from dual");
		
		try {
			con = dbManager.getConnection();
			sql = con.prepareStatement(selectSql.toString());
			sql.setString(1, to);
			sql.setString(2, from);  
			rs = sql.executeQuery();

			return (rs.next()) ? rs.getString("BIZDT") : null;
		} catch (DBException dbe) {
			System.out.println(dbe.toString());			
			return null;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		} finally {
			try { 
				if(rs != null) rs.close();
				if(sql != null) sql.close();
				if(con != null) con.close();
			} catch(Exception e) { System.out.println(e.toString()); }
		}
	}

	/** ���� �ð��� ��-��-�� �ð�:��:�� �� �����Ѵ�
	  * @return String ����ð�
	  */
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		StringBuffer time = new StringBuffer();

		//�⵵		
		time.append(String.valueOf(cal.get(Calendar.YEAR)));
		//��
		time.append("-").append(String.valueOf(cal.get(Calendar.MONTH)+1));
		//��
		time.append("-").append(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));		
		//�ð�
		time.append(" ").append(String.valueOf(cal.get(Calendar.HOUR) + ((cal.get(Calendar.AM_PM) == Calendar.PM) ? 12 : 0)));
		//��
		time.append(":").append(String.valueOf(cal.get(Calendar.MINUTE)));
		//��
		time.append(":").append(String.valueOf(cal.get(Calendar.SECOND)));

		return  time.toString();
	}

	/** �Է¹��� ���ڷ� ���� �Է¹޴� ���� ���� ���� ������ �������� ���ϴ� �޼ҵ�
	  @param from      ���� ��¥(yyyymmdd)
	  @param yaer      ���ϰ��� �ϴ� ����(...,-2,-1,0,1,2...)
	  @return String   ������ ������
	*/

	public static String getLastBizDtAddMonth(String from, int month) {
		StringBuffer selectSql;
		PreparedStatement sql = null;
		ResultSet rs = null;
		Connection con = null;
		BatchDBManager dbManager = new BatchDBManager();
		String sBizDt = null;
		selectSql = new StringBuffer("");
		selectSql.append("");
		selectSql.append("SELECT 	MAX(BIZ_DT) BIZ_DT                                                                  \n");
		selectSql.append("FROM 		X1_BIZDT                                                                            \n");
		selectSql.append("WHERE 	BIZ_DT >= TO_CHAR(ADD_MONTHS(TO_DATE(?,'YYYYMMDD'),?),'YYYYMM') || '01'   \n");
		selectSql.append("AND			BIZ_DT <= TO_CHAR(ADD_MONTHS(TO_DATE(?,'YYYYMMDD'),?),'YYYYMM') || '31'   \n");
		selectSql.append("AND			BIZ_DT_CLS IN ('00','01','02')                                                      \n");
		selectSql.append("ORDER BY BIZ_DT																																							\n");

		try {
			con = dbManager.getConnection();
			sql = con.prepareStatement(selectSql.toString());
			sql.setString(1,from);
			sql.setInt(2,month);
			sql.setString(3,from);
			sql.setInt(4,month);			
			rs = sql.executeQuery();
			if (rs.next()){
			  sBizDt = rs.getString("BIZ_DT");
			}
		} catch (DBException dbe) {
			System.out.println(dbe.toString());
			return null;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		} finally {
			try { 
				if(rs != null) rs.close();
				if(sql != null) sql.close();
				if(con != null) con.close();
			} catch(Exception e) { System.out.println(e.toString()); }
		}

		return sBizDt;
	}
}