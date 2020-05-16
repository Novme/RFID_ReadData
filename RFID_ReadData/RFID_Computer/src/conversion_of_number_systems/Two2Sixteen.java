package conversion_of_number_systems;

public class Two2Sixteen {
	public static void main(String[] args) {// 测试

		System.out.println(binaryStrToHexStr("110001110101111001110111"));
	}

	/**
	 * 二进制字符串转换为十六进制字符串
	 * <p>
	 * 二进制字符串位数必须满足是4的倍数
	 * 
	 * @param binaryStr
	 * @return
	 */
	public static String binaryStrToHexStr(String binaryStr) {

		if (binaryStr == null || binaryStr.equals("")
				|| binaryStr.length() % 4 != 0) {
			return null;
		}

		StringBuffer sbs = new StringBuffer();
		// 二进制字符串是4的倍数，所以四位二进制转换成一位十六进制
		for (int i = 0; i < binaryStr.length() / 4; i++) {
			String subStr = binaryStr.substring(i * 4, i * 4 + 4);
			String hexStr = Integer.toHexString(Integer.parseInt(subStr, 2));
			sbs.append(hexStr);
		}

		return sbs.toString().toUpperCase();
	}
}
