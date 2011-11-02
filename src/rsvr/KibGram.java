package rsvr;

public class KibGram implements IKibGramEnum {
	
	private static final int[] headerPositon = {
		      // index kibnet전문
		0     //     1  1
		, 4   //  1  2  2 (  4)전문길이
		, 8   //  2  3  3 (  4)CS번호
		, 16  //  3  4  4 (  8)CS관리 기관코드
		, 17  //  4  5  5 (  1)REACTION CODE
		, 20  //  5  6  6 (  3)연속거래번호(3)
		, 21  //  6  7  7 (  1)송수신FLAG(1)
		, 29  //  7  8  8 (  8)취급기관코드(8)
		, 33  //  8  8  8 (  5)취급영업점코드(4)
		, 38  //  9 10  9 (  5)취급단말코드(5)
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

	private byte[] sToBytes;
	private String[] kibHeader;
	private String orgGram;

	private KibGram(String msg) throws Exception {
		orgGram = msg;
		parserKibHeader();
	}

	public void parserKibHeader() throws Exception {

		if (!isValidation(orgGram))
			throw new Exception("Validation Exception...");

		kibHeader = new String[headerPositon.length - 1];
		sToBytes = orgGram.getBytes();

		for (int i = 0; i < headerPositon.length - 1; i++) {
			kibHeader[i] = subString(headerPositon[i], headerPositon[i + 1]);
		}
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
	}

	private boolean isValidation(String msg) {
		if (!isCheckLength(msg))
			return false;
		return true;
	}

	private boolean isCheckLength(String msg) {

		return (msg.length() == Integer.parseInt(msg.substring(0, 4)));

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

	public String[] getKibHeader() {
		return kibHeader;
	}

	// public static int[] getHeaderPostion() {
	// return headerPositon;
	// }

	/**
	 * 입금대행취소거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	private boolean isInamountAgentCancelTransaction() {
		return (kibHeader[h전문구분코드].equals(전문코드_거래취소코드)
				&& kibHeader[h거래구분코드].equals(거래구분_입금대행거래));
	}

	/**
	 * 입금취소거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	private boolean isInamountCancelTransaction() {
		return (kibHeader[h전문구분코드].equals(전문코드_거래취소코드)
				&& kibHeader[h거래구분코드].equals(거래구분_입금거래));
	}

	/**
	 * 수취조회거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	private boolean isReceiveReadTransaction() {
		return (kibHeader[h전문구분코드].equals(전문코드_거래실행코드)
				&& kibHeader[h거래구분코드].equals("4100"));
	}

	/**
	 * 입금대행거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	private boolean isInamountAgentTransaction() {
		return (kibHeader[h전문구분코드].equals(전문코드_거래실행코드)
				&& kibHeader[h거래구분코드].equals("1300"));
	}

	/**
	 * 입금거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	private boolean isInamountTransaction() {
		return (kibHeader[h전문구분코드].equals(전문코드_거래실행코드)
				&& kibHeader[h거래구분코드].equals(거래구분_입금거래));
	}

	/**
	 * 개시거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	private boolean isStartGram() {
		return (kibHeader[h전문구분코드].equals(전문코드_은행기관관리코드)
				&& kibHeader[h거래구분코드].equals(거래구분_개시거래));
	}

	/**
	 * 종료거래인가?
	 * 
	 * @return boolean - true(그렇다) / false(아니다)
	 */
	private boolean isEndGram() {
		return (kibHeader[h전문구분코드].equals(전문코드_은행기관관리코드)
				&& kibHeader[h거래구분코드].equals(거래구분_종료거래));
	}

	/**
	 * 전문 생성 메소드
	 * @param msg - 전문
	 * @return 전문클래스
	 * @throws Exception
	 */
	public static KibGram create(String msg) throws Exception {
		return new KibGram(msg);
	}

}
