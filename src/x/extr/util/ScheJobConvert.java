package x.extr.util;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * <p>파일명 : ScheJobConvert.java </p>
 * <p>버전 : 1.1.1.0</p>
 * <p>작성일 : 2006. 2. 20 </p>
 * <p>작성자 : 황재천</p>
 * <p>프로그램명 : 스케쥴잡 엔티티 컨버터 </p>
 * <p>내용 : 스케쥴잡 객체 속성변환 </p>
 */
public class ScheJobConvert {
	private static int rowCount1 = 0;
	private static int rowCount2 = 0;
	
    public ScheJobConvert() {
    }

    /**
     * <p>메소드명 : ScheJobEntity</p>
     * <p>설명 : DB에서 조회한 스케쥴작업 대상을 객체로 생성</p>
     * <p>메소드인수1 : ResultSet rs</p>
     * <p>메소드리턴값 : ScheJobEntity 오브젝트</p>
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
     * <p>메소드명 : PrevScheJobEntity</p>
     * <p>설명 : DB에서 조회한 스케쥴작업 대상을 객체로 생성</p>
     * <p>메소드인수1 : ResultSet rs</p>
     * <p>메소드리턴값 : PrevScheJobEntity 오브젝트</p>
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
