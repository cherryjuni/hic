package rsvr;

public class KibGram implements IKibGramEnum {

	private static final int H응답코드_순번 = 12;
	private static final int H소켓전문길이_글자수 = 4;
	private static final String H응답코드_오류없음 = "000";
	
	private static final int[] headerPosition = {
		      // index kibnet전문
		0     //     1  1
		, 4   //  1  2  2 (  4)전문길이
		, 8   //  2  3  3 (  4)CS번호
		, 16  //  3  4  4 (  8)CS관리 기관코드
		, 17  //  4  5  5 (  1)REACTION CODE
		, 20  //  5  6  6 (  3)연속거래번호(3)
		, 21  //  6  7  7 (  1)송수신FLAG(1)
		, 29  //  7  8  8 (  8)취급기관코드(8)
		, 34  //  8  8  8 (  5)취급영업점코드(4)
		, 38  //  9 10  9 (  4)취급단말코드(5)
		, 39  // 10 11 10 (  1)매체(발생)구분(1)
		, 43  // 11 12 11 (  4)전문구분코드(MSG TYPE)(4)
		, 47  // 12 13 12 (  4)거래구분코드(4)
		, 50  // 13 14 13 (  3)응답코드(3)
		, 58  // 14 15 14 (  8)거래일자(8)
		, 64  // 15 16 15 (  6)거래시간(6)
		, 71  // 16 17 16 (  7)거래일련번호(7)
		, 72  // 17 18 17 (  1)한글코드구분(1)
		, 73  // 18 19 18 (  1)마감후구분(1)
		, 81  // 19 20 19 (  8)개설기관코드(8)
		, 82  // 20 21 20 (  1)KIB_USE_1 M/S TRACK번호(1)
		, 223 // 21 21 21 (141)KIB_USE_2 M/S TRACK DATA(141)
		, 224 // 22 21 22 (  1)KIB_USE_3 카드구분(1)
		, 225 // 23 21 23 (  1)KIB_USE_4 정형화된 조회 출력 구분(1)
		, 226 // 24 21 24 (  1)KIB_USE_5 이체거래시 입출기관구분(1)
		, 240 // 25 21 25 ( 14)KIB_USE_6 USER WORK AREA(14)
		, 280 // 26 22 26 ( 40)응 답 MESSAGE(40)
		, 286 // 27 29 27 (  6)원거래요소(6)
		, 290 // 28 29 28 (  4)FILLER1(4) 은행응답코드(4)
		, 300 // 29 30 29 ( 10)FILLER(10)
	};

	private static final int[] bodyPosition = {
		      // index kibnet전문
		0     //     1  1 -----금액정보
		, 13  //  1  2  2 ( 13)거래금액
		, 14  //  2  3  3 (  1)양,음 구분표시
		, 27  //  3  4  4 ( 13)거래후 계좌잔액
		, 40  //  4  5  5 ( 13)미결제 타점권 금액
		//------------ ---입금계좌부
		, 48  //  5  6  6 (  8)제휴기관코드    ( 8)
		, 50  //  6  7  7 (  2)입금계좌구분코드( 2) - '00' 세팅
		, 66  //  7  8  8 ( 16)계좌번호(가상)  (16)
		, 86  //  8  8  8 ( 20)입금계좌성명    (20)
	    //----------------출금계좌부
		, 94  //  9 10  9 (  8)은행(제후기관코드)( 8) - '00000020' or '00000088'
		, 96  // 10 11 10 (  2)출금계좌구분코드  ( 2) - '00'
		, 112 // 11 12 11 ( 16)계좌번호(가상)    (16)
		, 120 // 12 13 12 (  8)비밀번호          ( 8)
		, 140 // 13 14 13 ( 20)출금계좌성명      (20)
	    //----------------개별부
		, 145 // 14 15 14 (  5)수수료금액        ( 5) - '00000'
		, 148 // 15 16 15 (  3)현금매수          ( 3) - '000'
		, 150 // 16 17 16 (  2)수표매수          ( 2) - '00'
	    //----------------개별부-수표 1 / 5
		, 158 // 17 18 17 (  8)수표번호     (8)
		, 164 // 18 19 18 (  6)수표발행정보 (6)
		, 166 // 19 20 19 (  2)수표권종     (2)
	    //----------------개별부-수표 2 / 5
		, 174 // 17 18 17 (  8)수표번호    (8)
		, 180 // 18 19 18 (  6)수표발행정보(6)
		, 182 // 19 20 19 (  2)수표권종    (2)
	    //----------------개별부-수표 3 / 5
		, 190 // 17 18 17 (  8)수표번호    (8)
		, 196 // 18 19 18 (  6)수표발행정보(6)
		, 198 // 19 20 19 (  2)수표권종    (2)
	    //----------------개별부-수표 4 / 5
		, 206 // 17 18 17 (  8)수표번호    (8)
		, 212 // 18 19 18 (  6)수표발행정보(6)
		, 214 // 19 20 19 (  2)수표권종    (2)
	    //----------------개별부-수표 5 / 5
		, 222 // 17 18 17 (  8)수표번호    (8)
		, 228 // 18 19 18 (  6)수표발행정보(6)
		, 230 // 19 20 19 (  2)수표권종    (2)
	    //----------------개별부-계속
		, 233 // 26 22 26 (  3)취소사유      ( 3)
		, 245 // 27 29 27 ( 12)거래관리번호  (12)
		, 250 // 28 29 28 (  5)배분수수료금액( 5)
		, 256 // 28 29 28 (  6)복기부호      ( 6)
		, 300 // 29 30 29 ( 44)FILLER        (44)
	};

	private byte[] sToBytes;
	private String[] header;
	private String[] body;
	private String orgGram;
	private boolean alreadyProcessHeader = false;
	private boolean alreadyProcessBody = false;

	private KibGram(String msg) {
		orgGram = msg;
		sToBytes = orgGram.substring(H소켓전문길이_글자수).getBytes();
	}

	public String[] parserKibHeader() throws Exception {

		if (!isValidation(orgGram))
			throw new Exception("Validation Exception...");

		if (isAreadyProcessHeader()) return header;
		
		header = new String[headerPosition.length - 1];

		for (int i = 0; i < headerPosition.length - 1; i++) {
			header[i] = subString(headerPosition[i], headerPosition[i + 1]);
		}
		processHeader();
		// kibHeader[0] = subString(0,4); // 전문길이(4)
		// kibHeader[1] = subString(4,8); // CS번호(4)
		// kibHeader[2] = subString(8,16); // CS관리 기관코드(8)
		// kibHeader[3] = subString(16,17); // REACTION CODE(1)
		// kibHeader[4] = subString(17,20); // 연속거래번호(3)
		// kibHeader[5] = subString(20,21); // 송수신FLAG(1)
		// kibHeader[6] = subString(21,29); // 취급기관코드(8)
		// kibHeader[7] = subString(29,33); // 취급영업점코드(4)
		// kibHeader[8] = subString(33,38); // 취급단말코드(5)
		// kibHeader[9] = subString(38,39); // 매체(발생)구분(1)
		// kibHeader[h전문구분코드] = subString(39,43); // 전문구분코드(MSG TYPE)(4)
		// kibHeader[h거래구분코드] = subString(43,47); // 거래구분코드(4)
		// kibHeader[12] = subString(47,50); // 응답코드(3)
		// kibHeader[13] = subString(50,58); // 거래일자(8)
		// kibHeader[14] = subString(58,64); // 거래시간(6)
		// kibHeader[15] = subString(64,71); // 거래일련번호(7)
		// kibHeader[16] = subString(71,72); // 한글코드구분(1)
		// kibHeader[17] = subString(72,73); // 마감후구분(1)
		// kibHeader[18] = subString(73,81); // 개설기관코드(8)
		// kibHeader[19] = subString(81,82); // M/S TRACK번호(1)
		// kibHeader[20] = subString(82,223); // M/S TRACK DATA(141)
		// kibHeader[21] = subString(223,224); // 카드구분(1)
		// kibHeader[22] = subString(224,225); // 정형화된 조회 출력 구분(1)
		// kibHeader[23] = subString(225,226); // 이체거래시 입출기관구분(1)
		// kibHeader[24] = subString(226,240); // USER WORK AREA(14)
		// kibHeader[25] = subString(240,280); // 응답 MESSAGE(40)
		// kibHeader[26] = subString(280,286); // 원거래요소(6)
		// kibHeader[27] = subString(286,300); // FILLER(14)
		return header;
	}

	private void processBody() {
		alreadyProcessBody = true;
	}

	private boolean isAreadyProcessBody() {
		
		return alreadyProcessBody;
	}

	private void processHeader() {
		alreadyProcessHeader = true;
	}

	private boolean isAreadyProcessHeader() {
		
		return alreadyProcessHeader;
	}

	public String[] parserKibBody() throws Exception {

		if (!isValidation(orgGram))
			throw new Exception("Validation Exception...");

		if (isAreadyProcessBody()) return body;
		
		body = new String[bodyPosition.length - 1];
		int kibHeaderSize = headerPosition[headerPosition.length-1]; 

		for (int i = 0; i < bodyPosition.length - 1; i++) {
			body[i] = subString(kibHeaderSize+bodyPosition[i], kibHeaderSize+bodyPosition[i + 1]);
		}
		processBody();
		
		return body;
	}

	private boolean isValidation(String msg) {
		if (!isCheckLength(msg))
			return false;
		return true;
	}

	private boolean isCheckLength(String msg) {

		return (msg.getBytes().length == Integer.parseInt(msg.substring(0, 4)));

	}

	/**
	 * 문자열 바이트로 계산 - Exception 에러 발생
	 * 
	 * @return a String - 요청한 바이트 사이의 문자열
	 * @param int to Set - 시작 바이트
	 * @param int to Set - 마지막 바이트
	 */
	private String subString(int start, int end) throws Exception {
		String strRtn = "";
		byte[] bOut = new byte[end - start];

		try {
			for (int i = start; i < end; i++) {
				bOut[i - start] = sToBytes[i];
			}

			strRtn = new String(bOut);

			return strRtn;
		} catch (NullPointerException ne) {
			throw new Exception("subString() NullPointerException : "
					+ ne.getMessage());
		} catch (Exception e) {
			throw new Exception("subString() Exception : " + e.getMessage());
		}
	}

	public String[] getHeader() throws Exception {
		parserKibHeader();
		return header;
	}

	public String[] getBody() throws Exception {
		parserKibBody();
		return header;
	}

	// public static int[] getHeaderPostion() {
	// return headerPositon;
	// }

	/**
	 * 입금대행취소거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountAgentCancelTransaction() {
		return (header[h전문구분코드].equals(전문코드_거래취소코드)
				&& header[h거래구분코드].equals(거래구분_입금대행거래));
	}

	/**
	 * 입금취소거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountCancelTransaction() {
		return (header[h전문구분코드].equals(전문코드_거래취소코드)
				&& header[h거래구분코드].equals(거래구분_입금거래));
	}

	/**
	 * 수취조회거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	@SuppressWarnings("unused")
	private boolean isReceiveReadTransaction() {
		return (header[h전문구분코드].equals(전문코드_거래실행코드)
				&& header[h거래구분코드].equals("4100"));
	}

	/**
	 * 입금대행거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountAgentTransaction() {
		return (header[h전문구분코드].equals(전문코드_거래실행코드)
				&& header[h거래구분코드].equals("1300"));
	}

	/**
	 * 입금거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountTransaction() {
		return (header[h전문구분코드].equals(전문코드_거래실행코드)
				&& header[h거래구분코드].equals(거래구분_입금거래));
	}

	/**
	 * 개시거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	@SuppressWarnings("unused")
	private boolean isStartGram() {
		return (header[h전문구분코드].equals(전문코드_은행기관관리코드)
				&& header[h거래구분코드].equals(거래구분_개시거래));
	}

	/**
	 * 종료거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	@SuppressWarnings("unused")
	private boolean isEndGram() {
		return (header[h전문구분코드].equals(전문코드_은행기관관리코드)
				&& header[h거래구분코드].equals(거래구분_종료거래));
	}

	/**
	 * 전문 생성 메소드
	 * @param msg - 전문
	 * @return 전문클래스
	 * @throws Exception
	 */
	public static KibGram create(String msg) {
		return new KibGram(msg);
	}

	/**
	 * 리턴 전문 생성 메소드
	 * @param msg - 전문
	 * @return 전문클래스
	 * @throws Exception
	 */
	public static String createReturnMsg(String msg) throws Exception {
		KibGram kibGram = new KibGram(msg);
		kibGram.parserKibHeader();
		kibGram.parserKibBody();
		return kibGram.makeReturnMsg();
	}

	private String makeReturnMsg() {
		String result = "";
		
		for(int i = 0; i < header.length; i++) {
			if (H응답코드_순번 == i)
				result += H응답코드_오류없음;
			else
				result += header[i];
		}
		for(int i = 0; i < body.length; i++) {
			result += body[i];
		}
		
		result = "0"+(result.getBytes().length+ H소켓전문길이_글자수)+result;
		
		return result;
	}

//	public static int[] getHeaderPositon() {
//		return headerPosition;
//	}
//
//	public static int[] getBodyPositon() {
//		return headerPosition;
//	}

}
