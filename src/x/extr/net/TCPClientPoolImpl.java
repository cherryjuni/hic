/** Main system          : x
  * Sub system           : net
  * Classname            : TCPClientPoolImpl.java
  * Initial date         : 2005.11.16
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : DPS�� ���ӵǾ��ִ� TCP Client�� ���ҽ�Ǯ
  * Version information  : v 1.0
  */
package x.extr.net;

import java.rmi.*;
import java.rmi.server.*;
import javax.naming.*;
import java.io.*;
import java.util.*;
import x.extr.log.*;
import x.extr.lib.*;
import x.extr.util.*;

/** DPS�� ���ӵǾ��ִ� TCP Client�� ���ҽ�Ǯ
  * @author Ȳ��õ
*/
public class TCPClientPoolImpl extends UnicastRemoteObject implements TCPClientPool {
	String sSystem = "net";
	String sClassName = "TCPClientPoolImpl";
	
	private final int RECONNECT_DELAY = 5000;
	private final int MAX_ALLOC_DELAY = 5000;
	private int seq = 0;
	private int sCnt = -1;
	private boolean sockAlloc[] = null;
	private TCPSockClient[] cSock = null;// �¶��� ������ ���ϰ���
	private TCPSockClient   sSock = null;// �ϰ����� ��Ŷ�� ���ϰ���
	private String 	remoteHost = "";
	private int 	remotePort = 0;
	//private String serverIp = "152.149.42.2";	//�¶��� ������ �� SenderR ������ IP
	private String serverIp = "192.168.50.11";	//�¶��� ������ �� SenderR ������ IP
	//private int serverPort = 60007;	//�¶��� ������ �� SenderR ������ IP
	private int serverPort = 9002;	//�¶��� ������ �� SenderR ������ IP
	//private String wasIp = "152.149.42.2";		//WAS RMI ���񽺸� ���� WAS IP
	private String wasIp = "192.168.50.11";		//WAS RMI ���񽺸� ���� WAS IP
	//private int wasPort = 9756;		//WAS RMI�� ���� ��Ʈ 
	private int wasPort = 9736;		//WAS RMI�� ���� ��Ʈ
	
	public String getServerIp(){
		return this.serverIp;
	}
	
	public int getServerPort(){
		return this.serverPort;
	}
	
	public String getWasIp(){
		return this.wasIp;
	}
	
	public int getWasPort(){
		return this.wasPort;
	}

	/** ������
      */
	public TCPClientPoolImpl() throws RemoteException { 
		super();
	};
	
	/** DPS������ ������ TCPSockClient���� �����Ѵ�.(�¶��� ���� ����)
      * @param cnt              ������ TCPSockClient�� ����
      * @param host             TCPSockClient�� ������ IP Address
      * @param port             TCPSockClient�� ������ Port
      * @exception RemoteException
	  */
	public void initSock(int cnt,String host,int port) throws RemoteException {
		remoteHost = host;
		remotePort = port; 
		sockAlloc  = new boolean[cnt];
		for (int i=0;i<cnt;i++) {
			sockAlloc[i] = false;
		}
		sCnt = cnt;

		try {
			cSock = new TCPSockClient[cnt];
			for (int i=0;i<cnt;i++) {
				cSock[i] = null;
			}
			for (int i=0;i<cnt;i++) {
				cSock[i] = new TCPSockClient(host,port);
				/*
				PDS�� Locking ��ī�������� ���Ͽ� 0.5�� delay�� ������ 
				�����Ѵ�.
				*/
				Thread.sleep(100);				
			}
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "initSock()", Log.LOG_CRITICAL, "initSock()ó���� �������߻��߽��ϴ�. : " + e.toString());
		}
	}

	/** DPS������ ������ TCPSockClient���� �����Ѵ�.(�ϰ����� ���ϻ���)
     * @param cnt              ������ TCPSockClient�� ����
     * @param host             TCPSockClient�� ������ IP Address
     * @param port             TCPSockClient�� ������ Port
     * @exception RemoteException
	  */
	public void initSock(String host,int port) throws RemoteException {
		try {
			// �ϰ����ۿ� ����ó�� ��� ����
			sSock = new TCPSockClient();			
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "initSock()", Log.LOG_CRITICAL, "initSock()ó���� �������߻��߽��ϴ�. : " + e.toString());
		}
	}
	
	/** TCPSockClient���� ���������Ѵ�.
      * @return boolean
      * @exception RemoteException
      */
	public boolean closeSock() throws RemoteException {
		try {
			for (int i=0;i<sCnt;i++) {
				if (cSock[i] != null) {
					cSock[i].close();
				}
			}
			return true;
		} catch (Exception e) {
			Log.writeLog(sSystem, sClassName, "closeSock()", Log.LOG_CRITICAL, "closeSock()ó���� �������߻��߽��ϴ�.");
			return false;
		}
	}

	/** ��밡���� TCPSockClient�� �Ҵ��Ѵ�.
      * @return int
      * @exception RemoteException
	  */
	public synchronized int alloc() throws RemoteException {
		for (int i=0;i<sCnt;i++) {
			if ((cSock[i] != null) && (!sockAlloc[i])) {
				sockAlloc[i] = true;
				System.out.println("["+i+"]������ �Ҵ�");
				return i;
			}
		}
		return -1;
	}

	/** ���̻� ������� �ʴ� TCPSockClient�� Pool�� ��ȯ�Ѵ�.
      * @param int           ��ȯ�� TCPSockClient �ε���
      * @return boolean
      * @exception RemoteException
	  */
	public synchronized boolean free(int index) throws RemoteException {
		sockAlloc[index] = false;
		System.out.println("["+index+"]������ ����");
		return true;
	}

	/** ���� Pool�� ���¸� ���ڿ��� �����Ѵ�.
      * @return String
      * @exception RemoteException
	  */
	public String state() throws RemoteException {
		StringBuffer sBuf = new StringBuffer("State : ");
		for (int i=0;i<sCnt;i++) {
			if (cSock[i] == null) {
				sBuf.append("[X]");
			} else if (sockAlloc[i]) {
				sBuf.append("[U]");
			} else {
				sBuf.append("[_]");
			}
		}
		return sBuf.toString();
	}

	/** DPS�� ������ �����Ѵ�.
      * @param index           ���ۿ� ����� TCPSockClient �ε���
      * @param filename        ������ ���ϸ�
      * @param startCount      ���۽��� ī��Ʈ
      * @return int
      * @exception RemoteException
	  */
	public int sendFTP(int index,String fileName,int startCount) throws Exception,RemoteException {
		String sFrame = "0015FSNS0000000";
		byte[] bBuf = new byte[1000];
		int rLen = 1;
		int count = 0;
		int totalSize = 0;

		try {
			FileInputStream fis = new FileInputStream(fileName);

			Log.writeLog(sSystem, sClassName, "sendFTP", Log.LOG_DEBUG,"[" + fileName + "] ���۽���");
			while (rLen > 0) {
				rLen = fis.read(bBuf,0,1000);
				
				if (rLen > 0) {
					count++;
					totalSize += rLen;
					if (count >= startCount) {
						sFrame = new StringBuffer(sFrame).replace(0,4,FormatData.numToStr(rLen + 15,4,"0")).replace(9,15,FormatData.numToStr(count,6,"0")).toString();
						cSock[index].send(sFrame);
						cSock[index].send(bBuf,0,rLen);
					}
				}
			}
			fis.close();

			Log.writeLog(sSystem, sClassName, "sendFTP", Log.LOG_DEBUG,"[" + fileName + "] ���ۿϷ� (" + totalSize + " bytes)");

		} catch (Exception e) {
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_CRITICAL, "send()ó���� �������߻��߽��ϴ�.");
			return 0;
		}

		return count;
	}


	/** DPS�� ���ڿ��� Byte[]Ÿ���� �����͸� �����ؼ� �����Ѵ�.
      * @param index           ���ۿ� ����� TCPSockClient �ε���
      * @param data            ������ ���ڿ�
      * @param binaryData      ������ Byte������
      * @param offset          ������ ������ Byte�� offset
      * @param len             ������ Byte�� ����
      * @exception RemoteException
	  */
	public void send(int index,String data,byte[] binaryData,int offset,int len) throws Exception,RemoteException {
		
		try {
			cSock[index].send(data);
			cSock[index].send(binaryData,offset,len);
		} catch (Exception e) {
			try { 
				cSock[index].close(); 
			} catch (Exception ee) {}
			
			cSock[index] = null;
			throw new Exception("sending : I/O Exception");
		}

	}
	
	/** DPS�� ���ڿ��� �����Ѵ�.
      * @param index           ���ۿ� ����� TCPSockClient �ε���
      * @param data            ������ ���ڿ�
      * @exception RemoteException
	  */
	public void send(int index,String data) throws Exception,RemoteException {
		try {
			cSock[index].send(data);
			System.out.println("���ۿϷ�.........................................");
		} catch (Exception e) {
			try { 
				cSock[index].close(); 
			} catch (Exception ee) {}
			
			cSock[index] = null;
			System.out.println("send()ó���� �������߻��߽��ϴ�.");
			throw new Exception("send()ó���� �������߻��߽��ϴ�.");
		}

		return;
	}
	
	/** DPS�� ���ڿ��� �����Ѵ�.
     * @param index           ���ۿ� ����� TCPSockClient �ε���
     * @param data            ������ ���ڿ�
     * @exception RemoteException
	  */
	public void send(String data) throws Exception,RemoteException {
		try {
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_DEBUG, "Send:" + data);
			System.out.println("�����غ�.........................................");
			sSock.setNull();
			sSock.init(serverIp,serverPort);
			System.out.println("���� : " + serverIp  + "Port : " + String.valueOf(serverPort) + "==>����Ϸ�.........................................");
			System.out.println("�����غ�.........................................");
			System.out.println(data);
			sSock.send(data);
			System.out.println("���ۿϷ�.........................................");
			sSock.close();
			System.out.println("�������.........................................");
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_DEBUG, "Send Done");
		} catch (Exception e) {
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_CRITICAL, "send()ó���� �������߻��߽��ϴ�.");
			try { sSock.close(); } catch (Exception ee) {}
			throw new Exception("sending : I/O Exception");
		}

		return;
	}
	
	/** DPS�� ���� �����͸� �����Ѵ�.
      * @param index           ���ſ� ����� TCPSockClient �ε���
      * @return String
      * @exception RemoteException
	  */
	public String recv(int index) throws Exception,RemoteException {
		try {
			String sBuf = cSock[index].recv();

			if (sBuf == null) {
				// Ÿ�Ӿƿ����� ���� ��쵵 ������
				try { 
					cSock[index].close(); 
				} catch (Exception ee) {}
				cSock[index] = null;
			}
			
			return sBuf;
		} catch (Exception e) {
			try { 
				cSock[index].close(); 
			} catch (Exception ee) {}
			cSock[index] = null;
			System.out.println("recv()ó���� �������߻��߽��ϴ�.");
			throw new Exception("recv()ó���� �������߻��߽��ϴ�.");
		}
	}

	/** ���ο� ���� �������� ��´�.
      * @return int
      * @exception RemoteException
	  */
	private synchronized int getSeq() {
		seq++;
		return seq;
	}

	/** DPS�� �����͸� �����ϰ� ȸ���� �����Ѵ�.<p>
	  * ���۵����Ϳ� ����Seq�� �ڵ����� �Ҵ��Ѵ�.
      * @param data            ������ ���ڿ�
      * @param return String
      * @exception RemoteException
	  */
	public String callWithoutSeq(String data) throws Exception,RemoteException {
		int seq = getSeq();
		return call(data);
	}
	
	/** 2005.12.14 Ȳ��õ
	  */
	public void callWithoutSeqForPDS(String data) throws Exception,RemoteException {
		callTemp(data);
		return;
	}
	
	public String callWithoutSeqForHome(String data) throws Exception,RemoteException {
		return callHome(data);
	}	

	/** DPS�� �����͸� �����ϰ� ȸ���� �����Ѵ�.
      * @param data            ������ ���ڿ�
      * @return String
      * @exception RemoteException
	  */
	public String call(String data) throws Exception,RemoteException {
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " TCPClientPoolImpl.call Start");

		int id = -1;
		String rData;
		String sSeq;
		long st,et;

		st = System.currentTimeMillis();
		do {
			et = System.currentTimeMillis();
			if ((et - st) > MAX_ALLOC_DELAY) {
				throw new Exception("Error : TCPClientPoolImpl.callTemp===>" + "�Ҵ簡���� ������ �����ϴ�");
			}
			id = alloc();
			delay(50);
		} while (id < 0);
		
		// �ĳ��ý��� ��û���� �ܸ���ȣ ������ ����.
		data = new StringBuffer(data).replace(44,52,FormatData.numToStr((id + 1),8,"0")).toString();
		sSeq = data.substring(9,15);
		
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  "Data : ==>" + data);

		try {
			send(id,data);
		} catch ( Exception e ) {
			free(id);
			throw new Exception(e.toString());
		}

		try {
			rData = recv(id);
		} catch ( Exception e ) {
			free(id);
			throw new Exception("Error : TCPClientPoolImpl.callTemp===>" + e.toString());
		}

		free(id);
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  "===>TCPClientPoolImpl.call Finish");

		return rData;
	}
	
	/** DPS�� �����͸� �����Ѵ�. ftp ���ۿ�
     * @param data            ������ ���ڿ�
     * @return String
     * @exception RemoteException
	  */
	public void callTemp(String data) throws Exception,RemoteException {
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " TCPClientPoolImpl.callTemp Start");

		String sSeq;
		long st,et;

		try {
			send(data);
		} catch ( Exception e ) {
			free(0);
			throw new Exception("Error : TCPClientPoolImpl.callTemp===>" + e.toString());
		}
		
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " TCPClientPoolImpl.callTemp Finish");
		return;
	}
	
	public String callHome(String data) throws Exception,RemoteException {
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " TCPClientPoolImpl.call Start");

		int id = -1;
		String rData;
		String sSeq;
		long st,et;

		st = System.currentTimeMillis();
		do {
			et = System.currentTimeMillis();
			if ((et - st) > MAX_ALLOC_DELAY) {
				throw new Exception("Error : TCPClientPoolImpl.callTemp===>" + "�Ҵ簡���� ������ �����ϴ�");
			}
			id = alloc();
			delay(50);
		} while (id < 0);
		
		try {
			send(id,data);
		} catch ( Exception e ) {
			free(id);
			throw new Exception(e.toString());
		}

		try {
			rData = recv(id);
		} catch ( Exception e ) {
			free(id);
			throw new Exception("Error : TCPClientPoolImpl.callTemp===>" + e.toString());
		}

		free(id);
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  "===>TCPClientPoolImpl.call Finish");

		return rData;

	}

	/** DPS�� �����͸� �����ϰ� ȸ���� �����Ѵ�.<p>
	  * ���ۿ� ����� TCPSockClient�� �̹� �Ҵ���� ���¿��� ����Ѵ�.
      * @param id              ���ۿ� ����� TCPSockClient�� �ε���
      * @param data            ������ ���ڿ�
      * @return String
      * @exception RemoteException
	  */
	public String call(int id, String data) throws Exception,RemoteException {
		String rData;
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " TCPClientPoolImpl.call Start");
		
		try {
			send(id,data);
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
		try {
			rData = recv(id);
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) +  " TCPClientPoolImpl.call Finish");
		return rData;
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

	/** ���� Pool�� ���¸� ���ڿ���̷� ��ȯ�Ѵ�.
      * @return String[]
      * @exception RemoteException
	  */
	public String[] getState() throws RemoteException {
		String[] sState = new String[sCnt];
		for (int i=0;i<sCnt;i++) {
			if (cSock[i] == null) {
				sState[i] = "X";
			} else if (sockAlloc[i]) {
				sState[i] = "U";
			} else {
				sState[i] = "_";
			}
		}
		return sState;
	}


	/** Ŭ���̾�Ʈ�� ������ ������������ �ǴܵǴ� Socket���� �ʱ�ȭ �Ѵ�.
	  */
	private void checkSock() {
		try {
			new CheckSock().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "checkSock()", Log.LOG_CRITICAL, "checkSock()ó���� �������߻��߽��ϴ�.");
		}
	}

	/** ������ ������ ���������� ���������� �ǴܵǴ� Socket�� �ʱ�ȭ�ϱ����� Thread
	  */
	class CheckSock extends Thread {
		public void run() {

			while (true) {
				for (int i=0;i<sCnt;i++) {
					try {
						if (cSock[i] == null) {
							// �������ؾ���	
							System.out.println("ClientSocket Reconnected.......");
							cSock[i] = new TCPSockClient(remoteHost,remotePort);
							sockAlloc[i] = false;
						}
					} catch (Exception e) {
						System.out.println("Socket[" + i + "] reconnect failure......");
						Log.writeLog("Com", "TCPClientPoolImpl", "CheckSock", Log.LOG_CRITICAL, "Socket[" + i + "] reconnect failure......");
						cSock[i] = null;
						break;
					}
				}

				try {
					Thread.sleep(RECONNECT_DELAY);
				} catch (Exception e) {};
			}
		}
	}

	/** ������ ���ֽ�Ű�� ���� Main ���
      * @exception Exception
	  */
	public static void main(String[] args) throws Exception {
		try {
			if( args.length < 1 ) {
				System.out.println("usage: java TCPClientPoolImpl PoolName SocketCnt ServerName PortName");
				System.exit(0);
			}
    		PropertyManager pm = PropertyManager.getInstance();
    
    		String sPoolName = "TCPClientPool";
    		
    		
    		TCPClientPoolImpl sm = new TCPClientPoolImpl();
    		
    		int    sCnt = 3;
    		String sIP = sm.getServerIp();
    		int    sPort = 7;

    		/* Java Application ���� JNDI ����� ���� */
			//java.util.Properties pt = new java.util.Properties();
    		java.util.Hashtable pt = new java.util.Hashtable();
			
    		pt.put(Context.SECURITY_PRINCIPAL, "administrator");
			pt.put(Context.SECURITY_CREDENTIALS, "jeusadmin");
			pt.put(Context.PROVIDER_URL, sm.getWasIp() + ":" + String.valueOf(sm.getWasPort()));
			pt.put(Context.INITIAL_CONTEXT_FACTORY,"jeus.jndi.JEUSContextFactory");
			
			System.out.println("pt ==> " + pt);
    		
			Context ctx = new InitialContext(pt);
    		if (args.length >= 1 ) {
    			sPoolName = args[0]; 
    		}
    		System.out.println("Was Port : " + String.valueOf(sm.getWasPort()) );	
    		
    		ctx.rebind(sPoolName,sm);
    		
    		System.out.println("TCPClientPool Server started...[" + sPoolName + "]");
    		Object obj = ctx.lookup(sPoolName);
    		System.out.println("obj.toString() [" + obj.toString() + "]");	
    		
    		if (args.length == 2) {
    			sCnt = Integer.parseInt(args[1]);
    		} else if (args.length == 3) {
    			sCnt = Integer.parseInt(args[1]);
    			sIP = pm.getStr(args[2] + "_IP");
    			sPort = Integer.parseInt(pm.getStr(args[2] + "_PORT"));
    		} else if (args.length == 4) {
    			sCnt = Integer.parseInt(args[1]);
    			sIP = args[2];
    			sPort = Integer.parseInt(args[3]);
    		} else if (args.length == 5) {
    			sCnt = Integer.parseInt(args[1]);
    			sIP = args[2];
    			sPort = Integer.parseInt(args[3]);
    		}
    		
    		//Client Socket����
    		if( args.length == 4 ){
    			sm.initSock(sCnt,sIP,sPort);
        		System.out.println("TCPClientPool Server Init...[" + sPoolName + "][IP:" + sIP + "][Port:" + sPort + "][Cnt:" + sCnt + "]");
        		Log.writeLog("net", "TCPClientPoolImpl", "main()", Log.LOG_DEBUG, "TCPClientPool�� �⵿�Ǿ����ϴ�.[" + sPoolName + "][IP:" + sIP + "][Port:" + sPort + "][Cnt:" + sCnt + "]");    			
    			sm.checkSock();

    			TCPClient.getInstance();
    			System.out.println("ONLINECLIENT2 �� LOOKUP �Ͽ����ϴ�.");
    			
    		}else if( args.length == 5 ) {
    			sm.initSock(sIP,sPort);
    			System.out.println("�ϰ����� Ŭ���̾�Ʈ ������ ���µǾ����ϴ�.");
    		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}

	}

}