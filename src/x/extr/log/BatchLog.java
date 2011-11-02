/** Main system         : x.extr
 * Sub system           : log
 * Classname            : BatchLog.java
 * Initial date         : 2005.11.14
 * Author               : 황재천
 * Update date/Modifier : 
 * Description          : Log파일처리를 위한 공통 Class
 * Version information  : v 1.0
 */



package x.extr.log;

import java.sql.*;
import java.io.*;

import x.extr.db.*;
import x.extr.lib.*;
import x.extr.util.*;
import x.extr.exception.*;



public class BatchLog {
	//batch log flag
	public static final int    LOG_STDOUT    = 1;
	public static final int    LOG_FILE      = 2;
	public static final int    LOG_BOTH      = 3;
	
	private int logFlag = LOG_FILE;
	private boolean isServer;
	
	//log file명
	private String logSysName = null;
	private String logClassName = null;
	private String logMethodName = null;
	private final String LOG_EXT = ".log";                                 //log 확장자명
	
	private boolean isLogRunning = false;	                                 //로그가 start되었는지체크하는 flag
	private boolean isLogAppend  = false;                                  //로그파일 오픈시 기존파일에 append할지 여부
	private String LOG_PATH = null;
	private final String SLASH = File.separator;                           //system dependent 파일구분자
	private final	String NEWLINE = System.getProperty("line.separator");   //system dependent newline 문자
	
	private Writer writer = null;
	
	//시작 시간
	private long startTimeMill;
	private String startTime;
	
	/** constructor
	  */
	public BatchLog() {
		this("","","");
	}
	
	
	/* constructor
	 * @param logSysName    String 시스템명
	 * @param logClassName  String 클래스명
	 * @param logMethodName String 메소드명
	 */

	public BatchLog(String logSysName, String logClassName, String logMethodName) {

		this.logSysName = logSysName;

		this.logClassName = logClassName;

		this.logMethodName = logMethodName;
		
		//log 디렉토리 설정
		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0) {
			LOG_PATH = "c:\\kiis\\log";
			this.isServer = false;
		} 
		else {			
			LOG_PATH = "/weblogic/bea/wlserver6.1/config/kiisdomain/applications/DefaultWebApp/log";
			this.isServer = true;
		}

	}

		

	/** log file start
	  * 지정된 업무명, 클래스명, 메소드명으로 로그파일을 생성한다
	  * @param sysName    String 시스템명 ex) X1
	  * @param className  String 클래스명
	  * @param methodName String 메소드명 
	  */
	public void startLog(String sysName, String className, String methodName) throws Exception, IOException {
		String today = DateUtils.getToday();
		File logFile;
		Writer writer =null;
		
		//이미 로그가 오픈되었는지 검사한다
		if(isLogRunning) throw new Exception("method startLog error : Log file already open!");
		
		//처음 시작이면
		isLogRunning = true;
		
		this.logSysName    = sysName;
		this.logClassName  = className;
		this.logMethodName = methodName;
		
		//log flag에 따른 output stream 지정
		if(this.logFlag == LOG_STDOUT) {      //standard out으로 지정된 경우
			writer = new BufferedWriter(new OutputStreamWriter(System.out));
		}
		else if(this.logFlag == LOG_FILE) {   //file 로 지정된 경우
			writer = new BufferedWriter(new FileWriter(this.getLogFileName(), this.getLogAppend()));
		}
		else throw new Exception("LOG FLAG error!, illegal log flag value, value = " + logFlag);
		
		//writer setting		
		this.setLogFileWriter(writer);
		
		//start message 작성
		StringBuffer startMessage = new StringBuffer();
		
		startMessage.append("*****************************************************************").append(NEWLINE);
		startMessage.append("SYSTEM NAME : ").append(logSysName).append(NEWLINE);
		startMessage.append("CLASS  NAME : ").append(logClassName).append(NEWLINE);
		startMessage.append("METHOD NAME : ").append(logMethodName).append(NEWLINE);
		startMessage.append("*****************************************************************").append(NEWLINE);
		startMessage.append(this.getLogPrefix()).append("BATCH START").append(NEWLINE);
		
		//시작 시간 설정
		startTime = BatchUtility.getCurrentTime();
		startTimeMill = System.currentTimeMillis();
		
		//write!!
		writer.write(startMessage.toString());
		writer.flush();
	}

	/** log file end
	  * 해당 로그 파일을 닫는다
	  */
	public void endLog() {	
		StringBuffer end;	
		Writer writer;
		long endTimeMill;
		int rMinutes, rSeconds;
		
		if(!isLogRunning) return;
		
		end = new StringBuffer();
		writer = this.getLogFileWriter();
		//setting flag
		isLogRunning = false;
		
		endTimeMill = System.currentTimeMillis();
		
		rMinutes = (int)((endTimeMill - startTimeMill)/1000L)/60;

		rSeconds = (int)((endTimeMill - startTimeMill)/1000L) % 60;
		
		end.append("*****************************************************************").append(NEWLINE);
		end.append("START  TIME : ").append(this.startTime).append(NEWLINE);
		end.append("END    TIME : ").append(BatchUtility.getCurrentTime()).append(NEWLINE);
		end.append("ELAPSE TIME : ").append(rMinutes).append(" minutes ").append(rSeconds).append(" seconds").append(NEWLINE);
		end.append("*****************************************************************").append(NEWLINE);
		
		try {
			writer.write(end.toString());
			writer.close();
		} catch(IOException e) {
			System.out.println("log file handling error!\n" + e.toString());	
		}
	}

	
	/** get log file name
	  * 로그파일생성위치
	  * @return String 로그파일명
	  */
	private String getLogFileName() {
		StringBuffer buf = new StringBuffer();
		String today = DateUtils.getToday();

		//기본 위치
		buf.append(LOG_PATH);
		//system명
		buf.append(SLASH).append(logSysName.toUpperCase());
		//일자
		buf.append(SLASH).append(today);
		//클래스명
		buf.append("_").append(logClassName);
		//메소드명
		buf.append("_").append(logMethodName);
		//로그확장자
		buf.append(LOG_EXT);
		
		//System.out.println("log file name : " + buf.toString());
		return buf.toString();
	}
	
	
	/** write batch log 
	  * 기존프로그램과의 호환성때문에 놔둠
	  */
	public void writeLog(String sysName, String className, String method, short severity, String message) {		
		this.writeLog(message);
	}
	
	
	/** write batch log
	  * 기존프로그램과의 호환성때문에 놔둠
	  */
	public void writeLog(String sysName, String className, String method, String sErrorCode,short severity, String message) {
		this.writeLog(message);
	}
	
	/** Batch log write
	  * @param log String 로그파일에 남길 로그 메세지
	  */
	public void writeLog(String log) {
		Writer writer;
		if(!isLogRunning) return; //throw new Exception("method writeLog error : log file does not open!");
		
		try {			
			writer = this.getLogFileWriter();
			writer.write(isServer ? toKorean(this.getLogPrefix() + log + NEWLINE) : this.getLogPrefix() + log + NEWLINE);
			writer.flush();

		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}




	/** Log의 방향을 지정하는 메소드
	  * @param logFlag  BatchDAO의 LOG_STDOUT, LOG_FILE, LOG_BOTH값
	  */
	public void setLogOutput(int logFlag) {
		//check validation
		//System.out.println(logFlag);
		if(logFlag == LOG_STDOUT || logFlag == LOG_FILE || logFlag == LOG_BOTH) this.logFlag = logFlag;
	}
	
	/** Log의 방향 얻기
	  * @return int 
	  */
	public int getLogOutput() {
		return this.logFlag;
	}
	

	/** file log append여부 설정
	  * @param flag boolean true면 append, false면 append하지 않음
	  */
	public void setLogAppend(boolean flag) {
		this.isLogAppend = flag;
	}
	
	/** file log append 여부
	  * @return boolean append여부
	  */
	public boolean getLogAppend() {
		return this.isLogAppend;
	}


	/** setter
	  */
	private void setLogFileWriter(Writer writer) {
		this.writer = writer;
	}
	
		
	/** getter
	  */
	public Writer getLogFileWriter() {
		return this.writer;
	}


	/** log prefix
	  */
	  
	private String getLogPrefix() {
		return ("[" + BatchUtility.getCurrentTime() + "]::");
	}




	/** 한글변환 메소드 
	  * 서버 컴퓨터에서 한글이 깨져 변환한다 EUC-KR -> 8859_1로
	  * @param data          변환전 스트링
	  * @return String       변환 스트링
	  */
	public String toKorean(String data) {
		String temp = null;
		
		try {
			temp =  new String(data.getBytes("EUC-KR"), "8859_1");
		} catch(UnsupportedEncodingException e) {
			System.out.println("convert error");
		}
		
		return temp;
	}

}