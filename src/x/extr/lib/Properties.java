/** Main system          : foundation
  * Sub system           : lib
  * Classname            : Properties.java
  * Initial date         : 2005.11.14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : Properties�� �����ϴ� Ŭ����
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.lib;

import java.io.*;
import java.util.*;

/** ������Ƽ�� �����ϴ� �⺻ Class�Դϴ�.
    @author Ȳ��õ
*/
public class Properties extends java.util.Properties {


	/** Proeperties ������
	*/
	public Properties() {
		super();
	}


	/** FileInputStream�� ���Ͽ� �ε��Ѵ�
	 * @param Dir      ������Ƽȭ�ϸ�
	*/
	public void loadFromFile(String Dir) {
		try {
			FileInputStream fin = new FileInputStream(Dir);
			this.load(fin);
			fin.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}


	/** FileOutputStream�� ���� properties���Ͽ� �����Ѵ�.
	 * @param Dir       ������Ƽȭ�ϸ�
	*/
	public void saveToFile(String Dir) {
		try {
			FileOutputStream fout = new FileOutputStream(Dir);
			this.store(fout, "properties Save");
			fout.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}


	/** key�� �ش��ϴ� ���� properties���� int type���� �����´�.
	 * @param key       ������Ƽ Ű��
	 * @return int
	*/
	public int getInt(String key) {
		Integer m_int = new Integer(getProperty(key));
		return m_int.intValue();
	}


	/** key�� �ش��ϴ� ���� properties���� String ������ �����´�.
	 * @param key       ������Ƽ Ű��
	 * @return String
	 */
	public String getStr(String key) {
		return getProperty(key);
	}


	/** key�� �ش��ϴ� ���� properties���� Object�� �����´�.
	 * @param key
	 * @return Object
	 */
	public Object getObject(String key) {
		return get(key);
	}


	/** propeties�� �ش��ϴ� key ���� ������ key�� value�� �ִ´�.
	 * @param key            ������Ƽ Ű��
	 * @param value          ������Ƽ ������
	 * @return boolean
	*/
	public boolean setObject(String key, Object value) {
		if(isAvailable(key)) {
			put(key, value);
			return true;
		} else  {
			return false;
		}
	}


	/** properties�� �����ϴ� key�� ������ key�� value�� �����Ѵ�.
	 * @param key               ������Ƽ Ű��
	 * @param value             ������Ƽ ������
	 * @return boolean
	*/
	public boolean setValue(String key, String value) {
		if(isAvailable(key)) {
			setProperty(key, value);
			return true;
		} else  {
			return false;
		}
	}


	/** key�� properties�� �ִ°��� ã�Ƽ� ������ false�� ������ true�� ��ȯ�Ѵ�.
	 * @param key             ������Ƽ Ű��
	 * @return boolean
	*/
	public boolean isAvailable(String key) {
		if(containsKey(key)) {
			return false;
		} else {
			return true;
		}
	}
}