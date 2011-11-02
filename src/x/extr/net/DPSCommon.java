/** Main system          : x.extr
  * Sub system           : net
  * Classname            : DPSCommon.java
  * Initial date         : 2005.11.15
  * Author               : Ȳ��õ
  * Update date/Modifier : 
  * Description          : DPS�ý����� ������� ó���� Class
  * Version information  : v 1.0
  *
  */

package x.extr.net;

/** DPS�ý����� ������� ó���� Class
  * @author Ȳ��õ
*/
public class DPSCommon  {
	/** �ܺα�� - ���������� */
	public static String EX01 = "KFTCONL  ";		//����������
	/** �ܺα�� - ���࿬��ȸ */
	public static String EX02 = "KFBCIF   ";		//���࿬��ȸ
	/** �ܺα�� - �ѽ��� */
	public static String EX03 = "NICECBONL";		//�ѽ���
	/** �ܺα�� - �ѽ���:�ܱ⿬ü���� */
	public static String EX04 = "KISSDONL ";		//�ѽ���-�ܱ⿬ü����
	/** �ܺα�� - �ѽ���:�Һ��ڽſ����� */
	public static String EX05 = "KISSDONLO";		//�ѽ���-�Һ��ڽſ�����
	/** �ܺα�� - �ѳ�Ʈ */
	public static String EX06 = "HANNETONL";		//�ѳ�Ʈ
	/** �ܺα�� - ���� */
	public static String EX07 = "NONGONL  ";		//����
	/** �ܺα�� - �츮���� */
	public static String EX08 = "WOORIONL ";		//�츮����
	/** �ܺα�� - SMS */
	public static String EX09 = "SMSONL   ";		//SMS
	/** �ܺα�� - FAX */
	public static String EX10 = "FAXONL   ";		//FAX
	/** �ܺα�� - E-Mail */
	public static String EX11 = "EMAILONL ";		//E-Mail
	/** �ܺα�� - �ѽ���(FTP) */
	public static String EX12 = "KISSDFTP ";		//�ѽ���(FTP)
	/** �ܺα�� - ����������(FTP) */
	public static String EX13 = "KFTCFTP  ";		//����������(FTP)
	/** �ܺα�� - ���࿬��ȸ(FTP) */
	public static String EX14 = "KFBFTP   ";		//���࿬��ȸ(FTP)
	/** �ܺα�� - �ѽ���:��ݾ����� */
	public static String EX15 = "KISSDONLD";		//�ѽ���:��ݾ�����

	/** �ܺα������ ��û�� - �ѳ�Ʈ */
	public static String EX51 = "HANNETON2";		//�ѳ�Ʈ
	/** �ܺα������ ��û�� - ���� */
	public static String EX52 = "NONGONL2 ";		//����
	/** �ܺα������ ��û�� - ARS */
	public static String EX53 = "ARSONL2  ";		//���࿬��ȸ(FTP)
	/** �ܺα������ ��û�� - KS-NET */
	public static String EX54 = "KSNETONL2";		//KS-NET

	public String exCode   = "__";					// 2
	public String srFlag   = "_";					// 1	
	public String trCode   = "________";			// 8
	public String mngNum   = "____________________";// 20
	public String trSnum   = "__________"; 			// 10
	public String rspnCd   = "____"; 				// 4
	public String trDate   = "________"; 			// 8
	public String trTime   = "______"; 				// 6
	public String dataLen  = "____"; 				// 4
	public String termNo   = "________"; 			// 8
	public String empNo    = "________"; 			// 8
	public String statCd   = "_"; 					// 1
	public String reserved = "____________________";// 20
	public String data     = "";

	/** ������
     * @param sCommonHeader    ���ð������
	 * @param data             �������ʵ�
     */
	public DPSCommon(String sCommonHeader, String sRequestData) {
		try{
			this.exCode   = sCommonHeader.substring(0,2);	// ��ܱ���ڵ�(2)
			this.srFlag   = sCommonHeader.substring(2,3);	// �ۼ��ſ���(1)
			this.trCode   = sCommonHeader.substring(3,11);	// �ŷ������ڵ�(8)
			this.mngNum   = sCommonHeader.substring(11,31);	// ������ȣ(20)
			this.trSnum   = sCommonHeader.substring(31,41);	// �ŷ��Ϸù�ȣ(10)
			this.rspnCd   = sCommonHeader.substring(41,45);	// �����ڵ�(4)
			this.trDate   = sCommonHeader.substring(45,53); // �ŷ�����(8)
			this.trTime   = sCommonHeader.substring(53,59); // �ŷ��ð�(6)
			this.dataLen  = sCommonHeader.substring(59,63); // �����ͱ���(4)
			this.termNo   = sCommonHeader.substring(63,71); // ó���ܸ���ȣ(8)
			this.empNo    = sCommonHeader.substring(71,79); // �ܸ���� �����ȣ(8)
			this.statCd   = sCommonHeader.substring(79,80); // �����ڵ�(1)
			this.reserved = sCommonHeader.substring(80,100);// �����ʵ�(20)
			this.data     = sRequestData;
		}catch(Exception e){
			System.out.println("������� ������ �߸��Ǿ����ϴ�");
		}finally{
			System.out.println("Reserved=[" + reserved + "]");
		}
	} 
	
	/** ������
      * @param exCode           �ܺα���ڵ�
	  * @param trCode           TR-Code
	  * @param bizCode          �����ڵ�
	  * @param userId           �����ID
	  * @param termNo           �ܸ���ȣ
	  * @param srFlag           �ۼ��ű���
	  * @param returnCode       �����ڵ�
	  * @param data             �������ʵ�
      */
	public DPSCommon(String exCode,String srFlag,String trCode,String mngNum,String trSnum,String rspnCd,String trDate,String trTime,String dataLen,String termNo,String empNo, String statCd, String reserved, String data) {
		this.exCode = exCode;
		this.srFlag = srFlag;
		this.trCode = trCode;
		this.mngNum = mngNum;
		this.trSnum = trSnum;
		this.rspnCd = rspnCd;
		this.trDate = trDate;
		this.trTime = trTime;
		this.dataLen= dataLen;
		this.termNo = termNo;
		this.empNo  = empNo;
		this.statCd = statCd;
		this.reserved = reserved;
		this.data = data;

		/*
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		StringBuffer cTime = new StringBuffer(FormatData.numToStr(c.get(Calendar.YEAR),4,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.MONTH) + 1,2,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.DATE),2,"0"));
		
		//������ ��� 24�ð�ǥ������� ǥ���ϱ� ����.
		if (c.get(Calendar.AM_PM) == 1){
			cTime.append(FormatData.numToStr(c.get(Calendar.HOUR) + 12,2,"0"));
		}else{
			cTime.append(FormatData.numToStr(c.get(Calendar.HOUR),2,"0"));
		}
	  //cTime.append(FormatData.numToStr(c.get(Calendar.HOUR)  ,2,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.MINUTE),2,"0"));
		cTime.append(FormatData.numToStr(c.get(Calendar.SECOND),2,"0"));
		this.sysDate = cTime.toString();
		*/
	}
	/** ������
      */
	public DPSCommon() {};

	/** ���������� ���ڿ��� ��ȯ
	  * @return String
      */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(exCode);
		buf.append(srFlag);
		buf.append(trCode);
		buf.append(mngNum);
		buf.append(trSnum);
		buf.append(rspnCd);
		buf.append(trDate);
		buf.append(trTime);
		buf.append(dataLen);
		buf.append(termNo);
		buf.append(empNo);
		buf.append(statCd);
		buf.append(reserved);
		buf.append(data);

		return buf.toString();
	}

	/** ���ڿ�Ÿ���� �������� ����� �����ͺη� ����<p>
	  * String[0] : ���<p>
	  * String[1] : �����ͺ�
      * @param data             ���ڿ� ������
	  * @return String[]
      */
	public static String[] divideHeader(String data) {
		String[] sData = new String[3];
		
		try{
			byte[] temp = data.getBytes();
			sData[0] = new String(temp,0,100);		// ���ð������(������ 100 ����Ʈ)
			if(data.substring(0,2).equals("11")){
				// ���࿬��ȸ
				sData[1] = new String(temp,100,90);
				sData[2] = new String(temp,190,temp.length-190);			
			}else if(data.substring(0,2).equals("12")) {
				// �ѽ���
				sData[1] = new String(temp,100,110);	// �ѽ��� �������(������ �׸��� ���� 10����Ʈ ����+�������100 ����Ʈ)
				sData[2] = new String(temp,210,temp.length-210);		// ������û�� �� ����Ÿ��			
			}else if(data.substring(0,2).equals("13")) {
				// �ѽ���
				if( data.substring(7,11).startsWith("SA")){
					sData[1] = new String(temp,100,127);	// �ѽ��� �������(127 ����Ʈ)
					sData[2] = new String(temp,227,temp.length-227);		// �����				
				}else if( data.substring(7,11).equals("TR23")){
					sData[1] = new String(temp,100,87);	// �ѽ��� �������(87 ����Ʈ)
					sData[2] = new String(temp,187,temp.length-187);		// �����				
				}
			}else if(data.substring(0,2).equals("21")) {
				// KIB
				sData[1] = new String(temp,100,300);	// KIB AP COMMON �������(300 ����Ʈ)
				sData[2] = new String(temp,400,temp.length-400);		// ��������			
			}else if(data.substring(0,2).equals("22")) {
				// KIB
				sData[1] = "No exists gram common division";
				sData[2] = new String(temp,100,110);	// ������� �������� 			
			}
		}catch(Exception e){
			System.out.println("Errors in divideHeader : �������� ����� �����ͺη� ������ �����߻�");
			System.out.println(data);
		}
		return sData;
	}

	/** ���ڿ�Ÿ���� �������� ������� �����ͺη� ����<p>
	  * String[0] : �ܺα���ڵ�<p>
	  * String[1] : TR-Code<p>
	  * String[2] : �����ڵ�<p>
	  * String[3] : ����ID<p>
	  * String[4] : �ܸ���ȣ<p>
	  * String[5] : ó���ð�<p>
	  * String[6] : �ۼ��ű���<p>
	  * String[7] : �����ͺ��� ����<p>
	  * String[8] : �����ڵ�<p>
	  * String[9] : Reserved<p>
	  * String[10] : �����ͺ�
      * @param data             ���ڿ� ������
	  * @return String[]
      */
	public static String[] divideHeaderDetail(String data) {
		String[] sData = new String[14];

		sData[0] = data.substring(0,2); //2
		sData[1] = data.substring(2,3); //1
		sData[2] = data.substring(3,11);//8
		sData[3] = data.substring(11,31);//20
		sData[4] = data.substring(31,41);//10
		sData[5] = data.substring(41,45);//4
		sData[6] = data.substring(45,53);//8
		sData[7] = data.substring(53,59);//6
		sData[8] = data.substring(59,63);//4
		sData[9] = data.substring(63,71);//8
		sData[10] = data.substring(71,79);//8
		sData[11] = data.substring(79,80);//1
		sData[12] = data.substring(80,100);//20
		sData[13] = data.substring(100);

		return sData;
	}
	
	/** 
	 ���������� ���ڿ��� ��ȯ
	 @return String
	 */
	public String toString(String enc) {
		StringBuffer buf = new StringBuffer();
		buf.append(exCode);
		buf.append(srFlag);
		buf.append(trCode);
		buf.append(mngNum);
		buf.append(trSnum);
		buf.append(rspnCd);
		buf.append(trDate);
		buf.append(trTime);
		buf.append(dataLen);
		buf.append(termNo);
		buf.append(empNo);
		buf.append(statCd);
		buf.append(reserved);
		buf.append(data);

		return buf.toString();
	}

}