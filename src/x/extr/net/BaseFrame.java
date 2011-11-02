/** Main system          : x.extr
  * Sub system           : net
  * Classname            : BaseFrame.java
  * Initial date         : 2005.11.20
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DPS와 통신을 위한 통신헤더를 처리하는 기본 클래스
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.net;

import x.extr.util.*;

/** DPS와 통신을 위한 통신헤더를 처리하는 기본 클래스
  * @author 황재천
*/
public class BaseFrame  {
	private int len = 0;
	public String trCode = null;
	public String sr = null;
	public String error = "0";
	public int seq = 0;
	public String data = null;

	/** 생성자
      * @param trCode         TR-Code
	  * @param sr             송수신구분
	  * @param error          에러코드
	  * @param seq            전송 Sequence
	  * @param data           데이터필드
      */
	BaseFrame(String trCode,String sr,String error,int seq,String data) {
		this.trCode = trCode;
		this.sr = sr;
		this.error = error;
		this.seq = seq;
		this.data = data;
	}
	/** 생성자
      */
	BaseFrame() {};


	/** 설정정보를 문자열로 반환
	  * @return 
      */
	public String toString() {
		StringBuffer buf = null ;
		int len = 0;
		StringBuffer size = null; 
		try{
			//buf = new StringBuffer(trCode);
			buf = new StringBuffer();
			//buf.append(sr);
			//buf.append(error);
			//buf.append(FormatData.numToStr(seq,6,"0"));
			buf.append(data);
	
			//len = buf.toString().getBytes("EUC_KR").length;
			//size = new StringBuffer(FormatData.numToStr(len + 4,4,"0"));
			//size.append(buf);
		}catch(Exception e){
			e.toString();
		}
//System.out.println("BaseFrame 에서.....:"+size.toString());		
		//return size.toString();
		return buf.toString();
	}


	/** 문자열타입의 프레임을 포맷정보를 이용해서 분할
      * @param data           문자열프레임
	  * @param format         분할정보를 가지고 있는 포맷데이터
	  * @return
      */
	public static String[] parseFrame(String data,String format) {
		return parseFrame(data,format,";");
	}
	/** 문자열타입의 프레임을 포맷정보를 이용해서 분할(한글처리)
      * @param data           문자열프레임
	  * @param format         분할정보를 가지고 있는 포맷데이터
	  * @return
      */
	public static String[] parseFrameHan(String data,String format) {
		return parseFrameHan(data,format,";");
	}

	/** 문자열타입의 프레임을 포맷정보를 이용해서 분할
      * @param data           문자열프레임
	  * @param format         분할정보를 가지고 있는 포맷데이터
	  * @param deli           포멧데이터의 구분자
	  * @return
      */
	public static String[] parseFrame(String data,String format,String deli) {
/*		String[] sFormat = FormatData.strSplit(format,";");
		String[] parsedData = new String[sFormat.length];
		byte[] bData = data.getBytes();
		int formatLen;
		int cutPos;
System.out.println("strSplit : " + format + " - " + sFormat.length);
		cutPos = 0;
		for (int i=0;i<sFormat.length;i++) {

			formatLen = Integer.parseInt(sFormat[i]);
//			parsedData[i] = data.substring(cutPos,cutPos + formatLen);
			parsedData[i] = new String(bData,cutPos,formatLen);
			cutPos += formatLen;
		}

		return parsedData;
*/

		//루프처리부분과 데이터가 부족한 부분처리수정
		
		int sPos,ePos;
		int lPos,lCount;
		String fStr,bStr,lStr;

		// 포맷데이터의 루핑정보를 처리한다.
		System.out.println("parse frame start");
		while (true) {
			sPos = format.indexOf("[");
			if (sPos < 0) {
				break;
			} else {
				ePos = format.indexOf("]");
				if (ePos < 0) {
					System.out.println("포맷문자열에 루핑정보에 오류가 있습니다.(])");
					return null;
				} else {
					fStr = format.substring(0,sPos);
					lStr = format.substring(sPos + 1,ePos);
					bStr = format.substring(ePos + 1);

					lPos = lStr.indexOf(":");
					if (lPos < 0) {
						System.out.println("포맷문자열에 루핑정보에 오류가 있습니다.(반복횟수)");
						return null;
					} else {
						lCount = Integer.parseInt(lStr.substring(0,lPos));
						lStr = lStr.substring(lPos + 1);

						format = fStr;
						for (int j=1;j<=lCount;j++) {
							if (j>1) {
								format += (";" + lStr);
							} else {
								format += lStr;
							}
						}
						format += bStr;
					}
				}
			}
		}
System.out.println("parse frame start2");
		String[] sFormat = FormatData.strSplit(format,deli);
		System.out.println("parse frame start3");
		String[] parsedData = new String[sFormat.length];
		System.out.println("parse frame start4");
		try{
			//byte[] bData = data.getBytes("EUC_KR");
			System.out.println("================================");
			System.out.println("DATA~~~" + data);
			System.out.println("================================");
			byte[] bData = data.getBytes();
			int formatLen;
			int cutPos;
			System.out.println("strSplit : " + format + " - " + sFormat.length);
			cutPos = 0;
			for (int i=0;i<sFormat.length;i++) {
	            // 데이터가 부족한경우 처리
				formatLen = Integer.parseInt(sFormat[i]);
				if (bData.length >= (cutPos + formatLen)) {
					parsedData[i] = new String(bData,cutPos,formatLen,"KSC5601");
					
				} else {
					parsedData[i] = "";
				}
				cutPos += formatLen;
			}
		}catch(Exception e){
			System.out.println("BaseFrame.parseFrame - Error Occured:" + e.toString() );
			if (parsedData == null){
				System.out.println("parsedData is Null");	
			}
			return parsedData;
		}
		return parsedData;
	}
/** 문자열타입의 프레임을 포맷정보를 이용해서 분할(한글처리)
      * @param data           문자열프레임
	  * @param format         분할정보를 가지고 있는 포맷데이터
	  * @param deli           포멧데이터의 구분자
	  * @return
      */
	public static String[] parseFrameHan(String data,String format,String deli) {


		//루프처리부분과 데이터가 부족한 부분처리수정
		
		int sPos,ePos;
		int lPos,lCount;
		String fStr,bStr,lStr;

		// 포맷데이터의 루핑정보를 처리한다.
		System.out.println("parse frame start");
		while (true) {
			sPos = format.indexOf("[");
			if (sPos < 0) {
				break;
			} else {
				ePos = format.indexOf("]");
				if (ePos < 0) {
					System.out.println("포맷문자열에 루핑정보에 오류가 있습니다.(])");
					return null;
				} else {
					fStr = format.substring(0,sPos);
					lStr = format.substring(sPos + 1,ePos);
					bStr = format.substring(ePos + 1);

					lPos = lStr.indexOf(":");
					if (lPos < 0) {
						System.out.println("포맷문자열에 루핑정보에 오류가 있습니다.(반복횟수)");
						return null;
					} else {
						lCount = Integer.parseInt(lStr.substring(0,lPos));
						lStr = lStr.substring(lPos + 1);

						format = fStr;
						for (int j=1;j<=lCount;j++) {
							if (j>1) {
								format += (";" + lStr);
							} else {
								format += lStr;
							}
						}
						format += bStr;
					}
				}
			}
		}
System.out.println("parse frame start2");
		String[] sFormat = FormatData.strSplit(format,deli);
		System.out.println("parse frame start3");
		String[] parsedData = new String[sFormat.length];
		System.out.println("parse frame start4");
		try{
			//byte[] bData = data.getBytes("EUC_KR");
			System.out.println("================================");
			System.out.println("DATA~~~" + data);
			System.out.println("================================");
			byte[] bData = data.getBytes("EUC_KR");
			int formatLen;
			int cutPos;
			System.out.println("strSplit : " + format + " - " + sFormat.length);
			cutPos = 0;
			for (int i=0;i<sFormat.length;i++) {
	            // 데이터가 부족한경우 처리
				formatLen = Integer.parseInt(sFormat[i]);
				if (bData.length >= (cutPos + formatLen)) {
					parsedData[i] = new String(bData,cutPos,formatLen,"KSC5601");
					
				} else {
					parsedData[i] = "";
				}
				cutPos += formatLen;
			}
		}catch(Exception e){
			System.out.println("BaseFrame.parseFrame - Error Occured:" + e.toString() );
			if (parsedData == null){
				System.out.println("parsedData is Null");	
			}
			return parsedData;
		}
		return parsedData;
	}

}
