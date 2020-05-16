package getAddress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/*
 * 根据传入的省市县信息返回相应的二进制字符串
 */
public class MatchCode {
	public static void main(String[] args) throws IOException {
		System.out.println(getAddress("01011", "00010", "01000"));
		
		
		
	}

	public static String getAddress(String province, String city, String county)
			throws IOException, IOException {
		String num = "";
		String p = null;
		String c = null;
		String co = null;
		HashMap<String, String> hmProvince = GetData
				.getSingleData("address\\province.txt");
		HashMap<String, String> hmCity = null;
		HashMap<String, String> hmCounty = null;
		for (String key : hmProvince.keySet()) {// 获取省二进制编号对应的省
			if (province.equals(key)) {
				p = hmProvince.get(key);
				hmCity = GetData.getSingleData("address\\"//获取具体省下的所有的市集合
						+ p + "_市.txt");
			}
		}
		for (String key : hmCity.keySet()) {// 获取具体省_市二进制编号对应的市
			if (city.equals(key)) {
				c = hmCity.get(key);
				hmCounty = GetData.getSingleData("address\\"//获取具体市下的所有县的集合
						+ p + "_" + c + ".txt");
			}
		}
		for (String key : hmCounty.keySet()) {// 获取具体省_市_县二进制编号对应的县
			if (county.equals(key)) {
				co = hmCounty.get(key);
			}
		}
		num = p + "," + c + "," +co; 
		return num;
	}
}
