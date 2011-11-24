package ssvr;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cabis.fw.util.gram.GramExecutor;

// SendDaemon 은 로컬에서 테스트 할 수 없다
//  SendDaemon이 KIBNET의 서버에 직접 연결되어야 테스트가능하다
//  SendDaemon 테스트는 개발서버의 SendDaemon 에 접속하여 테스트 하도록 작성했다.
// config.properties의 정보가 테스트 데이터베이스를 선택하도록 주의할 것.
//  이 환경파일이 리얼 데이터베이스를 선택하면 리얼의 SendDaemon을 호출 한다
// 아래 클래스의 설명을 주의 깊게 읽어보고 수정할 것
public class SendDaemonTest {
	// config.properties 에 의해 DB가 선택됨
	//   config.properties는 클래스 root 에 있어야 함
	// KIBNET 대외계관련 선언
	// 1. GRAM_CD 로 채널 정보 가져오기
	//    TABLE - GUSER.GSCT_GRAM_BASE
	//      DEV  - CHNL04
	//      REAL - CHNL14
	// 2. 1번에서 선택된 채널[CHNL04]에서 IP / PORT 가져오기
	//    TABLE - GUSER.GSCT_GRAM_CHNL
	//      DEV  - IP:172.20.101.20 PORT:9130
	//      REAL - IP:172.20.101.20 PORT:9530
	private final String GRAM_CD = "KB0200";

	private static final int 상수_지급이체테스트 = 0;
	private static final int 상수_지급이체테스트_20111124_01 = 1;
//	private static final int 상수_예상전문_지급이체01_수취조회 = 1;
//	private static final int 상수_예상전문_지급이체02_지급이체 = 2;
	
//	static String RealTestIP = "172.20.101.20"; // HIC Real서버; 
//	String kibRealIP = "192.168.4.100";  // KIBNET REAL
	final static String sdRealIP = "172.20.101.20";  // KIBNET TEST - 개발자 컴에서 테스트 안됨
	final static String sdTestIP = sdRealIP;  // HIC 테스트서버; 
	final static String localTestIP = "127.0.0.1";    // 로컬 
	
	
//	static int port = 50811;             // KIBNET PORT
//	static int sdRealPort    = 9530;     // sendDaemon PORT REAL
	final static int sdDevPort     = 9130;     // sendDaemon PORT DEV
	final static int sdDevLocalPort= sdDevPort;// sendDaemon PORT LOCAL

	// 개발자 로칼
//	static String ip = localTestIP;
//	static int  port = sdDevLocalPort;
	
	// 대외계 개발 서버
	static String ip = sdTestIP;
	static int  port = sdDevPort;
	
	static Socket     socket = null;
	static BufferedReader in = null;
	static BufferedWriter out= null;

	//                                                  20032901 - 수취조회
	//                                                  20032902 - 지급이체
	//                                                  20032903 - 가상계좌
	// 가상계좌전송 테스트시 기존 데이터 삭제 필요
	// 작업후, 테이블에 추가 됨
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1004' and tr_no = '201111030209662' and tr_dt = '2011-03'
	String[] sd01_지급이체              = { "06040600000920032902000052003290220032902 30200110000020111123100249007828220000009880                                                                                                                                                                                                      000000              0000004000000 000000000000000000000000000000000500620202237914    박주영              0000008800100024180210            하이캐피탈          0000000000              00              00              00              00              00               00000                                                  "
                                          , "06040600000920032902000052003290220032902 30200110000020111124125049007851720000009880                                                                                                                                                                                                      000000              0000003000000 000000000000000000000000000000000400219240194161    최홍길              0000008800100024180210            하이캐피탈          0000000000              00              00              00              00              00               00000                                                  "
	};
//	String[] sd02_rd_kb_지급이체_KIB전송 = { ""
//                                          , ""
//	};
//	String[] sd03_kb_rd_지급이체_KIB수신 = { ""
//                                          , ""
//	};
	String[] sd04_지급이체              = { "06040600000920032902000062003290220032902 3021011000002011112310032800782822000000988620032901                                                                                                                                     0 009412632      정상처리                                000000              0000004000000 000053464525100000000000000000000500620202237914    박주영              0000008800100024180210            하이캐피탈          002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000                                                  "
                                          , "06040600000920032902000062003290220032902 3021011000002011112412512800785172000000988620032901                                                                                                                                     0 003743888      정상처리                                000000              0000003000000 000043439525100000000000000000000400219240194161    최홍길              0000008800100024180210            하이캐피탈          002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000                                                  "
};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSendDaemonSendMsg() {
		String 보낼전송전문;
		String 예상수신전문;
		String 실제전문;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		보낼전송전문 = sd01_지급이체[상수_지급이체테스트_20111124_01];
		예상수신전문 = sd04_지급이체[상수_지급이체테스트_20111124_01];
		
		try {
			실제전문 = GramExecutor.getInstance().sendMsg(GRAM_CD, 보낼전송전문);
			// 전문에서
			// 날짜 부분 무시 - 50 column 8자리
			// 타임 부분 무시 - 58 column 6자리
			// 계좌잔액무시 - 319 column 13자리
			// 수수료금액 무시 (테스트와 리얼 차이) - 445 column 5자리
			
			assertEquals(예상수신전문.substring(  0,  50+5-1)
						, 실제전문.substring(  0,  55+5-1));
			
			assertEquals(예상수신전문.substring(5 + 50 + 8 + 6 , 5 + 50 + 8 + 6 + 17 - 1)
						, 실제전문.substring(5 + 50 + 8 + 6, 5 + 50 + 8 + 6 + 17 - 1));
			
			assertEquals(예상수신전문.substring(5 + 50 + 8 + 6 + 17, 5 + 314 - 1)
						, 실제전문.substring(5 + 50 + 8 + 6 + 17, 5 + 314 - 1));
			
			assertEquals(예상수신전문.substring(5 + 319 + 13, 5 + 445 - 1)
						, 실제전문.substring(5 + 319 + 13, 5 + 445 - 1));
			
			assertEquals(예상수신전문.substring(5 + 445)
						, 실제전문.substring(5 + 445));
		} catch (Exception e) {
			fail("전문 송수신 실패..");
			e.printStackTrace();
		}
		
	}
}
