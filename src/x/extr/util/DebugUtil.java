package x.extr.util;

/**
 * <p>파일명 : DebugUtil.java </p>
 * <p>버전 : 1.1.1.0</p>
 * <p>작성일 : 2006. 2. 20 </p>
 * <p>작성자 : 황재천</p>
 * <p>내용 : getter/setter를 가진 Value Object의 trace를 위해
 * toString상속받아 사용할수 있도록 작성함.
 * </p>
 */
public class DebugUtil {
	public DebugUtil() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		String fName = null;
		String uName = null;
		Class cls = this.getClass();
		String value = null;
		java.lang.reflect.Field[] f = cls.getDeclaredFields();

		sBuffer.append("[============ ");
		sBuffer.append(cls.getName());
		sBuffer.append(" ============\n");
		int len = f.length;
		for (int i = 0; i < len; i++) {
			try {
				fName = f[i].getName();
				uName = fName.substring(0, 1).toUpperCase() +
					fName.substring(1, fName.length());

				java.lang.reflect.Method m = cls.getMethod("get" + uName,
					new Class[] {});
				value = (String) m.invoke(this, new Object[] {});
				sBuffer.append("\t#");
				sBuffer.append(i + 1);
				sBuffer.append("#");
				sBuffer.append(fName);
				sBuffer.append("|");
				sBuffer.append(value);
				sBuffer.append("|");
				if ( (i + 1) % 5 == 0 && i != (len - 1)) {
					sBuffer.append("\n");
				}
			}
			catch (Exception e) {

			}
		}
		sBuffer.append("\n]");

		return sBuffer.toString();
	}

}
