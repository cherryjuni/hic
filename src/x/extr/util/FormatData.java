/** Main system          : foundation
  * Sub system           : util
  * Classname            : FormatData.java
  * Initial date         : 2005.11.14
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : 데이타 포맷팅관련 유틸리티 Class
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.util;

import java.util.StringTokenizer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.math.BigDecimal;
import java.text.*;
import java.io.*;



/** 데이타 포맷팅관련 유틸리티 Class
  * @author 황재천
*/
public class FormatData {


	/** 바꾸고자 하는 스트링의 인덱스 모음을 구한다.
      * @param sql              문자열
      * @param word             찾을 문자열
      * @return Vector
      */
	public static Vector getSelectedTextIndex(String sql, String word) {
		int index = 0;
		int fromIndex = 0;
		Vector tempIndexArray = new Vector();

		do {
			index = sql.indexOf(word,fromIndex);
			if (index != -1) {
				tempIndexArray.add(new Integer(index));
				fromIndex = index + word.length();
			}
		} while(index != -1);
		return tempIndexArray;
	}


	/** 특정문자를 특정 문자로 바꿔준다.
      * @param strString           원본문자열
      * @param strOld              치환될 문자열
      * @param strNew              치환할 문자열
      * @return Vector
      */
	public static String strGetReplace(String strString, String strOld, String strNew) {
		Vector aResult = new Vector();
		String strResult = "";

		try {
			aResult = strGetSplit(strString, strOld);
			for (int i = 0; i < aResult.size(); i++) {
				strResult += aResult.get(i) + strNew;
			}
			strResult = strResult.substring(0,strResult.length() - strNew.length());
		} catch(Exception e) {
			System.out.println(e.toString());
			return null;
		}
		return strResult;
	}

	
	/** 스트링을 특정 문자를 기준으로 나누어 준다.
      * @param strString           원본문자열
      * @param strDelimeter        분리의 기준이될 문자열
      * @return Vector
      */
	public static Vector strGetSplit(String strString, String strDelimeter) {
		Vector vResult = new Vector();
		int nCount = 0, nLastIndex = 0;
		try {
			nLastIndex = strString.indexOf(strDelimeter);
			if (nLastIndex == -1) {
				vResult.add(0,strString);
			} else {
				while ((strString.indexOf(strDelimeter) > -1)) {
					nLastIndex = strString.indexOf(strDelimeter);
					vResult.add(nCount, strString.substring(0,nLastIndex));
					strString = strString.substring(nLastIndex + strDelimeter.length(), strString.length());
					nCount ++;
				}
				vResult.add(nCount, strString);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		return vResult;
	}


	/** 스트링을 특정 문자를 기준으로 나누어 준다.
      * @param strString              원본문자열
      * @param strDelimeter           분리의 기준이 될 문자열 
      * @return String[]
      */
	public static String[] strSplit(String strString, String strDelimeter) {
		Vector vResult = new Vector();
		Object[] oa = null;
		String[] sa = null;
		int nCount = 0, nLastIndex = 0;
		try {
			nLastIndex = strString.indexOf(strDelimeter);
			if (nLastIndex == -1) {
				vResult.add(0,strString);
			} else {
				while ((strString.indexOf(strDelimeter) > -1)) {
					nLastIndex = strString.indexOf(strDelimeter);
					vResult.add(nCount, strString.substring(0,nLastIndex));
					strString = strString.substring(nLastIndex + strDelimeter.length(), strString.length());
					nCount ++;
				}
				vResult.add(nCount, strString);
			}

			oa = vResult.toArray();
			sa = new String[oa.length];
			for (int i=0;i<oa.length;i++) {
				sa[i] = (String)oa[i];
			}

		} catch (Exception e) {
			return null;
		}
		return sa;
	}



	/** 숫자형테이터를 포맷팅된 문자열로 반환한다.
      * @param int                 숫자값
      * @param int                 생성할 문자열의 길이
      * @param String              빈자리를 채울 문자
      * @return Stirng
      */
	public static String numToStr(int num,int size,String space) {
		return numToStr((long)num,size,space);
	}
	/** 숫자형테이터를 포맷팅된 문자열로 반환한다.
      * @param num                 숫자값
      * @param size                생성할 문자열의 길이
      * @param space               빈자리를 채울 문자
      * @return String
      */
	public static String numToStr(long num,int size,String space) {
		String sNum = String.valueOf(num);
		StringBuffer buf = new StringBuffer("");
		int len = 0;
		try{
		len = sNum.getBytes("EUC_KR").length;
		//len = sNum.getBytes().length;
		}catch(Exception e){
			e.printStackTrace();
		}

		for (int i=len;i<size;i++) {
			buf.append(space);
		}
		if (sNum.substring(0,1).equals("-")) {
			StringBuffer buf2 = new StringBuffer("-").append(buf).append(sNum.substring(1));
			buf = buf2;
		} else {
			buf.append(sNum);
		}

		return buf.toString();
	}
	/** 숫자형테이터를 포맷팅된 문자열로 반환한다.
      * @param num                 숫자값
      * @param size                생성할 문자열의 길이
      * @param space               빈자리를 채울 문자
      * @return String
      */
	public static String numToStr(BigDecimal num,int size,String space) {
		String sNum = num.toString();
		StringBuffer buf = new StringBuffer("");
		int len = 0;
		try{
			len = sNum.getBytes("EUC_KR").length;
		}catch(Exception e){
			e.printStackTrace();
		}

		for (int i=len;i<size;i++) {
			buf.append(space);
		}
		if (sNum.substring(0,1).equals("-")) {
			StringBuffer buf2 = new StringBuffer("-").append(buf).append(sNum.substring(1));
			buf = buf2;
		} else {
			buf.append(sNum);
		}

		return buf.toString();
	}
	/** 입력된 문자를 가지고 부족한 문자열을 길이를 맞춘다.
	 * @param data                원본문자열
	 * @param size                생성할 문자열의 길이
	 * @param space               빈자리를 채울 문자  
	 * @return String
	*/
	public static String fillStr(String data,int size,String space) {
		StringBuffer buf = new StringBuffer(data);
		try{
		
		int len = data.getBytes("EUC_KR").length;

		for (int i=len;i<size;i++) {
			buf.append(space);
		}
		}catch(Exception e){
			e.printStackTrace();
		}

		return buf.toString();
	}


	/** 입력된 문자열이 NULL인 경우, 디폴트문자열로 치환한다.
	 * @param data                  원본문자열
	 * @param replace               치환할 디폴트문자열
	 * @return String
	*/
	public static String replaceNull(String data,String replace) {

		if( data == null ) {
			return replace;
		} else {
	        return data;
		}
        
	}

	/** 입력된 문자열이 NULL 이나 empty스트링(only space문자열 포함)인 경우, 디폴트문자열로 치환한다.
	 * @param data                  원본문자열
	 * @param default               치환할 디폴트문자열
	 * @return String
	*/
	public static String replaceEmpty(String data,String replace) {
    
		if( data == null || data.trim().equals("") ) {
			return replace;
		} else {
	        return data;
		}
        
	}

/**[공통](method) 한글포함_문자열의 size초과 byte 제거한다.
     @param String sStr - 문자열
     @param int         - iSize 요구사이즈
     @return String     - 크기가 조절된 문자열
   */
  public static String removeExceedData(String sStr, int iSize) throws Exception
  {  
    try
    {
      /* 요구된 size이상의 문자를 제거한다. */
      byte[] bTemp = sStr.getBytes("EUC_KR");
      
      if (bTemp.length > iSize)  {  sStr = new String(bTemp, 0, iSize, "KSC5601");  }
    }
    catch(Exception e)
    {
      throw e;
    }
    
    return sStr;
  } 
  
  /**[공통](method) 파일path와 파일명에 맞게 파일을 생성한다.
     @param String sFilePath - 파일path
     @param String sFileName - 파일명
     @param String sCrtStr   - 파일내용
     @return boolean         - 성공여부
   */
  public static boolean creatFile(String sFilePath, String sFileName, String sCrtStr) throws Exception
  {  
    try
    {
      // 파일을 생성한다. 
      File file = new File(sFilePath, sFileName);
      String fullPath = file.getAbsolutePath();
      FileWriter writer = new FileWriter(fullPath, false);
      PrintWriter printWriter = new PrintWriter(new BufferedWriter(writer), true);
      printWriter.println(sCrtStr);      
      printWriter.close();
    }
    catch(Exception e)
    {
      throw e;
    }
    
    return true;
  } 

  /**
   * [공통](method) 일련의 string정보를 size 배열에 맞게끔 쪼개어 Arrary에 저장
   *
   * @param String sTotStr   - 정보스트링
   * @param int    iLineCnt  - 데이터건수 
   * @param int[]  iSect     - 스트링을 자르기 위한 size 배열 
   * @return String[][]      - 쪼개어진 정보 2차원배열 
   */
  public static String[][] extractData(String sTotStr, int iLineCnt, int[] iSect)
  {
    String[][] sTempStr = new String[iLineCnt][iSect.length];
    int iColCnt = iSect.length;
    int iCnt = 0;
    int iColLoc = 0;

    try
    {
      for (int i = 0; i < iLineCnt; i++)
      {
        for (int j = 0; j < iSect.length; j++)
        {
          sTempStr[i][j] = subStringKor(sTotStr, iColLoc, iColLoc + iSect[j]);
          iColLoc = iColLoc + iSect[j];
        }
      }    
    }
    catch(Exception e)
    {
      System.out.println(e.toString());
      return null;
    }
    
    return sTempStr;    
  }

 /**
   * [공통](method) 한글포함 스트링의 substring method
   *
   * 만든목적: 한글이 포함된 String의 경우 2byte인 한글을 한자리로 인식을
   *           하여 substring에서의 문제점이 발생한다.
   * @param    String strString : input string
   * @param    int    nStart    : start string location
   * @param    int    nEndLoc   : end string location
   * @return   String           - substring된 문자열
   */
  public static String subStringKor(String strString, int nStartIndex, int nEndLoc)
  {
    try
    {
      byte[] bTemp = strString.getBytes("EUC_KR");
      byte[] bRst = new byte[nEndLoc-nStartIndex];
      
      try
      {
        for (int i = 0; i < nEndLoc-nStartIndex; i++)
        {
          bRst[i] = bTemp[nStartIndex+i];
        }
      }
      catch(Exception e)
      {
        System.out.println(e.toString());
        return null;
      }
  
      strString = new String(bRst, "KSC5601");
    }
    catch(Exception e)
    {
      System.out.println(e.toString());
      return null;
    }
      
    return strString;
  }
 /** 해당 문자에 입력받는 문자열을 앞에 추가시켜준다.                 
    @param data      원본문자열                                       
    @param size      생성할 문자열의 길이                             
    @param space     빈자리를 채울 문자                               
    @return String                                                    
 */                                                                   
 public static String fillLeftStr(String data, int size, String space)
 {                                                                    
   StringBuffer sBuff    = new StringBuffer("");
   int nDataLen = 0;
   try{                      
   	nDataLen = data.getBytes("EUC_KR").length;    // 원래 데이타 길이      
   }catch(Exception e){
   	e.printStackTrace();
   }
   int nFillLen  = size - nDataLen;          // 해당채울 문자의 길이  
                                                                      
                                                                      
   for(int i=0; i< nFillLen; i++)                                     
   {                                                                  
     sBuff.append(space);                                             
   }                                                                  
                                                                      
   sBuff.append(data);                                                
   return sBuff.toString();                                           
 }            
 /** 입력된 문자를 가지고 부족한 문자열을 길이를 맞춘다.
	 * @param data                원본문자열
	 * @param size                생성할 문자열의 길이
	 * @param space               빈자리를 채울 문자  
	 * @return String
	*/
	public static String fillCha(String data, int size, String space) 
	{
		StringBuffer 	buf = new StringBuffer(data);                      
		
		int strlen = 0;
		for(int j = 0; j < data.length(); j++)
		{
			char c = data.charAt(j);    		
			if ( c  <  0xac00 || 0xd7a3 < c )
			{
				strlen++;
			} 
			else
			{  
				strlen+=2;  //한글이다..    		
			} 
		}
		
		for (int i = strlen; i < size; i++) 
		{
			buf.append(space);
		}
	
		return buf.toString();
	}
	  /** 특수문자를 제거하는 메서드_해당 숫자만 뽑아냄
   @param s     특수문자를 제거할 스트링
   @return String
  */
  public static String removeSpeChar(String s)
  {
    StringBuffer sBuff = new StringBuffer("");
    
    char[] chData = s.toCharArray();
        
    int nIndex = 0;
    int nLen   = chData.length;    
    int[] nData = new int[nLen];
    
   
    // Step1 : 우선 char배열로 변환된것중에 숫자만 찾아서 int배열에 넣는다.    
    for (int i = 0; i < nLen; i++)
    {
      if( Character.getNumericValue(chData[i]) == 0 ||
          Character.getNumericValue(chData[i]) == 1 ||
          Character.getNumericValue(chData[i]) == 2 ||
          Character.getNumericValue(chData[i]) == 3 ||
          Character.getNumericValue(chData[i]) == 4 ||
          Character.getNumericValue(chData[i]) == 5 ||
          Character.getNumericValue(chData[i]) == 6 ||
          Character.getNumericValue(chData[i]) == 7 ||
          Character.getNumericValue(chData[i]) == 8 ||
          Character.getNumericValue(chData[i]) == 9 )
      {
        nData[nIndex++] = Character.getNumericValue(chData[i]);        
      }
      
    }
    
    // Step 2 : int배열에 든 값들을 하나의 스트링로 만들어준다.    
    for(int j = 0; j < nIndex; j++)
    {
      sBuff.append(nData[j]);
    }
    
    return sBuff.toString(); 
    
    
  }
  
  /** 특수문자를 제거하는 메서드(배열형태로 처리)
   @param s     특수문자를 제거할 스트링 배열
   @return String[]
  */
  public static String[] removeSpeCharArray(String[] s)
  {
    int nLen = s.length;
    
    String[] aStr = new String[nLen];
    
    
    for(int i =0; i < nLen; i++)
    {
      aStr[i] = removeSpeChar(s[i]);
    }
    
    return aStr;    
  }
  /** string의 끝부분 특정문자를 제거.
     @param String str      - 처리대상 문자열
     @param String specChar - 제거할 특정문자
     @return String         - 끝부분의 특정문자가 제거된 문자열
   */
  public static String removeEndKorChar(String str, String specChar) throws Exception {
    String sRet  = "";
    String sTemp = "";
    
    int iSt = 0;
    int iEd = 1;
    int iStrSize = 0;
    
 		try {
      iStrSize = str.length();
      iSt = iStrSize - 1;
      iEd = iStrSize;
      
      sRet = str;
      
      while (iSt >= 0) { 
        sTemp = str.substring(iSt, iEd); 
        
        if (!sTemp.equals(specChar)) break;
  
        iSt--;
        iEd--;
      }

      sRet = str.substring(0, iEd);
		} catch(Exception e) {
			System.out.println(e.toString());
			throw e;
		}
		
    return sRet;
  }
    /**[공통](method) 문자열중 특수문자를 KSC-5601 2 byte 완성형 한글코드로 변환
     @param  String sStr - 문자열
     @return String     - 변환된 문자열
   */
  public static String convertToKsc(String sStr) throws Exception  {   
    char[] ch;

    try {

      ch = sStr.toCharArray();

      for (int i = 0; i < ch.length; i++) {
        if      (ch[i] == '1')  ch[i] = '１';
        else if (ch[i] == '2')  ch[i] = '２';
        else if (ch[i] == '3')  ch[i] = '３';
        else if (ch[i] == '4')  ch[i] = '４';
        else if (ch[i] == '5')  ch[i] = '５';
        else if (ch[i] == '6')  ch[i] = '６';
        else if (ch[i] == '7')  ch[i] = '７';
        else if (ch[i] == '8')  ch[i] = '８';
        else if (ch[i] == '9')  ch[i] = '９';
        else if (ch[i] == '0')  ch[i] = '０';
        else if (ch[i] == '~')  ch[i] = '～';
        else if (ch[i] == '`')  ch[i] = '｀';
        else if (ch[i] == '!')  ch[i] = '！';
        else if (ch[i] == '@')  ch[i] = '＠';
        else if (ch[i] == '#')  ch[i] = '＃';
        else if (ch[i] == '$')  ch[i] = '＄';
        else if (ch[i] == '%')  ch[i] = '％';
        else if (ch[i] == '^')  ch[i] = '＾';
        else if (ch[i] == '&')  ch[i] = '＆';
        else if (ch[i] == '*')  ch[i] = '＊';
        else if (ch[i] == '(')  ch[i] = '（';
        else if (ch[i] == ')')  ch[i] = '）';
        else if (ch[i] == '_')  ch[i] = '＿';
        else if (ch[i] == '-')  ch[i] = '－';
        else if (ch[i] == '+')  ch[i] = '＋';
        else if (ch[i] == '=')  ch[i] = '＝';
        else if (ch[i] == '{')  ch[i] = '｛';
        else if (ch[i] == '}')  ch[i] = '｝';
        else if (ch[i] == '[')  ch[i] = '［';
        else if (ch[i] == ']')  ch[i] = '］';
        else if (ch[i] == '|')  ch[i] = '｜';
        else if (ch[i] == '\\') ch[i] = '￦';
        else if (ch[i] == ':')  ch[i] = '：';
        else if (ch[i] == ';')  ch[i] = '；';
        else if (ch[i] == '"') ch[i] = '＂';
        else if (ch[i] == '\'') ch[i] = '＇';
        else if (ch[i] == '<')  ch[i] = '＜';
        else if (ch[i] == '>')  ch[i] = '＞';
        else if (ch[i] == ',')  ch[i] = '，';
        else if (ch[i] == '.')  ch[i] = '．';
        else if (ch[i] == '?')  ch[i] = '？';
        else if (ch[i] == '/')  ch[i] = '／';
        else if (ch[i] == ' ')  ch[i] = '　';

        else if (ch[i] == 'a')  ch[i] = 'ａ';
        else if (ch[i] == 'b')  ch[i] = 'ｂ';
        else if (ch[i] == 'c')  ch[i] = 'ｃ';
        else if (ch[i] == 'd')  ch[i] = 'ｄ';
        else if (ch[i] == 'e')  ch[i] = 'ｅ';
        else if (ch[i] == 'f')  ch[i] = 'ｆ';
        else if (ch[i] == 'g')  ch[i] = 'ｇ';
        else if (ch[i] == 'h')  ch[i] = 'ｈ';
        else if (ch[i] == 'i')  ch[i] = 'ｉ';
        else if (ch[i] == 'j')  ch[i] = 'ｊ';
        else if (ch[i] == 'k')  ch[i] = 'ｋ';
        else if (ch[i] == 'l')  ch[i] = 'ｌ';
        else if (ch[i] == 'm')  ch[i] = 'ｍ';
        else if (ch[i] == 'n')  ch[i] = 'ｎ';
        else if (ch[i] == 'o')  ch[i] = 'ｏ';
        else if (ch[i] == 'p')  ch[i] = 'ｐ';
        else if (ch[i] == 'q')  ch[i] = 'ｑ';
        else if (ch[i] == 'r')  ch[i] = 'ｒ';
        else if (ch[i] == 's')  ch[i] = 'ｓ';
        else if (ch[i] == 't')  ch[i] = 'ｔ';
        else if (ch[i] == 'u')  ch[i] = 'ｕ';
        else if (ch[i] == 'v')  ch[i] = 'ｖ';
        else if (ch[i] == 'w')  ch[i] = 'ｗ';
        else if (ch[i] == 'x')  ch[i] = 'ｘ';
        else if (ch[i] == 'y')  ch[i] = 'ｙ';
        else if (ch[i] == 'z')  ch[i] = 'ｚ';
        else if (ch[i] == 'A')  ch[i] = 'Ａ';
        else if (ch[i] == 'B')  ch[i] = 'Ｂ';
        else if (ch[i] == 'C')  ch[i] = 'Ｃ';
        else if (ch[i] == 'D')  ch[i] = 'Ｄ';
        else if (ch[i] == 'E')  ch[i] = 'Ｅ';
        else if (ch[i] == 'F')  ch[i] = 'Ｆ';
        else if (ch[i] == 'G')  ch[i] = 'Ｇ';
        else if (ch[i] == 'H')  ch[i] = 'Ｈ';
        else if (ch[i] == 'I')  ch[i] = 'Ｉ';
        else if (ch[i] == 'J')  ch[i] = 'Ｊ';
        else if (ch[i] == 'K')  ch[i] = 'Ｋ';
        else if (ch[i] == 'L')  ch[i] = 'Ｌ';
        else if (ch[i] == 'M')  ch[i] = 'Ｍ';
        else if (ch[i] == 'N')  ch[i] = 'Ｎ';
        else if (ch[i] == 'O')  ch[i] = 'Ｏ';
        else if (ch[i] == 'P')  ch[i] = 'Ｐ';
        else if (ch[i] == 'Q')  ch[i] = 'Ｑ';
        else if (ch[i] == 'R')  ch[i] = 'Ｒ';
        else if (ch[i] == 'S')  ch[i] = 'Ｓ';
        else if (ch[i] == 'T')  ch[i] = 'Ｔ';
        else if (ch[i] == 'U')  ch[i] = 'Ｕ';
        else if (ch[i] == 'V')  ch[i] = 'Ｖ';
        else if (ch[i] == 'W')  ch[i] = 'Ｗ';
        else if (ch[i] == 'X')  ch[i] = 'Ｘ';
        else if (ch[i] == 'Y')  ch[i] = 'Ｙ';
        else if (ch[i] == 'Z')  ch[i] = 'Ｚ';
        

      }
    } catch(Exception e) {
      throw e;
    }
    
    return String.valueOf(ch);
  } 
   
  
  /**
  입력된 전화번호를 Format에 맞게 변경하여 리턴한다.
     @param  String TelNum - 전화번호
     @return String     - 변환된 전화번호
   */
  public static String formatTelNum(String s) {
   
    String sTel        = "";
  	String sTelCheck   = "";
  	StringBuffer sBuff = new StringBuffer("");
    try{
      char[] chData = s.toCharArray();
          
      int nIndex  = 0;
      int nLen    = chData.length;    
      int[] nData = new int[nLen];    
     
      // Step1 : 우선 char배열로 변환된것중에 숫자만 찾아서 int배열에 넣는다.    
      for (int i = 0; i < nLen; i++)
      {
        if( Character.getNumericValue(chData[i]) == 0 ||
            Character.getNumericValue(chData[i]) == 1 ||
            Character.getNumericValue(chData[i]) == 2 ||
            Character.getNumericValue(chData[i]) == 3 ||
            Character.getNumericValue(chData[i]) == 4 ||
            Character.getNumericValue(chData[i]) == 5 ||
            Character.getNumericValue(chData[i]) == 6 ||
            Character.getNumericValue(chData[i]) == 7 ||
            Character.getNumericValue(chData[i]) == 8 ||
            Character.getNumericValue(chData[i]) == 9 )
        {
          nData[nIndex++] = Character.getNumericValue(chData[i]);        
        }
        
      }
      
      // Step 2 : int배열에 든 값들을 하나의 스트링로 만들어준다.    
      for(int j = 0; j < nIndex; j++)
      {
        sBuff.append(nData[j]);
      }
     	
     	sTel = sBuff.toString();
     	System.out.println("sTel=" +sTel);
     	sTelCheck = sTel.substring(0,2);
      if (sTelCheck.equals("02")) {
     		if (sTel.length() == 9) {
     		  sBuff.insert(2,"-");
     		  sBuff.insert(6,"-");
     	  }else if (sTel.length() == 10) {
     		  sBuff.insert(2,"-");
     		  sBuff.insert(7,"-");
     	  }else { }	 
     	//서울이 아닌 경우	
     	} else {
     		if (sTel.length() == 10) {
     		  sBuff.insert(3,"-");
     		  sBuff.insert(7,"-");
     	  }else if (sTel.length() == 11) {
     		  sBuff.insert(3,"-");
     		  sBuff.insert(8,"-");
     	  }else { }
     	}
    }catch(Exception e){
      e.toString();
    }	
    return sBuff.toString();
  }
   /** [한글형타입]입력된 문자를 가지고 부족한 문자열을 길이를 맞춘다.
	 * @param data                원본문자열
	 * @param size                생성할 문자열의 길이
	 * @param space             빈자리를 채울 한글형문자  
	 * @return String
	*/
	public static String fillStrHan(String data,int size,String space) {
		StringBuffer buf = new StringBuffer(data);

		try{
  			int len = data.getBytes("EUC_KR").length;
  			int jumplen = space.getBytes("EUC_KR").length;

		  	for (int i=len; i<size; i += jumplen) {
			  	buf.append(space);
	  		}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return buf.toString();
	}
  
}//end fo file