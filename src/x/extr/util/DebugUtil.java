package x.extr.util;

/**
 * <p>���ϸ� : DebugUtil.java </p>
 * <p>���� : 1.1.1.0</p>
 * <p>�ۼ��� : 2006. 2. 20 </p>
 * <p>�ۼ��� : Ȳ��õ</p>
 * <p>���� : getter/setter�� ���� Value Object�� trace�� ����
 * toString��ӹ޾� ����Ҽ� �ֵ��� �ۼ���.
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
