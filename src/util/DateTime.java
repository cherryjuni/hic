package util;
import java.text.*;
import java.util.Date;
import java.util.Locale;

/**
	 * ���� : ��¥ ó�� �Լ� format�� ���� �ð� �����͸� String���� ��´�.
*/

public final class DateTime {
	private DateTime()
    {
    }

	/**
	 * ��¥ Format�� �´°� üũ
	 * ������ �ٸ��� Exception �߻�
	 * @param String to set
	 */
    public static void check(String s)
        throws Exception
    {
        check(s, "yyyy-MM-dd");
    }

	/**
	 * �ش� ���ڰ� �ش� ���˿� �´°� üũ
	 * @param String to set - ��¥ String
	 * @param String to set - Format
	 */
    public static void check(String s, String format)
        throws ParseException
    {
        if(s == null)
            throw new NullPointerException("date string to check is null");
        if(format == null)
            throw new NullPointerException("format string to check date is null");
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = null;
        try
        {
            date = formatter.parse(s);
        }
        catch(ParseException e)
        {
            throw new ParseException(e.getMessage() + " with format \"" + format + "\"", e.getErrorOffset());
        }
        if(!formatter.format(date).equals(s))
            throw new ParseException("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0);
        else
            return;
    }

	/**
	 * ���õ� ��¥�� yyyy-mm-dd ������ ���ڿ��� ���
	 * @return a String
	 */
    public static String getDateString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * ���õ� ��¥���� ���ڸ� int�� ���
	 * @return a int
	 */
    public static int getDay()
    {
        return getNumberByPattern("dd");
    }

	/**
	 * ��¥ Format ����
	 * @return a String
	 * @param String to set - Format
	 */
    public static String getFormatString(String pattern)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
        String dateString = formatter.format(new Date());
        return dateString;
    }

	/**
	 * ���õ� ��¥����  ���� int�� ���
	 * @return a int
	 */
    public static int getMonth()
    {
        return getNumberByPattern("MM");
    }

	/**
	 * ��¥�� �Էµ� pattern�� ����������  ���
	 * @return a int
	 * @param String to set - ����
	 */
    public static int getNumberByPattern(String pattern)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
        String dateString = formatter.format(new Date());
        return Integer.parseInt(dateString);
    }

	/**
	 * ��¥�� yyyymmdd Format �� ���ڿ��� ���
	 * @return a String
	 */
    public static String getShortDateString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * �ð��� HHmmss ������ ���ڿ��� ���
	 * @return a String
	 */
    public static String getShortTimeString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * ���ڸ� yyyy-MM-dd-HH:mm:ss:SSS ������ ���ڿ��� ���
	 * @return a String
	 */
    public static String getTimeStampString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * �ð��� HH:mm:ss ������ ���ڿ��� ���
	 * @return a String
	 */
    public static String getTimeString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * ���ڿ��� ������ ������ ���
	 * @return a int
	 */
    public static int getYear()
    {
        return getNumberByPattern("yyyy");
    }
}

