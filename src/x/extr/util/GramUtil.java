package x.extr.util;

import java.math.BigInteger;


/**
	 * String 데이터 차리를 위한 클래스
	 * <p>
	 * 널데이터나 널종료 문자열에 대한 처리를 해준다.
*/
public class GramUtil{
	
	//왼쪽정렬, 모자라는 길이만큼 스페이스를 채워넣는다.
    public static String fillSpace(String s, int leng) throws Exception
    {
    	if(s==null || s.equals("")){
    		s = " ";
    	}
    	return fill(s, leng, " ");
    }
    
    
    //왼쪽정렬, 모자라는 길이만큼 fillStr을 채워넣는다.
    public static String fill(String inStr, int size, String fillStr) throws Exception
    {
//        StringBuffer st = new StringBuffer();
//        st.append(s);
//
//        byte[] b = s.getBytes();   //한글 2byte로 인식해서 계산
//        for (int i=b.length ; i<leng ; i++ ){
//            st.append(c) ;
//        }
//
//        return st.toString();
    	String tmp = (inStr == null) ? "" : inStr.trim();
        byte[] inByte = tmp.getBytes();
        int inStrLen = inByte.length;

        if (inStrLen > size) {
        	return subString(inByte, 0, size );
        }
        StringBuffer buff = new StringBuffer(size);
        buff.append(tmp);
        for( int i= inStrLen; i < size; i++) {
        	buff.append(fillStr);
        }
        return buff.toString();
    }

    //오른쪽 정렬, 모자라는 길이만큼 "0"을 채워넣는다.
    public static String fixEndZero(String s, int leng) throws Exception
    {
        return fillEnd(s, leng, "0");
    }
    
    //오른쪽 정렬, 모자라는 길이만큼  fillStr을 채워넣는다.
    //이때, 소수점이하자리 절삭.
    public static String fillEnd(String inStr, int size, String fillStr) throws Exception
    {
//        StringBuffer st = new StringBuffer();
//        for (int i=s.length() ; i<leng ; i++ ) {
//        	st.append(c) ;
//        }
//        st.append(s);
//        return st.toString();
    	String tmp = (inStr == null) ? "" : inStr.trim();
        byte[] inByte = tmp.getBytes();
        int inStrLen = inByte.length;

        if (inStrLen > size) {
        	return subString(inByte, 0, size );
        }
        StringBuffer buff = new StringBuffer(size);
        for( int i= inStrLen; i < size; i++) {
        	buff.append(fillStr);
        }
        buff.append(tmp);
        return buff.toString();
    }

    public static String cnvNum(String s) throws Exception
    {
        BigInteger bi = new BigInteger("0");
        try{
            if(s==null || "".equals(s.trim())){
               s = "0";
            }
            bi = new BigInteger(s);
        }catch(Exception e){
            throw new Exception( "숫자포맷 에러 : " + s );
        }

    	return bi.toString();
    }

    public static String subString(byte[] cToBytes,
    		                        int start, int end) throws Exception {
        String strRtn = "";
        byte[]   bOut = new byte[end - start];

        try {
            for (int i = start; i < end; i++) {
                bOut[i - start] = cToBytes[i];
            }

            strRtn = new String(bOut);

            return strRtn;
        } catch (NullPointerException ne) {
            throw new Exception("subString() NullPointerException : " + ne.getMessage());
        } catch (Exception e) {
            throw new Exception("subString() Exception : " + e.getMessage());
        }
    }

	public static String[] parseString(byte[] byteArr, int[] len) throws Exception{

		int itmp = 0;
		int[] lenStart = new int[len.length];
		int[] lenEnd   = new int[len.length];
		for(int i=0;i<len.length;i++){
			if(i==0){
				lenStart[i] = 0;
			}else{
				lenStart[i] = lenStart[i-1] + len[i-1];
			}
			lenEnd[i] = lenStart[i] + len[i];
		}

		String[] strArr = new String[len.length];
		for(int i=0;i<len.length;i++){
                    strArr[i] = GramUtil.subString(byteArr, lenStart[i], lenEnd[i]);
		}

		return strArr;
	}

}
