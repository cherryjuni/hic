/** Main system          : foundation
  * Sub system           : lib
  * Classname            : PropertyManager.java
  * Initial date         : 2005.11.16
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : Property 처리관련 공통 Module
  * Version information  : v 1.0
  *
*/

package x.extr.lib;

import x.extr.log.Log;
import x.extr.exception.GeneralException;
import x.extr.lib.ErrorConstants;



/** 서버기동의 기본 환경을 위한 프로퍼티들을 관리하는 클래스이다.<p>
  * 서버이름 , 서버루트 , 로그, DB풀, Jdbc Driver등을 위한 프로퍼티 관리<p>
  * 싱글리톤으로 초기에 한번만 기동한다.<p>
  * @author 황재천
*/
public class PropertyManager extends x.extr.lib.Properties {
	private static final boolean verbose = true;
	private static x.extr.lib.PropertyManager m_instance;

	private static String CONF_FILE_DIR;
	private static final String CONF_FILE = "/kiis.properties";
	/* NT */
//	private static String CONF_FILE_DIR="c:/bea/wlserver6.1/config/kiisdomain/applications/DefaultWebApp/WEB-INF/classes/kiis/property";
//	private static final String CONF_FILE = "/kiis.properties";
	/* AIX */
//	private static String CONF_FILE_DIR="/weblogic/bea/wlserver6.1/config/kiisdomain/applications/DefaultWebApp/WEB-INF/classes/kiis/property";
//	private static String CONF_FILE = "/kiis.properties";
	/* SERVER DEFAULT */
//	private static String CONF_FILE_DIR ="";
//	private static final String CONF_FILE ="/exim.properties";
	/* Hdw NT */
//	private static String CONF_FILE_DIR="c:/DefaultWeb/serverclasses/exim/property";
//	private static String CONF_FILE = "/exim.properties";
	/* dev UNIX */
//	private static String CONF_FILE_DIR="/apps/dev/classes/exim/property";
//	private static String CONF_FILE = "/exim.properties";
	/* exim UNIX */
//	private static String CONF_FILE_DIR="/apps/exim/classes/exim/property";
//	private static String CONF_FILE = "/exim.properties";


	/** Constructor PropertyManager()
	*/
	private PropertyManager() {
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0) {
			//CONF_FILE_DIR="c:/bea/wlserver6.1/config/kiisdomain/applications/DefaultWebApp/WEB-INF/classes/kiis/property";
			CONF_FILE_DIR="d:/jeus42/webhome/servlet_home/webapps/x/WEB-INF/classes";
		} else {
			//CONF_FILE_DIR="/weblogic/bea/wlserver6.1/config/kiisdomain/applications/DefaultWebApp/WEB-INF/classes/kiis/property";
			CONF_FILE_DIR="/TMAX/jeus42/webhome/servlet_home/webapps/x/WEB-INF/classes";
		}
	}


	/** Init() 메소드를 통해 생성된 PropetyManager객체를 얻는다
	 * @return PropertyManager
	 * @throws Exception
	*/
	public static PropertyManager getInstance() throws Exception {
		/* PropertyManager의 인스턴스가 생성이 되어 있다면 생성된 인스턴스를 리턴
		 * 그렇지 않으면 새로운 인스턴스를 생성
		*/
		if (m_instance == null) {
			m_instance = new PropertyManager();				// 객체를 생성한다
			m_instance.init();								// 객체를 초기화 하기 위한 Init()실행
			return m_instance;								// 생성된 인스턴스를 돌려줌
		}
		return m_instance;
	}


	/** Init() 메소드를 통해 생성된 PropetyManager객체를 얻는다
	 * @param args         디렉토리
	 * @return PropertyManager
	 * @throws Exception
	 */
	public static PropertyManager getInstance(String args) throws Exception {
		if (m_instance == null) {
			m_instance = new PropertyManager();
			m_instance.init(args);
			return m_instance;
		}
		return m_instance;
	}


	/** 싱글리톤을 초기화한다.(PropertyManager객체를 생성한다)<p>
	 * 디렉터리를 새로이 ErpDir를 통해서 세팅하고자 하는경우
	 * @param ErpDir             디렉토리
	 * @throws GeneralException
	*/
	public void init(String ErpDir) throws GeneralException
	{
		/*
		 * 인스턴스가 존재하지않으면 생성
		 * 존재 한다면 다음단계로의 진행
		*/
		if (m_instance == null) {
			m_instance = new PropertyManager();
		}

		try {
			CONF_FILE_DIR = ErpDir;
			m_instance.loadFromFile(ErpDir + CONF_FILE);
		} catch (Exception e) {
			Log.writeLog("Comm", "PropertyManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			throw new GeneralException("PropertyManager : init() Exception error..");
		}
	}


	/** 싱글리톤을 초기화한다.(PropertyManager객체를 생성한다)
	 * 디렉터리를 내부에 설정해 놓고 사용하는 경우
	 * @throws GeneralException
	*/
	public void init() throws GeneralException {
		/*
		 * 인스턴스가 존재하지않으면 생성
		 * 존재 한다면 다음단계로의 진핼
		*/
		if (m_instance == null) {
			m_instance = new PropertyManager();
		}

		try {
			/*  */
			m_instance.loadFromFile(PropertyManager.CONF_FILE_DIR + CONF_FILE);
		} catch (Exception e) {
			throw new GeneralException("PropertyManager : init() Exception error..");
		}
	}


	/** PropertyManager 싱글리톤을 리셑한다.
	 * @throws GeneralException
	*/
	public static void reset() throws GeneralException {
		if (m_instance == null) {
			m_instance = new PropertyManager();
		}

		try {
			m_instance.clear();
			m_instance.loadFromFile(PropertyManager.CONF_FILE_DIR + CONF_FILE);
		} catch (Exception e) {
			throw new GeneralException("PropertyManager : init() Exception error..");
		}
	}

}