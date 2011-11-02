/** Main system          : foundation
  * Sub system           : util
  * Classname            : MathUtil.java
  * Initial date         : 2005.11.26
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : 수식관련 유틸리티 Class
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


/** 수식관련 유틸리티 Class
  * @author 황재천
*/
public class MathUtil {
	static String sSystem = "Com";
	static String sClassName = "MathUtil";


	/** 이자를 계산하는 메소드
	  @param sFromDt         이자계산시작일
	  @param sToDt           이자계산종료일
	  @param lPrn            이자계산원금
	  @param dIrt            이자율
	  @return BigDecimal
	*/ 
     public static BigDecimal getIntAmt(String sFromDt, String sToDt, BigDecimal lPrn, BigDecimal dIrt) {
		BigDecimal result;
 	
     	try {
             BigDecimal day = new BigDecimal(DateUtils.betweenDay(sFromDt,sToDt));
             result = lPrn.multiply(dIrt).multiply(day);             
     	} catch (Exception e) {
     		Log.writeLog(sSystem, sClassName, "getIntAmt()", Log.LOG_CRITICAL, "Exception 발생");
     		System.out.println(e.toString());
     		result = new BigDecimal(0);
     	}
		
		return result;
     }
}