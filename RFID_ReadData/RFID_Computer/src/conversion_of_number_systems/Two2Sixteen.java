package conversion_of_number_systems;

public class Two2Sixteen {
	public static void main(String[] args) {// ����

		System.out.println(binaryStrToHexStr("110001110101111001110111"));
	}

	/**
	 * �������ַ���ת��Ϊʮ�������ַ���
	 * <p>
	 * �������ַ���λ������������4�ı���
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
		// �������ַ�����4�ı�����������λ������ת����һλʮ������
		for (int i = 0; i < binaryStr.length() / 4; i++) {
			String subStr = binaryStr.substring(i * 4, i * 4 + 4);
			String hexStr = Integer.toHexString(Integer.parseInt(subStr, 2));
			sbs.append(hexStr);
		}

		return sbs.toString().toUpperCase();
	}
}
