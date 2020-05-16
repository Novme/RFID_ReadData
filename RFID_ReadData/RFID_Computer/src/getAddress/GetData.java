package getAddress;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*
 * ����Ϊ�˻�ȡ�ļ��е��������ݣ�Ϊ���ݵ�ƥ����׼��
 */
public class GetData {
	public static void main(String[] args) throws IOException {// ������Ϊ�˲���
		String name = "address\\province.txt";
		HashMap<String, String> hm = getSingleData(name);
		/*Set<String> keySet = hm.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = hm.get(key);
			System.out.println(key + "=" + value);
		}*/
		int i = 0;
		for (String key : hm.keySet()) {
			System.out.println(key + "=" + hm.get(key));
			i++;
		}
		System.out.println(i);
	}

	public static HashMap<String, String> getSingleData(String name)
			throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(name));// �����ַ���
		HashMap<String, String> hm = new HashMap<>();// ����HashMap
		String line;
		while ((line = br.readLine()) != null) {// ���������ж�ȡ
			String[] arr = line.split(",");// �����������ݷֿ���ȡ
			hm.put(arr[0], arr[1]);// �����ݷŵ�hm������
		}
		br.close();
		return hm;
	}
}
