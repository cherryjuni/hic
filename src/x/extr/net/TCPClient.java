/** Main system          : x
  * Sub system           : net
  * Classname            : TCPClient.java
  * Initial date         : 2005.11.14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : DPS�� �̿��ϱ� ���� TCP Client�� �⺻ Class
  */

package x.extr.net;

import javax.naming.*;

import x.extr.log.*;
import x.extr.util.*;

/** DPS�� �̿��ϱ� ���� TCP Client�� �⺻ Class
  * @author Ȳ��õ
*/
public class TCPClient {
	static String sSystem = "net";
	static String sClassName = "TCPClient";
	private static TCPClient m_instance = null;
	public  static Context ctx;

	private static int MODE_START = 0;
	private static int MODE_SENDING = 1;
	private static int MODE_CHECK = 2;
	private static int MODE_FINISH = 3;

	/** ������
      */
	private TCPClient(){}

	/** ������ �����Ǿ��ִ� Instance�� ��ȯ
	  * @return TCPClient
      */
	public static TCPClient getInstance() {
		System.out.println("02S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.getInstance Start");

		if (m_instance == null) {
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "TCPClient �ν��Ͻ��� ���θ����.");
			m_instance = new TCPClient();					// ��ü�� �����Ѵ�
			TCPClient.init();
		} else {
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "TCPClient �ν��Ͻ��� �׳ɾ���.");
		}
		/* 2006.04.19 �߰� */
		System.setProperty("java.naming.factory.initial","jeus.jndi.JNSContextFactory");
		
		System.out.println("02E " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.getInstance Finish");
		return m_instance;
	}


	/** Instance�� �ʱ�ȭ
      */
	public static void init() {
		/*
		 * �ν��Ͻ��� �������������� ����
		 * ���� �Ѵٸ� �����ܰ���� ����
		 */
		if (m_instance == null) {
			m_instance = new TCPClient();
		}

		try {
    		/* Java Application ���� JNDI ����� ���� 
    		 * 
    		 * ���ø����̼ǿ��� �� ����� ���� JNDI�� �����ϱ����ؼ��� JNDI�� ������ �ʿ��ϴ�.
    		 * ��������� ���̵�� ��ȣ�̸� ������Ƽ�� ����Ͽ� �����Ѵ�.
    		 * */
			java.util.Properties pw = new java.util.Properties();
			pw.put(Context.SECURITY_PRINCIPAL, "administrator");
			pw.put(Context.SECURITY_CREDENTIALS, "jeusadmin");
			//pw.put(Context.PROVIDER_URL, "192.168.0.205:9736");
			//pw.put(Context.INITIAL_CONTEXT_FACTORY,"jeus.jndi.JEUSContextFactory");
			
			
			TCPClient.ctx = new InitialContext(pw);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String callHome(String packet) throws Exception {
		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.callHome Start");
		String sBuf = null;
		
		try{
			TCPClientPool sm = (TCPClientPool)TCPClient.ctx.lookup("ONLINECLIENT");
			sBuf = sm.callWithoutSeqForHome(packet + "\n");
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
		
		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.callPDS Finish");
		
		return sBuf;
	}

	public void callPDS ( String packet, String poolName ) throws Exception {
		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.callPDS Start");
		
		try{
			TCPClientPool sm = (TCPClientPool)TCPClient.ctx.lookup(poolName);
			/* 
			 * PDS ��Ȯ�� 170 byte �� �����ϴ°����� ���. ���๮��(\n)�� 
			 * ���ڿ��� ���� �˸��� ����� ������� �ƴ���
			 */
			sm.callWithoutSeqForPDS(packet); 
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
		
		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.callPDS Finish");
		
		return;
	}

	/** DPS�� Online�������� �����ϰ� ȸ���� �����Ѵ�.
      * @param of                  ������ �¶���������
	  * @return String
      */
	public String callOnline ( OnlineFrame of ) throws Exception {
		return callOnline(of,"ONLINECLIENT");
	}

	public String callOnline ( OnlineFrame of, String poolName ) throws Exception {
		String sBuf = null;
		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.callOnline Start");

		try{
			TCPClientPool sm = (TCPClientPool)TCPClient.ctx.lookup(poolName);
			sBuf = sm.callWithoutSeq(of.toString()+"\n");
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.callOnline Finish");
		return sBuf;
	}

	/** DPS�� FTP�������� �����Ѵ�.
      * @param ff                     ������ FTP������
	  * @return boolean
      */
	public int callFTP ( FTPFrame ff ) {
		return callFTP(ff,"FTPCLIENT");
	}

	/** DPS�� FTP�������� �����Ѵ�.
      * @param ff                     ������ FTP������
      * @param poolName               ���ۿ� �̿��� Pool��
	  * @return int(01 = ����, 02 = ���������� FTP���۽����� �ź�, 03 = �˼����� ������ ����, 04 = �������� �ʴ� ����, 05 = �� �� ���� ����)
      */
	public int callFTP (FTPFrame ff,String poolName) {
		int iRet = 1;
		String sBuf = null;
		int count = 0;
		int rLen;
		String sFrame = "0015FSNS0000000";
		String sTemp = "";
		byte[] bBuf = new byte[1000];
		int mode = MODE_START;
		int poolId = -1;
		int sCount = 1;
		int sLen;

		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.FTP Start");

		try{
			TCPClientPool sm = (TCPClientPool)TCPClient.ctx.lookup(poolName);

			do {
				poolId = sm.alloc();
				delay(100);
			} while (poolId < 0);
			
			System.out.println("[" + poolId + "]�� �Ҵ�޾ҽ��ϴ�.");

			while (true) {
				
				if (mode == MODE_START) {

					sTemp = "0015FSTS0001000" + ff.toPath;
					sLen = sTemp.getBytes().length;
					sTemp = new StringBuffer(sTemp).replace(0,4,FormatData.numToStr(sLen,4,"0")).toString();

					sBuf = sm.call(poolId,sTemp); //�ܼ���Ʈ���� ������ ��� ������ �޴´�.
					if (sBuf.substring(4,9).equals("FSTR0")) {
						mode = MODE_SENDING;
						System.out.println("Send[" + sTemp + "] Recv[" + sBuf + "] �ų��� ��������.");

						continue;
					} else if (sBuf.substring(4,9).equals("FSTR1")) {
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"���������� FTP���۽����� �ź��߽��ϴ�.");
						iRet = 2;
						break;
					} else {
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"[" + sBuf + "]�� �˼����� �������Դϴ�.");
						iRet = 3;
						break;
					}
				} else if (mode == MODE_SENDING) {
					//�ų��� ��������.(������ ������ ��� �����Ѵ�)
					count = sm.sendFTP(poolId,ff.fromPath,sCount);
					mode = MODE_CHECK;
					continue;
				} else if (mode == MODE_CHECK) {
					// �ܼ���Ʈ���� ������ ��� ������ �޴´�.
					sBuf = sm.call(poolId,"0015FEDS0" + FormatData.numToStr(count,6,"0"));
					if (sBuf.substring(4,9).equals("FEDR0")) {
					/*
					    ��������: ������ ������ ũ�Ⱑ 0�� �ܿ� Message�� �˷��ش�.
					 */
						System.out.println("Send[0015FSTS0001000] Recv[" + sBuf + "] FTP���ۿϷ�.");
						if (count == 0){
							iRet = 4;
							Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"������ �õ��� ������ ũ�Ⱑ 0�Դϴ�. ");
						}else{
							iRet = 1;
						}						
						break;
					} else if (sBuf.substring(4,9).equals("FEDR1")) {
						// �̷� ���� ����.
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"[" + sBuf + "]�� �˼����� �������Դϴ�.");
						iRet = 3;
						break;
					} else if (sBuf.substring(4,8).equals("FRSR")) {
						sCount = Integer.parseInt(sBuf.substring(9,15));
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"���������� [" + sCount + "]�����Ӻ��� FTP�������� �䱸�߽��ϴ�.");
						mode = MODE_SENDING;
						continue;
					} else {
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"[" + sBuf + "]�� �˼����� �������Դϴ�.");
						iRet = 3;
						break;
					}
				} else if (mode == MODE_FINISH) {
					iRet = 1;
					break;
				}
			}

			sm.free(poolId);
		} catch(Exception e) {
			e.printStackTrace();
			Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,e.getMessage());
			iRet = 5;
		}

		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.FTP Finish");
		
		return iRet;
	}

	/** �������� delayó���� ���� �޼ҵ�
      * @param msec              ����� �ð�(milli second)
	  * @return int
      */
	private int delay( int msec ) {
		int newSec = msec;
		try {
			Thread.sleep(msec);
			newSec = msec * 2;
			if (newSec > 5000) { newSec = 5000; };
		} catch (Exception e) {	}

		return newSec;
	}

/*
	public static String callOnline ( OnlineFrame of, String poolName ) {
		String sBuf = null;

		try{
			PropertyManager pm = PropertyManager.getInstance();
			java.util.Properties pt = new java.util.Properties();
			pt.put(Context.INITIAL_CONTEXT_FACTORY, (String)(pm.getProperty("SYS_FACTORY")));
			pt.put(Context.PROVIDER_URL, (String)(pm.getProperty("NET_URL")));
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "Online 5");

			Context ctx = new InitialContext(pt);
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "Online 6");
			TCPClientPool sm = (TCPClientPool)ctx.lookup(poolName);
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "Online 7");

			sBuf = sm.call(of.toString());
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "Online 8");

		} catch(Exception e) {
			Log.writeLog(sSystem, sClassName, "callOnline()", Log.LOG_CRITICAL,e.getMessage());
			sBuf = null;
		}

		return sBuf;

	}
*/

}