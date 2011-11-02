/** Main system          : foundation
  * Sub system           : util
  * Classname            : CommonInfoDM.java
  * Initial date         : 2005.11.20
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : TR관련 공통정보용 DM
  * Version information  : v 1.0
  *
  * Copyright notice     : Copyright (C)2003 by Daewoo Information Systems Co. Ltd.
  *                              All right reserved.
  */

package x.extr.util;

import java.io.Serializable;


/** TR관련 공통정보용 DM
  * @author 황재천
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


	/** 현재 설정된 상태를 스트링으로 반환
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

	/** 생성자
	*/
	public CommonInfoDM() {
	}
	/** 생성자
      @param user                     유저ID
	  @param dpt                      부서코드
	  @param brch                     지점코드
	  @param term                     단말번호
      @param rCnt                     조회시 요청건수
	  @param aCnt                     조회시 조회된 건수
	  @param rTime                    단말에서 처리요청시간
      @param aTime                    서버에서 처리된 시간
	  @param rNextKey                 조회시 사용할 연속조회키
	  @param aNextKey                 다음번 연속조회시 사용할 연속조회키
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
	/** 생성자
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

	/** 처리요청시 필요한 정보 설정
      @param user                     유저ID
	  @param dept                     부서코드
	  @param brch                     지점코드
	  @param term                     단말번호
      @param rCnt                     조회시 요청건수
	  @param rTime                    단말에서 처리요청시간
	  @param rNextKey                 조회시 사용할 연속조회키
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

	/** 회답시 필요한 정보 설정
	  @param aCnt                     조회시 조회된 건수
      @param aTime                    서버에서 처리된 시간
	  @param aNextKey                 다음번 연속조횟 사용할 연속조회키
	*/
	public void setAns(int aCnt,java.sql.Timestamp aTime,Object aNextKey) {
		if (aCnt >= 0) { ansCount = aCnt; }
		if (aTime != null) { ansTime = aTime; }
		if (aNextKey != null) { ansNextKey = aNextKey; }
	}

	/** 유저ID설정
      @param user                     유저ID
	*/
	public void setUserID(String user) {
		userID = user;
	}
	/** 유저ID 반환
	  @return String
	*/
	public String getUserID() {
		return userID;
	}
	/** 부서코드설정
	  @param dept                     부서코드
	*/
	public void setDptCd(String dept) {
		dptCd = dept;
	}
	/** 부서코드 반환
	  @return String
	*/
	public String getDptCd() {
		return dptCd;
	}
	/** 지점코드설정
	  @param brch                     지점코드
	*/
	public void setBrchCd(String brch) {
		brchCd = brch;
	}
	/** 지점코드 반환
	  @return String
	*/
	public String getBrchCd() {
		return brchCd;
	}
	/** 단말번호설정
	  @param term                     단말번호
	*/
	public void setTermID(String term) {
		termID = term;
	}
	/** 단말번호 반환
	  @return String
	*/
	public String getTermID() {
		return termID;
	}
	/** 조회시 요청건수 설정
      @param rCnt                     조회시 요청건수
	*/
	public void setReqCount(int rCnt) {
		reqCount = rCnt;
	}
	/** 조회시 요청건수 반환
	  @return int
	*/
	public int getReqCount() {
		return reqCount;
	}
	/** 조회시 조회된 건수 설정
	  @param aCnt                     조회시 조회된 건수
	*/
	public void setAnsCount(int aCnt) {
		ansCount = aCnt;
	}
	/** 조회시 조회된 건수 반환
	  @return int
	*/
	public int getAnsCount() {
		return ansCount;
	}
	/**  단말에서 처리요청시간 설정
	  @param rTime                    단말에서 처리요청시간
	*/
	public void setReqTime(java.sql.Timestamp rTime) {
		reqTime = rTime;
	}
	/**  단말에서 처리요청시간 반환
	  @return java.sql.Timestamp
	*/
	public java.sql.Timestamp getReqTime() {
		return reqTime;
	}
	/** 서버에서 처리된 시간 설정
      @param aTime                    서버에서 처리된 시간
	*/
	public void setAnsTime(java.sql.Timestamp aTime) {
		ansTime = aTime;
	}
	/** 서버에서 처리된 시간 반환
	  @return java.sql.Timestamp
	*/
	public java.sql.Timestamp getAnsTime() {
		return ansTime;
	}
	/** 조회시 사용할 연속조회키 설정
	  @param rNextKey                 조회시 사용할 연속조회키
	*/
	public void setReqNextKey(Object rNextKey) {
		reqNextKey = rNextKey;
	}
	/** 조회시 사용할 연속조회키 반환
	  @return Object
	*/
	public Object getReqNextKey() {
		return reqNextKey;
	}
	/** 다음번 연속조회시 사용할 연속조회키 설정
	  @param aNextKey                 다음번 연속조회시 사용할 연속조회키
	*/
	public void setAnsNextKey(Object aNextKey) {
		ansNextKey = aNextKey;
	}
	/** 다음번 연속조회시 사용할 연속조회키 반환
	  @return Object
	*/
	public Object getAnsNextKey() {
		return ansNextKey;
	}


}