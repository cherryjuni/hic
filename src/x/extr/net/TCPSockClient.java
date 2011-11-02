/** Main system          : x
  * Sub system           : net
  * Classname            : TCPSockClient.java
  * Initial date         : 2005.11.15
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : TCP Client Socket의 기본 Class
  * Version information  : v 1.0
  *
*/

package x.extr.net;


import java.net.*;
import java.io.*;

/** TCP Client Socket의 기본 Class
  * @author 황재천
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

	/** 생성자
      * @param host                접속할 IP Address
	  * @param port                접속할 Port
	  * @exception IOException
	  */
	public TCPSockClient(String host,int port) throws IOException {
		if (sock == null) {
			init(host,port);
		}
	}
	/** 생성자 - 디폴트(152.149.138.242 - 9001)로 접속함
	  * @exception IOException
	  */
	public TCPSockClient() throws IOException {
		System.out.println("소켓오브젝트만 생성");
		/*
		this("127.0.0.1",9001);
		*/
	}

	/** 소켓상태를 점검한다.(현재 기능 구현중)
      */
	public void check() throws IOException {
		sock.setKeepAlive(true);
		sock.getOutputStream().write(new byte[1],0,0); // write 0
		int i = sock.getInputStream().read(new byte[1],0,0); // read i
		return;
	}

	/** TCPSockClinet의 정보를 문자열로 리턴한다.
      * @return
      */
	public String toString() {
		if (sock == null) {
			return "Socket is null";
		} else {
			return sock.toString();
		}
	}

	/** 서버에 접속하고, 입출력스트림을 생성한다.
      * @param host                접속할 IP Address
      * @param port                접속할 Port
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

	/** 서버와 접속을 종료한다.
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

	/** 소켓오브젝트를 null 로 초기화 한다. 
     * @exception IOException
     * @Comment 2006.01.10 황재천 추가
     */
	public void setNull() {
		sock = null;
	}
	
	/** 문자열 데이터를 전송한다.
      * @param data             전송할 문자열
      * @exception IOException
      */
	public void send(String data) throws IOException {
		dos.write(data.getBytes());
		dos.flush();	// added by hjc
//		dos.writeUTF(data);
	}

	/** BYTE 데이터를 전송한다.
      * @param data              전송할 Byte[]
	  * @param offset            byte[]중 자료를 전송할 시작위치
	  * @param len               전송할 데이터의 길이
	  * @exception IOException
      */
	public void send(byte[] data,int offset,int len) throws IOException {
		String snData = new String(data,offset,len);
		dos.write(data,offset,len);
		dos.flush();	// added by hjc	
//		dos.writeUTF(data);
	}

	/** 데이터를 수신한다.
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
				 * 스트림에 읽을 데이타(바이트)가 남아있을경우 계속읽는다.
				 * 스트림이 닫혔을경우(스트림의 끝) -1을 리턴한다. 
				 * 읽은 바이트의 수를 리턴한다.
				 * bData의 길이가 10000 이므로 10000 바이트를 읽고 지나간다. 
				 */
				cnt = dis.read(bData);

				// 읽은 바이트수를 누적 
				receivedSum += cnt;
				System.out.println("received Data byte size->" + cnt); 
				
				// 처음 10바이트는 데몬에서 총길이를 붙인것으로 제거하고 화면으로 넘긴다.
				if(bExec){
					// 바이트배열을 스트링으로 변환
					sTemp = new String(bData,10,cnt-10);
					sData += sTemp;
				}else{
					// 바이트배열을 스트링으로 변환 
					sTemp = new String(bData,0,cnt);
					sData += sTemp;
				}
				
				//전문의 길이부분은 두번 읽어서는 안된다.
				if ( bExec){  
					sLength = new String(bData,0,10);//헤더에서 전문길이를 가져온다.
					iTotLength = Integer.parseInt(sLength);
					
					bExec = false;
				}
				//전문길이만큼 다 수신한 경우 종료
				if (iTotLength <= receivedSum-10){
					break;  
				}
			}
		}
		
		System.out.println("TotLength = [" + sLength + "] ;" + "receivedSumLen = [" + receivedSum + "]"); 
		return sData;
	}

}