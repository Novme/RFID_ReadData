package getAddress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
/*
 * ������01110 00000 00000������ʡ������Ϣ
 */
public class MatchBinary {
	public static void main(String[] args) throws IOException {
		System.out.println(getDetaileAddress());
	}

	public static String getDetaileAddress() throws IOException, IOException {
		String address = "";// ��ϸ��ַ
		String provice = "01110";// ʡ
		String city = "00000";// ��
		String county = "00000";// ��
		String[] temp = new String[3];// ����
		HashMap<String, String> hm = GetData.getSingleData("address\\province.txt");// ��ȡʡ����Ϣ
		for (String key : hm.keySet()) {
			if (provice.equals(key)) {
				temp[0] = hm.get(key);
				break;
			}
		}

		HashMap<String, String> hm1 = GetData.getSingleData("address\\" + temp[0] + "_"
				+ "��.txt");// ��ȡ����Ϣ
		for (String key : hm1.keySet()) {
			if (city.equals(key)) {
				temp[1] = hm1.get(key);
				break;
			}
		}

		HashMap<String, String> hm2 = GetData.getSingleData("address\\" + temp[0] + "_"
				+ temp[1] + ".txt");// ��ȡ����Ϣ
		for (String key : hm2.keySet()) {
			if (county.equals(key)) {
				temp[2] = hm2.get(key);
				break;
			}
		}

		System.out.println(temp[0] + temp[1] + temp[2]);

		address = address + temp[0] + temp[1] + temp[2];
		return address;
	}
}
