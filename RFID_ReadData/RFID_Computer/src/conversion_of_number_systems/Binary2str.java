package conversion_of_number_systems;

public class Binary2str {
	public static void main(String[] args) {
		String binary = "110001 110101 111001 110111";
		System.out.println(toString(binary));
	}

	public static String toString(String binary) {
		String[] tempStr = binary.split(" ");
		char[] tempChar = new char[tempStr.length];
		for (int i = 0; i < tempStr.length; i++) {
			tempChar[i] = BinstrToChar(tempStr[i]);
		}
		return String.valueOf(tempChar);
	}

	// 将二进制字符串转换成int数组
	public static int[] BinstrToIntArray(String binStr) {
		char[] temp = binStr.toCharArray();
		int[] result = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = temp[i] - 48;
		}
		return result;
	}

	// 将二进制转换成字符
	public static char BinstrToChar(String binStr) {
		int[] temp = BinstrToIntArray(binStr);
		int sum = 0;
		for (int i = 0; i < temp.length; i++) {
			sum += temp[temp.length - 1 - i] << i;
		}
		return (char) sum;
	}

}
