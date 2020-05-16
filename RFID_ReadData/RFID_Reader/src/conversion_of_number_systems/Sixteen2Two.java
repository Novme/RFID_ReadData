package conversion_of_number_systems;

public class Sixteen2Two {
	public static void main(String[] args) {// ����
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

	public static final String hexString2binaryString(String hexString) {//1 ��ʮ�������ַ���ת�������ַ���
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
	 * ��ʮ�����Ƶ��ַ���ת���ɶ����Ƶ��ַ���
	 * 
	 * @param hexString
	 * @return
	 */
	public static String hexStrToBinaryStr(String hexString) {//2��ʮ�������ַ���ת�������ַ���

		if (hexString == null || hexString.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		// ��ÿһ��ʮ�������ַ��ֱ�ת����һ����λ�Ķ������ַ�
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
