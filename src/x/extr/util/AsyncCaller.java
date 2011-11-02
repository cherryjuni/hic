/** Main system          : x
  * Sub system           : util
  * Classname            : AsyncCaller.java
  * Initial date         : 2005.12.15
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : 회답이 올때까지 대기할 필요가 없는 동적 메소드콜을 위한 Class
  * Version information  : v 1.0
  *
  */

package x.extr.util;

import java.lang.reflect.*;
import x.extr.log.*;

/** 회답이 올때까지 대기할 필요가 없는 동적 메소드콜을 위한 Class
  * @author 황재천
*/
public class AsyncCaller implements Runnable{
	String sSystem = "util";
	String sClassName = "AsyncCaller";

	String className = null;
	String methodName = null;
	Object[] objectArray = null;


	/** 생성자
	  @param cName                      Class명
	  @param mName                      Method명
	  @param oa[]                       메소드의 파라메터 어레이
	*/
	public AsyncCaller(String cName,String mName,Object[] oa) {
		super();
		this.className = cName;
		this.methodName = mName;
		
		/*
		 * oa 는 0 밖에 없다. 0에 아규먼트 배열이 다시 들어가 있다. 즉 2차원 배열이다.
		 * 일차배열 oa 의 원소가 1개이고 그 원소가 아규먼트 배열이다.
		 * 
		 * 아규먼트 배열에서 0-3까지는 자체파라메타로 호출하는 프로그램으로는 넘겨주지 않는다.(업무파라메타만 넘김)
		 */
		String[] tmp1 = (String[])oa[0];
		String[] tmp2 = new String[tmp1.length-4];
		for(int i=0;i<tmp1.length-4;i++){
			tmp2[i] = tmp1[i+4];
		}
		objectArray = new Object[1];
		objectArray[0] = tmp2;

		System.setProperty("java.class.path","$CLASSPATH:/TMAX/jeus42/lib/application/log4j.jar:.");
		System.setProperty("java.naming.factory.initial","jeus.jndi.JNSContextFactory");
	}


	
	/** Thread의 run메소드
	*/
	public void run() {
		Method m;
//		Class[] mParamTypes = {String.class, String.class, String[].class};
		Class[] mParamTypes = {String[].class};
		String sMethod;
		int intParam;
		Class rm;
		Object rmo;

		try {
			rm = Class.forName(className);
			rmo = rm.newInstance();
			m = rm.getMethod(methodName, mParamTypes);
			System.out.println(className+ "." + methodName + ": start");
			m.invoke(rmo, objectArray);
			System.out.println(className+ "." + methodName + ": end");
		} catch (Exception e) {
			System.out.println("className=" + className + " methodName=" + methodName + " Error=" + e.toString());
			Log.writeLog(sSystem, sClassName, "run()", Log.LOG_CRITICAL, e.getMessage());
		}
	}

	/*
	public void run() {
		Method[] m;
		Class[] mParamTypes;
		String sMethod;
		int intParam;

		try {
			Class rm = Class.forName(className);
			Object rmo = rm.newInstance();

			m = rm.getMethods();

			for (int i=0;i<m.length;i++) {
				sMethod = m[i].getName();
				mParamTypes = m[i].getParameterTypes();

				if (sMethod.equals(methodName)) {
					System.out.println(className+ "." + methodName + ": start");
					m[i].invoke(rmo,objectArray);
					System.out.println(className+ "." + methodName + ": end");
					return;
				}
			}
		} catch (Exception e) {
			Log.writeLog(sSystem, sClassName, "run()", Log.LOG_CRITICAL, e.getMessage());
		}
	}
	*/
	
}