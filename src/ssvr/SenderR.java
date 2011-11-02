/*
 * ��ܰ� �¶��� �۽��� �����̴�.
 * 
 * ��ܱ���� ���������� ������ �ְ�޴� �����̴�.
 * Ŭ���̾�Ʈ(ȭ���)�� ��û�� �޾� ���ð�������� �Ľ��Ѵ�.
 * �Ľ̵� ��������� ��������� ���� �ش������� �����Ѵ�.
 * �� ������ �ѽ���,�ѽ���,KIB,������� ����� ���� ���������� �۵��Ѵ�.
 * �� ������ ���࿬��ȸ�� ���� �񵿱������� �۵��Ѵ�.
 * �������� ��û������ ������� �����ϰ� ���������� �ö����� ������ ���ŷ ���¿� ����.
 * �񵿱����� ��û������ ������� ������ �����ϰ� ���������� �޽���ť���� ������ȣ�� ��ġ�Ѵ�.
 * ������ Ÿ�Ӿƿ�(Time-out) �ð��� ����ϸ� Time-out Exception �� �߻���Ű��
 * ��û������ ������ Ŭ���̾�Ʈ�� ��ֻ��¸� �ڵ�� �����Ѵ�.
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
	 * ������
	 * Connection �����ڸ� �����ϰ� �����Ѵ�.
	 * Listener �� �����ϰ� �����Ѵ�.
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
	 * ������ ���� �����Լ�
	 */
	public static void main(String[] args) throws Exception {
		try{
			if(args[0] == null) 
				SenderR.sPORT = 9002;
			else
				SenderR.sPORT = Integer.parseInt(args[0]);
			SenderR server = new SenderR();
			System.out.println("=========================================================");
			System.out.println("���������� �����Ǿ����ϴ�......");
			System.out.println("=========================================================");
		}catch(IOException ie){
			System.out.println("�ް��ڽ�(MEGABOX)��� ���ӿ� ������ �߻��Ͽ� ������ �⵿�Ҽ� �����ϴ�.");
			System.out.println("=========================================================");
			System.out.println(ie.getMessage());
			System.out.println("=========================================================");
			System.out.println("�ް��ڽ��� ������ ��ɸ� �����Ͻñ� �ٶ��ϴ�.");
			//System.exit(-1);
		}catch(Exception e){
			System.out.println("������ �⵿�ϴ��� ������ �߻��Ͽ� ������ �⵿�Ҽ� �����ϴ�.");
			System.out.println("=========================================================");
			System.out.println(e.getMessage());
			System.out.println("=========================================================");			
			System.out.println("������ �����Ͽ����ϴ�.");
			System.exit(-1);			
		}
	}

	/**
	 * �űԿ�û
	 * ���������� ��ٸ���.
	 * ���ӵ� ������ Connection Manager �� ����Ѵ�.
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
	 * ��û ����
	 * ���������� �����Ѵ�.
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
							System.out.println("Ŭ���̾�Ʈ ä���� ����Ǿ����ϴ�.");
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
	 * ��û ����
	 * ���������� �����Ѵ�.
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

			System.out.println("MEGA BOX["+0+"]��° ä���� �Ҵ�Ǿ����ϴ�");
			return (MegaBoxClient)megaboxclients.get(0);
		}

		public void run() {
			MegaBoxClient megaboxclient = null;
			for(;;) {
				try {
					for(int i = 0; i < megaboxclients.size(); i++) {
						megaboxclient = (MegaBoxClient)megaboxclients.elementAt(i);
						
						if(megaboxclient.bank_xS == null) {
							System.out.println("�ް��ڽ� ä���� ����Ǿ����ϴ�:"+megaboxclient.PORT);
							megaboxclients.remove(i);
							Thread.sleep(1000);
							makeChannel(megaboxclient.PORT);
							System.out.println("�ް��ڽ� ä���� �����Ǿ����ϴ�:"+megaboxclient.PORT);
							break;
						}
					}
					
					Thread.sleep(1000);
				} catch(Exception e) {
					System.out.println("�ް��ڽ� ä�ο� �����Ҽ� �����ϴ�:"+megaboxclient.PORT);
					System.out.println("�޽���:"+e.getMessage());
				}
			}
		}
	}	

	class MegaBoxClient extends Thread {
		// �ް��ڽ��� �¶��ε����� ��������� ������ ���´�.
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
		private String IP   = "152.149.46.138";     //��Ʈ��ũ �����۾����� ���� 152.149.138.250 -> 152.149.46.138(kkmin,20070104)

		private int    PORT = 0;

		// ������Ʈ���� �����ϴ� ����Ʈ�迭
		private byte[] byteArray = null;
		
		// MEGABOX I/O ��Ʈ��
		MegaBoxClient(int PORT) throws Exception {
			try{
				this.PORT = PORT;
				
				if( bank_xS == null ) {
					bank_xS   = new Socket(IP,PORT);
					bank_xin  = new DataInputStream(bank_xS.getInputStream());                  //kkmin,20070326
					bank_xout = new DataOutputStream(bank_xS.getOutputStream());			
					bank_xS.setKeepAlive(true);
					System.out.println("MEGABOX �������ӿϷ�:"+PORT);
					setEOR();
				}
			}catch(Exception e){
				System.out.println("MEGABOX �������ӿ���:"+PORT);
				throw e;
			}			
		}

		/* 
		 * ���� 08:30 - 09:30 ���̿� ���������� �ֱ������� Polling �Ѵ�.
		 * ���������� ���� ������ ��� 09:30 �к��ʹ� ����ȸ�� ������ �����Ѵ�.
		 * ���������� �޾������ �������������� �ۼ��ϰ� �����Ѵ�.
		 */
		public void run(){
			String   recvGram = "";
			String   sendGram = ""; 
			String[] openGram = null;

			int cTime	= 0;
			
			for(;;){
				try{
					// ���� ����
					recvGram = rcvKfbMsg();
					if( !recvGram.equals("")) {
						openGram = parseKfbHeader(recvGram);
						// ���� �Ľ�
						if( openGram[3].equals("0800") && openGram[4].equals("100")) {
							// ���������̸� �ش簪�� �ٲپ� �������Ѵ�.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("���࿬��ȸ �ſ�������ȸ ���񽺰� ���õǾ����ϴ�.");
							
							SVC_START = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("101")) {
							// �簳�������̸� �ش簪�� �ٲپ� �������Ѵ�.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("���࿬��ȸ �ſ�������ȸ ���񽺰� �簳�õǾ����ϴ�.");
							
							SVC_START = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("200")) {
							// ���Ό���̸� �ش簪�� �ٲپ� �������Ѵ�.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("���࿬��ȸ �ſ�������ȸ ���񽺰� ���Ό�� �Ǿ����ϴ�.");
							
							SVC_START = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("201")) {
							// ���������̸� �ش簪�� �ٲپ� �������Ѵ�.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("���࿬��ȸ �ſ�������ȸ ���񽺰� ���� �Ǿ����ϴ�.");
							
							SVC_CLOSE = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("400")) {
							// ��������̸� �ش簪�� �ٲپ� �������Ѵ�.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("���࿬��ȸ �ſ�������ȸ ���񽺰� ��ֻ����Դϴ�.");
							
							SVC_CLOSE = true;
						}else if( openGram[3].equals("0800") && openGram[4].equals("500")) {
							// ���������̸� �ش簪�� �ٲپ� �������Ѵ�.
							openGram[3] = "0810";
							openGram[5] = "B";
							openGram[6] = "000";
							openGram[9] = getUnformatTimeString();
							for(int j=0;j<openGram.length;j++){
								sendGram += openGram[j];
							}

							bankSendMsg(sendGram);
							System.out.println("���࿬��ȸ �ſ�������ȸ ���񽺰� ��ָ� ȸ���Ͽ����ϴ�.");
							
							SVC_CLOSE = true;
						}else{
							// ������û ī��Ʈ ����
							//reqCount++;
							//System.out.println(this+":��ûī��Ʈ:"+reqCount);
							
							// ������û ī��Ʈ ����
							//reqCount--;
							//System.out.println(this+":����ī��Ʈ:"+reqCount);
							
							System.out.println("�޽���ť�� ���� [���������ڵ�:"+openGram[3]+"][����������ȣ:"+openGram[8]+"]");
							gramQueue.put(openGram[8],recvGram);
						}
					}

					cTime   = Integer.parseInt(getShortTimeString());
				}catch(Exception e){
					System.out.println("���࿬��ȸ �ſ�������ȸ �������� ó���� ������ �߻��Ͽ����ϴ�.");
					System.out.println("��������["+recvGram+"]");
					e.printStackTrace();
				}
				
				try{
					// 0.1 �� ������ �����Ѵ�.
					Thread.sleep(100);
				}catch(Exception e){
					;
				}
			}
			// end for
		}
		// end run()

		/**
		 * �ð��� HHmmss ������ ���ڿ��� ���
		 * @return a String
		 */
	    public String getShortTimeString()
	    {
	        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss", Locale.KOREA);
	        return formatter.format(new Date());
	    }
	    
		/**
		 * ���ڸ� yyyy-MM-dd-HH:mm:ss:SSS ������ ���ڿ��� ���
		 * @return a String
		 */
	    public String getTimeStampString()
	    {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS", Locale.KOREA);
	        return formatter.format(new Date());
	    }
	    
		/**
		 * ���ڸ� yyyyMMddHHmmss ������ ���ڿ��� ���
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
						System.out.println("MEGABOX �� EOR MODE �� �۵�����:"+PORT);
					}else{
						System.out.println("MEGABOX �� EOR MODE �� �۵�����:"+PORT);
					}
				}
			}catch(Exception e){
				System.out.println("MEGABOX ��� EOR MODE �� ��ȯ�� �����߻�:"+ PORT + ":" + e.toString());
			}
		}
		
		public synchronized String getGramMesg(String gram) {
			// �ܺα������ �޴������� �����ϴ� ����
			String xRcv = "";
			
			try {
		
				try{
					// �������� ����
					bankSendMsg(gram);
				}catch(Exception e){
					sendError = "9";		// �۽źҰ�
				}
				
				if(!sendError.equals("9")){
					try{
						xRcv   = rcvKfbMsg();
					}catch(Exception e){
						recvError = "8";	// ���źҰ�.
					}
				}
				
			} catch(Exception e) {
				try {
					// �ܺμ��� ����
					bank_xin.close();         
					bank_xout.close();					
					bank_xS.close();
					bank_xS = null;
					System.out.println("��� ������ �߻��Ͽ� MEGA BOX ���������� �����Ͽ����ϴ�");
				} catch (Exception ex) {
					;
				}
			} finally {
		
			}
			
			return xRcv;
		}
		
		/*
		 * ó����û ī��Ʈ�� �����Ѵ�.
		 */
		public long getReqCount() {
			return reqCount;
		}

		/**
		 * ���࿬��ȸ�� ������ �����Ѵ�.
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
				 * �����̴��ݰ� ���࿬��ȸ(WAN)���뼱(56K)�� �� �ӵ����̰� �ʹ����� ó�� �ް��ڽ��� 
				 * ������ �ξ����� �ణ�� �����ð��� �ְ� ������ ������ �Ѵ�.�׷��� ������ 
				 * ������ �ҽǵȴ�.
				 * �׷��� ����� ���� ä���� ������ �⵿�Ҷ� ���� �����Ű�� ����ϸ� �� sleep �� �ʿ����.
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
		
		/** ����ȸ �����͸� �����Ѵ�.
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
					 * ��Ʈ���� ���� ����Ÿ(����Ʈ)�� ������� �д´�.
					 * ��Ʈ���� ���������(��Ʈ���� ��) -1�� �����Ѵ�.
					 * ���� ����Ʈ�� ���� �����Ѵ�.
					 * bData�� ���̰� 10000 �̹Ƿ� 10000 ����Ʈ�� �а� ��������. 
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
						// ���� ����Ʈ���� ���� 
						receivedSum += cnt;

						// ����Ʈ�迭�� ��Ʈ������ ��ȯ
//						sTemp = new String(bData,0,cnt,"euc-kr");   //kkmin,20070326
//						sData += sTemp;                             //kkmin,20070326

						// �������Ṯ��(0xFF,0xEF)�� ã�´�.
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
		 * �ް��ڽ� EOR �� �޾Ƶ��δ�. ���࿬��ȸ
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
                System.out.println("MegaBox Connection�� ����" + e.toString());
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
		 * ���࿬��ȸ ������ ��������� �Ľ�
		 * @param String ���࿬��ȸ ����
		 * @return String[] ���࿬��ȸ ������� �׸�迭
		 * Comment : ���࿬��ȸ ������ ������� �κ��� �����׸����� �и��Ѵ�.
		 */
		public String[] parseKfbHeader(String req) throws Exception {
			
			byteArray = req.getBytes();
			String[] attr = new String[13];
			
			try{
				attr[0]  = subString(0,9);		// TRANSACTION CODE(9)
				attr[1]  = subString(9,11);		// SYSTEM-ID(2)
				attr[2]  = subString(11,14);	// ��ǥ����ڵ�(3)
				attr[3]  = subString(14,18);	// ���������ڵ�(4)
				attr[4]  = subString(18,21);	// ���������ڵ�(3)
				attr[5]  = subString(21,22);	// �ۼ���FLAG(1)
				attr[6]  = subString(22,25);	// �����ڵ�(3)
				attr[7]  = subString(25,32);	// �����ڵ�(7)
				attr[8]  = subString(32,39);	// �����������������ȣ(7)
				attr[9]  = subString(39,53);	// ��������������۽ð�(14)
				attr[10] = subString(53,60);	// ����ȸ����������ȣ(7)
				attr[11] = subString(60,74);	// ����ȸ�������۽ð�(14)
				attr[12] = subString(74,90);	// ���������ʵ�(16)
				
				/*
				System.out.println("���࿬��ȸ KFB HEADER BEGIN============================");
				System.out.println("["+attr[0]	+"]TRANSACTION CODE");	// TRANSACTION CODE(9)
				System.out.println("["+attr[1]  +"]SYSTEM-ID");	// SYSTEM-ID(2)
				System.out.println("["+attr[2]  +"]��ǥ����ڵ�");	// ��ǥ����ڵ�(3)
				System.out.println("["+attr[3]  +"]���������ڵ�");	// ���������ڵ�(4)
				System.out.println("["+attr[4]  +"]���������ڵ�");	// ���������ڵ�(3)
				System.out.println("["+attr[5]  +"]�ۼ���FLAG");	// �ۼ���FLAG(1)
				System.out.println("["+attr[6]  +"]�����ڵ�");	// �����ڵ�(3)
				System.out.println("["+attr[7]  +"]�����ڵ�");	// �����ڵ�(7)
				System.out.println("["+attr[8]  +"]�����������������ȣ");	// �����������������ȣ(7)
				System.out.println("["+attr[9]  +"]��������������۽ð�");	// ��������������۽ð�(14)
				System.out.println("["+attr[10] +"]����ȸ����������ȣ");	// ����ȸ����������ȣ(7)
				System.out.println("["+attr[11] +"]����ȸ�������۽ð�");	// ����ȸ�������۽ð�(14)
				System.out.println("["+attr[12] +"]���������ʵ�");			// ���������ʵ�(16)
				System.out.println("���࿬��ȸ KFB HEADER END==============================");
				*/
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}
		
		/**
		 * ���ڿ� ����Ʈ�� ��� - Exception ���� �߻�
		 * @return a String - ��û�� ����Ʈ ������ ���ڿ�
		 * @param int to Set - ���� ����Ʈ
		 * @param int to Set - ������ ����Ʈ
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
	 * @comment : Ŭ���̾�Ʈ�� 200���� Ǯ�� �ξ�д�.
	 *
	 *************************************************************************/
	class Client extends Thread {
		// ���ü���
		Socket lS = null;
		// �ܺμ���
		Socket xS = null;
		
		// ���ü��� ��Ʈ��
		BufferedReader in   = null;
		BufferedWriter out  = null;

		// �ܺμ��� ��Ʈ��
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

		// ���� I/O ��Ʈ��
		Client(Socket con) throws Exception {
			lS = con;
			in  = new BufferedReader(new InputStreamReader(lS.getInputStream()));
		    out = new BufferedWriter(new OutputStreamWriter(lS.getOutputStream()));			
		}

		public void run() {
			try {
				for(;;) {
					// ���ÿ�û�� �޴´�.
					String str = in.readLine();

					// ������� �Ľ��� ���� ����Ʈ �迭�� ��ȯ�Ѵ�.
					sToBytes = str.getBytes();

					if( sToBytes.length < 100 ) {
						System.out.println("��û�� ������ ���̰� �ʹ� �۽��ϴ�. ������ ���õǾ����ϴ�.["+str+"]");
						continue;
					}

					// ��������� �Ľ��Ѵ�.
					String[] localRequestHeader = parseOnlineCommonHeader(str);
					
					/* ��û����ڵ� ��ȯ */
					int orgCode = 00;
					try{
						orgCode = Integer.parseInt(localRequestHeader[0]);
					}catch(Exception e){
						orgCode = 99;
						continue;
					}
					if((orgCode != 11 &&  orgCode != 12 && orgCode != 13 &&  orgCode != 21 && 
						orgCode != 22 &&  orgCode != 14 && orgCode != 31)|| (orgCode == 99) ){            //14�߰� (kkmin,20061228)
						System.out.println("���� ��û����� �߸��Ǿ����ϴ�.");
						System.out.println("---------------Daemon Local Header Parsing--------------------");
						System.out.println("["+localRequestHeader[0] +"]��ܱ���ڵ�");
						System.out.println("["+localRequestHeader[1] +"]�ۼ��ſ���");
						System.out.println("["+localRequestHeader[2] +"]�ŷ������ڵ�");
						System.out.println("["+localRequestHeader[3] +"]������ȣ");
						System.out.println("["+localRequestHeader[4] +"]�ŷ��Ϸù�ȣ");
						System.out.println("["+localRequestHeader[5] +"]�����ڵ�");
						System.out.println("["+localRequestHeader[6] +"]�ŷ�����");
						System.out.println("["+localRequestHeader[7] +"]�ŷ��ð�");
						System.out.println("["+localRequestHeader[8] +"]�����ͱ���");
						System.out.println("["+localRequestHeader[9] +"]ó���ܸ���ȣ");
						System.out.println("["+localRequestHeader[10]+"]�ܸ���ϻ����ȣ");
						System.out.println("["+localRequestHeader[11]+"]�����ڵ�");
						System.out.println("["+localRequestHeader[12]+"]������������");
						System.out.println("["+localRequestHeader[13]+"]����Ÿ����");
						System.out.println("--------------------------------------------------------------");
						continue;						
					}
					
					// ����� IP/PORT ����
					switch(orgCode) {
						case 11:
							break;
						case 12:
							// �ѽ���
							//IP   = "150.50.50.51";
							//PORT = 29176;// ������Ʈ=29176, ���Ʈ=29175
							IP   = "203.234.213.51";
							PORT = 28101;// ������Ʈ=28101
							break;
						case 13:
							// �ѽ���
							IP   = "210.121.32.250";
							//PORT = 8131; // ������Ʈ=8131 , ���Ʈ=8130
							PORT = 16350;  //port : � 16350, �׽�Ʈ 16351
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
							PORT = 1911; // ������Ʈ=1911, ���Ʈ=1912 
							break;		
						case 22:
							// �������
							IP   = "152.149.234.164";
							PORT = 3024; // ������Ʈ=3024, ���Ʈ=???? 
							break;
						case 31:
							// Ȩ������
							//IP   = "192.168.45.205";
							//PORT = 9010; // ������Ʈ=3024, ���Ʈ=????
							IP = "196.1.1.1";
							PORT = 39020;
						default:
							break;
					}
					
					// �ܺα���� ���Ͽ����ϰ� I/O ��Ʈ���� �����Ѵ�.
					String sendError = "0";
					try{
						if( orgCode > 11 ){
							// �ѽ���,�ѽ���,KIB �̸� ���ο� ������ �����Ѵ�.
							System.out.println("--------------- ���ο� ���� ������ �����մϴ�.... ------------");
							System.out.println(IP + " : " + String.valueOf(PORT) + " ������ ����õ���.....");
							xS   = new Socket(IP,PORT);
							is   = xS.getInputStream();                            //(kkmin,20070117)
							xin  = new BufferedReader(new InputStreamReader(is));  //(kkmin,20070117)
							xout = new BufferedWriter(new OutputStreamWriter(xS.getOutputStream()));
							System.out.println(IP + " : " + String.valueOf(PORT) + " ������ �����߽��ϴ�.");
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println(IP + " : " + String.valueOf(PORT) + " ���� ������ �����߽��ϴ�.");
						sendError = "9";
					}
					
					/*
					 * ������� ������� ��ƼƼ ���� ���� ����� �����ϰ�
					 * ȭ�鿡�� ������ ����Ÿ������ �տ� ����� �ٿ� �ش������� �����Ѵ�
					 */
					MegaBoxClient mboxC = null;
					
					try{
						switch(orgCode) {
							case 11:
								/*
								 * ���࿬��ȸ
								 * �������۵� ����Ÿ�� �����Ͽ� �����Ѵ�.
								 */
								/*
								 * ���࿬��ȸ������ ������ ������ ����.
								 */
								//mboxC = mboxmanager.getChannel();
								//mboxC.bankSendMsg(localRequestHeader[13]);
								/*
								 * ���࿬��ȸ������ ������ ������ ����.
								 */
								/*
								System.out.println("[���󿡼� �ϼ��� ��û����]-----------------------------------------");
								System.out.println("-"+localRequestHeader[13]+"-");
								System.out.println("--------------------------------------------------------------");
								*/						
								break;
							case 12:
						        /* �ѽ���
						         * �������۵� ����Ÿ�� �����Ͽ� �����Ѵ�.
						         */
								System.out.println("===== �ѽ����� ������ ������ =======");
								System.out.println(localRequestHeader[13]);
								System.out.println("===============================");
						        sendMsg(localRequestHeader[13]);
								break;
							case 13:
								/* �ѽ��� *
								 * �ѽ����� ��������(4 byte)�� ������ ���Եȴ�(�����׸��� �Ϻ��̴�)
								 * ��, ��������Ÿ�� 100 ����Ʈ�̸� �Ǿ��� 4����Ʈ�� �� �������̸�
								 * ��Ÿ���� ��Ʈ�� 0100 �� �ȴ�.
								 * �ѽ��� ������ �������Ž� ��ó�� 4����Ʈ�� �о� �������̸� �Ǵ��ϰ�
								 * ������ 96 ����Ʈ�� �а� �ȴ�.
								 */
								System.out.println("===== �ѽ��� ������ ������ =======");
								System.out.println(localRequestHeader[13]);
								System.out.println("===============================");
						        sendMsg(localRequestHeader[13]);
								break;
							case 14:   //KCB (kkmin,20061228)
								System.out.println("===== KCB�� ������ ������ =======");
								System.out.println(localRequestHeader[13]);
								System.out.println("===============================");
						        sendMsg(localRequestHeader[13]);
								break;
							case 21:
						        /* KIB
						         * �������۵� ����Ÿ�� �����Ͽ� �����Ѵ�.
						         */
			            	    int gramLen = localRequestHeader[13].getBytes().length+4;
			            	    String strLen = Integer.toString(gramLen);
			            	    for( int i=strLen.length(); i<4 ; i++ ){
			            	    	strLen = "0"+strLen;
			            	    }
			            	    localRequestHeader[13] = strLen+localRequestHeader[13];
	
						        sendMsg(localRequestHeader[13]);
						        /*
								System.out.println("[���󿡼� �ϼ��� ��û���� KIB]--------------------------------------");
								System.out.println("["+localRequestHeader[13]+"]");
								System.out.println("--------------------------------------------------------------["+localRequestHeader[13].getBytes().length+"]");
								*/
								break;
							case 22:
								/* ������� */
						        sendMsg(localRequestHeader[13]);
						        /*
						        System.out.println("[���󿡼� �ϼ��� ��û���� �������]------------------------------------");
								System.out.println("["+localRequestHeader[13]+"]"+localRequestHeader[13].getBytes().length);
								System.out.println("--------------------------------------------------------------");
								*/
								break;
							case 31:
								/* Ȩ������ */
								String sendGram = str.substring(2, str.length());
								System.out.println("�� bytes : " + str.getBytes().length);
								
								//String sendGram = str.substring(2, str.getBytes().length);
								sendMsg(sendGram);
						        System.out.println("[���󿡼� �ϼ��� ��û���� Ȩ������]------------------------------------");
								System.out.println(sendGram);
								System.out.println("--------------------------------------------------------------");
								break;	
							default:
								break;
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("�������ۿ���");
						sendError = "9";	// �۽źҰ�
					}

					/* ��û����ڵ忡 ���� ���� */
					String xRcv = "";		// �ܺα������ �޴������� �����ϴ� ����
					String recvError = "0";
					if(!sendError.equals("9")){
						try{
							switch(orgCode) {
								case 11:
									// ���࿬��ȸ
									String   strOrgMngNo = localRequestHeader[3];
									strOrgMngNo = strOrgMngNo.substring(13);
									int cnt = 0;
									/*
									 * ���࿬��ȸ ������ ����.
									 */
									/*while(xRcv == null || xRcv.equals("") ){
										Thread.sleep(200);cnt++;
										System.out.println("�޽���ť�� ���� [������ȣ:"+strOrgMngNo+"]"+this);
										try{
											xRcv = mboxC.getQueueMsg(strOrgMngNo);
											mboxC.delQueueMsg(strOrgMngNo);
										}catch(Exception e){
											System.out.println(e.getMessage());
											break;
										}
										if( cnt > 150 ){
											// ���࿬��ȸ �ſ����� ��ȸ Ÿ�Ӿƿ� �ð��� 30�ʰ� ����ϸ� Ÿ�Ӿƿ� ó���Ѵ�.(100ms*300=30sec)
											System.out.println("������ȣ��["+strOrgMngNo+"]�� ������ �������� ���߽��ϴ�");
											break;
										}
									}*/
									/*
									 * ���࿬��ȸ ������ ����.
									 */
									/*
									 * �������� �����.
									 */
									xRcv += "   KFBCIF";	//TR�ڵ� (9)
									xRcv += "90";			//System ID (2)
									xRcv += "373";			//��ǥ����ڵ� (3)
									xRcv += "0200";			//���������ڵ� (4)
									xRcv += "200";			//���������ڵ� (3)
									xRcv += "B";			//�ۼ���Flag(1)
									xRcv += "   ";			//�����ڵ� (3)
									xRcv += "3730102";		//�����ڵ�(7)
									xRcv += "0000001";		//�����������������ȣ(7)
									xRcv += "20080928171300";	// ��������������۽ð�(14)
									xRcv += "       ";		// ����ȸ����������ȣ(7)
									xRcv += "              ";	// ����ȸ�������۽ð�(14)
									xRcv += "                ";	// ���������ʵ�(16)
								
									break;
								case 12:
									// �ѽ���
									xRcv   = rcvNiceMsg();
									xRcv  += rcvNiceMsg();
									break;
								case 13:
									// �ѽ���
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
									// �������
									xRcv   = rcvDwMsg();
									break;
								case 31:
									// Ȩ������
									// Ȩ�������� ���� �޼����� ������ ��.
									xRcv   = rcvDwMsg();
									break;
								default:
									break;
							}
						}catch(Exception e){
							System.out.println("�������ſ���");
							recvError = "8";	// ���źҰ�.
						}
					}

					try{
						if(xRcv.getBytes().length == 0){
							System.out.println("���ݵ������κ��� ���������� �����ϴ�");
							recvError = "8";	// ���źҰ�.
						}
					}catch(Exception e){
						System.out.println("����Ʈ Array ����üũ ����");
						recvError = "8";		// ���źҰ�.
					}					
					
					if( orgCode == 11 ){
						//xRcv = new String(xRcv.getBytes("euc-kr"));
						//xRcv = new String(xRcv.getBytes("euc-kr"));
						//System.out.println("[����ȸ �������� --------------------------------------]");
						//System.out.println("["+xRcv+"]");
						//System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
					}else if( orgCode == 12 ) {
						/*
						System.out.println("[�ѽ��� ��������--------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
						*/
					}else if( orgCode == 13 ) {
						/*
						System.out.println("[�ѽ��� �������� --------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
						*/
					}else if( orgCode == 14 ) {
						/*
						System.out.println("[KCB �������� --------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
						*/
					}else if( orgCode == 21 ) {
						/*
						System.out.println("[KIB �������� ----------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[---------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
						*/
					}else if( orgCode == 22 ) {
						/*
						System.out.println("[������� �������� ----------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[---------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
						*/
					}else if( orgCode == 31 ) {
						System.out.println("[Ȩ������ �������� ----------------------------------------]");
						System.out.println("["+xRcv+"]");
						System.out.println("[---------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
					}
						
						
					// ����� �������� ������� �Ľ��Լ� ȣ��
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
					String[] homeHeader  = null;   //Ȩ������
					
					try{
						switch(orgCode) {
							case 11:
								// ���࿬��ȸ
								kfbHeader  = parseKfbHeader(xRcv);
								break;
							case 12:
								// �ѽ���(���� ���� 10 ����Ʈ�� �����ϰ�)
								niceHeader = parseNiceHeader(xRcv.substring(10));
								break;
							case 13:
								// �ѽ���
/*								
								if( localRequestHeader[2].trim().startsWith("SA") ){
									kisHeader  = parseKisHeaderSA(xRcv);
								}else if( localRequestHeader[2].trim().startsWith("TR") ){
									kisHeader  = parseKisHeaderTR(xRcv);
								}                                                                  //kkmin,20070131 ���ƹ��� ���� ���� ���Ÿ��� �ϰ� �ֱ�
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
								// �������
								dwHeader  = parseDwHeader(xRcv);
								for(int i=0;i<dwHeader.length;i++) System.out.println("dwHeader["+i+"]=["+dwHeader[i]+"]len="+dwHeader[i].getBytes().length);							
								break;
							default:
								break;
						}
					}catch(Exception e){
						System.out.println("�Ľ̿��� - ��������Ÿ ����");
						recvError = "7";	// ��������Ÿ ����
					}
					
					/* 
					 * ���� ����� ����(����)
					 * ����� ���������� ����θ� �Ľ��Ͽ� ���������� ���ð���θ� �����Ѵ�.
					 */
					String stsCode = "0";
					if( sendError.equals("0")){
						// �۽��� �����̸�
						if( recvError.equals("0")){
							// ������ �����̸�
							stsCode = "0";		// ��Ʈ�� �������� �Ǵ�
						}else{
							if( recvError.equals("8") ){
								stsCode = "8";	// ���ſ����� �Ǵ�
							}else{
								stsCode = "7";	// ������������� �Ǵ�
							}
						}
					}else{
						// �۽��� �����̸�
						stsCode = "9";			// �۽ſ����� �Ǵ�(��Ʈ�� �������)
					}
					
					String localResponseHeader = "";
					try{
						if( stsCode.equals("0")){
							switch(orgCode) {// ����ڵ�
								case 11:
									// ���࿬��ȸ
									localResponseHeader += localRequestHeader[0];  			// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
									localResponseHeader += "R";  							// �ۼ��ſ���(1)
									localResponseHeader += kfbHeader[3];					// ���������ڵ�(4)
									localResponseHeader += kfbHeader[4]+" ";				// �ŷ������ڵ�(4)
									localResponseHeader += "00000000000000000000"; 			// ������ȣ(20)
									localResponseHeader += "0000000000";					// �ŷ��Ϸù�ȣ(10)
									localResponseHeader += kfbHeader[6]+" ";				// �����ڵ�(4)
									localResponseHeader += kfbHeader[9].substring(0,8);		// �ŷ�����(8)
									localResponseHeader += kfbHeader[9].substring(8,14);	// �ŷ��ð�(6)
									localResponseHeader += "0000";							// �����ͱ���(4)
									localResponseHeader += "11111111";						// ó���ܸ���ȣ(8)
									localResponseHeader += "22222222";						// �ܸ���ϻ����ȣ(8)
									localResponseHeader += stsCode;							// �����ڵ�(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// ������������(20)
									localResponseHeader += xRcv;								
									break;
								case 12:
									// �ѽ���
									localResponseHeader += localRequestHeader[0];  			// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
									localResponseHeader += "R";  							// �ۼ��ſ���(1)
									localResponseHeader += niceHeader[1];					// ���������ڵ�(4)
									localResponseHeader += niceHeader[2].substring(0,4);	// �ŷ������ڵ�(4)
									localResponseHeader += "00000000000000000000"; 			// ������ȣ(20)
									localResponseHeader += "0000000000";					// �ŷ��Ϸù�ȣ(10)
									localResponseHeader += niceHeader[5];					// �����ڵ�(4)
									localResponseHeader += niceHeader[10].substring(0,8);	// �ŷ�����(8)
									localResponseHeader += niceHeader[10].substring(8,14);	// �ŷ��ð�(6)
									localResponseHeader += "0000";							// �����ͱ���(4)
									localResponseHeader += "11111111";						// ó���ܸ���ȣ(8)
									localResponseHeader += "22222222";						// �ܸ���ϻ����ȣ(8)
									localResponseHeader += stsCode;							// �����ڵ�(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// ������������(20)
									localResponseHeader += xRcv;							// ����Ÿ����(��������)(����)
									break;
								case 13:
									// �ѽ���
/*
									if( localRequestHeader[2].trim().startsWith("SA") ){
										localResponseHeader += localRequestHeader[0];  			// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
										localResponseHeader += "R";  							// �ۼ��ſ���(1)
										localResponseHeader += kisHeader[2]+"  ";				// ���������ڵ�(4)
										localResponseHeader += kisHeader[1];					// �ŷ������ڵ�(4)
										localResponseHeader += "00000000000000000000"; 			// ������ȣ(20)
										localResponseHeader += "0000000000";					// �ŷ��Ϸù�ȣ(10)
										localResponseHeader += kisHeader[8]+"  ";				// �����ڵ�(4)
										localResponseHeader += "YYYY"+kisHeader[6].substring(0,4);// �ŷ�����(8)
										localResponseHeader += kisHeader[6].substring(4,10);	// �ŷ��ð�(6)
										localResponseHeader += "0000";							// �����ͱ���(4)
										localResponseHeader += "11111111";						// ó���ܸ���ȣ(8)
										localResponseHeader += "22222222";						// �ܸ���ϻ����ȣ(8)
										localResponseHeader += stsCode;							// �����ڵ�(1)
										localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// ������������(20)
										localResponseHeader += xRcv;							// ����Ÿ����(��������)(����)
									}else if( localRequestHeader[2].trim().startsWith("TR") ){
										localResponseHeader += localRequestHeader[0];  			// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
										localResponseHeader += "R";  							// �ۼ��ſ���(1)
										localResponseHeader += kisHeader[2]+"  ";				// ���������ڵ�(4)
										localResponseHeader += kisHeader[1];					// �ŷ������ڵ�(4)
										localResponseHeader += "00000000000000000000"; 			// ������ȣ(20)
										localResponseHeader += "0000000000";					// �ŷ��Ϸù�ȣ(10)
										localResponseHeader += kisHeader[6]+"  ";				// �����ڵ�(4)
										localResponseHeader += "YYYY"+kisHeader[4].substring(0,4);// �ŷ�����(8)
										localResponseHeader += kisHeader[4].substring(4,10);	// �ŷ��ð�(6)
										localResponseHeader += "0000";							// �����ͱ���(4)
										localResponseHeader += "11111111";						// ó���ܸ���ȣ(8)
										localResponseHeader += "22222222";						// �ܸ���ϻ����ȣ(8)
										localResponseHeader += stsCode;							// �����ڵ�(1)
										localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";			// ������������(20)
										localResponseHeader += xRcv;							// ����Ÿ����(��������)(����)
									}                                                           // kkmin,20070131 �������� ���Ÿ��� �ϰ� �ֱ�.
*/
									localResponseHeader += fillPkString(localRequestHeader[0],100);	// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)(TS10�����߰�,kkmin,20070131)
									localResponseHeader += xRcv;					                // ����Ÿ����(��������)(����)(TS10�����߰�,kkmin,20070131)
									break;
								case 14:
									// KCB (kkmin,20061228)
									localResponseHeader += fillPkString(localRequestHeader[0],100);	// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
									localResponseHeader += xRcv;					// ����Ÿ����(��������)(����)
									manager.setKcbPortSeq(kcbPortSeq); //KCB port deallocation(kkmin,20070423)
									System.out.println("KCB port deallocation sequence : " + kcbPortSeq);  //kkmin,20070423
									break;
								case 21:
									// KIB
									localResponseHeader += localRequestHeader[0];  	// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
									localResponseHeader += "R";  					// �ۼ��ſ���(1)
									localResponseHeader += kibHeader[10];			// ���������ڵ�(4)
									localResponseHeader += kibHeader[11];			// �ŷ������ڵ�(4)
									localResponseHeader += "00000000000000000000"; 	// ������ȣ(20)
									localResponseHeader += "0000000000";			// �ŷ��Ϸù�ȣ(10)
									localResponseHeader += "0000";					// �����ڵ�(4)
									localResponseHeader += "20060102";				// �ŷ�����(8)
									localResponseHeader += "101010";				// �ŷ��ð�(6)
									localResponseHeader += "0000";					// �����ͱ���(4)
									localResponseHeader += "11111111";				// ó���ܸ���ȣ(8)
									localResponseHeader += "22222222";				// �ܸ���ϻ����ȣ(8)
									localResponseHeader += stsCode;					// �����ڵ�(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";	// ������������(20)
									localResponseHeader += xRcv;					// ����Ÿ����(��������)(����)
									break;	
								case 22:
									// �������
									localResponseHeader += localRequestHeader[0];  	// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
									localResponseHeader += "R";  					// �ۼ��ſ���(1)
									localResponseHeader += localRequestHeader[2];	// ���������ڵ�(4)+�ŷ������ڵ�(4)
									localResponseHeader += "00000000000000000000"; 	// ������ȣ(20)
									localResponseHeader += "0000000000";			// �ŷ��Ϸù�ȣ(10)
									localResponseHeader += "0000";					// �����ڵ�(4)
									localResponseHeader += "20060102";				// �ŷ�����(8)
									localResponseHeader += "101010";				// �ŷ��ð�(6)
									localResponseHeader += "0000";					// �����ͱ���(4)
									localResponseHeader += "11111111";				// ó���ܸ���ȣ(8)
									localResponseHeader += "22222222";				// �ܸ���ϻ����ȣ(8)
									localResponseHeader += stsCode;					// �����ڵ�(1)
									localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";	// ������������(20)
									localResponseHeader += xRcv;					// ����Ÿ����(��������)(����)
									break;
								case 31:
									// Ȩ������
									localResponseHeader += xRcv;					// ��������
									break;
								default:
									break;
							}
						}else{
							// ��,���� ���� �� ���������
							localResponseHeader += localRequestHeader[0];  	// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
							localResponseHeader += "R";  					// �ۼ��ſ���(1)
							localResponseHeader += localRequestHeader[2];	// ���������ڵ�(4)+�ŷ������ڵ�(4)
							localResponseHeader += "00000000000000000000"; 	// ������ȣ(20)
							localResponseHeader += "0000000000";			// �ŷ��Ϸù�ȣ(10)
							localResponseHeader += "0000";					// �����ڵ�(4)
							localResponseHeader += "20060428";				// �ŷ�����(8)
							localResponseHeader += "101010";				// �ŷ��ð�(6)
							localResponseHeader += "0000";					// �����ͱ���(4)
							localResponseHeader += "11111111";				// ó���ܸ���ȣ(8)
							localResponseHeader += "22222222";				// �ܸ���ϻ����ȣ(8)
							localResponseHeader += stsCode;					// �����ڵ�(1)
							localResponseHeader += "XXXXXXXXXXXXXXXXXXXX";	// ������������(20)
							localResponseHeader += xRcv;					// ����Ÿ����(��������)(����)
							
							System.out.println("���ſ����� ��������:"+localResponseHeader);

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

					// ���������� �����Ѵ�.
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
								System.out.println("�ѽ������� �ۼ����� ���������� ����:"+e.getMessage());
							}
							if( orgCode == 13 ){
								System.out.println("�ѽ������� �ۼ����� ���������� ����:"+e.getMessage());
							}
							if( orgCode == 14 ){        //KCB (kkmin,20061228)
								System.out.println("KCB ���� �ۼ����� ���������� ����:"+e.getMessage());
							}
							if( orgCode == 21 ){
								System.out.println("KIB  ���� �ۼ����� ���������� ����:"+e.getMessage());
							}	
							if( orgCode == 22 ){
								System.out.println("�������  ���� �ۼ����� ���������� ����:"+e.getMessage());
							}
							if( orgCode == 31 ){
								System.out.println("Ȩ������  ���� �ۼ����� ���������� ����:"+e.getMessage());
							}
						}						
					}
					
				}

			} catch(Exception e) {
				e.printStackTrace();
				try {
					// �� ���ϵ��� ���¸� üũ�Ͽ� ���̰ų� �̿밡�ɻ��°� �ƴҶ��� ������.!!!!!
					// ���μ��� ����
					in.close();
					out.close();
					lS.close();
					// �ܺμ��� ����
					xin.close();
					xout.close();					
					xS.close();
					System.out.println("��� �Ǵ� �����Ľ��� ������ �߻��Ͽ� ���������� �����Ͽ����ϴ�");
				} catch (Exception ex) {
					System.out.println("��� �Ǵ� �����Ľ��� ������ �߻��Ͽ� �������� ������ �����߻���"+ex.getMessage());
				}
			} finally {

			}

		}

		/**
		 * �ܺα���� ��û�Ѵ�.�ѽ���,�ѽ���
		 * @param String to set
		 */
		public synchronized void sendMsg(String command) throws IOException {
			try {
				System.out.println("========== ���� Write Start ==================================");
				xout.write(command);
				System.out.println("========== ���� Write End  ===================================");
				xout.flush();
				System.out.println("========== ���� frush End  ===================================");
			} catch (IOException e) {
				System.out.println("========== sendMsg �Լ� ���̴� �������� IOException �߻� ==========");
				System.out.println(e.getMessage());
				System.out.println("=============================================================");
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				System.out.println("========== sendMsg �Լ� ���̴� �������� Exception �߻� ============");
				System.out.println(e.getMessage());
				System.out.println("=============================================================");
				throw new IOException(e.getMessage());
			} finally {
			}
		}
		
		/**
		 * �ܺα�� ������ �޾Ƶ��δ�.�ѽ���
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
				System.out.println("======== �ѽ������� ���� ���� ===================");
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
		 * �ܺα�� ������ �޾Ƶ��δ�.�ѽ���
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

					// �ѽ����� �������̰� ������ ���ԵǹǷ� �̹̲��� 4 byte �� ���� ������. 
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
			
			System.out.println("======== �ѽ��򿡼� ���� ���� ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();			
		}
		/*--------------------------------------------------------------------------------------------*/

		/**
		 * �ܺα�� ������ �޾Ƶ��δ�.KCB
		 * @return a String
		 */
		public synchronized String rcvKcbMsg() throws IOException {   //KCB �߰�(kkmin,20070104)
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
					System.out.println("==== KCB �������� : " + String.valueOf(iTot[4]) + " ====");
					rtnStr.append(fillPkNumber(String.valueOf(iTot[4]),4));
					for (i = 0; i < (iTot[4]); i++) {
						response = xin.read();
						if (response > 255) {i++;}      //�ѱ��ΰ�� 2byteó��
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
			
			System.out.println("======== KCB���� ���� ���� ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();			
		}
		/*--------------------------------------------------------------------------------------------*/

		/**
		 * �ܺα�� ������ �޾Ƶ��δ�. KIB
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

					// KIB�� �������̰� ������ ���ԵǹǷ� �̹̲��� 4 byte �� ���� ������. 
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
			
			System.out.println("======== KIB���� ���� ���� ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();			
		}
		
		/**
		 * �ܺα�� ������ �޾Ƶ��δ�. Ȩ������
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

					// Ȩ�������� �������� + �����(260)
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
			
			System.out.println("======== Ȩ���������� ���� ���� =================");
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
			
			System.out.println("======== Ȩ���������� ���� ���� ===================");
			System.out.println(rtnStr.toString());
			System.out.println("============================================");

			return rtnStr.toString();
		}	
		*/	
		
		/*
		 * ���ð������ �Ľ�
		 * @param String ���ð������
		 * @return String[] ���ð������ �׸�迭
		 * Comment ȭ�鿡�� �ۼ��� �Ѿ�� �������+���������� �Ľ��Ͽ� ��Ʈ���迭�� �����Ѵ�. 
		 */
		public String[] parseOnlineCommonHeader(String req) throws Exception {
			String[] attr = new String[14];

			try{
				attr[0] = subString(0,2);		// ��ܱ���ڵ�(2)
				
				// Ȩ������ ������ ������ ���� �¿��.
				System.out.println("����� ������ �и��մϴ�.");
				if(attr[0].equals("31")) {
					attr[1] = subString(2,6);		// ���������α���(4)
					attr[2] = subString(6,14);		// ó������(8)
					attr[3] = subString(14,20);		// ó���ð�(6)
					attr[4] = subString(20,28);		// �����Ϸù�ȣ(8)
					attr[5] = subString(28,32);		// �޽�������(4)
					attr[6] = subString(32,36);		// �����ڵ�(4)
					attr[7] = subString(36,40);		// �����ڵ�(4)
					attr[8] = subString(40,240);	// ����޼���(200)
					attr[9] = subString(240,262);	// FILLER(22)
					attr[10] = subString(262,sToBytes.length); // ����Ÿ����
				} else {
					attr[1] = subString(2,3);		// �ۼ��� ����(1)
					attr[2] = subString(3,11);		// �ŷ������ڵ�(8)
					attr[3] = subString(11,31);		// ������ȣ(20)
					attr[4] = subString(31,41);		// �ŷ��Ϸù�ȣ(10)
					attr[5] = subString(41,45);		// �����ڵ�(4)
					attr[6] = subString(45,53);		// �ŷ�����(8)
					attr[7] = subString(53,59);		// �ŷ��ð�(6)
					attr[8] = subString(59,63);		// ������ ����(4)
					attr[9] = subString(63,71);		// ó���ܸ���ȣ(8)
					attr[10] = subString(71,79);	// �ܸ���� �����ȣ(8)
					attr[11] = subString(79,80);	// �����ڵ�(1)
					attr[12] = subString(80,100);	// ������������(20)
					attr[13] = subString(100,sToBytes.length);		// ����Ÿ����
				}
				System.out.println("����� ������ �и��Ϸ��߽��ϴ�.");
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}
			
			return attr;
		}

		/*
		 * �ѽ��� ������ ��������� �Ľ�
		 * @param String �ѽ��� ����
		 * @return String[] �ѽ��� ������� �׸�迭
		 * Comment : �ѽ��� ������ ������� �κ��� �����׸����� �и��Ѵ�.
		 */
		public String[] parseNiceHeader(String req) throws Exception {
			String[] attr = new String[13];
			
			try{
				attr[0] = subString(0,9);		// �����۽ű���ڵ�(9)
				attr[1] = subString(9,13);		// ���������ڵ�(4)
				attr[2] = subString(13,18);		// �ŷ������ڵ�(5)
				attr[3] = subString(18,19);		// �ۼ����÷���(1)
				attr[4] = subString(19,22);		// �ܸ��ⱸ��(3)
				attr[5] = subString(22,26);		// �����ڵ�(4)
				attr[6] = subString(26,35);		// �������ID(9)
				attr[7] = subString(35,45);		// �������������ȣ(10)
				attr[8] = subString(45,59);		// ����������۽ð�(14)
				attr[9] = subString(59,69);		// NICE����������ȣ(10)
				attr[10] = subString(69,83);	// NICE�������۽ð�(14)
				attr[11] = subString(83,99);	// Primary Bitmap(16)
				attr[12] = subString(99,100);	// ����(Extend Bitmap Code)(1)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}		
		
		/*
		 * �ѽ��� ������ ��������� �Ľ�
		 * @param String �ѽ��� ����
		 * @return String[] �ѽ��� ������� �׸�迭
		 * Comment : �ѽ��� ������ ������� �κ��� �����׸����� �и��Ѵ�.
		 */		
		public String[] parseKisHeaderSA(String req) throws Exception {
			String[] attr = new String[10];
	
			try{
				attr[0]  = subString(0,4);		// ��������(4)
				attr[1]  = subString(4,8);		// TX-ID(4)
				attr[2]  = subString(8,10);		// ���������ڵ�(2)
				attr[3]  = subString(10,17);	// ȸ���ڵ�(7)
				attr[4]  = subString(17,47);	// ������(30)
				attr[5]  = subString(47,57);	// �����ID(10)
				attr[6]  = subString(57,67);	// �ŷ��Ͻ�(10)
				attr[7]  = subString(67,127);	// �����AREA(60)
				attr[8]  = subString(127,129);	// �����ڵ�(2)
				attr[9]  = subString(129,149);	// ����޽���(20)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}
		
		/*
		 * �ѽ��� ������ ��������� �Ľ�
		 * @param String �ѽ��� ����
		 * @return String[] �ѽ��� ������� �׸�迭
		 * Comment : �ѽ��� ������ ������� �κ��� �����׸����� �и��Ѵ�.
		 */		
		public String[] parseKisHeaderTR(String req) throws Exception {
			String[] attr = new String[8];
	
			try{
				attr[0]  = subString(0,4);		// ��������(4)
				attr[1]  = subString(4,8);		// TX-ID(4)
				attr[2]  = subString(8,10);		// ���������ڵ�(2)
				attr[3]  = subString(10,17);	// ȸ���ڵ�(7)
				attr[4]  = subString(17,27);	// �ŷ��Ͻ�(10)
				attr[5]  = subString(27,87);	// �����AREA(60)
				attr[6]  = subString(87,89);	// �����ڵ�(2)
				attr[7]  = subString(89,109);	// ����޽���(20)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}		
		
		/*
		 * KCB ��������� �Ľ�
		 * @param String KCB ����
		 * @return String[] KCB ������� �׸�迭
		 * Comment : KCB ������ ������� �κ��� �����׸����� �и��Ѵ�.
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
		 * KIB ������ ��������� �Ľ�
		 * @param String KIB ����
		 * @return String[] KIB ������� �׸�迭
		 * Comment : KIB ������ ������� �κ��� �����׸����� �и��Ѵ�.
		 */
		public String[] parseKibHeader(String req) throws Exception {
			String[] attr = new String[28];
			
			try{
				attr[0]   = subString(0,4);		// ��������(4)- ���������� ����(�ǹ̾���)
				attr[1]   = subString(4,8);		// CS��ȣ(4)
				attr[2]   = subString(8,16);	// CS���� ����ڵ�(8)
				attr[3]   = subString(16,17);	// REACTION CODE(1)
				attr[4]   = subString(17,20);	// ���Ӱŷ���ȣ(3)
				attr[5]   = subString(20,21);	// �ۼ���FLAG(1)
				attr[6]   = subString(21,29);	// ��ޱ���ڵ�(8)
				attr[7]   = subString(29,33);	// ��޿������ڵ�(4)
				attr[8]   = subString(33,38);	// ��޴ܸ��ڵ�(5)
				attr[9]   = subString(38,39);	// ��ü(�߻�)����(1)
				attr[10]  = subString(39,43);	// ���������ڵ�(MSG TYPE)(4)
				attr[11]  = subString(43,47);	// �ŷ������ڵ�(4)
				attr[12]  = subString(47,50);	// �����ڵ�(3)
				attr[13]  = subString(50,58);	// �ŷ�����(8)
				attr[14]  = subString(58,64);	// �ŷ��ð�(6)
				attr[15]  = subString(64,71);	// �ŷ��Ϸù�ȣ(7)
				attr[16]  = subString(71,72);	// �ѱ��ڵ屸��(1)
				attr[17]  = subString(72,73);	// �����ı���(1)
				attr[18]  = subString(73,81);	// ��������ڵ�(8)
				attr[19]  = subString(81,82);	// M/S TRACK��ȣ(1)
				attr[20]  = subString(82,223);	// M/S TRACK DATA(141)
				attr[21]  = subString(223,224);	// ī�屸��(1)
				attr[22]  = subString(224,225);	// ����ȭ�� ��ȸ ��� ����(1)
				attr[23]  = subString(225,226);	// ��ü�ŷ��� ����������(1)
				attr[24]  = subString(226,240);	// USER WORK AREA(14)
				attr[25]  = subString(240,280);	// ���� MESSAGE(40)
				attr[26]  = subString(280,286);	// ���ŷ����(6)
				attr[27]  = subString(286,300);	// FILLER(14)
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}
		
		/*
		 * ������� ������ ��������� �Ľ�
		 * @param String ������� ����
		 * @return String[] ������� ������� �׸�迭
		 * Comment : ������� ������ ������� �κ��� �����׸����� �и��Ѵ�.
		 */
		public String[] parseDwHeader(String req) throws Exception {
			String[] attr = new String[16];
			StringTokenizer dwGram = new StringTokenizer(req,"|");
			try{
				/*
				attr[0]   // �����ڵ�(8)
				attr[1]   // �����ڵ�(4)
				attr[2]   // ��������(10)
				attr[3]   // ó������(8)
				attr[4]   // ó���ð�(6)
				attr[5]   // ����(6)
				attr[6]   // ��������(1)
				attr[7]   // �����Ǽ�(3)
				attr[8]   // ��ҿ���(1)
				attr[9]   // ȸ������(8)
				attr[10]  // ��������(8)
				attr[11]  // ���ݱݾ�(13)
				attr[12]  // ���ǰ����ڵ�(6)
				attr[13]  // ����ȣ(10)
				attr[14]  // �Ա�ǥ��ȣ(10)
				attr[15]  // �����ȣ(8)
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
		 * ���࿬��ȸ ������ ��������� �Ľ�
		 * @param String ���࿬��ȸ ����
		 * @return String[] ���࿬��ȸ ������� �׸�迭
		 * Comment : ���࿬��ȸ ������ ������� �κ��� �����׸����� �и��Ѵ�.
		 */		
		public String[] parseKfbHeader(String req) throws Exception {
			String[] attr = new String[13];
			
			try{
				attr[0]  = subString(0,9);		// TRANSACTION CODE(9)
				attr[1]  = subString(9,11);		// SYSTEM-ID(2)
				attr[2]  = subString(11,14);	// ��ǥ����ڵ�(3)
				attr[3]  = subString(14,18);	// ���������ڵ�(4)
				attr[4]  = subString(18,21);	// ���������ڵ�(3)
				attr[5]  = subString(21,22);	// �ۼ���FLAG(1)
				attr[6]  = subString(22,25);	// �����ڵ�(3)
				attr[7]  = subString(25,32);	// �����ڵ�(7)
				attr[8]  = subString(32,39);	// �����������������ȣ(7)
				attr[9]  = subString(39,53);	// ��������������۽ð�(14)
				attr[10] = subString(53,60);	// ����ȸ����������ȣ(7)
				attr[11] = subString(60,74);	// ����ȸ�������۽ð�(14)
				attr[12] = subString(74,90);	// ���������ʵ�(16)
				/*
				System.out.println("KFB HEADER BEGIN============================");
				System.out.println("["+attr[0]	+"]");	// TRANSACTION CODE(9)
				System.out.println("["+attr[1]  +"]");	// SYSTEM-ID(2)
				System.out.println("["+attr[2]  +"]");	// ��ǥ����ڵ�(3)
				System.out.println("["+attr[3]  +"]");	// ���������ڵ�(4)
				System.out.println("["+attr[4]  +"]");	// ���������ڵ�(3)
				System.out.println("["+attr[5]  +"]");	// �ۼ���FLAG(1)
				System.out.println("["+attr[6]  +"]");	// �����ڵ�(3)
				System.out.println("["+attr[7]  +"]");	// �����ڵ�(7)
				System.out.println("["+attr[8]  +"]");	// �����������������ȣ(7)
				System.out.println("["+attr[9]  +"]");	// ��������������۽ð�(14)
				System.out.println("["+attr[10] +"]");	// ����ȸ����������ȣ(7)
				System.out.println("["+attr[11] +"]");	// ����ȸ�������۽ð�(14)
				System.out.println("["+attr[12] +"]");	// ���������ʵ�(16)
				System.out.println("KFB HEADER END==============================");
				*/
			}catch(Exception e){
				throw e;
			}
			
			return attr;
		}
		
		/**
		 * ���ڿ� ����Ʈ�� ��� - Exception ���� �߻�
		 * @return a String - ��û�� ����Ʈ ������ ���ڿ�
		 * @param int to Set - ���� ����Ʈ
		 * @param int to Set - ������ ����Ʈ
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

	    private String fillPkNumber(String str, int pkLen) {  //KCB �߰�(kkmin,20070110)
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