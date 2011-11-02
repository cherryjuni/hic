/** Main system          : x
  * Sub system           : net
  * Classname            : TCPClientPoolImpl.java
  * Initial date         : 2005.11.16
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DPS와 접속되어있는 TCP Client의 리소스풀
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

/** DPS와 접속되어있는 TCP Client의 리소스풀
  * @author 황재천
*/
public class TCPClientPoolImpl extends UnicastRemoteObject implements TCPClientPool {
	String sSystem = "net";
	String sClassName = "TCPClientPoolImpl";
	
	private final int RECONNECT_DELAY = 5000;
	private final int MAX_ALLOC_DELAY = 5000;
	private int seq = 0;
	private int sCnt = -1;
	private boolean sockAlloc[] = null;
	private TCPSockClient[] cSock = null;// 온라인 전문용 소켓관리
	private TCPSockClient   sSock = null;// 일괄전송 패킷용 소켓관리
	private String 	remoteHost = "";
	private int 	remotePort = 0;
	//private String serverIp = "152.149.42.2";	//온라인 전송을 할 SenderR 데몬의 IP
	private String serverIp = "192.168.50.11";	//온라인 전송을 할 SenderR 데몬의 IP
	//private int serverPort = 60007;	//온라인 전송을 할 SenderR 데몬의 IP
	private int serverPort = 9002;	//온라인 전송을 할 SenderR 데몬의 IP
	//private String wasIp = "152.149.42.2";		//WAS RMI 서비스를 위한 WAS IP
	private String wasIp = "192.168.50.11";		//WAS RMI 서비스를 위한 WAS IP
	//private int wasPort = 9756;		//WAS RMI를 위한 포트 
	private int wasPort = 9736;		//WAS RMI를 위한 포트
	
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

	/** 생성자
      */
	public TCPClientPoolImpl() throws RemoteException { 
		super();
	};
	
	/** DPS서버와 접속할 TCPSockClient들을 생성한다.(온라인 소켓 생성)
      * @param cnt              생성할 TCPSockClient의 갯수
      * @param host             TCPSockClient가 접속할 IP Address
      * @param port             TCPSockClient가 접속할 Port
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
				PDS의 Locking 메카니즘으로 인하여 0.5의 delay를 가지고 
				연결한다.
				*/
				Thread.sleep(100);				
			}
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "initSock()", Log.LOG_CRITICAL, "initSock()처리중 오류가발생했습니다. : " + e.toString());
		}
	}

	/** DPS서버와 접속할 TCPSockClient들을 생성한다.(일괄전송 소켓생성)
     * @param cnt              생성할 TCPSockClient의 갯수
     * @param host             TCPSockClient가 접속할 IP Address
     * @param port             TCPSockClient가 접속할 Port
     * @exception RemoteException
	  */
	public void initSock(String host,int port) throws RemoteException {
		try {
			// 일괄전송용 소켓처리 모듈 생성
			sSock = new TCPSockClient();			
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "initSock()", Log.LOG_CRITICAL, "initSock()처리중 오류가발생했습니다. : " + e.toString());
		}
	}
	
	/** TCPSockClient들을 접속종료한다.
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
			Log.writeLog(sSystem, sClassName, "closeSock()", Log.LOG_CRITICAL, "closeSock()처리중 오류가발생했습니다.");
			return false;
		}
	}

	/** 사용가능한 TCPSockClient를 할당한다.
      * @return int
      * @exception RemoteException
	  */
	public synchronized int alloc() throws RemoteException {
		for (int i=0;i<sCnt;i++) {
			if ((cSock[i] != null) && (!sockAlloc[i])) {
				sockAlloc[i] = true;
				System.out.println("["+i+"]번소켓 할당");
				return i;
			}
		}
		return -1;
	}

	/** 더이상 사용하지 않는 TCPSockClient를 Pool에 반환한다.
      * @param int           반환할 TCPSockClient 인덱스
      * @return boolean
      * @exception RemoteException
	  */
	public synchronized boolean free(int index) throws RemoteException {
		sockAlloc[index] = false;
		System.out.println("["+index+"]번소켓 해제");
		return true;
	}

	/** 현재 Pool의 상태를 문자열로 리턴한다.
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

	/** DPS로 파일을 전송한다.
      * @param index           전송에 사용할 TCPSockClient 인덱스
      * @param filename        전송할 파일명
      * @param startCount      전송시작 카운트
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

			Log.writeLog(sSystem, sClassName, "sendFTP", Log.LOG_DEBUG,"[" + fileName + "] 전송시작");
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

			Log.writeLog(sSystem, sClassName, "sendFTP", Log.LOG_DEBUG,"[" + fileName + "] 전송완료 (" + totalSize + " bytes)");

		} catch (Exception e) {
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_CRITICAL, "send()처리중 오류가발생했습니다.");
			return 0;
		}

		return count;
	}


	/** DPS로 문자열과 Byte[]타입의 데이터를 연속해서 전송한다.
      * @param index           전송에 사용할 TCPSockClient 인덱스
      * @param data            전송할 문자열
      * @param binaryData      전송할 Byte데이터
      * @param offset          전송할 시작할 Byte의 offset
      * @param len             전송할 Byte의 길이
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
	
	/** DPS로 문자열을 전송한다.
      * @param index           전송에 사용할 TCPSockClient 인덱스
      * @param data            전송할 문자열
      * @exception RemoteException
	  */
	public void send(int index,String data) throws Exception,RemoteException {
		try {
			cSock[index].send(data);
			System.out.println("전송완료.........................................");
		} catch (Exception e) {
			try { 
				cSock[index].close(); 
			} catch (Exception ee) {}
			
			cSock[index] = null;
			System.out.println("send()처리중 오류가발생했습니다.");
			throw new Exception("send()처리중 오류가발생했습니다.");
		}

		return;
	}
	
	/** DPS로 문자열을 전송한다.
     * @param index           전송에 사용할 TCPSockClient 인덱스
     * @param data            전송할 문자열
     * @exception RemoteException
	  */
	public void send(String data) throws Exception,RemoteException {
		try {
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_DEBUG, "Send:" + data);
			System.out.println("연결준비.........................................");
			sSock.setNull();
			sSock.init(serverIp,serverPort);
			System.out.println("서버 : " + serverIp  + "Port : " + String.valueOf(serverPort) + "==>연결완료.........................................");
			System.out.println("전송준비.........................................");
			System.out.println(data);
			sSock.send(data);
			System.out.println("전송완료.........................................");
			sSock.close();
			System.out.println("연결닫힘.........................................");
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_DEBUG, "Send Done");
		} catch (Exception e) {
			Log.writeLog(sSystem, sClassName, "send()", Log.LOG_CRITICAL, "send()처리중 오류가발생했습니다.");
			try { sSock.close(); } catch (Exception ee) {}
			throw new Exception("sending : I/O Exception");
		}

		return;
	}
	
	/** DPS로 부터 데이터를 수신한다.
      * @param index           수신에 사용할 TCPSockClient 인덱스
      * @return String
      * @exception RemoteException
	  */
	public String recv(int index) throws Exception,RemoteException {
		try {
			String sBuf = cSock[index].recv();

			if (sBuf == null) {
				// 타임아웃으로 인한 경우도 재접속
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
			System.out.println("recv()처리중 오류가발생했습니다.");
			throw new Exception("recv()처리중 오류가발생했습니다.");
		}
	}

	/** 새로운 전송 시퀀스를 얻는다.
      * @return int
      * @exception RemoteException
	  */
	private synchronized int getSeq() {
		seq++;
		return seq;
	}

	/** DPS로 데이터를 전송하고 회답을 수신한다.<p>
	  * 전송데이터에 전송Seq를 자동으로 할당한다.
      * @param data            전송할 문자열
      * @param return String
      * @exception RemoteException
	  */
	public String callWithoutSeq(String data) throws Exception,RemoteException {
		int seq = getSeq();
		return call(data);
	}
	
	/** 2005.12.14 황재천
	  */
	public void callWithoutSeqForPDS(String data) throws Exception,RemoteException {
		callTemp(data);
		return;
	}
	
	public String callWithoutSeqForHome(String data) throws Exception,RemoteException {
		return callHome(data);
	}	

	/** DPS로 데이터를 전송하고 회답을 수신한다.
      * @param data            전송할 문자열
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
				throw new Exception("Error : TCPClientPoolImpl.callTemp===>" + "할당가능한 소켓이 없습니다");
			}
			id = alloc();
			delay(50);
		} while (id < 0);
		
		// 파나시스의 요청으로 단말번호 강제로 설정.
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
	
	/** DPS로 데이터를 전송한다. ftp 전송용
     * @param data            전송할 문자열
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
				throw new Exception("Error : TCPClientPoolImpl.callTemp===>" + "할당가능한 소켓이 없습니다");
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

	/** DPS로 데이터를 전송하고 회답을 수신한다.<p>
	  * 전송에 사용할 TCPSockClient를 이미 할당받은 상태에서 사용한다.
      * @param id              전송에 사용할 TCPSockClient의 인덱스
      * @param data            전송할 문자열
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

	/** 지능적인 delay처리를 위한 메소드
      * @param msec              대기할 시간(milli second)
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

	/** 현재 Pool의 상태를 문자열어레이로 반환한다.
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


	/** 클라이언트와 접속이 끊어진것으로 판단되는 Socket들을 초기화 한다.
	  */
	private void checkSock() {
		try {
			new CheckSock().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "checkSock()", Log.LOG_CRITICAL, "checkSock()처리중 오류가발생했습니다.");
		}
	}

	/** 서버와 접속이 정상적이지 않은것으로 판단되는 Socket을 초기화하기위한 Thread
	  */
	class CheckSock extends Thread {
		public void run() {

			while (true) {
				for (int i=0;i<sCnt;i++) {
					try {
						if (cSock[i] == null) {
							// 재접속해야지	
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

	/** 서버에 상주시키기 위한 Main 모듈
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

    		/* Java Application 에서 JNDI 사용자 인증 */
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
    		
    		//Client Socket생성
    		if( args.length == 4 ){
    			sm.initSock(sCnt,sIP,sPort);
        		System.out.println("TCPClientPool Server Init...[" + sPoolName + "][IP:" + sIP + "][Port:" + sPort + "][Cnt:" + sCnt + "]");
        		Log.writeLog("net", "TCPClientPoolImpl", "main()", Log.LOG_DEBUG, "TCPClientPool이 기동되었습니다.[" + sPoolName + "][IP:" + sIP + "][Port:" + sPort + "][Cnt:" + sCnt + "]");    			
    			sm.checkSock();

    			TCPClient.getInstance();
    			System.out.println("ONLINECLIENT2 를 LOOKUP 하였읍니다.");
    			
    		}else if( args.length == 5 ) {
    			sm.initSock(sIP,sPort);
    			System.out.println("일괄전송 클라이언트 소켓이 오픈되었습니다.");
    		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}

	}

}