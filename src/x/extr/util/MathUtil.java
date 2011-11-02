/** Main system          : foundation
  * Sub system           : util
  * Classname            : MathUtil.java
  * Initial date         : 2005.11.26
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ���İ��� ��ƿ��Ƽ Class
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.util;

import java.util.*;
import java.math.*;
import x.extr.util.*;
import x.extr.log.*;


/** ���İ��� ��ƿ��Ƽ Class
  * @author Ȳ��õ
*/
public class MathUtil {
	static String sSystem = "Com";
	static String sClassName = "MathUtil";


	/** ���ڸ� ����ϴ� �޼ҵ�
	  @param sFromDt         ���ڰ�������
	  @param sToDt           ���ڰ��������
	  @param lPrn            ���ڰ�����
	  @param dIrt            ������
	  @return BigDecimal
	*/ 
     public static BigDecimal getIntAmt(String sFromDt, String sToDt, BigDecimal lPrn, BigDecimal dIrt) {
		BigDecimal result;
 	
     	try {
             BigDecimal day = new BigDecimal(DateUtils.betweenDay(sFromDt,sToDt));
             result = lPrn.multiply(dIrt).multiply(day);             
     	} catch (Exception e) {
     		Log.writeLog(sSystem, sClassName, "getIntAmt()", Log.LOG_CRITICAL, "Exception �߻�");
     		System.out.println(e.toString());
     		result = new BigDecimal(0);
     	}
		
		return result;
     }
}