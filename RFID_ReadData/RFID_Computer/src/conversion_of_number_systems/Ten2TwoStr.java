package conversion_of_number_systems;

public class Ten2TwoStr {
	public static void main(String[] args) {
		System.out.println(decimal2Binary(15));
	}
	public static String decimal2Binary(int d) {
		// return Integer.toBinaryString((int)d);
		/*
		 * �������������Integer.toBinaryString(int)�Ϳ��Եõ��������ֵĶ����ƽ����
		 * ��Ϊ��չʾʮ����ת�����Ƶ��㷨����ѡ�����³���������ת��
		 */
		String result = "";
		long inte = (long) d;
		int index = 0;
		while (true) {
			result += inte % 2;
			inte = inte / 2;
			index++;
			/*if (index % 4 == 0) {
				result += " ";
			}*/
			if (inte == 0) {
				while (index % 4 != 0) {
					result += "0";
					index++;
				}
				break;
			}
		}
		char[] c = result.toCharArray();
		char[] cc = new char[c.length];
		for (int i = c.length; i > 0; i--) {
			cc[cc.length - i] = c[i - 1];
		}
		return new String(cc);
	}
}
