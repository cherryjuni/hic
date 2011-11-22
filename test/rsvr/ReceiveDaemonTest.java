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

	private static final int ���_������������׽�Ʈ = 0;
	private static final int ���_��������_�������01_������ȸ = 1;
	private static final int ���_��������_�������02_��������Ա� = 2;
	
	static String devTestIP = "172.20.102.41";  // HIC �׽�Ʈ����; 
	static String localTestIP = "127.0.0.1";    // ���� 
//	static String RealTestIP = "172.20.101.41"; // HIC Real����; 
	static String kibTestIP = "192.168.4.200";  // KIBNET TEST
	
//	String kibRealIP = "192.168.4.100";  // KIBNET REAL
	
	static int krLocalPort = 9200;       // HIC TEST KibReceiver SERVER PORT
	static int krDevPort   = 9300;       // HIC TEST KibReceiver SERVER PORT
	static int krRealPort  = 9800;       // HIC REAL KibReceiver SERVER PORT
	
//	static int port = 50811;             // KIBNET PORT
	static int sdport            = 9130; // SenderDaemon PORT
	static int rdImgAcntPort     = 9300; // ReceiveDaemon PORT �������
	static int rdAcntQryAcntPort = 9310; // ReceiveDaemon PORT ������ȸ

	// ������ ��Į
//	static String ip = localTestIP;
//	static int  port = rdImgAcntPort;
	
	// ��ܰ� ���� ����
	static String ip = devTestIP;
	static int  port = rdImgAcntPort;
	
	static Socket     socket = null;
	static BufferedReader in = null;
	static BufferedWriter out= null;

	//                                                  20032901 - ������ȸ
	//                                                  20032902 - ������ü
	//                                                  20032903 - �������
	// ����������� �׽�Ʈ�� ���� ������ ���� �ʿ�
	// �۾���, ���̺� �߰� ��
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1004' and tr_no = '201111030209662' and tr_dt = '2011-03'
	String[] rd01_�������              = { "0554055000092003290300006000008880120010  302001100   2011110315463102096622020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
			 /* ������ȸ */                , "0585055000092003290300006000008880040010  302004100   2011110320111102096872020032903601032903                                                                                                                           20001347  0 3                                                      000447              0000000100000 00000000000000000000000000200329030056201550207156                      0000008800123456789012            ������              00000                     0000000         00000000   000000  01111031546300000000                                                            "
			 /* ������ü */                , "0554055000092003290300006000008880040010  302001100   2011110315465802098512020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000100000 00000000000000000000000000200329030056201550207156                      0000002600                        ������              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};
	String[] rd02_rd_kb_�������_KR���� = { "0554055000092003290300006000008880120010  302001100   2011110315463102096622020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};
	String[] rd03_kb_rd_�������_KR���� = { "0554055000092003290300005000008880120010  3021011000002011110315463102096622020032903601032903                                                                                                                           20001347  0 0              ����                                    000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};
	String[] rd04_�������              = { "0554055000092003290300005000008880120010  3021011000002011110315463102096622020032903601032903                                                                                                                           20001347  0 0              ����                                    000000              0000000099996 00000000000000000000000000200329030056201550486180                      0000002600                        �����֡���          000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
			/* ������ȸ */                 , "0554055000092003290300005000008880040010  3021041000002011110320111102096872020032903601032903                                                                                                                           20001347  0 3              ����                                    000447              0000000100000 00000000000000000000000000200329030056201550207156  ������-����         0000008800123456789012            ������              00000000000000000000000000000000000000000000000000000000000  01111031546300000000000000000               00000"
			/* ������ü */                 , "0554055000092003290300005000008880040010  3021011000002011110315465802098512020032903601032903                                                                                                                           20001347  0 0              ����                                    000000              0000000100000 00000000000000000000000000200329030056201550207156                      0000002600                        ������              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
                                          , ""
	};

	String �׽�Ʈ�������� = "06040600000920032902000052003290220032902 30200110000020111107194220007611220000009880                                                                                                                                                                                                      000000              0000001000000 000000000000000000000000000000008800110276454855    �����              0000008800100024180210            ����ĳ��Ż          0000000000              00              00              00              00              00               00000                                                  ";
	String �׽�Ʈ�������� = "06040600000920032902000062003290220032902 3021011000002011110719425500761122000000988620032901                                                                                                                                     0 009236584      ����ó��                                000000              0000001000000 000006835207400000000000000000008800110276454855    �����              0000008800100024180210            ����ĳ��Ż          001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000                                                  ";
	
	
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
	
	private StringBuffer ��������() {
		StringBuffer rtnStr = new StringBuffer("");
		try {
			int response;
			int i = 0;
			int totCnt = 0;
			char[] cTot = new char[4];
			
			if (in.read(cTot) != -1) {
				rtnStr.append(new String(cTot));
				totCnt = StringUtil.cInt(StringUtil.CHSTRING(rtnStr.toString(), " ", "0"));
				// KIB�� �������̰� ������ ���ԵǹǷ� �̹̲��� 4 byte �� ���� ������.
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

	private void ��������(String ����) {
		try {
			out.write(����);
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
		String �������� = �׽�Ʈ��������;

		��������(��������);
		StringBuffer �������� = ��������();
		String �������� = ��������.toString();

		assertEquals(�׽�Ʈ��������, ��������);
	}

	@Test
	public void test���������ü() {
		String ������������;
		String �����������;
		StringBuffer ��������;
		String ��������;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		������������ = rd01_�������[���_������������׽�Ʈ];
		����������� = rd04_�������[���_������������׽�Ʈ];
		��������(������������);
		�������� = ��������();
		�������� = ��������.toString();
		
		assertEquals(�����������, ��������);
		closeSocket();
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		������������ = rd01_�������[���_��������_�������01_������ȸ];
		����������� = rd04_�������[���_��������_�������01_������ȸ];
		
		��������(������������);
		�������� = ��������();
		�������� = ��������.toString();
		
		assertEquals(�����������, ��������);
		closeSocket();

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		������������ = rd01_�������[���_��������_�������02_��������Ա�];
		����������� = rd04_�������[���_��������_�������02_��������Ա�];
		
		��������(������������);
		�������� = ��������();
		�������� = ��������.toString();
		
		assertEquals(�����������, ��������);
		closeSocket();
	}

}
