package conversion_of_number_systems;

public class Ten2TwoStr {
	public static void main(String[] args) {
		System.out.println(decimal2Binary(15));
	}
	public static String decimal2Binary(int d) {
		// return Integer.toBinaryString((int)d);
		/*
		 * 本来利用上面的Integer.toBinaryString(int)就可以得到整数部分的二进制结果，
		 * 但为了展示十进制转二进制的算法，现选择以下程序来进行转换
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
