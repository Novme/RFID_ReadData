package getAddress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/*
 * ���ݴ����ʡ������Ϣ������Ӧ�Ķ������ַ���
 */
public class MatchChinese {
	public static void main(String[] args) throws IOException {
		System.out.println(getBinary("����ʡ", "פ�����", "�²���"));
		
	}

	public static String getBinary(String province, String city, String county)
			throws IOException, IOException {
		String num = "";

		HashMap<String, String> hmProvince = GetData
				.getSingleData("address\\province.txt");
		HashMap<String, String> hmCity = GetData.getSingleData("address\\"
				+ province + "_��.txt");
		HashMap<String, String> hmCounty = GetData.getSingleData("address\\"
				+ province + "_" + city + ".txt");
		for (String key : hmProvince.keySet()) {// ��ȡʡ�����Ʊ��
			if (province.equals(hmProvince.get(key))) {
				num = num + key;
			}
		}
		for (String key : hmCity.keySet()) {// ��ȡ����ʡ_�ж����Ʊ��
			if (city.equals(hmCity.get(key))) {
				num = num + key;
			}
		}
		for (String key : hmCounty.keySet()) {// ��ȡ����ʡ_��_�ض����Ʊ��
			if (county.equals(hmCounty.get(key))) {
				num = num + key;
			}
		}
		return num;
	}
}
