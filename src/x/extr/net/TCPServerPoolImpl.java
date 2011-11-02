/** Main system          : x
  * Sub system           : net
  * Classname            : TCPServerPoolImpl.java
  * Initial date         : 2005.11.15
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DPS와 접속되어있는 TCP Server의 리소스풀
  * Version information  : v 1.0
  *
*/

package x.extr.net;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import javax.naming.*;
import java.io.*;
import x.extr.log.*;
import x.extr.lib.*;
import x.extr.util.*;

/** DPS와 접속되어있는 TCP Server의 리소스풀
  * @author 황재천
*/
public class TCPServerPoolImpl extends UnicastRemoteObject implements TCPServerPool {
	String sSystem = "Com";
	String sClassName = "TCPServerPoolImpl";

	String sPINGReq = "0015PNGS0000000";
	String sPINGAns = "0015PNGR0000000";

	String sOnlineReq = "OLNS";
	String sOnlineAns = "OLNR";
	String sFTPStartReq = "FSTS";
	String sFTPStartAns = "FSTR";
	String sFTPSend = "FSNS";
	String sFTPResendReq = "FRSR";
	String sFTPEndReq = "FEDS";
	String sFTPEndAns = "FEDR";
	String sPingReq = "OLNS";
	String sPingAns = "OLNR";
	String sStatus = "STSS";

	private static int sPort = 7;
	private static int sCnt = 3;
	private static int MAX_STORE_BUFFER = 1000000;
	private static int MAX_READ_BUFFER = 100000;
	private static long MAX_NOACTIVE = 60000;
	private static boolean sockAlloc[] = null;
	private static Socket[] cSock = null;
	private static ServerSocket sSock = null;
	private static long[] lastActiveTime = null; 
	

	/** @ Constructor
	*/
	public TCPServerPoolImpl() throws RemoteException { super(); };
	
	/** ServerSocket을 초기화 한다.
      * @exception RemoteException
	  */
	public void initSock() throws RemoteException {
		try {
			cSock = new Socket[sCnt];
			sockAlloc = new boolean[sCnt];
			lastActiveTime = new long[sCnt];
			for (int i=0;i<sCnt;i++) {
				sockAlloc[i] = false;
			}
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "initSock()", Log.LOG_CRITICAL, "initSock()처리중 오류가발생했습니다.");
			System.out.println(e.toString());
		}
	}

	/** ServerSocket들을 Listen상태로 만든다.
	  */
	private void listenSock() {
		try {
			new ListenSock().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "listenSock()", Log.LOG_CRITICAL, "listenSock()처리중 오류가발생했습니다.");
			System.out.println(e.toString());
		}
	}

	/** 클라이언트와 접속이 끊어진것으로 판단되는 ServerSocket들을 초기화 한다.
	  */
	private void cleanSock() {
		try {
			new CleanSock().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "cleanSock()", Log.LOG_CRITICAL, "cleanSock()처리중 오류가발생했습니다.");
			System.out.println(e.toString());
		}
	}


	/** 서버역할을 수행할 Thread
	  */
	class RunSock extends Thread {
		int index;

		RunSock(int idx) {
			super();
			index = idx;
		}
		RunSock() {
			super();
		}

		public void run() {
			String sData = "";
			int sLen;
			byte[] bData = new byte[MAX_READ_BUFFER];
			byte[] fData = new byte[MAX_READ_BUFFER];
			byte[] storeData = new byte[MAX_STORE_BUFFER];
			int storeIndex = 0;
			InputStream is = null;
			OutputStream os = null;
			DataOutputStream dos = null;
			int cnt;
			Object oa[];
			String sMethod = null;
			String sTemp = null;
			String[] sCall = new String[3];
			String sSeq = "";

			FileOutputStream fos = null;
			int FTPSize = 0;
			int FTPCount = 0;
			int FTPTotalSize = 0;
			long FTPTime = 0;
			String FTPPath = "";

			int readSize;			
			
			SyncCaller sc = new SyncCaller();
			try {
				PropertyManager pm = PropertyManager.getInstance();
				MethodManager mm = MethodManager.getInstance();

				cSock[index].setReceiveBufferSize(100000);
				is = cSock[index].getInputStream();
				os = cSock[index].getOutputStream();
				dos = new DataOutputStream(cSock[index].getOutputStream());

				while (true) {
					cnt  = is.available();					
					if (storeIndex >= 4 || ( cnt > 0 )) {
						lastActiveTime[index] = System.currentTimeMillis();

						if ( cnt > 0 ) {
							if ((MAX_STORE_BUFFER - storeIndex) >= MAX_READ_BUFFER) {
								readSize = is.read(bData,0,MAX_READ_BUFFER);
							} else {
								readSize = is.read(bData,0,MAX_STORE_BUFFER - storeIndex);
							}

              //System.out.println("[RECV]= [" + new String(bData,0,readSize) + "]");

							if ((storeIndex + readSize) > MAX_STORE_BUFFER) {
								Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL, "미처리된 프레임이 버퍼를 초과하였습니다.");
								storeIndex = 0;
								is.skip(is.available());
								continue;
							}

							for (int i=0;i<readSize;i++) {
								storeData[storeIndex + i] = bData[i];
							}
							storeIndex += readSize;
						}
            //System.out.println("004S[RECV]= [" + new String(bData,0,bData.length) + "]");
            //###################################################################################
            // 처리할만한 내용이 쌓였는지 확인
						try {
							if (storeIndex >= 4) {
								sLen = Integer.parseInt(new String(storeData,0,4));
							} else {
								continue;
							}
							
            //System.out.println("sLen:" + sLen);
						} catch (Exception e) {
							System.out.println("TCPServerImpl 198Line...................");
							Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL, "프레임에 오류가 있어 폐기합니다.");
							storeIndex = 0;
							is.skip(is.available());
							continue;
						}
						if ( storeIndex >= sLen ) {
							if (new String(storeData,4,4).equals(sFTPSend)) {
								// FTP를 위한 특수 처리
								sData = new String(storeData,0,15);

								for (int i=15;i<sLen;i++) {
									fData[i - 15] = storeData[i];
								}
								FTPSize = sLen - 15;

								for (int i=sLen;i<storeIndex;i++) {
									storeData[i - sLen] = storeData[i];
								}
								storeIndex -= sLen;
							} else {
								FTPSize = 0;
								sData = new String(storeData,0,sLen);
								for (int i=sLen;i<storeIndex;i++) {
									storeData[i - sLen] = storeData[i];
								}
								storeIndex -= sLen;
							}
//System.out.println("005 sData:" + sData);
//System.out.println("005 sData:" + sData.length());
//if (storeData != null) { System.out.println("storeData:" + new String(storeData,0,storeIndex)); }
						} else {
//System.out.println("continue" + storeData);
							continue;
						}


// 여기서 로직 처리를 해야지.........
						// Online과 FTP, 기타를 구분한다.
						if (sData.substring(4,8).equals(sOnlineReq)) {
							//온라인 전문처리를 해야 합니다.
							oa = new Object[2];
							oa[0] = sData.substring(15);
							sSeq = sData.substring(9,15);

							sMethod = FormatData.strGetReplace(sData.substring(15,34)," ","_");
							//System.out.println("^^^^^^^^^^sDAta" + sData);
							//System.out.println("^^^^^^^^^^sData.length()" + sData.length());
							sTemp = (String)mm.get(sMethod);
							if (sTemp == null){
								System.out.println("-------------------------------------------");
								System.out.println("Can't find Method from Method.property File");
								System.out.println("메소드를 등록하세요........................");
								System.out.println("-------------------------------------------");
								continue;
							}
							//System.out.println("sTemp" + sTemp);
							//System.out.println("sMethod" + sMethod);
							sCall = FormatData.strSplit(sTemp,",");
							//System.out.println("(String)oa[0]:" + (String)oa[0]);
							//System.out.println("sCall[2]:" + sCall[2]);
//for (int j=0;j<3;j++) { System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + "##[" + j + "]" + sCall[j]); }
System.out.println("TCPServerPoolImpl.parseFrae before.............................");
							oa[1] = BaseFrame.parseFrame((String)oa[0],sCall[2]);
							
							//System.out.println("TCPServerPoolImpl.parseFrae afer.............................");

							if ( (sCall[0] != null) && (sCall[1] != null) && (!sCall[0].equals("")) && (!sCall[1].equals("")) ) {
//System.out.println("05S " + new java.sql.Timestamp(System.currentTimeMillis()) + " SyncCall[B]:");
//System.out.println("[00001] ");
								sData = sc.callString(sCall[0],sCall[1],oa);
//System.out.println("006: 272 sData"+ sData + "["+ sData.length() + "]");
								sData = new StringBuffer(sData).replace(9,15,sSeq).toString();
//System.out.println("05E " + new java.sql.Timestamp(System.currentTimeMillis()) + " SyncCall[A]:" + sData);
							} else {
								sData = "등록되지 않은 메소드" + sData;
							}
						} else if (sData.substring(4,8).equals(sFTPStartReq)) {
							// FTP작업을 시작하기 위한 준비를 합니다.
							if (fos != null) {
								Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL,"이미 오픈중인 FTP수신용화일이 종료되지 않았습니다." );
								sData = "0015" + sFTPStartAns + "1" + "000000";
							} else {
								try {
									FTPPath = sData.substring(15);
									FTPTotalSize = 0;
									FTPTime = System.currentTimeMillis();									
									Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_DEBUG,cSock[index].getInetAddress().getHostName() + "로부터 [" + FTPPath + "] 수신시작 요청을 받았습니다." );
									fos = new FileOutputStream(FTPPath);
									sData = "0015" + sFTPStartAns + "0" + sData.substring(9,15);
									FTPCount = 1;
								} catch ( Exception e ) {
									e.printStackTrace();
									Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL,"이미 FTP수신용화일오픈을 실패하였습니다." );
									sData = "0015" + sFTPStartAns + "1" + "000000";
								}
							}
						} else if (sData.substring(4,8).equals(sFTPSend)) {
							// FTP작업위해 받은 데이터를 화일에 저장합니다.
//System.out.println("[FSNS][" + FTPSize + "]" + sData);
							if (Integer.parseInt(sData.substring(9,15)) == FTPCount) {
								fos.write(fData,0,FTPSize);
								FTPCount++;
								FTPTotalSize += FTPSize;
							} else {
System.out.println("잘못된 프레임[FSNS][" + FTPSize + "]" + sData);
							}
continue;
						} else if (sData.substring(4,8).equals(sFTPEndReq)) {
							// FTP작업을 종료처리합니다.
							if (Integer.parseInt(sData.substring(9,15)) == (FTPCount - 1)) {
								sData = "0015" + sFTPEndAns + "0" + sData.substring(9,15);
								fos.close();
								//파일 쓰기가 정상적으로 종료되면 대외기관을 확인하여
								//해당하는 EJB를 호출하도록 한다.
								System.out.println("File Write ended");
								System.out.print("FTPPath:" + FTPPath);
								System.out.println("-------------------------------------------------------------");
								//EJBBroker eb = new EJBBroker();

								//eb.run(FTPPath);

								fos = null;

								FTPTime = System.currentTimeMillis() - FTPTime;

								Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_DEBUG,cSock[index].getInetAddress().getHostName() + "로부터 [" + FTPPath + "] 수신완료 (" + FTPTotalSize + " bytes, " + FTPTime + " ms, " + (FTPTotalSize / FTPTime * 1000) + " byte/s)");

							} else {
								sData = "0015" + sFTPResendReq + "0" + FormatData.numToStr(FTPCount,6,"0");
							}
						} else if (sData.substring(4,8).equals(sPingReq)) {
							// PING회답을 합니다.
continue;
						} else if (sData.substring(4,8).equals(sStatus)) {
							// 나중을 위해 준비된 TrCode입니다.
							// 현재 기능이 없습니다.
continue;
						} else {
System.out.println("[이건뭐지]" + sData);
							Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL, sData.substring(4,8) + "는 알수없는 TrCode입니다.");
							continue;
						}

						os.write(sData.getBytes("EUC_KR"));			

					} else {	
						Thread.sleep(300);
					}
				}
			} catch (Exception e) {
				Log.writeLog(sSystem, sClassName, "RunSock", Log.LOG_CRITICAL, "Socket[" + index + "] Release..." + cSock[index]);
System.out.println("Exception occured at TCPServerPoolImpl.run....");				
				e.printStackTrace();
				
				sockAlloc[index] = false;
				cSock[index] = null;
			}
		}
	}//end of while


	/** 클라이언트와 접속이 끊어진것으로 판단되는 ServerSocket을 초기화하기위한 Thread
	  */
	class CleanSock extends Thread {
		public void run() {
			long now;

			while (true) {
				// 오랫동안 데이터송수신이 없었던 소켓에 대한 정리
				now = System.currentTimeMillis();
				for (int i=0;i<sCnt;i++) {
					if (sockAlloc[i]) {
						if ( (now - lastActiveTime[i]) > MAX_NOACTIVE ) {
              System.out.println("[" + i + "]Socket Maybe disConnect :" + (now - lastActiveTime[i]));					
							Log.writeLog(sSystem, sClassName, "CleanSock", Log.LOG_CRITICAL, "Socket[" + i + "] Release...: Time" + cSock[i]);
							sockAlloc[i] = false;
							cSock[i] = null;
						}
					}
				}
				System.gc();
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				};
			}
		}
	}


	/** 클라이언트의 접속요청을 처리하기위한 Thread
	  */
	class ListenSock extends Thread {
		public void run() {

			try {
				sSock = new ServerSocket(sPort);

				while (true) {
					for (int i=0;i<sCnt;i++) {
						if (sockAlloc[i] == false) {
							System.out.println("[" + i + "]Socket - listen");
							cSock[i] = sSock.accept();
//							cSock[i].setKeepAlive(true);
							System.out.println("[" + i + "]Socket - Accept : " + System.currentTimeMillis() );
							lastActiveTime[i] = System.currentTimeMillis();

							new RunSock(i).start();
							Thread.sleep(500);
							sockAlloc[i] = true;
							break;
						}
					}
//					Thread.sleep(1000);
				}
			} catch ( Exception e ) {
				Log.writeLog(sSystem, sClassName, "listenSock()", Log.LOG_CRITICAL, "initSock()처리중 오류가발생했습니다.");
				e.printStackTrace();
				System.out.println(e.toString());
			}
		}
	}


/*

	private int delay( int msec ) {
		int newSec = msec;
		try {
			Thread.sleep(msec);
			newSec = msec * 2;
			if (newSec > 5000) { newSec = 5000; };
		} catch (Exception e) {	}

		return newSec;
	}
*/

	/** Pool의 현재상태를 리턴한다.
      * @return String[]
      * @exception RemoteException
	  */
	public String[] getState() throws RemoteException {
		String[] sState = new String[sCnt];
		for (int i=0;i<sCnt;i++) {
			if (sockAlloc[i]) {
				sState[i] = "U";
			} else {
				sState[i] = "_";
			}
		}
		return sState;
	}

	/** 서버에 상주시키기 위한 Main 모듈
      * @exception Exception
	  */
	public static void main(String[] args) throws Exception {
		PropertyManager pm = PropertyManager.getInstance();
		String sPoolName = "TCPServerPool";

		TCPServerPoolImpl sm = new TCPServerPoolImpl();

		Context ctx = new InitialContext();
		if (args.length >= 1 ) {
			sPoolName = args[0];
		}
		ctx.rebind(sPoolName,sm);
			
		if (args.length == 2) {
			sCnt = Integer.parseInt(args[1]);
		} else if (args.length == 3) {
			sCnt = Integer.parseInt(args[1]);
			sPort = Integer.parseInt(args[2]);
		}
		//ServerSocket을 초기화 한다.
		sm.initSock();
		System.out.println("DPS Server(OnLine Server) started...[" + sPoolName + "][Port:" + sPort + "][Cnt:" + sCnt + "]");
		//클라이언트와 접속이 끊어진것으로 판단되는 ServerSocket을 초기화하기위한 Thread
		sm.cleanSock();
		//클라이언트의 접속요청을 처리
		sm.listenSock();

	}

}