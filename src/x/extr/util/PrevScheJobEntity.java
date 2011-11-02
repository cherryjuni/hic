package x.extr.util;

/**
 * <p>파일명      : PrevSchdJobEntity.java</p>
 * <p>버젼        : 1.1.1.0</p>
 * <p>작성일      : 2006.02.20</p>
 * <p>작성자      : </p>
 * <p>Use Case명  : </p>
 * <p>프로그램명  : </p>
 * <p>내용        : </p>
 */	
public class PrevScheJobEntity extends DebugUtil {
	private String precSendTp; 		//  선행_전송_타입
	private String precExtrOrgCd; 	//  선행_대외기관_코드
	private String precBsnDivCd; 	//  선행_업무_구분_코드
	private String precSchdJobDiv; 	//  선행_스케쥴_작업_구분
	private String prevCurrStat;	//  선행_스케쥴_작업_상태

	public String getPrecSendTp() { return precSendTp; }
	public String getPrecExtrOrgCd() { return precExtrOrgCd; }
	public String getPrecBsnDivCd() { return precBsnDivCd; }
	public String getPrecSchdJobDiv() { return precSchdJobDiv; }
	public String getPrevCurrStat() { return prevCurrStat; }

	public void setPrecSendTp(String precSendTp) {this.precSendTp = precSendTp;}
	public void setPrecExtrOrgCd(String precExtrOrgCd) {this.precExtrOrgCd = precExtrOrgCd;}
	public void setPrecBsnDivCd(String precBsnDivCd) {this.precBsnDivCd = precBsnDivCd;}
	public void setPrecSchdJobDiv(String precSchdJobDiv) {this.precSchdJobDiv = precSchdJobDiv;}
	public void setPrevCurrStat(String prevCurrStat) {this.prevCurrStat = prevCurrStat;}
}
