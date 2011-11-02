/** Main system          : x
  * Sub system           : net
  * Classname            : TCPServerPoolImpl.java
  * Initial date         : 2005.11.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : DPS�� ���ӵǾ��ִ� TCP Server�� ���ҽ�Ǯ
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

/** DPS�� ���ӵǾ��ִ� TCP Server�� ���ҽ�Ǯ
  * @author Ȳ��õ
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
	
	/** ServerSocket�� �ʱ�ȭ �Ѵ�.
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
			Log.writeLog(sSystem, sClassName, "initSock()", Log.LOG_CRITICAL, "initSock()ó���� �������߻��߽��ϴ�.");
			System.out.println(e.toString());
		}
	}

	/** ServerSocket���� Listen���·� �����.
	  */
	private void listenSock() {
		try {
			new ListenSock().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "listenSock()", Log.LOG_CRITICAL, "listenSock()ó���� �������߻��߽��ϴ�.");
			System.out.println(e.toString());
		}
	}

	/** Ŭ���̾�Ʈ�� ������ ������������ �ǴܵǴ� ServerSocket���� �ʱ�ȭ �Ѵ�.
	  */
	private void cleanSock() {
		try {
			new CleanSock().start();
		} catch ( Exception e ) {
			Log.writeLog(sSystem, sClassName, "cleanSock()", Log.LOG_CRITICAL, "cleanSock()ó���� �������߻��߽��ϴ�.");
			System.out.println(e.toString());
		}
	}


	/** ���������� ������ Thread
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
								Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL, "��ó���� �������� ���۸� �ʰ��Ͽ����ϴ�.");
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
            // ó���Ҹ��� ������ �׿����� Ȯ��
						try {
							if (storeIndex >= 4) {
								sLen = Integer.parseInt(new String(storeData,0,4));
							} else {
								continue;
							}
							
            //System.out.println("sLen:" + sLen);
						} catch (Exception e) {
							System.out.println("TCPServerImpl 198Line...................");
							Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL, "�����ӿ� ������ �־� ����մϴ�.");
							storeIndex = 0;
							is.skip(is.available());
							continue;
						}
						if ( storeIndex >= sLen ) {
							if (new String(storeData,4,4).equals(sFTPSend)) {
								// FTP�� ���� Ư�� ó��
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


// ���⼭ ���� ó���� �ؾ���.........
						// Online�� FTP, ��Ÿ�� �����Ѵ�.
						if (sData.substring(4,8).equals(sOnlineReq)) {
							//�¶��� ����ó���� �ؾ� �մϴ�.
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
								System.out.println("�޼ҵ带 ����ϼ���........................");
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
								sData = "��ϵ��� ���� �޼ҵ�" + sData;
							}
						} else if (sData.substring(4,8).equals(sFTPStartReq)) {
							// FTP�۾��� �����ϱ� ���� �غ� �մϴ�.
							if (fos != null) {
								Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL,"�̹� �������� FTP���ſ�ȭ���� ������� �ʾҽ��ϴ�." );
								sData = "0015" + sFTPStartAns + "1" + "000000";
							} else {
								try {
									FTPPath = sData.substring(15);
									FTPTotalSize = 0;
									FTPTime = System.currentTimeMillis();									
									Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_DEBUG,cSock[index].getInetAddress().getHostName() + "�κ��� [" + FTPPath + "] ���Ž��� ��û�� �޾ҽ��ϴ�." );
									fos = new FileOutputStream(FTPPath);
									sData = "0015" + sFTPStartAns + "0" + sData.substring(9,15);
									FTPCount = 1;
								} catch ( Exception e ) {
									e.printStackTrace();
									Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL,"�̹� FTP���ſ�ȭ�Ͽ����� �����Ͽ����ϴ�." );
									sData = "0015" + sFTPStartAns + "1" + "000000";
								}
							}
						} else if (sData.substring(4,8).equals(sFTPSend)) {
							// FTP�۾����� ���� �����͸� ȭ�Ͽ� �����մϴ�.
//System.out.println("[FSNS][" + FTPSize + "]" + sData);
							if (Integer.parseInt(sData.substring(9,15)) == FTPCount) {
								fos.write(fData,0,FTPSize);
								FTPCount++;
								FTPTotalSize += FTPSize;
							} else {
System.out.println("�߸��� ������[FSNS][" + FTPSize + "]" + sData);
							}
continue;
						} else if (sData.substring(4,8).equals(sFTPEndReq)) {
							// FTP�۾��� ����ó���մϴ�.
							if (Integer.parseInt(sData.substring(9,15)) == (FTPCount - 1)) {
								sData = "0015" + sFTPEndAns + "0" + sData.substring(9,15);
								fos.close();
								//���� ���Ⱑ ���������� ����Ǹ� ��ܱ���� Ȯ���Ͽ�
								//�ش��ϴ� EJB�� ȣ���ϵ��� �Ѵ�.
								System.out.println("File Write ended");
								System.out.print("FTPPath:" + FTPPath);
								System.out.println("-------------------------------------------------------------");
								//EJBBroker eb = new EJBBroker();

								//eb.run(FTPPath);

								fos = null;

								FTPTime = System.currentTimeMillis() - FTPTime;

								Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_DEBUG,cSock[index].getInetAddress().getHostName() + "�κ��� [" + FTPPath + "] ���ſϷ� (" + FTPTotalSize + " bytes, " + FTPTime + " ms, " + (FTPTotalSize / FTPTime * 1000) + " byte/s)");

							} else {
								sData = "0015" + sFTPResendReq + "0" + FormatData.numToStr(FTPCount,6,"0");
							}
						} else if (sData.substring(4,8).equals(sPingReq)) {
							// PINGȸ���� �մϴ�.
continue;
						} else if (sData.substring(4,8).equals(sStatus)) {
							// ������ ���� �غ�� TrCode�Դϴ�.
							// ���� ����� �����ϴ�.
continue;
						} else {
System.out.println("[�̰ǹ���]" + sData);
							Log.writeLog(sSystem, sClassName, "RunSock.run()", Log.LOG_CRITICAL, sData.substring(4,8) + "�� �˼����� TrCode�Դϴ�.");
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


	/** Ŭ���̾�Ʈ�� ������ ������������ �ǴܵǴ� ServerSocket�� �ʱ�ȭ�ϱ����� Thread
	  */
	class CleanSock extends Thread {
		public void run() {
			long now;

			while (true) {
				// �������� �����ͼۼ����� ������ ���Ͽ� ���� ����
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


	/** Ŭ���̾�Ʈ�� ���ӿ�û�� ó���ϱ����� Thread
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
				Log.writeLog(sSystem, sClassName, "listenSock()", Log.LOG_CRITICAL, "initSock()ó���� �������߻��߽��ϴ�.");
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

	/** Pool�� ������¸� �����Ѵ�.
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

	/** ������ ���ֽ�Ű�� ���� Main ���
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
		//ServerSocket�� �ʱ�ȭ �Ѵ�.
		sm.initSock();
		System.out.println("DPS Server(OnLine Server) started...[" + sPoolName + "][Port:" + sPort + "][Cnt:" + sCnt + "]");
		//Ŭ���̾�Ʈ�� ������ ������������ �ǴܵǴ� ServerSocket�� �ʱ�ȭ�ϱ����� Thread
		sm.cleanSock();
		//Ŭ���̾�Ʈ�� ���ӿ�û�� ó��
		sm.listenSock();

	}

}