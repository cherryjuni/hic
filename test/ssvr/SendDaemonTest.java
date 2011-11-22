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
	private static final int ���_������ü�׽�Ʈ = 0;
//	private static final int ���_��������_������ü01_������ȸ = 1;
//	private static final int ���_��������_������ü02_������ü = 2;
	
//	static String RealTestIP = "172.20.101.20"; // HIC Real����; 
//	String kibRealIP = "192.168.4.100";  // KIBNET REAL
	static String sdRealIP = "172.20.101.20";  // KIBNET TEST - ������ �Ŀ��� �׽�Ʈ �ȵ�
	static String sdTestIP = sdRealIP;  // HIC �׽�Ʈ����; 
	static String localTestIP = "127.0.0.1";    // ���� 
	
	
//	static int port = 50811;             // KIBNET PORT
//	static int sdRealPort    = 9530;     // sendDaemon PORT REAL
	static int sdDevPort     = 9130;     // sendDaemon PORT DEV
	static int sdDevLocalPort= sdDevPort;// sendDaemon PORT LOCAL

	// ������ ��Į
//	static String ip = localTestIP;
//	static int  port = sdDevLocalPort;
	
	// ��ܰ� ���� ����
	static String ip = sdTestIP;
	static int  port = sdDevPort;
	
	static Socket     socket = null;
	static BufferedReader in = null;
	static BufferedWriter out= null;

	//                                                  20032901 - ������ȸ
	//                                                  20032902 - ������ü
	//                                                  20032903 - �������
	// ����������� �׽�Ʈ�� ���� ������ ���� �ʿ�
	// �۾���, ���̺� �߰� ��
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1004' and tr_no = '201111030209662' and tr_dt = '2011-03'
	String[] sd01_������ü              = { "06040600000920032902000052003290220032902 30200110000020111116181048007732920000009880                                                                                                                                                                                                      000000              0000003000000 000000000000000000000000000000008800110083688780    ȫ����              0000008800100024180210            ����ĳ��Ż          0000000000              00              00              00              00              00               00000                                                  "
                                          , ""
	};
	String[] sd02_rd_kb_������ü_KIB���� = { ""
                                          , ""
	};
	String[] sd03_kb_rd_������ü_KIB���� = { ""
                                          , ""
	};
	String[] sd04_������ü              = { "06040600000920032902000062003290220032902 3021011000002011111618112500773292000000988620032901                                                                                                                                     0 002154736      ����ó��                                000000              0000003000000 000016007061900000000000000000008800110083688780    ȫ����              0000008800100024180210            ����ĳ��Ż          001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000                                                  "
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

	@Test
	public void testSendDaemonSendMsg() {
		String ������������;
		String �����������;
		StringBuffer ��������;
		String ��������;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		openSocket();
		������������ = sd01_������ü[���_������ü�׽�Ʈ];
		����������� = sd04_������ü[���_������ü�׽�Ʈ];
		��������(������������);
		�������� = ��������();
		�������� = ��������.toString();
		
		assertEquals(�����������, ��������);
		closeSocket();
	}
}
