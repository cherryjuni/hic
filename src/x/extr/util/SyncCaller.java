/** Main system          : x
  * Sub system           : util
  * Classname            : SyncCaller.java
  * Initial date         : 2005.11.14
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : 회답을 기다려야 하는 동적 메소드콜을 위한 Class
  * Version information  : v 1.0
  *
  */

package x.extr.util;

import java.lang.reflect.*;
import x.extr.log.*;


/** 회답을 기다려야 하는 동적 메소드콜을 위한 Class
  * @author 황재천
*/
public class SyncCaller {
	String sSystem = "Com";
	String sClassName = "SyncCaller";


	/** 메소드를 실행하고 그 결과를 스트링으로 반환
	  @param cName                      Class명
	  @param mName                      Method명
	  @param oa[]                       메소드의 파라메터 어레이
	  @return String
	*/
	public String callString(String cName,String mName,Object[] oa) {
		return (String)call(cName,mName,oa);
	}


	/** 메소드를 실행하는 기본 메소드
	  @param cName                      Class명
	  @param mName                      Method명
	  @param oa[]                       메소드의 파라메터 어레이
	  @return Object
	*/
	public Object call(String cName,String mName,Object[] oa) {
		Method[] m;
		Class[] mParamTypes;
		String sMethod;
		int intParam;

		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " SyncCall.call Start");
		try {
			Class rm = Class.forName(cName);
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " SyncCall.call Check 1");
			Object rmo = rm.newInstance();
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " SyncCall.call Check 2");
			System.out.println("##" + rm.toString());

			m = rm.getMethods();

			for (int i=0;i<m.length;i++) {
				sMethod = m[i].getName();
				mParamTypes = m[i].getParameterTypes();
				System.out.println("**[" + i + "]" + sMethod);

				/*
				for (int j=0;j<mParamTypes.length;j++) {
					System.out.println(" p[" + j + "]" + mParamTypes[j].getName());
				}
				*/
				
				if (sMethod.equals(mName)) {
					// System.out.println(">>Call " + mName);
					System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " SyncCall.call Finish - 직전");
					return m[i].invoke(rmo,oa);
				} 
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.writeLog(sSystem, sClassName, "callString()", Log.LOG_CRITICAL, e.getMessage());
		}

		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " SyncCall.call Finish");
		return null;
	}

}