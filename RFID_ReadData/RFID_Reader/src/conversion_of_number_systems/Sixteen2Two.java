package conversion_of_number_systems;

public class Sixteen2Two {
	public static void main(String[] args) {// 测试
		int i;
		String t;
		for (i = 0; i < 10; i++) {
			t = i + "";
			System.out.println(hexString2binaryString(t) + "\r\n");
			System.out.println(hexStrToBinaryStr(t) + "\r\n");
			t = "";
		}
		System.out.println(hexStrToBinaryStr("a") + "\r\n");
		System.out.println(hexStrToBinaryStr("b") + "\r\n");
		System.out.println(hexStrToBinaryStr("c") + "\r\n");
		System.out.println(hexStrToBinaryStr("d") + "\r\n");
		System.out.println(hexStrToBinaryStr("e") + "\r\n");
		System.out.println(hexStrToBinaryStr("f") + "\r\n");
		
		System.out.println(hexString2binaryString("A") + "\r\n");
		System.out.println(hexString2binaryString("b") + "\r\n");
		System.out.println(hexString2binaryString("c") + "\r\n");
		System.out.println(hexString2binaryString("d") + "\r\n");
		System.out.println(hexString2binaryString("e") + "\r\n");
		System.out.println(hexString2binaryString("f") + "\r\n");

	}

	public static final String hexString2binaryString(String hexString) {//1 ，十六进制字符串转二进制字符串
		if (hexString == null)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * 将十六进制的字符串转换成二进制的字符串
	 * 
	 * @param hexString
	 * @return
	 */
	public static String hexStrToBinaryStr(String hexString) {//2，十六进制字符串转二进制字符串

		if (hexString == null || hexString.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		// 将每一个十六进制字符分别转换成一个四位的二进制字符
		for (int i = 0; i < hexString.length(); i++) {
			String indexStr = hexString.substring(i, i + 1);
			String binaryStr = Integer.toBinaryString(Integer.parseInt(
					indexStr, 16));
			while (binaryStr.length() < 4) {
				binaryStr = "0" + binaryStr;
			}
			sb.append(binaryStr);
		}

		return sb.toString();
	}

}
