/** Main system          : foundation
 * Sub system           : lib
 * Classname            : ErrorConstants.java
 * Initial date         : 2005-11-14
 * Author               : Ȳ��õ
 */

package x.extr.lib;


/** ��ϵ� �޽��� �����͸� �����ϴ� Class�Դϴ�.
    @author ��Կ�
*/
public class ErrorConstants {
	//Exception ó���� ���� �κ�
	public static final Integer NORMAL = new Integer(0);
	public static final Integer GENERALEX = new Integer(-1);
	public static final Integer DBEX = new Integer(-2);
	public static final Integer EX = new Integer(-3);

	//DataBase Error�� ���� ����...
	/** DataBase�� ���� ���� �߻� */
	public static final String DB_CONNECT_FAILED = "ECOM01";
	/** Query ����� ���� �߻� */
	public static final String DB_QUERY_FAILED = "ECOM02";
	/** Statement�� ������ ���� �߻� */
	public static final String DB_CLOSE_RS_FAILED = "ECOM03";
	/** Transaction ���¸� �����ö� ���� �߻� */
	public static final String DB_GET_AUTOCOMMIT_FAILED = "ECOM04";
	/** Session ������ invalid */
	public static final String SYSERR_INVALID_SESSION = "ECOM05";
	/** Configuration ������ �о� ���̴¶��� ���� �߻� */
	public static final String LOAD_CONFIG_FILE_FAILED = "ECOM06";
	/** Configuration ������ �о� ���̴¶��� ���� �߻� */
	public static final String DB_INSERT_ERROR = "ECOM07";


	//System Errors
	/** ������ ��ġ�� �������� ������ ���� �߻� */
	public static final String FILE_UPLOAD_DIR_NOT_SPEC = "ECOM08";
	/** File ���̰� 0�϶� ���� �߻� */
	public static final String FILE_UPLOAD_INVALID_FILE = "ECOM09";
	/** �߸��� Column �̸� */
	public static final String DB_NO_SUCH_COLUMN = "ECOM10";
	/** InputStream���κ��� �о� ���϶��� ���� �߻� */
	public static final String FIELD_NOT_READ = "ECOM11";
	/** InputStream���κ��� �о� ���϶��� ���� �߻� */
	public static final String EX_ERROR = "ECOM12";

	//General Messages
	/** ���������� �۵��ҽ��� code */
	public static final String MSG_NO_ERROR="ECOM13";
	/** DB�� ������ �Ѿ� ���� ���� ���� �߻� */
	public static final String DB_OUT_OF_BOUNDS="ECOM14";
}
