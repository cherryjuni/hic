/*
 * ��ܰ� �¶��� ������ �����̴�.
 *
 * ��ܱ�����κ���  ������ �޾� ���������� �۽��ϴ� �����̴�.
 * �ܺα���� �� ��û�� �޾� ����� ���������� �Ǵ��Ѵ�.
 * �Ľ̵� ��������� ��������� ���� �� ����� ���������� �տ� ���̰�
 * �ش���� ����� ȣ��(����)�Ѵ�.
 * �� ������ ���������� �۵��Ѵ�.
 * ���� ȣ��� ��⿡ ������� ������������ �Ķ��Ÿ�� �ѱ��.
 * ȣ���� ����� �Լ��κ��� ���������� �ö����� ������ ���ŷ ���¿� ����.
 * ������ Ÿ�Ӿƿ�(Time-out) �ð��� ����ϸ� Time-out Exception �� �߻���Ű��
 * ��û������ ������ ������� ��ֻ��¸� �ڵ�� �����Ѵ�.
 */
package rsvr;

import java.sql.*;
import java.util.StringTokenizer;
import java.net.*;
import java.io.*;

import com.cabis.fw.dbutil.dbmanager.util.ConnMng;
import com.cabis.xutil.*;

import util.StringUtil;
import x.extr.util.*;

public class KibReceiver {
	public 	 static int sPORT = 9020;
    public static String inDbNm;
    public static String inUsrNm;
    public static String inPwd;
	Listener listener;

	/*
	 * ������
	 * Connection �����ڸ� �����ϰ� �����Ѵ�.
	 * Listener �� �����ϰ� �����Ѵ�.
	 */
	public KibReceiver() throws Exception {
		listener = new Listener();
		listener.start();
		System.out.println("Receive Daemon Started. Port No = " + sPORT);
	}

	/*
	 * ������ ���� �����Լ�
	 */
	public static void main(String[] args) throws Exception {
		try{
            /** input argument ���� 2007.03.23 pch */

            if (args.length <= 3 ) {
                System.out.println("�Է�����: 1.port, 2.dbNm, 3.user, 4.pwd");
                System.exit(-1);
            }

			KibReceiver.sPORT = Integer.parseInt(args[0]);
            inDbNm = args[1];
            inUsrNm = args[2];
            inPwd = args[3];

            if (KibReceiver.inDbNm.length() <= 0 ||
                 KibReceiver.inUsrNm.length() <= 0 ||
                 KibReceiver.inPwd.length() <= 0) {
                System.out.println("�Է�����:1.port[" + sPORT + "], 2.dbNm[" +
                                   inDbNm + "], 3.user[" + inUsrNm +
                                   "]" + "], 4.pwd[" + inPwd + "]");
                System.exit(-1);
            }

            System.out.println("�Է�����:1.port[" + sPORT + "], 2.dbNm[" +
                               inDbNm + "], 3.user[" + inUsrNm +
                               "]" + "], 4.pwd[" + inPwd + "]");

			KibReceiver server = new KibReceiver();
		}catch(Exception e){
			throw e;
		}
	}

    private Connection getConnection() {
    	Connection con = null;
    	try {
    		con  = ConnMng.getInstance("").getConnection();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return con; //
    	
//    	Connection conn = null;
//        
//    	try {
//            String driver = "oracle.jdbc.driver.OracleDriver";
//            String url = "jdbc:oracle:thin:@172.20.102.41:1521:"+inDbNm;
//            String user = inUsrNm;
//            String pwd = inPwd;
//
//            Class.forName(driver);
//            conn = DriverManager.getConnection(url, user, pwd);
//            System.out.println("Connection gain complete. ");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return conn;
        
    }

	/**
	* �űԿ�û
	* ���������� ��ٸ���.
	* ���ӵ� ������ Connection Manager �� ����Ѵ�.
	*/
	class Listener extends Thread {
		ServerSocket ss;
		public Listener() throws Exception {
			ss = new ServerSocket(KibReceiver.sPORT);
		}

		public void run() {
			for(;;) {
				try {
                    sleep(1);
                    System.out.println("=== accept waiting ===");
					Socket con = ss.accept();
                    System.out.println("=== accepted ===");
					Client client = new Client(con);
					client.start();
				} catch(Exception e) {
					System.out.println("======== KIB CMS �Ա�ó���� �����߻�(����) ==============");
					e.printStackTrace();
					System.out.println("======================================================");
				}
			}
		}
	}

	public class Client extends Thread {

		Connection con = getConnection();

		// �ܺα�� ��Ʈ��
		Socket xS = null;
		BufferedReader in;
		BufferedWriter out;

		private byte[] sToBytes;

		public Client(Socket con) throws Exception {
			xS = con;
			in  = new BufferedReader(new InputStreamReader(xS.getInputStream()));
		    out = new BufferedWriter(new OutputStreamWriter(xS.getOutputStream()));
		}

		public void run() {
			try {
				// ���ݿ�û�� �����Ѵ�.
				String xRcv = rcvKibMsg();
				System.out.println("[���ݼ�������-----------------------------------------------]");
				System.out.println("["+xRcv+"]  [" + xRcv.getBytes().length + "]");
				System.out.println("[---------------------------------------------------------]["+xRcv.getBytes().length+"]byte");

				xRcv = xRcv.substring(4);

				// �������� ������� �Ľ�
				String[] kibHeader = parseKibHeader(xRcv);

				// �������� ó��.
				if(isStartGram(kibHeader)){
					System.out.println("�����ŷ�����["+kibHeader[10]+"] ���������ڵ�["+kibHeader[11]+"] ������������");
					kibHeader[5]  = "5";	// �ۼ����÷���
					kibHeader[10] = "0810";	// ���������ڵ�

					String rtnLen  = xRcv.substring(0,4);
					String rtnGram = "";
					for(int i=0;i<kibHeader.length;i++){
						rtnGram += kibHeader[i];
					}
					rtnGram = rtnLen+rtnGram+xRcv.substring(300);
                    System.out.println("������������["+rtnGram+"]");
					sendMsg(rtnGram);
				} else if(kibHeader[10].equals("0800") && kibHeader[11].equals("2400")){
					System.out.println("�����ŷ�����["+kibHeader[10]+"] ���������ڵ�["+kibHeader[11]+"] ������������");
					kibHeader[5]  = "5";	// �ۼ����÷���
					kibHeader[10] = "0810";	// ���������ڵ�

					String rtnLen  = xRcv.substring(0,4);
					String rtnGram = "";
					for(int i=0;i<kibHeader.length;i++){
						rtnGram += kibHeader[i];
					}
					rtnGram = rtnLen+rtnGram;
					sendMsg(rtnGram);
				} else if (isInamountTransaction(kibHeader) ||  /* �Ա� */
                    isInamountAgentTransaction(kibHeader)   ||  /* �Աݴ��� */
                    isReceiveReadTransaction(kibHeader)     ||  /* ������ȸ */
                    isInamountCancelTransaction(kibHeader)  ||  /* ��� */
                    isInamountAgentCancelTransaction(kibHeader)) {  /* ��Ҵ��� */
					System.out.println("�����ŷ�����["+kibHeader[10]+"] ���������ڵ�["+kibHeader[11]+"] �Ա���������");

					String localHeaderMake = hicLocalHeadMake(xRcv, kibHeader);

					/*
					 * û���Ա��� �������α׷��� ȣ���Ͽ� ���������� �����޴´�.
					 */
					Object oa[] = {localHeaderMake,con};
					SyncCaller sc = new SyncCaller();

					/*
					 * ���⼭ ȣ���ϴ� �������α׷��� kibHeader �� ���������ڵ�� �ŷ������ڵ忡 ���� ����
					 * �ش��ϴ� ���α׷��� ȣ���Ѵ�. 0210 �����϶� ����Ÿ���� Ư���÷��� ���� ���� �ش����α׷���
					 * ȣ���Ѵ�(û��PL���� �ڼ��� �ٽ� ����� )
					 */
					//String oGram  = sc.callString("com.cabis.sc.r.co.cm.CmsRecvGramCC","procRecv",oa);
					String oGram  = sc.callString("com.cabis.sc.b.cp.cm.CmsRecvGramCC","procRecv",oa); //2008.06.10 �����丵 ��Ű����κ���
					

					// ������� �Ľ��� ���� ����Ʈ �迭�� ��ȯ�Ͽ� �д�.
					sToBytes = oGram.getBytes();
					/*
					 * ���ϰ���� ��������� �Ľ��Ѵ�.
					 */
					String[] localResponseHeader = parseOnlineCommonHeader(oGram);

					System.out.println("[������������-----------------------------------------]");
					System.out.println("localResponseHeader[0][" +localResponseHeader[0] +"]");
					System.out.println("localResponseHeader[1][" +localResponseHeader[1] +"]");
					System.out.println("localResponseHeader[2][" +localResponseHeader[2] +"]");
					System.out.println("localResponseHeader[3][" +localResponseHeader[3] +"]");
					System.out.println("localResponseHeader[4][" +localResponseHeader[4] +"]");
					System.out.println("localResponseHeader[5][" +localResponseHeader[5] +"]");
					System.out.println("localResponseHeader[6][" +localResponseHeader[6] +"]");
					System.out.println("localResponseHeader[7][" +localResponseHeader[7] +"]");
					System.out.println("localResponseHeader[8][" +localResponseHeader[8] +"]");
					System.out.println("localResponseHeader[9][" +localResponseHeader[9] +"]");
					System.out.println("localResponseHeader[10]["+localResponseHeader[10]+"]");
					System.out.println("localResponseHeader[11]["+localResponseHeader[11]+"]");
					System.out.println("localResponseHeader[12]["+localResponseHeader[12]+"]");
	//				System.out.println("localResponseHeader[13]["+localResponseHeader[13]+"]");
					System.out.println("[--------------------------------------------------]");

					/*
					 * ���������� ����ȣ��Ʈ�� �����Ѵ�.
					 */
					// ���� �պκп� ������ ���̸� 4�ڸ��� ä�� �����Ѵ�
	        	    int gramLen = localResponseHeader[13].getBytes().length+4;
	        	    String strLen = Integer.toString(gramLen);

					String localRspnCd = localResponseHeader[5].trim();
                    /** �ŷ��� ���� ���� 2007.03.22 P.C.H */
                    if (((isInamountTransaction(kibHeader) ||  /* �Ա� */
                          isInamountAgentTransaction(kibHeader) ||  /* �Աݴ��� */
                          isInamountCancelTransaction(kibHeader) ||  /* ��� */
                          isInamountAgentCancelTransaction(kibHeader)) && /* ��Ҵ��� */
                         localRspnCd.equals("000")) ||                                       /* ���� */
                        (isReceiveReadTransaction(kibHeader))) {  /* ������ȸ */
						// ��⿡�� ���������� ó���Ǿ������� ���������� ������
		        	    for( int i=strLen.length(); i<4 ; i++ ){
		        	    	strLen = "0"+strLen;
		        	    }
						sendMsg(strLen+localResponseHeader[13]);
						System.out.println("���������� KIB ����ȣ��Ʈ�� ����-----------------------------------------");
						System.out.println("["+strLen+localResponseHeader[13]+"]["+(strLen+localResponseHeader[13]).getBytes().length+"]");
						System.out.println("-------------------------------------------------------------------------");
						
						/* �����α׸� ����Ѵ�. *****************************************/
						writeGramLog(con, "", strLen+localResponseHeader[13], xRcv, "S", "");	
						/* �����α׸� ��� ��. ******************************************/
					} else {
						for( int i=strLen.length(); i<4 ; i++ ){
		        	    	strLen = "0"+strLen;
		        	    }
						sendMsg(strLen+localResponseHeader[13]);
						System.out.println("����ó�� ���(������ �Ǵ� ���ø����̼� ����)-------------------------------");
						System.out.println("["+strLen+localResponseHeader[13]+"]["+(strLen+localResponseHeader[13]).getBytes().length+"]");
						System.out.println("--------------------------------------------------------------------");
						
						/* �����α׸� ����Ѵ�. *****************************************/
						writeGramLog(con, "", strLen+localResponseHeader[13], xRcv, "F", "");	
						/* �����α׸� ��� ��. ******************************************/
					}
					con.commit();
				}else{
					System.out.println("�����ŷ�����["+kibHeader[10]+"] ���������ڵ�["+kibHeader[11]+"] �˼���������");
					/* �����α׸� ����Ѵ�. *****************************************/
					writeGramLog(con, "", "", "�����ŷ�����["+kibHeader[10]+"] ���������ڵ�["+kibHeader[11]+"] �˼���������", "F", "");	
					/* �����α׸� ��� ��. ******************************************/
				}
				
				
				// �ణ�� �����ð��� �ΰ� ������ �����Ѵ�
				Thread.sleep(100);
				in.close();
				out.close();
				xS.close();
			} catch(Exception e) {
				System.out.println("======== KIB CMS �Ա�ó���� �����߻�(ó����) ==============");
				/* �����α׸� ����Ѵ�. *****************************************/
				writeGramLog(con, "", "", "", "F", e.getMessage());	
				/* �����α׸� ��� ��. ******************************************/
				e.printStackTrace();
				System.out.println("======================================================");
				try {
					in.close();
					out.close();
					xS.close();
				} catch (Exception ex) {;}
			} finally {
				try{
					con.close();
				}catch(Exception e){}
			}

		}

		private String hicLocalHeadMake(String xRcv, String[] kibHeader) {
			/*
			 * ���� ����� ���� - ������������� ����θ� �Ľ��Ͽ� ���������� ���ð���θ� �����Ѵ�.
			 */
			String localHeaderMake = "";
			localHeaderMake += "21";  					// ��ܱ���ڵ�(��û���� ����ڵ带 �״�� ����)(2)
			localHeaderMake += "R";  					// �ۼ��ſ���(1)
			localHeaderMake += kibHeader[10];			// ���������ڵ�(4)
			localHeaderMake += kibHeader[11];			// �ŷ������ڵ�(4)
			localHeaderMake += "00000000000000000000"; 	// ������ȣ(20)
			localHeaderMake += "0000000000";			// �ŷ��Ϸù�ȣ(10)
			localHeaderMake += "0000";					// �����ڵ�(4)
			localHeaderMake += "20060102";				// �ŷ�����(8)
			localHeaderMake += "101010";				// �ŷ��ð�(6)
			localHeaderMake += "0000";					// �����ͱ���(4)
			localHeaderMake += "00000001";				// ó���ܸ���ȣ(8)
			localHeaderMake += "SYSTEMOP";				// �ܸ���ϻ����ȣ(8)
			localHeaderMake += "1";						// �����ڵ�(1)
			localHeaderMake += "XXXXXXXXXXXXXXXXXXXX";	// ������������(20)
			localHeaderMake += xRcv;					// ����Ÿ����(��������)(����)
			
			return localHeaderMake;
		}

		/**
		 * �Աݴ�����Ұŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		private boolean isInamountAgentCancelTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0400") && kibHeader[11].equals("1300");
		}

		/**
		 * �Ա���Ұŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		private boolean isInamountCancelTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0400") && kibHeader[11].equals("1100");
		}

		/**
		 * ������ȸ�ŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		private boolean isReceiveReadTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0200") && kibHeader[11].equals("4100");
		}

		/**
		 * �Աݴ���ŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		private boolean isInamountAgentTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0200") && kibHeader[11].equals("1300");
		}

		/**
		 * �Աݰŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		private boolean isInamountTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0200") && kibHeader[11].equals("1100");
		}

		/**
		 * ���ðŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		private boolean isStartGram(String[] kibHeader) {
			return kibHeader[10].equals("0800") && kibHeader[11].equals("2100");
		}

		/**
		 * ����ŷ��ΰ�?
		 * 
		 * @return boolean - true(�׷���) / false(�ƴϴ�)
		 */
		private boolean isEndGram(String[] kibHeader) {
			return kibHeader[10].equals("0800") && kibHeader[11].equals("2400");
		}
		/**
		 * �ܺα���� ���������� �����Ѵ�.
		 * @param String to set
		 */
		public void sendMsg(String command) throws IOException {
			try {
				out.write(command);
				out.flush();
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {
			}
		}

		/*--------------------------------------------------------------------------------------------*/

		/**
		 * �ܺα�� ������ �޾Ƶ��δ�. KIB
		 * @return a String
		 */
		public String rcvKibMsg() throws IOException {
			StringBuffer rtnStr = new StringBuffer("");

			try {
				int response;
				int i = 0;
				int totCnt = 0;
				char[] cTot = new char[4];
				
				if (in.read(cTot) != -1) {
					rtnStr.append(new String(cTot));
					
					totCnt = StringUtil.cInt(StringUtil.CHSTRING(rtnStr.toString(), " ", "0"));

					// KIB�� �������̰� ������ ���ԵǹǷ� �̹̲��� 4 byte �� ���� ������.
					for (i = 0; i < (totCnt - 4); i++) {
						response = in.read();
						
						if (response > 255) {
							i++;
						}

						rtnStr.append((char) response);
					}
				}
			} catch (java.net.SocketException se) {
				se.printStackTrace();
				throw new IOException(se.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new IOException(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException(e.getMessage());
			} finally {

			}
			return rtnStr.toString();
		}
		/*--------------------------------------------------------------------------------------------*/

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
				attr[13] = subString(100,sToBytes.length);	// ����Ÿ����
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
			sToBytes = req.getBytes();

			try{
				attr[0]   = subString(0,4);		// ��������(4)
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
				

				System.out.println("attr[0]   : " + attr[0] ); 
			    System.out.println("attr[1]   : " + attr[1] );
			    System.out.println("attr[2]   : " + attr[2] );
			    System.out.println("attr[3]   : " + attr[3] );
			    System.out.println("attr[4]   : " + attr[4] );
			    System.out.println("attr[5]   : " + attr[5] );
			    System.out.println("attr[6]   : " + attr[6] );
			    System.out.println("attr[7]   : " + attr[7] );
			    System.out.println("attr[8]   : " + attr[8] );
			    System.out.println("attr[9]   : " + attr[9] );
			    System.out.println("attr[10]  : " + attr[10]);
			    System.out.println("attr[11]  : " + attr[11]);
			    System.out.println("attr[12]  : " + attr[12]);
			    System.out.println("attr[13]  : " + attr[13]);
			    System.out.println("attr[14]  : " + attr[14]);
			    System.out.println("attr[15]  : " + attr[15]);
			    System.out.println("attr[16]  : " + attr[16]);
			    System.out.println("attr[17]  : " + attr[17]);
			    System.out.println("attr[18]  : " + attr[18]);
			    System.out.println("attr[19]  : " + attr[19]);
			    System.out.println("attr[20]  : " + attr[20]);
			    System.out.println("attr[21]  : " + attr[21]);
			    System.out.println("attr[22]  : " + attr[22]);
			    System.out.println("attr[23]  : " + attr[23]);
			    System.out.println("attr[24]  : " + attr[24]);
			    System.out.println("attr[25]  : " + attr[25]);
			    System.out.println("attr[26]  : " + attr[26]);
			    System.out.println("attr[27]  : " + attr[27]);
			}catch(Exception e){
				throw e;
			}

			return attr;
		}

		/*
		 * KIB �������� �Ľ�
		 * @param String KIB ����
		 * @return String[] KIB �������� �׸�迭
		 * Comment : KIB ���������� �����׸����� �и��Ѵ�.
		 */
		public String[] parseMngGram(String req) throws Exception {
			String[] attr = new String[10];
			sToBytes = req.getBytes();

			try{
				attr[0]   = subString(300,308);	// ��������(8)
				attr[1]   = subString(308,309);	// �������ı���(1)
				attr[2]   = subString(309,310);	// ���ϱ���(1)
				attr[3]   = subString(310,311);	// ��������(1)
				attr[4]   = subString(311,312);	// ��ֱ���(1)
				attr[5]   = subString(312,322);	// HOST�ý��� ���û��� ��������(10)
				attr[6]   = subString(322,422);	// ����������(100)
				attr[7]   = subString(422,427);	// EFMS SAF �Ǽ�(5)
				attr[8]   = subString(427,432);	// HOST �� ��� SAF �Ǽ�(5)
				attr[9]   = subString(432,500);	// FILLER(68)
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

	}
	
	private void writeGramLog(Connection con, String gramCd, String sendGram, String recvGram, String sfFg, String logMsg) 
	{
		System.out.println("���� �α׸� ��� ===================");
		PreparedStatement pstmtlog = null;
		String logNo = gramCd + System.currentTimeMillis();
		String logSql = "INSERT INTO GUSER.GSCT_GRAM_LOG(GRAM_LOG_NO, GRAM_CD, EXEC_DT, EXEC_TM, TRSM_GRAM, RECV_GRAM, SNR_DIV, SUCS_FAIL_FG, LOG_MSG) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			pstmtlog = con.prepareStatement(logSql);
			pstmtlog.setString(1, logNo);
			pstmtlog.setString(2, gramCd);
			pstmtlog.setString(3, getDay());
			pstmtlog.setString(4, getTime());
			pstmtlog.setString(5, sendGram);
			pstmtlog.setString(6, recvGram);
			pstmtlog.setString(7, "R");
			pstmtlog.setString(8, sfFg);
			pstmtlog.setString(9, logMsg);
			
			pstmtlog.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmtlog.close();
			} catch(Exception e) {
				
			}
		}
	}
	
	private String getDay()
    {
        Timestamp timestamp = new Timestamp( System.currentTimeMillis() );

        String retVal = "";

        String temp = timestamp.toString().substring( 0, 10 );
        StringTokenizer st = new StringTokenizer( temp, "- :" );
        while ( st.hasMoreElements() )
        {
            retVal += st.nextElement();
        }

        return retVal;
    }

    private String getTime()
    {
        Timestamp timestamp = new Timestamp( System.currentTimeMillis() );

        String retVal = "";

        String temp = timestamp.toString().substring( 11, 19 );
        StringTokenizer st = new StringTokenizer( temp, "- :" );
        while ( st.hasMoreElements() )
        {
            retVal += st.nextElement();
        }

        return retVal;
    }

}
