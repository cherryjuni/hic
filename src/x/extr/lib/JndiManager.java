/** Main system          : x.extr
  * Sub system           : lib
  * Classname            : JndiManager.java
  * Initial date         : 2005.11.17
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : Jndi 처리관련 공통 Module
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.lib;

import java.util.*;
import java.io.*;
import x.extr.log.Log;
import x.extr.exception.GeneralException;
import x.extr.lib.ErrorConstants;



/** EJB클래스의 컨텍스트 이름을 관리하는 클래스이다.
  * 싱글리톤으로 초기에 한번만 기동한다.
  * @author 황재천
*/
public class JndiManager extends x.extr.lib.Properties {
	private static final boolean verbose = true;
	private static x.extr.lib.JndiManager m_instance;

	/** Constructor JndiManager
	*/
	private JndiManager() { }


	/** Init() 메소드를 통해 생성된 JndiManager객체를 얻는다
	 * @return JndiManager
	 * @throws Exception
	*/
	public static JndiManager getInstance() throws Exception {
		if (m_instance == null) {
			m_instance = new JndiManager();
			m_instance.init();
		}
		return m_instance;
	}


	/** 싱글리톤을 초기화한다.(JndiManager객체를 생성한다)
	 * @throws GeneralException
	 */
	public void init() throws GeneralException {
		if (m_instance == null) {
			m_instance = new JndiManager();
		}

		try {
			PropertyManager pm = PropertyManager.getInstance();

			// PropertyManager 에 기록된 값을 가지고 온다
			String propPath =(String)pm.get("JNDI_PROPERTIES");
			if (propPath == null || propPath.length() <= 0)
				propPath = "jndi.properties";

			m_instance.loadFromFile(propPath);
		} catch (Exception e) {
			// 세부시스템이름, 클래스이름, 메소드이름, 로그종류, 뿌리고자 하는 메세지
			Log.writeLog("Com", "JndiManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			throw new GeneralException("JndiManager : init() Exception error..");
		}
	}


	/** JndiManager 싱글리톤을 리셑한다.
	 * @throws GeneralException
	*/
	public static void reset() throws GeneralException {
		if (m_instance == null) {
			m_instance = new JndiManager();
		}

		try {
			PropertyManager pm = PropertyManager.getInstance();

			// PropertyManager 에 기록된 값을 가지고 온다
			String propPath =(String)pm.get("JNDI_PROPERTIES");
			if (propPath == null || propPath.length() <= 0)
				propPath = "jndi.properties";

			m_instance.clear();
			m_instance.loadFromFile(propPath);
		} catch (Exception e) {
			// 세부시스템이름, 클래스이름, 메소드이름, 로그종류, 뿌리고자 하는 메세지
			Log.writeLog("Com", "JndiManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			throw new GeneralException("JndiManager : reset() Exception error..");
		}
	}


}