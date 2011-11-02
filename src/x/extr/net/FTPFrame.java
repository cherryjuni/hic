/** Main system          : x
  * Sub system           : net
  * Classname            : FTPFrame.java
  * Initial date         : 2005.11.16
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : DPS와 FTP처리를 위한 기본Frame
  */

package x.extr.net;

import x.extr.util.*;
import x.extr.lib.*;

/** DPS와 FTP처리를 위한 기본Frame
  * @author 황재천
  */
public class FTPFrame  {
	public String fromPath = "";
	public String toPath = "";

	public String scheJobId = "";
	public String sendDt = "";
	public String extrOrgType = "";
	public String extrOrgCd = "";
	public String bsnDivCd = "";
	public String stndDt = "";
	public String snrFlag = "";
	public String filePath = "";
	public String fileName = "";
	public String totRecCnt = "";
	public String procTrmnNo = "";
	public String trmnRegMan = "";
	public String trmnRegManPw = "";
	public String reserved = "";

	/** 생성자
      * @param fromTo             kiis.properties에 정의되었는 FTP화일명 정보KEY
      */
	public FTPFrame(String fromTo) {
		String sTemp;
		int cutPos;
		try {
			PropertyManager pm = PropertyManager.getInstance();
			sTemp = (String)pm.getProperty("FTP_" + fromTo);
			cutPos = sTemp.indexOf(",");
			if (cutPos > 0 ) {
				fromPath = sTemp.substring(0,cutPos);
				toPath = sTemp.substring(cutPos + 1);
			}
		} catch ( Exception e ) {
			this.fromPath = "";
			this.toPath = "";
		}
	}
	/** 생성자
      */
	public FTPFrame() {};

	/** DPS규칙으로 송수신측 파일명 설정
      * @param exCode             외부기관코드
      * @param trCode             TR-Code
      * @param date               작업일자
      */
	public void setDPSStyle(String exCode,String trCode,String date) {

		try {
			PropertyManager pm = PropertyManager.getInstance();
		
			this.fromPath = (String)pm.getProperty("FTP_DPSSTYLE_BASE_FROM");
			this.toPath = (String)pm.getProperty("FTP_DPSSTYLE_BASE_TO");

			if ((exCode != null) && (!exCode.equals(""))) {
				this.fromPath += exCode + "/";
				this.toPath += exCode + "/";
			}
			this.fromPath += trCode;
			this.toPath += trCode;
			if ((date != null) && (!date.equals(""))) {
				this.fromPath += "." + date;
				this.toPath += "." + date;
			}
		} catch ( Exception e ) {
			this.fromPath = "";
			this.toPath = "";
		}

	}

	/** DPS규칙으로 송수신측 파일명 설정
     * @param exCode             외부기관코드
     * @param trCode             TR-Code
     * @param date               작업일자
     */
	public void setDPSStyle(String scheJobId,String sendDt,String extrOrgType,String extrOrgCd,String bsnDivCd,String stndDt,String snrFlag,String filePath,String fileName,String totRecCnt,String procTrmnNo,String trmnRegMan,String trmnRegManPw,String reserved) {
		this.scheJobId 		= scheJobId; 
		this.sendDt 		= sendDt;
		this.extrOrgType	= extrOrgType;
		this.extrOrgCd		= extrOrgCd;
		this.bsnDivCd		= bsnDivCd;
		this.stndDt			= stndDt;
		this.snrFlag		= snrFlag;
		this.filePath		= filePath;
		this.fileName		= fileName;
		this.totRecCnt		= totRecCnt;
		this.procTrmnNo		= procTrmnNo;
		this.trmnRegMan		= trmnRegMan;
		this.trmnRegManPw	= trmnRegManPw;
		this.reserved		= reserved;
	}	
	
	public String toString() {
		StringBuffer buf = new StringBuffer("");
		
		/*
		if (shortPath != null) {
			try {
				PropertyManager pm = PropertyManager.getInstance();
				buf.append((String)pm.getProperty("FILES_" + shortPath));
				buf.append(exPath);
			} catch ( Exception e ) {
				return "";
			}
		} else {
			buf.append(fullPath);
			buf.append(exPath);
		}
		*/
		
		buf.append(scheJobId); 
		buf.append(sendDt);
		buf.append(extrOrgType);
		buf.append(extrOrgCd);
		buf.append(bsnDivCd);
		buf.append(stndDt);
		buf.append(snrFlag);
		buf.append(filePath);
		buf.append(fileName);
		buf.append(totRecCnt);
		buf.append(procTrmnNo);
		buf.append(trmnRegMan);
		buf.append(trmnRegManPw);
		buf.append(reserved);		

		return buf.toString();
	}

}