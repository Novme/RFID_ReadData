package conversion_of_number_systems;

public class Sixteen2Two {
	public static void main(String[] args) {// ����
		int i;
		String t;
		
		System.out.println(spaceAt6(hexString2binaryString("C75E77")) + "\r\n");
		System.out.println(hexStrToBinaryStr("10C72CF4D76DF8E71CB3D35D"));
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
			String binaryStr = Integer.toBinaryString(Integer.parseInt(indexStr, 16));
			while (binaryStr.length() < 4) {
				binaryStr = "0" + binaryStr;
			}
				sb.append(binaryStr);
			
		}
 

		
		
		return sb.toString();
	}
	public static String spaceAt6(String str) {
		 
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i += 6) {
            if (length - i <= 12) {      //��ֹArrayIndexOutOfBoundsException
                sb.append(str.substring(i, i + 6)).append(" ");
                sb.append(str.substring(i + 6));
                break;
            }
            sb.append(str.substring(i, i + 6)).append(" ");
        }
 
        return sb.toString();
    }

}
