package util;

import java.util.*;

/**
	 * ������ : 2006�� 2�� 09��
	 * <p>
	 * ������ :
	 * <p>
	 * ������ :
	 * <p>
	 * ���� : 
	 * <p>
	 * String ������ ������ ���� Ŭ����
	 * <p>
	 * �ε����ͳ� ������ ���ڿ��� ���� ó���� ���ش�. 
*/
public class StringUtil {
	/**
	 * �ý��ۿ� ���� ���� ������
	 */
	public static String lineSeparator = System.getProperty("line.separator");
	
	/**
	 * �ý��ۿ� ���� ���� ������
	 */
	public static String fileSeparator = System.getProperty("file.separator");
	private StringUtil(){
	}
	
	private static final int ASCII[] = {
                0x0020, 0x0021, 0x0022, 0x0023, 0x0024, 0x0025, 0x0026, 0x0027, 0x0028,
                0x0029, 0x002a, 0x002b, 0x002c, 0x002d, 0x002e, 0x002f,
                0x0030, 0x0031, 0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037, 0x0038,
                0x0039, 0x003a, 0x003b, 0x003c, 0x003d, 0x003e, 0x003f,
                0x0040, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048,
                0x0049, 0x004a, 0x004b, 0x004c, 0x004d, 0x004e, 0x004f,
                0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058,
                0x0059, 0x005a, 0x005b, 0x005c, 0x005d, 0x005e, 0x005f,
                0x0060, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067, 0x0068,
                0x0069, 0x006a, 0x006b, 0x006c, 0x006d, 0x006e, 0x006f,
                0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077, 0x0078,
                0x0079, 0x007a, 0x007b, 0x007c, 0x007d, 0x007e
	};      
	private static final int EBCDIC[] = {
                0x0040, 0x005a, 0x007f, 0x007b, 0x005b, 0x006c, 0x0050, 0x007d, 0x004d,
                0x005d, 0x005c, 0x004e, 0x006b, 0x0060, 0x004b, 0x0061,
                0x00f0, 0x00f1, 0x00f2, 0x00f3, 0x00f4, 0x00f5, 0x00f6, 0x00f7, 0x00f8,
                0x00f9, 0x007a, 0x005e, 0x004c, 0x007e, 0x006e, 0x006f,
                0x007c, 0x00c1, 0x00c2, 0x00c3, 0x00c4, 0x00c5, 0x00c6, 0x00c7, 0x00c8,
                0x00c9, 0x00d1, 0x00d2, 0x00d3, 0x00d4, 0x00d5, 0x00d6,
                0x00d7, 0x00d8, 0x00d9, 0x00e2, 0x00e3, 0x00e4, 0x00e5, 0x00e6, 0x00e7,
                0x00e8, 0x00e9, 0x00ad, 0x00e0, 0x00bd, 0x005f, 0x006d,
                0x0079, 0x0081, 0x0082, 0x0083, 0x0084, 0x0085, 0x0086, 0x0087, 0x0088,
                0x0089, 0x0091, 0x0092, 0x0093, 0x0094, 0x0095, 0x0096,
                0x0097, 0x0098, 0x0099, 0x00a2, 0x00a3, 0x00a4, 0x00a5, 0x00a6, 0x00a7,
                0x00a8, 0x00a9, 0x00c0, 0x006a, 0x00d0, 0x00a1
	};    
	
	/**
	 * �Էµ� �ҽ��� ���ڿ��� NULL �ϰ�� ""�� ����
	 * @return a String
	 * @param String to set
	 */
	public static String NVL(String strIn)
    {
        return (strIn == null ? "" : strIn);
    }
    
    
    /**
	 * �Էµ� �ҽ��� ���ڿ��� null�� ��� ���ϴ� ���ڿ��� ����
	 * @return a String
	 * @param String to set - source
	 * @param String to set - ����� String
	 */
    public static String NVL(String strIn, String strOut){
    	return (strIn == null ? strOut : strIn);
    }


	/**
	 * �Էµ� �ҽ��� ���ڿ��� null �̰ų� ""�̰�� ���ϴ� ���ڿ��� ����
	 * @return a String
	 * @param String to set - source
	 * @param String to set - ����� String
	 */    
    public static String EVL(String strIn, String strOut){
    	return (strIn == null || strIn.equals("") ? strOut : strIn);
    }
    
    /**
	 * �Էµ� �ҽ��� ���ڿ��� �ش� ���ڰ� ������� ������ ����
	 * @return a String
	 * @param String to set - source
	 * @param char to set - ����� ����
	 */    
    public static String REMOVE(String strIn, char delchar){
    	strIn = NVL(strIn);
    	strIn = strIn.replace(delchar,'\0');
    	return strIn;
    }

	/**
	 * �Էµ� �ҽ��� ���ڿ��� �ش� ���ڿ��� ������� ���ڿ� ����
	 * @return a String
	 * @param String to set - source
	 * @param char to set - ������ ����
	 * @param char to set - ����� ����
	 */        
    public static String CHSTRING(String source, String delim, String chstr){
    	source = NVL(source);
    	StringTokenizer strToken = new StringTokenizer(source, delim);
    	String strRtn = "";
    	int nCnt = 0;
    	while(strToken.hasMoreElements()){
    		if(nCnt > 0){
    			strRtn += chstr;
    		}
    		nCnt++;
    		strRtn += strToken.nextElement();
    	}
    	return strRtn;
    }
    
    /**
	* ���ڸ� ���ڿ��� �ٲٴ� �Լ� 
	* @param int to Set : ����
	* @return a String
	*/
	public static String cStr(int val) {
		Integer VAL = new Integer(val);
		return VAL.toString();
	}       

	/**
	* ���ڸ� ���ڷ� �ٲٴ� �Լ�
	* @param String to Set
	* @return a int
	*/
	public static int cInt(String val) {
		Integer VAL = new Integer(val);
		return VAL.intValue();
	}
	
	/**
	* ���ڸ� �޾ƿͼ� ���ϴ� ������ String   ���� ��ȯ�ϴ� �Լ� 
	* @param int to Set : ����
	* @param int to Set : ���ϴ� ����      
	*/
	public String addZero (int chkNumber, int chkLen){
		String temp = StringUtil.cStr(chkNumber);  
		int len = temp.length();
		
		if (len < chkLen){
			for(int i=1; i<=(chkLen-len); i++) {
				temp = "0" + temp;      
			}
		} 
		return temp; 
	}
	
	/**
	* �ֹι�ȣ�� ���ڷ� �޾� �ùٸ� �ֹι�ȣ���� üũ
	* @param String to Set : �ֹι�ȣ 13�ڸ�
	* @return boolean  : �ùٸ� �ֹι�ȣ���� üũ
	*/
	public static boolean isRegID(String val) {
		if(val == null || val.length() != 13)
			return false;
		char[] chRegs = val.toCharArray();
		if(chRegs[6] != '1' && chRegs[6] != '2' && chRegs[6] != '3' && chRegs[6] != '4' && chRegs[6] != '9' && chRegs[6] != '0'){
			return false;
		}
		int nTotInt = 0;
		int nMulti = 2;
		if(chRegs.length != 13)
			return false;
		for(int i = 0; i < chRegs.length - 1; i++){
			if(chRegs[i] < 48 || chRegs[i] > 57){
				return false;
			}
			if(i == 12)
				break;
			int curInt = chRegs[i] - 48;
			if(nMulti == 10)
				nMulti = 2;
			nTotInt += nMulti * curInt;
			nMulti++;
		}
		
		int nResult = (11 - (nTotInt % 11)) % 10;
		int nComp = chRegs[12] - 48;
		if(nResult == nComp)
			return true;
		return false;
	}
	
	/**
	 * Description:   ASCII�� EBCDIC���� ��ȯ �Լ�
	 * @param String the int to be translated
	 * @return String the translated int
	**/  
	public static String A2EB(String strInput){
		byte[] bIn = strInput.getBytes();
		byte[] bOut = new byte[bIn.length];
		for(int i=0; i < bIn.length; i++){
			bOut[i] = translateByte(bIn[i]);
		}
		String strOut = new String(bOut);
		return strOut;
	}
	
	/**
	 * Description:   ASCII�� EBCDIC���� ��ȯ �Լ�
	 * @param int the int to be translated
	 * @return int the translated int
	**/  
	public static byte translateByte(byte i) {
		for (int j = 0; j< ASCII.length; i++) {
			if (i == (byte)ASCII[j]) {
				return (byte)EBCDIC[j];
			}
		}
		return i;
	}
}

