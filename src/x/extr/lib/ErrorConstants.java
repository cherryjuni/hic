/** Main system          : foundation
 * Sub system           : lib
 * Classname            : ErrorConstants.java
 * Initial date         : 2005-11-14
 * Author               : 황재천
 */

package x.extr.lib;


/** 등록된 메시지 데이터를 관리하는 Class입니다.
    @author 김규완
*/
public class ErrorConstants {
	//Exception 처리에 관한 부분
	public static final Integer NORMAL = new Integer(0);
	public static final Integer GENERALEX = new Integer(-1);
	public static final Integer DBEX = new Integer(-2);
	public static final Integer EX = new Integer(-3);

	//DataBase Error에 대한 문제...
	/** DataBase에 접속 오류 발생 */
	public static final String DB_CONNECT_FAILED = "ECOM01";
	/** Query 실행시 오류 발생 */
	public static final String DB_QUERY_FAILED = "ECOM02";
	/** Statement를 닫을때 오류 발생 */
	public static final String DB_CLOSE_RS_FAILED = "ECOM03";
	/** Transaction 상태를 가져올때 오류 발생 */
	public static final String DB_GET_AUTOCOMMIT_FAILED = "ECOM04";
	/** Session 정보의 invalid */
	public static final String SYSERR_INVALID_SESSION = "ECOM05";
	/** Configuration 파일을 읽어 들이는때의 오류 발생 */
	public static final String LOAD_CONFIG_FILE_FAILED = "ECOM06";
	/** Configuration 파일을 읽어 들이는때의 오류 발생 */
	public static final String DB_INSERT_ERROR = "ECOM07";


	//System Errors
	/** 저장한 위치가 지정되지 않을떄 오류 발생 */
	public static final String FILE_UPLOAD_DIR_NOT_SPEC = "ECOM08";
	/** File 길이가 0일때 오류 발생 */
	public static final String FILE_UPLOAD_INVALID_FILE = "ECOM09";
	/** 잘못된 Column 이름 */
	public static final String DB_NO_SUCH_COLUMN = "ECOM10";
	/** InputStream으로부터 읽어 들일때의 오류 발생 */
	public static final String FIELD_NOT_READ = "ECOM11";
	/** InputStream으로부터 읽어 들일때의 오류 발생 */
	public static final String EX_ERROR = "ECOM12";

	//General Messages
	/** 정상적으로 작동할시의 code */
	public static final String MSG_NO_ERROR="ECOM13";
	/** DB의 범위를 넘어 섰을 때의 오류 발생 */
	public static final String DB_OUT_OF_BOUNDS="ECOM14";
}
