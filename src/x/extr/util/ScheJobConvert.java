package x.extr.util;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * <p>���ϸ� : ScheJobConvert.java </p>
 * <p>���� : 1.1.1.0</p>
 * <p>�ۼ��� : 2006. 2. 20 </p>
 * <p>�ۼ��� : Ȳ��õ</p>
 * <p>���α׷��� : �������� ��ƼƼ ������ </p>
 * <p>���� : �������� ��ü �Ӽ���ȯ </p>
 */
public class ScheJobConvert {
	private static int rowCount1 = 0;
	private static int rowCount2 = 0;
	
    public ScheJobConvert() {
    }

    /**
     * <p>�޼ҵ�� : ScheJobEntity</p>
     * <p>���� : DB���� ��ȸ�� �������۾� ����� ��ü�� ����</p>
     * <p>�޼ҵ��μ�1 : ResultSet rs</p>
     * <p>�޼ҵ帮�ϰ� : ScheJobEntity ������Ʈ</p>
     */
    public static List getScheJobList(ResultSet rs) throws SQLException {
    	List jobList = new ArrayList();
    	
        while(rs.next()){
	    	int i = 1;
	        ScheJobEntity jobEntity = new ScheJobEntity();
	        
	        jobEntity.setSendTp(rs.getString(i++));
	        jobEntity.setExtrOrgCd(rs.getString(i++));
	        jobEntity.setBsnDivCd(rs.getString(i++));
	        jobEntity.setSchdJobDiv(rs.getString(i++));
	        jobEntity.setSchdExecTm(rs.getString(i++));
	        jobEntity.setSchdTp(rs.getString(i++));
	        jobEntity.setSchdJobNm(rs.getString(i++));
	        jobEntity.setExecClass(rs.getString(i++));
	        jobEntity.setExecMethod(rs.getString(i++));
	        jobEntity.setExecParm(rs.getString(i++));
	        jobEntity.setAfrowJobAutoProcFg(rs.getString(i++));
	        
	        jobList.add(jobEntity);
        }
        rowCount1 = jobList.size();
        
        return jobList;
    }

    /**
     * <p>�޼ҵ�� : PrevScheJobEntity</p>
     * <p>���� : DB���� ��ȸ�� �������۾� ����� ��ü�� ����</p>
     * <p>�޼ҵ��μ�1 : ResultSet rs</p>
     * <p>�޼ҵ帮�ϰ� : PrevScheJobEntity ������Ʈ</p>
     */
    public static List getPrevScheJobList(ResultSet rs) throws SQLException {
    	List prevJobList = new ArrayList();
    	
        while(rs.next()){
	    	int i = 1;
	        PrevScheJobEntity prevJobEntity = new PrevScheJobEntity();
	        
	        prevJobEntity.setPrecSendTp(rs.getString(i++));
	        prevJobEntity.setPrecExtrOrgCd(rs.getString(i++));
	        prevJobEntity.setPrecBsnDivCd(rs.getString(i++));
	        prevJobEntity.setPrecSchdJobDiv(rs.getString(i++));
	        prevJobEntity.setPrevCurrStat(rs.getString(i++));
	        
	        prevJobList.add(prevJobEntity);
        }
        rowCount2 = prevJobList.size();
        
        return prevJobList;
    }
    
    public static int getJobRowCount() {
    	return rowCount1;
    }
	
    public static int getPrevJobRowCount() {
    	return rowCount2;
    }    
}
