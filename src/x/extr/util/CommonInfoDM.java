/** Main system          : foundation
  * Sub system           : util
  * Classname            : CommonInfoDM.java
  * Initial date         : 2005.11.20
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : TR���� ���������� DM
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
  */

package x.extr.util;

import java.io.Serializable;


/** TR���� ���������� DM
  * @author Ȳ��õ
*/
public class CommonInfoDM implements java.io.Serializable {
	private String userID = null;
	private String dptCd = null;
	private String brchCd = null;
	private String termID = null;
	private int reqCount = 0;
	private int ansCount = 0;
	private java.sql.Timestamp reqTime = null;
	private java.sql.Timestamp ansTime = null;
	private Object reqNextKey = null;
	private Object ansNextKey = null;


	/** ���� ������ ���¸� ��Ʈ������ ��ȯ
	  @return String
	*/
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[USERID:" + userID + "]");
		sb.append("[DEPTCD:" + dptCd + "]");
		sb.append("[BRCHCD:" + brchCd + "]");
		sb.append("[TERMID:" + termID + "]");
		sb.append("[ReqCount:" + reqCount + "]");
		sb.append("[AnsCount:" + ansCount + "]");
		sb.append("[ReqTime:" + reqTime + "]");
		sb.append("[AnsTime:" + ansTime + "]");
		sb.append("[ReqNextKey:" + reqNextKey + "]");
		sb.append("[AnsNextKey:" + ansNextKey + "]");

		return new String(sb.toString());

	}

	/** ������
	*/
	public CommonInfoDM() {
	}
	/** ������
      @param user                     ����ID
	  @param dpt                      �μ��ڵ�
	  @param brch                     �����ڵ�
	  @param term                     �ܸ���ȣ
      @param rCnt                     ��ȸ�� ��û�Ǽ�
	  @param aCnt                     ��ȸ�� ��ȸ�� �Ǽ�
	  @param rTime                    �ܸ����� ó����û�ð�
      @param aTime                    �������� ó���� �ð�
	  @param rNextKey                 ��ȸ�� ����� ������ȸŰ
	  @param aNextKey                 ������ ������ȸ�� ����� ������ȸŰ
	*/
	public CommonInfoDM(String user,String dept,String brch,String term,int rCnt,int aCnt,
						java.sql.Timestamp rTime,java.sql.Timestamp aTime,
						Object rNextKey,Object aNextKey) {
		if (user != null) { userID = user; }
		if (dept != null) { dptCd = dept; }
		if (brch != null) { brchCd = brch; }
		if (term != null) { termID = term; }
		if (rCnt >= 0) { reqCount = rCnt; }
		if (aCnt >= 0) { ansCount = aCnt; }
		if (rTime != null) { reqTime = rTime; }
		if (aTime != null) { ansTime = aTime; }
		if (rNextKey != null) { reqNextKey = rNextKey; }
		if (aNextKey != null) { ansNextKey = aNextKey; }
	}
	/** ������
      @param ciDM                     CommonInfoDM
	*/
	public CommonInfoDM(CommonInfoDM ciDM) {
		userID = ciDM.userID;
		dptCd = ciDM.dptCd;
		brchCd = ciDM.brchCd;
		termID = ciDM.termID;
		reqCount = ciDM.reqCount;
		ansCount = ciDM.ansCount;
		reqTime = ciDM.reqTime;
		ansTime = ciDM.ansTime;
		reqNextKey = ciDM.reqNextKey;
		ansNextKey = ciDM.ansNextKey;
	}

	/** ó����û�� �ʿ��� ���� ����
      @param user                     ����ID
	  @param dept                     �μ��ڵ�
	  @param brch                     �����ڵ�
	  @param term                     �ܸ���ȣ
      @param rCnt                     ��ȸ�� ��û�Ǽ�
	  @param rTime                    �ܸ����� ó����û�ð�
	  @param rNextKey                 ��ȸ�� ����� ������ȸŰ
	*/
	public void setReq(String user,String dept,String brch,String term,int rCnt,java.sql.Timestamp rTime,Object rNextKey) {
		if (user != null) { userID = user; }
		if (dept != null) { dptCd = dept; }
		if (brch != null) { brchCd = brch; }
		if (term != null) { termID = term; }
		if (rCnt >= 0) { reqCount = rCnt; }
		if (rTime != null) { reqTime = rTime; }
		if (rNextKey != null) { reqNextKey = rNextKey; }
	}

	/** ȸ��� �ʿ��� ���� ����
	  @param aCnt                     ��ȸ�� ��ȸ�� �Ǽ�
      @param aTime                    �������� ó���� �ð�
	  @param aNextKey                 ������ ������Ƚ ����� ������ȸŰ
	*/
	public void setAns(int aCnt,java.sql.Timestamp aTime,Object aNextKey) {
		if (aCnt >= 0) { ansCount = aCnt; }
		if (aTime != null) { ansTime = aTime; }
		if (aNextKey != null) { ansNextKey = aNextKey; }
	}

	/** ����ID����
      @param user                     ����ID
	*/
	public void setUserID(String user) {
		userID = user;
	}
	/** ����ID ��ȯ
	  @return String
	*/
	public String getUserID() {
		return userID;
	}
	/** �μ��ڵ弳��
	  @param dept                     �μ��ڵ�
	*/
	public void setDptCd(String dept) {
		dptCd = dept;
	}
	/** �μ��ڵ� ��ȯ
	  @return String
	*/
	public String getDptCd() {
		return dptCd;
	}
	/** �����ڵ弳��
	  @param brch                     �����ڵ�
	*/
	public void setBrchCd(String brch) {
		brchCd = brch;
	}
	/** �����ڵ� ��ȯ
	  @return String
	*/
	public String getBrchCd() {
		return brchCd;
	}
	/** �ܸ���ȣ����
	  @param term                     �ܸ���ȣ
	*/
	public void setTermID(String term) {
		termID = term;
	}
	/** �ܸ���ȣ ��ȯ
	  @return String
	*/
	public String getTermID() {
		return termID;
	}
	/** ��ȸ�� ��û�Ǽ� ����
      @param rCnt                     ��ȸ�� ��û�Ǽ�
	*/
	public void setReqCount(int rCnt) {
		reqCount = rCnt;
	}
	/** ��ȸ�� ��û�Ǽ� ��ȯ
	  @return int
	*/
	public int getReqCount() {
		return reqCount;
	}
	/** ��ȸ�� ��ȸ�� �Ǽ� ����
	  @param aCnt                     ��ȸ�� ��ȸ�� �Ǽ�
	*/
	public void setAnsCount(int aCnt) {
		ansCount = aCnt;
	}
	/** ��ȸ�� ��ȸ�� �Ǽ� ��ȯ
	  @return int
	*/
	public int getAnsCount() {
		return ansCount;
	}
	/**  �ܸ����� ó����û�ð� ����
	  @param rTime                    �ܸ����� ó����û�ð�
	*/
	public void setReqTime(java.sql.Timestamp rTime) {
		reqTime = rTime;
	}
	/**  �ܸ����� ó����û�ð� ��ȯ
	  @return java.sql.Timestamp
	*/
	public java.sql.Timestamp getReqTime() {
		return reqTime;
	}
	/** �������� ó���� �ð� ����
      @param aTime                    �������� ó���� �ð�
	*/
	public void setAnsTime(java.sql.Timestamp aTime) {
		ansTime = aTime;
	}
	/** �������� ó���� �ð� ��ȯ
	  @return java.sql.Timestamp
	*/
	public java.sql.Timestamp getAnsTime() {
		return ansTime;
	}
	/** ��ȸ�� ����� ������ȸŰ ����
	  @param rNextKey                 ��ȸ�� ����� ������ȸŰ
	*/
	public void setReqNextKey(Object rNextKey) {
		reqNextKey = rNextKey;
	}
	/** ��ȸ�� ����� ������ȸŰ ��ȯ
	  @return Object
	*/
	public Object getReqNextKey() {
		return reqNextKey;
	}
	/** ������ ������ȸ�� ����� ������ȸŰ ����
	  @param aNextKey                 ������ ������ȸ�� ����� ������ȸŰ
	*/
	public void setAnsNextKey(Object aNextKey) {
		ansNextKey = aNextKey;
	}
	/** ������ ������ȸ�� ����� ������ȸŰ ��ȯ
	  @return Object
	*/
	public Object getAnsNextKey() {
		return ansNextKey;
	}


}