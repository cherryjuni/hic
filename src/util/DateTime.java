package util;
import java.text.*;
import java.util.Date;
import java.util.Locale;

/**
	 * 내용 : 날짜 처리 함수 format및 날자 시간 데이터를 String으로 얻는다.
*/

public final class DateTime {
	private DateTime()
    {
    }

	/**
	 * 날짜 Format이 맞는가 체크
	 * 포맷이 다르면 Exception 발생
	 * @param String to set
	 */
    public static void check(String s)
        throws Exception
    {
        check(s, "yyyy-MM-dd");
    }

	/**
	 * 해당 문자가 해당 포맷에 맞는가 체크
	 * @param String to set - 날짜 String
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
	 * 세팅된 날짜르 yyyy-mm-dd 형태의 문자열로 출력
	 * @return a String
	 */
    public static String getDateString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * 세팅된 날짜에서 일자를 int로 출력
	 * @return a int
	 */
    public static int getDay()
    {
        return getNumberByPattern("dd");
    }

	/**
	 * 날짜 Format 지정
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
	 * 세팅된 날짜에서  월을 int로 출력
	 * @return a int
	 */
    public static int getMonth()
    {
        return getNumberByPattern("MM");
    }

	/**
	 * 날짜를 입력된 pattern의 정수형으로  출력
	 * @return a int
	 * @param String to set - 패턴
	 */
    public static int getNumberByPattern(String pattern)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
        String dateString = formatter.format(new Date());
        return Integer.parseInt(dateString);
    }

	/**
	 * 날짜를 yyyymmdd Format 의 문자열로 출력
	 * @return a String
	 */
    public static String getShortDateString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * 시간을 HHmmss 형태의 문자열로 출력
	 * @return a String
	 */
    public static String getShortTimeString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * 날자를 yyyy-MM-dd-HH:mm:ss:SSS 형태의 문자열로 출력
	 * @return a String
	 */
    public static String getTimeStampString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * 시간을 HH:mm:ss 형태의 문자열로 출력
	 * @return a String
	 */
    public static String getTimeString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        return formatter.format(new Date());
    }

	/**
	 * 날자에서 연도를 정수로 출력
	 * @return a int
	 */
    public static int getYear()
    {
        return getNumberByPattern("yyyy");
    }
}

