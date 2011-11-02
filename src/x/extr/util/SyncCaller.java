/** Main system          : x
  * Sub system           : util
  * Classname            : SyncCaller.java
  * Initial date         : 2005.11.14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ȸ���� ��ٷ��� �ϴ� ���� �޼ҵ����� ���� Class
  * Version information  : v 1.0
  *
  */

package x.extr.util;

import java.lang.reflect.*;
import x.extr.log.*;


/** ȸ���� ��ٷ��� �ϴ� ���� �޼ҵ����� ���� Class
  * @author Ȳ��õ
*/
public class SyncCaller {
	String sSystem = "Com";
	String sClassName = "SyncCaller";


	/** �޼ҵ带 �����ϰ� �� ����� ��Ʈ������ ��ȯ
	  @param cName                      Class��
	  @param mName                      Method��
	  @param oa[]                       �޼ҵ��� �Ķ���� ���
	  @return String
	*/
	public String callString(String cName,String mName,Object[] oa) {
		return (String)call(cName,mName,oa);
	}


	/** �޼ҵ带 �����ϴ� �⺻ �޼ҵ�
	  @param cName                      Class��
	  @param mName                      Method��
	  @param oa[]                       �޼ҵ��� �Ķ���� ���
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
					System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " SyncCall.call Finish - ����");
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