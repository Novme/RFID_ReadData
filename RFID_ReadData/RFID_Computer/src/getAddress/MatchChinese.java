package getAddress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/*
 * 根据传入的省市县信息返回相应的二进制字符串
 */
public class MatchChinese {
	public static void main(String[] args) throws IOException {
		System.out.println(getBinary("河南省", "驻马店市", "新蔡县"));
		
	}

	public static String getBinary(String province, String city, String county)
			throws IOException, IOException {
		String num = "";

		HashMap<String, String> hmProvince = GetData
				.getSingleData("address\\province.txt");
		HashMap<String, String> hmCity = GetData.getSingleData("address\\"
				+ province + "_市.txt");
		HashMap<String, String> hmCounty = GetData.getSingleData("address\\"
				+ province + "_" + city + ".txt");
		for (String key : hmProvince.keySet()) {// 获取省二进制编号
			if (province.equals(hmProvince.get(key))) {
				num = num + key;
			}
		}
		for (String key : hmCity.keySet()) {// 获取具体省_市二进制编号
			if (city.equals(hmCity.get(key))) {
				num = num + key;
			}
		}
		for (String key : hmCounty.keySet()) {// 获取具体省_市_县二进制编号
			if (county.equals(hmCounty.get(key))) {
				num = num + key;
			}
		}
		return num;
	}
}
