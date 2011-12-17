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
/*
 * 서버에서만 실행된다.
 * 방화벽과 로컬네트워크 아이피를 사용하기 때문..
 * 아래의 아규멘트를 넣고 실행한다
 * 
 * 서버에서만 KIBNET과 연결된다.
 * 로컬에서는 실행은 되지만, KIBNET과 연결될 수 없다 
 * 로컬에서 테스트하고,
 * 실제 KIBNET연결이 되지 않을때, 테스트하기 위한 프로그램
 * 연결되고 KIBNET으로 연결은되지 않지만
 * KIBNET연결을 시뮬레이션 한다.
 * 아래의 아규멘트를 넣고 실행한다
 * 
 * arg -> 9230 127.0.0.1 50811
 * 
 */

package ssvr;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.cabis.xutil.Logger;
import com.cabis.xutil.StringUtil;

import rsvr.IKibGramEnum;
import rsvr.KibGram;
import ssvr.SendDaemonLocalReturn.Listener;
import ssvr.SendDaemonLocalReturn.Client;
import ssvr.SendDaemonLocalReturn.Manager;
import util.*;

public class SendDaemonLocalReturn {
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
	public SendDaemonLocalReturn() throws Exception {
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
			SendDaemonLocalReturn.nIP   = args[1];
			SendDaemonLocalReturn.nPORT = Integer.parseInt(args[2]);
			
			SendDaemonLocalReturn.sPORT = Integer.parseInt(args[0]);
			
			SendDaemonLocalReturn server = new SendDaemonLocalReturn();
			System.out.println("=========================================================");
			System.out.println(SendDaemonLocalReturn.nIP + " 서버데몬이 가동되었습니다......");
			System.out.println("=========================================================");
		}catch(IOException ie){
			System.out.println(SendDaemonLocalReturn.nIP + " 데몬을 기동하는중 오류가 발생하여 데몬을 기동할수 없습니다.");
			System.out.println("=========================================================");
			System.out.println(ie.getMessage());
			System.out.println("=========================================================");
			//System.exit(-1);
		}catch(Exception e){
			System.out.println(SendDaemonLocalReturn.nIP + " 데몬을 기동하는중 오류가 발생하여 데몬을 기동할수 없습니다.");
			System.out.println("=========================================================");
			System.out.println(e.getMessage());
			System.out.println("=========================================================");			
			System.out.println(SendDaemonLocalReturn.nIP + " 데몬을 종료하였습니다.");
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
			ss = new ServerSocket(SendDaemonLocalReturn.sPORT);
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
		private Socket lS = null;
		// 외부소켓
		private Socket xS = null;
		
		// 로컬소켓 스트림
		private BufferedReader in   = null;
		private BufferedWriter out  = null;

		// 외부소켓 스트림
		private BufferedReader xin  = null;   
		private BufferedWriter xout = null;   
		private InputStream is  = null;      //(kkmin,20070117)

		private byte[] sToBytes;
		private String IP   = SendDaemonLocalReturn.nIP;
		private int    PORT = SendDaemonLocalReturn.nPORT;
		
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
						totCnt -= 4;
						
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
//							xin.close();
//							xout.close();					
//							xS.close();

							break;
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
//					
//					/* 요청기관코드 변환 */
//					int orgCode = 00;
//					
//					// 외부기관에 소켓연결하고 I/O 스트림을 오픈한다.
//					String sendError = "0";
//					
//					try{
//						// 신용정보사에 새로운 소켓을 연결한다.
//						System.out.println("--------------- 새로운 소켓 연결을 시작합니다.... ------------");
//						System.out.println(IP + " : " + String.valueOf(PORT) + " 서버에 연결시도중.....");
//						xS   = new Socket(IP,PORT);
//						is   = xS.getInputStream();                            //(kkmin,20070117)
//						xin  = new BufferedReader(new InputStreamReader(is));  //(kkmin,20070117)
//						xout = new BufferedWriter(new OutputStreamWriter(xS.getOutputStream()));
//						System.out.println(IP + " : " + String.valueOf(PORT) + " 서버에 연결했습니다.");
//					}catch(Exception e){
//						e.printStackTrace();
//						System.out.println(IP + " : " + String.valueOf(PORT) + " 서버 연결이 실패했습니다.");
//						sendError = "9";
//					}
//					
//					/*
//					 * 기관별로 공통헤더 엔티티 빈을 통해 헤더를 생성하고
//					 * 화면에서 전송한 데이타본문의 앞에 헤더를 붙여 해당기관으로 전송한다
//					 */
//					
//					try{
//						System.out.println("===== 신용정보사에 보내는 데이터 =======");
//						//System.out.println(localRequestHeader[13]);
//						System.out.println("[" + localRequestHeader[2] + "][" + localRequestHeader[2].getBytes().length + "]");
//						System.out.println("===============================");
//				        //sendMsg(localRequestHeader[13]);
//						sendMsg(localRequestHeader[2]);
//						
//					}catch(Exception e){
//						e.printStackTrace();
//						System.out.println("전문전송에러");
//						sendError = "9";	// 송신불가
//					}
//
//					/* 요청기관코드에 따른 수신 */
//					String xRcv = "";		// 외부기관에서 받는전문을 저장하는 변수
//					String recvError = "0";
//					if(!sendError.equals("9")){
//						try{
//							xRcv   = rcvMsg();
//							//xRcv  += rcvMsg();
//						}catch(Exception e){
//							System.out.println("전문수신에러");
//							recvError = "8";	// 수신불가.
//						}
//					}
//
//					try{
//						if(xRcv.getBytes().length == 0){
//							System.out.println("원격데몬으로부터 전문응답이 없습니다");
//							recvError = "8";	// 수신불가.
//						}
//					}catch(Exception e){
//						System.out.println("바이트 Array 길이체크 오류");
//						recvError = "8";		// 수신불가.
//					}					
//					
//					System.out.println("[신용정보사 응답전문--------------------------------------]");
//					System.out.println("["+xRcv+"]");
//					System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
//						
//						
//					// 기관별 응답전문 공통헤더 파싱함수 호출
//					sToBytes = xRcv.getBytes();
//					String[] niceHeader = null;
//					
//					/* 
//					 * 로컬 공통부 구성(응답)
//					 * 기관별 응답전문의 공통부를 파싱하여 얻은정보로 로컬공통부를 구성한다.
//					 */
//					String stsCode = "0";
//					if( sendError.equals("0")){
//						// 송신이 정상이면
//						if( recvError.equals("0")){
//							// 수신이 정상이면
//							stsCode = "0";		// 네트웍 정상으로 판단
//						}else{
//							if( recvError.equals("8") ){
//								stsCode = "8";	// 수신오류로 판단
//							}else{
//								stsCode = "7";	// 비정상수신으로 판단
//							}
//						}
//					}else{
//						// 송신이 오류이면
//						stsCode = "9";			// 송신오류로 판단(네트웍 연결오류)
//					}
					
					System.out.println("===== 로컬에 보내는 데이터 =======");
					System.out.println("[" + localRequestHeader[2] + "][" + localRequestHeader[2].getBytes().length + "]");
					System.out.println("===============================");
					String xRcv = "";
					if(str.length() > 300) xRcv = KibGram.createReturnMsg(localRequestHeader[2]);
					System.out.println("[로컬 응답전문--------------------------------------]");
					System.out.println("["+xRcv+"]["+xRcv.getBytes().length + "]");
					System.out.println("[--------------------------------------------------]");
					// 로컬응답을 리턴한다.
					try{
						//out.write(localResponseHeader);
						out.write(xRcv);
						out.flush();
					}catch(Exception e){
						e.printStackTrace();
					}
					
					try{
//						xin.close();
//						xout.close();							
//						xS.close();				
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
//					xin.close();
//					xout.close();					
//					xS.close();
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

				if (SendDaemonLocalReturn.nPORT == 28099 || SendDaemonLocalReturn.nPORT == 28100) { // 1F005 전문은 전문길이가 10바이트로 온다.
					gramLength = 10;
				} else {                         // 그이외에는 전문길이가 4바이트로 온다.
					gramLength = 4;
				}
				System.out.println(" SendDaemon.nPORT  : " + SendDaemonLocalReturn.nPORT + " , gramLength : " + gramLength);

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

	//////////////////////////////////////////////////
	// KIBNET을 가지 않고 메시지 응답코드 세팅후, 돌려줌
	//////////////////////////////////////////////////
	private static class KibGram implements IKibGramEnum {

		private static final int H순번_응답코드 = 12;
		private static final String H_KB0200 = "KB0200"; 
		private static final int H순번_거래후계좌잔액 = 2;
		private static final int H순번_이체수수료금액 = 13;
		
		private static final int H소켓전문길이_글자수 = 4;
		private static final String H응답코드_오류없음 = "000";
		private static final String H신한은행_코드 = "00000088";
		private static final String H우리은행_코드 = "00000020";
		private static final int wrAcntVal = 1000000000;
		private static final int shAcntVal = 1000000000;
		private static final int 자행이체수수료 = 100;
		private static final int 타행이체수수료 = 500;
		
		private static final int[] headerPosition = {
			      // index kibnet전문
			0     //     1  1
			, 4   //  1  2  2 (  4)전문길이
			, 8   //  2  3  3 (  4)CS번호
			, 16  //  3  4  4 (  8)CS관리 기관코드
			, 17  //  4  5  5 (  1)REACTION CODE
			, 20  //  5  6  6 (  3)연속거래번호(3)
			, 21  //  6  7  7 (  1)송수신FLAG(1)
			, 29  //  7  8  8 (  8)취급기관코드(8)
			, 34  //  8  8  8 (  5)취급영업점코드(4)
			, 38  //  9 10  9 (  4)취급단말코드(5)
			, 39  // 10 11 10 (  1)매체(발생)구분(1)
			, 43  // 11 12 11 (  4)전문구분코드(MSG TYPE)(4)
			, 47  // 12 13 12 (  4)거래구분코드(4)
			, 50  // 13 14 13 (  3)응답코드(3)
			, 58  // 14 15 14 (  8)거래일자(8)
			, 64  // 15 16 15 (  6)거래시간(6)
			, 71  // 16 17 16 (  7)거래일련번호(7)
			, 72  // 17 18 17 (  1)한글코드구분(1)
			, 73  // 18 19 18 (  1)마감후구분(1)
			, 81  // 19 20 19 (  8)개설기관코드(8)
			, 82  // 20 21 20 (  1)KIB_USE_1 M/S TRACK번호(1)
			, 223 // 21 21 21 (141)KIB_USE_2 M/S TRACK DATA(141)
			, 224 // 22 21 22 (  1)KIB_USE_3 카드구분(1)
			, 225 // 23 21 23 (  1)KIB_USE_4 정형화된 조회 출력 구분(1)
			, 226 // 24 21 24 (  1)KIB_USE_5 이체거래시 입출기관구분(1)
			, 240 // 25 21 25 ( 14)KIB_USE_6 USER WORK AREA(14)
			, 280 // 26 22 26 ( 40)응 답 MESSAGE(40)
			, 286 // 27 29 27 (  6)원거래요소(6)
			, 290 // 28 29 28 (  4)FILLER1(4) 은행응답코드(4)
			, 300 // 29 30 29 ( 10)FILLER(10)
		};

		private static final int[] bodyPosition = {
			      // index kibnet전문
			0     //     1  1 -----금액정보
			, 13  //  1  2  2 ( 13)거래금액
			, 14  //  2  3  3 (  1)양,음 구분표시
			, 27  //  3  4  4 ( 13)거래후 계좌잔액
			, 40  //  4  5  5 ( 13)미결제 타점권 금액
			//------------ ---입금계좌부
			, 48  //  5  6  6 (  8)제휴기관코드    ( 8)
			, 50  //  6  7  7 (  2)입금계좌구분코드( 2) - '00' 세팅
			, 66  //  7  8  8 ( 16)계좌번호(가상)  (16)
			, 86  //  8  8  8 ( 20)입금계좌성명    (20)
		    //----------------출금계좌부
			, 94  //  9 10  9 (  8)은행(제후기관코드)( 8) - '00000020' or '00000088'
			, 96  // 10 11 10 (  2)출금계좌구분코드  ( 2) - '00'
			, 112 // 11 12 11 ( 16)계좌번호(가상)    (16)
			, 120 // 12 13 12 (  8)비밀번호          ( 8)
			, 140 // 13 14 13 ( 20)출금계좌성명      (20)
		    //----------------개별부
			, 145 // 14 15 14 (  5)수수료금액        ( 5) - '00000'
			, 148 // 15 16 15 (  3)현금매수          ( 3) - '000'
			, 150 // 16 17 16 (  2)수표매수          ( 2) - '00'
		    //----------------개별부-수표 1 / 5
			, 158 // 17 18 17 (  8)수표번호     (8)
			, 164 // 18 19 18 (  6)수표발행정보 (6)
			, 166 // 19 20 19 (  2)수표권종     (2)
		    //----------------개별부-수표 2 / 5
			, 174 // 17 18 17 (  8)수표번호    (8)
			, 180 // 18 19 18 (  6)수표발행정보(6)
			, 182 // 19 20 19 (  2)수표권종    (2)
		    //----------------개별부-수표 3 / 5
			, 190 // 17 18 17 (  8)수표번호    (8)
			, 196 // 18 19 18 (  6)수표발행정보(6)
			, 198 // 19 20 19 (  2)수표권종    (2)
		    //----------------개별부-수표 4 / 5
			, 206 // 17 18 17 (  8)수표번호    (8)
			, 212 // 18 19 18 (  6)수표발행정보(6)
			, 214 // 19 20 19 (  2)수표권종    (2)
		    //----------------개별부-수표 5 / 5
			, 222 // 17 18 17 (  8)수표번호    (8)
			, 228 // 18 19 18 (  6)수표발행정보(6)
			, 230 // 19 20 19 (  2)수표권종    (2)
		    //----------------개별부-계속
			, 233 // 26 22 26 (  3)취소사유      ( 3)
			, 245 // 27 29 27 ( 12)거래관리번호  (12)
			, 250 // 28 29 28 (  5)배분수수료금액( 5)
			, 256 // 28 29 28 (  6)복기부호      ( 6)
			, 300 // 29 30 29 ( 44)FILLER        (44)
		};

		private byte[] sToBytes;
		private String[] header;
		private String[] body;
		private String orgGram;
		private boolean alreadyProcessHeader = false;
		private boolean alreadyProcessBody = false;

		private KibGram(String msg) {
			orgGram = msg;
			sToBytes = orgGram.getBytes();
		}

		public String[] parserKibHeader() throws Exception {

			if (!isValidation(orgGram))
				throw new Exception("Validation Exception...");

			if (isAreadyProcessHeader()) return header;
			
			header = new String[headerPosition.length - 1];

			for (int i = 0; i < headerPosition.length - 1; i++) {
				header[i] = subString(headerPosition[i], headerPosition[i + 1]);
			}
			processHeader();
			// kibHeader[0] = subString(0,4); // 전문길이(4)
			// kibHeader[1] = subString(4,8); // CS번호(4)
			// kibHeader[2] = subString(8,16); // CS관리 기관코드(8)
			// kibHeader[3] = subString(16,17); // REACTION CODE(1)
			// kibHeader[4] = subString(17,20); // 연속거래번호(3)
			// kibHeader[5] = subString(20,21); // 송수신FLAG(1)
			// kibHeader[6] = subString(21,29); // 취급기관코드(8)
			// kibHeader[7] = subString(29,33); // 취급영업점코드(4)
			// kibHeader[8] = subString(33,38); // 취급단말코드(5)
			// kibHeader[9] = subString(38,39); // 매체(발생)구분(1)
			// kibHeader[h전문구분코드] = subString(39,43); // 전문구분코드(MSG TYPE)(4)
			// kibHeader[h거래구분코드] = subString(43,47); // 거래구분코드(4)
			// kibHeader[12] = subString(47,50); // 응답코드(3)
			// kibHeader[13] = subString(50,58); // 거래일자(8)
			// kibHeader[14] = subString(58,64); // 거래시간(6)
			// kibHeader[15] = subString(64,71); // 거래일련번호(7)
			// kibHeader[16] = subString(71,72); // 한글코드구분(1)
			// kibHeader[17] = subString(72,73); // 마감후구분(1)
			// kibHeader[18] = subString(73,81); // 개설기관코드(8)
			// kibHeader[19] = subString(81,82); // M/S TRACK번호(1)
			// kibHeader[20] = subString(82,223); // M/S TRACK DATA(141)
			// kibHeader[21] = subString(223,224); // 카드구분(1)
			// kibHeader[22] = subString(224,225); // 정형화된 조회 출력 구분(1)
			// kibHeader[23] = subString(225,226); // 이체거래시 입출기관구분(1)
			// kibHeader[24] = subString(226,240); // USER WORK AREA(14)
			// kibHeader[25] = subString(240,280); // 응답 MESSAGE(40)
			// kibHeader[26] = subString(280,286); // 원거래요소(6)
			// kibHeader[27] = subString(286,300); // FILLER(14)
			return header;
		}

		private void processBody() {
			alreadyProcessBody = true;
		}

		private boolean isAreadyProcessBody() {
			
			return alreadyProcessBody;
		}

		private void processHeader() {
			alreadyProcessHeader = true;
		}

		private boolean isAreadyProcessHeader() {
			
			return alreadyProcessHeader;
		}

		public String[] parserKibBody() throws Exception {

			if (!isValidation(orgGram))
				throw new Exception("Validation Exception...");

			if (isAreadyProcessBody()) return body;
			
			body = new String[bodyPosition.length - 1];
			int kibHeaderSize = headerPosition[headerPosition.length-1]; 

			for (int i = 0; i < bodyPosition.length - 1; i++) {
				body[i] = subString(kibHeaderSize+bodyPosition[i], kibHeaderSize+bodyPosition[i + 1]);
			}
			processBody();
			
			return body;
		}

		private boolean isValidation(String msg) {
//			if (!isCheckLength(msg))
//				return false;
			return true;
		}

		private boolean isCheckLength(String msg) {

			return (msg.getBytes().length == Integer.parseInt(msg.substring(0, 4)));

		}

		/**
		 * 문자열 바이트로 계산 - Exception 에러 발생
		 * 
		 * @return a String - 요청한 바이트 사이의 문자열
		 * @param int to Set - 시작 바이트
		 * @param int to Set - 마지막 바이트
		 */
		private String subString(int start, int end) throws Exception {
			String strRtn = "";
			byte[] bOut = new byte[end - start];

			try {
				for (int i = start; i < end; i++) {
					bOut[i - start] = sToBytes[i];
				}

				strRtn = new String(bOut);

				return strRtn;
			} catch (NullPointerException ne) {
				throw new Exception("subString() NullPointerException : "
						+ ne.getMessage());
			} catch (Exception e) {
				throw new Exception("subString() Exception : " + e.getMessage());
			}
		}

		public String[] getHeader() throws Exception {
			parserKibHeader();
			return header;
		}

		public String[] getBody() throws Exception {
			parserKibBody();
			return header;
		}

		// public static int[] getHeaderPostion() {
		// return headerPositon;
		// }

		/**
		 * 입금대행취소거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountAgentCancelTransaction() {
			return (header[h전문구분코드].equals(전문코드_거래취소코드)
					&& header[h거래구분코드].equals(거래구분_입금대행거래));
		}

		/**
		 * 입금취소거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountCancelTransaction() {
			return (header[h전문구분코드].equals(전문코드_거래취소코드)
					&& header[h거래구분코드].equals(거래구분_입금거래));
		}

		/**
		 * 수취조회거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		@SuppressWarnings("unused")
		private boolean isReceiveReadTransaction() {
			return (header[h전문구분코드].equals(전문코드_거래실행코드)
					&& header[h거래구분코드].equals("4100"));
		}

		/**
		 * 입금대행거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountAgentTransaction() {
			return (header[h전문구분코드].equals(전문코드_거래실행코드)
					&& header[h거래구분코드].equals("1300"));
		}

		/**
		 * 입금거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountTransaction() {
			return (header[h전문구분코드].equals(전문코드_거래실행코드)
					&& header[h거래구분코드].equals(거래구분_입금거래));
		}

		/**
		 * 개시거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		@SuppressWarnings("unused")
		private boolean isStartGram() {
			return (header[h전문구분코드].equals(전문코드_은행기관관리코드)
					&& header[h거래구분코드].equals(거래구분_개시거래));
		}

		/**
		 * 종료거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		@SuppressWarnings("unused")
		private boolean isEndGram() {
			return (header[h전문구분코드].equals(전문코드_은행기관관리코드)
					&& header[h거래구분코드].equals(거래구분_종료거래));
		}

		/**
		 * 전문 생성 메소드
		 * @param msg - 전문
		 * @return 전문클래스
		 * @throws Exception
		 */
		public static KibGram create(String msg) {
			return new KibGram(msg);
		}

		/**
		 * 리턴 전문 생성 메소드
		 * @param msg - 전문
		 * @return 전문클래스
		 * @throws Exception
		 */
		public static String createReturnMsg(String msg) throws Exception {
			KibGram kibGram = new KibGram(msg);
			kibGram.parserKibHeader();
			kibGram.parserKibBody();
			String result = kibGram.makeReturnMsg();
			return kibGram.makeSocketMsg(result);
		}

		private String makeReturnMsg() {
			String result = "";
			
			header[H순번_응답코드] = H응답코드_오류없음;
			body[H순번_거래후계좌잔액] = "0001234500000";
			body[H순번_이체수수료금액] = "00100";
			
			for(int i = 0; i < header.length; i++) {
				result += header[i];
			}
			for(int i = 0; i < body.length; i++) {
				result += body[i];
			}

			return result;
		}

		private String makeSocketMsg(String result) {
			return "0"+(result.getBytes().length+H소켓전문길이_글자수)+result;
		}

//		public static int[] getHeaderPositon() {
//			return headerPosition;
//		}
	//
//		public static int[] getBodyPositon() {
//			return headerPosition;
//		}

	}
}