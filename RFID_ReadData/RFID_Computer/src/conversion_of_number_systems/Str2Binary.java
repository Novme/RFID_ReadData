package conversion_of_number_systems;

public class Str2Binary {
	public static void main(String[] args) {
		String str = "1597";
		String binary = toBinary(str);
		System.out.println(binary);
	}

	public static String toBinary(String str) {
		// ���ַ���ת���ַ�����
		char[] strChar = str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			// toBinaryString(int i)���ر����Ķ����Ʊ�ʾ���ַ���
			// toHexString(int i) �˽���
			// toOctalString(int i) ʮ������
			result += Integer.toBinaryString(strChar[i]);
		}
		return result;
	}
}
