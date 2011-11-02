/** Main system          : x
 * Sub system           : db
 * Classname            : GeneralException.java
 * Initial date         : 2005-11-14
 * Author               : 황재천
 * Update date/Modifier : 
 */

package x.extr.exception;

import java.rmi.*;
import javax.ejb.*;
import java.io.*;
import x.extr.log.Log;
import x.extr.lib.*;

/** 등록된 메시지 데이터를 관리하는 Class입니다.
    @author 김규완
*/
public class GeneralException extends Exception {
	private String errCd;
	/* 디버깅모드 정의 */
	private boolean DEBUG_VERBOSE = false;


	/** Catches exceptions without a specified string
	*/
	public GeneralException() {
		super();
	}


	/** 에러코드를 이용해서 생성
	 *
	 * @param errCd             에러코드
	*/
	public GeneralException(String errCd) {
		this.errCd = errCd;
		/* 세부시스템이름, 클래스이름, 메소드이름, 로그종류, 뿌리고자 하는 메세지 */
		if (DEBUG_VERBOSE) {
			Log.writeLog("Com", "GeneralException", "Constructor", Log.LOG_DEBUG, errCd);
		}
	}


	/** 메세지를 반환한다.
	 * @return String
	*/
	public String getMessage() {
		MessageManager	msgMgr = null;
		String strErrMsg = null;
		if (DEBUG_VERBOSE) {
			Log.writeLog("Com", "GeneralException", "getMessage()", Log.LOG_DEBUG, "strNo is : " + errCd);
		}

		try {
			msgMgr = MessageManager.getInstance();
			strErrMsg = msgMgr.getMessage(errCd);
//			strErrMsg = "나중에 완성해야지.";

			if (DEBUG_VERBOSE) {
				Log.writeLog("Com", "GeneralException", "getMessage()", Log.LOG_DEBUG,"errCd is : " + errCd);
				Log.writeLog("Com", "GeneralException", "getMessage()", Log.LOG_DEBUG,"strErrMsg : " + strErrMsg);
			} 
		} catch(Exception e) {
			strErrMsg = "Can't get error message from MessageManager";
			Log.writeLog("Com", "GeneralException", "getMessage()", Log.LOG_ERROR,"strNo : " + errCd);
			Log.writeLog("Com", "GeneralException", "getMessage()", Log.LOG_ERROR,"Exception is : " + e.getMessage());
		}
		return (strErrMsg == null ? "Not Exist Error MsgCode" : strErrMsg) + "\n";
	}


	/** 메세지를 반환한다.
	 * @return String
	*/
	public String getMessageScript() {
		MessageManager	msgMgr = null;
		String strErrMsg = null;
		if (DEBUG_VERBOSE) {
			Log.writeLog("Com", "GeneralException", "getMessageScript()", Log.LOG_DEBUG, "strNo is : " + errCd);
		}
		try {
			msgMgr = MessageManager.getInstance();
			strErrMsg = msgMgr.getMessage(errCd );
//			strErrMsg = "나중에 완성해야지.";

			if (DEBUG_VERBOSE) {
				Log.writeLog("Com", "GeneralException", "getMessageScript()", Log.LOG_DEBUG,"In try strNo is : " + errCd);
				Log.writeLog("Com", "GeneralException", "getMessageScript()", Log.LOG_DEBUG,"strErrMsg : " + strErrMsg);
			}
		} catch(Exception e) {
			strErrMsg = "Can't get error message from MessageManager";
			Log.writeLog("Com", "GeneralException", "getMessageScript()", Log.LOG_ERROR,"strNo : " + errCd);
			Log.writeLog("Com", "GeneralException", "getMessageScript()", Log.LOG_ERROR,"Exception is : " + e.getMessage());
		}
		return (strErrMsg == null ? "Not Exist Error MsgCode" : strErrMsg) + "\n";
	}


	/** 메시지 정보를 스트링으로 반환한다.
	 * @return String
	*/
	public String toString() {
		return getMessage();
	}


	/** 메시지코드를  반환한다.
	 * @return String
	*/
	public String getCode() {
		String err_code = super.getMessage();
		System.out.println("Exception Err Code : " + err_code );
		return  err_code;
	}


/*
  public static void main(String[] args)
  {
    GeneralException e = new GeneralException("ComDQE00001");
    System.out.println("Message 내용은 " + e.getMessage());
  }
*/
}