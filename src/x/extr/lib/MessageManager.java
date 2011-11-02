/** Main system          : x
  * Sub system           : lib
  * Classname            : MessageManager.java
  * Initial date         : 2005.11.14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : Message ó������ ���� Module
  * Version information  : v 1.0
  *
  */

package x.extr.lib;

import java.util.*;
import java.io.*;
import java.sql.*;
import x.extr.exception.DBException;
import x.extr.exception.GeneralException;
import x.extr.lib.ErrorConstants;
import x.extr.log.Log;
import x.extr.dao.MessageDAO;



/** ���� �޼����� �����ϴ� Ŭ�����̴�.
 * �̱۸������� �ʱ⿡ �ѹ��� �⵿�Ѵ�.
 * @author Ȳ��õ
*/
public class MessageManager extends x.extr.lib.Properties {
	private static final boolean verbose = true;
	private static MessageManager m_instance;
	protected HashMap m_msgMap;
	protected HashMap m_msgTypeMap;

	/** MessageManager�� Constructor
	*/
	private MessageManager() {
		m_msgMap = new HashMap();
		m_msgTypeMap = new HashMap();
		m_instance = null;
	}

	/** Init() �޼ҵ带 ���� ������ MessageManager��ü�� ��´�
	 * @return MessageManager
	 * @throws Exception
	*/
	public static MessageManager getInstance() throws Exception {
		if (m_instance == null) {
			m_instance.init();
		}
		return m_instance;
	}

	/** �̱۸����� �ʱ�ȭ�Ѵ�.( MessageManager��ü�� �����Ѵ�)
	 * @throws GeneralException
	*/
	public void init() throws GeneralException {
		if (m_instance == null) {
			m_instance = new MessageManager();
		}
		
		try {
//			Properties ���Ͽ��� message.properties�� ���� ��ġ�� �˾ƿ�
			PropertyManager pm = PropertyManager.getInstance();
//      	pm.init();

			String propPath = (String)pm.get("MESSAGE_PROPERTIES");
			if (propPath == null || propPath.length() <= 0) {
				propPath = "message.properties";
			}
//			message.properties�� ����Ǿ� �ִ� ��� ������ properties ���Ͽ� ��� ���´�.
			m_instance.loadFromFile(propPath);
			m_instance.convertProperty();
			//m_instance.dbConvertProperty();
		} catch (Exception e) {
			Log.writeLog("Comm", "MessageManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			e.printStackTrace();
			throw new GeneralException("MessageManager : init() Exception error..");
		}
	}


	/** message.properties ���Ͽ� ���ǵ� �޽����� ���´�.
	 * @param msgCode
	 * @return String
	 * @throws Exception
	*/
	public String getMessage(String msgCode) throws Exception {
		String returnMsg = null;

		//java.util.Properties�� getProperty, msgCode�� �ش��ϴ� String type�� value�� return
		return getProperty(msgCode);
	}


	/** msgCode�� �ش��ϴ� msg String[] ����
	 * @param msgCode
	 * @return String[]
	 * @throws Exception
	*/
	private String[] getMsgs(String msgCode) throws Exception {
		// m_msgMap���� msgCode�� Ű��
		// msgContent�� �о ����
		String msg[] = new String[2];
		
		try {
			//m_msgMap���� magCode�� �ش��ϴ� ���� msg[0]�� �Է�
			msg[0] = (String)m_msgMap.get(msgCode);
			//m_msgTypeMap���� magCode�� �ش��ϴ� ���� msg[1]�� �Է�
			msg[1] = (String)m_msgTypeMap.get(msgCode);
		} catch (NullPointerException e) {
			throw new Exception("not found Code :" + msgCode + " !!");
		}
		return msg;
	}


	/** Enumeration�� properyName�� �����ϰ� propertyName�� �ش��ϴ� value�� �̾Ƴ��� m_instance�� name,value�� ����
	*/
	public void convertProperty() {
		String argName = null;
		String argValue = null;
		Enumeration enum1= m_instance.propertyNames();
		while ( enum1.hasMoreElements() ) {
			argName = (String)enum1.nextElement();
			argValue = (String)m_instance.getProperty(argName);

			try {
				m_instance.setProperty(argName, argValue);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	/** DB���� �����͸� �����ͼ� m_instance�� name, value�� ����
	 * @throws DBException
	 * @throws Exception
	*/
	public void dbConvertProperty() throws DBException, Exception {
		MessageDAO msgDAO = new MessageDAO();
		ResultSet rs = msgDAO.queryMsg();
		String argName = null;
		String argValue = null;

		try {
			while (rs.next()) {
				argName = rs.getString("MSG_CD");
				argValue = rs.getString("MSG_TXT");
				m_instance.setProperty(argName,HanToAsc(argValue));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}


	/** �ѱ�ȭ ó�� ACSII Ÿ���� KSC5601�� ��ȯ
	 * @param strSrc
	 * @return String
	*/
	public static String strToKorean(String strSrc) {
		String strDest = "";
		try {
//			strDest = new String(strSrc.getBytes("8859_1"), "KSC5601");
			strDest = new String(strSrc.getBytes("8859_1"), "UTF-8");
		} catch(UnsupportedEncodingException ueEx) {
			ueEx.getMessage();
		}
		return strDest;
	}


	/** �ѱ��� ASCII�� ��ȯ
	 * @param strSrc
	 * @return String
	 */
	public static String HanToAsc(String strSrc) {
		String strDest = "";
		try {
//			strDest = new String(strSrc.getBytes("KSC5601"), "8859_1");
			strDest = new String(strSrc.getBytes("UTF-8"), "8859_1");
		} catch(UnsupportedEncodingException ueEx) {
			ueEx.getMessage();
		}
		return strDest;
	}


	/** MessageManager �̱۸����� ���V�Ѵ�.
	 * @throws GeneralException
	*/
	public static void reset() throws GeneralException {
		if (m_instance == null) {
			m_instance = new MessageManager();
		}
		
		try {
			//Properties ���Ͽ��� message.properties�� ���� ��ġ�� �˾ƿ�
			PropertyManager pm = PropertyManager.getInstance();

			String propPath = (String)pm.get("MESSAGE_PROPERTIES");
			if (propPath == null || propPath.length() <= 0) {
				propPath = "message.properties";
			}
			//message.properties�� ����Ǿ� �ִ� ��� ������ properties ���Ͽ� ��� ���´�.
			m_instance.clear();
			m_instance.loadFromFile(propPath);
			m_instance.convertProperty();
			m_instance.dbConvertProperty();
		} catch (Exception e) {
			Log.writeLog("Comm", "MessageManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			e.printStackTrace();
			throw new GeneralException("MessageManager : reset() Exception error..");
		}
	}


}
