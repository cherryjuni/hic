/*
 * ��ܰ� �¶��� �۽��� �����̴�.
 * 
 * ��ܱ���� ���������� ������ �ְ�޴� �����̴�.
 * Ŭ���̾�Ʈ(ȭ���)�� ��û�� �޾� ���ð�������� �Ľ��Ѵ�.
 * �Ľ̵� ��������� ��������� ���� �ش������� �����Ѵ�.
 * �� ������ �ѽ��� ����� ���� ���������� �۵��Ѵ�.
 * �������� ��û������ ������� �����ϰ� ���������� �ö����� ������ ���ŷ ���¿� ����.
 * �񵿱����� ��û������ ������� ������ �����ϰ� ���������� �޽���ť���� ������ȣ�� ��ġ�Ѵ�.
 * ������ Ÿ�Ӿƿ�(Time-out) �ð��� ����ϸ� Time-out Exception �� �߻���Ű��
 * ��û������ ������ Ŭ���̾�Ʈ�� ��ֻ��¸� �ڵ�� �����Ѵ�.
 * ���߼���
 */
/*
 * ���������� ����ȴ�.
 * ��ȭ���� ���ó�Ʈ��ũ �����Ǹ� ����ϱ� ����..
 * �Ʒ��� �ƱԸ�Ʈ�� �ְ� �����Ѵ�
 * 
 * ���������� KIBNET�� ����ȴ�.
 * ���ÿ����� ������ ������, KIBNET�� ����� �� ���� 
 * ���ÿ��� �׽�Ʈ�ϰ�,
 * ���� KIBNET������ ���� ������, �׽�Ʈ�ϱ� ���� ���α׷�
 * ����ǰ� KIBNET���� ���������� ������
 * KIBNET������ �ùķ��̼� �Ѵ�.
 * �Ʒ��� �ƱԸ�Ʈ�� �ְ� �����Ѵ�
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
	 * ������
	 * Connection �����ڸ� �����ϰ� �����Ѵ�.
	 * Listener �� �����ϰ� �����Ѵ�.
	 */
	public SendDaemonLocalReturn() throws Exception {
		clients = new Vector();
		
		manager = new Manager();
		manager.start();
		
		listener = new Listener();
		listener.start();
	}

	/*
	 * ������ ���� �����Լ�
	 */
	public static void main(String[] args) throws Exception {
		try{
			SendDaemonLocalReturn.nIP   = args[1];
			SendDaemonLocalReturn.nPORT = Integer.parseInt(args[2]);
			
			SendDaemonLocalReturn.sPORT = Integer.parseInt(args[0]);
			
			SendDaemonLocalReturn server = new SendDaemonLocalReturn();
			System.out.println("=========================================================");
			System.out.println(SendDaemonLocalReturn.nIP + " ���������� �����Ǿ����ϴ�......");
			System.out.println("=========================================================");
		}catch(IOException ie){
			System.out.println(SendDaemonLocalReturn.nIP + " ������ �⵿�ϴ��� ������ �߻��Ͽ� ������ �⵿�Ҽ� �����ϴ�.");
			System.out.println("=========================================================");
			System.out.println(ie.getMessage());
			System.out.println("=========================================================");
			//System.exit(-1);
		}catch(Exception e){
			System.out.println(SendDaemonLocalReturn.nIP + " ������ �⵿�ϴ��� ������ �߻��Ͽ� ������ �⵿�Ҽ� �����ϴ�.");
			System.out.println("=========================================================");
			System.out.println(e.getMessage());
			System.out.println("=========================================================");			
			System.out.println(SendDaemonLocalReturn.nIP + " ������ �����Ͽ����ϴ�.");
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

	/*************************************************************************
	 * 
	 * @comment : Ŭ���̾�Ʈ�� 200���� Ǯ�� �ξ�д�.
	 *
	 *************************************************************************/
	class Client extends Thread {
		// ���ü���
		private Socket lS = null;
		// �ܺμ���
		private Socket xS = null;
		
		// ���ü��� ��Ʈ��
		private BufferedReader in   = null;
		private BufferedWriter out  = null;

		// �ܺμ��� ��Ʈ��
		private BufferedReader xin  = null;   
		private BufferedWriter xout = null;   
		private InputStream is  = null;      //(kkmin,20070117)

		private byte[] sToBytes;
		private String IP   = SendDaemonLocalReturn.nIP;
		private int    PORT = SendDaemonLocalReturn.nPORT;
		
		// ���� I/O ��Ʈ��
		Client(Socket con) throws Exception {
			lS = con;
			in  = new BufferedReader(new InputStreamReader(lS.getInputStream()));
		    out = new BufferedWriter(new OutputStreamWriter(lS.getOutputStream()));			
		}

		public void run() {
			try {
				for(;;) {
					//System.out.println("��ܰ� ���� ��û ó�� ����!!!!!!!!");
					
					// ���ÿ�û�� �޴´�.
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
						
						// �������̰� ���� �Ǿ��ڸ��� ���ԵǹǷ� �̹̲��� 10 byte �� ���� ������.
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
					
					
					//System.out.println("FW���� ���۵� ��û���� : [" + str + "]");

					// ������� �Ľ��� ���� ����Ʈ �迭�� ��ȯ�Ѵ�.
					sToBytes = str.getBytes();

					//if( sToBytes.length < 100 ) {
					//	System.out.println("��û�� ������ ���̰� �ʹ� �۽��ϴ�. ������ ���õǾ����ϴ�.["+str+"]");
					//	continue;
					//}

					// ��������� �Ľ��Ѵ�.
					String[] localRequestHeader = parseOnlineCommonHeader(str);
					
					if(localRequestHeader == null) { 
						System.out.println("localRequestHeader�� null�̹Ƿ� ���������մϴ�.");
						
						try {
							// �� ���ϵ��� ���¸� üũ�Ͽ� ���̰ų� �̿밡�ɻ��°� �ƴҶ��� ������.!!!!!
							// ���μ��� ����
							in.close();
							out.close();
							lS.close();
							// �ܺμ��� ����
//							xin.close();
//							xout.close();					
//							xS.close();

							break;
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
//					
//					/* ��û����ڵ� ��ȯ */
//					int orgCode = 00;
//					
//					// �ܺα���� ���Ͽ����ϰ� I/O ��Ʈ���� �����Ѵ�.
//					String sendError = "0";
//					
//					try{
//						// �ſ������翡 ���ο� ������ �����Ѵ�.
//						System.out.println("--------------- ���ο� ���� ������ �����մϴ�.... ------------");
//						System.out.println(IP + " : " + String.valueOf(PORT) + " ������ ����õ���.....");
//						xS   = new Socket(IP,PORT);
//						is   = xS.getInputStream();                            //(kkmin,20070117)
//						xin  = new BufferedReader(new InputStreamReader(is));  //(kkmin,20070117)
//						xout = new BufferedWriter(new OutputStreamWriter(xS.getOutputStream()));
//						System.out.println(IP + " : " + String.valueOf(PORT) + " ������ �����߽��ϴ�.");
//					}catch(Exception e){
//						e.printStackTrace();
//						System.out.println(IP + " : " + String.valueOf(PORT) + " ���� ������ �����߽��ϴ�.");
//						sendError = "9";
//					}
//					
//					/*
//					 * ������� ������� ��ƼƼ ���� ���� ����� �����ϰ�
//					 * ȭ�鿡�� ������ ����Ÿ������ �տ� ����� �ٿ� �ش������� �����Ѵ�
//					 */
//					
//					try{
//						System.out.println("===== �ſ������翡 ������ ������ =======");
//						//System.out.println(localRequestHeader[13]);
//						System.out.println("[" + localRequestHeader[2] + "][" + localRequestHeader[2].getBytes().length + "]");
//						System.out.println("===============================");
//				        //sendMsg(localRequestHeader[13]);
//						sendMsg(localRequestHeader[2]);
//						
//					}catch(Exception e){
//						e.printStackTrace();
//						System.out.println("�������ۿ���");
//						sendError = "9";	// �۽źҰ�
//					}
//
//					/* ��û����ڵ忡 ���� ���� */
//					String xRcv = "";		// �ܺα������ �޴������� �����ϴ� ����
//					String recvError = "0";
//					if(!sendError.equals("9")){
//						try{
//							xRcv   = rcvMsg();
//							//xRcv  += rcvMsg();
//						}catch(Exception e){
//							System.out.println("�������ſ���");
//							recvError = "8";	// ���źҰ�.
//						}
//					}
//
//					try{
//						if(xRcv.getBytes().length == 0){
//							System.out.println("���ݵ������κ��� ���������� �����ϴ�");
//							recvError = "8";	// ���źҰ�.
//						}
//					}catch(Exception e){
//						System.out.println("����Ʈ Array ����üũ ����");
//						recvError = "8";		// ���źҰ�.
//					}					
//					
//					System.out.println("[�ſ������� ��������--------------------------------------]");
//					System.out.println("["+xRcv+"]");
//					System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
//						
//						
//					// ����� �������� ������� �Ľ��Լ� ȣ��
//					sToBytes = xRcv.getBytes();
//					String[] niceHeader = null;
//					
//					/* 
//					 * ���� ����� ����(����)
//					 * ����� ���������� ����θ� �Ľ��Ͽ� ���������� ���ð���θ� �����Ѵ�.
//					 */
//					String stsCode = "0";
//					if( sendError.equals("0")){
//						// �۽��� �����̸�
//						if( recvError.equals("0")){
//							// ������ �����̸�
//							stsCode = "0";		// ��Ʈ�� �������� �Ǵ�
//						}else{
//							if( recvError.equals("8") ){
//								stsCode = "8";	// ���ſ����� �Ǵ�
//							}else{
//								stsCode = "7";	// ������������� �Ǵ�
//							}
//						}
//					}else{
//						// �۽��� �����̸�
//						stsCode = "9";			// �۽ſ����� �Ǵ�(��Ʈ�� �������)
//					}
					
					System.out.println("===== ���ÿ� ������ ������ =======");
					System.out.println("[" + localRequestHeader[2] + "][" + localRequestHeader[2].getBytes().length + "]");
					System.out.println("===============================");
					String xRcv = "";
					if(str.length() > 300) xRcv = KibGram.createReturnMsg(localRequestHeader[2]);
					System.out.println("[���� ��������--------------------------------------]");
					System.out.println("["+xRcv+"]["+xRcv.getBytes().length + "]");
					System.out.println("[--------------------------------------------------]");
					// ���������� �����Ѵ�.
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
							System.out.println("�ſ������� ���� �ۼ����� ���������� ����:"+e.getMessage());
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
//					xin.close();
//					xout.close();					
//					xS.close();
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
				//System.out.println("========== ���� Write Start ==================================");
				xout.write(command);
				//System.out.println("========== ���� Write End  ===================================");
				xout.flush();
				//System.out.println("========== ���� frush End  ===================================");
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
		 * �ܺα�� ������ �޾Ƶ��δ�.
		 * @return a String
		 */
		public synchronized String rcvMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");

			try {
				int gramLength = 0;

				if (SendDaemonLocalReturn.nPORT == 28099 || SendDaemonLocalReturn.nPORT == 28100) { // 1F005 ������ �������̰� 10����Ʈ�� �´�.
					gramLength = 10;
				} else {                         // ���̿ܿ��� �������̰� 4����Ʈ�� �´�.
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
				//System.out.println("======== rcvMsg() �ſ������翡�� ���� ���� ===================");
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
		 * ���ð������ �Ľ�
		 * @param String ���ð������
		 * @return String[] ���ð������ �׸�迭
		 * Comment ȭ�鿡�� �ۼ��� �Ѿ�� �������+���������� �Ľ��Ͽ� ��Ʈ���迭�� �����Ѵ�. 
		 */
		public String[] parseOnlineCommonHeader(String req) throws Exception {
			if(req == null || req.length() == 0) return null;
			
			String[] attr = new String[3];

			try{
				attr[0] = subString(0, 6);		          // ������ȣ
				attr[1] = subString(6, 10);               // ��������
				attr[2] = subString(10, sToBytes.length); // �۽��� ��
				
				System.out.println("����� ������ �и��Ϸ��߽��ϴ�.");
			}catch(Exception e){
				e.printStackTrace();
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

	//////////////////////////////////////////////////
	// KIBNET�� ���� �ʰ� �޽��� �����ڵ� ������, ������
	//////////////////////////////////////////////////
	private static class KibGram implements IKibGramEnum {

		private static final int H����_�����ڵ� = 12;
		private static final String H_KB0200 = "KB0200"; 
		private static final int H����_�ŷ��İ����ܾ� = 2;
		private static final int H����_��ü������ݾ� = 13;
		
		private static final int H������������_���ڼ� = 4;
		private static final String H�����ڵ�_�������� = "000";
		private static final String H��������_�ڵ� = "00000088";
		private static final String H�츮����_�ڵ� = "00000020";
		private static final int wrAcntVal = 1000000000;
		private static final int shAcntVal = 1000000000;
		private static final int ������ü������ = 100;
		private static final int Ÿ����ü������ = 500;
		
		private static final int[] headerPosition = {
			      // index kibnet����
			0     //     1  1
			, 4   //  1  2  2 (  4)��������
			, 8   //  2  3  3 (  4)CS��ȣ
			, 16  //  3  4  4 (  8)CS���� ����ڵ�
			, 17  //  4  5  5 (  1)REACTION CODE
			, 20  //  5  6  6 (  3)���Ӱŷ���ȣ(3)
			, 21  //  6  7  7 (  1)�ۼ���FLAG(1)
			, 29  //  7  8  8 (  8)��ޱ���ڵ�(8)
			, 34  //  8  8  8 (  5)��޿������ڵ�(4)
			, 38  //  9 10  9 (  4)��޴ܸ��ڵ�(5)
			, 39  // 10 11 10 (  1)��ü(�߻�)����(1)
			, 43  // 11 12 11 (  4)���������ڵ�(MSG TYPE)(4)
			, 47  // 12 13 12 (  4)�ŷ������ڵ�(4)
			, 50  // 13 14 13 (  3)�����ڵ�(3)
			, 58  // 14 15 14 (  8)�ŷ�����(8)
			, 64  // 15 16 15 (  6)�ŷ��ð�(6)
			, 71  // 16 17 16 (  7)�ŷ��Ϸù�ȣ(7)
			, 72  // 17 18 17 (  1)�ѱ��ڵ屸��(1)
			, 73  // 18 19 18 (  1)�����ı���(1)
			, 81  // 19 20 19 (  8)��������ڵ�(8)
			, 82  // 20 21 20 (  1)KIB_USE_1 M/S TRACK��ȣ(1)
			, 223 // 21 21 21 (141)KIB_USE_2 M/S TRACK DATA(141)
			, 224 // 22 21 22 (  1)KIB_USE_3 ī�屸��(1)
			, 225 // 23 21 23 (  1)KIB_USE_4 ����ȭ�� ��ȸ ��� ����(1)
			, 226 // 24 21 24 (  1)KIB_USE_5 ��ü�ŷ��� ����������(1)
			, 240 // 25 21 25 ( 14)KIB_USE_6 USER WORK AREA(14)
			, 280 // 26 22 26 ( 40)�� �� MESSAGE(40)
			, 286 // 27 29 27 (  6)���ŷ����(6)
			, 290 // 28 29 28 (  4)FILLER1(4) ���������ڵ�(4)
			, 300 // 29 30 29 ( 10)FILLER(10)
		};

		private static final int[] bodyPosition = {
			      // index kibnet����
			0     //     1  1 -----�ݾ�����
			, 13  //  1  2  2 ( 13)�ŷ��ݾ�
			, 14  //  2  3  3 (  1)��,�� ����ǥ��
			, 27  //  3  4  4 ( 13)�ŷ��� �����ܾ�
			, 40  //  4  5  5 ( 13)�̰��� Ÿ���� �ݾ�
			//------------ ---�Աݰ��º�
			, 48  //  5  6  6 (  8)���ޱ���ڵ�    ( 8)
			, 50  //  6  7  7 (  2)�Աݰ��±����ڵ�( 2) - '00' ����
			, 66  //  7  8  8 ( 16)���¹�ȣ(����)  (16)
			, 86  //  8  8  8 ( 20)�Աݰ��¼���    (20)
		    //----------------��ݰ��º�
			, 94  //  9 10  9 (  8)����(���ı���ڵ�)( 8) - '00000020' or '00000088'
			, 96  // 10 11 10 (  2)��ݰ��±����ڵ�  ( 2) - '00'
			, 112 // 11 12 11 ( 16)���¹�ȣ(����)    (16)
			, 120 // 12 13 12 (  8)��й�ȣ          ( 8)
			, 140 // 13 14 13 ( 20)��ݰ��¼���      (20)
		    //----------------������
			, 145 // 14 15 14 (  5)������ݾ�        ( 5) - '00000'
			, 148 // 15 16 15 (  3)���ݸż�          ( 3) - '000'
			, 150 // 16 17 16 (  2)��ǥ�ż�          ( 2) - '00'
		    //----------------������-��ǥ 1 / 5
			, 158 // 17 18 17 (  8)��ǥ��ȣ     (8)
			, 164 // 18 19 18 (  6)��ǥ�������� (6)
			, 166 // 19 20 19 (  2)��ǥ����     (2)
		    //----------------������-��ǥ 2 / 5
			, 174 // 17 18 17 (  8)��ǥ��ȣ    (8)
			, 180 // 18 19 18 (  6)��ǥ��������(6)
			, 182 // 19 20 19 (  2)��ǥ����    (2)
		    //----------------������-��ǥ 3 / 5
			, 190 // 17 18 17 (  8)��ǥ��ȣ    (8)
			, 196 // 18 19 18 (  6)��ǥ��������(6)
			, 198 // 19 20 19 (  2)��ǥ����    (2)
		    //----------------������-��ǥ 4 / 5
			, 206 // 17 18 17 (  8)��ǥ��ȣ    (8)
			, 212 // 18 19 18 (  6)��ǥ��������(6)
			, 214 // 19 20 19 (  2)��ǥ����    (2)
		    //----------------������-��ǥ 5 / 5
			, 222 // 17 18 17 (  8)��ǥ��ȣ    (8)
			, 228 // 18 19 18 (  6)��ǥ��������(6)
			, 230 // 19 20 19 (  2)��ǥ����    (2)
		    //----------------������-���
			, 233 // 26 22 26 (  3)��һ���      ( 3)
			, 245 // 27 29 27 ( 12)�ŷ�������ȣ  (12)
			, 250 // 28 29 28 (  5)��м�����ݾ�( 5)
			, 256 // 28 29 28 (  6)�����ȣ      ( 6)
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
			// kibHeader[0] = subString(0,4); // ��������(4)
			// kibHeader[1] = subString(4,8); // CS��ȣ(4)
			// kibHeader[2] = subString(8,16); // CS���� ����ڵ�(8)
			// kibHeader[3] = subString(16,17); // REACTION CODE(1)
			// kibHeader[4] = subString(17,20); // ���Ӱŷ���ȣ(3)
			// kibHeader[5] = subString(20,21); // �ۼ���FLAG(1)
			// kibHeader[6] = subString(21,29); // ��ޱ���ڵ�(8)
			// kibHeader[7] = subString(29,33); // ��޿������ڵ�(4)
			// kibHeader[8] = subString(33,38); // ��޴ܸ��ڵ�(5)
			// kibHeader[9] = subString(38,39); // ��ü(�߻�)����(1)
			// kibHeader[h���������ڵ�] = subString(39,43); // ���������ڵ�(MSG TYPE)(4)
			// kibHeader[h�ŷ������ڵ�] = subString(43,47); // �ŷ������ڵ�(4)
			// kibHeader[12] = subString(47,50); // �����ڵ�(3)
			// kibHeader[13] = subString(50,58); // �ŷ�����(8)
			// kibHeader[14] = subString(58,64); // �ŷ��ð�(6)
			// kibHeader[15] = subString(64,71); // �ŷ��Ϸù�ȣ(7)
			// kibHeader[16] = subString(71,72); // �ѱ��ڵ屸��(1)
			// kibHeader[17] = subString(72,73); // �����ı���(1)
			// kibHeader[18] = subString(73,81); // ��������ڵ�(8)
			// kibHeader[19] = subString(81,82); // M/S TRACK��ȣ(1)
			// kibHeader[20] = subString(82,223); // M/S TRACK DATA(141)
			// kibHeader[21] = subString(223,224); // ī�屸��(1)
			// kibHeader[22] = subString(224,225); // ����ȭ�� ��ȸ ��� ����(1)
			// kibHeader[23] = subString(225,226); // ��ü�ŷ��� ����������(1)
			// kibHeader[24] = subString(226,240); // USER WORK AREA(14)
			// kibHeader[25] = subString(240,280); // ���� MESSAGE(40)
			// kibHeader[26] = subString(280,286); // ���ŷ����(6)
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
		 * ���ڿ� ����Ʈ�� ��� - Exception ���� �߻�
		 * 
		 * @return a String - ��û�� ����Ʈ ������ ���ڿ�
		 * @param int to Set - ���� ����Ʈ
		 * @param int to Set - ������ ����Ʈ
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
		 * �Աݴ�����Ұŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountAgentCancelTransaction() {
			return (header[h���������ڵ�].equals(�����ڵ�_�ŷ�����ڵ�)
					&& header[h�ŷ������ڵ�].equals(�ŷ�����_�Աݴ���ŷ�));
		}

		/**
		 * �Ա���Ұŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountCancelTransaction() {
			return (header[h���������ڵ�].equals(�����ڵ�_�ŷ�����ڵ�)
					&& header[h�ŷ������ڵ�].equals(�ŷ�����_�Աݰŷ�));
		}

		/**
		 * ������ȸ�ŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		@SuppressWarnings("unused")
		private boolean isReceiveReadTransaction() {
			return (header[h���������ڵ�].equals(�����ڵ�_�ŷ������ڵ�)
					&& header[h�ŷ������ڵ�].equals("4100"));
		}

		/**
		 * �Աݴ���ŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountAgentTransaction() {
			return (header[h���������ڵ�].equals(�����ڵ�_�ŷ������ڵ�)
					&& header[h�ŷ������ڵ�].equals("1300"));
		}

		/**
		 * �Աݰŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		@SuppressWarnings("unused")
		private boolean isInamountTransaction() {
			return (header[h���������ڵ�].equals(�����ڵ�_�ŷ������ڵ�)
					&& header[h�ŷ������ڵ�].equals(�ŷ�����_�Աݰŷ�));
		}

		/**
		 * ���ðŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		@SuppressWarnings("unused")
		private boolean isStartGram() {
			return (header[h���������ڵ�].equals(�����ڵ�_�����������ڵ�)
					&& header[h�ŷ������ڵ�].equals(�ŷ�����_���ðŷ�));
		}

		/**
		 * ����ŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		@SuppressWarnings("unused")
		private boolean isEndGram() {
			return (header[h���������ڵ�].equals(�����ڵ�_�����������ڵ�)
					&& header[h�ŷ������ڵ�].equals(�ŷ�����_����ŷ�));
		}

		/**
		 * ���� ���� �޼ҵ�
		 * @param msg - ����
		 * @return ����Ŭ����
		 * @throws Exception
		 */
		public static KibGram create(String msg) {
			return new KibGram(msg);
		}

		/**
		 * ���� ���� ���� �޼ҵ�
		 * @param msg - ����
		 * @return ����Ŭ����
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
			
			header[H����_�����ڵ�] = H�����ڵ�_��������;
			body[H����_�ŷ��İ����ܾ�] = "0001234500000";
			body[H����_��ü������ݾ�] = "00100";
			
			for(int i = 0; i < header.length; i++) {
				result += header[i];
			}
			for(int i = 0; i < body.length; i++) {
				result += body[i];
			}

			return result;
		}

		private String makeSocketMsg(String result) {
			return "0"+(result.getBytes().length+H������������_���ڼ�)+result;
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