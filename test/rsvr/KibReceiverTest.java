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

	private static final int ���_��������_�������_�������� = 0;
	private static final int ���_��������_�������_������ȸ = 1;
	private static final int ���_��������_�������_�����Ա� = 2;
	
	private static String devIP = "172.20.102.41";  // HIC �׽�Ʈ����; 
	private static String localIP = "127.0.0.1";    // ���� 
//	private static String RealTestIP = "172.20.101.41"; // HIC Real����; 
	private static String kibTestIP = "192.168.4.200";  // KIBNET TEST
	
//	private String kibRealIP = "192.168.4.100";  // KIBNET REAL
	
	private static int krLocalPort = 9200;       // HIC TEST KibReceiver SERVER PORT
	private static int krDevPort   = 9300;       // HIC TEST KibReceiver SERVER PORT
	private static int krRealPort  = 9800;       // HIC REAL KibReceiver SERVER PORT
	
//	private static int port = 50811;             // KIBNET PORT
	private static int sdport            = 9130; // SenderDaemon PORT
	private static int rdImgAcntPort     = 9300; // ReceiveDaemon PORT �������
	private static int rdAcntQryAcntPort = 9310; // ReceiveDaemon PORT ������ȸ

	// ������ ��Į
//	private static String ip = localTestIP;
//	private static int  port = rdImgAcntPort;
	
	// ��ܰ� ���� ����
	private static String ip = devIP;
	private static int  port = rdImgAcntPort;
	
	private static Socket     socket = null;
	private static BufferedReader in = null;
	private static BufferedWriter out= null;

	//                                                  20032901 - ������ȸ
	//                                                  20032902 - ������ü
	//                                                  20032903 - �������
	// ����������� �׽�Ʈ�� ���� ������ ���� �ʿ�
	// �۾���, ���̺��� �߰� ��
	// delete from buser.bvat_cm_iamt_desc where tr_org_cd = 'C1004' and tr_no = '201111030209662' and tr_dt = '2011-03'
	private String[] kr01_�������_��������_���� = { 
			// ��������  0800 / 2100 [504]
			// ����� ����
			"05040500000920032903000069999999620032903 4080021000002011112400124100000010020032903001032903                                                                                                                                     00000000000                                                                  20111124001010010   0300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                    "
			// ������ȸ 0200 / 4100 [585]
			,"0585055000092003290300006000008880050010  302004100   2011112420111100040492020032903601032903                                                                                                                           20001347  0 3                                                      000002              0000000129979 00000000000000000000000000200329030056201550473773                      0000008800123456789012            ��ȯ��              00000                     0000000         00000000   000000  01111240013100000000                                                            "
			// �����Ա� 0200 / 1100 [554]
			,"0554055000092003290300006000008880263370  302001100   2011112400152400051532020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        ����              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
	};
	private String[] kr02_�������_��������_���� = {
			// ��������  0810 / 2100 [504] -> [500] - ���ϱ��̰� 500���� �Ǿ����� .. ���� ����
			//"05000500000920032903000059999999620032903 4081021000002011112400124100000010020032903001032903                                                                                                                                     00000000000                                                                  20111124001010010   0300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                    "
			""
			// ������ȸ 0210 / 4100 [554]
			,"0554055000092003290300005000008880050010  3021041000002011112420111100040492020032903601032903                                                                                                                           20001347  0 3              ����                                    000002              0000000129979 00000000000000000000000000200329030056201550473773  ����ȣ-����         0000008800123456789012            ��ȯ��              00000000000000000000000000000000000000000000000000000000000  01111240013100000000000000000               00000"
			// �����Ա� 0210 / 1100 [554]
			,"0554055000092003290300005000008880263370  3021011000002011112400152400051532020032903601032903                                                                                                                           20001347  0 0              ����                                    000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        ����              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
	};
	private String[] kr01_�������_��������_�츮 = { 
			// ��������  0800 / 2100 [504]
			// ����� ����
			""
			// ������ȸ 0200 / 4100 [585]
			,"0585055000092003290300006000008880050010  302004100   2011112420111100040492020032903601032903                                                                                                                           20001347  0 3                                                      000002              0000000129979 00000000000000000000000000200329030056201550473773                      0000008800123456789012            ��ȯ��              00000                     0000000         00000000   000000  01111240013100000000                                                            "
			// �����Ա� 0200 / 1100 [554]
			,"0554055000092003290300006000008880263370  302001100   2011112400152400051532020032903601032903                                                                                                                           20001347  0 0                                                      000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        ����              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
	};
	private String[] kr02_�������_��������_�츮 = {
			// ��������  0810 / 2100 [504] -> [500] - ���ϱ��̰� 500���� �Ǿ����� .. ���� ����
			//"05000500000920032903000059999999620032903 4081021000002011112400124100000010020032903001032903                                                                                                                                     00000000000                                                                  20111124001010010   0300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                    "
			""
			// ������ȸ 0210 / 4100 [554]
			,"0554055000092003290300005000008880050010  3021041000002011112420111100040492020032903601032903                                                                                                                           20001347  0 3              ����                                    000002              0000000129979 00000000000000000000000000200329030056201550473773  ����ȣ-����         0000008800123456789012            ��ȯ��              00000000000000000000000000000000000000000000000000000000000  01111240013100000000000000000               00000"
			// �����Ա� 0210 / 1100 [554]
			,"0554055000092003290300005000008880263370  3021011000002011112400152400051532020032903601032903                                                                                                                           20001347  0 0              ����                                    000000              0000000168373 00000000000000000000000000200329030056201552094550                      0000002600                        ����              000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000               00000"
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

	@Test
	public void testLocalKibReceiveSendMsg() {
		String ��������;
		String ��������;
		StringBuffer ��������;
		String ��������;

		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
//		setSocket(krTestIP, krTestPort);
		setSocket(localIP, krLocalPort);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		��������= kr01_�������_��������_����[���_��������_�������_��������];
		��������= kr02_�������_��������_����[���_��������_�������_��������];
		
		openSocket();
		��������(��������);
		�������� = ��������();
		closeSocket();
		�������� = ��������.toString();

		assertEquals(��������, ��������);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		��������= kr01_�������_��������_����[���_��������_�������_������ȸ];
		��������= kr02_�������_��������_����[���_��������_�������_������ȸ];
		
		openSocket();
		��������(��������);
		�������� = ��������();
		closeSocket();
		�������� = ��������.toString();

		assertEquals(��������, ��������);
		
		//////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////
		��������= kr01_�������_��������_����[���_��������_�������_�����Ա�];
		��������= kr02_�������_��������_����[���_��������_�������_�����Ա�];
		
		openSocket();
		��������(��������);
		�������� = ��������();
		closeSocket();
		�������� = ��������.toString();

		assertEquals(��������, ��������);
		
	}

//	@Test
//	public void test���������ü() {
//		String ������������;
//		String �����������;
//		StringBuffer ��������;
//		String ��������;
//
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		setSocket(devTestIP, rdImgAcntPort);
//		
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		������������ = rd01_�������[���_������������׽�Ʈ];
//		����������� = rd04_�������[���_������������׽�Ʈ];
//		openSocket();
//		��������(������������);
//		�������� = ��������();
//		closeSocket();
//		�������� = ��������.toString();
//		
//		assertEquals(�����������, ��������);
//		
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		������������ = rd01_�������[���_��������_�������01_������ȸ];
//		����������� = rd04_�������[���_��������_�������01_������ȸ];
//		
//		openSocket();
//		��������(������������);
//		�������� = ��������();
//		closeSocket();
//		�������� = ��������.toString();
//		
//		assertEquals(�����������, ��������);
//
//		//////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////
//		openSocket();
//		������������ = rd01_�������[���_��������_�������02_��������Ա�];
//		����������� = rd04_�������[���_��������_�������02_��������Ա�];
//		
//		��������(������������);
//		�������� = ��������();
//		�������� = ��������.toString();
//		
//		assertEquals(�����������, ��������);
//		closeSocket();
//	}

}