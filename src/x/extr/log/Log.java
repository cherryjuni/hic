/** Main system          : foundation
  * Sub system           : log
  * Classname            : Log.java
  * Initial date         : 2005.11.14
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : Log����ó���� ���� ���� Class
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


/** Log�� ���Ͽ� ���� ���� Ŭ����<p>
  * ��� �޼ҵ�� log ���Ͽ� ���ԵǾ� ���� ���� log�� ���ٰ� error�� �߻��ϸ�<p>
  * stdout(JVM console)���� log�� redirect
  * @author Ȳ��õ
 */
public class Log {
	/** Debug�� Log Level */
	public static final short LOG_DEBUG = 1;
	/** Error �߻��� ���� Log Level  */
	public static final short LOG_ERROR = 2;
	/** ������ ���̽��� ������ �� ���� �Ͱ� ���� �ɰ��� ������ ���� Log Level  */
	public static final short LOG_CRITICAL = 3;
	/** �⺻���� Log Level(�ʿ�� ����)  */
	private static final short DEFAULT_SEVERITY = LOG_DEBUG;
	/** �⺻������ ���Ǵ� Log ������ ���λ�  */
	private static final String DEFAULT_LOG_FILE_PREFIX = "LOG";
	/** �⺻������ ���Ǵ� Log ������ ���̻�(Ȯ����)  */
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

	/** Application Log�� ���� Level  */
	private static short logSeverity = DEFAULT_SEVERITY;
	/** Log File ���λ�  */
	private static String fileNamePrefix = DEFAULT_LOG_FILE_PREFIX;
	/** Log File �̸�  */
	private static String fileName;
	/** Log ���Ͽ� ������ Message�� ������ �ð�  */
	private static String lastLogDateTime;
	/** �� Ŭ������ �ʱ�ȭ �ƴ��� �ƴ��� Check�ϴ� ����  */
	private static boolean isInitialized = false;
	/** �� Ŭ������ �̸�  */
	private static final String className = "x.extr.log.Log";
	/**  sample data  */
	public static final String MSG_NO_ERROR = "ComSSE0001"; // Servlet Exception
	/**  Console error  */
	public static final String LOG_VERBOSE = "LOG_VERBOSE";


	/** �� Ŭ������ �ʱ�ȭ �ƴ��� Check�ϴ� �޼ҵ�
      * @return boolean
	  */
	public static boolean isInitialized() {
		return isInitialized;
	}


	/** Log�� ������ �޼�����  ���� �ð��� �����ϴ� Ư�� Format�� String���� �����ϴ� �޼ҵ�
	  * @param message            �޽������ڿ�
	  * @return String
	  */
	private static String formatMessage(String message) {
		String dateTime = "";
		// 256byte�� StringBuffre�� ����
		StringBuffer dateBuf = new StringBuffer(BUF_SIZE);
		// currentDate ��ü ����
		java.util.Date currentDate = new java.util.Date();
		//  ���� �ð��� ����
		Timestamp ts = new Timestamp(currentDate.getTime());

		// TimeStamp ��ü�κ��� datetime�� ������ ���Ĵ�� ��� ��
		dateBuf.append(ts.toString().substring(START_INDEX_OF_DATETIME_IN_TIMESTAMP,END_INDEX_OF_DATETIME_IN_TIMESTAMP));
		dateBuf.append(" |");
		// dateTime�� ����
		dateTime = dateBuf.toString();

		//StringBuffer ����
		StringBuffer buffer = new StringBuffer(BUF_SIZE);
		//buffer�� message �տ� dateTime �߰�
		message = buffer.append(dateTime).append(message).toString();
		return message;
	}


	/** LOG_DEBUG�� �ش�Ǵ� ������ ������ File�� ��Ʈ���� �����Ѵ�.
	  * @param sysName             kiis.properties�� ���ǵ� �ý��۸�
	  * @return PrintWriter
	*/
	private static PrintWriter openDFile(String sysName) {
		PrintWriter fileHandle = null;
		String fileDir = "";

		java.util.Date currentDate = new java.util.Date();
		//���� �ð��� ����
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
			// open log file in append mode ���丮 ������ �̰��� ���ָ� �ȴ�. but ������ ��� �׸��� ��� �´� �� ��������?
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


	/** LOG_ERROR�� �ش�Ǵ� ������ ������ file�� ��Ʈ���� �����Ѵ�.
	 * @param sysName             kiis.properties�� ���ǵ� �ý��۸�
	 * @return PrintWriter
	*/
	private static PrintWriter openEFile(String sysName) {
		PrintWriter fileHandle = null;
		String fileDir = "";
		java.util.Date currentDate = new java.util.Date();
		//���� �ð��� ����
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
				//�ش� ���丮�� �ִ��� �������� Ȯ���ϰ� ������ �������� �ʰ� ��.
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


	/** Log file�� Ư�� Message�� ���� �۾��� �ϴ� Ŭ����.<p>
      * ���� fileHandler�� Open�Ǿ� �ִ��� Ȯ��.  In case of no filehandler,<p>
      * ���� Handler�� ������ ���� �����ϰ� ����.<p>
      * ��¥�� ������ ���� handler�� close�ϰ� ���ο� file Handler�� �����Ѵ�.<p>
      * @param message                      log�� ������ �޼���
      * @param severity                     severity Error�� Severity
      * @param sysName                      kiis.properties�� ���ǵ� �ý��۸�
	  */
	private static void logMessage(String message,String severity,String sysName)
	{
		PrintWriter fileHandle = null;
		String padMonth = "";
		String padDay = "";
		java.util.Date currentDate = new java.util.Date();
		//���� �ð��� Timestamp�� ����
		Timestamp ts = new Timestamp(currentDate.getTime());
		//ts���� day �κи��� ����
		int tmpDay = Integer.parseInt(ts.toString().substring(START_INDEX_OF_DATE_IN_TIMESTAMP,END_INDEX_OF_DATE_IN_TIMESTAMP));
		//ts���� month �κи��� ����
		int tmpMonth = Integer.parseInt(ts.toString().substring(START_INDEX_OF_MONTH_IN_TIMESTAMP,END_INDEX_OF_MONTH_IN_TIMESTAMP));
		//ts���� year �κи��� ����
		int tmpYear = Integer.parseInt(ts.toString().substring(START_INDEX_OF_YEAR_IN_TIMESTAMP,END_INDEX_OF_YEAR_IN_TIMESTAMP));
		//tmpMonth�� 10���� ������ padMonth�� 0�� ����
		if (tmpMonth < 10) {
			padMonth = "0";
		} 
		//tmpDay�� 10���� ������ padDay�� 0�� ����
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


	/** Ư�� Log ������ ����ϱ����� �ʿ��� ������ ini ���Ͽ��� �о�ͼ� Log Ŭ������ �ʱ�ȭ�ϴ� �۾�
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


	/** Ư���� String ���� ���ϴ� ������ ũ��� �����ؼ� ��ȯ
      * @param iPos                      specifing PREFIX or POSTFIX
      * @param sToFormat                 ������ ���ڿ�
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


	/** Log file�� Ư���� ������ �޼����� Write�ϴ� �޼ҵ�<p>
      *  - Class ��ü�� ���� �Ű� ������ ����<p>
      *  - ����� ������ ����<p>
      * Class name, method name, Error code and severity.<p>
      * <pre>Example:
      *          String strMethod = "methodName";
      *          ...
      *          writeLog(this, strMethod, ErrorCode, severity, "Message");</pre>
      * @param sysName                     ������ �α��� �ý��۸�
      * @param classObj                    ������ �߻��� ������Ʈ
      * @param method                      ������ �߻��� �޼ҵ�
      * @param sErrorCode                  �߻��� �����ڵ�
      * @param severity                    The message severity
      * @param message                     �����޽���
      * @throws Exception
      */
	public static void writeLog(String sysName, Object classObj, String method, String sErrorCode,short severity, String message) throws Exception {
		initialize();
		String sErrorMessage;
		String tmpSev = "";

		if (severity < logSeverity ) {
			return;
		}

		//class �̸��� �����´�.
		String classStr = classObj.getClass().getName();

		//Error Code�� �ش��ϴ� message�� �����´�.
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

		//log message�� timestamp �߰��ϴ� formatMessage ȣ��
		message = formatMessage(message);
		tmpSev = Short.toString(severity);
		printDebug(message,tmpSev);
		//���Ͽ� �����ϴ� logMesage ȣ��
		logMessage(message,tmpSev,sysName);
	}



	/** Log file�� Ư���� ������ �޼����� Write�ϴ� �޼ҵ�<p>
      *  - Class ��ü�� ���� �Ű� ������ ����<p>
      *  - ����� ������ ����<p>
      * Class name, method name, Error code and severity.<p>
      * <pre>Example:
      *          String strMethod = "methodName";
      *          ...
      *          writeLog(this, strMethod, ErrorCode, severity, "Message");</pre>
      * @param sysName                     ������ �α��� �ý��۸�
      * @param classObj                    ������ �߻��� ������Ʈ
      * @param method                      ������ �߻��� �޼ҵ�
      * @param sErrorCode                  �߻��� �����ڵ�
      * @param severity                    The message severity
      * @param message                     �����޽���
      * @param user                        �����ID
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
		//Log_verbose�� true���� false ������ ���� console�� ����� �� ������ ����
		printDebug(message,tmpSev);
		//call logMessage method to write to file
		logMessage(message,tmpSev,sysName);
	}


	/** Log file�� Ư���� ������ �޼����� Write�ϴ� �޼ҵ�<p>
      *  - Class �̸��� �Ű� ������ ����<p>
      *  - ����� ������ ����<p>
      * <pre>Example: 
      *
      *          String strMethod = "methodName";
      *          ...
      *          writeLog(this, strMethod, Log.LOG_ERROR, "Message", "John Doe");</pre>
      * @param sysName                     ������ �α��� �ý��۸�
      * @param classStr                    ������ �߻��� Class��
      * @param method                      ������ �߻��� �޼ҵ�
      * @param sErrorCode                  �߻��� �����ڵ�
      * @param severity                    The message severity
      * @param message                     �����޽���
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



	/** Log file�� Ư���� ������ �޼����� Write�ϴ� �޼ҵ�<p>
      *  - Class �̸��� �Ű� ������ ����<p>
      *  - Error �ڵ尡 ����<p>
      *  - ����� ������ ����<p>
      * @param sysName                     ������ �α��� �ý��۸�
      * @param classObj                    ������ �߻��� ������Ʈ
      * @param method                      ������ �߻��� �޼ҵ�
      * @param severity                    The message severity
      * @param message                     �����޽���
      */
	public static void writeLog(String sysName, Object classObj, String method, short severity, String message) {
		try {
			writeLog(sysName, classObj, method, "", severity,  message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/** Log file�� Ư���� ������ �޼����� Write�ϴ� �޼ҵ�<p>
      *  - Class �̸��� �Ű� ������ ����<p>
      *  - Error �ڵ尡 ����<p>
      *  - ����� ���� ����<p>
      * @param sysName                     ������ �α��� �ý��۸�
      * @param classObj                    ������ �߻��� ������Ʈ
      * @param method                      ������ �߻��� �޼ҵ�
      * @param severity                    The message severity
      * @param message                     �����޽���
      * @param user                        �����ID
      */
	public static void writeLog(String sysName,Object classObj, String method, short severity, String message, String user) {
		writeLog( sysName, classObj, method,"", severity, message, user);
	}


	/** Log file�� Ư���� ������ �޼����� Write�ϴ� �޼ҵ�<p>
      *  - Class �̸��� �Ű� ������ ����<p>
      *  - Error �ڵ尡 ����<p>
      *  - ����� ���� ����<p>
      * @param sysName                     ������ �α��� �ý��۸�
      * @param classStr                    ������ �߻��� Class��
      * @param method                      ������ �߻��� �޼ҵ�
      * @param severity                    The message severity
      * @param message                     �����޽���
      */
	public static void writeLog(String sysName,String classStr, String method, short severity, String message) {
		writeLog(sysName,classStr, method, "", severity, message);
	}


	/** ����� ȭ�鿡 ����Ѵ�.
      * @param s                ȭ�鿡 ����� �޽���
      */
	public static void err(String s) {
		System.out.println(s);
	}


	/** message.properties�� Log_Debug�� true, false ������ ���� ���Ͽ� �����ϴ��� �ֿܼ� �Ѹ����� ����
      * Log_Verbose�� true�̸� console�� ���
      * @param message                     �����޽���
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


	/** "8859_1"Ÿ���� "UTF-8"Ÿ������ ��ȯ
	 * @param strSrc          ��ȯ�� ���ڿ�
	 * @return String
	*/
	public static String strToKorean(String strSrc) {
		String strDest = "";
		String strMethod = "strToKorean";
		String strClassName = "Log";

		try {
			//ASCII Ÿ���� ���ڸ� KSC5601�� ��ȯ
//			strDest = new String(strSrc.getBytes("8859_1"), "KSC5601");
			strDest = new String(strSrc.getBytes("8859_1"), "UTF-8");
		} catch(UnsupportedEncodingException ueEx) {
			ueEx.getMessage();
		}
		return strDest;
	}
}
