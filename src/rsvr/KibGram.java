package rsvr;

public class KibGram implements IKibGramEnum {

	private static final int H�����ڵ�_���� = 12;
	private static final int H������������_���ڼ� = 4;
	private static final String H�����ڵ�_�������� = "000";
	
	private static final int[] headerPosition = {
		      // index kibnet����
		0     //     1  1
		, 4   //  1  2  2 (  4)��������
		, 8   //  2  3  3 (  4)CS��ȣ
		, 16  //  3  4  4 (  8)CS���� ����ڵ�
		, 17  //  4  5  5 (  1)REACTION CODE
		, 20  //  5  6  6 (  3)���Ӱŷ���ȣ(3)
		, 21  //  6  7  7 (  1)�ۼ���FLAG(1)
		, 29  //  7  8  8 (  8)��ޱ���ڵ�(8)
		, 34  //  8  8  8 (  5)��޿������ڵ�(4)
		, 38  //  9 10  9 (  4)��޴ܸ��ڵ�(5)
		, 39  // 10 11 10 (  1)��ü(�߻�)����(1)
		, 43  // 11 12 11 (  4)���������ڵ�(MSG TYPE)(4)
		, 47  // 12 13 12 (  4)�ŷ������ڵ�(4)
		, 50  // 13 14 13 (  3)�����ڵ�(3)
		, 58  // 14 15 14 (  8)�ŷ�����(8)
		, 64  // 15 16 15 (  6)�ŷ��ð�(6)
		, 71  // 16 17 16 (  7)�ŷ��Ϸù�ȣ(7)
		, 72  // 17 18 17 (  1)�ѱ��ڵ屸��(1)
		, 73  // 18 19 18 (  1)�����ı���(1)
		, 81  // 19 20 19 (  8)��������ڵ�(8)
		, 82  // 20 21 20 (  1)KIB_USE_1 M/S TRACK��ȣ(1)
		, 223 // 21 21 21 (141)KIB_USE_2 M/S TRACK DATA(141)
		, 224 // 22 21 22 (  1)KIB_USE_3 ī�屸��(1)
		, 225 // 23 21 23 (  1)KIB_USE_4 ����ȭ�� ��ȸ ��� ����(1)
		, 226 // 24 21 24 (  1)KIB_USE_5 ��ü�ŷ��� ����������(1)
		, 240 // 25 21 25 ( 14)KIB_USE_6 USER WORK AREA(14)
		, 280 // 26 22 26 ( 40)�� �� MESSAGE(40)
		, 286 // 27 29 27 (  6)���ŷ����(6)
		, 290 // 28 29 28 (  4)FILLER1(4) ���������ڵ�(4)
		, 300 // 29 30 29 ( 10)FILLER(10)
	};

	private static final int[] bodyPosition = {
		      // index kibnet����
		0     //     1  1 -----�ݾ�����
		, 13  //  1  2  2 ( 13)�ŷ��ݾ�
		, 14  //  2  3  3 (  1)��,�� ����ǥ��
		, 27  //  3  4  4 ( 13)�ŷ��� �����ܾ�
		, 40  //  4  5  5 ( 13)�̰��� Ÿ���� �ݾ�
		//------------ ---�Աݰ��º�
		, 48  //  5  6  6 (  8)���ޱ���ڵ�    ( 8)
		, 50  //  6  7  7 (  2)�Աݰ��±����ڵ�( 2) - '00' ����
		, 66  //  7  8  8 ( 16)���¹�ȣ(����)  (16)
		, 86  //  8  8  8 ( 20)�Աݰ��¼���    (20)
	    //----------------��ݰ��º�
		, 94  //  9 10  9 (  8)����(���ı���ڵ�)( 8) - '00000020' or '00000088'
		, 96  // 10 11 10 (  2)��ݰ��±����ڵ�  ( 2) - '00'
		, 112 // 11 12 11 ( 16)���¹�ȣ(����)    (16)
		, 120 // 12 13 12 (  8)��й�ȣ          ( 8)
		, 140 // 13 14 13 ( 20)��ݰ��¼���      (20)
	    //----------------������
		, 145 // 14 15 14 (  5)������ݾ�        ( 5) - '00000'
		, 148 // 15 16 15 (  3)���ݸż�          ( 3) - '000'
		, 150 // 16 17 16 (  2)��ǥ�ż�          ( 2) - '00'
	    //----------------������-��ǥ 1 / 5
		, 158 // 17 18 17 (  8)��ǥ��ȣ     (8)
		, 164 // 18 19 18 (  6)��ǥ�������� (6)
		, 166 // 19 20 19 (  2)��ǥ����     (2)
	    //----------------������-��ǥ 2 / 5
		, 174 // 17 18 17 (  8)��ǥ��ȣ    (8)
		, 180 // 18 19 18 (  6)��ǥ��������(6)
		, 182 // 19 20 19 (  2)��ǥ����    (2)
	    //----------------������-��ǥ 3 / 5
		, 190 // 17 18 17 (  8)��ǥ��ȣ    (8)
		, 196 // 18 19 18 (  6)��ǥ��������(6)
		, 198 // 19 20 19 (  2)��ǥ����    (2)
	    //----------------������-��ǥ 4 / 5
		, 206 // 17 18 17 (  8)��ǥ��ȣ    (8)
		, 212 // 18 19 18 (  6)��ǥ��������(6)
		, 214 // 19 20 19 (  2)��ǥ����    (2)
	    //----------------������-��ǥ 5 / 5
		, 222 // 17 18 17 (  8)��ǥ��ȣ    (8)
		, 228 // 18 19 18 (  6)��ǥ��������(6)
		, 230 // 19 20 19 (  2)��ǥ����    (2)
	    //----------------������-���
		, 233 // 26 22 26 (  3)��һ���      ( 3)
		, 245 // 27 29 27 ( 12)�ŷ�������ȣ  (12)
		, 250 // 28 29 28 (  5)��м�����ݾ�( 5)
		, 256 // 28 29 28 (  6)�����ȣ      ( 6)
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
		sToBytes = orgGram.substring(H������������_���ڼ�).getBytes();
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
		// kibHeader[0] = subString(0,4); // ��������(4)
		// kibHeader[1] = subString(4,8); // CS��ȣ(4)
		// kibHeader[2] = subString(8,16); // CS���� ����ڵ�(8)
		// kibHeader[3] = subString(16,17); // REACTION CODE(1)
		// kibHeader[4] = subString(17,20); // ���Ӱŷ���ȣ(3)
		// kibHeader[5] = subString(20,21); // �ۼ���FLAG(1)
		// kibHeader[6] = subString(21,29); // ��ޱ���ڵ�(8)
		// kibHeader[7] = subString(29,33); // ��޿������ڵ�(4)
		// kibHeader[8] = subString(33,38); // ��޴ܸ��ڵ�(5)
		// kibHeader[9] = subString(38,39); // ��ü(�߻�)����(1)
		// kibHeader[h���������ڵ�] = subString(39,43); // ���������ڵ�(MSG TYPE)(4)
		// kibHeader[h�ŷ������ڵ�] = subString(43,47); // �ŷ������ڵ�(4)
		// kibHeader[12] = subString(47,50); // �����ڵ�(3)
		// kibHeader[13] = subString(50,58); // �ŷ�����(8)
		// kibHeader[14] = subString(58,64); // �ŷ��ð�(6)
		// kibHeader[15] = subString(64,71); // �ŷ��Ϸù�ȣ(7)
		// kibHeader[16] = subString(71,72); // �ѱ��ڵ屸��(1)
		// kibHeader[17] = subString(72,73); // �����ı���(1)
		// kibHeader[18] = subString(73,81); // ��������ڵ�(8)
		// kibHeader[19] = subString(81,82); // M/S TRACK��ȣ(1)
		// kibHeader[20] = subString(82,223); // M/S TRACK DATA(141)
		// kibHeader[21] = subString(223,224); // ī�屸��(1)
		// kibHeader[22] = subString(224,225); // ����ȭ�� ��ȸ ��� ����(1)
		// kibHeader[23] = subString(225,226); // ��ü�ŷ��� ����������(1)
		// kibHeader[24] = subString(226,240); // USER WORK AREA(14)
		// kibHeader[25] = subString(240,280); // ���� MESSAGE(40)
		// kibHeader[26] = subString(280,286); // ���ŷ����(6)
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
	 * ���ڿ� ����Ʈ�� ��� - Exception ���� �߻�
	 * 
	 * @return a String - ��û�� ����Ʈ ������ ���ڿ�
	 * @param int to Set - ���� ����Ʈ
	 * @param int to Set - ������ ����Ʈ
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
	 * �Աݴ�����Ұŷ��ΰ�?
	 * 
	 * @return boolean - true(�׷���) / false(�ƴϴ�)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountAgentCancelTransaction() {
		return (header[h���������ڵ�].equals(�����ڵ�_�ŷ�����ڵ�)
				&& header[h�ŷ������ڵ�].equals(�ŷ�����_�Աݴ���ŷ�));
	}

	/**
	 * �Ա���Ұŷ��ΰ�?
	 * 
	 * @return boolean - true(�׷���) / false(�ƴϴ�)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountCancelTransaction() {
		return (header[h���������ڵ�].equals(�����ڵ�_�ŷ�����ڵ�)
				&& header[h�ŷ������ڵ�].equals(�ŷ�����_�Աݰŷ�));
	}

	/**
	 * ������ȸ�ŷ��ΰ�?
	 * 
	 * @return boolean - true(�׷���) / false(�ƴϴ�)
	 */
	@SuppressWarnings("unused")
	private boolean isReceiveReadTransaction() {
		return (header[h���������ڵ�].equals(�����ڵ�_�ŷ������ڵ�)
				&& header[h�ŷ������ڵ�].equals("4100"));
	}

	/**
	 * �Աݴ���ŷ��ΰ�?
	 * 
	 * @return boolean - true(�׷���) / false(�ƴϴ�)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountAgentTransaction() {
		return (header[h���������ڵ�].equals(�����ڵ�_�ŷ������ڵ�)
				&& header[h�ŷ������ڵ�].equals("1300"));
	}

	/**
	 * �Աݰŷ��ΰ�?
	 * 
	 * @return boolean - true(�׷���) / false(�ƴϴ�)
	 */
	@SuppressWarnings("unused")
	private boolean isInamountTransaction() {
		return (header[h���������ڵ�].equals(�����ڵ�_�ŷ������ڵ�)
				&& header[h�ŷ������ڵ�].equals(�ŷ�����_�Աݰŷ�));
	}

	/**
	 * ���ðŷ��ΰ�?
	 * 
	 * @return boolean - true(�׷���) / false(�ƴϴ�)
	 */
	@SuppressWarnings("unused")
	private boolean isStartGram() {
		return (header[h���������ڵ�].equals(�����ڵ�_�����������ڵ�)
				&& header[h�ŷ������ڵ�].equals(�ŷ�����_���ðŷ�));
	}

	/**
	 * ����ŷ��ΰ�?
	 * 
	 * @return boolean - true(�׷���) / false(�ƴϴ�)
	 */
	@SuppressWarnings("unused")
	private boolean isEndGram() {
		return (header[h���������ڵ�].equals(�����ڵ�_�����������ڵ�)
				&& header[h�ŷ������ڵ�].equals(�ŷ�����_����ŷ�));
	}

	/**
	 * ���� ���� �޼ҵ�
	 * @param msg - ����
	 * @return ����Ŭ����
	 * @throws Exception
	 */
	public static KibGram create(String msg) {
		return new KibGram(msg);
	}

	/**
	 * ���� ���� ���� �޼ҵ�
	 * @param msg - ����
	 * @return ����Ŭ����
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
			if (H�����ڵ�_���� == i)
				result += H�����ڵ�_��������;
			else
				result += header[i];
		}
		for(int i = 0; i < body.length; i++) {
			result += body[i];
		}
		
		result = "0"+(result.getBytes().length+ H������������_���ڼ�)+result;
		
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
