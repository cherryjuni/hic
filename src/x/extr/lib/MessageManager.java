/** Main system          : x
  * Sub system           : lib
  * Classname            : MessageManager.java
  * Initial date         : 2005.11.14
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : Message 처리관련 공통 Module
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



/** 예외 메세지를 관리하는 클래스이다.
 * 싱글리톤으로 초기에 한번만 기동한다.
 * @author 황재천
*/
public class MessageManager extends x.extr.lib.Properties {
	private static final boolean verbose = true;
	private static MessageManager m_instance;
	protected HashMap m_msgMap;
	protected HashMap m_msgTypeMap;

	/** MessageManager의 Constructor
	*/
	private MessageManager() {
		m_msgMap = new HashMap();
		m_msgTypeMap = new HashMap();
		m_instance = null;
	}

	/** Init() 메소드를 통해 생성된 MessageManager객체를 얻는다
	 * @return MessageManager
	 * @throws Exception
	*/
	public static MessageManager getInstance() throws Exception {
		if (m_instance == null) {
			m_instance.init();
		}
		return m_instance;
	}

	/** 싱글리톤을 초기화한다.( MessageManager객체를 생성한다)
	 * @throws GeneralException
	*/
	public void init() throws GeneralException {
		if (m_instance == null) {
			m_instance = new MessageManager();
		}
		
		try {
//			Properties 파일에서 message.properties의 파일 위치를 알아옴
			PropertyManager pm = PropertyManager.getInstance();
//      	pm.init();

			String propPath = (String)pm.get("MESSAGE_PROPERTIES");
			if (propPath == null || propPath.length() <= 0) {
				propPath = "message.properties";
			}
//			message.properties에 저장되어 있는 모든 내용을 properties 파일에 담아 놓는다.
			m_instance.loadFromFile(propPath);
			m_instance.convertProperty();
			//m_instance.dbConvertProperty();
		} catch (Exception e) {
			Log.writeLog("Comm", "MessageManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			e.printStackTrace();
			throw new GeneralException("MessageManager : init() Exception error..");
		}
	}


	/** message.properties 파일에 정의된 메시지를 얻어온다.
	 * @param msgCode
	 * @return String
	 * @throws Exception
	*/
	public String getMessage(String msgCode) throws Exception {
		String returnMsg = null;

		//java.util.Properties의 getProperty, msgCode에 해당하는 String type의 value를 return
		return getProperty(msgCode);
	}


	/** msgCode에 해당하는 msg String[] 리턴
	 * @param msgCode
	 * @return String[]
	 * @throws Exception
	*/
	private String[] getMsgs(String msgCode) throws Exception {
		// m_msgMap에서 msgCode를 키로
		// msgContent를 읽어서 리턴
		String msg[] = new String[2];
		
		try {
			//m_msgMap에서 magCode에 해당하는 값을 msg[0]에 입력
			msg[0] = (String)m_msgMap.get(msgCode);
			//m_msgTypeMap에서 magCode에 해당하는 값을 msg[1]에 입력
			msg[1] = (String)m_msgTypeMap.get(msgCode);
		} catch (NullPointerException e) {
			throw new Exception("not found Code :" + msgCode + " !!");
		}
		return msg;
	}


	/** Enumeration에 properyName을 저장하고 propertyName에 해당하는 value를 뽑아내어 m_instance에 name,value로 저장
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


	/** DB에서 데이터를 가져와서 m_instance에 name, value로 저장
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


	/** 한글화 처리 ACSII 타입을 KSC5601로 변환
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


	/** 한글을 ASCII로 변환
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


	/** MessageManager 싱글리톤을 리셑한다.
	 * @throws GeneralException
	*/
	public static void reset() throws GeneralException {
		if (m_instance == null) {
			m_instance = new MessageManager();
		}
		
		try {
			//Properties 파일에서 message.properties의 파일 위치를 알아옴
			PropertyManager pm = PropertyManager.getInstance();

			String propPath = (String)pm.get("MESSAGE_PROPERTIES");
			if (propPath == null || propPath.length() <= 0) {
				propPath = "message.properties";
			}
			//message.properties에 저장되어 있는 모든 내용을 properties 파일에 담아 놓는다.
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
