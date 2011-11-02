/** Main system          : x.extr
  * Sub system           : lib
  * Classname            : JndiManager.java
  * Initial date         : 2005.11.17
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : Jndi ó������ ���� Module
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



/** EJBŬ������ ���ؽ�Ʈ �̸��� �����ϴ� Ŭ�����̴�.
  * �̱۸������� �ʱ⿡ �ѹ��� �⵿�Ѵ�.
  * @author Ȳ��õ
*/
public class JndiManager extends x.extr.lib.Properties {
	private static final boolean verbose = true;
	private static x.extr.lib.JndiManager m_instance;

	/** Constructor JndiManager
	*/
	private JndiManager() { }


	/** Init() �޼ҵ带 ���� ������ JndiManager��ü�� ��´�
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


	/** �̱۸����� �ʱ�ȭ�Ѵ�.(JndiManager��ü�� �����Ѵ�)
	 * @throws GeneralException
	 */
	public void init() throws GeneralException {
		if (m_instance == null) {
			m_instance = new JndiManager();
		}

		try {
			PropertyManager pm = PropertyManager.getInstance();

			// PropertyManager �� ��ϵ� ���� ������ �´�
			String propPath =(String)pm.get("JNDI_PROPERTIES");
			if (propPath == null || propPath.length() <= 0)
				propPath = "jndi.properties";

			m_instance.loadFromFile(propPath);
		} catch (Exception e) {
			// ���νý����̸�, Ŭ�����̸�, �޼ҵ��̸�, �α�����, �Ѹ����� �ϴ� �޼���
			Log.writeLog("Com", "JndiManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			throw new GeneralException("JndiManager : init() Exception error..");
		}
	}


	/** JndiManager �̱۸����� ���V�Ѵ�.
	 * @throws GeneralException
	*/
	public static void reset() throws GeneralException {
		if (m_instance == null) {
			m_instance = new JndiManager();
		}

		try {
			PropertyManager pm = PropertyManager.getInstance();

			// PropertyManager �� ��ϵ� ���� ������ �´�
			String propPath =(String)pm.get("JNDI_PROPERTIES");
			if (propPath == null || propPath.length() <= 0)
				propPath = "jndi.properties";

			m_instance.clear();
			m_instance.loadFromFile(propPath);
		} catch (Exception e) {
			// ���νý����̸�, Ŭ�����̸�, �޼ҵ��̸�, �α�����, �Ѹ����� �ϴ� �޼���
			Log.writeLog("Com", "JndiManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			throw new GeneralException("JndiManager : reset() Exception error..");
		}
	}


}