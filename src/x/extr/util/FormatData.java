/** Main system          : foundation
  * Sub system           : util
  * Classname            : FormatData.java
  * Initial date         : 2005.11.14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ����Ÿ �����ð��� ��ƿ��Ƽ Class
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



/** ����Ÿ �����ð��� ��ƿ��Ƽ Class
  * @author Ȳ��õ
*/
public class FormatData {


	/** �ٲٰ��� �ϴ� ��Ʈ���� �ε��� ������ ���Ѵ�.
      * @param sql              ���ڿ�
      * @param word             ã�� ���ڿ�
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


	/** Ư�����ڸ� Ư�� ���ڷ� �ٲ��ش�.
      * @param strString           �������ڿ�
      * @param strOld              ġȯ�� ���ڿ�
      * @param strNew              ġȯ�� ���ڿ�
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

	
	/** ��Ʈ���� Ư�� ���ڸ� �������� ������ �ش�.
      * @param strString           �������ڿ�
      * @param strDelimeter        �и��� �����̵� ���ڿ�
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


	/** ��Ʈ���� Ư�� ���ڸ� �������� ������ �ش�.
      * @param strString              �������ڿ�
      * @param strDelimeter           �и��� ������ �� ���ڿ� 
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



	/** �����������͸� �����õ� ���ڿ��� ��ȯ�Ѵ�.
      * @param int                 ���ڰ�
      * @param int                 ������ ���ڿ��� ����
      * @param String              ���ڸ��� ä�� ����
      * @return Stirng
      */
	public static String numToStr(int num,int size,String space) {
		return numToStr((long)num,size,space);
	}
	/** �����������͸� �����õ� ���ڿ��� ��ȯ�Ѵ�.
      * @param num                 ���ڰ�
      * @param size                ������ ���ڿ��� ����
      * @param space               ���ڸ��� ä�� ����
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
	/** �����������͸� �����õ� ���ڿ��� ��ȯ�Ѵ�.
      * @param num                 ���ڰ�
      * @param size                ������ ���ڿ��� ����
      * @param space               ���ڸ��� ä�� ����
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
	/** �Էµ� ���ڸ� ������ ������ ���ڿ��� ���̸� �����.
	 * @param data                �������ڿ�
	 * @param size                ������ ���ڿ��� ����
	 * @param space               ���ڸ��� ä�� ����  
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


	/** �Էµ� ���ڿ��� NULL�� ���, ����Ʈ���ڿ��� ġȯ�Ѵ�.
	 * @param data                  �������ڿ�
	 * @param replace               ġȯ�� ����Ʈ���ڿ�
	 * @return String
	*/
	public static String replaceNull(String data,String replace) {

		if( data == null ) {
			return replace;
		} else {
	        return data;
		}
        
	}

	/** �Էµ� ���ڿ��� NULL �̳� empty��Ʈ��(only space���ڿ� ����)�� ���, ����Ʈ���ڿ��� ġȯ�Ѵ�.
	 * @param data                  �������ڿ�
	 * @param default               ġȯ�� ����Ʈ���ڿ�
	 * @return String
	*/
	public static String replaceEmpty(String data,String replace) {
    
		if( data == null || data.trim().equals("") ) {
			return replace;
		} else {
	        return data;
		}
        
	}

/**[����](method) �ѱ�����_���ڿ��� size�ʰ� byte �����Ѵ�.
     @param String sStr - ���ڿ�
     @param int         - iSize �䱸������
     @return String     - ũ�Ⱑ ������ ���ڿ�
   */
  public static String removeExceedData(String sStr, int iSize) throws Exception
  {  
    try
    {
      /* �䱸�� size�̻��� ���ڸ� �����Ѵ�. */
      byte[] bTemp = sStr.getBytes("EUC_KR");
      
      if (bTemp.length > iSize)  {  sStr = new String(bTemp, 0, iSize, "KSC5601");  }
    }
    catch(Exception e)
    {
      throw e;
    }
    
    return sStr;
  } 
  
  /**[����](method) ����path�� ���ϸ� �°� ������ �����Ѵ�.
     @param String sFilePath - ����path
     @param String sFileName - ���ϸ�
     @param String sCrtStr   - ���ϳ���
     @return boolean         - ��������
   */
  public static boolean creatFile(String sFilePath, String sFileName, String sCrtStr) throws Exception
  {  
    try
    {
      // ������ �����Ѵ�. 
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
   * [����](method) �Ϸ��� string������ size �迭�� �°Բ� �ɰ��� Arrary�� ����
   *
   * @param String sTotStr   - ������Ʈ��
   * @param int    iLineCnt  - �����ͰǼ� 
   * @param int[]  iSect     - ��Ʈ���� �ڸ��� ���� size �迭 
   * @return String[][]      - �ɰ����� ���� 2�����迭 
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
   * [����](method) �ѱ����� ��Ʈ���� substring method
   *
   * �������: �ѱ��� ���Ե� String�� ��� 2byte�� �ѱ��� ���ڸ��� �ν���
   *           �Ͽ� substring������ �������� �߻��Ѵ�.
   * @param    String strString : input string
   * @param    int    nStart    : start string location
   * @param    int    nEndLoc   : end string location
   * @return   String           - substring�� ���ڿ�
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
 /** �ش� ���ڿ� �Է¹޴� ���ڿ��� �տ� �߰������ش�.                 
    @param data      �������ڿ�                                       
    @param size      ������ ���ڿ��� ����                             
    @param space     ���ڸ��� ä�� ����                               
    @return String                                                    
 */                                                                   
 public static String fillLeftStr(String data, int size, String space)
 {                                                                    
   StringBuffer sBuff    = new StringBuffer("");
   int nDataLen = 0;
   try{                      
   	nDataLen = data.getBytes("EUC_KR").length;    // ���� ����Ÿ ����      
   }catch(Exception e){
   	e.printStackTrace();
   }
   int nFillLen  = size - nDataLen;          // �ش�ä�� ������ ����  
                                                                      
                                                                      
   for(int i=0; i< nFillLen; i++)                                     
   {                                                                  
     sBuff.append(space);                                             
   }                                                                  
                                                                      
   sBuff.append(data);                                                
   return sBuff.toString();                                           
 }            
 /** �Էµ� ���ڸ� ������ ������ ���ڿ��� ���̸� �����.
	 * @param data                �������ڿ�
	 * @param size                ������ ���ڿ��� ����
	 * @param space               ���ڸ��� ä�� ����  
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
				strlen+=2;  //�ѱ��̴�..    		
			} 
		}
		
		for (int i = strlen; i < size; i++) 
		{
			buf.append(space);
		}
	
		return buf.toString();
	}
	  /** Ư�����ڸ� �����ϴ� �޼���_�ش� ���ڸ� �̾Ƴ�
   @param s     Ư�����ڸ� ������ ��Ʈ��
   @return String
  */
  public static String removeSpeChar(String s)
  {
    StringBuffer sBuff = new StringBuffer("");
    
    char[] chData = s.toCharArray();
        
    int nIndex = 0;
    int nLen   = chData.length;    
    int[] nData = new int[nLen];
    
   
    // Step1 : �켱 char�迭�� ��ȯ�Ȱ��߿� ���ڸ� ã�Ƽ� int�迭�� �ִ´�.    
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
    
    // Step 2 : int�迭�� �� ������ �ϳ��� ��Ʈ���� ������ش�.    
    for(int j = 0; j < nIndex; j++)
    {
      sBuff.append(nData[j]);
    }
    
    return sBuff.toString(); 
    
    
  }
  
  /** Ư�����ڸ� �����ϴ� �޼���(�迭���·� ó��)
   @param s     Ư�����ڸ� ������ ��Ʈ�� �迭
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
  /** string�� ���κ� Ư�����ڸ� ����.
     @param String str      - ó����� ���ڿ�
     @param String specChar - ������ Ư������
     @return String         - ���κ��� Ư�����ڰ� ���ŵ� ���ڿ�
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
    /**[����](method) ���ڿ��� Ư�����ڸ� KSC-5601 2 byte �ϼ��� �ѱ��ڵ�� ��ȯ
     @param  String sStr - ���ڿ�
     @return String     - ��ȯ�� ���ڿ�
   */
  public static String convertToKsc(String sStr) throws Exception  {   
    char[] ch;

    try {

      ch = sStr.toCharArray();

      for (int i = 0; i < ch.length; i++) {
        if      (ch[i] == '1')  ch[i] = '��';
        else if (ch[i] == '2')  ch[i] = '��';
        else if (ch[i] == '3')  ch[i] = '��';
        else if (ch[i] == '4')  ch[i] = '��';
        else if (ch[i] == '5')  ch[i] = '��';
        else if (ch[i] == '6')  ch[i] = '��';
        else if (ch[i] == '7')  ch[i] = '��';
        else if (ch[i] == '8')  ch[i] = '��';
        else if (ch[i] == '9')  ch[i] = '��';
        else if (ch[i] == '0')  ch[i] = '��';
        else if (ch[i] == '~')  ch[i] = '��';
        else if (ch[i] == '`')  ch[i] = '��';
        else if (ch[i] == '!')  ch[i] = '��';
        else if (ch[i] == '@')  ch[i] = '��';
        else if (ch[i] == '#')  ch[i] = '��';
        else if (ch[i] == '$')  ch[i] = '��';
        else if (ch[i] == '%')  ch[i] = '��';
        else if (ch[i] == '^')  ch[i] = '��';
        else if (ch[i] == '&')  ch[i] = '��';
        else if (ch[i] == '*')  ch[i] = '��';
        else if (ch[i] == '(')  ch[i] = '��';
        else if (ch[i] == ')')  ch[i] = '��';
        else if (ch[i] == '_')  ch[i] = '��';
        else if (ch[i] == '-')  ch[i] = '��';
        else if (ch[i] == '+')  ch[i] = '��';
        else if (ch[i] == '=')  ch[i] = '��';
        else if (ch[i] == '{')  ch[i] = '��';
        else if (ch[i] == '}')  ch[i] = '��';
        else if (ch[i] == '[')  ch[i] = '��';
        else if (ch[i] == ']')  ch[i] = '��';
        else if (ch[i] == '|')  ch[i] = '��';
        else if (ch[i] == '\\') ch[i] = '��';
        else if (ch[i] == ':')  ch[i] = '��';
        else if (ch[i] == ';')  ch[i] = '��';
        else if (ch[i] == '"') ch[i] = '��';
        else if (ch[i] == '\'') ch[i] = '��';
        else if (ch[i] == '<')  ch[i] = '��';
        else if (ch[i] == '>')  ch[i] = '��';
        else if (ch[i] == ',')  ch[i] = '��';
        else if (ch[i] == '.')  ch[i] = '��';
        else if (ch[i] == '?')  ch[i] = '��';
        else if (ch[i] == '/')  ch[i] = '��';
        else if (ch[i] == ' ')  ch[i] = '��';

        else if (ch[i] == 'a')  ch[i] = '��';
        else if (ch[i] == 'b')  ch[i] = '��';
        else if (ch[i] == 'c')  ch[i] = '��';
        else if (ch[i] == 'd')  ch[i] = '��';
        else if (ch[i] == 'e')  ch[i] = '��';
        else if (ch[i] == 'f')  ch[i] = '��';
        else if (ch[i] == 'g')  ch[i] = '��';
        else if (ch[i] == 'h')  ch[i] = '��';
        else if (ch[i] == 'i')  ch[i] = '��';
        else if (ch[i] == 'j')  ch[i] = '��';
        else if (ch[i] == 'k')  ch[i] = '��';
        else if (ch[i] == 'l')  ch[i] = '��';
        else if (ch[i] == 'm')  ch[i] = '��';
        else if (ch[i] == 'n')  ch[i] = '��';
        else if (ch[i] == 'o')  ch[i] = '��';
        else if (ch[i] == 'p')  ch[i] = '��';
        else if (ch[i] == 'q')  ch[i] = '��';
        else if (ch[i] == 'r')  ch[i] = '��';
        else if (ch[i] == 's')  ch[i] = '��';
        else if (ch[i] == 't')  ch[i] = '��';
        else if (ch[i] == 'u')  ch[i] = '��';
        else if (ch[i] == 'v')  ch[i] = '��';
        else if (ch[i] == 'w')  ch[i] = '��';
        else if (ch[i] == 'x')  ch[i] = '��';
        else if (ch[i] == 'y')  ch[i] = '��';
        else if (ch[i] == 'z')  ch[i] = '��';
        else if (ch[i] == 'A')  ch[i] = '��';
        else if (ch[i] == 'B')  ch[i] = '��';
        else if (ch[i] == 'C')  ch[i] = '��';
        else if (ch[i] == 'D')  ch[i] = '��';
        else if (ch[i] == 'E')  ch[i] = '��';
        else if (ch[i] == 'F')  ch[i] = '��';
        else if (ch[i] == 'G')  ch[i] = '��';
        else if (ch[i] == 'H')  ch[i] = '��';
        else if (ch[i] == 'I')  ch[i] = '��';
        else if (ch[i] == 'J')  ch[i] = '��';
        else if (ch[i] == 'K')  ch[i] = '��';
        else if (ch[i] == 'L')  ch[i] = '��';
        else if (ch[i] == 'M')  ch[i] = '��';
        else if (ch[i] == 'N')  ch[i] = '��';
        else if (ch[i] == 'O')  ch[i] = '��';
        else if (ch[i] == 'P')  ch[i] = '��';
        else if (ch[i] == 'Q')  ch[i] = '��';
        else if (ch[i] == 'R')  ch[i] = '��';
        else if (ch[i] == 'S')  ch[i] = '��';
        else if (ch[i] == 'T')  ch[i] = '��';
        else if (ch[i] == 'U')  ch[i] = '��';
        else if (ch[i] == 'V')  ch[i] = '��';
        else if (ch[i] == 'W')  ch[i] = '��';
        else if (ch[i] == 'X')  ch[i] = '��';
        else if (ch[i] == 'Y')  ch[i] = '��';
        else if (ch[i] == 'Z')  ch[i] = '��';
        

      }
    } catch(Exception e) {
      throw e;
    }
    
    return String.valueOf(ch);
  } 
   
  
  /**
  �Էµ� ��ȭ��ȣ�� Format�� �°� �����Ͽ� �����Ѵ�.
     @param  String TelNum - ��ȭ��ȣ
     @return String     - ��ȯ�� ��ȭ��ȣ
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
     
      // Step1 : �켱 char�迭�� ��ȯ�Ȱ��߿� ���ڸ� ã�Ƽ� int�迭�� �ִ´�.    
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
      
      // Step 2 : int�迭�� �� ������ �ϳ��� ��Ʈ���� ������ش�.    
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
     	//������ �ƴ� ���	
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
   /** [�ѱ���Ÿ��]�Էµ� ���ڸ� ������ ������ ���ڿ��� ���̸� �����.
	 * @param data                �������ڿ�
	 * @param size                ������ ���ڿ��� ����
	 * @param space             ���ڸ��� ä�� �ѱ�������  
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