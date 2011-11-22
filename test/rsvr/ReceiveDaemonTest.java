package rsvr;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.StringUtil;

public class ReceiveDaemonTest {

	private static final int 상수_가상계좌전송테스트 = 0;
	private static final int 상수_예상전문_가상계좌01_수취조회 = 1;
	private static final int 상수_예상전문_가상계좌02_가상계좌입금 = 2;
	
	static String devTestIP = "172.20.102.41";  // HIC 테스트서버; 
	static String localTestIP = "127.0.0.1";    // 로컬 
//	static String RealTestIP = "172.20.101.41"; // HIC Real서버; 
	static String kibTestIP = "192.168.4.200";  // KIBNET TEST
	
//	String kibRealIP = "192.168.4.100";  // KIBNET REAL
	
	static int krLocalPort = 9200;       // HIC TEST KibReceiver SERVER PORT
	static int krDevPort   = 9300;       // HIC TEST KibReceiver SERVER PORT
	static int krRealPort  = 9800;       // HIC REAL KibReceiver SERVER PORT
	
//	static int port = 50811;             // KIBNET PORT
	static int sdport            = 9130; // SenderDaemon PORT
	static int rdImgAcntPort     = 9300; // ReceiveDaemon PORT 가상계좌
	static int rdAcntQryAcntPort = 9310; // ReceiveDaemon PORT 수취조회

	// 개발자 로칼
//	static String ip = localTestIP;
//	static int  port = rdImgAcntPort;
	
	// 대외계 개발 서버
	static String ip = devTestIP;
	static int  port = rdImgAcntPort;
	
	static Socket     socket = null;
	static BufferedReader in = null;
	static BufferedWriter out= null;

	//                                                  20032901 - 수취조회
	//                                                  20032902 - 지급이체
	//                                                  20032903 - 가상계좌
	// 가상계좌전송 테스트시 기존 데이터 삭제 필요
	// 작업후, 테이블에 추가 됨
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1004' and tr_no = '201111030209662' and tr_dt = '2011-03'
	String[] rd01_가상계좌              = { "0554055000092003290300006000008880120010  302001100   2011110315463102096622020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        강기주　　          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
			 /* 수최조회 */                , "0585055000092003290300006000008880040010  302004100   2011110320111102096872020032903601032903                                                                                                                           20001347  0 3                                                      000447              0000000100000 00000000000000000000000000200329030056201550207156                      0000008800123456789012            박정현              00000                     0000000         00000000   000000  01111031546300000000                                                            "
			 /* 지급이체 */                , "0554055000092003290300006000008880040010  302001100   2011110315465802098512020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000100000 00000000000000000000000000200329030056201550207156                      0000002600                        박정현              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};
	String[] rd02_rd_kb_가상계좌_KR전송 = { "0554055000092003290300006000008880120010  302001100   2011110315463102096622020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        강기주　　          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};
	String[] rd03_kb_rd_가상계좌_KR수신 = { "0554055000092003290300005000008880120010  3021011000002011110315463102096622020032903601032903                                                                                                                           20001347  0 0              정상                                    000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        강기주　　          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};
	String[] rd04_가상계좌              = { "0554055000092003290300005000008880120010  3021011000002011110315463102096622020032903601032903                                                                                                                           20001347  0 0              정상                                    000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        강기주　　          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
			/* 수최조회 */                 , "0554055000092003290300005000008880040010  3021041000002011110320111102096872020032903601032903                                                                                                                           20001347  0 3              정상                                    000447              0000000100000 00000000000000000000000000200329030056201550207156  박정현-하이         0000008800123456789012            박정현              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
			/* 지급이체 */                 , "0554055000092003290300005000008880040010  3021011000002011110315465802098512020032903601032903                                                                                                                           20001347  0 0              정상                                    000000              0000000100000 00000000000000000000000000200329030056201550207156                      0000002600                        박정현              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};

	String 테스트보낼전문 = "06040600000920032902000052003290220032902 30200110000020111107194220007611220000009880                                                                                                                                                                                                      000000              0000001000000 000000000000000000000000000000008800110276454855    김순녀              0000008800100024180210            하이캐피탈          0000000000              00              00              00              00              00               00000                                                  ";
	String 테스트예상전문 = "06040600000920032902000062003290220032902 3021011000002011110719425500761122000000988620032901                                                                                                                                     0 009236584      정상처리                                000000              0000001000000 000006835207400000000000000000008800110276454855    김순녀              0000008800100024180210            하이캐피탈          001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000                                                  ";
	
	
	@BeforeClass
	public static void setUpClass() {
//		try {
//			socket = new Socket(ip, port);
//			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//			fail("socket..");
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail("socket open..");
//		}			
	}
	
	@AfterClass
	public static void tearDownClass() {
//		try {
//			in.close();
//			out.close();
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail("socket close..");
//		}
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
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testKibReceiveSendMsg() {
		String 보낼전문 = 테스트보낼전문;

		전문전송(보낼전문);
		StringBuffer 받은전문 = 전문수신();
		String 실제전문 = 받은전문.toString();

		assertEquals(테스트예상전문, 실제전문);
	}

	@Test
	public void test가상계좌이체() {
		String 보낼전송전문;
		String 예상수신전문;
		StringBuffer 받은전문;
		String 실제전문;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		보낼전송전문 = rd01_가상계좌[상수_가상계좌전송테스트];
		예상수신전문 = rd04_가상계좌[상수_가상계좌전송테스트];
		전문전송(보낼전송전문);
		받은전문 = 전문수신();
		실제전문 = 받은전문.toString();
		
		assertEquals(예상수신전문, 실제전문);
		closeSocket();
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		보낼전송전문 = rd01_가상계좌[상수_예상전문_가상계좌01_수취조회];
		예상수신전문 = rd04_가상계좌[상수_예상전문_가상계좌01_수취조회];
		
		전문전송(보낼전송전문);
		받은전문 = 전문수신();
		실제전문 = 받은전문.toString();
		
		assertEquals(예상수신전문, 실제전문);
		closeSocket();

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		보낼전송전문 = rd01_가상계좌[상수_예상전문_가상계좌02_가상계좌입금];
		예상수신전문 = rd04_가상계좌[상수_예상전문_가상계좌02_가상계좌입금];
		
		전문전송(보낼전송전문);
		받은전문 = 전문수신();
		실제전문 = 받은전문.toString();
		
		assertEquals(예상수신전문, 실제전문);
		closeSocket();
	}

}
