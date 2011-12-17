package rsvr;

public interface IKibGramEnum {
	public static final String 전문코드_거래실행코드 = "0200";
	public static final String 전문코드_거래취소코드 = "0400";

	public static final String 전문코드_은행기관관리코드 = "0800";
	
	public static final String 거래구분_입금거래 = "1100";
	public static final String 거래구분_입금대행거래 = "1300";

	public static final String 거래구분_개시거래 = "2100";
	public static final String 거래구분_종료거래 = "2400";
	
	public static final int h전문길이               = 0;
	public static final int hCS번호                 = 1;
	public static final int hCS관리기관코드         = 2;
	public static final int hREACTION_CODE          = 3;
	public static final int h연속거래번호           = 4;
	public static final int h송수신FLAG             = 5;
	public static final int h취급기관코드           = 6;
	public static final int h취급영업점코드         = 7;
	public static final int h취급단말코드           = 8;
	public static final int h매체_발생_구분         = 9;
	public static final int h전문구분코드           = 10;
	public static final int h거래구분코드           = 11;
	public static final int h응답코드               = 12;
	public static final int h거래일자               = 13;
	public static final int h거래시간               = 14;
	public static final int h거래일련번호           = 15;
	public static final int h한글코드구분           = 16;
	public static final int h마감후구분             = 17;
	public static final int h개설기관코드           = 18;
	public static final int hMS_TRACK번호           = 19;
	public static final int hMS_TRACK_DATA          = 20;
	public static final int h카드구분               = 21;
	public static final int h정형화된조회출력구분   = 22;
	public static final int h이체거래시입출기관구분 = 23;
	public static final int hUSER_WORK_AREA         = 24;
	public static final int h응답MESSAGE            = 25;
	public static final int h원거래요소             = 26;
	public static final int h은행응답코드           = 27;
	public static final int hFILLER                 = 28;
	public static final int h해더개수               = 29;
}