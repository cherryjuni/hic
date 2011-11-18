package ssvr;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.StringUtil;

public class SendDaemonTest {
	static String devTestIP = "172.20.102.41";  // HIC �׽�Ʈ����; 
	static String localTestIP = "127.0.0.1";    // ���� 
//	static String RealTestIP = "172.20.101.41"; // HIC Real����; 
	static String kibTestIP = "192.168.4.200";  // KIBNET TEST
	
//	String kibRealIP = "192.168.4.100";  // KIBNET REAL
	static String ip = localTestIP;
	
	static int krLocalPort = 9200;       // HIC TEST KibReceiver SERVER PORT
	static int krDevPort   = 9300;       // HIC TEST KibReceiver SERVER PORT
	static int krRealPort  = 9800;       // HIC REAL KibReceiver SERVER PORT
	
//	static int port = 50811;             // KIBNET PORT
	static int sdport            = 9130; // SenderDaemon PORT
	static int rdImgAcntPort     = 9300; // ReceiveDaemon PORT �������
	static int rdAcntQryAcntPort = 50811; // ReceiveDaemon PORT ������ȸ
	static int port = sdport;
	
	static Socket socket = null;
	static BufferedReader in   = null;
	static BufferedWriter out  = null;

	//                                                  20032901 - ������ȸ
	//                                                  20032902 - ������ü
	//                                                  20032903 - �������
	String sd01_���ް�������              = "0554055000092003290300006000008880120010  302001100   2011110315463102096622020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000";
	String sd02_sd_kib_���ް�������_KR���� = "0554055000092003290300006000008880120010  302001100   2011110315463102096622020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000";
	String sd03_kib_sd_���ް��¼���_KR���� = "0554055000092003290300005000008880120010  3021011000002011110315463102096622020032903601032903                                                                                                                           20001347  0 0              ����                                    000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000";
	String sd04_���ް��¼���              = "0554055000092003290300005000008880120010  3021011000002011110315463102096622020032903601032903                                                                                                                           20001347  0 0              ����                                    000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000";

	String testGram =     "06040600000920032902000052003290220032902 30200110000020111107194220007611220000009880                                                                                                                                                                                                      000000              0000001000000 000000000000000000000000000000008800110276454855    �����              0000008800100024180210            ����ĳ��Ż          0000000000              00              00              00              00              00               00000                                                  ";
	String expectedGram = "06040600000920032902000062003290220032902 3021011000002011110719425500761122000000988620032901                                                                                                                                     0 009236584      ����ó��                                000000              0000001000000 000006835207400000000000000000008800110276454855    �����              0000008800100024180210            ����ĳ��Ż          001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000                                                  ";

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
		try {
			out.write(sd01_���ް�������);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Error Socket Write..");
		}
		
		StringBuffer rtnStr = new StringBuffer("");
		try {
			int response;
			int i = 0;
			int totCnt = 0;
			char[] cTot = new char[4];
			
			if (in.read(cTot) != -1) {
				rtnStr.append(new String(cTot));
				totCnt = StringUtil.cInt(StringUtil.CHSTRING(rtnStr.toString(), " ", "0"));
				// RD�� �������̰� ������ ���ԵǹǷ� �̹̲��� 4 byte �� ���� ������.
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
		String actuualMsg = rtnStr.toString();
		
		assertEquals(sd04_���ް��¼���, actuualMsg);
	}
}
