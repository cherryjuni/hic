package rsvr;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.DateTimeUtil;
import util.StringUtil;

// ReceiveDaemon 을 호출해서 테스트를 진행하면
// KibReceiver를 호출하므로
// KibReceiver는 BlaclBox 테스트가 진행 된다.
// KibReceiver 테스트는 따로 작성했다.
// ReceiveDaemon의 테스트가 성공한다면
// KibReceiver 테스트는 따로 학인할 필요가 없다.
// 하지만
// KibReceiver에서 실제적인 전문처리와 비즈니스 로직이 있으므로
// 자세하게 비즈니스 로직을 검토할 시에는
// KibReceiver의 테스트를 작동시키면서
// 비즈니스 로직을 이해해야 한다.
public class ReceiveDaemonTest {

	// ReceiveDaemonTest는 3가지 단계로 테스트 가능하도록 되어있다.
	// 1단계 - 각 개발자 로컬
	// 2단계 - 개발 서버
	// 3단계 - 리얼 서버
	// 이렇게 단계를 분리하기위해,
	// 각 정보를 분리 저장했다.
	// 3단계 리얼 서버 테스트는 전문을 새로 작성하는게 좋을듯
	private static final int 상수_전문_분리_수 = 3;
	
//	private static String devTestIP = "172.20.102.41";  // HIC 테스트서버; 
	private static String devTestIP = "172.20.101.20";  // HIC 대외계 테스트서버; 
	private static String localTestIP = "127.0.0.1";    // 로컬 
//	private static String RealTestIP = "172.20.101.41"; // HIC Real서버; 
//	private static String krTestIP = "192.168.4.200";  // KIBNET TEST
	
//	private String kibRealIP = "192.168.4.100";  // KIBNET REAL
	
	private static int krLocalPort = 9200;       // HIC TEST KibReceiver SERVER PORT
	private static int krDevPort   = 9300;       // HIC TEST KibReceiver SERVER PORT
//	private static int krRealPort  = 9800;       // HIC REAL KibReceiver SERVER PORT
	
//	private static int port = 50811;             // KIBNET PORT
//	private static int sdport            = 9130; // SenderDaemon PORT
	private static int rdImgAcntPort     = 9300; // ReceiveDaemon PORT 가상계좌
	private static int rdLocalImgAcntPort= rdImgAcntPort; // ReceiveDaemon PORT 가상계좌
//	private static int rdAcntQryAcntPort = 9310; // ReceiveDaemon PORT 거래명세

	// 개발자 로칼
//	private static String ip = localTestIP;
//	private static int  port = rdImgAcntPort;
	
	// 대외계 개발 서버
	private static String ip = null; //devTestIP;
	private static int  port = 0; //rdImgAcntPort;
	
	private static Socket     socket = null;
	private static BufferedReader in = null;
	private static BufferedWriter out= null;

	//                                                  20032901 - 수취조회
	//                                                  20032902 - 지급이체
	//                                                  20032903 - 가상계좌
	// 가상계좌전송 테스트시 기존 데이터 삭제 필요
	// 단 거래번호를 변경하게 되면 필요없음
	// 거래번호를 자동생성하도록 수정함 - 삭제하지 않아도 됨
	// 작업후, 테이블에 추가 됨
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1004' and tr_no = '201111030209662' and tr_dt = '2011-03'
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1003' and tr_no = '201111030209662' and tr_dt = '2011-03'
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 아래 위치의 것을 수정해서 테스트 할 것 (중복안되게 할것) - 중복되지 않으면 테이블 삭제 하지 않아도 됨
	// yyyymmdd - 테스트당일
	// hhmmss - 테스트시간
	// ###### - 거래순번
	// n234567890123456 - 가상 계좌 번호 입력
	//    - 테스트 가능한 계좌 5개 = 26665004518923 강미순 26665004518931 박명애 26665004518940 최석빈 26665004518956 김윤중 26665004518964 임종수
	/////////////////////////////////////////////////////////////////////////////////////////////////      yyyymmddhhmmss####### ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------     =============------------------------------------ n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
	
	
	@BeforeClass
	public static void setUpClass() {
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
//		setSocket(localTestIP, rdLocalImgAcntPort);
		setSocket(devTestIP, rdImgAcntPort);
		
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	private static void openSocket() {
		try {
			socket = new Socket(ip, port);
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			fail("socket..");
		} catch (IOException e) {
			e.printStackTrace();
			fail("socket open..");
		}			
	}
	
	@SuppressWarnings("unused")
	private static void openSocket(String _ip, int _port) {
		setSocket(_ip, _port);
		openSocket();
	}

	private static void setSocket(String _ip, int _port) {
		ip = _ip;
		port = _port;
	}
	
	private static void closeSocket() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("socket close..");
		}
	}
	
	private StringBuffer 전문수신() {
		StringBuffer rtnStr = new StringBuffer("");
		try {
			int response;
			int i = 0;
			int totCnt = 0;
			char[] cTot = new char[4];
			
			if (in.read(cTot) != -1) {
				rtnStr.append(new String(cTot));
				totCnt = StringUtil.cInt(StringUtil.CHSTRING(rtnStr.toString(), " ", "0"));
				// KIB는 전문길이가 전문에 포함되므로 이미꺼낸 4 byte 는 빼고 꺼낸다.
				for (i = 0; i < (totCnt - 4); i++) {
					response = in.read();
					if (response > 255) {
						i++;
					}
					rtnStr.append((char) response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Error Socket Write..");
		}
		return rtnStr;
	}

	private void 전문전송(String 전문) {
		try {
			out.write(전문);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Error Socket Write..");
		}
	}

	@Before
	public void setUp() throws Exception {
		openSocket();
	}

	@After
	public void tearDown() throws Exception {
		closeSocket();
	}

	@Test
	public void testLocal가상계좌이체_수취조회_계좌있음() {
		//    - 테스트 가능한 계좌 5개 = 26665004518923  강미순 26665004518931  박명애 26665004518940  최석빈 26665004518956  김윤중 26665004518964  임종수
		//                신한 계좌    = 56201550661748
		//////////////////////////////////////////////////////////////////////                   yyyymmddhhmmss#######-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    =============-=============-------------2003290300가상계좌번호3456n234567890123456namebankcode00출금계좌번호3456비밀번호name567890123456name-----==--==---============-----=============-------================---------------------------------------------------------------------------------------------
		String[] 수취조회_요청 = {
		/* 수최조회계좌있음*/             "0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518923                      0000002000123456789012            강미순              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌있음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518931                      0000002000123456789012            박명애              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌있음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518940                      0000002000123456789012            최석빈              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌있음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518956                      0000002000123456789012            김윤중              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌있음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518964                      0000002000123456789012            임종수              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌있음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030056201550661748                      0000002000123456789012            황상하              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌있음-신한*/		 ,"0585055000092003290300006000008880262810  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000260              ", "0000000090500 00000000000000000000000000200329030056201550443871                      0000008800123456789012            창업엔              00000                     0000000         00000000   000000  01112071319100000000                                                            "
//										 ,""
		};
		//////////////////////////////////////////////////////////////////////////////////////// yyyymmddhhmm4s#######------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------   n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 수취조회_응답 ={ 
		/* 수최조회계좌있음*/             "0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518923  강미순-하이         0000002000123456789012            강미순              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌있음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518931  박명애-하이         0000002000123456789012            박명애              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌있음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518940  최석빈-하이         0000002000123456789012            최석빈              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌있음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518956  김윤중-하이         0000002000123456789012            김윤중              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌있음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518964  임종수-하이         0000002000123456789012            임종수              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌있음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030056201550661748  황상하-하이         0000002000123456789012            황상하              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌있음-신한*/		 ,"0554055000092003290300005000008880262810  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000260              ", "0000000090500 00000000000000000000000000200329030056201550443871  장상영-하이         0000008800123456789012            창업엔              00000000000000000000000000000000000000000000000000000000000  01112071319100000000000000000               00000"
//										 ,""
		};

		String 보낼전송전문;
		String 예상수신전문;
		StringBuffer 받은전문;
		String 실제전문;
		String 전문번호;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		for(int i = 0; i < 수취조회_요청.length; i+=상수_전문_분리_수) {
			// 거래번호는 7자리지만, 맨앞의 한자리는 KIBNET에서 사용하므로 "0"로 세팅
			전문번호 = DateTimeUtil.getToday08()+DateTimeUtil.getTime06()+"0"+DateTimeUtil.getTime07().substring(0,6);
			보낼전송전문 = 수취조회_요청[i]
					      +전문번호
					      +수취조회_요청[i+1]
					      +수취조회_요청[i+2];
			
			예상수신전문 = 수취조회_응답[i]
					      +전문번호
					      +수취조회_응답[i+1]
					      +수취조회_응답[i+2];
			
			전문전송(보낼전송전문);
			받은전문 = 전문수신();
			실제전문 = 받은전문.toString();
			
			assertEquals(예상수신전문, 실제전문);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testLocal가상계좌이체_수취조회_계좌없음() {
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 아래 위치의 것을 수정해서 테스트 할 것 (중복안되게 할것) - 중복되지 않으면 테이블 삭제 하지 않아도 됨
		// yyyymmdd - 테스트당일
		// hhmmss - 테스트시간
		// ###### - 거래순번
		// n234567890123456 - 가상 계좌 번호 입력
		//    - 테스트 가능한 계좌 5개 = 26665004518923 강미순 26665004518931 박명애 26665004518940 최석빈 26665004518956 김윤중 26665004518964 임종수
		///////////////////////////////////////////////////////////////////////////////////////  yyyymmddhhmmss#######------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------     =============------------------------------------ n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 수취조회_요청 = {
		/* 수최조회계좌없음*/             "0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518924                      0000002000123456789012            강미순              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌없음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518932                      0000002000123456789012            박명애              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌없음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518941                      0000002000123456789012            최석빈              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌없음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518957                      0000002000123456789012            김윤중              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌없음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030026665004518965                      0000002000123456789012            임종수              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌없음*/            ,"0585055000092003290300006000008200040010  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000447              ", "0000000100000 00000000000000000000000000200329030056201550661749                      0000002000123456789012            황상하              00000                     0000000         00000000   000000  01111031546300000000                                                            "
		/* 수최조회계좌없음-신한*/		 ,"0585055000092003290300006000008880262810  302004100   ",                  "2020032903601032903                                                                                                                           20001347  0 3                                                      000260              ", "0000000090500 00000000000000000000000000200329030056201550443872                      0000008800123456789012            창업엔              00000                     0000000         00000000   000000  01112071319100000000                                                            "
//                                          , ""
		};
		///////////////////////////////////////////////////////////////////////////////////////  yyyymmddhhmmss#######------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------     =============------------------------------------ n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 수취조회_응답 ={ 
		/* 수최조회계좌없음*/             "0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518924  -하이               0000002000123456789012            강미순              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌없음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518932  -하이               0000002000123456789012            박명애              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌없음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518941  -하이               0000002000123456789012            최석빈              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌없음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518957  -하이               0000002000123456789012            김윤중              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌없음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030026665004518965  -하이               0000002000123456789012            임종수              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌없음*/            ,"0554055000092003290300005000008200040010  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              ", "0000000100000 00000000000000000000000000200329030056201550661749  -하이               0000002000123456789012            황상하              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
		/* 수최조회계좌없음-신한*/		 ,"0554055000092003290300005000008880262810  302104100000",                  "2020032903601032903                                                                                                                           20001347  0 3              정상                                    000260              ", "0000000090500 00000000000000000000000000200329030056201550443872  -하이               0000008800123456789012            창업엔              00000000000000000000000000000000000000000000000000000000000  01112071319100000000000000000               00000"
		};

		String 보낼전송전문;
		String 예상수신전문;
		StringBuffer 받은전문;
		String 실제전문;
		String 전문번호;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		for(int i = 0; i < 수취조회_요청.length; i+=상수_전문_분리_수) {
			// 거래번호는 7자리지만, 맨앞의 한자리는 KIBNET에서 사용하므로 "0"로 세팅
			전문번호 = DateTimeUtil.getToday08()+DateTimeUtil.getTime06()+"0"+DateTimeUtil.getTime07().substring(0,6);
			보낼전송전문 = 수취조회_요청[i]
				      +전문번호
				      +수취조회_요청[i+1]
				      +수취조회_요청[i+2];
		
			예상수신전문 = 수취조회_응답[i]
				      +전문번호
				      +수취조회_응답[i+1]
				      +수취조회_응답[i+2];
	
			전문전송(보낼전송전문);
			받은전문 = 전문수신();
			실제전문 = 받은전문.toString();
			
			assertEquals(예상수신전문, 실제전문);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testLocal가상계좌_입금_계좌있음() {
		//    - 테스트 가능한 계좌 5개 = 26665004518923 강미순 26665004518931 박명애 26665004518940 최석빈 26665004518956 김윤중 26665004518964 임종수
		/////////////////////////////////////////////////////////////////////////////////////////////////      yyyymmddhhmmss####### ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------     =============------------------------------------ n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 가상계좌_요청 = {
		/* 가상계좌입금있음*/             "0554055000092003290300006000008200040010  302001100   ", 						"2020032903601032903                                                                                                                           20001347  0 0                                                      000000              ", "0000000300000 00000000000000000000000000200329030026665004518923                      0000002000                        강미순              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
		};
		/////////////////////////////////////////////////////////////////////////////////////////////////      yyyymmddhhmm4s####### -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------   n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 가상계좌_응답 = { 
		/* 가상계좌입금있음*/             "0554055000092003290300005000008200040010  302101100000", 						"2020032903601032903                                                                                                                           20001347  0 0              정상                                    000000              ", "0000000300000 00000000000000000000000000200329030026665004518923                      0000002000                        강미순              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
        //                                  ---------------------------------------------------rrr-- 344 해당계좌없음
		};

		String 보낼전송전문;
		String 예상수신전문;
		StringBuffer 받은전문;
		String 실제전문;
		String 전문번호;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		for(int i = 0; i < 가상계좌_요청.length; i+=상수_전문_분리_수) {
			// 거래번호는 7자리지만, 맨앞의 한자리는 KIBNET에서 사용하므로 "0"로 세팅
			전문번호 = DateTimeUtil.getToday08()+DateTimeUtil.getTime06()+"0"+DateTimeUtil.getTime07().substring(0,6);
			보낼전송전문 = 가상계좌_요청[i]
				      +전문번호
				      +가상계좌_요청[i+1]
				      +가상계좌_요청[i+2];
		
			예상수신전문 = 가상계좌_응답[i]
				      +전문번호
				      +가상계좌_응답[i+1]
				      +가상계좌_응답[i+2];
			
			전문전송(보낼전송전문);
			받은전문 = 전문수신();
			실제전문 = 받은전문.toString();
			
			assertEquals(예상수신전문, 실제전문);
		}
	}

	@Test
	public void testLocal가상계좌_입금_계좌없음() {
		//    - 테스트 가능한 계좌 5개 = 26665004518923 강미순 26665004518931 박명애 26665004518940 최석빈 26665004518956 김윤중 26665004518964 임종수
		/////////////////////////////////////////////////////////////////////////////////////////////////      yyyymmddhhmmss####### ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------     =============------------------------------------ n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 가상계좌_요청 = {
		/* 가상계좌입금계좌없음*/              "0554055000092003290300006000008200040010  302001100   ", 						"2020032903601032903                                                                                                                           20001347  0 0                                                      000000              ", "0000000400000 00000000000000000000000000200329030026665004518911                      0000002000                        강미순              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
		};
		/////////////////////////////////////////////////////////////////////////////////////////////////      yyyymmddhhmm4s####### -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------   n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 가상계좌_응답 ={ 
		/* 가상계좌입금계좌없음*/              "0554055000092003290300005000008200040010  302101100344", 						"2020032903601032903                                                                                                                           20001347  0 0              해당계좌없음                            000000              ", "0000000400000 00000000000000000000000000200329030026665004518911                      0000002000                        강미순              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
        //                                  ---------------------------------------------------rrr-- 344 해당계좌없음
		};

		String 보낼전송전문;
		String 예상수신전문;
		StringBuffer 받은전문;
		String 실제전문;
		String 전문번호;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		for(int i = 0; i < 가상계좌_요청.length; i+=상수_전문_분리_수) {
			// 거래번호는 7자리지만, 맨앞의 한자리는 KIBNET에서 사용하므로 "0"로 세팅
			전문번호 = DateTimeUtil.getToday08()+DateTimeUtil.getTime06()+"0"+DateTimeUtil.getTime07().substring(0,6);
			보낼전송전문 = 가상계좌_요청[i]
				      +전문번호
				      +가상계좌_요청[i+1]
				      +가상계좌_요청[i+2];
		
			예상수신전문 = 가상계좌_응답[i]
				      +전문번호
				      +가상계좌_응답[i+1]
				      +가상계좌_응답[i+2];
			
			
			전문전송(보낼전송전문);
			받은전문 = 전문수신();
			실제전문 = 받은전문.toString();
			
			assertEquals(예상수신전문, 실제전문);
		}
	}

	@Test
	public void testLocal가상계좌_입금_중복() {
		String[] 가상계좌_요청 = {
		/* 가상계좌입금있음중복*/        "0554055000092003290300006000008200040010  302001100   ", 						"2020032903601032903                                                                                                                           20001347  0 0                                                      000000              ", "0000000500000 00000000000000000000000000200329030026665004518964                      0000002000                        임종수              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
        //                             , ""
		};
		/////////////////////////////////////////////////////////////////////////////////////////////////      yyyymmddhhmm4s####### -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------   n234567890123456name567890123456name---------------------------------------------------------------------------------------------------------------------------------------------
		String[] 가상계좌_응답 ={
	    //                                ---------------------------------------------------rrr-- 344 해당계좌없음
		/* 가상계좌입금있음중복*/        "0554055000092003290300005000008200040010  302101100000", "2020032903601032903                                                                                                                           20001347  0 0              정상                                    000000              ", "0000000500000 00000000000000000000000000200329030026665004518964                      0000002000                        임종수              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
		};

		String 보낼전송전문;
		String 예상수신전문;
		StringBuffer 받은전문;
		String 실제전문;
		String 전문번호;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		for(int i = 0; i < 가상계좌_요청.length; i+=상수_전문_분리_수) {
			// 거래번호는 7자리지만, 맨앞의 한자리는 KIBNET에서 사용하므로 "0"로 세팅
			전문번호 = DateTimeUtil.getToday08()+DateTimeUtil.getTime06()+"0"+DateTimeUtil.getTime07().substring(0,6);
			보낼전송전문 = 가상계좌_요청[i]
				      +전문번호
				      +가상계좌_요청[i+1]
				      +가상계좌_요청[i+2];
		
			예상수신전문 = 가상계좌_응답[i]
				      +전문번호
				      +가상계좌_응답[i+1]
				      +가상계좌_응답[i+2];
			
			전문전송(보낼전송전문);
			받은전문 = 전문수신();
			실제전문 = 받은전문.toString();
			
			assertEquals(예상수신전문, 실제전문);
	
			System.out.println("시간: " + DateTimeUtil.getTime07());
			//////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////
			보낼전송전문 = 가상계좌_요청[i]
				      +전문번호
				      +가상계좌_요청[i+1]
				      +가상계좌_요청[i+2];
		
			예상수신전문 = 가상계좌_응답[i]
				      +전문번호
				      +가상계좌_응답[i+1]
				      +가상계좌_응답[i+2];
			
			전문전송(보낼전송전문);
			받은전문 = 전문수신();
			실제전문 = 받은전문.toString();
			
			assertEquals(예상수신전문, 실제전문);
			System.out.println("시간: " + DateTimeUtil.getTime07());
		}
	}

}
