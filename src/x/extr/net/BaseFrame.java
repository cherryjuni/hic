/** Main system          : x.extr
  * Sub system           : net
  * Classname            : BaseFrame.java
  * Initial date         : 2005.11.20
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : DPS�� ����� ���� �������� ó���ϴ� �⺻ Ŭ����
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.net;

import x.extr.util.*;

/** DPS�� ����� ���� �������� ó���ϴ� �⺻ Ŭ����
  * @author Ȳ��õ
*/
public class BaseFrame  {
	private int len = 0;
	public String trCode = null;
	public String sr = null;
	public String error = "0";
	public int seq = 0;
	public String data = null;

	/** ������
      * @param trCode         TR-Code
	  * @param sr             �ۼ��ű���
	  * @param error          �����ڵ�
	  * @param seq            ���� Sequence
	  * @param data           �������ʵ�
      */
	BaseFrame(String trCode,String sr,String error,int seq,String data) {
		this.trCode = trCode;
		this.sr = sr;
		this.error = error;
		this.seq = seq;
		this.data = data;
	}
	/** ������
      */
	BaseFrame() {};


	/** ���������� ���ڿ��� ��ȯ
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
//System.out.println("BaseFrame ����.....:"+size.toString());		
		//return size.toString();
		return buf.toString();
	}


	/** ���ڿ�Ÿ���� �������� ���������� �̿��ؼ� ����
      * @param data           ���ڿ�������
	  * @param format         ���������� ������ �ִ� ���˵�����
	  * @return
      */
	public static String[] parseFrame(String data,String format) {
		return parseFrame(data,format,";");
	}
	/** ���ڿ�Ÿ���� �������� ���������� �̿��ؼ� ����(�ѱ�ó��)
      * @param data           ���ڿ�������
	  * @param format         ���������� ������ �ִ� ���˵�����
	  * @return
      */
	public static String[] parseFrameHan(String data,String format) {
		return parseFrameHan(data,format,";");
	}

	/** ���ڿ�Ÿ���� �������� ���������� �̿��ؼ� ����
      * @param data           ���ڿ�������
	  * @param format         ���������� ������ �ִ� ���˵�����
	  * @param deli           ���䵥������ ������
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

		//����ó���κа� �����Ͱ� ������ �κ�ó������
		
		int sPos,ePos;
		int lPos,lCount;
		String fStr,bStr,lStr;

		// ���˵������� ���������� ó���Ѵ�.
		System.out.println("parse frame start");
		while (true) {
			sPos = format.indexOf("[");
			if (sPos < 0) {
				break;
			} else {
				ePos = format.indexOf("]");
				if (ePos < 0) {
					System.out.println("���˹��ڿ��� ���������� ������ �ֽ��ϴ�.(])");
					return null;
				} else {
					fStr = format.substring(0,sPos);
					lStr = format.substring(sPos + 1,ePos);
					bStr = format.substring(ePos + 1);

					lPos = lStr.indexOf(":");
					if (lPos < 0) {
						System.out.println("���˹��ڿ��� ���������� ������ �ֽ��ϴ�.(�ݺ�Ƚ��)");
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
	            // �����Ͱ� �����Ѱ�� ó��
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
/** ���ڿ�Ÿ���� �������� ���������� �̿��ؼ� ����(�ѱ�ó��)
      * @param data           ���ڿ�������
	  * @param format         ���������� ������ �ִ� ���˵�����
	  * @param deli           ���䵥������ ������
	  * @return
      */
	public static String[] parseFrameHan(String data,String format,String deli) {


		//����ó���κа� �����Ͱ� ������ �κ�ó������
		
		int sPos,ePos;
		int lPos,lCount;
		String fStr,bStr,lStr;

		// ���˵������� ���������� ó���Ѵ�.
		System.out.println("parse frame start");
		while (true) {
			sPos = format.indexOf("[");
			if (sPos < 0) {
				break;
			} else {
				ePos = format.indexOf("]");
				if (ePos < 0) {
					System.out.println("���˹��ڿ��� ���������� ������ �ֽ��ϴ�.(])");
					return null;
				} else {
					fStr = format.substring(0,sPos);
					lStr = format.substring(sPos + 1,ePos);
					bStr = format.substring(ePos + 1);

					lPos = lStr.indexOf(":");
					if (lPos < 0) {
						System.out.println("���˹��ڿ��� ���������� ������ �ֽ��ϴ�.(�ݺ�Ƚ��)");
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
	            // �����Ͱ� �����Ѱ�� ó��
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
