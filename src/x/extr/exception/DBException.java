/** Main system          : x
 * Sub system           : db
 * Classname            : DBException.java
 * Initial date         : 2005-11-14
 * Author               : Ȳ��õ
 * Update date/Modifier : 
 */

package x.extr.exception;

/** DB���� �߻� ó����� ���� ���� class
  * @author Ȳ��õ
  */
public class DBException extends Exception {
	private String code = null;
	private String userMessage = null;
	private int venderCode ;

	/** ���ܻ�Ȳ ���� : ����� �Է�  */
	public static final String UI = "Input";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String UI_MSG = "����� �Է� Error.";

	/** ���ܻ�Ȳ ���� : ��������(select)  */
	public static final String SEL = "Select";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String SEL_MSG = "Select Error.";

	/** ���ܻ�Ȳ ���� : ��������(insert) */
	public static final String INS = "Insert";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String INS_MSG = "Insert Error.";
	/** �ߺ��� Ű�� �Է��� �� ó�� �޽��� */
	public static final String INS_DUP_MSG = "Ű���� �ߺ��Ǿ����ϴ�.";
	/** �ʼ��Է��׸��� �Է����� �ʾ����� ó�� �޽��� */
	public static final String INS_NUL_MSG = "�ʼ��Է��׸��� �Է����� �ʾҽ��ϴ�.";
	/** �Էµ������� �ڸ����� Ŭ��  ó�� �޽��� */
	public static final String INS_OVR_MSG = "�Էµ������� �ڸ����� �ʹ� Ů�ϴ�.";
	/** �߸��� ����Ÿ�� �ԷµǾ��� �� ó�� �޽��� */
	public static final String INS_WRG_MSG = "�߸��� ����Ÿ�� �ԷµǾ����ϴ�.";
	/** �ڵ��Է��� �߸��Ǿ��� �� ó�� �޽��� */
	public static final String INS_CDE_MSG = "�ڵ� �Է��� �߸��Ǿ����ϴ�.";
	/** ����Ÿ������ �߸��Ǿ��� �� ó�� �޽��� */
	public static final String INS_DTE_MSG = "������������ �߸��Ǿ����ϴ�.";
	/** ���ܻ�Ȳ ���� : ��������(update) */
	public static final String UPD = "Update";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String UPD_MSG = "Update Error.";

	/** ���ܻ�Ȳ ���� : ��������(delete) */
	public static final String DEL = "Delete";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String DEL_MSG = "Delete Error.";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String DEL_MAS_MSG = "�����Ϸ��� �����͸� �����ϴ� ���������Ͱ� �ֽ��ϴ�.";

	/** ���ܻ�Ȳ ���� : ��������(function) */
	public static final String FUNC = "Function";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String FUNC_MSG = "Function Error.";


	/** ���ܻ�Ȳ ���� : ��������(procedure) */
	public static final String PROC = "Procedure";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String PROC_MSG = "Procedure Error.";

	/** ���ܻ�Ȳ ���� : �����͸� ã�� ���� */
	public static final String NDF = "NDF";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String NDF_MSG = "No Data Found.";

	/** ���ܻ�Ȳ ���� : ������ ���� */
	public static final String DS  = "DS";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String DS_MSG  = "Data Setting error.";

	/** ���ܻ�Ȳ ���� : ���� �������� */
	public static final String SQL = "SQL";
	/** ���ܻ�Ȳ ó�� �޽��� */
	public static final String SQL_MSG  = "Invalid SQL Statement.";
	/** debug �޽��� ���� */
	public static final String debugMessage = "Debug Message ## ";

	/** DBException Constructor
	*/
	public DBException() {
		super();
	}
	/** DBException Constructor
	 * @param s          �޽���
	*/
	public DBException(String s) {
		super(s);
	}
	/** DBException Constructor
	 * @param code       �����ڵ�
	 * @param s          �޽���
	*/
	public DBException(String code, String s) {
		this(s);
		this.code = code;
	}
	/** DBException Constructor
	 * @param code       �����ڵ�
	 * @param s          �޽���
	 * @param msg        �����޽���
	*/
	public DBException(String code, String s, String msg) {
		this(code,s);
		this.userMessage = msg;
	}
	/** DBException Constructor
	 * @param code       �����ڵ�
	 * @param s          �޽���
	 * @param venderCode ����Ÿ���̽� �����ڵ�
	*/
	public DBException(String code, String s, int venderCode) {
		this(code,s);
		this.venderCode = venderCode;
	}


	/** �߻��� ���ܻ�Ȳ ������ ��´�
	 * @return String
	 */
	public String getCode() {
		return this.code;
	}


	/** �߻��� ���ܻ�Ȳ�� ���� ����� ���� �޽����� ��´�
	 * @return String
	*/
	public String getUserMessage() {
		if( userMessage == null) {
			return "";
		}
		return this.userMessage;
	}


	/** Ư�� ����Ÿ ���̽��� Vender exception code�� ��ȯ�Ѵ�.
	 * @return int
	*/
	public int getVenderCode() {
		return this.venderCode;
	}


	/** �߻��� ���ܻ�Ȳ�� ���� �ڵ带 ��Ʈ������ ���
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