package getAddress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/*
 * ���ݴ����ʡ������Ϣ������Ӧ�Ķ������ַ���
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
		for (String key : hmProvince.keySet()) {// ��ȡʡ�����Ʊ�Ŷ�Ӧ��ʡ
			if (province.equals(key)) {
				p = hmProvince.get(key);
				hmCity = GetData.getSingleData("address\\"//��ȡ����ʡ�µ����е��м���
						+ p + "_��.txt");
			}
		}
		for (String key : hmCity.keySet()) {// ��ȡ����ʡ_�ж����Ʊ�Ŷ�Ӧ����
			if (city.equals(key)) {
				c = hmCity.get(key);
				hmCounty = GetData.getSingleData("address\\"//��ȡ�������µ������صļ���
						+ p + "_" + c + ".txt");
			}
		}
		for (String key : hmCounty.keySet()) {// ��ȡ����ʡ_��_�ض����Ʊ�Ŷ�Ӧ����
			if (county.equals(key)) {
				co = hmCounty.get(key);
			}
		}
		num = p + "," + c + "," +co; 
		return num;
	}
}
