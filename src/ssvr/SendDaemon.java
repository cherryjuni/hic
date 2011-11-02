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
	 * ������
	 * Connection �����ڸ� �����ϰ� �����Ѵ�.
	 * Listener �� �����ϰ� �����Ѵ�.
	 */
	public SendDaemon() throws Exception {
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
			SendDaemon.nIP   = args[1];
			SendDaemon.nPORT = Integer.parseInt(args[2]);
			
			SendDaemon.sPORT = Integer.parseInt(args[0]);
			
			SendDaemon server = new SendDaemon();
			System.out.println("=========================================================");
			System.out.println(SendDaemon.nIP + " ���������� �����Ǿ����ϴ�......");
			System.out.println("=========================================================");
		}catch(IOException ie){
			System.out.println(SendDaemon.nIP + " ������ �⵿�ϴ��� ������ �߻��Ͽ� ������ �⵿�Ҽ� �����ϴ�.");
			System.out.println("=========================================================");
			System.out.println(ie.getMessage());
			System.out.println("=========================================================");
			//System.exit(-1);
		}catch(Exception e){
			System.out.println(SendDaemon.nIP + " ������ �⵿�ϴ��� ������ �߻��Ͽ� ������ �⵿�Ҽ� �����ϴ�.");
			System.out.println("=========================================================");
			System.out.println(e.getMessage());
			System.out.println("=========================================================");			
			System.out.println(SendDaemon.nIP + " ������ �����Ͽ����ϴ�.");
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
		private String IP   = SendDaemon.nIP;
		private int    PORT = SendDaemon.nPORT;
		
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
							xin.close();
							xout.close();					
							xS.close();

							break;
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					
					/* ��û����ڵ� ��ȯ */
					int orgCode = 00;
					
					// �ܺα���� ���Ͽ����ϰ� I/O ��Ʈ���� �����Ѵ�.
					String sendError = "0";
					
					try{
						// �ſ������翡 ���ο� ������ �����Ѵ�.
						System.out.println("--------------- ���ο� ���� ������ �����մϴ�.... ------------");
						System.out.println(IP + " : " + String.valueOf(PORT) + " ������ ����õ���.....");
						xS   = new Socket(IP,PORT);
						is   = xS.getInputStream();                            //(kkmin,20070117)
						xin  = new BufferedReader(new InputStreamReader(is));  //(kkmin,20070117)
						xout = new BufferedWriter(new OutputStreamWriter(xS.getOutputStream()));
						System.out.println(IP + " : " + String.valueOf(PORT) + " ������ �����߽��ϴ�.");
					}catch(Exception e){
						e.printStackTrace();
						System.out.println(IP + " : " + String.valueOf(PORT) + " ���� ������ �����߽��ϴ�.");
						sendError = "9";
					}
					
					/*
					 * ������� ������� ��ƼƼ ���� ���� ����� �����ϰ�
					 * ȭ�鿡�� ������ ����Ÿ������ �տ� ����� �ٿ� �ش������� �����Ѵ�
					 */
					
					try{
						System.out.println("===== �ſ������翡 ������ ������ =======");
						//System.out.println(localRequestHeader[13]);
						System.out.println("[" + localRequestHeader[2] + "][" + localRequestHeader[2].getBytes().length + "]");
						System.out.println("===============================");
				        //sendMsg(localRequestHeader[13]);
						sendMsg(localRequestHeader[2]);
						
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
							xRcv   = rcvMsg();
							//xRcv  += rcvMsg();
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
					
					System.out.println("[�ſ������� ��������--------------------------------------]");
					System.out.println("["+xRcv+"]");
					System.out.println("[--------------------------------------------------]["+xRcv.getBytes().length+"]����Ʈ");
						
						
					// ����� �������� ������� �Ľ��Լ� ȣ��
					sToBytes = xRcv.getBytes();
					String[] niceHeader = null;
					
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
					
					
					// ���������� �����Ѵ�.
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

				if (SendDaemon.nPORT == 28099 || SendDaemon.nPORT == 28100) { // 1F005 ������ �������̰� 10����Ʈ�� �´�.
					gramLength = 10;
				} else {                         // ���̿ܿ��� �������̰� 4����Ʈ�� �´�.
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
	
}