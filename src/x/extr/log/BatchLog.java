/** Main system         : x.extr
 * Sub system           : log
 * Classname            : BatchLog.java
 * Initial date         : 2005.11.14
 * Author               : Ȳ��õ
 * Update date/Modifier : 
 * Description          : Log����ó���� ���� ���� Class
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
	
	//log file��
	private String logSysName = null;
	private String logClassName = null;
	private String logMethodName = null;
	private final String LOG_EXT = ".log";                                 //log Ȯ���ڸ�
	
	private boolean isLogRunning = false;	                                 //�αװ� start�Ǿ�����üũ�ϴ� flag
	private boolean isLogAppend  = false;                                  //�α����� ���½� �������Ͽ� append���� ����
	private String LOG_PATH = null;
	private final String SLASH = File.separator;                           //system dependent ���ϱ�����
	private final	String NEWLINE = System.getProperty("line.separator");   //system dependent newline ����
	
	private Writer writer = null;
	
	//���� �ð�
	private long startTimeMill;
	private String startTime;
	
	/** constructor
	  */
	public BatchLog() {
		this("","","");
	}
	
	
	/* constructor
	 * @param logSysName    String �ý��۸�
	 * @param logClassName  String Ŭ������
	 * @param logMethodName String �޼ҵ��
	 */

	public BatchLog(String logSysName, String logClassName, String logMethodName) {

		this.logSysName = logSysName;

		this.logClassName = logClassName;

		this.logMethodName = logMethodName;
		
		//log ���丮 ����
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
	  * ������ ������, Ŭ������, �޼ҵ������ �α������� �����Ѵ�
	  * @param sysName    String �ý��۸� ex) X1
	  * @param className  String Ŭ������
	  * @param methodName String �޼ҵ�� 
	  */
	public void startLog(String sysName, String className, String methodName) throws Exception, IOException {
		String today = DateUtils.getToday();
		File logFile;
		Writer writer =null;
		
		//�̹� �αװ� ���µǾ����� �˻��Ѵ�
		if(isLogRunning) throw new Exception("method startLog error : Log file already open!");
		
		//ó�� �����̸�
		isLogRunning = true;
		
		this.logSysName    = sysName;
		this.logClassName  = className;
		this.logMethodName = methodName;
		
		//log flag�� ���� output stream ����
		if(this.logFlag == LOG_STDOUT) {      //standard out���� ������ ���
			writer = new BufferedWriter(new OutputStreamWriter(System.out));
		}
		else if(this.logFlag == LOG_FILE) {   //file �� ������ ���
			writer = new BufferedWriter(new FileWriter(this.getLogFileName(), this.getLogAppend()));
		}
		else throw new Exception("LOG FLAG error!, illegal log flag value, value = " + logFlag);
		
		//writer setting		
		this.setLogFileWriter(writer);
		
		//start message �ۼ�
		StringBuffer startMessage = new StringBuffer();
		
		startMessage.append("*****************************************************************").append(NEWLINE);
		startMessage.append("SYSTEM NAME : ").append(logSysName).append(NEWLINE);
		startMessage.append("CLASS  NAME : ").append(logClassName).append(NEWLINE);
		startMessage.append("METHOD NAME : ").append(logMethodName).append(NEWLINE);
		startMessage.append("*****************************************************************").append(NEWLINE);
		startMessage.append(this.getLogPrefix()).append("BATCH START").append(NEWLINE);
		
		//���� �ð� ����
		startTime = BatchUtility.getCurrentTime();
		startTimeMill = System.currentTimeMillis();
		
		//write!!
		writer.write(startMessage.toString());
		writer.flush();
	}

	/** log file end
	  * �ش� �α� ������ �ݴ´�
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
	  * �α����ϻ�����ġ
	  * @return String �α����ϸ�
	  */
	private String getLogFileName() {
		StringBuffer buf = new StringBuffer();
		String today = DateUtils.getToday();

		//�⺻ ��ġ
		buf.append(LOG_PATH);
		//system��
		buf.append(SLASH).append(logSysName.toUpperCase());
		//����
		buf.append(SLASH).append(today);
		//Ŭ������
		buf.append("_").append(logClassName);
		//�޼ҵ��
		buf.append("_").append(logMethodName);
		//�α�Ȯ����
		buf.append(LOG_EXT);
		
		//System.out.println("log file name : " + buf.toString());
		return buf.toString();
	}
	
	
	/** write batch log 
	  * �������α׷����� ȣȯ�������� ����
	  */
	public void writeLog(String sysName, String className, String method, short severity, String message) {		
		this.writeLog(message);
	}
	
	
	/** write batch log
	  * �������α׷����� ȣȯ�������� ����
	  */
	public void writeLog(String sysName, String className, String method, String sErrorCode,short severity, String message) {
		this.writeLog(message);
	}
	
	/** Batch log write
	  * @param log String �α����Ͽ� ���� �α� �޼���
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




	/** Log�� ������ �����ϴ� �޼ҵ�
	  * @param logFlag  BatchDAO�� LOG_STDOUT, LOG_FILE, LOG_BOTH��
	  */
	public void setLogOutput(int logFlag) {
		//check validation
		//System.out.println(logFlag);
		if(logFlag == LOG_STDOUT || logFlag == LOG_FILE || logFlag == LOG_BOTH) this.logFlag = logFlag;
	}
	
	/** Log�� ���� ���
	  * @return int 
	  */
	public int getLogOutput() {
		return this.logFlag;
	}
	

	/** file log append���� ����
	  * @param flag boolean true�� append, false�� append���� ����
	  */
	public void setLogAppend(boolean flag) {
		this.isLogAppend = flag;
	}
	
	/** file log append ����
	  * @return boolean append����
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




	/** �ѱۺ�ȯ �޼ҵ� 
	  * ���� ��ǻ�Ϳ��� �ѱ��� ���� ��ȯ�Ѵ� EUC-KR -> 8859_1��
	  * @param data          ��ȯ�� ��Ʈ��
	  * @return String       ��ȯ ��Ʈ��
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