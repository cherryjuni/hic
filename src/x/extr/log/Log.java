/** Main system          : foundation
  * Sub system           : log
  * Classname            : Log.java
  * Initial date         : 2005.11.14
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : Log파일처리를 위한 공통 Class
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
*/

package x.extr.log;

import java.io.*;
import java.sql.*;
import x.extr.lib.PropertyManager;
import x.extr.lib.MessageManager;


/** Log를 파일에 쓰기 위한 클래스<p>
  * 모든 메소드는 log 파일에 쓰게되어 있음 만약 log를 쓰다가 error가 발생하면<p>
  * stdout(JVM console)으로 log가 redirect
  * @author 황재천
 */
public class Log {
	/** Debug용 Log Level */
	public static final short LOG_DEBUG = 1;
	/** Error 발생에 대한 Log Level  */
	public static final short LOG_ERROR = 2;
	/** 데이터 베이스에 접근할 수 없는 것과 같은 심각한 에러에 대한 Log Level  */
	public static final short LOG_CRITICAL = 3;
	/** 기본적인 Log Level(필요시 설정)  */
	private static final short DEFAULT_SEVERITY = LOG_DEBUG;
	/** 기본적으로 사용되는 Log 파일의 접두사  */
	private static final String DEFAULT_LOG_FILE_PREFIX = "LOG";
	/** 기본적으로 사용되는 Log 파일의 접미사(확장자)  */
	private static final String LOG_FILE_SUFFIX = ".log";

	/** Final Attributes */
	private static final int START_INDEX_OF_YEAR_IN_HASHTABLE = 0;
	private static final int END_INDEX_OF_YEAR_IN_HASHTABLE = 4;
	private static final int START_INDEX_OF_MONTH_IN_HASHTABLE = 4;
	private static final int END_INDEX_OF_MONTH_IN_HASHTABLE = 6;
	private static final int START_INDEX_OF_DATE_IN_HASHTABLE = 6;
	private static final int END_INDEX_OF_DATE_IN_HASHTABLE = 8;

	private static final int START_INDEX_OF_YEAR_IN_TIMESTAMP = 0;
	private static final int END_INDEX_OF_YEAR_IN_TIMESTAMP = 4;
	private static final int START_INDEX_OF_MONTH_IN_TIMESTAMP = 5;
	private static final int END_INDEX_OF_MONTH_IN_TIMESTAMP = 7;
	private static final int START_INDEX_OF_DATE_IN_TIMESTAMP = 8;
	private static final int END_INDEX_OF_DATE_IN_TIMESTAMP = 10;
	private static final int START_INDEX_OF_DATETIME_IN_TIMESTAMP = 0;
	private static final int END_INDEX_OF_DATETIME_IN_TIMESTAMP = 19;

	private static final int CLASS_NAME_SIZE = 30;
	private static final int METHOD_NAME_SIZE = 25;
	private static final int ERROR_CODE_SIZE = 4;
	private static final int USER_SIZE = 19;
	public static int BUF_SIZE = 256;
	public static final int PREFIX = 1;
	public static final int SUFFIX = 0;

	/** Application Log에 대한 Level  */
	private static short logSeverity = DEFAULT_SEVERITY;
	/** Log File 접두사  */
	private static String fileNamePrefix = DEFAULT_LOG_FILE_PREFIX;
	/** Log File 이름  */
	private static String fileName;
	/** Log 파일에 쓰여진 Message의 마지막 시간  */
	private static String lastLogDateTime;
	/** 이 클래스가 초기화 됐는지 아닌지 Check하는 변수  */
	private static boolean isInitialized = false;
	/** 이 클래스의 이름  */
	private static final String className = "x.extr.log.Log";
	/**  sample data  */
	public static final String MSG_NO_ERROR = "ComSSE0001"; // Servlet Exception
	/**  Console error  */
	public static final String LOG_VERBOSE = "LOG_VERBOSE";


	/** 이 클래스가 초기화 됐는지 Check하는 메소드
      * @return boolean
	  */
	public static boolean isInitialized() {
		return isInitialized;
	}


	/** Log에 저장할 메세지를  생성 시간을 포함하는 특정 Format의 String으로 변경하는 메소드
	  * @param message            메시지문자열
	  * @return String
	  */
	private static String formatMessage(String message) {
		String dateTime = "";
		// 256byte의 StringBuffre를 생성
		StringBuffer dateBuf = new StringBuffer(BUF_SIZE);
		// currentDate 객체 생성
		java.util.Date currentDate = new java.util.Date();
		//  현재 시간을 구함
		Timestamp ts = new Timestamp(currentDate.getTime());

		// TimeStamp 객체로부터 datetime을 정해진 형식대로 얻는 것
		dateBuf.append(ts.toString().substring(START_INDEX_OF_DATETIME_IN_TIMESTAMP,END_INDEX_OF_DATETIME_IN_TIMESTAMP));
		dateBuf.append(" |");
		// dateTime이 저장
		dateTime = dateBuf.toString();

		//StringBuffer 생성
		StringBuffer buffer = new StringBuffer(BUF_SIZE);
		//buffer에 message 앞에 dateTime 추가
		message = buffer.append(dateTime).append(message).toString();
		return message;
	}


	/** LOG_DEBUG에 해당되는 내용을 저장할 File의 스트림을 생성한다.
	  * @param sysName             kiis.properties에 정의된 시스템명
	  * @return PrintWriter
	*/
	private static PrintWriter openDFile(String sysName) {
		PrintWriter fileHandle = null;
		String fileDir = "";

		java.util.Date currentDate = new java.util.Date();
		//현재 시간을 얻음
		Timestamp ts = new Timestamp(currentDate.getTime());
		String tmpMonth = "";
		String tmpDate = "";
		tmpMonth = ts.toString().substring(START_INDEX_OF_MONTH_IN_TIMESTAMP,END_INDEX_OF_MONTH_IN_TIMESTAMP);
		tmpDate = ts.toString().substring(START_INDEX_OF_DATE_IN_TIMESTAMP,END_INDEX_OF_DATE_IN_TIMESTAMP);

		// making filename of the log file
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(fileNamePrefix);
		tmpBuffer.append("D");
		tmpBuffer.append(tmpMonth).append(tmpDate);
		tmpBuffer.append(LOG_FILE_SUFFIX);

		fileName = tmpBuffer.toString();
		try {
			if (sysName == null || sysName.equals("")) {
				fileDir = " ";
			} else {
				PropertyManager pm = PropertyManager.getInstance();
//				pm.init();
//				fileDir = (String)pm.get(sysName);
				fileDir = pm.getStr(sysName);
				File file = new File(fileDir);
				file.mkdirs();
			}
			// open log file in append mode 디렉토리 구조를 이곳에 써주면 된다. but 구별은 어떻게 그리고 어떻게 맞는 걸 가져오지?
			System.out.println("Log file : " + fileDir + fileName );
			fileHandle = new PrintWriter(new FileOutputStream( fileDir + fileName, true));
		} catch(FileNotFoundException f) {
			System.out.println("Failed to open Log file f:" + f.getMessage());
		} catch(IOException fExp) {
			System.out.println("Failed to open Log file fExp:" + fExp.getMessage());
		} catch(Exception e) {
			System.out.println("Failed to open Log file e:" + e.getMessage());
			//e.getMessage();
		}
		return fileHandle;
	}


	/** LOG_ERROR에 해당되는 내용을 저장할 file의 스트림을 생성한다.
	 * @param sysName             kiis.properties에 정의된 시스템명
	 * @return PrintWriter
	*/
	private static PrintWriter openEFile(String sysName) {
		PrintWriter fileHandle = null;
		String fileDir = "";
		java.util.Date currentDate = new java.util.Date();
		//현재 시간을 얻음
		Timestamp ts = new Timestamp(currentDate.getTime());
		String tmpMonth = "";
		String tmpDate = "";
		tmpMonth = ts.toString().substring(START_INDEX_OF_MONTH_IN_TIMESTAMP,END_INDEX_OF_MONTH_IN_TIMESTAMP);
		tmpDate = ts.toString().substring(START_INDEX_OF_DATE_IN_TIMESTAMP,END_INDEX_OF_DATE_IN_TIMESTAMP);

		// making filename of the log file
		StringBuffer tmpBuffer = new StringBuffer();

		tmpBuffer.append(fileNamePrefix);
		tmpBuffer.append("E");
		tmpBuffer.append(tmpMonth).append(tmpDate);
		tmpBuffer.append(LOG_FILE_SUFFIX);

		fileName = tmpBuffer.toString();
		try {
			if (sysName == null || sysName.equals("")) {
				fileDir = " ";
			} else {
				PropertyManager pm = PropertyManager.getInstance();
//				pm.init();
//				fileDir = (String)pm.get(sysName);
				fileDir = pm.getStr(sysName);
				//해당 디렉토리가 있는지 없는지를 확인하고 있으면 생성하지 않고 씀.
				File file = new File(fileDir);
				file.mkdirs();
			}
			// open log file in append mode
			fileHandle = new PrintWriter(new FileOutputStream(fileDir + fileName, true));
		} catch(FileNotFoundException f) {
			System.out.println("Failed to open Log file:" + f.getMessage());
		} catch(IOException fExp) {
			System.out.println("Failed to open Log file:" + fExp.getMessage());
		} catch(Exception e) {
			System.out.println("Failed to open Log file:" + e.getMessage());
			e.printStackTrace();
			//e.getMessage();
		}

		return fileHandle;
	}


	/** Log file에 특정 Message를 쓰는 작업을 하는 클래스.<p>
      * 먼저 fileHandler가 Open되어 있는지 확인.  In case of no filehandler,<p>
      * 만약 Handler가 없으면 새로 생성하고 쓴다.<p>
      * 날짜가 지나면 기존 handler는 close하고 새로운 file Handler를 생성한다.<p>
      * @param message                      log에 저장할 메세지
      * @param severity                     severity Error의 Severity
      * @param sysName                      kiis.properties에 정의된 시스템명
	  */
	private static void logMessage(String message,String severity,String sysName)
	{
		PrintWriter fileHandle = null;
		String padMonth = "";
		String padDay = "";
		java.util.Date currentDate = new java.util.Date();
		//현재 시간을 Timestamp로 저장
		Timestamp ts = new Timestamp(currentDate.getTime());
		//ts에서 day 부분만을 추출
		int tmpDay = Integer.parseInt(ts.toString().substring(START_INDEX_OF_DATE_IN_TIMESTAMP,END_INDEX_OF_DATE_IN_TIMESTAMP));
		//ts에서 month 부분만을 추출
		int tmpMonth = Integer.parseInt(ts.toString().substring(START_INDEX_OF_MONTH_IN_TIMESTAMP,END_INDEX_OF_MONTH_IN_TIMESTAMP));
		//ts에서 year 부분만을 추출
		int tmpYear = Integer.parseInt(ts.toString().substring(START_INDEX_OF_YEAR_IN_TIMESTAMP,END_INDEX_OF_YEAR_IN_TIMESTAMP));
		//tmpMonth가 10보다 작으면 padMonth는 0을 설정
		if (tmpMonth < 10) {
			padMonth = "0";
		} 
		//tmpDay가 10보다 작으면 padDay는 0을 설정
		if (tmpDay < 10) {
			padDay = "0";
		}

		//check whether the file is already open
		if (fileHandle == null) {
			//opening the log file
			int tmpSev = Integer.parseInt(severity);

			switch(tmpSev) {
				case 1:
					fileHandle = openDFile(sysName);
					break;
				case 2:
					fileHandle = openEFile(sysName);
					break;
				case 3:
					fileHandle = openEFile(sysName);
					break;
			}

			if (fileHandle != null) {
				StringBuffer lastLogTimestamp = new StringBuffer();

				//create the new timestamp
				lastLogTimestamp.append(tmpYear);
				lastLogTimestamp.append(padMonth).append(tmpMonth);
				lastLogTimestamp.append(padDay).append(tmpDay);
				lastLogDateTime = lastLogTimestamp.toString();
			}
		} else  {
			// check if there is a day rollover. Then close the old file and
			// make a new file.

			if (lastLogDateTime != null) {
				int logYear = Integer.parseInt(lastLogDateTime.substring(START_INDEX_OF_YEAR_IN_HASHTABLE,END_INDEX_OF_YEAR_IN_HASHTABLE));
				int logMonth = Integer.parseInt(lastLogDateTime.substring(START_INDEX_OF_MONTH_IN_HASHTABLE,END_INDEX_OF_MONTH_IN_HASHTABLE));
				int logDay = Integer.parseInt(lastLogDateTime.substring(START_INDEX_OF_DATE_IN_HASHTABLE,END_INDEX_OF_DATE_IN_HASHTABLE));

				if (tmpDay > logDay || tmpMonth > logMonth || tmpYear > logYear) {
					// making filename of the log file
					StringBuffer lastLogTimestamp = new StringBuffer();

					lastLogTimestamp.append(tmpYear);
					lastLogTimestamp.append(padMonth).append(tmpMonth);
					lastLogTimestamp.append(padDay).append(tmpDay);
					lastLogDateTime = lastLogTimestamp.toString();

					fileHandle.close();

					int tmpSev = Integer.parseInt(severity);
					switch(tmpSev) {
						case 1:
							fileHandle = openDFile(sysName);
							break;
						case 2:
							fileHandle = openEFile(sysName);
							break;
						case 3:
							fileHandle = openEFile(sysName);
							break;
					}
				}
			}
		}

		if (fileHandle != null) {
			StringBuffer out = new StringBuffer();
			out.append(message);
			fileHandle.println(out);
			fileHandle.flush();
			fileHandle.close();
		} else {
			System.out.println(message);
		}
	}


	/** 특정 Log 파일을 사용하기위해 필요한 정보를 ini 파일에서 읽어와서 Log 클래스를 초기화하는 작업
	  */
	private static void initialize() {
		String strMethod = "initialize";

		if (isInitialized) {
			return;
		}

		try {
		//	LOG_FILE_PATH_PREFIX = "LOG_PATH_PREFIX"
			fileNamePrefix = "Log";
			isInitialized = true;
		} catch (Exception e) {
			System.out.println("Failed to retrieve log file path/prefix and severity level from INI file!");
			System.out.println("Using default path/prefix: " + fileNamePrefix + ", log level: " + String.valueOf(logSeverity));
		}
	}


	/** 특정한 String 값을 원하는 포맷의 크기로 변경해서 반환
      * @param iPos                      specifing PREFIX or POSTFIX
      * @param sToFormat                 포맷전 문자열
      * @param iFinalLen                 total length of formatted string
      * @param cToPad                    char used for padding
      * @return String
      */
	public static String sFormatString(int iPos,String sToFormat,int iFinalLen, char cToPad) {
		int iLoopCount;
		int iCount = iFinalLen - sToFormat.length();
		switch(iPos) {
			case 1:// prefix
				for(iLoopCount = 0;iLoopCount<iCount;iLoopCount++)
					sToFormat = ""+cToPad+sToFormat;
					break;
			case 0:// postfix
				for(iLoopCount = 0;iLoopCount<iCount;iLoopCount++)
					sToFormat += cToPad;
					break;
		}
		return sToFormat;
	}


	/** Log file에 특정한 형태의 메세지를 Write하는 메소드<p>
      *  - Class 객체를 직접 매개 변수로 전달<p>
      *  - 사용자 정보가 없음<p>
      * Class name, method name, Error code and severity.<p>
      * <pre>Example:
      *          String strMethod = "methodName";
      *          ...
      *          writeLog(this, strMethod, ErrorCode, severity, "Message");</pre>
      * @param sysName                     저장할 로그의 시스템명
      * @param classObj                    에러가 발생한 오브젝트
      * @param method                      에러가 발생한 메소드
      * @param sErrorCode                  발생한 에러코드
      * @param severity                    The message severity
      * @param message                     에러메시지
      * @throws Exception
      */
	public static void writeLog(String sysName, Object classObj, String method, String sErrorCode,short severity, String message) throws Exception {
		initialize();
		String sErrorMessage;
		String tmpSev = "";

		if (severity < logSeverity ) {
			return;
		}

		//class 이름을 가져온다.
		String classStr = classObj.getClass().getName();

		//Error Code에 해당하는 message를 가져온다.
		try {
			MessageManager mm = MessageManager.getInstance();
			sErrorMessage = strToKorean(mm.getMessage(sErrorCode));
		} catch (Exception e) {
			sErrorMessage = "";
		}

		message = sErrorMessage + "\n  |" + message;

		//form the message String
		StringBuffer dataBuf = new StringBuffer(BUF_SIZE);

		if(severity == LOG_DEBUG ) {
			dataBuf.append("D").append(" |");
		} else if(severity == LOG_ERROR ) {
			dataBuf.append("E").append(" |");
		} else if(severity == LOG_CRITICAL ) {
			dataBuf.append("C").append(" |");
		}
		//Globals.SUFFIX = 0, CLASS_NAME_SIZE=30
		dataBuf.append(sFormatString(SUFFIX, classStr, CLASS_NAME_SIZE, ' ') ).append(" |");
		//Globals.SUFFIX = 0, METHOD_NAME_SIZE=25
		dataBuf.append(sFormatString(SUFFIX, method, METHOD_NAME_SIZE, ' ') ).append(" |");
		//Globals.SUFFIX = 0, ERROR_CODE_SIZE = 4
		dataBuf.append(sFormatString(SUFFIX, sErrorCode, ERROR_CODE_SIZE, ' ') ).append(" |");
		//Globals.SUFFIX = 0, ERROR_CODE_SIZE = 4
		dataBuf.append(sFormatString(SUFFIX, "", ERROR_CODE_SIZE, ' ') ).append(" |");
		dataBuf.append(message);
		message = dataBuf.toString();

		//log message에 timestamp 추가하는 formatMessage 호출
		message = formatMessage(message);
		tmpSev = Short.toString(severity);
		printDebug(message,tmpSev);
		//파일에 저장하는 logMesage 호출
		logMessage(message,tmpSev,sysName);
	}



	/** Log file에 특정한 형태의 메세지를 Write하는 메소드<p>
      *  - Class 객체를 직접 매개 변수로 전달<p>
      *  - 사용자 정보가 있음<p>
      * Class name, method name, Error code and severity.<p>
      * <pre>Example:
      *          String strMethod = "methodName";
      *          ...
      *          writeLog(this, strMethod, ErrorCode, severity, "Message");</pre>
      * @param sysName                     저장할 로그의 시스템명
      * @param classObj                    에러가 발생한 오브젝트
      * @param method                      에러가 발생한 메소드
      * @param sErrorCode                  발생한 에러코드
      * @param severity                    The message severity
      * @param message                     에러메시지
      * @param user                        사용자ID
      */
	public static void writeLog(String sysName, Object classObj, String method,String sErrorCode, short severity, String message, String user) {
		String sErrorMessage;
		String tmpSev;
		initialize();

		if (severity < logSeverity ) {
			return;
		}

		// get the classname
		String classStr = classObj.getClass().getName();

		//get Message Corresponding to Error Code
		try {
			MessageManager mm = MessageManager.getInstance();
			sErrorMessage = strToKorean(mm.getMessage(sErrorCode));
		} catch (Exception e) {
			sErrorMessage = "";
		}

		message = sErrorMessage + "\n  |" + message;

		//form the message String
		StringBuffer dataBuf = new StringBuffer(BUF_SIZE);

		if(severity == LOG_DEBUG ) {
			dataBuf.append("D").append(" |");
		} else if(severity == LOG_ERROR ) {
			dataBuf.append("E").append(" |");
		} else if(severity == LOG_CRITICAL ) {
			dataBuf.append("C").append(" |");
		}
		dataBuf.append(sFormatString(SUFFIX, classStr, CLASS_NAME_SIZE, ' ') ).append(" |");
		dataBuf.append(sFormatString(SUFFIX, method, METHOD_NAME_SIZE, ' ') ).append(" |");
		dataBuf.append(sFormatString(SUFFIX, sErrorCode, ERROR_CODE_SIZE, ' ') ).append(" |");
		dataBuf.append(sFormatString(SUFFIX, user, ERROR_CODE_SIZE, ' ') ).append(" |");
		dataBuf.append(message);
		message = dataBuf.toString();

		//call formatMessage to add timestamp to the log message
		message = formatMessage(message);
		tmpSev = Short.toString(severity);
		//Log_verbose가 true인지 false 인지에 따라 console에 출력을 할 것인지 결정
		printDebug(message,tmpSev);
		//call logMessage method to write to file
		logMessage(message,tmpSev,sysName);
	}


	/** Log file에 특정한 형태의 메세지를 Write하는 메소드<p>
      *  - Class 이름을 매개 변수로 전달<p>
      *  - 사용자 정보가 없음<p>
      * <pre>Example: 
      *
      *          String strMethod = "methodName";
      *          ...
      *          writeLog(this, strMethod, Log.LOG_ERROR, "Message", "John Doe");</pre>
      * @param sysName                     저장할 로그의 시스템명
      * @param classStr                    에러가 발생한 Class명
      * @param method                      에러가 발생한 메소드
      * @param sErrorCode                  발생한 에러코드
      * @param severity                    The message severity
      * @param message                     에러메시지
      */
	public static void writeLog(String sysName, String classStr, String method, String sErrorCode, short severity, String message) {
		String sErrorMessage;
		String tmpSev;
		initialize();

		if (severity < logSeverity ) {
			return;
		}

		//get Message Corresponding to Error Code
		try {
			MessageManager mm = MessageManager.getInstance();
			sErrorMessage = strToKorean(mm.getMessage(sErrorCode));
		} catch (Exception e) {
			sErrorMessage = "";
		}

		message = sErrorMessage + "\n  |" + message;

		//form the message String
		StringBuffer dataBuf = new StringBuffer(BUF_SIZE);

		if(severity == LOG_DEBUG ) {
			dataBuf.append("D").append(" |");
		} else if(severity == LOG_ERROR ) {
			dataBuf.append("E").append(" |");
		} else if(severity == LOG_CRITICAL ) {
			dataBuf.append("C").append(" |");
		}

		dataBuf.append(sFormatString(SUFFIX, classStr, CLASS_NAME_SIZE, ' ') ).append(" |");
		dataBuf.append(sFormatString(SUFFIX, method, METHOD_NAME_SIZE, ' ') ).append(" |");
		dataBuf.append(sFormatString(SUFFIX, sErrorCode, ERROR_CODE_SIZE, ' ') ).append(" |");
		dataBuf.append(sFormatString(SUFFIX, "", ERROR_CODE_SIZE, ' ') ).append(" |");
		dataBuf.append(message);
		message = dataBuf.toString();

		//call formatMessage to add timestamp to the log message
		message = formatMessage(message);
		tmpSev = Short.toString(severity);
		printDebug(message,tmpSev);
		//call logMessage method to write to file
		logMessage(message,tmpSev,sysName);
	}



	/** Log file에 특정한 형태의 메세지를 Write하는 메소드<p>
      *  - Class 이름을 매개 변수로 전달<p>
      *  - Error 코드가 없음<p>
      *  - 사용자 정보가 없음<p>
      * @param sysName                     저장할 로그의 시스템명
      * @param classObj                    에러가 발생한 오브젝트
      * @param method                      에러가 발생한 메소드
      * @param severity                    The message severity
      * @param message                     에러메시지
      */
	public static void writeLog(String sysName, Object classObj, String method, short severity, String message) {
		try {
			writeLog(sysName, classObj, method, "", severity,  message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/** Log file에 특정한 형태의 메세지를 Write하는 메소드<p>
      *  - Class 이름을 매개 변수로 전달<p>
      *  - Error 코드가 없음<p>
      *  - 사용자 정보 있음<p>
      * @param sysName                     저장할 로그의 시스템명
      * @param classObj                    에러가 발생한 오브젝트
      * @param method                      에러가 발생한 메소드
      * @param severity                    The message severity
      * @param message                     에러메시지
      * @param user                        사용자ID
      */
	public static void writeLog(String sysName,Object classObj, String method, short severity, String message, String user) {
		writeLog( sysName, classObj, method,"", severity, message, user);
	}


	/** Log file에 특정한 형태의 메세지를 Write하는 메소드<p>
      *  - Class 이름을 매개 변수로 전달<p>
      *  - Error 코드가 없음<p>
      *  - 사용자 정보 없음<p>
      * @param sysName                     저장할 로그의 시스템명
      * @param classStr                    에러가 발생한 Class명
      * @param method                      에러가 발생한 메소드
      * @param severity                    The message severity
      * @param message                     에러메시지
      */
	public static void writeLog(String sysName,String classStr, String method, short severity, String message) {
		writeLog(sysName,classStr, method, "", severity, message);
	}


	/** 결과를 화면에 출력한다.
      * @param s                화면에 출력할 메시지
      */
	public static void err(String s) {
		System.out.println(s);
	}


	/** message.properties의 Log_Debug가 true, false 인지에 따라 파일에 저장하는지 콘솔에 뿌리는지 설정
      * Log_Verbose가 true이면 console에 출력
      * @param message                     에러메시지
      * @param severity                    The message severity
      */
	public static void printDebug(String message, String severity) {
		int sev = Integer.parseInt(severity);
		String sLogVerbose = null;
		try {
			MessageManager mm = MessageManager.getInstance();
			sLogVerbose = mm.getMessage(LOG_VERBOSE);
			boolean bLogVerbose = true;

			if (!sLogVerbose.equals("FALSE") && sLogVerbose != "FALSE" && sLogVerbose.length() > 0) {
				bLogVerbose = true;
			} else {
				bLogVerbose = false;
			}

			if (bLogVerbose) {
				switch(sev) {
					case 1:
						err(message);
						break;
					case 2:
						break;
					case 3:
						break;
				}
			}
		} catch(Exception e) {
			e.getMessage();
		}
	}


	/** "8859_1"타입을 "UTF-8"타입으로 변환
	 * @param strSrc          변환전 문자열
	 * @return String
	*/
	public static String strToKorean(String strSrc) {
		String strDest = "";
		String strMethod = "strToKorean";
		String strClassName = "Log";

		try {
			//ASCII 타입의 문자를 KSC5601로 변환
//			strDest = new String(strSrc.getBytes("8859_1"), "KSC5601");
			strDest = new String(strSrc.getBytes("8859_1"), "UTF-8");
		} catch(UnsupportedEncodingException ueEx) {
			ueEx.getMessage();
		}
		return strDest;
	}
}
