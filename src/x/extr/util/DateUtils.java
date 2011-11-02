/** Main system          : foundation
  * Sub system           : util
  * Classname            : DateUtil.java
  * Initial date         : 2005.11.27
  * Author               : 황재천
  * Update date/Modifier : 
  * Description          : 날짜관련 유틸리티 Class
  * Version information  : v 1.0
  */

package x.extr.util;

import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.*;

/** 날짜관련 유틸리티 Class
  * @author 황재천
*/
public class DateUtils {
	String sSystem = "Com";
	String sClassName = "DateUtil";
	private static Calendar c = Calendar.getInstance();

	//음력설 데이터
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

	//석가탄신일 데이터
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

	//추석 데이터
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

	/** 입력된 연도에 해당하는 영업일 어레이를 반환한다.
	  @param year                 년도
	  @return String[]
	*/
	public static String[] getBizData(int year) {
		int dayMax = 365;
		int month;
		int date;
		int day;
		int newYearMonth1,newYearDate1;				// 음력설 전날
		int newYearMonth2,newYearDate2;				// 음력설
		int newYearMonth3,newYearDate3;				// 음력설 다음날
		int buddaMonth,buddaDate;					// 석가탄신일
		int thanksGivingMonth1,thanksGivingDate1;	// 추석 전날
		int thanksGivingMonth2,thanksGivingDate2;	// 추석
		int thanksGivingMonth3,thanksGivingDate3;	// 추석 다음날

		String bizCode;
		String[] bizData;
		String sDate;

		Calendar c = Calendar.getInstance();
		c.set(year,1,29);
		if (c.get(Calendar.MONTH) == 1)	{
			dayMax = 366;
		}
		bizData = new String[dayMax];

		// 음력설 할당
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
		// 석가탄신일 할당
		buddaMonth = -1;
		buddaDate = -1;
		for (int i=0;i<sBudda.length ;i++ )	{
			if (Integer.parseInt(sBudda[i].substring(0,4)) == year) {
				buddaMonth = Integer.parseInt(sBudda[i].substring(4,6));
				buddaDate = Integer.parseInt(sBudda[i].substring(6,8));
				break;
			}
		}
		// 추석 할당
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

			// 1990년 이전에 사라진 공휴일을 제외합니다.
			// 10월 9일 한글날 :    1970 - 1990
			// 음력명절
			// 1월 1일  설날 :      1998부터 3일씩 
			// 4월 8일  석가탄신일 :     - 1975
			if ((month == 1) && (date == 1)) {
				bizCode = "10";			//신정			일제 - 
			} else if ((month == newYearMonth1) && (date == newYearDate1)) {
				bizCode = "11";			//구정전날
			} else if ((month == newYearMonth2) && (date == newYearDate2)) {
				bizCode = "11";			//구정		
			} else if ((month == newYearMonth3) && (date == newYearDate3)) {
				bizCode = "11";			//구정다음날
			} else if ((month == 3) && (date == 1)) {
				bizCode = "22";			//3.1절			1950 -
			} else if ((month == 4) && (date == 5)) {
				bizCode = "12";			//식목일		1961 -
			} else if ((month == buddaMonth) && (date == buddaDate)) {
				bizCode = "14";			//석가탄신일
			} else if ((month == 5) && (date == 5)) {
				bizCode = "13";			//어린이날		1970 - 
			} else if ((month == 6) && (date == 6)) {
				bizCode = "15";			//현충일		1970 - 
			} else if ((month == 7) && (date == 17)) {
				bizCode = "16";			//제헌절		1948 - 
			} else if ((month == 8) && (date == 15)) {
				bizCode = "17";			//광복절		1950 - 
			} else if ((month == thanksGivingMonth1) && (date == thanksGivingDate1)) {
				bizCode = "18";			//추석전날
			} else if ((month == thanksGivingMonth2) && (date == thanksGivingDate2)) {
				bizCode = "18";			//추석		
			} else if ((month == thanksGivingMonth3) && (date == thanksGivingDate3)) {
				bizCode = "18";			//추석다음날
			} else if ((month == 10) && (date == 3)) {
				bizCode = "19";			//개천절		1949 - 
			} else if ((month == 12) && (date == 25)) {
				bizCode = "20";			//성탄절        1949 - 
			} else if ((month == 5) && (date == 1)) {
				bizCode = "21";			//근로자의날	1994 - 
			} else if (day == 1) {
				bizCode = "90";			//일요일
			} else if (day == 7) {
				bizCode = "02";			//영업일(은행제외)
			} else {
				bizCode = "00";
			}

			bizData[i-1] = sDate + day + bizCode;

		}
		return bizData;
	}


	/** 문자열로 입력된 두 날짜 사이의 날수를 반환한다.
	  @param from                 시작날짜
	  @param to                   종료날짜
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


	/** 문자타입의 날짜정보를 Timestamp타입으로 변환하여 반환한다.
	  @param  date      날짜문자열
	  @param  setTime   현재시간 설정유무(true - 설정,false - 비설정)
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
	/** 문자타입의 날짜정보를 Timestamp타입으로 변환하여 반환한다.
	  @param  date      날짜문자열
	  @return Timestamp
	*/
	public static Timestamp toTimestamp(String date) {
		return toTimestamp(date,false);
	}



	/** 현재 시스템 날짜를 반환한다.
	  @return String
	*/
	public static String getToday() {
		Calendar cal = Calendar.getInstance();

		cal.setTime(new java.util.Date(System.currentTimeMillis()));

		return FormatData.numToStr(cal.get(Calendar.YEAR),4,"0") + FormatData.numToStr(cal.get(Calendar.MONTH) + 1,2,"0") + FormatData.numToStr(cal.get(Calendar.DATE),2,"0");
	}


	/** 문자열로 입력된 날짜로 부터 특정조건이 흐른날짜를 구한다.
	  @param from                 시작날짜
	  @param year                 경과된 년수
	  @param month                경과된 달수
	  @param date                 경과된 일수
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
/** 현재 시스템일자를 지정된 FORMAT로 변환한다.
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

   * date로 들어온 날짜에 iMonth 만큼 월을 더 해서 반환하는 메소드

   * @param date 'yyyy-mm-dd' : 기준일

   * @param iMonth : 월수

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

    * DateTime의 날짜를 iYear,iMonth, iDay로 설정한다.

    * setCustomDate(2002,5,27)

    * @param iYear : 년수

    * @param iMonth : 월수

    * @param iDay :일수

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

   * 월에 iMonth만큼 더함

   * @param iMonth : 달수

   */

  public static void addMonth(int iMonth)

  {

    c.add(Calendar.MONTH,iMonth);

  }



  /**

   * 정해진 형식으로 현재 날짜를 반환한다

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

   * iYear로 calendar 객체를 설정

   * @param iYear : 년수

   */

  public static void setYear(int iYear)

  {

    c.set(Calendar.YEAR, iYear);

  }



  /**

  	* 년을 int로 반환

    * @return int

  	*/

  public static int getYear()

  {

    return c.get(Calendar.YEAR);

  }



  /**

   * iMonth로 월 설정

   * @param iMonth : 달수

   */

  public static void setMonth(int iMonth)

  {

    c.set(Calendar.MONTH,iMonth-1);

  }



  /**

  	* 달을 int로 반환

    * @return int

  	*/

  public static int getMonth()

  {

    return c.get(Calendar.MONTH) + 1;

  }



  /**

   * iDay로 날짜 설정

   * @param iDay 날짜수

   */

  public static void setDay(int iDay)

  {

    c.set(Calendar.DATE,iDay);

  }

 /**

  	* 날을 int로 반환

    * @return int

  	*/

  public static int getDay()

  {

    return c.get(Calendar.DATE);

  }

}