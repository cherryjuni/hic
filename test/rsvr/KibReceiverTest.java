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

public class KibReceiverTest {

	private static final int 상수_예상전문_가상계좌_개시전문 = 0;
	private static final int 상수_예상전문_가상계좌_수취조회 = 1;
	private static final int 상수_예상전문_가상계좌_가상입금 = 2;
	
	private static String devIP = "172.20.102.41";  // HIC 테스트서버; 
	private static String localIP = "127.0.0.1";    // 로컬 
//	private static String RealTestIP = "172.20.101.41"; // HIC Real서버; 
	private static String kibTestIP = "192.168.4.200";  // KIBNET TEST
	
//	private String kibRealIP = "192.168.4.100";  // KIBNET REAL
	
	private static int krLocalPort = 9200;       // HIC TEST KibReceiver SERVER PORT
	private static int krDevPort   = 9300;       // HIC TEST KibReceiver SERVER PORT
	private static int krRealPort  = 9800;       // HIC REAL KibReceiver SERVER PORT
	
//	private static int port = 50811;             // KIBNET PORT
	private static int sdport            = 9130; // SenderDaemon PORT
	private static int rdImgAcntPort     = 9300; // ReceiveDaemon PORT 가상계좌
	private static int rdAcntQryAcntPort = 9310; // ReceiveDaemon PORT 수취조회

	// 개발자 로칼
//	private static String ip = localTestIP;
//	private static int  port = rdImgAcntPort;
	
	// 대외계 개발 서버
	private static String ip = devIP;
	private static int  port = rdImgAcntPort;
	
	private static Socket     socket = null;
	private static BufferedReader in = null;
	private static BufferedWriter out= null;

	//                                                  20032901 - 수취조회
	//                                                  20032902 - 지급이체
	//                                                  20032903 - 가상계좌
	// 가상계좌전송 테스트시 기존 데이터 삭제 필요
	// 작업후, 테이블에 추가 됨
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1004' and tr_no = '201111030209662' and tr_dt = '2011-03'
	private String[] kr01_가상계좌_전송전문_신한 = { 
			// 개시전문  0800 / 2100 [504]
			// 은행과 무관
			"05040500000920032903000069999999620032903 4080021000002011112400124100000010020032903001032903                                                                                                                                     00000000000                                                                  20111124001010010   0300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                    "
			// 수취조회 0200 / 4100 [585]
			,"0585055000092003290300006000008880050010  302004100   2011112420111100040492020032903601032903                                                                                                                           20001347  0 3                                                      000002              0000000129979 00000000000000000000000000200329030056201550473773                      0000008800123456789012            외환은              00000                     0000000         00000000   000000  01111240013100000000                                                            "
			// 가상입금 0200 / 1100 [554]
			,"0554055000092003290300006000008880263370  302001100   2011112400152400051532020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        김용민              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
	};
	private String[] kr02_가상계좌_예상전문_신한 = {
			// 개시전문  0810 / 2100 [504] -> [500] - 소켓길이가 500으로 되어있음 .. 내부 버그
			//"05000500000920032903000059999999620032903 4081021000002011112400124100000010020032903001032903                                                                                                                                     00000000000                                                                  20111124001010010   0300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                    "
			"05000500000920032903000059999999620032903 4081021000002011112400124100000010020032903001032903                                                                                                                                     00000000000                                                                  20111124001010010   0300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                "
			// 수취조회 0210 / 4100 [554]
			,"0554055000092003290300005000008880050010  3021041000002011112420111100040492020032903601032903                                                                                                                           20001347  0 3              정상                                    000002              0000000129979 00000000000000000000000000200329030056201550473773  권종호-하이         0000008800123456789012            외환은              00000000000000000000000000000000000000000000000000000000000  01111240013100000000000000000               00000"
			// 가상입금 0210 / 1100 [554]
			,"0554055000092003290300005000008880263370  3021011000002011112400152400051532020032903601032903                                                                                                                           20001347  0 0              정상                                    000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        김용민              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
	};
	
	private String[] kr01_가상계좌_전송전문_우리 = { 
			// 개시전문  0800 / 2100 [504]
			// 은행과 무관
			""
			// 수취조회 0200 / 4100 [585]
			,"0585055000092003290300006000008880050010  302004100   2011112420111100040492020032903601032903                                                                                                                           20001347  0 3                                                      000002              0000000129979 00000000000000000000000000200329030056201550473773                      0000008800123456789012            외환은              00000                     0000000         00000000   000000  01111240013100000000                                                            "
			// 가상입금 0200 / 1100 [554]
			,"0554055000092003290300006000008880263370  302001100   2011112400152400051532020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        김용민              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
	};
	private String[] kr02_가상계좌_예상전문_우리 = {
			// 개시전문  0810 / 2100 [504] -> [500] - 소켓길이가 500으로 되어있음 .. 내부 버그
			//"05000500000920032903000059999999620032903 4081021000002011112400124100000010020032903001032903                                                                                                                                     00000000000                                                                  20111124001010010   0300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                    "
			""
			// 수취조회 0210 / 4100 [554]
			,"0554055000092003290300005000008880050010  3021041000002011112420111100040492020032903601032903                                                                                                                           20001347  0 3              정상                                    000002              0000000129979 00000000000000000000000000200329030056201550473773  권종호-하이         0000008800123456789012            외환은              00000000000000000000000000000000000000000000000000000000000  01111240013100000000000000000               00000"
			// 가상입금 0210 / 1100 [554]
			,"0554055000092003290300005000008880263370  3021011000002011112400152400051532020032903601032903                                                                                                                           20001347  0 0              정상                                    000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        김용민              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
	};

	
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
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testLocalKibReceiveSendMsg() {
		String 보낼전문;
		String 예상전문;
		StringBuffer 받은전문;
		String 실제전문;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
//		setSocket(krTestIP, krTestPort);
		setSocket(localIP, krLocalPort);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		보낼전문= kr01_가상계좌_전송전문_신한[상수_예상전문_가상계좌_개시전문];
		예상전문= kr02_가상계좌_예상전문_신한[상수_예상전문_가상계좌_개시전문];
		
		openSocket();
		전문전송(보낼전문);
		받은전문 = 전문수신();
		closeSocket();
		실제전문 = 받은전문.toString();

		assertEquals(예상전문, 실제전문);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		보낼전문= kr01_가상계좌_전송전문_신한[상수_예상전문_가상계좌_수취조회];
		예상전문= kr02_가상계좌_예상전문_신한[상수_예상전문_가상계좌_수취조회];
		
		openSocket();
		전문전송(보낼전문);
		받은전문 = 전문수신();
		closeSocket();
		실제전문 = 받은전문.toString();

		assertEquals(예상전문, 실제전문);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		보낼전문= kr01_가상계좌_전송전문_신한[상수_예상전문_가상계좌_가상입금];
		예상전문= kr02_가상계좌_예상전문_신한[상수_예상전문_가상계좌_가상입금];
		
		openSocket();
		전문전송(보낼전문);
		받은전문 = 전문수신();
		closeSocket();
		실제전문 = 받은전문.toString();

		assertEquals(예상전문, 실제전문);
		
	}

	@Test
	public void testDevKibReceiveSendMsg() {
		String 보낼전문;
		String 예상전문;
		StringBuffer 받은전문;
		String 실제전문;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		setSocket(devIP, krDevPort);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		보낼전문= kr01_가상계좌_전송전문_신한[상수_예상전문_가상계좌_개시전문];
		예상전문= kr02_가상계좌_예상전문_신한[상수_예상전문_가상계좌_개시전문];
		
		openSocket();
		전문전송(보낼전문);
		받은전문 = 전문수신();
		closeSocket();
		실제전문 = 받은전문.toString();

		assertEquals(예상전문, 실제전문);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		보낼전문= kr01_가상계좌_전송전문_우리[상수_예상전문_가상계좌_수취조회];
		예상전문= kr02_가상계좌_예상전문_우리[상수_예상전문_가상계좌_수취조회];
		
		openSocket();
		전문전송(보낼전문);
		받은전문 = 전문수신();
		closeSocket();
		실제전문 = 받은전문.toString();

		assertEquals(예상전문, 실제전문);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		보낼전문= kr01_가상계좌_전송전문_우리[상수_예상전문_가상계좌_가상입금];
		예상전문= kr02_가상계좌_예상전문_우리[상수_예상전문_가상계좌_가상입금];
		
		openSocket();
		전문전송(보낼전문);
		받은전문 = 전문수신();
		closeSocket();
		실제전문 = 받은전문.toString();

		assertEquals(예상전문, 실제전문);
		
	}
	
//	@Test
//	public void test가상계좌이체() {
//		String 보낼전송전문;
//		String 예상수신전문;
//		StringBuffer 받은전문;
//		String 실제전문;
//
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		setSocket(devTestIP, rdImgAcntPort);
//		
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		보낼전송전문 = rd01_가상계좌[상수_가상계좌전송테스트];
//		예상수신전문 = rd04_가상계좌[상수_가상계좌전송테스트];
//		openSocket();
//		전문전송(보낼전송전문);
//		받은전문 = 전문수신();
//		closeSocket();
//		실제전문 = 받은전문.toString();
//		
//		assertEquals(예상수신전문, 실제전문);
//		
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		보낼전송전문 = rd01_가상계좌[상수_예상전문_가상계좌01_수취조회];
//		예상수신전문 = rd04_가상계좌[상수_예상전문_가상계좌01_수취조회];
//		
//		openSocket();
//		전문전송(보낼전송전문);
//		받은전문 = 전문수신();
//		closeSocket();
//		실제전문 = 받은전문.toString();
//		
//		assertEquals(예상수신전문, 실제전문);
//
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		openSocket();
//		보낼전송전문 = rd01_가상계좌[상수_예상전문_가상계좌02_가상계좌입금];
//		예상수신전문 = rd04_가상계좌[상수_예상전문_가상계좌02_가상계좌입금];
//		
//		전문전송(보낼전송전문);
//		받은전문 = 전문수신();
//		실제전문 = 받은전문.toString();
//		
//		assertEquals(예상수신전문, 실제전문);
//		closeSocket();
//	}

}
