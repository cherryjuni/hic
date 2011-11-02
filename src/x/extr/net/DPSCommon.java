/** Main system          : x.extr
  * Sub system           : net
  * Classname            : DPSCommon.java
  * Initial date         : 2005.11.15
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DPS시스템의 공통헤더 처리용 Class
  * Version information  : v 1.0
  *
  */

package x.extr.net;

/** DPS시스템의 공통헤더 처리용 Class
  * @author 황재천
*/
public class DPSCommon  {
	/** 외부기관 - 금융결제원 */
	public static String EX01 = "KFTCONL  ";		//금융결제원
	/** 외부기관 - 은행연합회 */
	public static String EX02 = "KFBCIF   ";		//은행연합회
	/** 외부기관 - 한신정 */
	public static String EX03 = "NICECBONL";		//한신정
	/** 외부기관 - 한신평:단기연체정보 */
	public static String EX04 = "KISSDONL ";		//한신평-단기연체정보
	/** 외부기관 - 한신평:소비자신용정보 */
	public static String EX05 = "KISSDONLO";		//한신평-소비자신용정보
	/** 외부기관 - 한네트 */
	public static String EX06 = "HANNETONL";		//한네트
	/** 외부기관 - 농협 */
	public static String EX07 = "NONGONL  ";		//농협
	/** 외부기관 - 우리은행 */
	public static String EX08 = "WOORIONL ";		//우리은행
	/** 외부기관 - SMS */
	public static String EX09 = "SMSONL   ";		//SMS
	/** 외부기관 - FAX */
	public static String EX10 = "FAXONL   ";		//FAX
	/** 외부기관 - E-Mail */
	public static String EX11 = "EMAILONL ";		//E-Mail
	/** 외부기관 - 한신평(FTP) */
	public static String EX12 = "KISSDFTP ";		//한신평(FTP)
	/** 외부기관 - 금융결제원(FTP) */
	public static String EX13 = "KFTCFTP  ";		//금융결제원(FTP)
	/** 외부기관 - 은행연합회(FTP) */
	public static String EX14 = "KFBFTP   ";		//은행연합회(FTP)
	/** 외부기관 - 한신평:대금업정보 */
	public static String EX15 = "KISSDONLD";		//한신평:대금업정보

	/** 외부기관에서 요청시 - 한네트 */
	public static String EX51 = "HANNETON2";		//한네트
	/** 외부기관에서 요청시 - 농협 */
	public static String EX52 = "NONGONL2 ";		//농협
	/** 외부기관에서 요청시 - ARS */
	public static String EX53 = "ARSONL2  ";		//은행연합회(FTP)
	/** 외부기관에서 요청시 - KS-NET */
	public static String EX54 = "KSNETONL2";		//KS-NET

	public String exCode   = "__";					// 2
	public String srFlag   = "_";					// 1	
	public String trCode   = "________";			// 8
	public String mngNum   = "____________________";// 20
	public String trSnum   = "__________"; 			// 10
	public String rspnCd   = "____"; 				// 4
	public String trDate   = "________"; 			// 8
	public String trTime   = "______"; 				// 6
	public String dataLen  = "____"; 				// 4
	public String termNo   = "________"; 			// 8
	public String empNo    = "________"; 			// 8
	public String statCd   = "_"; 					// 1
	public String reserved = "____________________";// 20
	public String data     = "";

	/** 생성자
     * @param sCommonHeader    로컬공통헤더
	 * @param data             데이터필드
     */
	public DPSCommon(String sCommonHeader, String sRequestData) {
		try{
			this.exCode   = sCommonHeader.substring(0,2);	// 대외기관코드(2)
			this.srFlag   = sCommonHeader.substring(2,3);	// 송수신여부(1)
			this.trCode   = sCommonHeader.substring(3,11);	// 거래구분코드(8)
			this.mngNum   = sCommonHeader.substring(11,31);	// 관리번호(20)
			this.trSnum   = sCommonHeader.substring(31,41);	// 거래일련번호(10)
			this.rspnCd   = sCommonHeader.substring(41,45);	// 응답코드(4)
			this.trDate   = sCommonHeader.substring(45,53); // 거래일자(8)
			this.trTime   = sCommonHeader.substring(53,59); // 거래시간(6)
			this.dataLen  = sCommonHeader.substring(59,63); // 데이터길이(4)
			this.termNo   = sCommonHeader.substring(63,71); // 처리단말번호(8)
			this.empNo    = sCommonHeader.substring(71,79); // 단말등록 사원번호(8)
			this.statCd   = sCommonHeader.substring(79,80); // 상태코드(1)
			this.reserved = sCommonHeader.substring(80,100);// 예약필드(20)
			this.data     = sRequestData;
		}catch(Exception e){
			System.out.println("공통헤더 구성이 잘못되었습니다");
		}finally{
			System.out.println("Reserved=[" + reserved + "]");
		}
	} 
	
	/** 생성자
      * @param exCode           외부기관코드
	  * @param trCode           TR-Code
	  * @param bizCode          업무코드
	  * @param userId           사용자ID
	  * @param termNo           단말번호
	  * @param srFlag           송수신구분
	  * @param returnCode       응답코드
	  * @param data             데이터필드
      */
	public DPSCommon(String exCode,String srFlag,String trCode,String mngNum,String trSnum,String rspnCd,String trDate,String trTime,String dataLen,String termNo,String empNo, String statCd, String reserved, String data) {
		this.exCode = exCode;
		this.srFlag = srFlag;
		this.trCode = trCode;
		this.mngNum = mngNum;
		this.trSnum = trSnum;
		this.rspnCd = rspnCd;
		this.trDate = trDate;
		this.trTime = trTime;
		this.dataLen= dataLen;
		this.termNo = termNo;
		this.empNo  = empNo;
		this.statCd = statCd;
		this.reserved = reserved;
		this.data = data;

		/*
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		StringBuffer cTime = new StringBuffer(FormatData.numToStr(c.get(Calendar.YEAR),4,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.MONTH) + 1,2,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.DATE),2,"0"));
		
		//오후인 경우 24시간표기법으로 표현하기 위함.
		if (c.get(Calendar.AM_PM) == 1){
			cTime.append(FormatData.numToStr(c.get(Calendar.HOUR) + 12,2,"0"));
		}else{
			cTime.append(FormatData.numToStr(c.get(Calendar.HOUR),2,"0"));
		}
	  //cTime.append(FormatData.numToStr(c.get(Calendar.HOUR)  ,2,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.MINUTE),2,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.SECOND),2,"0"));
		this.sysDate = cTime.toString();
		*/
	}
	/** 생성자
      */
	public DPSCommon() {};

	/** 설정정보를 문자열로 반환
	  * @return String
      */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(exCode);
		buf.append(srFlag);
		buf.append(trCode);
		buf.append(mngNum);
		buf.append(trSnum);
		buf.append(rspnCd);
		buf.append(trDate);
		buf.append(trTime);
		buf.append(dataLen);
		buf.append(termNo);
		buf.append(empNo);
		buf.append(statCd);
		buf.append(reserved);
		buf.append(data);

		return buf.toString();
	}

	/** 문자열타입의 프레임을 헤더와 데이터부로 분할<p>
	  * String[0] : 헤더<p>
	  * String[1] : 데이터부
      * @param data             문자열 프레임
	  * @return String[]
      */
	public static String[] divideHeader(String data) {
		String[] sData = new String[3];
		
		try{
			byte[] temp = data.getBytes();
			sData[0] = new String(temp,0,100);		// 로컬공통헤더(무조건 100 바이트)
			if(data.substring(0,2).equals("11")){
				// 은행연합회
				sData[1] = new String(temp,100,90);
				sData[2] = new String(temp,190,temp.length-190);			
			}else if(data.substring(0,2).equals("12")) {
				// 한신정
				sData[1] = new String(temp,100,110);	// 한신정 공통헤더(전문외 항목인 길이 10바이트 포함+공통헤더100 바이트)
				sData[2] = new String(temp,210,temp.length-210);		// 개별요청부 및 데이타부			
			}else if(data.substring(0,2).equals("13")) {
				// 한신평
				if( data.substring(7,11).startsWith("SA")){
					sData[1] = new String(temp,100,127);	// 한신평 공통헤더(127 바이트)
					sData[2] = new String(temp,227,temp.length-227);		// 내용블럭				
				}else if( data.substring(7,11).equals("TR23")){
					sData[1] = new String(temp,100,87);	// 한신평 공통헤더(87 바이트)
					sData[2] = new String(temp,187,temp.length-187);		// 내용블럭				
				}
			}else if(data.substring(0,2).equals("21")) {
				// KIB
				sData[1] = new String(temp,100,300);	// KIB AP COMMON 공통헤더(300 바이트)
				sData[2] = new String(temp,400,temp.length-400);		// 업무전문			
			}else if(data.substring(0,2).equals("22")) {
				// KIB
				sData[1] = "No exists gram common division";
				sData[2] = new String(temp,100,110);	// 대우자판 업무전문 			
			}
		}catch(Exception e){
			System.out.println("Errors in divideHeader : 프레임을 헤더와 데이터부로 분할중 에러발생");
			System.out.println(data);
		}
		return sData;
	}

	/** 문자열타입의 프레임을 상세헤더와 데이터부로 분할<p>
	  * String[0] : 외부기관코드<p>
	  * String[1] : TR-Code<p>
	  * String[2] : 업무코드<p>
	  * String[3] : 유저ID<p>
	  * String[4] : 단말번호<p>
	  * String[5] : 처리시간<p>
	  * String[6] : 송수신구분<p>
	  * String[7] : 데이터부의 길이<p>
	  * String[8] : 리턴코드<p>
	  * String[9] : Reserved<p>
	  * String[10] : 데이터부
      * @param data             문자열 프레임
	  * @return String[]
      */
	public static String[] divideHeaderDetail(String data) {
		String[] sData = new String[14];

		sData[0] = data.substring(0,2); //2
		sData[1] = data.substring(2,3); //1
		sData[2] = data.substring(3,11);//8
		sData[3] = data.substring(11,31);//20
		sData[4] = data.substring(31,41);//10
		sData[5] = data.substring(41,45);//4
		sData[6] = data.substring(45,53);//8
		sData[7] = data.substring(53,59);//6
		sData[8] = data.substring(59,63);//4
		sData[9] = data.substring(63,71);//8
		sData[10] = data.substring(71,79);//8
		sData[11] = data.substring(79,80);//1
		sData[12] = data.substring(80,100);//20
		sData[13] = data.substring(100);

		return sData;
	}
	
	/** 
	 설정정보를 문자열로 반환
	 @return String
	 */
	public String toString(String enc) {
		StringBuffer buf = new StringBuffer();
		buf.append(exCode);
		buf.append(srFlag);
		buf.append(trCode);
		buf.append(mngNum);
		buf.append(trSnum);
		buf.append(rspnCd);
		buf.append(trDate);
		buf.append(trTime);
		buf.append(dataLen);
		buf.append(termNo);
		buf.append(empNo);
		buf.append(statCd);
		buf.append(reserved);
		buf.append(data);

		return buf.toString();
	}

}