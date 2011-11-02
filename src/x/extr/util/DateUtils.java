/** Main system          : foundation
  * Sub system           : util
  * Classname            : DateUtil.java
  * Initial date         : 2005.11.27
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : ��¥���� ��ƿ��Ƽ Class
  * Version information  : v 1.0
  */

package x.extr.util;

import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.*;

/** ��¥���� ��ƿ��Ƽ Class
  * @author Ȳ��õ
*/
public class DateUtils {
	String sSystem = "Com";
	String sClassName = "DateUtil";
	private static Calendar c = Calendar.getInstance();

	//���¼� ������
	static String[] sNewYear = {"19910215","00000000","00000000",
						 "19920204","00000000","00000000",
						 "19930123","00000000","00000000",
						 "19940210","00000000","00000000",
						 "19950131","00000000","00000000",
						 "19960219","00000000","00000000",
						 "19970208","00000000","00000000",
						 "19980127","19980128","19980129",
						 "19990215","19990216","19990217",
						 "20000204","20000205","20000206",
						 "20010123","20010124","20010125",
						 "20020211","20020212","20020213",
						 "20030131","20030201","20030202",
						 "20040121","20040122","20040123",
						 "20050208","20050209","20050210",
						 "20060129","20060130","20060131",
						 "20070217","20070218","20070219",
						 "20080206","20080207","20080208",
						 "20090125","20090126","20090127",
						 "20100213","20100214","20100215",
						 "20110202","20110203","20110204",
						 "20120122","20120123","20120124",
						 "20130209","20130210","20130211",
						 "20140130","20140131","20140201",
						 "20150218","20150219","20150220",
						 "20160208","20160209","20160210",
						 "20170127","20170128","20170129",
						 "20180215","20180216","20180217",
						 "20190204","20190205","20190206",
						 "20200124","20200125","20200126"};

	//����ź���� ������
	static String[] sBudda = {"19910521",
						 "19920510",
						 "19930528",
						 "19940518",
						 "19950507",
						 "19960524",
						 "19970514",
						 "19980503",
						 "19990522",
						 "20000511",
						 "20010501",
						 "20020519",
						 "20030508",
						 "20040526",
						 "20050515",
						 "20060505",
						 "20070524",
						 "20080512",
						 "20090502",
						 "20105021",
						 "20110510",
						 "20120528",
						 "20130517",
						 "20140506",
						 "20150525",
						 "20160514",
						 "20170503",
						 "20180522",
						 "20190512",
						 "20200430"};

	//�߼� ������
	static String[] sThanksGiving = {"19910921","19910922","19910923",
						 "19920910","19920911","19920912",
						 "19930929","19930930","19931001",
						 "19940919","19940920","19940921",
						 "19950908","19950909","19950910",
						 "19960926","19960927","19960928",
						 "19970915","19970916","19970917",
						 "19981004","19981005","19981006",
						 "19990923","19990924","19990925",
						 "20000911","20000912","20000913",
						 "20010930","20011001","20011002",
						 "20020920","20020921","20020922",
						 "20030910","20030911","20030912",
						 "20040927","20040928","20040929",
						 "20050917","20050918","20050919",
						 "20061005","20061006","20061007",
						 "20070924","20070925","20070926",
						 "20080913","20080914","20080915",
						 "20091002","20091003","20091004",
						 "20100921","20100922","20100923",
						 "20110911","20110912","20110913",
						 "20120929","20120930","20121001",
						 "20130918","20130919","20130920",
						 "20140907","20140908","20140909",
						 "20150926","20150927","20150928",
						 "20160914","20160915","20160916",
						 "20171003","20171004","20171005",
						 "20180923","20180924","20180925",
						 "20190912","20190913","20190914",
						 "20200930","20201001","20201002"};

	/** �Էµ� ������ �ش��ϴ� ������ ��̸� ��ȯ�Ѵ�.
	  @param year                 �⵵
	  @return String[]
	*/
	public static String[] getBizData(int year) {
		int dayMax = 365;
		int month;
		int date;
		int day;
		int newYearMonth1,newYearDate1;				// ���¼� ����
		int newYearMonth2,newYearDate2;				// ���¼�
		int newYearMonth3,newYearDate3;				// ���¼� ������
		int buddaMonth,buddaDate;					// ����ź����
		int thanksGivingMonth1,thanksGivingDate1;	// �߼� ����
		int thanksGivingMonth2,thanksGivingDate2;	// �߼�
		int thanksGivingMonth3,thanksGivingDate3;	// �߼� ������

		String bizCode;
		String[] bizData;
		String sDate;

		Calendar c = Calendar.getInstance();
		c.set(year,1,29);
		if (c.get(Calendar.MONTH) == 1)	{
			dayMax = 366;
		}
		bizData = new String[dayMax];

		// ���¼� �Ҵ�
		newYearMonth1 = -1;
		newYearDate1 = -1;
		newYearMonth2 = -1;
		newYearDate2 = -1;
		newYearMonth3 = -1;
		newYearDate3 = -1;
		for (int i=0;i<sNewYear.length ;i++ )	{
			if (Integer.parseInt(sNewYear[i].substring(0,4)) == year) {
				newYearMonth1 = Integer.parseInt(sNewYear[i].substring(4,6));
				newYearDate1 = Integer.parseInt(sNewYear[i].substring(6,8));
				newYearMonth2 = Integer.parseInt(sNewYear[i+1].substring(4,6));
				newYearDate2 = Integer.parseInt(sNewYear[i+1].substring(6,8));
				newYearMonth3 = Integer.parseInt(sNewYear[i+2].substring(4,6));
				newYearDate3 = Integer.parseInt(sNewYear[i+2].substring(6,8));

				break;
			}
		}
		// ����ź���� �Ҵ�
		buddaMonth = -1;
		buddaDate = -1;
		for (int i=0;i<sBudda.length ;i++ )	{
			if (Integer.parseInt(sBudda[i].substring(0,4)) == year) {
				buddaMonth = Integer.parseInt(sBudda[i].substring(4,6));
				buddaDate = Integer.parseInt(sBudda[i].substring(6,8));
				break;
			}
		}
		// �߼� �Ҵ�
		thanksGivingMonth1 = -1;
		thanksGivingDate1 = -1;
		thanksGivingMonth2 = -1;
		thanksGivingDate2 = -1;
		thanksGivingMonth3 = -1;
		thanksGivingDate3 = -1;
		for (int i=0;i<sThanksGiving.length ;i++ ) {
			if (Integer.parseInt(sThanksGiving[i].substring(0,4)) == year) {
				thanksGivingMonth1 = Integer.parseInt(sThanksGiving[i].substring(4,6));
				thanksGivingDate1 = Integer.parseInt(sThanksGiving[i].substring(6,8));
				thanksGivingMonth2 = Integer.parseInt(sThanksGiving[i+1].substring(4,6));
				thanksGivingDate2 = Integer.parseInt(sThanksGiving[i+1].substring(6,8));
				thanksGivingMonth3 = Integer.parseInt(sThanksGiving[i+2].substring(4,6));
				thanksGivingDate3 = Integer.parseInt(sThanksGiving[i+2].substring(6,8));

				break;
			}
		}


		for (int i=1;i<=dayMax;i++) {
			c.set(Calendar.DAY_OF_YEAR,i);
			month = c.get(Calendar.MONTH) + 1;
			date = c.get(Calendar.DATE);
			day = c.get(Calendar.DAY_OF_WEEK);
			sDate = FormatData.numToStr(year,4,"0") + FormatData.numToStr(month,2,"0") + FormatData.numToStr(date,2,"0");

			// 1990�� ������ ����� �������� �����մϴ�.
			// 10�� 9�� �ѱ۳� :    1970 - 1990
			// ���¸���
			// 1�� 1��  ���� :      1998���� 3�Ͼ� 
			// 4�� 8��  ����ź���� :     - 1975
			if ((month == 1) && (date == 1)) {
				bizCode = "10";			//����			���� - 
			} else if ((month == newYearMonth1) && (date == newYearDate1)) {
				bizCode = "11";			//��������
			} else if ((month == newYearMonth2) && (date == newYearDate2)) {
				bizCode = "11";			//����		
			} else if ((month == newYearMonth3) && (date == newYearDate3)) {
				bizCode = "11";			//����������
			} else if ((month == 3) && (date == 1)) {
				bizCode = "22";			//3.1��			1950 -
			} else if ((month == 4) && (date == 5)) {
				bizCode = "12";			//�ĸ���		1961 -
			} else if ((month == buddaMonth) && (date == buddaDate)) {
				bizCode = "14";			//����ź����
			} else if ((month == 5) && (date == 5)) {
				bizCode = "13";			//��̳�		1970 - 
			} else if ((month == 6) && (date == 6)) {
				bizCode = "15";			//������		1970 - 
			} else if ((month == 7) && (date == 17)) {
				bizCode = "16";			//������		1948 - 
			} else if ((month == 8) && (date == 15)) {
				bizCode = "17";			//������		1950 - 
			} else if ((month == thanksGivingMonth1) && (date == thanksGivingDate1)) {
				bizCode = "18";			//�߼�����
			} else if ((month == thanksGivingMonth2) && (date == thanksGivingDate2)) {
				bizCode = "18";			//�߼�		
			} else if ((month == thanksGivingMonth3) && (date == thanksGivingDate3)) {
				bizCode = "18";			//�߼�������
			} else if ((month == 10) && (date == 3)) {
				bizCode = "19";			//��õ��		1949 - 
			} else if ((month == 12) && (date == 25)) {
				bizCode = "20";			//��ź��        1949 - 
			} else if ((month == 5) && (date == 1)) {
				bizCode = "21";			//�ٷ����ǳ�	1994 - 
			} else if (day == 1) {
				bizCode = "90";			//�Ͽ���
			} else if (day == 7) {
				bizCode = "02";			//������(��������)
			} else {
				bizCode = "00";
			}

			bizData[i-1] = sDate + day + bizCode;

		}
		return bizData;
	}


	/** ���ڿ��� �Էµ� �� ��¥ ������ ������ ��ȯ�Ѵ�.
	  @param from                 ���۳�¥
	  @param to                   ���ᳯ¥
	  @return int
	*/
	public static int betweenDay(String from,String to) {
		Calendar cFrom = Calendar.getInstance();
		Calendar cTo = Calendar.getInstance();
		int year = Integer.parseInt(from.substring(0,4));
		int month = Integer.parseInt(from.substring(4,6)) - 1;
		int date = Integer.parseInt(from.substring(6,8));

		cFrom.set(year,month,date);
		year = Integer.parseInt(to.substring(0,4));
		month = Integer.parseInt(to.substring(4,6)) - 1;
		date = Integer.parseInt(to.substring(6,8));
		cTo.set(year,month,date);

		java.util.Date dFrom = cFrom.getTime();
		java.util.Date dTo = cTo.getTime();

		return (int)((dTo.getTime() - dFrom.getTime()) / 86400000);
	}


	/** ����Ÿ���� ��¥������ TimestampŸ������ ��ȯ�Ͽ� ��ȯ�Ѵ�.
	  @param  date      ��¥���ڿ�
	  @param  setTime   ����ð� ��������(true - ����,false - ����)
	  @return Timestamp
	*/
	public static Timestamp toTimestamp(String date,boolean setTime) {
		Calendar c = Calendar.getInstance();
		int year = 0;
		int month = 0;
		int day = 0;

		year = Integer.parseInt(date.substring(0,4));
		month = Integer.parseInt(date.substring(4,6)) - 1;
		day = Integer.parseInt(date.substring(6,8));

		if (setTime) {
			c.set(year,month,day);
		} else {
			c.set(year,month,day,0,0,0);
			c.set(Calendar.MILLISECOND,0);
		}

		return new Timestamp(c.getTime().getTime());
	}
	/** ����Ÿ���� ��¥������ TimestampŸ������ ��ȯ�Ͽ� ��ȯ�Ѵ�.
	  @param  date      ��¥���ڿ�
	  @return Timestamp
	*/
	public static Timestamp toTimestamp(String date) {
		return toTimestamp(date,false);
	}



	/** ���� �ý��� ��¥�� ��ȯ�Ѵ�.
	  @return String
	*/
	public static String getToday() {
		Calendar cal = Calendar.getInstance();

		cal.setTime(new java.util.Date(System.currentTimeMillis()));

		return FormatData.numToStr(cal.get(Calendar.YEAR),4,"0") + FormatData.numToStr(cal.get(Calendar.MONTH) + 1,2,"0") + FormatData.numToStr(cal.get(Calendar.DATE),2,"0");
	}


	/** ���ڿ��� �Էµ� ��¥�� ���� Ư�������� �帥��¥�� ���Ѵ�.
	  @param from                 ���۳�¥
	  @param year                 ����� ���
	  @param month                ����� �޼�
	  @param date                 ����� �ϼ�
	  @return String
	*/
	public static String calcDate(String from,int year,int month,int date) {
		Calendar cFrom = Calendar.getInstance();

		int year2 = Integer.parseInt(from.substring(0,4));
		int month2 = Integer.parseInt(from.substring(4,6)) - 1;
		int date2 = Integer.parseInt(from.substring(6,8));
		cFrom.set(year2,month2,date2);

		cFrom.add(Calendar.YEAR,year);
		cFrom.add(Calendar.MONTH,month);
		cFrom.add(Calendar.DATE,date);

		return FormatData.numToStr(cFrom.get(Calendar.YEAR),4,"0") + FormatData.numToStr(cFrom.get(Calendar.MONTH) + 1,2,"0") + FormatData.numToStr(cFrom.get(Calendar.DATE),2,"0");
	}
/** ���� �ý������ڸ� ������ FORMAT�� ��ȯ�Ѵ�.
	  @param curFormat                 FORMAT
	  @return String
	*/	
	 public static String getDateFormat(String curFormat)

    {

        java.util.Date date = new java.util.Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat(curFormat);



        return dateFormat.format(date);

    }

 /**

   * date�� ���� ��¥�� iMonth ��ŭ ���� �� �ؼ� ��ȯ�ϴ� �޼ҵ�

   * @param date 'yyyy-mm-dd' : ������

   * @param iMonth : ����

   * @return String

   */

  public static String addMonth(String date,int iMonth)

  {

    

    try

    {

      int iy = Integer.parseInt(date.substring(0,4));

      int im = Integer.parseInt(date.substring(4,6));

      int id = Integer.parseInt(date.substring(6,8));



      setCustomDate(iy,im,id);

      addMonth(iMonth);

      return getCurrentDateNoSeper();

    }catch(Exception e)

    {

      return "";

    }

  }





  /**

    * DateTime�� ��¥�� iYear,iMonth, iDay�� �����Ѵ�.

    * setCustomDate(2002,5,27)

    * @param iYear : ���

    * @param iMonth : ����

    * @param iDay :�ϼ�

    * @return String

    */

  public static String setCustomDate(int iYear, int iMonth, int iDay)

  {

    String month="";

    String day="";

    String hour="";

    setYear(iYear);

    setMonth(iMonth);

    setDay(iDay);



    if(getMonth() < 10)

    {

      month = "0" + getMonth();

    }

    else

    {

      month = "" + getMonth();

    }

    if(getDay() < 10)

    {

      day = "0" + getDay();

    }

    else

    {

      day = "" + getDay();

    }

    return getYear() + month  + day;

  }

  

  /**

   * ���� iMonth��ŭ ����

   * @param iMonth : �޼�

   */

  public static void addMonth(int iMonth)

  {

    c.add(Calendar.MONTH,iMonth);

  }



  /**

   * ������ �������� ���� ��¥�� ��ȯ�Ѵ�

   * @return return yyyymmdd

   */

  public static String getCurrentDateNoSeper()

  {

    String month="";

    String day="";

    String hour="";

    if(getMonth() < 10)

    {

      month = "0" + getMonth();

    }

    else

    {

      month = "" + getMonth();

    }

    if(getDay() < 10)

    {

      day = "0" + getDay();

    }

    else

    {

      day = "" + getDay();

    }

    

    return getYear() + month  + day;

  }



  /**

   * iYear�� calendar ��ü�� ����

   * @param iYear : ���

   */

  public static void setYear(int iYear)

  {

    c.set(Calendar.YEAR, iYear);

  }



  /**

  	* ���� int�� ��ȯ

    * @return int

  	*/

  public static int getYear()

  {

    return c.get(Calendar.YEAR);

  }



  /**

   * iMonth�� �� ����

   * @param iMonth : �޼�

   */

  public static void setMonth(int iMonth)

  {

    c.set(Calendar.MONTH,iMonth-1);

  }



  /**

  	* ���� int�� ��ȯ

    * @return int

  	*/

  public static int getMonth()

  {

    return c.get(Calendar.MONTH) + 1;

  }



  /**

   * iDay�� ��¥ ����

   * @param iDay ��¥��

   */

  public static void setDay(int iDay)

  {

    c.set(Calendar.DATE,iDay);

  }

 /**

  	* ���� int�� ��ȯ

    * @return int

  	*/

  public static int getDay()

  {

    return c.get(Calendar.DATE);

  }

}