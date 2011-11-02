/** Main system          : foundation
  * Sub system           : lib
  * Classname            : PropertyManager.java
  * Initial date         : 2005.11.16
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : Property ó������ ���� Module
  * Version information  : v 1.0
  *
*/

package x.extr.lib;

import x.extr.log.Log;
import x.extr.exception.GeneralException;
import x.extr.lib.ErrorConstants;



/** �����⵿�� �⺻ ȯ���� ���� ������Ƽ���� �����ϴ� Ŭ�����̴�.<p>
  * �����̸� , ������Ʈ , �α�, DBǮ, Jdbc Driver���� ���� ������Ƽ ����<p>
  * �̱۸������� �ʱ⿡ �ѹ��� �⵿�Ѵ�.<p>
  * @author Ȳ��õ
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


	/** Init() �޼ҵ带 ���� ������ PropetyManager��ü�� ��´�
	 * @return PropertyManager
	 * @throws Exception
	*/
	public static PropertyManager getInstance() throws Exception {
		/* PropertyManager�� �ν��Ͻ��� ������ �Ǿ� �ִٸ� ������ �ν��Ͻ��� ����
		 * �׷��� ������ ���ο� �ν��Ͻ��� ����
		*/
		if (m_instance == null) {
			m_instance = new PropertyManager();				// ��ü�� �����Ѵ�
			m_instance.init();								// ��ü�� �ʱ�ȭ �ϱ� ���� Init()����
			return m_instance;								// ������ �ν��Ͻ��� ������
		}
		return m_instance;
	}


	/** Init() �޼ҵ带 ���� ������ PropetyManager��ü�� ��´�
	 * @param args         ���丮
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


	/** �̱۸����� �ʱ�ȭ�Ѵ�.(PropertyManager��ü�� �����Ѵ�)<p>
	 * ���͸��� ������ ErpDir�� ���ؼ� �����ϰ��� �ϴ°��
	 * @param ErpDir             ���丮
	 * @throws GeneralException
	*/
	public void init(String ErpDir) throws GeneralException
	{
		/*
		 * �ν��Ͻ��� �������������� ����
		 * ���� �Ѵٸ� �����ܰ���� ����
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


	/** �̱۸����� �ʱ�ȭ�Ѵ�.(PropertyManager��ü�� �����Ѵ�)
	 * ���͸��� ���ο� ������ ���� ����ϴ� ���
	 * @throws GeneralException
	*/
	public void init() throws GeneralException {
		/*
		 * �ν��Ͻ��� �������������� ����
		 * ���� �Ѵٸ� �����ܰ���� ����
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


	/** PropertyManager �̱۸����� ���V�Ѵ�.
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