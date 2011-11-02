/*
 * 대외계 온라인 송신측 데몬이다.
 * 
 * 대외기관과 직접적으로 전문을 주고받는 데몬이다.
 * 클라이언트(화면단)의 요청을 받아 로컬공통헤더를 파싱한다.
 * 파싱된 로컬헤더의 기관정보에 따라 해당기관으로 전송한다.
 * 이 데몬은 한신정 기관에 대해 동기방식으로 작동한다.
 * 동기방식은 요청전문을 기관으로 전송하고 응답전문이 올때까지 소켓은 블록킹 상태에 들어간다.
 * 비동기방식은 요청전문을 기관으로 무조건 전송하고 응답전문은 메시지큐에서 관리번호로 페치한다.
 * 지정된 타임아웃(Time-out) 시간이 경과하면 Time-out Exception 을 발생시키고
 * 요청전문을 전송한 클라이언트에 장애상태를 코드로 리턴한다.
 * 개발서버
 */
package ssvr;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.cabis.xutil.Logger;
import com.cabis.xutil.StringUtil;

import ssvr.SendDaemon.Listener;
import ssvr.SendDaemon.Client;
import ssvr.SendDaemon.Manager;
import util.*;

public class SendDaemon {
	public static String nIP = "";
	public static int nPORT = 0;
	
	public static int sPORT = 0;
	public static boolean SVC_START = false;
	public static boolean SVC_CLOSE = false;
	public Vector   clients;
	public Manager  manager;
	public Listener listener;

	/*
	 * 생성자
	 * Connection 관리자를 생성하고 구동한다.
	 * Listener 를 생성하고 구동한다.
	 */
	public SendDaemon() throws Exception {
		clients = new Vector();
		
		manager = new Manager();
		manager.start();
		
		listener = new Listener();
		listener.start();
	}

	/*
	 * 데몬의 시작 메인함수
	 */
	public static void main(String[] args) throws Exception {
		try{
			SendDaemon.nIP   = args[1];
			SendDaemon.nPORT = Integer.parseInt(args[2]);
			
			SendDaemon.sPORT = Integer.parseInt(args[0]);
			
			SendDaemon server = new SendDaemon();
			System.out.println("=========================================================");
			System.out.println(SendDaemon.nIP + " 서버데몬이 가동되었습니다......");
			System.out.println("=========================================================");
		}catch(IOException ie){
			System.out.println(SendDaemon.nIP + " 데몬을 기동하는중 오류가 발생하여 데몬을 기동할수 없습니다.");
			System.out.println("=========================================================");
			System.out.println(ie.getMessage());
			System.out.println("=========================================================");
			//System.exit(-1);
		}catch(Exception e){
			System.out.println(SendDaemon.nIP + " 데몬을 기동하는중 오류가 발생하여 데몬을 기동할수 없습니다.");
			System.out.println("=========================================================");
			System.out.println(e.getMessage());
			System.out.println("=========================================================");			
			System.out.println(SendDaemon.nIP + " 데몬을 종료하였습니다.");
			System.exit(-1);			
		}
	}

	/**
	 * 신규요청
	 * 소켓접속을 기다린다.
	 * 접속된 소켓은 Connection Manager 에 등록한다.
	 */
	class Listener extends Thread {
		ServerSocket ss;
		public Listener() throws Exception {
			ss = new ServerSocket(SendDaemon.sPORT);
		}

		public void run() {
			for(;;) {
				try {
					Socket con = ss.accept();
					System.out.println("Client Socket Accepted");
					manager.addConnection(con);
				} catch(Exception e) {;}
			}
		}
	}
	
	/**
	 * 요청 관리
	 * 소켓접속을 관리한다.
	 */
	class Manager extends Thread {
		//int[] kcbAlloc = {0,0,0,0,0,0};                       //KCB port allocation check(kkmin,20070423)
		int[] kcbAlloc = {0};
		public void addConnection(Socket con) throws Exception {
			Client client = new Client(con);
			client.start();
			clients.addElement(client);
		}

		//KCB port allocation check(kkmin,20070423)
		public int getKcbPortSeq(){
			int rtnInt = kcbAlloc.length;
			int i = 0;
			for (i=0; i<kcbAlloc.length ; i++){
				if (kcbAlloc[i]==0){
					rtnInt = i;
					kcbAlloc[i] = 1;
					return i;
				}
			}
			rtnInt = i;
			if (rtnInt >= kcbAlloc.length){
				System.out.println("KCB All Port is allocated");
				rtnInt = 0;
				//kcbAlloc[0] = 1;
			}
			return rtnInt;
		}
		
		//KCB port deallocation (kkmin,20070423)
		public void setKcbPortSeq(int seq){
			kcbAlloc[seq] = 0;
		}
		
		public void run() {
			for(;;) {
				try {
					for(int i = 0; i < clients.size(); i++) {
						Thread client = (Thread)clients.elementAt(i);
						if(!client.isAlive()) {
							System.out.println("클라이언트 채널이 종료되었습니다.");
							clients.remove(i);
							break;
						}
					}
					Thread.sleep(1000);
				} catch(Exception e) {
					;
				}
			}
		}
	}

	/*************************************************************************
	 * 
	 * @comment : 클라이언트와 200개의 풀을 맺어둔다.
	 *
	 *************************************************************************/
	class Client extends Thread {
		// 로컬소켓
		Socket lS = null;
		// 외부소켓
		Socket xS = null;
		
		// 로컬소켓 스트림
		BufferedReader in   = null;
		BufferedWriter out  = null;

		// 외부소켓 스트림
		BufferedReader xin  = null;   
		BufferedWriter xout = null;   
		InputStream is  = null;      //(kkmin,20070117)

		private byte[] sToBytes;
		private String IP   = SendDaemon.nIP;
		private int    PORT = SendDaemon.nPORT;
		
		// 로컬 I/O 스트림
		Client(Socket con) throws Exception {
			lS = con;
			in  = new BufferedReader(new InputStreamReader(lS.getInputStream()));
		    out = new BufferedWriter(new OutputStreamWriter(lS.getOutputStream()));			
		}

		public void run() {
			try {
				for(;;) {
					//System.out.println("대외계 전문 요청 처리 시작!!!!!!!!");
					
					// 로컬요청을 받는다.
					//String str = in.readLine();
					
					/*--------------------------------------------------------------*/
					StringBuffer rtnStr = new StringBuffer("");
					int response;
					int i = 0;
					int totCnt = 0;
				
					char[] cTot = new char[10];
				
					if (in.read(cTot) != -1) {
						rtnStr.append(new String(cTot));
						
						totCnt = StringUtil.cInt(StringUtil.CHSTRING(rtnStr.toString().substring(6, 10), " ", "0"));
						
						// 전문길이가 전문 맨앞자리에 포함되므로 이미꺼낸 10 byte 는 빼고 꺼낸다.
						for (i = 0; i < totCnt; i++) {
							response = in.read();
							
							if (response > 255) {
								i++;
							}
	
							rtnStr.append((char) response);
						}
					}
					String str = rtnStr.toString();
					/*--------------------------------------------------------------*/
					
					
					//System.out.println("FW에서 전송된 요청전문 : [" + str + "]");

					// 공통헤더 파싱을 위해 바이트 배열로 변환한다.
					sToBytes = str.getBytes();

					//if( sToBytes.length < 100 ) {
					//	System.out.println("요청한 전문의 길이가 너무 작습니다. 전문은 무시되었습니다.["+str+"]");
					//	continue;
					//}

					// 공통헤더를 파싱한다.
					String[] localRequestHeader = parseOnlineCommonHeader(str);
					
					if(localRequestHeader == null) { 
						System.out.println("localRequestHeader가 null이므로 소켓종료합니다.");
						
						try {
							// 이 소켓들의 상태를 체크하여 널이거나 이용가능상태가 아닐때만 닫을것.!!!!!
							// 내부소켓 종료
							in.close();
							out.close();
							lS.close();
							// 외부소켓 종료
							xin.close();
							xout.close();					
							xS.close();

							break;
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					
					/* 요청기관코드 변환 */
					int orgCode = 00;
					
					// 외부기관에 소켓연결하고 I/O 스트림을 오픈한다.
					String sendError = "0";
					
					try{
						// 신용정보사에 새로운 소켓을 연결한다.
						System.out.println("--------------- 새로운 소켓 연결을 시작합니다.... ------------");
						System.out.println(IP + " : " + String.valueOf(PORT) + " 서버에 연결시도중.....");
						xS   = new Socket(IP,PORT);
						is   = xS.getInputStream();                            //(kkmin,20070117)
						xin  = new BufferedReader(new InputStreamReader(is));  //(kkmin,20070117)
						xout = new BufferedWriter(new OutputStreamWriter(xS.getOutputStream()));
						System.out.println(IP + " : " + String.valueOf(PORT) + " 서버에 연결했습니다.");
					}catch(Exception e){
						e.printStackTrace();
						System.out.println(IP + " : " + String.valueOf(PORT) + " 서버 연결이 실패했습니다.");
						sendError = "9";
					}
					
					/*
					 * 기관별로 공통헤더 엔티티 빈을 통해 헤더를 생성하고
					 * 화면에서 전송한 데이타본문의 앞에 헤더를 붙여 해당기관으로 전송한다
					 */
					
					try{
						System.out.println("===== 신용정보사에 보내는 데이터 =======");
						//System.out.println(localRequestHeader[13]);
						System.out.println("[" + localRequestHeader[2] + "][" + localRequestHeader[2].getBytes().length + "]");
						System.out.println("===============================");
				        //sendMsg(localRequestHeader[13]);
						sendMsg(localRequestHeader[2]);
						
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("전문전송에러");
						sendError = "9";	// 송신불가
					}

					/* 요청기관코드에 따른 수신 */
					String xRcv = "";		// 외부기관에서 받는전문을 저장하는 변수
					String recvError = "0";
					if(!sendError.equals("9")){
						try{
							xRcv   = rcvMsg();
							//xRcv  += rcvMsg();
						}catch(Exception e){
							System.out.println("전문수신에러");
							recvError = "8";	// 수신불가.
						}
					}

					try{
						if(xRcv.getBytes().length == 0){
							System.out.println("원격데몬으로부터 전문응답이 없습니다");
							recvError = "8";	// 수신불가.
						}
					}catch(Exception e){
						System.out.println("바이트 Array 길이체크 오류");
						recvError = "8";		// 수신불가.
					}					
					
					System.out.println("[신용정보사 응답전문--------------------------------------]");
					System.out.println("["+xRcv+"]");
					System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
						
						
					// 기관별 응답전문 공통헤더 파싱함수 호출
					sToBytes = xRcv.getBytes();
					String[] niceHeader = null;
					
					/* 
					 * 로컬 공통부 구성(응답)
					 * 기관별 응답전문의 공통부를 파싱하여 얻은정보로 로컬공통부를 구성한다.
					 */
					String stsCode = "0";
					if( sendError.equals("0")){
						// 송신이 정상이면
						if( recvError.equals("0")){
							// 수신이 정상이면
							stsCode = "0";		// 네트웍 정상으로 판단
						}else{
							if( recvError.equals("8") ){
								stsCode = "8";	// 수신오류로 판단
							}else{
								stsCode = "7";	// 비정상수신으로 판단
							}
						}
					}else{
						// 송신이 오류이면
						stsCode = "9";			// 송신오류로 판단(네트웍 연결오류)
					}
					
					
					// 로컬응답을 리턴한다.
					try{
						//out.write(localResponseHeader);
						out.write(xRcv);
						out.flush();
					}catch(Exception e){
						e.printStackTrace();
					}
					
					try{
						xin.close();
						xout.close();							
						xS.close();				
					}catch(Exception e){
							System.out.println("신용정보사 전문 송수신후 소켓종료중 오류:"+e.getMessage());
					}						
				}

			} catch(Exception e) {
				e.printStackTrace();
				try {
					// 이 소켓들의 상태를 체크하여 널이거나 이용가능상태가 아닐때만 닫을것.!!!!!
					// 내부소켓 종료
					in.close();
					out.close();
					lS.close();
					// 외부소켓 종료
					xin.close();
					xout.close();					
					xS.close();
					System.out.println("통신 또는 전문파싱중 오류가 발생하여 소켓접속을 종료하였습니다");
				} catch (Exception ex) {
					System.out.println("통신 또는 전문파싱중 오류가 발생하여 소켓접속 종료중 오류발생함"+ex.getMessage());
				}
			} finally {

			}

		}

		/**
		 * 외부기관에 요청한다.한신정,한신평
		 * @param String to set
		 */
		public synchronized void sendMsg(String command) throws IOException {
			try {
				//System.out.println("========== 버퍼 Write Start ==================================");
				xout.write(command);
				//System.out.println("========== 버퍼 Write End  ===================================");
				xout.flush();
				//System.out.println("========== 버퍼 frush End  ===================================");
			} catch (IOException e) {
				System.out.println("========== sendMsg 함수 데이더 보내는중 IOException 발생 ==========");
				System.out.println(e.getMessage());
				System.out.println("=============================================================");
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				System.out.println("========== sendMsg 함수 데이더 보내는중 Exception 발생 ============");
				System.out.println(e.getMessage());
				System.out.println("=============================================================");
				throw new IOException(e.getMessage());
			} finally {
			}
		}
		
		/**
		 * 외부기관 응답을 받아들인다.
		 * @return a String
		 */
		public synchronized String rcvMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");

			try {
				int gramLength = 0;

				if (SendDaemon.nPORT == 28099 || SendDaemon.nPORT == 28100) { // 1F005 전문은 전문길이가 10바이트로 온다.
					gramLength = 10;
				} else {                         // 그이외에는 전문길이가 4바이트로 온다.
					gramLength = 4;
				}
				System.out.println(" SendDaemon.nPORT  : " + SendDaemon.nPORT + " , gramLength : " + gramLength);

				int response;
				int i = 0;
				int totCnt = 0;
				//char[] cTot = new char[4];
				char[] cTot = new char[ gramLength ];

				if (xin.read(cTot) != -1) {
					rtnStr.append(new String(cTot));
					totCnt = StringUtil.cInt(StringUtil.CHSTRING(rtnStr.toString(), " ", "0"));
					System.out.println(" totCnt : " + totCnt);

					if(nPORT == 30032 || nPORT == 28099 || nPORT == 30022 || nPORT == 28100) gramLength = 0;
					//if(nPORT == 30032 ) gramLength = 0;
					
					//for (i = 0; i < totCnt-4; i++) {
					for (i = 0; i < totCnt-gramLength; i++) {
					//for (i = 0; i < totCnt; i++) {
						response = xin.read();

						if (response > 255) {
							i++;
						}

						rtnStr.append((char) response);
					}
				}
				//System.out.println("======== rcvMsg() 신용정보사에서 받은 전문 ===================");
				//System.out.println("[" + rtnStr.toString() + "]");
				//System.out.println("============================================");
				return rtnStr.toString();

			} catch (java.net.SocketException se) {
				throw new IOException(se.getMessage());
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {

			}
		}
		/*--------------------------------------------------------------------------------------------*/

		/*
		 * 로컬공통헤더 파싱
		 * @param String 로컬공통헤더
		 * @return String[] 로컬공통헤더 항목배열
		 * Comment 화면에서 작성해 넘어온 로컬헤더+본문전문을 파시하여 스트링배열로 리턴한다. 
		 */
		public String[] parseOnlineCommonHeader(String req) throws Exception {
			if(req == null || req.length() == 0) return null;
			
			String[] attr = new String[3];

			try{
				attr[0] = subString(0, 6);		          // 전문번호
				attr[1] = subString(6, 10);               // 전문길이
				attr[2] = subString(10, sToBytes.length); // 송신할 전
				
				System.out.println("헤더와 본문을 분리완료했습니다.");
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}
			
			return attr;
		}
		
		/**
		 * 문자열 바이트로 계산 - Exception 에러 발생
		 * @return a String - 요청한 바이트 사이의 문자열
		 * @param int to Set - 시작 바이트
		 * @param int to Set - 마지막 바이트
		*/
		private String subString(int start, int end) throws Exception {
			String strRtn = "";
			byte[]   bOut = new byte[end - start];

			try {
				for (int i = start; i < end; i++) {
					bOut[i - start] = sToBytes[i];
				}

				strRtn = new String(bOut);

				return strRtn;
			} catch (NullPointerException ne) {
				throw new Exception("subString() NullPointerException : " + ne.getMessage());
			} catch (Exception e) {
				throw new Exception("subString() Exception : " + e.getMessage());
			}
		}
		
	    /**
	     * @param totLen 
	     */
	    private String getStrLength(int totLen) {
	    	String strTotLen = Integer.toString(totLen);
	    	int nCnt = strTotLen.length();
	        for (int i = 10; i > nCnt; i--) {
	        	strTotLen = "0" + strTotLen;
	        }
	        
	        return strTotLen;
	    }		

	    private String fillPkString(String str, int pkLen) {
	        if (str == null) {
	            return "";
	        }

	        if (str.length() >= pkLen) {
	            return str;
	        }

	        String temp = "";

	        for (int i = 0; i < (pkLen - str.length()); i++) {
	            temp += " ";
	        }

	        return str + temp;
	    }

	    private String fillPkNumber(String str, int pkLen) {  //KCB 추가(kkmin,20070110)
	        if (str == null) {
	            return "";
	        }

	        if (str.length() >= pkLen) {
	            return str;
	        }

	        String temp = "";

	        for (int i = 0; i < (pkLen - str.length()); i++) {
	            temp += "0";
	        }

	        return temp + str;
	    }

	}
	
}