/*
 * 대외계 온라인 수신측 데몬이다.
 *
 * 대외기관으로부터  전문을 받아 응답전문을 송신하는 데몬이다.
 * 외부기관의 의 요청을 받아 기관과 전문종류를 판단한다.
 * 파싱된 로컬헤더의 기관정보에 따라 각 기관의 공통전문을 앞에 붙이고
 * 해당업무 모듈을 호출(실행)한다.
 * 이 데몬은 동기방식으로 작동한다.
 * 따라서 호출된 모듈에 기관에서 수신한전문을 파라메타로 넘긴다.
 * 호출한 모듈의 함수로부터 응답전문이 올때까지 소켓은 블록킹 상태에 들어간다.
 * 지정된 타임아웃(Time-out) 시간이 경과하면 Time-out Exception 을 발생시키고
 * 요청전문을 전송한 기관에에 장애상태를 코드로 리턴한다.
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
	 * 생성자
	 * Connection 관리자를 생성하고 구동한다.
	 * Listener 를 생성하고 구동한다.
	 */
	public KibReceiver() throws Exception {
		listener = new Listener();
		listener.start();
		System.out.println("Receive Daemon Started. Port No = " + sPORT);
	}

	/*
	 * 데몬의 시작 메인함수
	 */
	public static void main(String[] args) throws Exception {
		try{
            /** input argument 수정 2007.03.23 pch */

            if (args.length <= 3 ) {
                System.out.println("입력인자: 1.port, 2.dbNm, 3.user, 4.pwd");
                System.exit(-1);
            }

			KibReceiver.sPORT = Integer.parseInt(args[0]);
            inDbNm = args[1];
            inUsrNm = args[2];
            inPwd = args[3];

            if (KibReceiver.inDbNm.length() <= 0 ||
                 KibReceiver.inUsrNm.length() <= 0 ||
                 KibReceiver.inPwd.length() <= 0) {
                System.out.println("입력인자:1.port[" + sPORT + "], 2.dbNm[" +
                                   inDbNm + "], 3.user[" + inUsrNm +
                                   "]" + "], 4.pwd[" + inPwd + "]");
                System.exit(-1);
            }

            System.out.println("입력인자:1.port[" + sPORT + "], 2.dbNm[" +
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
	* 신규요청
	* 소켓접속을 기다린다.
	* 접속된 소켓은 Connection Manager 에 등록한다.
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
					System.out.println("======== KIB CMS 입금처리중 오류발생(대기부) ==============");
					e.printStackTrace();
					System.out.println("======================================================");
				}
			}
		}
	}

	public class Client extends Thread {

		Connection con = getConnection();

		// 외부기관 스트림
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
				// 원격요청을 수신한다.
				String xRcv = rcvKibMsg();
				System.out.println("[원격수신전문-----------------------------------------------]");
				System.out.println("["+xRcv+"]  [" + xRcv.getBytes().length + "]");
				System.out.println("[---------------------------------------------------------]["+xRcv.getBytes().length+"]byte");

				xRcv = xRcv.substring(4);

				// 수신전문 공통헤더 파싱
				String[] kibHeader = parseKibHeader(xRcv);

				// 개시전문 처리.
				if(isStartGram(kibHeader)){
					System.out.println("전문거래유형["+kibHeader[10]+"] 전문구분코드["+kibHeader[11]+"] 개시전문수신");
					kibHeader[5]  = "5";	// 송수신플래그
					kibHeader[10] = "0810";	// 전문구분코드

					String rtnLen  = xRcv.substring(0,4);
					String rtnGram = "";
					for(int i=0;i<kibHeader.length;i++){
						rtnGram += kibHeader[i];
					}
					rtnGram = rtnLen+rtnGram+xRcv.substring(300);
                    System.out.println("개시전문응답["+rtnGram+"]");
					sendMsg(rtnGram);
				} else if(kibHeader[10].equals("0800") && kibHeader[11].equals("2400")){
					System.out.println("전문거래유형["+kibHeader[10]+"] 전문구분코드["+kibHeader[11]+"] 종료전문수신");
					kibHeader[5]  = "5";	// 송수신플래그
					kibHeader[10] = "0810";	// 전문구분코드

					String rtnLen  = xRcv.substring(0,4);
					String rtnGram = "";
					for(int i=0;i<kibHeader.length;i++){
						rtnGram += kibHeader[i];
					}
					rtnGram = rtnLen+rtnGram;
					sendMsg(rtnGram);
				} else if (isInamountTransaction(kibHeader) ||  /* 입금 */
                    isInamountAgentTransaction(kibHeader)   ||  /* 입금대행 */
                    isReceiveReadTransaction(kibHeader)     ||  /* 수취조회 */
                    isInamountCancelTransaction(kibHeader)  ||  /* 취소 */
                    isInamountAgentCancelTransaction(kibHeader)) {  /* 취소대행 */
					System.out.println("전문거래유형["+kibHeader[10]+"] 전문구분코드["+kibHeader[11]+"] 입금전문수신");

					String localHeaderMake = hicLocalHeadMake(xRcv, kibHeader);

					/*
					 * 청구입금의 업무프로그램을 호출하여 응답전문을 돌려받는다.
					 */
					Object oa[] = {localHeaderMake,con};
					SyncCaller sc = new SyncCaller();

					/*
					 * 여기서 호출하는 업무프로그램은 kibHeader 의 전문구분코드와 거래구분코드에 따라 각각
					 * 해당하는 프로그램을 호출한다. 0210 전문일때 데이타부의 특정컬럼의 값에 따라 해당프로그램을
					 * 호출한다(청입PL한테 자세히 다시 물어볼것 )
					 */
					//String oGram  = sc.callString("com.cabis.sc.r.co.cm.CmsRecvGramCC","procRecv",oa);
					String oGram  = sc.callString("com.cabis.sc.b.cp.cm.CmsRecvGramCC","procRecv",oa); //2008.06.10 리팩토링 패키지경로변경
					

					// 공통헤더 파싱을 위해 바이트 배열로 변환하여 둔다.
					sToBytes = oGram.getBytes();
					/*
					 * 리턴결과의 공통헤더를 파싱한다.
					 */
					String[] localResponseHeader = parseOnlineCommonHeader(oGram);

					System.out.println("[로컬응답전문-----------------------------------------]");
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
					 * 로컬응답을 원격호스트에 리턴한다.
					 */
					// 전문 앞부분에 전문의 길이를 4자리로 채워 전송한다
	        	    int gramLen = localResponseHeader[13].getBytes().length+4;
	        	    String strLen = Integer.toString(gramLen);

					String localRspnCd = localResponseHeader[5].trim();
                    /** 거래에 따라 응답 2007.03.22 P.C.H */
                    if (((isInamountTransaction(kibHeader) ||  /* 입금 */
                          isInamountAgentTransaction(kibHeader) ||  /* 입금대행 */
                          isInamountCancelTransaction(kibHeader) ||  /* 취소 */
                          isInamountAgentCancelTransaction(kibHeader)) && /* 취소대행 */
                         localRspnCd.equals("000")) ||                                       /* 정상 */
                        (isReceiveReadTransaction(kibHeader))) {  /* 수취조회 */
						// 모듈에서 정상적으로 처리되었을때만 응답전문을 보낸다
		        	    for( int i=strLen.length(); i<4 ; i++ ){
		        	    	strLen = "0"+strLen;
		        	    }
						sendMsg(strLen+localResponseHeader[13]);
						System.out.println("응답전문을 KIB 원격호스트로 전송-----------------------------------------");
						System.out.println("["+strLen+localResponseHeader[13]+"]["+(strLen+localResponseHeader[13]).getBytes().length+"]");
						System.out.println("-------------------------------------------------------------------------");
						
						/* 전문로그를 기록한다. *****************************************/
						writeGramLog(con, "", strLen+localResponseHeader[13], xRcv, "S", "");	
						/* 전문로그를 기록 끝. ******************************************/
					} else {
						for( int i=strLen.length(); i<4 ; i++ ){
		        	    	strLen = "0"+strLen;
		        	    }
						sendMsg(strLen+localResponseHeader[13]);
						System.out.println("로컬처리 장애(디비오류 또는 어플리케이션 오류)-------------------------------");
						System.out.println("["+strLen+localResponseHeader[13]+"]["+(strLen+localResponseHeader[13]).getBytes().length+"]");
						System.out.println("--------------------------------------------------------------------");
						
						/* 전문로그를 기록한다. *****************************************/
						writeGramLog(con, "", strLen+localResponseHeader[13], xRcv, "F", "");	
						/* 전문로그를 기록 끝. ******************************************/
					}
					con.commit();
				}else{
					System.out.println("전문거래유형["+kibHeader[10]+"] 전문구분코드["+kibHeader[11]+"] 알수없는전문");
					/* 전문로그를 기록한다. *****************************************/
					writeGramLog(con, "", "", "전문거래유형["+kibHeader[10]+"] 전문구분코드["+kibHeader[11]+"] 알수없는전문", "F", "");	
					/* 전문로그를 기록 끝. ******************************************/
				}
				
				
				// 약간의 지연시간을 두고 소켓을 종료한다
				Thread.sleep(100);
				in.close();
				out.close();
				xS.close();
			} catch(Exception e) {
				System.out.println("======== KIB CMS 입금처리중 오류발생(처리부) ==============");
				/* 전문로그를 기록한다. *****************************************/
				writeGramLog(con, "", "", "", "F", e.getMessage());	
				/* 전문로그를 기록 끝. ******************************************/
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
			 * 로컬 공통부 구성 - 기관수신전문의 공통부를 파싱하여 얻은정보로 로컬공통부를 구성한다.
			 */
			String localHeaderMake = "";
			localHeaderMake += "21";  					// 대외기관코드(요청시의 기관코드를 그대로 리턴)(2)
			localHeaderMake += "R";  					// 송수신여부(1)
			localHeaderMake += kibHeader[10];			// 전문구분코드(4)
			localHeaderMake += kibHeader[11];			// 거래구분코드(4)
			localHeaderMake += "00000000000000000000"; 	// 관리번호(20)
			localHeaderMake += "0000000000";			// 거래일련번호(10)
			localHeaderMake += "0000";					// 응답코드(4)
			localHeaderMake += "20060102";				// 거래일자(8)
			localHeaderMake += "101010";				// 거래시간(6)
			localHeaderMake += "0000";					// 데이터길이(4)
			localHeaderMake += "00000001";				// 처리단말번호(8)
			localHeaderMake += "SYSTEMOP";				// 단말등록사원번호(8)
			localHeaderMake += "1";						// 상태코드(1)
			localHeaderMake += "XXXXXXXXXXXXXXXXXXXX";	// 예비정보내역(20)
			localHeaderMake += xRcv;					// 데이타본문(응답전문)(가변)
			
			return localHeaderMake;
		}

		/**
		 * 입금대행취소거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		private boolean isInamountAgentCancelTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0400") && kibHeader[11].equals("1300");
		}

		/**
		 * 입금취소거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		private boolean isInamountCancelTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0400") && kibHeader[11].equals("1100");
		}

		/**
		 * 수취조회거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		private boolean isReceiveReadTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0200") && kibHeader[11].equals("4100");
		}

		/**
		 * 입금대행거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		private boolean isInamountAgentTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0200") && kibHeader[11].equals("1300");
		}

		/**
		 * 입금거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		private boolean isInamountTransaction(String[] kibHeader) {
			return kibHeader[10].equals("0200") && kibHeader[11].equals("1100");
		}

		/**
		 * 개시거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		private boolean isStartGram(String[] kibHeader) {
			return kibHeader[10].equals("0800") && kibHeader[11].equals("2100");
		}

		/**
		 * 종료거래인가?
		 * 
		 * @return boolean - true(그렇다) / false(아니다)
		 */
		private boolean isEndGram(String[] kibHeader) {
			return kibHeader[10].equals("0800") && kibHeader[11].equals("2400");
		}
		/**
		 * 외부기관에 응답전문을 전송한다.
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
		 * 외부기관 응답을 받아들인다. KIB
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

					// KIB는 전문길이가 전문에 포함되므로 이미꺼낸 4 byte 는 빼고 꺼낸다.
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
		 * 로컬공통헤더 파싱
		 * @param String 로컬공통헤더
		 * @return String[] 로컬공통헤더 항목배열
		 * Comment 화면에서 작성해 넘어온 로컬헤더+본문전문을 파시하여 스트링배열로 리턴한다.
		 */
		public String[] parseOnlineCommonHeader(String req) throws Exception {
			String[] attr = new String[14];

			try{
				attr[0] = subString(0,2);		// 대외기관코드(2)
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
				attr[13] = subString(100,sToBytes.length);	// 데이타본문
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
			sToBytes = req.getBytes();

			try{
				attr[0]   = subString(0,4);		// 전문길이(4)
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
		 * KIB 관리전문 파싱
		 * @param String KIB 전문
		 * @return String[] KIB 관리전문 항목배열
		 * Comment : KIB 관리전문을 개별항목으로 분리한다.
		 */
		public String[] parseMngGram(String req) throws Exception {
			String[] attr = new String[10];
			sToBytes = req.getBytes();

			try{
				attr[0]   = subString(300,308);	// 기준일자(8)
				attr[1]   = subString(308,309);	// 마감전후구분(1)
				attr[2]   = subString(309,310);	// 휴일구분(1)
				attr[3]   = subString(310,311);	// 업무종류(1)
				attr[4]   = subString(311,312);	// 장애구분(1)
				attr[5]   = subString(312,322);	// HOST시스템 개시상태 예비정보(10)
				attr[6]   = subString(322,422);	// 장애은행상태(100)
				attr[7]   = subString(422,427);	// EFMS SAF 건수(5)
				attr[8]   = subString(427,432);	// HOST 및 기관 SAF 건수(5)
				attr[9]   = subString(432,500);	// FILLER(68)
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

	}
	
	private void writeGramLog(Connection con, String gramCd, String sendGram, String recvGram, String sfFg, String logMsg) 
	{
		System.out.println("전문 로그를 기록 ===================");
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
