/** Main system          : x
 * Sub system           : db
 * Classname            : DBException.java
 * Initial date         : 2005-11-14
 * Author               : 황재천
 * Update date/Modifier : 
 */

package x.extr.exception;

/** DB예외 발생 처리제어를 위한 공통 class
  * @author 황재천
  */
public class DBException extends Exception {
	private String code = null;
	private String userMessage = null;
	private int venderCode ;

	/** 예외상황 구분 : 사용자 입력  */
	public static final String UI = "Input";
	/** 예외상황 처리 메시지 */
	public static final String UI_MSG = "사용자 입력 Error.";

	/** 예외상황 구분 : 쿼리실행(select)  */
	public static final String SEL = "Select";
	/** 예외상황 처리 메시지 */
	public static final String SEL_MSG = "Select Error.";

	/** 예외상황 구분 : 쿼리실행(insert) */
	public static final String INS = "Insert";
	/** 예외상황 처리 메시지 */
	public static final String INS_MSG = "Insert Error.";
	/** 중복된 키를 입력할 떄 처리 메시지 */
	public static final String INS_DUP_MSG = "키값이 중복되었습니다.";
	/** 필수입력항목을 입력하지 않았을때 처리 메시지 */
	public static final String INS_NUL_MSG = "필수입력항목을 입력하지 않았습니다.";
	/** 입력데이터의 자리수가 클때  처리 메시지 */
	public static final String INS_OVR_MSG = "입력데이터의 자리수가 너무 큽니다.";
	/** 잘못된 데이타가 입력되었을 때 처리 메시지 */
	public static final String INS_WRG_MSG = "잘못된 데이타가 입력되었습니다.";
	/** 코드입력이 잘못되었을 때 처리 메시지 */
	public static final String INS_CDE_MSG = "코드 입력이 잘못되었습니다.";
	/** 데이타형식이 잘못되었을 때 처리 메시지 */
	public static final String INS_DTE_MSG = "데이터형식이 잘못되었습니다.";
	/** 예외상황 구분 : 쿼리실행(update) */
	public static final String UPD = "Update";
	/** 예외상황 처리 메시지 */
	public static final String UPD_MSG = "Update Error.";

	/** 예외상황 구분 : 쿼리실행(delete) */
	public static final String DEL = "Delete";
	/** 예외상황 처리 메시지 */
	public static final String DEL_MSG = "Delete Error.";
	/** 예외상황 처리 메시지 */
	public static final String DEL_MAS_MSG = "삭제하려는 데이터를 참조하는 하위데이터가 있습니다.";

	/** 예외상황 구분 : 쿼리실행(function) */
	public static final String FUNC = "Function";
	/** 예외상황 처리 메시지 */
	public static final String FUNC_MSG = "Function Error.";


	/** 예외상황 구분 : 쿼리실행(procedure) */
	public static final String PROC = "Procedure";
	/** 예외상황 처리 메시지 */
	public static final String PROC_MSG = "Procedure Error.";

	/** 예외상황 구분 : 데이터를 찾지 못함 */
	public static final String NDF = "NDF";
	/** 예외상황 처리 메시지 */
	public static final String NDF_MSG = "No Data Found.";

	/** 예외상황 구분 : 데이터 에러 */
	public static final String DS  = "DS";
	/** 예외상황 처리 메시지 */
	public static final String DS_MSG  = "Data Setting error.";

	/** 예외상황 구분 : 쿼리 구문에러 */
	public static final String SQL = "SQL";
	/** 예외상황 처리 메시지 */
	public static final String SQL_MSG  = "Invalid SQL Statement.";
	/** debug 메시지 정의 */
	public static final String debugMessage = "Debug Message ## ";

	/** DBException Constructor
	*/
	public DBException() {
		super();
	}
	/** DBException Constructor
	 * @param s          메시지
	*/
	public DBException(String s) {
		super(s);
	}
	/** DBException Constructor
	 * @param code       에러코드
	 * @param s          메시지
	*/
	public DBException(String code, String s) {
		this(s);
		this.code = code;
	}
	/** DBException Constructor
	 * @param code       에러코드
	 * @param s          메시지
	 * @param msg        유저메시지
	*/
	public DBException(String code, String s, String msg) {
		this(code,s);
		this.userMessage = msg;
	}
	/** DBException Constructor
	 * @param code       에러코드
	 * @param s          메시지
	 * @param venderCode 데이타베이스 벤더코드
	*/
	public DBException(String code, String s, int venderCode) {
		this(code,s);
		this.venderCode = venderCode;
	}


	/** 발생한 예외상황 구분을 얻는다
	 * @return String
	 */
	public String getCode() {
		return this.code;
	}


	/** 발생한 예외상황에 대한 사용자 정의 메시지를 얻는다
	 * @return String
	*/
	public String getUserMessage() {
		if( userMessage == null) {
			return "";
		}
		return this.userMessage;
	}


	/** 특정 데이타 베이스의 Vender exception code를 반환한다.
	 * @return int
	*/
	public int getVenderCode() {
		return this.venderCode;
	}


	/** 발생한 예외상황에 대한 코드를 스트링으로 출력
	 * @return String
	 */
	public String toString() {
		return new StringBuffer().append( "DBException : [ " )
								.append( code )
								.append(" ] ")
								.append( getMessage() )
								.toString();
	}
}