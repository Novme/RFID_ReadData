package getAddress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
/*
 * 给定“01110 00000 00000”返回省市县信息
 */
public class MatchBinary {
	public static void main(String[] args) throws IOException {
		System.out.println(getDetaileAddress());
	}

	public static String getDetaileAddress() throws IOException, IOException {
		String address = "";// 详细地址
		String provice = "01110";// 省
		String city = "00000";// 市
		String county = "00000";// 县
		String[] temp = new String[3];// 容器
		HashMap<String, String> hm = GetData.getSingleData("address\\province.txt");// 获取省份信息
		for (String key : hm.keySet()) {
			if (provice.equals(key)) {
				temp[0] = hm.get(key);
				break;
			}
		}

		HashMap<String, String> hm1 = GetData.getSingleData("address\\" + temp[0] + "_"
				+ "市.txt");// 获取市信息
		for (String key : hm1.keySet()) {
			if (city.equals(key)) {
				temp[1] = hm1.get(key);
				break;
			}
		}

		HashMap<String, String> hm2 = GetData.getSingleData("address\\" + temp[0] + "_"
				+ temp[1] + ".txt");// 获取县信息
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
