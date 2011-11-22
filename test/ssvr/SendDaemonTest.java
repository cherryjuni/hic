package ssvr;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

public class SendDaemonTest {
	private static final int 상수_지급이체테스트 = 0;
//	private static final int 상수_예상전문_지급이체01_수취조회 = 1;
//	private static final int 상수_예상전문_지급이체02_지급이체 = 2;
	
//	static String RealTestIP = "172.20.101.20"; // HIC Real서버; 
//	String kibRealIP = "192.168.4.100";  // KIBNET REAL
	static String sdRealIP = "172.20.101.20";  // KIBNET TEST - 개발자 컴에서 테스트 안됨
	static String sdTestIP = sdRealIP;  // HIC 테스트서버; 
	static String localTestIP = "127.0.0.1";    // 로컬 
	
	
//	static int port = 50811;             // KIBNET PORT
//	static int sdRealPort    = 9530;     // sendDaemon PORT REAL
	static int sdDevPort     = 9130;     // sendDaemon PORT DEV
	static int sdDevLocalPort= sdDevPort;// sendDaemon PORT LOCAL

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
	String[] sd01_지급이체              = { "06040600000920032902000052003290220032902 30200110000020111116181048007732920000009880                                                                                                                                                                                                      000000              0000003000000 000000000000000000000000000000008800110083688780    홍성희              0000008800100024180210            하이캐피탈          0000000000              00              00              00              00              00               00000                                                  "
                                          , ""
	};
	String[] sd02_rd_kb_지급이체_KIB전송 = { ""
                                          , ""
	};
	String[] sd03_kb_rd_지급이체_KIB수신 = { ""
                                          , ""
	};
	String[] sd04_지급이체              = { "06040600000920032902000062003290220032902 3021011000002011111618112500773292000000988620032901                                                                                                                                     0 002154736      정상처리                                000000              0000003000000 000016007061900000000000000000008800110083688780    홍성희              0000008800100024180210            하이캐피탈          001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000                                                  "
                                          , ""
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

	@Test
	public void testSendDaemonSendMsg() {
		String 보낼전송전문;
		String 예상수신전문;
		StringBuffer 받은전문;
		String 실제전문;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		보낼전송전문 = sd01_지급이체[상수_지급이체테스트];
		예상수신전문 = sd04_지급이체[상수_지급이체테스트];
		전문전송(보낼전송전문);
		받은전문 = 전문수신();
		실제전문 = 받은전문.toString();
		
		assertEquals(예상수신전문, 실제전문);
		closeSocket();
	}
}
