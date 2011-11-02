/** Main system          : foundation
  * Sub system           : admin
  * Classname            : MethodManager.java
  * Initial date         : 2005.11.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : Method ó������ ���� Module
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.lib;

import java.util.*;
import java.io.*;
import x.extr.log.Log;
import x.extr.exception.GeneralException;
import x.extr.lib.ErrorConstants;



/**  EJBŬ������ ���ؽ�Ʈ �̸��� �����ϴ� Ŭ�����̴�.
  * �̱۸������� �ʱ⿡ �ѹ��� �⵿�Ѵ�.
  * @author Ȳ��õ
*/
public class MethodManager extends x.extr.lib.Properties {
	private static final boolean verbose = true;
	private static x.extr.lib.MethodManager m_instance;

	/** Constructor MethodManager
	*/
	private MethodManager() { }


	/**
	 * Init() �޼ҵ带 ���� ������ MethodManager��ü�� ��´�
	 * @return MethodManager
	 * @throws Exception
	*/
	public static MethodManager getInstance() throws Exception {
		if (m_instance == null) {
			m_instance = new MethodManager();
			m_instance.init();
		}
		return m_instance;
	}


	/** �̱۸����� �ʱ�ȭ�Ѵ�.(MethodManager��ü�� �����Ѵ�)
	 * @throws GeneralException
	 */
	public void init() throws GeneralException {
		if (m_instance == null) {
			m_instance = new MethodManager();
		}

		try {
			PropertyManager pm = PropertyManager.getInstance();

			// PropertyManager �� ��ϵ� ���� ������ �´�
			String propPath =(String)pm.get("METHOD_PROPERTIES");
			if (propPath == null || propPath.length() <= 0)
				propPath = "method.properties";

			m_instance.loadFromFile(propPath);
		} catch (Exception e) {
			// ���νý����̸�, Ŭ�����̸�, �޼ҵ��̸�, �α�����, �Ѹ����� �ϴ� �޼���
			Log.writeLog("Com", "MethodManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			throw new GeneralException("MethodManager : init() Exception error..");
		}
	}

	/** MethodManager �̱۸����� ���V�Ѵ�.
	 * @throws GeneralException
	*/
	public static void reset() throws GeneralException {
		if (m_instance == null) {
			m_instance = new MethodManager();
		}

		try {
			PropertyManager pm = PropertyManager.getInstance();

			// PropertyManager �� ��ϵ� ���� ������ �´�
			String propPath =(String)pm.get("METHOD_PROPERTIES");
			if (propPath == null || propPath.length() <= 0)
				propPath = "method.properties";

			m_instance.clear();
			m_instance.loadFromFile(propPath);
		} catch (Exception e) {
			// ���νý����̸�, Ŭ�����̸�, �޼ҵ��̸�, �α�����, �Ѹ����� �ϴ� �޼���
			Log.writeLog("Com", "MethodManager", "init",ErrorConstants.LOAD_CONFIG_FILE_FAILED, Log.LOG_ERROR, "init GeneralException");
			throw new GeneralException("MethodManager : reset() Exception error..");
		}
	}

}