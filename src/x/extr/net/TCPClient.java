/** Main system          : x
  * Sub system           : net
  * Classname            : TCPClient.java
  * Initial date         : 2005.11.14
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DPS을 이용하기 위한 TCP Client의 기본 Class
  */

package x.extr.net;

import javax.naming.*;

import x.extr.log.*;
import x.extr.util.*;

/** DPS을 이용하기 위한 TCP Client의 기본 Class
  * @author 황재천
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

	/** 생성자
      */
	private TCPClient(){}

	/** 기존에 생성되어있던 Instance를 반환
	  * @return TCPClient
      */
	public static TCPClient getInstance() {
		System.out.println("02S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.getInstance Start");

		if (m_instance == null) {
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "TCPClient 인스턴스를 새로만든다.");
			m_instance = new TCPClient();					// 객체를 생성한다
			TCPClient.init();
		} else {
			System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "TCPClient 인스턴스를 그냥쓴다.");
		}
		/* 2006.04.19 추가 */
		System.setProperty("java.naming.factory.initial","jeus.jndi.JNSContextFactory");
		
		System.out.println("02E " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.getInstance Finish");
		return m_instance;
	}


	/** Instance를 초기화
      */
	public static void init() {
		/*
		 * 인스턴스가 존재하지않으면 생성
		 * 존재 한다면 다음단계로의 진행
		 */
		if (m_instance == null) {
			m_instance = new TCPClient();
		}

		try {
    		/* Java Application 에서 JNDI 사용자 인증 
    		 * 
    		 * 어플리케이션에서 이 모듈을 통해 JNDI에 접속하기위해서는 JNDI의 인증이 필요하다.
    		 * 인증방법은 아이디와 암호이며 프로퍼티를 사용하여 세팅한다.
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
			 * PDS 정확히 170 byte 를 전달하는것으로 약속. 개행문자(\n)로 
			 * 문자열을 끝을 알리는 방법을 사용하지 아니함
			 */
			sm.callWithoutSeqForPDS(packet); 
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}
		
		System.out.println("03S " + new java.sql.Timestamp(System.currentTimeMillis()) + " TCPClient.callPDS Finish");
		
		return;
	}

	/** DPS로 Online프레임을 전송하고 회답을 수신한다.
      * @param of                  전송할 온라인프레임
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

	/** DPS로 FTP프레임을 전송한다.
      * @param ff                     전송할 FTP프레임
	  * @return boolean
      */
	public int callFTP ( FTPFrame ff ) {
		return callFTP(ff,"FTPCLIENT");
	}

	/** DPS로 FTP프레임을 전송한다.
      * @param ff                     전송할 FTP프레임
      * @param poolName               전송에 이용할 Pool명
	  * @return int(01 = 성공, 02 = 수신측에서 FTP전송시작을 거부, 03 = 알수없는 프레임 오류, 04 = 존재하지 않는 파일, 05 = 알 수 없는 오류)
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
			
			System.out.println("[" + poolId + "]를 할당받았습니다.");

			while (true) {
				
				if (mode == MODE_START) {

					sTemp = "0015FSTS0001000" + ff.toPath;
					sLen = sTemp.getBytes().length;
					sTemp = new StringBuffer(sTemp).replace(0,4,FormatData.numToStr(sLen,4,"0")).toString();

					sBuf = sm.call(poolId,sTemp); //단순스트링을 보내고 즉시 응답을 받는다.
					if (sBuf.substring(4,9).equals("FSTR0")) {
						mode = MODE_SENDING;
						System.out.println("Send[" + sTemp + "] Recv[" + sBuf + "] 신나게 보내보자.");

						continue;
					} else if (sBuf.substring(4,9).equals("FSTR1")) {
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"수신측에서 FTP전송시작을 거부했습니다.");
						iRet = 2;
						break;
					} else {
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"[" + sBuf + "]는 알수없는 프레임입니다.");
						iRet = 3;
						break;
					}
				} else if (mode == MODE_SENDING) {
					//신나게 보내보자.(파일의 내용을 모두 전송한다)
					count = sm.sendFTP(poolId,ff.fromPath,sCount);
					mode = MODE_CHECK;
					continue;
				} else if (mode == MODE_CHECK) {
					// 단순스트링을 보내고 즉시 응답을 받는다.
					sBuf = sm.call(poolId,"0015FEDS0" + FormatData.numToStr(count,6,"0"));
					if (sBuf.substring(4,9).equals("FEDR0")) {
					/*
					    수정내용: 전송한 파일의 크기가 0인 겨우 Message로 알려준다.
					 */
						System.out.println("Send[0015FSTS0001000] Recv[" + sBuf + "] FTP전송완료.");
						if (count == 0){
							iRet = 4;
							Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"전송을 시도한 파일의 크기가 0입니다. ");
						}else{
							iRet = 1;
						}						
						break;
					} else if (sBuf.substring(4,9).equals("FEDR1")) {
						// 이런 경우는 없다.
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"[" + sBuf + "]는 알수없는 프레임입니다.");
						iRet = 3;
						break;
					} else if (sBuf.substring(4,8).equals("FRSR")) {
						sCount = Integer.parseInt(sBuf.substring(9,15));
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"수신측에서 [" + sCount + "]프레임부터 FTP재전송을 요구했습니다.");
						mode = MODE_SENDING;
						continue;
					} else {
						Log.writeLog(sSystem, sClassName, "callFTP()", Log.LOG_CRITICAL,"[" + sBuf + "]는 알수없는 프레임입니다.");
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