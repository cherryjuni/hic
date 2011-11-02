/** Main system          : x
 * Sub system           : db
 * Classname            : GeneralException.java
 * Initial date         : 2005-11-14
 * Author               : Ȳ��õ
 * Update date/Modifier : 
 */

package x.extr.exception;

import java.rmi.*;
import javax.ejb.*;
import java.io.*;
import x.extr.log.Log;
import x.extr.lib.*;

/** ��ϵ� �޽��� �����͸� �����ϴ� Class�Դϴ�.
    @author ��Կ�
*/
public class GeneralException extends Exception {
	private String errCd;
	/* ������� ���� */
	private boolean DEBUG_VERBOSE = false;


	/** Catches exceptions without a specified string
	*/
	public GeneralException() {
		super();
	}


	/** �����ڵ带 �̿��ؼ� ����
	 *
	 * @param errCd             �����ڵ�
	*/
	public GeneralException(String errCd) {
		this.errCd = errCd;
		/* ���νý����̸�, Ŭ�����̸�, �޼ҵ��̸�, �α�����, �Ѹ����� �ϴ� �޼��� */
		if (DEBUG_VERBOSE) {
			Log.writeLog("Com", "GeneralException", "Constructor", Log.LOG_DEBUG, errCd);
		}
	}


	/** �޼����� ��ȯ�Ѵ�.
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
//			strErrMsg = "���߿� �ϼ��ؾ���.";

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


	/** �޼����� ��ȯ�Ѵ�.
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
//			strErrMsg = "���߿� �ϼ��ؾ���.";

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


	/** �޽��� ������ ��Ʈ������ ��ȯ�Ѵ�.
	 * @return String
	*/
	public String toString() {
		return getMessage();
	}


	/** �޽����ڵ带  ��ȯ�Ѵ�.
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
    System.out.println("Message ������ " + e.getMessage());
  }
*/
}