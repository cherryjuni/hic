package x.extr.util;

/**
 * <p>���ϸ�      : PrevSchdJobEntity.java</p>
 * <p>����        : 1.1.1.0</p>
 * <p>�ۼ���      : 2006.02.20</p>
 * <p>�ۼ���      : </p>
 * <p>Use Case��  : </p>
 * <p>���α׷���  : </p>
 * <p>����        : </p>
 */	
public class PrevScheJobEntity extends DebugUtil {
	private String precSendTp; 		//  ����_����_Ÿ��
	private String precExtrOrgCd; 	//  ����_��ܱ��_�ڵ�
	private String precBsnDivCd; 	//  ����_����_����_�ڵ�
	private String precSchdJobDiv; 	//  ����_������_�۾�_����
	private String prevCurrStat;	//  ����_������_�۾�_����

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
