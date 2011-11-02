/*
 * 대외계 온라인 송신측 데몬이다.
 * 
 * 대외기관과 직접적으로 전문을 주고받는 데몬이다.
 * 클라이언트(화면단)의 요청을 받아 로컬공통헤더를 파싱한다.
 * 파싱된 로컬헤더의 기관정보에 따라 해당기관으로 전송한다.
 * 이 데몬은 한신정,한신평,KIB,대우자판 기관에 대해 동기방식으로 작동한다.
 * 이 데몬은 은행연합회에 대해 비동기방식으로 작동한다.
 * 동기방식은 요청전문을 기관으로 전송하고 응답전문이 올때까지 소켓은 블록킹 상태에 들어간다.
 * 비동기방식은 요청전문을 기관으로 무조건 전송하고 응답전문은 메시지큐에서 관리번호로 페치한다.
 * 지정된 타임아웃(Time-out) 시간이 경과하면 Time-out Exception 을 발생시키고
 * 요청전문을 전송한 클라이언트에 장애상태를 코드로 리턴한다.
 */
package ssvr;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.cabis.xutil.Logger;

import util.*;

public class SenderR {
	public static int sPORT = 0;
	public static boolean SVC_START = false;
	public static boolean SVC_CLOSE = false;
	public Vector   clients;
	public Vector   megaboxclients;
	public Manager  manager;
	public MegaBoxManager mboxmanager;
	public Listener listener;

	/*
	 * 생성자
	 * Connection 관리자를 생성하고 구동한다.
	 * Listener 를 생성하고 구동한다.
	 */
	public SenderR() throws Exception {
		clients = new Vector();
		megaboxclients = new Vector();
		
		manager = new Manager();
		manager.start();
		mboxmanager = new MegaBoxManager();
		
		listener = new Listener();
		listener.start();
	}

	/*
	 * 데몬의 시작 메인함수
	 */
	public static void main(String[] args) throws Exception {
		try{
			if(args[0] == null) 
				SenderR.sPORT = 9002;
			else
				SenderR.sPORT = Integer.parseInt(args[0]);
			SenderR server = new SenderR();
			System.out.println("=========================================================");
			System.out.println("서버데몬이 가동되었습니다......");
			System.out.println("=========================================================");
		}catch(IOException ie){
			System.out.println("메가박스(MEGABOX)장비 접속에 문제가 발생하여 데몬을 기동할수 없습니다.");
			System.out.println("=========================================================");
			System.out.println(ie.getMessage());
			System.out.println("=========================================================");
			System.out.println("메가박스를 제외한 기능만 수행하시기 바랍니다.");
			//System.exit(-1);
		}catch(Exception e){
			System.out.println("데몬을 기동하는중 오류가 발생하여 데몬을 기동할수 없습니다.");
			System.out.println("=========================================================");
			System.out.println(e.getMessage());
			System.out.println("=========================================================");			
			System.out.println("데몬을 종료하였습니다.");
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
			ss = new ServerSocket(SenderR.sPORT);
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

	/**
	 * 요청 관리
	 * 소켓접속을 관리한다.
	 */
	class MegaBoxManager extends Thread {
		int  alloc = 0;
		
		public void makeChannel(int PORT) throws Exception {
			try{
				MegaBoxClient megaboxclient = new MegaBoxClient(PORT);
				megaboxclient.start();
				megaboxclients.addElement(megaboxclient);
			}catch(Exception e){
				throw e;
			}
		}

		public MegaBoxClient getChannel(){
			int  loc = alloc++;
			if( alloc > 1 ) alloc = 0;

			System.out.println("MEGA BOX["+0+"]번째 채널이 할당되었습니다");
			return (MegaBoxClient)megaboxclients.get(0);
		}

		public void run() {
			MegaBoxClient megaboxclient = null;
			for(;;) {
				try {
					for(int i = 0; i < megaboxclients.size(); i++) {
						megaboxclient = (MegaBoxClient)megaboxclients.elementAt(i);
						
						if(megaboxclient.bank_xS == null) {
							System.out.println("메가박스 채널이 종료되었습니다:"+megaboxclient.PORT);
							megaboxclients.remove(i);
							Thread.sleep(1000);
							makeChannel(megaboxclient.PORT);
							System.out.println("메가박스 채널이 생성되었습니다:"+megaboxclient.PORT);
							break;
						}
					}
					
					Thread.sleep(1000);
				} catch(Exception e) {
					System.out.println("메가박스 채널에 연결할수 없습니다:"+megaboxclient.PORT);
					System.out.println("메시지:"+e.getMessage());
				}
			}
		}
	}	

	class MegaBoxClient extends Thread {
		// 메가박스와 온라인데몬을 상시적으로 연결해 놓는다.
		private Socket   		 bank_xS 	= null;
		private DataInputStream  bank_xin  	= null; 
		private DataOutputStream bank_xout 	= null;
		
		private Hashtable gramQueue = new Hashtable();
		
		private static final long MAX_RECV_TIMEOUT = 1000;		
		public boolean EOR_YN 	 = false;
		private long   reqCount  = 0;
		private String sendError = "0";
		private String recvError = "0";
		
		// MEGA BOX IP
		private String IP   = "152.149.46.138";     //네트워크 변경작업으로 수정 152.149.138.250 -> 152.149.46.138(kkmin,20070104)

		private int    PORT = 0;

		// 전문스트링을 저장하는 바이트배열
		private byte[] byteArray = null;
		
		// MEGABOX I/O 스트림
		MegaBoxClient(int PORT) throws Exception {
			try{
				this.PORT = PORT;
				
				if( bank_xS == null ) {
					bank_xS   = new Socket(IP,PORT);
					bank_xin  = new DataInputStream(bank_xS.getInputStream());                  //kkmin,20070326
					bank_xout = new DataOutputStream(bank_xS.getOutputStream());			
					bank_xS.setKeepAlive(true);
					System.out.println("MEGABOX 소켓접속완료:"+PORT);
					setEOR();
				}
			}catch(Exception e){
				System.out.println("MEGABOX 소켓접속에러:"+PORT);
				throw e;
			}			
		}

		/* 
		 * 매일 08:30 - 09:30 사이에 개시전문을 주기적으로 Polling 한다.
		 * 개시전문에 대한 응답이 없어도 09:30 분부터는 연합회는 업무를 개시한다.
		 * 개시전문을 받았을경우 개시응답전문을 작성하고 전송한다.
		 */
		public void run(){
			String   recvGram = "";
			String   sendGram = ""; 
			String[] openGram = null;

			int cTime	= 0;
			
			for(;;){
				try{
					// 전문 수신
					recvGram = rcvKfbMsg();
					if( !recvGram.equals("")) {
						openGram = parseKfbHeader(recvGram);
						// 전문 파싱
						if( openGram[3].equals("0800") && openGram[4].equals("100")) {
							// 개시전문이면 해당값을 바꾸어 재전송한다.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("은행연합회 신용정보조회 서비스가 개시되었습니다.");
							
							SVC_START = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("101")) {
							// 재개시전문이면 해당값을 바꾸어 재전송한다.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("은행연합회 신용정보조회 서비스가 재개시되었습니다.");
							
							SVC_START = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("200")) {
							// 종료예고이면 해당값을 바꾸어 재전송한다.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("은행연합회 신용정보조회 서비스가 종료예고 되었습니다.");
							
							SVC_START = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("201")) {
							// 종료전문이면 해당값을 바꾸어 재전송한다.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("은행연합회 신용정보조회 서비스가 종료 되었습니다.");
							
							SVC_CLOSE = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("400")) {
							// 장애전문이면 해당값을 바꾸어 재전송한다.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("은행연합회 신용정보조회 서비스가 장애상태입니다.");
							
							SVC_CLOSE = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("500")) {
							// 종료전문이면 해당값을 바꾸어 재전송한다.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("은행연합회 신용정보조회 서비스가 장애를 회복하였습니다.");
							
							SVC_CLOSE = true;
						}else{
							// 전문요청 카운트 증가
							//reqCount++;
							//System.out.println(this+":요청카운트:"+reqCount);
							
							// 전문요청 카운트 감소
							//reqCount--;
							//System.out.println(this+":해제카운트:"+reqCount);
							
							System.out.println("메시지큐에 넣음 [전문종별코드:"+openGram[3]+"][전문관리번호:"+openGram[8]+"]");
							gramQueue.put(openGram[8],recvGram);
						}
					}

					cTime   = Integer.parseInt(getShortTimeString());
				}catch(Exception e){
					System.out.println("은행연합회 신용정보조회 관리전문 처리중 오류가 발생하였습니다.");
					System.out.println("오류전문["+recvGram+"]");
					e.printStackTrace();
				}
				
				try{
					// 0.1 초 단위로 폴링한다.
					Thread.sleep(100);
				}catch(Exception e){
					;
				}
			}
			// end for
		}
		// end run()

		/**
		 * 시간을 HHmmss 형태의 문자열로 출력
		 * @return a String
		 */
	    public String getShortTimeString()
	    {
	        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss", Locale.KOREA);
	        return formatter.format(new Date());
	    }
	    
		/**
		 * 날자를 yyyy-MM-dd-HH:mm:ss:SSS 형태의 문자열로 출력
		 * @return a String
		 */
	    public String getTimeStampString()
	    {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS", Locale.KOREA);
	        return formatter.format(new Date());
	    }
	    
		/**
		 * 날자를 yyyyMMddHHmmss 형태의 문자열로 출력
		 * @return a String
		 */
	    public String getUnformatTimeString()
	    {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
	        return formatter.format(new Date());
	    }	    
	    
		public void setEOR(){
			try{
				if( EOR_YN == false ) {
					/* FFFB00FFFD00FFFB19FFFD19 */
					int[] rEOR = new int[] {0xFF, 0xFB, 0x00, 0xFF, 0xFD, 0x00, 0xFF, 0xFB, 0x19,0xFF, 0xFD, 0x19};   
					int cnt = 0;
					int[] rcvChar = rcvMboxMsg();
					for( int i=0; i<rEOR.length; i++ ){
						if( rcvChar[i] == rEOR[i] ) cnt++;
					}
					
					/* FFFD19 */ 
					byte[] iEOR = new byte[] {(byte)0xFF, (byte)0xFD, (byte)0x19};
	
					if(cnt == 12){
						bank_xout.write(iEOR,0,3);
						bank_xout.flush();
	
						EOR_YN = true;
					}
					
					if(EOR_YN){
						System.out.println("MEGABOX 가 EOR MODE 로 작동시작:"+PORT);
					}else{
						System.out.println("MEGABOX 가 EOR MODE 로 작동실패:"+PORT);
					}
				}
			}catch(Exception e){
				System.out.println("MEGABOX 장비 EOR MODE 로 전환중 오류발생:"+ PORT + ":" + e.toString());
			}
		}
		
		public synchronized String getGramMesg(String gram) {
			// 외부기관에서 받는전문을 저장하는 변수
			String xRcv = "";
			
			try {
		
				try{
					// 실제전문 전송
					bankSendMsg(gram);
				}catch(Exception e){
					sendError = "9";		// 송신불가
				}
				
				if(!sendError.equals("9")){
					try{
						xRcv   = rcvKfbMsg();
					}catch(Exception e){
						recvError = "8";	// 수신불가.
					}
				}
				
			} catch(Exception e) {
				try {
					// 외부소켓 종료
					bank_xin.close();         
					bank_xout.close();					
					bank_xS.close();
					bank_xS = null;
					System.out.println("통신 오류가 발생하여 MEGA BOX 소켓접속을 종료하였습니다");
				} catch (Exception ex) {
					;
				}
			} finally {
		
			}
			
			return xRcv;
		}
		
		/*
		 * 처리요청 카운트를 리턴한다.
		 */
		public long getReqCount() {
			return reqCount;
		}

		/**
		 * 은행연합회에 전문을 전송한다.
		 * @param String to set
		 */
		public synchronized void bankSendMsg(String command) throws IOException {
			byte[] eEOR = new byte[] {(byte)0xFF,(byte)0xEF};
			byte[] cREC = command.getBytes();
			byte[] Gram = new byte[cREC.length+2];
			
			try {
				for(int i=0;i<cREC.length;i++){
					Gram[i] = cREC[i];
				}
				
				int FF = Gram.length-2;
				int EF = Gram.length-1;
				Gram[FF] = eEOR[0];
				Gram[EF] = eEOR[1];
				
				/* 
				 * 내부이더넷과 은행연합회(WAN)전용선(56K)과 의 속도차이가 너무나서 처음 메가박스와 
				 * 연결을 맺었을시 약간의 지연시간을 주고 전문을 보내야 한다.그렇지 않으면 
				 * 전문이 소실된다.
				 * 그러나 현재와 같이 채널을 데몬을 기동할때 부터 연결시키고 사용하면 이 sleep 은 필요없다.
				 * Thread.sleep(100);
				 */
				bank_xout.write(Gram);
				bank_xout.flush();
				
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {

			}
		}
		
		/** 연합회 데이터를 수신한다.
		  * @return
		  * @exception IOException
	      */
		public synchronized String rcvKfbMsg() throws Exception {
			byte[] 	bData 	= new byte[100000];    
			String 	sData 	= "";
			String 	sTemp 	= "";
			
			int 	cnt 		= 0;
			long 	startTime	= 0;
			long 	endTime		= 0;
			int 	receivedSum = 0;
			byte[] rsltData = new byte[10000];
			
			try{
				startTime = System.currentTimeMillis();
				if ( bank_xS.getInputStream().available() > 0) {
					/* 
					 * 스트림에 읽을 데이타(바이트)가 있을경우 읽는다.
					 * 스트림이 닫혔을경우(스트림의 끝) -1을 리턴한다.
					 * 읽은 바이트의 수를 리턴한다.
					 * bData의 길이가 10000 이므로 10000 바이트를 읽고 지나간다. 
					 */
					while (true) {
						endTime = System.currentTimeMillis();
						
						if ((endTime - startTime) > MAX_RECV_TIMEOUT) {
							break;
						}
						boolean GramEnd = false;
						cnt = bank_xin.read(bData);
	
						for (int i=0; i<cnt; i++){
							rsltData[i+receivedSum] = bData[i];
						}
						// 읽은 바이트수를 누적 
						receivedSum += cnt;

						// 바이트배열을 스트링으로 변환
//						sTemp = new String(bData,0,cnt,"euc-kr");   //kkmin,20070326
//						sData += sTemp;                             //kkmin,20070326

						// 전문종료문자(0xFF,0xEF)를 찾는다.
						for(int k=0;k<cnt;k++){
							if(bData[k] == (byte)0xFF && bData[k+1] == (byte)0xEF){
								GramEnd = true;
							}
						}

						if ( GramEnd ){
							break;  
						}
					}
					sData = new String(rsltData, 0, receivedSum, "euc-kr");   //kkmin,20070326

				}
			}catch(Exception e){
				throw e;
			}
			
			return sData;
		}
		

		/**
		 * 메가박스 EOR 을 받아들인다. 은행연합회
		 * @return a String
		 */
		public int[] rcvMboxMsg() throws IOException {
			
			int[] cTot = new int[12];
			
			try {
				for(int i=0;i<12;i++){
					cTot[i] = bank_xin.read();                                 //kkmin,20070326
//					cTot[i] = xin.read();
				}
			} catch (Exception e) {
                System.out.println("MegaBox Connection시 오류" + e.toString());
				throw new IOException(e.getMessage());
			} finally {

			}

			return cTot;			
		}
		
		public synchronized String getQueueMsg(String strOrgMngNo) {
			return (String)gramQueue.get(strOrgMngNo);
		}
		public synchronized void delQueueMsg(String strOrgMngNo) {
			gramQueue.remove(strOrgMngNo);
		}
		public int getQueueSize() {
			return gramQueue.size();
		}				
		
		/*
		 * 은행연합회 전문중 공통헤더를 파싱
		 * @param String 은행연합회 전문
		 * @return String[] 은행연합회 공통헤더 항목배열
		 * Comment : 은행연합회 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */
		public String[] parseKfbHeader(String req) throws Exception {
			
			byteArray = req.getBytes();
			String[] attr = new String[13];
			
			try{
				attr[0]  = subString(0,9);		// TRANSACTION CODE(9)
				attr[1]  = subString(9,11);		// SYSTEM-ID(2)
				attr[2]  = subString(11,14);	// 대표기관코드(3)
				attr[3]  = subString(14,18);	// 전문종별코드(4)
				attr[4]  = subString(18,21);	// 업무구분코드(3)
				attr[5]  = subString(21,22);	// 송수신FLAG(1)
				attr[6]  = subString(22,25);	// 응답코드(3)
				attr[7]  = subString(25,32);	// 점포코드(7)
				attr[8]  = subString(32,39);	// 금융기관전문관리번호(7)
				attr[9]  = subString(39,53);	// 금융기관전문전송시간(14)
				attr[10] = subString(53,60);	// 연합회전문관리번호(7)
				attr[11] = subString(60,74);	// 연합회전문전송시간(14)
				attr[12] = subString(74,90);	// 예비정보필드(16)
				
				/*
				System.out.println("은행연합회 KFB HEADER BEGIN============================");
				System.out.println("["+attr[0]	+"]TRANSACTION CODE");	// TRANSACTION CODE(9)
				System.out.println("["+attr[1]  +"]SYSTEM-ID");	// SYSTEM-ID(2)
				System.out.println("["+attr[2]  +"]대표기관코드");	// 대표기관코드(3)
				System.out.println("["+attr[3]  +"]전문종별코드");	// 전문종별코드(4)
				System.out.println("["+attr[4]  +"]업무구분코드");	// 업무구분코드(3)
				System.out.println("["+attr[5]  +"]송수신FLAG");	// 송수신FLAG(1)
				System.out.println("["+attr[6]  +"]응답코드");	// 응답코드(3)
				System.out.println("["+attr[7]  +"]점포코드");	// 점포코드(7)
				System.out.println("["+attr[8]  +"]금융기관전문관리번호");	// 금융기관전문관리번호(7)
				System.out.println("["+attr[9]  +"]금융기관전문전송시간");	// 금융기관전문전송시간(14)
				System.out.println("["+attr[10] +"]연합회전문관리번호");	// 연합회전문관리번호(7)
				System.out.println("["+attr[11] +"]연합회전문전송시간");	// 연합회전문전송시간(14)
				System.out.println("["+attr[12] +"]예비정보필드");			// 예비정보필드(16)
				System.out.println("은행연합회 KFB HEADER END==============================");
				*/
			}catch(Exception e){
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
					bOut[i - start] = byteArray[i];
				}

				strRtn = new String(bOut);

				return strRtn;
			} catch (NullPointerException ne) {
				throw new Exception("subString() NullPointerException : " + ne.getMessage());
			} catch (Exception e) {
				throw new Exception("subString() Exception : " + e.getMessage());
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
		private String IP   = "";
		private int    PORT = 0;
		
		//KCB port (kkmin,20070423)
		//int[] kcbPort = {26001,26002,26003,26004,26005,26000};
		int[] kcbPort = {28800};
		int kcbPortSeq = 0;

		// 로컬 I/O 스트림
		Client(Socket con) throws Exception {
			lS = con;
			in  = new BufferedReader(new InputStreamReader(lS.getInputStream()));
		    out = new BufferedWriter(new OutputStreamWriter(lS.getOutputStream()));			
		}

		public void run() {
			try {
				for(;;) {
					// 로컬요청을 받는다.
					String str = in.readLine();

					// 공통헤더 파싱을 위해 바이트 배열로 변환한다.
					sToBytes = str.getBytes();

					if( sToBytes.length < 100 ) {
						System.out.println("요청한 전문의 길이가 너무 작습니다. 전문은 무시되었습니다.["+str+"]");
						continue;
					}

					// 공통헤더를 파싱한다.
					String[] localRequestHeader = parseOnlineCommonHeader(str);
					
					/* 요청기관코드 변환 */
					int orgCode = 00;
					try{
						orgCode = Integer.parseInt(localRequestHeader[0]);
					}catch(Exception e){
						orgCode = 99;
						continue;
					}
					if((orgCode != 11 &&  orgCode != 12 && orgCode != 13 &&  orgCode != 21 && 
						orgCode != 22 &&  orgCode != 14 && orgCode != 31)|| (orgCode == 99) ){            //14추가 (kkmin,20061228)
						System.out.println("전문 요청기관이 잘못되었습니다.");
						System.out.println("---------------Daemon Local Header Parsing--------------------");
						System.out.println("["+localRequestHeader[0] +"]대외기관코드");
						System.out.println("["+localRequestHeader[1] +"]송수신여부");
						System.out.println("["+localRequestHeader[2] +"]거래구분코드");
						System.out.println("["+localRequestHeader[3] +"]관리번호");
						System.out.println("["+localRequestHeader[4] +"]거래일련번호");
						System.out.println("["+localRequestHeader[5] +"]응답코드");
						System.out.println("["+localRequestHeader[6] +"]거래일자");
						System.out.println("["+localRequestHeader[7] +"]거래시간");
						System.out.println("["+localRequestHeader[8] +"]데이터길이");
						System.out.println("["+localRequestHeader[9] +"]처리단말번호");
						System.out.println("["+localRequestHeader[10]+"]단말등록사원번호");
						System.out.println("["+localRequestHeader[11]+"]상태코드");
						System.out.println("["+localRequestHeader[12]+"]예비정보내역");
						System.out.println("["+localRequestHeader[13]+"]데이타본문");
						System.out.println("--------------------------------------------------------------");
						continue;						
					}
					
					// 기관별 IP/PORT 세팅
					switch(orgCode) {
						case 11:
							break;
						case 12:
							// 한신정
							//IP   = "150.50.50.51";
							//PORT = 29176;// 개발포트=29176, 운영포트=29175
							IP   = "203.234.213.51";
							PORT = 28101;// 개발포트=28101
							break;
						case 13:
							// 한신평
							IP   = "210.121.32.250";
							//PORT = 8131; // 개발포트=8131 , 운영포트=8130
							PORT = 16350;  //port : 운영 16350, 테스트 16351
							break;
						case 14:
							// KCB  (kkmin,20061228)
							IP   = "219.255.136.241";
							PORT = 28800;
							//kcbPortSeq = manager.getKcbPortSeq();                       //kkmin,20070423
							//PORT = kcbPort[kcbPortSeq];
							//System.out.println("KCB port sequence : " + kcbPortSeq);
							break;
						case 21:
							// KIB
							IP   = "192.168.1.41";
							PORT = 1911; // 개발포트=1911, 운영포트=1912 
							break;		
						case 22:
							// 대우자판
							IP   = "152.149.234.164";
							PORT = 3024; // 개발포트=3024, 운영포트=???? 
							break;
						case 31:
							// 홈페이지
							//IP   = "192.168.45.205";
							//PORT = 9010; // 개발포트=3024, 운영포트=????
							IP = "196.1.1.1";
							PORT = 39020;
						default:
							break;
					}
					
					// 외부기관에 소켓연결하고 I/O 스트림을 오픈한다.
					String sendError = "0";
					try{
						if( orgCode > 11 ){
							// 한신정,한신평,KIB 이면 새로운 소켓을 연결한다.
							System.out.println("--------------- 새로운 소켓 연결을 시작합니다.... ------------");
							System.out.println(IP + " : " + String.valueOf(PORT) + " 서버에 연결시도중.....");
							xS   = new Socket(IP,PORT);
							is   = xS.getInputStream();                            //(kkmin,20070117)
							xin  = new BufferedReader(new InputStreamReader(is));  //(kkmin,20070117)
							xout = new BufferedWriter(new OutputStreamWriter(xS.getOutputStream()));
							System.out.println(IP + " : " + String.valueOf(PORT) + " 서버에 연결했습니다.");
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println(IP + " : " + String.valueOf(PORT) + " 서버 연결이 실패했습니다.");
						sendError = "9";
					}
					
					/*
					 * 기관별로 공통헤더 엔티티 빈을 통해 헤더를 생성하고
					 * 화면에서 전송한 데이타본문의 앞에 헤더를 붙여 해당기관으로 전송한다
					 */
					MegaBoxClient mboxC = null;
					
					try{
						switch(orgCode) {
							case 11:
								/*
								 * 은행연합회
								 * 실제전송될 데이타를 생성하여 전송한다.
								 */
								/*
								 * 은행연합회전문은 데이터 전송이 없다.
								 */
								//mboxC = mboxmanager.getChannel();
								//mboxC.bankSendMsg(localRequestHeader[13]);
								/*
								 * 은행연합회전문은 데이터 전송이 없다.
								 */
								/*
								System.out.println("[데몬에서 완성된 요청전문]-----------------------------------------");
								System.out.println("-"+localRequestHeader[13]+"-");
								System.out.println("--------------------------------------------------------------");
								*/						
								break;
							case 12:
						        /* 한신정
						         * 실제전송될 데이타를 생성하여 전송한다.
						         */
								System.out.println("===== 한신정에 보내는 데이터 =======");
								System.out.println(localRequestHeader[13]);
								System.out.println("===============================");
						        sendMsg(localRequestHeader[13]);
								break;
							case 13:
								/* 한신평 *
								 * 한신평의 전문길이(4 byte)는 전문에 포함된다(전문항목의 일부이다)
								 * 즉, 전문데이타가 100 바이트이면 맨앞의 4바이트는 이 전문길이를
								 * 나타내는 스트링 0100 이 된다.
								 * 한신평 데몬은 전문수신시 맨처음 4바이트를 읽어 전문길이를 판단하고
								 * 나머지 96 바이트를 읽게 된다.
								 */
								System.out.println("===== 한신평에 보내는 데이터 =======");
								System.out.println(localRequestHeader[13]);
								System.out.println("===============================");
						        sendMsg(localRequestHeader[13]);
								break;
							case 14:   //KCB (kkmin,20061228)
								System.out.println("===== KCB에 보내는 데이터 =======");
								System.out.println(localRequestHeader[13]);
								System.out.println("===============================");
						        sendMsg(localRequestHeader[13]);
								break;
							case 21:
						        /* KIB
						         * 실제전송될 데이타를 생성하여 전송한다.
						         */
			            	    int gramLen = localRequestHeader[13].getBytes().length+4;
			            	    String strLen = Integer.toString(gramLen);
			            	    for( int i=strLen.length(); i<4 ; i++ ){
			            	    	strLen = "0"+strLen;
			            	    }
			            	    localRequestHeader[13] = strLen+localRequestHeader[13];
	
						        sendMsg(localRequestHeader[13]);
						        /*
								System.out.println("[데몬에서 완성된 요청전문 KIB]--------------------------------------");
								System.out.println("["+localRequestHeader[13]+"]");
								System.out.println("--------------------------------------------------------------["+localRequestHeader[13].getBytes().length+"]");
								*/
								break;
							case 22:
								/* 대우자판 */
						        sendMsg(localRequestHeader[13]);
						        /*
						        System.out.println("[데몬에서 완성된 요청전문 대우자판]------------------------------------");
								System.out.println("["+localRequestHeader[13]+"]"+localRequestHeader[13].getBytes().length);
								System.out.println("--------------------------------------------------------------");
								*/
								break;
							case 31:
								/* 홈페이지 */
								String sendGram = str.substring(2, str.length());
								System.out.println("총 bytes : " + str.getBytes().length);
								
								//String sendGram = str.substring(2, str.getBytes().length);
								sendMsg(sendGram);
						        System.out.println("[데몬에서 완성된 요청전문 홈페이지]------------------------------------");
								System.out.println(sendGram);
								System.out.println("--------------------------------------------------------------");
								break;	
							default:
								break;
						}
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
							switch(orgCode) {
								case 11:
									// 은행연합회
									String   strOrgMngNo = localRequestHeader[3];
									strOrgMngNo = strOrgMngNo.substring(13);
									int cnt = 0;
									/*
									 * 은행연합회 전문은 없다.
									 */
									/*while(xRcv == null || xRcv.equals("") ){
										Thread.sleep(200);cnt++;
										System.out.println("메시지큐를 읽음 [관리번호:"+strOrgMngNo+"]"+this);
										try{
											xRcv = mboxC.getQueueMsg(strOrgMngNo);
											mboxC.delQueueMsg(strOrgMngNo);
										}catch(Exception e){
											System.out.println(e.getMessage());
											break;
										}
										if( cnt > 150 ){
											// 은행연합회 신용정보 조회 타임아웃 시간인 30초가 경과하면 타임아웃 처리한다.(100ms*300=30sec)
											System.out.println("관리번호가["+strOrgMngNo+"]인 전문을 수신하지 못했습니다");
											break;
										}
									}*/
									/*
									 * 은행연합회 전문은 없다.
									 */
									/*
									 * 가상으로 만든다.
									 */
									xRcv += "   KFBCIF";	//TR코드 (9)
									xRcv += "90";			//System ID (2)
									xRcv += "373";			//대표기관코드 (3)
									xRcv += "0200";			//전문종별코드 (4)
									xRcv += "200";			//업무구분코드 (3)
									xRcv += "B";			//송수신Flag(1)
									xRcv += "   ";			//응답코드 (3)
									xRcv += "3730102";		//점포코드(7)
									xRcv += "0000001";		//금융기관전문관리번호(7)
									xRcv += "20080928171300";	// 금융기관전문전송시간(14)
									xRcv += "       ";		// 연합회전문관리번호(7)
									xRcv += "              ";	// 연합회전문전송시간(14)
									xRcv += "                ";	// 예비정보필드(16)
								
									break;
								case 12:
									// 한신정
									xRcv   = rcvNiceMsg();
									xRcv  += rcvNiceMsg();
									break;
								case 13:
									// 한신평
									xRcv   = rcvKisMsg();
									break;
								case 14:
									// KCB  (kkmin,20061228)
									xRcv   = rcvKcbMsg();   //(kkmin,20061228)
									break;
								case 21:
									// KIB
									xRcv   = rcvKibMsg();
									break;		
								case 22:
									// 대우자판
									xRcv   = rcvDwMsg();
									break;
								case 31:
									// 홈페이지
									// 홈페이지용 수신 메세지를 만들어야 함.
									xRcv   = rcvDwMsg();
									break;
								default:
									break;
							}
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
					
					if( orgCode == 11 ){
						//xRcv = new String(xRcv.getBytes("euc-kr"));
						//xRcv = new String(xRcv.getBytes("euc-kr"));
						//System.out.println("[연합회 응답전문 --------------------------------------]");
						//System.out.println("["+xRcv+"]");
						//System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
					}else if( orgCode == 12 ) {
						/*
						System.out.println("[한신정 응답전문--------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
						*/
					}else if( orgCode == 13 ) {
						/*
						System.out.println("[한신평 응답전문 --------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
						*/
					}else if( orgCode == 14 ) {
						/*
						System.out.println("[KCB 응답전문 --------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
						*/
					}else if( orgCode == 21 ) {
						/*
						System.out.println("[KIB 응답전문 ----------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[---------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
						*/
					}else if( orgCode == 22 ) {
						/*
						System.out.println("[대우자판 응답전문 ----------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[---------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
						*/
					}else if( orgCode == 31 ) {
						System.out.println("[홈페이지 응답전문 ----------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[---------------------------------------------------]["+xRcv.getBytes().length+"]바이트");
					}
						
						
					// 기관별 응답전문 공통헤더 파싱함수 호출
					if(orgCode == 21){
						xRcv = xRcv.substring(4);
					}
					sToBytes = xRcv.getBytes();
					String[] kfbHeader  = null;
					String[] niceHeader = null;
					String[] kisHeader  = null;
					String[] kibHeader  = null;
					String[] dwHeader  = null;
					String[] kcbHeader  = null;    //KCB (kkmin,20061228)
					String[] homeHeader  = null;   //홈페이지
					
					try{
						switch(orgCode) {
							case 11:
								// 은행연합회
								kfbHeader  = parseKfbHeader(xRcv);
								break;
							case 12:
								// 한신정(앞의 길이 10 바이트를 제외하고)
								niceHeader = parseNiceHeader(xRcv.substring(10));
								break;
							case 13:
								// 한신평
/*								
								if( localRequestHeader[2].trim().startsWith("SA") ){
									kisHeader  = parseKisHeaderSA(xRcv);
								}else if( localRequestHeader[2].trim().startsWith("TR") ){
									kisHeader  = parseKisHeaderTR(xRcv);
								}                                                                  //kkmin,20070131 막아버림 쓸데 없는 짓거리를 하고 있군
*/
								break;
							case 14:
								// KCB
								kcbHeader  = parseKcbHeader(xRcv);
								break;
							case 21:
								// KIB
								kibHeader  = parseKibHeader(xRcv);
								for(int i=0;i<kibHeader.length;i++) System.out.println("kibHeader["+i+"]=["+kibHeader[i]+"]len="+kibHeader[i].getBytes().length);							
								break;	
							case 22:
								// 대우자판
								dwHeader  = parseDwHeader(xRcv);
								for(int i=0;i<dwHeader.length;i++) System.out.println("dwHeader["+i+"]=["+dwHeader[i]+"]len="+dwHeader[i].getBytes().length);							
								break;
							default:
								break;
						}
					}catch(Exception e){
						System.out.println("파싱에러 - 비정상데이타 수신");
						recvError = "7";	// 비정상데이타 수신
					}
					
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
					
					String localResponseHeader = "";
					try{
						if( stsCode.equals("0")){
							switch(orgCode) {// 기관코드
								case 11:
									// 은행연합회
									localResponseHeader += localRequestHeader[0];  			// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
									localResponseHeader += "R";  							// 송수신여부(1)
									localResponseHeader += kfbHeader[3];					// 전문구분코드(4)
									localResponseHeader += kfbHeader[4]+" ";				// 거래구분코드(4)
									localResponseHeader += "00000000000000000000"; 			// 관리번호(20)
									localResponseHeader += "0000000000";					// 거래일련번호(10)
									localResponseHeader += kfbHeader[6]+" ";				// 응답코드(4)
									localResponseHeader += kfbHeader[9].substring(0,8);		// 거래일자(8)
									localResponseHeader += kfbHeader[9].substring(8,14);	// 거래시간(6)
									localResponseHeader += "0000";							// 데이터길이(4)
									localResponseHeader += "11111111";						// 처리단말번호(8)
									localResponseHeader += "22222222";						// 단말등록사원번호(8)
									localResponseHeader += stsCode;							// 상태코드(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// 예비정보내역(20)
									localResponseHeader += xRcv;								
									break;
								case 12:
									// 한신정
									localResponseHeader += localRequestHeader[0];  			// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
									localResponseHeader += "R";  							// 송수신여부(1)
									localResponseHeader += niceHeader[1];					// 전문구분코드(4)
									localResponseHeader += niceHeader[2].substring(0,4);	// 거래구분코드(4)
									localResponseHeader += "00000000000000000000"; 			// 관리번호(20)
									localResponseHeader += "0000000000";					// 거래일련번호(10)
									localResponseHeader += niceHeader[5];					// 응답코드(4)
									localResponseHeader += niceHeader[10].substring(0,8);	// 거래일자(8)
									localResponseHeader += niceHeader[10].substring(8,14);	// 거래시간(6)
									localResponseHeader += "0000";							// 데이터길이(4)
									localResponseHeader += "11111111";						// 처리단말번호(8)
									localResponseHeader += "22222222";						// 단말등록사원번호(8)
									localResponseHeader += stsCode;							// 상태코드(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// 예비정보내역(20)
									localResponseHeader += xRcv;							// 데이타본문(응답전문)(가변)
									break;
								case 13:
									// 한신평
/*
									if( localRequestHeader[2].trim().startsWith("SA") ){
										localResponseHeader += localRequestHeader[0];  			// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
										localResponseHeader += "R";  							// 송수신여부(1)
										localResponseHeader += kisHeader[2]+"  ";				// 전문구분코드(4)
										localResponseHeader += kisHeader[1];					// 거래구분코드(4)
										localResponseHeader += "00000000000000000000"; 			// 관리번호(20)
										localResponseHeader += "0000000000";					// 거래일련번호(10)
										localResponseHeader += kisHeader[8]+"  ";				// 응답코드(4)
										localResponseHeader += "YYYY"+kisHeader[6].substring(0,4);// 거래일자(8)
										localResponseHeader += kisHeader[6].substring(4,10);	// 거래시간(6)
										localResponseHeader += "0000";							// 데이터길이(4)
										localResponseHeader += "11111111";						// 처리단말번호(8)
										localResponseHeader += "22222222";						// 단말등록사원번호(8)
										localResponseHeader += stsCode;							// 상태코드(1)
										localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// 예비정보내역(20)
										localResponseHeader += xRcv;							// 데이타본문(응답전문)(가변)
									}else if( localRequestHeader[2].trim().startsWith("TR") ){
										localResponseHeader += localRequestHeader[0];  			// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
										localResponseHeader += "R";  							// 송수신여부(1)
										localResponseHeader += kisHeader[2]+"  ";				// 전문구분코드(4)
										localResponseHeader += kisHeader[1];					// 거래구분코드(4)
										localResponseHeader += "00000000000000000000"; 			// 관리번호(20)
										localResponseHeader += "0000000000";					// 거래일련번호(10)
										localResponseHeader += kisHeader[6]+"  ";				// 응답코드(4)
										localResponseHeader += "YYYY"+kisHeader[4].substring(0,4);// 거래일자(8)
										localResponseHeader += kisHeader[4].substring(4,10);	// 거래시간(6)
										localResponseHeader += "0000";							// 데이터길이(4)
										localResponseHeader += "11111111";						// 처리단말번호(8)
										localResponseHeader += "22222222";						// 단말등록사원번호(8)
										localResponseHeader += stsCode;							// 상태코드(1)
										localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// 예비정보내역(20)
										localResponseHeader += xRcv;							// 데이타본문(응답전문)(가변)
									}                                                           // kkmin,20070131 쓸데없는 짓거리를 하고 있군.
*/
									localResponseHeader += fillPkString(localRequestHeader[0],100);	// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)(TS10전문추가,kkmin,20070131)
									localResponseHeader += xRcv;					                // 데이타본문(응답전문)(가변)(TS10전문추가,kkmin,20070131)
									break;
								case 14:
									// KCB (kkmin,20061228)
									localResponseHeader += fillPkString(localRequestHeader[0],100);	// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
									localResponseHeader += xRcv;					// 데이타본문(응답전문)(가변)
									manager.setKcbPortSeq(kcbPortSeq); //KCB port deallocation(kkmin,20070423)
									System.out.println("KCB port deallocation sequence : " + kcbPortSeq);  //kkmin,20070423
									break;
								case 21:
									// KIB
									localResponseHeader += localRequestHeader[0];  	// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
									localResponseHeader += "R";  					// 송수신여부(1)
									localResponseHeader += kibHeader[10];			// 전문구분코드(4)
									localResponseHeader += kibHeader[11];			// 거래구분코드(4)
									localResponseHeader += "00000000000000000000"; 	// 관리번호(20)
									localResponseHeader += "0000000000";			// 거래일련번호(10)
									localResponseHeader += "0000";					// 응답코드(4)
									localResponseHeader += "20060102";				// 거래일자(8)
									localResponseHeader += "101010";				// 거래시간(6)
									localResponseHeader += "0000";					// 데이터길이(4)
									localResponseHeader += "11111111";				// 처리단말번호(8)
									localResponseHeader += "22222222";				// 단말등록사원번호(8)
									localResponseHeader += stsCode;					// 상태코드(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";	// 예비정보내역(20)
									localResponseHeader += xRcv;					// 데이타본문(응답전문)(가변)
									break;	
								case 22:
									// 대우자판
									localResponseHeader += localRequestHeader[0];  	// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
									localResponseHeader += "R";  					// 송수신여부(1)
									localResponseHeader += localRequestHeader[2];	// 전문구분코드(4)+거래구분코드(4)
									localResponseHeader += "00000000000000000000"; 	// 관리번호(20)
									localResponseHeader += "0000000000";			// 거래일련번호(10)
									localResponseHeader += "0000";					// 응답코드(4)
									localResponseHeader += "20060102";				// 거래일자(8)
									localResponseHeader += "101010";				// 거래시간(6)
									localResponseHeader += "0000";					// 데이터길이(4)
									localResponseHeader += "11111111";				// 처리단말번호(8)
									localResponseHeader += "22222222";				// 단말등록사원번호(8)
									localResponseHeader += stsCode;					// 상태코드(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";	// 예비정보내역(20)
									localResponseHeader += xRcv;					// 데이타본문(응답전문)(가변)
									break;
								case 31:
									// 홈페이지
									localResponseHeader += xRcv;					// 응답전문
									break;
								default:
									break;
							}
						}else{
							// 송,수신 오류 및 비정상수신
							localResponseHeader += localRequestHeader[0];  	// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
							localResponseHeader += "R";  					// 송수신여부(1)
							localResponseHeader += localRequestHeader[2];	// 전문구분코드(4)+거래구분코드(4)
							localResponseHeader += "00000000000000000000"; 	// 관리번호(20)
							localResponseHeader += "0000000000";			// 거래일련번호(10)
							localResponseHeader += "0000";					// 응답코드(4)
							localResponseHeader += "20060428";				// 거래일자(8)
							localResponseHeader += "101010";				// 거래시간(6)
							localResponseHeader += "0000";					// 데이터길이(4)
							localResponseHeader += "11111111";				// 처리단말번호(8)
							localResponseHeader += "22222222";				// 단말등록사원번호(8)
							localResponseHeader += stsCode;					// 상태코드(1)
							localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";	// 예비정보내역(20)
							localResponseHeader += xRcv;					// 데이타본문(응답전문)(가변)
							
							System.out.println("수신오류시 리턴응답:"+localResponseHeader);

							if (orgCode == 14){
								manager.setKcbPortSeq(kcbPortSeq); //KCB port deallocation(kkmin,20070423)
								System.out.println("KCB port deallocation sequence : " + kcbPortSeq);  //kkmin,20070423
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}

					localResponseHeader = getStrLength(localResponseHeader.getBytes().length) + localResponseHeader;

					
					System.out.println(">>>>> sendError="+sendError+", recvError="+recvError+", stsCode="+stsCode+" <<<<<");

					// 로컬응답을 리턴한다.
					try{
						out.write(localResponseHeader);
						out.flush();
					}catch(Exception e){
						e.printStackTrace();
					}
					
					if( orgCode > 11 ) {
						try{
							xin.close();
							xout.close();							
							xS.close();				
						}catch(Exception e){
							if( orgCode == 12 ){
								System.out.println("한신정전문 송수신후 소켓종료중 오류:"+e.getMessage());
							}
							if( orgCode == 13 ){
								System.out.println("한신평전문 송수신후 소켓종료중 오류:"+e.getMessage());
							}
							if( orgCode == 14 ){        //KCB (kkmin,20061228)
								System.out.println("KCB 전문 송수신후 소켓종료중 오류:"+e.getMessage());
							}
							if( orgCode == 21 ){
								System.out.println("KIB  전문 송수신후 소켓종료중 오류:"+e.getMessage());
							}	
							if( orgCode == 22 ){
								System.out.println("대우자판  전문 송수신후 소켓종료중 오류:"+e.getMessage());
							}
							if( orgCode == 31 ){
								System.out.println("홈페이지  전문 송수신후 소켓종료중 오류:"+e.getMessage());
							}
						}						
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
				System.out.println("========== 버퍼 Write Start ==================================");
				xout.write(command);
				System.out.println("========== 버퍼 Write End  ===================================");
				xout.flush();
				System.out.println("========== 버퍼 frush End  ===================================");
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
		 * 외부기관 응답을 받아들인다.한신정
		 * @return a String
		 */
		public synchronized String rcvNiceMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");

			try {
				int response;
				int i = 0;
				int totCnt = 0;
				char[] cTot = new char[10];

				if (xin.read(cTot) != -1) {
					rtnStr.append(new String(cTot));
					totCnt = StringUtil.cInt(StringUtil.CHSTRING(rtnStr.toString(), " ", "0"));

					for (i = 0; i < totCnt; i++) {
						response = xin.read();

						if (response > 255) {
							i++;
						}

						rtnStr.append((char) response);
					}
				}
				System.out.println("======== 한신정에서 받은 전문 ===================");
				System.out.println(rtnStr.toString());
				System.out.println("============================================");
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

		/**
		 * 외부기관 응답을 받아들인다.한신평
		 * @return a String
		 */
		public synchronized String rcvKisMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");

			try {
				int response;
				int i = 0;
				int totCnt = 0;
				char[] cTot = new char[4];

				if (xin.read(cTot) != -1) {
					rtnStr.append(new String(cTot));
					totCnt = StringUtil.cInt(StringUtil.CHSTRING(
								rtnStr.toString(), " ", "0"));

					// 한신평은 전문길이가 전문에 포함되므로 이미꺼낸 4 byte 는 빼고 꺼낸다. 
					for (i = 0; i < (totCnt - 4); i++) {
						response = xin.read();

						if (response > 255) {
							i++;
						}

						rtnStr.append((char) response);
					}
				}
			} catch (java.net.SocketException se) {
				throw new IOException(se.getMessage());
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {

			}
			
			System.out.println("======== 한신평에서 받은 전문 ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();			
		}
		/*--------------------------------------------------------------------------------------------*/

		/**
		 * 외부기관 응답을 받아들인다.KCB
		 * @return a String
		 */
		public synchronized String rcvKcbMsg() throws IOException {   //KCB 추가(kkmin,20070104)
			StringBuffer rtnStr = new StringBuffer("");
			int response = 0;
 			int i = 0;
			int [] iTot = new int[5];
			byte[] bTot = new byte[4];
			char[] cTot = new char[4];   //kkmin,20070528

			try {
/*				
				if (is.read(bTot) > 0){ 
					iTot[0] = ((bTot[0]<0)?bTot[0]+256:bTot[0]) << 24; 
					iTot[1] = ((bTot[1]<0)?bTot[1]+256:bTot[1]) << 16; 
					iTot[2] = ((bTot[2]<0)?bTot[2]+256:bTot[2]) << 8; 
					iTot[3] = ((bTot[3]<0)?bTot[3]+256:bTot[3]) << 0; 
					iTot[4] = iTot[0] + iTot[1] + iTot[2] + iTot[3];
*/
				if (xin.read(cTot) != -1){                         //kkmin,20070528
					iTot[4] = Integer.parseInt(new String(cTot));  //kkmin,20070528
					System.out.println("==== KCB 전문길이 : " + String.valueOf(iTot[4]) + " ====");
					rtnStr.append(fillPkNumber(String.valueOf(iTot[4]),4));
					for (i = 0; i < (iTot[4]); i++) {
						response = xin.read();
						if (response > 255) {i++;}      //한글인경우 2byte처리
						rtnStr.append((char)response);
					}
				}

            } catch (java.net.SocketException se) {
				throw new IOException(se.getMessage());
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {

			}
			
			System.out.println("======== KCB에서 받은 전문 ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();			
		}
		/*--------------------------------------------------------------------------------------------*/

		/**
		 * 외부기관 응답을 받아들인다. KIB
		 * @return a String
		 */
		public synchronized String rcvKibMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");
			byte[] bTot = new byte[4];   //kkmin temp

			try {
				int response;
				int i = 0;
				int totCnt = 0;
				char[] cTot = new char[4];

				if (xin.read(cTot) != -1) {   
					rtnStr.append(new String(cTot));
					  
					totCnt = StringUtil.cInt(StringUtil.CHSTRING(
								rtnStr.toString(), " ", "0"));

					// KIB는 전문길이가 전문에 포함되므로 이미꺼낸 4 byte 는 빼고 꺼낸다. 
					for (i = 0; i < (totCnt - 4); i++) {
						response = xin.read();

						if (response > 255) {
							i++;
						}

						rtnStr.append((char) response);
					}
				}
			} catch (java.net.SocketException se) {
				throw new IOException(se.getMessage());
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {

			}
			
			System.out.println("======== KIB에서 받은 전문 ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();			
		}
		
		/**
		 * 외부기관 응답을 받아들인다. 홈페이지
		 * @return a String
		 */
		public synchronized String rcvDwMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");
			byte[] bTot = new byte[4];   //kkmin temp

			try {
				int response;
				int i = 0;
				int totCnt = 0;
				char[] cTot = new char[4];

				if (xin.read(cTot) != -1) {   
					rtnStr.append(new String(cTot));
					  
					totCnt = StringUtil.cInt(StringUtil.CHSTRING(
								rtnStr.toString(), " ", "0"));

					// 홈페이지는 전문길이 + 헤더부(260)
					for (i = 0; i < ((totCnt+260) - 4); i++) {
						response = xin.read();

						if (response > 255) {
							i++;
						}

						rtnStr.append((char) response);
					}
				}
			} catch (java.net.SocketException se) {
				throw new IOException(se.getMessage());
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {

			}
			
			System.out.println("======== 홈페이지에서 받은 전문 =================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();			
		}
		
		
		
		/*
		public synchronized String rcvDwMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");

			try {
				char[] cTot = new char[126];

				if (xin.read(cTot) != -1) {
					rtnStr.append(new String(cTot));
				}
			} catch (java.net.SocketException se) {
				throw new IOException(se.getMessage());
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {

			}
			
			System.out.println("======== 홈페이지에서 받은 전문 ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();
		}	
		*/	
		
		/*
		 * 로컬공통헤더 파싱
		 * @param String 로컬공통헤더
		 * @return String[] 로컬공통헤더 항목배열
		 * Comment 화면에서 작성해 넘어온 로컬헤더+본문전문을 파시하여 스트링배열로 리턴한다. 
		 */
		public String[] parseOnlineCommonHeader(String req) throws Exception {
			String[] attr = new String[14];

			try{
				attr[0] = subString(0,2);		// 대외기관코드(2)
				
				// 홈페이지 연동은 로직을 따로 태운다.
				System.out.println("헤더와 본문을 분리합니다.");
				if(attr[0].equals("31")) {
					attr[1] = subString(2,6);		// 전문개별부길이(4)
					attr[2] = subString(6,14);		// 처리일자(8)
					attr[3] = subString(14,20);		// 처리시간(6)
					attr[4] = subString(20,28);		// 전문일련번호(8)
					attr[5] = subString(28,32);		// 메시지구분(4)
					attr[6] = subString(32,36);		// 업무코드(4)
					attr[7] = subString(36,40);		// 응답코드(4)
					attr[8] = subString(40,240);	// 응답메세지(200)
					attr[9] = subString(240,262);	// FILLER(22)
					attr[10] = subString(262,sToBytes.length); // 데이타본문
				} else {
					attr[1] = subString(2,3);		// 송수신 여부(1)
					attr[2] = subString(3,11);		// 거래구분코드(8)
					attr[3] = subString(11,31);		// 관리번호(20)
					attr[4] = subString(31,41);		// 거래일련번호(10)
					attr[5] = subString(41,45);		// 응답코드(4)
					attr[6] = subString(45,53);		// 거래일자(8)
					attr[7] = subString(53,59);		// 거래시간(6)
					attr[8] = subString(59,63);		// 데이터 길이(4)
					attr[9] = subString(63,71);		// 처리단말번호(8)
					attr[10] = subString(71,79);	// 단말등록 사원번호(8)
					attr[11] = subString(79,80);	// 상태코드(1)
					attr[12] = subString(80,100);	// 예비정보내역(20)
					attr[13] = subString(100,sToBytes.length);		// 데이타본문
				}
				System.out.println("헤더와 본문을 분리완료했습니다.");
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}
			
			return attr;
		}

		/*
		 * 한신정 전문중 공통헤더를 파싱
		 * @param String 한신정 전문
		 * @return String[] 한신정 공통헤더 항목배열
		 * Comment : 한신정 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */
		public String[] parseNiceHeader(String req) throws Exception {
			String[] attr = new String[13];
			
			try{
				attr[0] = subString(0,9);		// 전문송신기관코드(9)
				attr[1] = subString(9,13);		// 전문구분코드(4)
				attr[2] = subString(13,18);		// 거래구분코드(5)
				attr[3] = subString(18,19);		// 송수신플래그(1)
				attr[4] = subString(19,22);		// 단말기구분(3)
				attr[5] = subString(22,26);		// 응답코드(4)
				attr[6] = subString(26,35);		// 참가기관ID(9)
				attr[7] = subString(35,45);		// 기관전문관리번호(10)
				attr[8] = subString(45,59);		// 기관전문전송시간(14)
				attr[9] = subString(59,69);		// NICE전문관리번호(10)
				attr[10] = subString(69,83);	// NICE전문전송시간(14)
				attr[11] = subString(83,99);	// Primary Bitmap(16)
				attr[12] = subString(99,100);	// 공란(Extend Bitmap Code)(1)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}		
		
		/*
		 * 한신평 전문중 공통헤더를 파싱
		 * @param String 한신평 전문
		 * @return String[] 한신평 공통헤더 항목배열
		 * Comment : 한신평 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */		
		public String[] parseKisHeaderSA(String req) throws Exception {
			String[] attr = new String[10];
	
			try{
				attr[0]  = subString(0,4);		// 전문길이(4)
				attr[1]  = subString(4,8);		// TX-ID(4)
				attr[2]  = subString(8,10);		// 전문구분코드(2)
				attr[3]  = subString(10,17);	// 회원코드(7)
				attr[4]  = subString(17,47);	// 지점명(30)
				attr[5]  = subString(47,57);	// 담당자ID(10)
				attr[6]  = subString(57,67);	// 거래일시(10)
				attr[7]  = subString(67,127);	// 사용자AREA(60)
				attr[8]  = subString(127,129);	// 응답코드(2)
				attr[9]  = subString(129,149);	// 응답메시지(20)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}
		
		/*
		 * 한신평 전문중 공통헤더를 파싱
		 * @param String 한신평 전문
		 * @return String[] 한신평 공통헤더 항목배열
		 * Comment : 한신평 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */		
		public String[] parseKisHeaderTR(String req) throws Exception {
			String[] attr = new String[8];
	
			try{
				attr[0]  = subString(0,4);		// 전문길이(4)
				attr[1]  = subString(4,8);		// TX-ID(4)
				attr[2]  = subString(8,10);		// 전문구분코드(2)
				attr[3]  = subString(10,17);	// 회원코드(7)
				attr[4]  = subString(17,27);	// 거래일시(10)
				attr[5]  = subString(27,87);	// 사용자AREA(60)
				attr[6]  = subString(87,89);	// 응답코드(2)
				attr[7]  = subString(89,109);	// 응답메시지(20)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}		
		
		/*
		 * KCB 공통헤더를 파싱
		 * @param String KCB 전문
		 * @return String[] KCB 공통헤더 항목배열
		 * Comment : KCB 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */
		public String[] parseKcbHeader(String req) throws Exception {
			String[] attr = new String[13];
			int totlByte = 0;
			try{
		    	attr[0]  = (req.substring(totlByte,totlByte+4));
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}		

		/*
		 * KIB 전문중 공통헤더를 파싱
		 * @param String KIB 전문
		 * @return String[] KIB 공통헤더 항목배열
		 * Comment : KIB 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */
		public String[] parseKibHeader(String req) throws Exception {
			String[] attr = new String[28];
			
			try{
				attr[0]   = subString(0,4);		// 전문길이(4)- 전문내부의 길이(의미없슴)
				attr[1]   = subString(4,8);		// CS번호(4)
				attr[2]   = subString(8,16);	// CS관리 기관코드(8)
				attr[3]   = subString(16,17);	// REACTION CODE(1)
				attr[4]   = subString(17,20);	// 연속거래번호(3)
				attr[5]   = subString(20,21);	// 송수신FLAG(1)
				attr[6]   = subString(21,29);	// 취급기관코드(8)
				attr[7]   = subString(29,33);	// 취급영업점코드(4)
				attr[8]   = subString(33,38);	// 취급단말코드(5)
				attr[9]   = subString(38,39);	// 매체(발생)구분(1)
				attr[10]  = subString(39,43);	// 전문구분코드(MSG TYPE)(4)
				attr[11]  = subString(43,47);	// 거래구분코드(4)
				attr[12]  = subString(47,50);	// 응답코드(3)
				attr[13]  = subString(50,58);	// 거래일자(8)
				attr[14]  = subString(58,64);	// 거래시간(6)
				attr[15]  = subString(64,71);	// 거래일련번호(7)
				attr[16]  = subString(71,72);	// 한글코드구분(1)
				attr[17]  = subString(72,73);	// 마감후구분(1)
				attr[18]  = subString(73,81);	// 개설기관코드(8)
				attr[19]  = subString(81,82);	// M/S TRACK번호(1)
				attr[20]  = subString(82,223);	// M/S TRACK DATA(141)
				attr[21]  = subString(223,224);	// 카드구분(1)
				attr[22]  = subString(224,225);	// 정형화된 조회 출력 구분(1)
				attr[23]  = subString(225,226);	// 이체거래시 입출기관구분(1)
				attr[24]  = subString(226,240);	// USER WORK AREA(14)
				attr[25]  = subString(240,280);	// 응답 MESSAGE(40)
				attr[26]  = subString(280,286);	// 원거래요소(6)
				attr[27]  = subString(286,300);	// FILLER(14)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}
		
		/*
		 * 대우자판 전문중 공통헤더를 파싱
		 * @param String 대우자판 전문
		 * @return String[] 대우자판 공통헤더 항목배열
		 * Comment : 대우자판 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */
		public String[] parseDwHeader(String req) throws Exception {
			String[] attr = new String[16];
			StringTokenizer dwGram = new StringTokenizer(req,"|");
			try{
				/*
				attr[0]   // 구분코드(8)
				attr[1]   // 오류코드(4)
				attr[2]   // 오류내용(10)
				attr[3]   // 처리일자(8)
				attr[4]   // 처리시간(6)
				attr[5]   // 공백(6)
				attr[6]   // 루프여부(1)
				attr[7]   // 루프건수(3)
				attr[8]   // 취소여부(1)
				attr[9]   // 회계일자(8)
				attr[10]  // 결제일자(8)
				attr[11]  // 현금금액(13)
				attr[12]  // 자판거점코드(6)
				attr[13]  // 출고번호(10)
				attr[14]  // 입금표번호(10)
				attr[15]  // 사원번호(8)
				*/
				int i=0;
				while(dwGram.hasMoreTokens()){
					attr[i++] = dwGram.nextToken();
				}
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}
		
		/*
		 * 은행연합회 전문중 공통헤더를 파싱
		 * @param String 은행연합회 전문
		 * @return String[] 은행연합회 공통헤더 항목배열
		 * Comment : 은행연합회 전문의 공통헤더 부분을 개별항목으로 분리한다.
		 */		
		public String[] parseKfbHeader(String req) throws Exception {
			String[] attr = new String[13];
			
			try{
				attr[0]  = subString(0,9);		// TRANSACTION CODE(9)
				attr[1]  = subString(9,11);		// SYSTEM-ID(2)
				attr[2]  = subString(11,14);	// 대표기관코드(3)
				attr[3]  = subString(14,18);	// 전문종별코드(4)
				attr[4]  = subString(18,21);	// 업무구분코드(3)
				attr[5]  = subString(21,22);	// 송수신FLAG(1)
				attr[6]  = subString(22,25);	// 응답코드(3)
				attr[7]  = subString(25,32);	// 점포코드(7)
				attr[8]  = subString(32,39);	// 금융기관전문관리번호(7)
				attr[9]  = subString(39,53);	// 금융기관전문전송시간(14)
				attr[10] = subString(53,60);	// 연합회전문관리번호(7)
				attr[11] = subString(60,74);	// 연합회전문전송시간(14)
				attr[12] = subString(74,90);	// 예비정보필드(16)
				/*
				System.out.println("KFB HEADER BEGIN============================");
				System.out.println("["+attr[0]	+"]");	// TRANSACTION CODE(9)
				System.out.println("["+attr[1]  +"]");	// SYSTEM-ID(2)
				System.out.println("["+attr[2]  +"]");	// 대표기관코드(3)
				System.out.println("["+attr[3]  +"]");	// 전문종별코드(4)
				System.out.println("["+attr[4]  +"]");	// 업무구분코드(3)
				System.out.println("["+attr[5]  +"]");	// 송수신FLAG(1)
				System.out.println("["+attr[6]  +"]");	// 응답코드(3)
				System.out.println("["+attr[7]  +"]");	// 점포코드(7)
				System.out.println("["+attr[8]  +"]");	// 금융기관전문관리번호(7)
				System.out.println("["+attr[9]  +"]");	// 금융기관전문전송시간(14)
				System.out.println("["+attr[10] +"]");	// 연합회전문관리번호(7)
				System.out.println("["+attr[11] +"]");	// 연합회전문전송시간(14)
				System.out.println("["+attr[12] +"]");	// 예비정보필드(16)
				System.out.println("KFB HEADER END==============================");
				*/
			}catch(Exception e){
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