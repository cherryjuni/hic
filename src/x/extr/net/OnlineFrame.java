/** Main system          : x
  * Sub system           : net
  * Classname            : OnlineFrame.java
  * Initial date         : 2005.11.16
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DPS와 Online처리를 위한 기본 Frame
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Tongyang Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.net;

import x.extr.util.*;

/** DPS와 Online처리를 위한 기본 Frame
  * @author 황재천
*/
public class OnlineFrame extends BaseFrame {
	static final String ONLINEFRAME = "OLN";

	/** 생성자
      * @param seq                전송 Sequence
      * @param data               전송할 데이터부
      */
	public OnlineFrame(int seq,String data) {
		this.trCode = ONLINEFRAME;
		this.sr = "S";
		this.error = "0";
		this.seq = seq;
		this.data = data;
	}
	/** 생성자
      * @param seq                전송 Sequence
      * @param sr                 송수신구분
      * @param data               전송할 데이터부
      */
	public OnlineFrame(int seq,String sr,String data) {
		this.trCode = ONLINEFRAME;
		this.sr = sr;
		this.error = "0";
		this.seq = seq;
		this.data = data;
	}

	/** 문자열타입의 프레임을 헤더와 데이터부로 분할<p>
	  * String[0] : 헤더<p>
	  * String[1] : 데이터부
      * @param data             문자열 프레임
	  * @return String[]
      */
	public static String[] divideHeader(String data) {
		System.out.println("divideHeader() Bef:[" + data + "]");
		String[] sData = new String[2];
		sData[0] = data.substring(0,10);//15
		sData[1] = data.substring(10);//15
		System.out.println("divideHeader() after:[" + sData[0] + "] [" + sData[1] + "]"  );
		return sData;
	}

/*
	public static void main(String[] args) 
	{
		String sTemp;
		OnlineFrame of = new OnlineFrame(1,"TEST     TESTTEST  123456789012345678901234567890abcdefghij12345678YYYYMMDDHHMMSSF1234abcd1234567890T00000");
//		OnlineFrame of = new OnlineFrame(1,"TEST_____TESTTEST__123456789012345678901234567890abcdefghij12345678YYYYMMDDHHMMSSF1234abcd1234567890ABCDEFGHJIKLMNOPQRSTUVWXYZ");
		
		sTemp = of.toString();
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "1SEND:" + sTemp);
		TCPClient tc = TCPClient.getInstance();
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "1Middle");
		sTemp = tc.callOnline(of);
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "1RECV:" + sTemp);

		sTemp = of.toString();
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "2SEND:" + sTemp);
		tc = TCPClient.getInstance();
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "2Middle");
		sTemp = tc.callOnline(of);
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "2RECV:" + sTemp);
	

	}
*/

}
