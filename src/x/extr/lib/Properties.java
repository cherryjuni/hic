/** Main system          : foundation
  * Sub system           : lib
  * Classname            : Properties.java
  * Initial date         : 2005.11.14
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : Properties를 관리하는 클래스
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.lib;

import java.io.*;
import java.util.*;

/** 프로퍼티를 관리하는 기본 Class입니다.
    @author 황재천
*/
public class Properties extends java.util.Properties {


	/** Proeperties 생성자
	*/
	public Properties() {
		super();
	}


	/** FileInputStream을 통하여 로딩한다
	 * @param Dir      프로퍼티화일명
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


	/** FileOutputStream을 통해 properties파일에 저장한다.
	 * @param Dir       프로퍼티화일명
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


	/** key에 해당하는 값을 properties에서 int type으로 가져온다.
	 * @param key       프로퍼티 키값
	 * @return int
	*/
	public int getInt(String key) {
		Integer m_int = new Integer(getProperty(key));
		return m_int.intValue();
	}


	/** key에 해당하는 값을 properties에서 String 값으로 가져온다.
	 * @param key       프로퍼티 키값
	 * @return String
	 */
	public String getStr(String key) {
		return getProperty(key);
	}


	/** key에 해당하는 값을 properties에서 Object로 가져온다.
	 * @param key
	 * @return Object
	 */
	public Object getObject(String key) {
		return get(key);
	}


	/** propeties에 해당하는 key 값이 없을때 key와 value를 넣는다.
	 * @param key            프로퍼티 키값
	 * @param value          프로퍼티 데이터
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


	/** properties에 지정하는 key가 없을때 key와 value를 저장한다.
	 * @param key               프로퍼티 키값
	 * @param value             프로퍼티 데이터
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


	/** key가 properties에 있는가를 찾아서 있으면 false를 없으면 true를 반환한다.
	 * @param key             프로퍼티 키값
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