package x.extr.util;

/**
 * <p>���ϸ�       : ScheJobEntity.java</p>
 * <p>����         : 1.1.1.0</p>
 * <p>�ۼ���       : 2006.02.20</p>
 * <p>�ۼ���       : </p>
 * <p>Use Case��  : </p>
 * <p>���α׷���   : </p>
 * <p>����        : ScheJobEntity</p>
 */	
public class ScheJobEntity extends DebugUtil {
	private String sendTp; 		//	����_Ÿ��
	private String extrOrgCd; 	//	��ܱ��_�ڵ�
	private String bsnDivCd; 	//	����_�����ڵ�
	private String schdJobDiv; 	//	������_�۾�_����(����,����)	
	private String schdExecTm; 	//	������_����_�ð�
	private String schdTp; 		//	������_Ÿ��
	private String schdJobNm; 	//	������_�۾�_��
	private String execClass; 	//	����_Ŭ����
	private String execMethod; 	//	����_�޼ҵ�
	private String execParm; 	//	����_�Ķ����
	private String afrowJobAutoProcFg; //����_�۾�_�ڵ�_ó��_����

	
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
