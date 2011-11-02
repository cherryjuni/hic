/** Main system          : x
  * Sub system           : net
  * Classname            : TCPSockClient.java
  * Initial date         : 2005.11.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : TCP Client Socket�� �⺻ Class
  * Version information  : v 1.0
  *
*/

package x.extr.net;


import java.net.*;
import java.io.*;

/** TCP Client Socket�� �⺻ Class
  * @author Ȳ��õ
  */
public class TCPSockClient {
	
	String sSystem = "net";
	String sClassName = "TCPSockClient";

	public static final int NOT_INIT = 0;
	public static final int CONNECT = 1;
	public static final int DISCONNECT = 2;

	private static final long MAX_RECV_TIMEOUT = 90000; 

	private Socket sock = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	public static int state = NOT_INIT;

	/** ������
      * @param host                ������ IP Address
	  * @param port                ������ Port
	  * @exception IOException
	  */
	public TCPSockClient(String host,int port) throws IOException {
		if (sock == null) {
			init(host,port);
		}
	}
	/** ������ - ����Ʈ(152.149.138.242 - 9001)�� ������
	  * @exception IOException
	  */
	public TCPSockClient() throws IOException {
		System.out.println("���Ͽ�����Ʈ�� ����");
		/*
		this("127.0.0.1",9001);
		*/
	}

	/** ���ϻ��¸� �����Ѵ�.(���� ��� ������)
      */
	public void check() throws IOException {
		sock.setKeepAlive(true);
		sock.getOutputStream().write(new byte[1],0,0); // write 0
		int i = sock.getInputStream().read(new byte[1],0,0); // read i
		return;
	}

	/** TCPSockClinet�� ������ ���ڿ��� �����Ѵ�.
      * @return
      */
	public String toString() {
		if (sock == null) {
			return "Socket is null";
		} else {
			return sock.toString();
		}
	}

	/** ������ �����ϰ�, ����½�Ʈ���� �����Ѵ�.
      * @param host                ������ IP Address
      * @param port                ������ Port
      * @exception IOException
      */
	public void init(String host,int port) throws IOException {
		if (sock == null) {
			sock = new Socket(host,port);
			dos = new DataOutputStream(sock.getOutputStream());
			dis = new DataInputStream(sock.getInputStream());

			state = CONNECT;
			sock.setKeepAlive(true);
			System.out.println("--------------------------------------------------------");
			System.out.println("Completed: Client Socket Connect to " + host + ":" + port);
			System.out.println("--------------------------------------------------------");
		}
	}

	/** ������ ������ �����Ѵ�.
      * @exception IOException
      */
	public void close() throws IOException {
		if (sock == null) {
			dis.close();
			dos.close();
			sock.close();
		
			state = DISCONNECT;
		}
	}

	/** ���Ͽ�����Ʈ�� null �� �ʱ�ȭ �Ѵ�. 
     * @exception IOException
     * @Comment 2006.01.10 Ȳ��õ �߰�
     */
	public void setNull() {
		sock = null;
	}
	
	/** ���ڿ� �����͸� �����Ѵ�.
      * @param data             ������ ���ڿ�
      * @exception IOException
      */
	public void send(String data) throws IOException {
		dos.write(data.getBytes());
		dos.flush();	// added by hjc
//		dos.writeUTF(data);
	}

	/** BYTE �����͸� �����Ѵ�.
      * @param data              ������ Byte[]
	  * @param offset            byte[]�� �ڷḦ ������ ������ġ
	  * @param len               ������ �������� ����
	  * @exception IOException
      */
	public void send(byte[] data,int offset,int len) throws IOException {
		String snData = new String(data,offset,len);
		dos.write(data,offset,len);
		dos.flush();	// added by hjc	
//		dos.writeUTF(data);
	}

	/** �����͸� �����Ѵ�.
	  * @return
	  * @exception IOException
      */
	public String recv() throws IOException {
		byte[] 	bData 	= new byte[10000];
		String 	sData 	= "";
		String 	sTemp 	= "";
		String 	sLength = "";
		
		int 	cnt 		= 0;
		int 	iTotLength 	= 0;
		long 	startTime	= 0;
		long 	endTime		= 0;
		int 	receivedSum = 0;
		boolean bExec 		= true;
		
		startTime = System.currentTimeMillis();
		while (true) {
			endTime = System.currentTimeMillis();
			
			/* 60 second */
			if ((endTime - startTime) > MAX_RECV_TIMEOUT) {
				System.out.println("Recv Timeout: i will return NULL frame");
				System.out.println("endTime - startTime=" + (endTime - startTime));
				System.out.println("MAX_RECV_TIMEOUT=" + MAX_RECV_TIMEOUT);
				sData = null;
				break;
			}
			
			try{Thread.sleep(10);}catch(Exception e){;}
			
			if ( sock.getInputStream().available() > 0) {
				/* 
				 * ��Ʈ���� ���� ����Ÿ(����Ʈ)�� ����������� ����д´�.
				 * ��Ʈ���� ���������(��Ʈ���� ��) -1�� �����Ѵ�. 
				 * ���� ����Ʈ�� ���� �����Ѵ�.
				 * bData�� ���̰� 10000 �̹Ƿ� 10000 ����Ʈ�� �а� ��������. 
				 */
				cnt = dis.read(bData);

				// ���� ����Ʈ���� ���� 
				receivedSum += cnt;
				System.out.println("received Data byte size->" + cnt); 
				
				// ó�� 10����Ʈ�� ���󿡼� �ѱ��̸� ���ΰ����� �����ϰ� ȭ������ �ѱ��.
				if(bExec){
					// ����Ʈ�迭�� ��Ʈ������ ��ȯ
					sTemp = new String(bData,10,cnt-10);
					sData += sTemp;
				}else{
					// ����Ʈ�迭�� ��Ʈ������ ��ȯ 
					sTemp = new String(bData,0,cnt);
					sData += sTemp;
				}
				
				//������ ���̺κ��� �ι� �о�� �ȵȴ�.
				if ( bExec){  
					sLength = new String(bData,0,10);//������� �������̸� �����´�.
					iTotLength = Integer.parseInt(sLength);
					
					bExec = false;
				}
				//�������̸�ŭ �� ������ ��� ����
				if (iTotLength <= receivedSum-10){
					break;  
				}
			}
		}
		
		System.out.println("TotLength = [" + sLength + "] ;" + "receivedSumLen = [" + receivedSum + "]"); 
		return sData;
	}

}