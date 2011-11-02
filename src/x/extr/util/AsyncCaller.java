/** Main system          : x
  * Sub system           : util
  * Classname            : AsyncCaller.java
  * Initial date         : 2005.12.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ȸ���� �ö����� ����� �ʿ䰡 ���� ���� �޼ҵ����� ���� Class
  * Version information  : v 1.0
  *
  */

package x.extr.util;

import java.lang.reflect.*;
import x.extr.log.*;

/** ȸ���� �ö����� ����� �ʿ䰡 ���� ���� �޼ҵ����� ���� Class
  * @author Ȳ��õ
*/
public class AsyncCaller implements Runnable{
	String sSystem = "util";
	String sClassName = "AsyncCaller";

	String className = null;
	String methodName = null;
	Object[] objectArray = null;


	/** ������
	  @param cName                      Class��
	  @param mName                      Method��
	  @param oa[]                       �޼ҵ��� �Ķ���� ���
	*/
	public AsyncCaller(String cName,String mName,Object[] oa) {
		super();
		this.className = cName;
		this.methodName = mName;
		
		/*
		 * oa �� 0 �ۿ� ����. 0�� �ƱԸ�Ʈ �迭�� �ٽ� �� �ִ�. �� 2���� �迭�̴�.
		 * �����迭 oa �� ���Ұ� 1���̰� �� ���Ұ� �ƱԸ�Ʈ �迭�̴�.
		 * 
		 * �ƱԸ�Ʈ �迭���� 0-3������ ��ü�Ķ��Ÿ�� ȣ���ϴ� ���α׷����δ� �Ѱ����� �ʴ´�.(�����Ķ��Ÿ�� �ѱ�)
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


	
	/** Thread�� run�޼ҵ�
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