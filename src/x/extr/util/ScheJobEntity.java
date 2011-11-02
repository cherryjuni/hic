package x.extr.util;

/**
 * <p>파일명       : ScheJobEntity.java</p>
 * <p>버젼         : 1.1.1.0</p>
 * <p>작성일       : 2006.02.20</p>
 * <p>작성자       : </p>
 * <p>Use Case명  : </p>
 * <p>프로그램명   : </p>
 * <p>내용        : ScheJobEntity</p>
 */	
public class ScheJobEntity extends DebugUtil {
	private String sendTp; 		//	전송_타입
	private String extrOrgCd; 	//	대외기관_코드
	private String bsnDivCd; 	//	업무_구분코드
	private String schdJobDiv; 	//	스케쥴_작업_구분(파일,전송)	
	private String schdExecTm; 	//	스케쥴_실행_시각
	private String schdTp; 		//	스케쥴_타입
	private String schdJobNm; 	//	스케쥴_작업_명
	private String execClass; 	//	실행_클래스
	private String execMethod; 	//	실행_메소드
	private String execParm; 	//	실행_파라미터
	private String afrowJobAutoProcFg; //후행_작업_자동_처리_여부

	
	public String getSendTp() { return sendTp; }
	public String getExtrOrgCd() { return extrOrgCd; }
	public String getBsnDivCd() { return bsnDivCd; }
	public String getSchdJobDiv() { return schdJobDiv; }
	public String getSchdExecTm() { return schdExecTm; }
	public String getSchdTp() { return schdTp; }
	public String getSchdJobNm() { return schdJobNm; }
	public String getExecClass() { return execClass; }
	public String getExecMethod() { return execMethod; }
	public String getExecParm() { return execParm; }
	public String getAfrowJobAutoProcFg() { return afrowJobAutoProcFg; }

	
	public void setSendTp(String sendTp) {this.sendTp = sendTp;}
	public void setExtrOrgCd(String extrOrgCd) {this.extrOrgCd = extrOrgCd;}
	public void setBsnDivCd(String bsnDivCd) {this.bsnDivCd = bsnDivCd;}
	public void setSchdJobDiv(String schdJobDiv) {this.schdJobDiv = schdJobDiv;}
	public void setSchdExecTm(String schdExecTm) {this.schdExecTm = schdExecTm;}
	public void setSchdTp(String schdTp) {this.schdTp = schdTp;}
	public void setSchdJobNm(String schdJobNm) {this.schdJobNm = schdJobNm;}
	public void setExecClass(String execClass) {this.execClass = execClass;}
	public void setExecMethod(String execMethod) {this.execMethod = execMethod;}
	public void setExecParm(String execParm) {this.execParm = execParm;}
	public void setAfrowJobAutoProcFg(String afrowJobAutoProcFg) {this.afrowJobAutoProcFg = afrowJobAutoProcFg;}
}
