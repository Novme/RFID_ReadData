package getAddress;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*
 * 该类为了获取文件中单个的数据，为数据的匹配做准备
 */
public class GetData {
	public static void main(String[] args) throws IOException {// 主方法为了测试
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
		BufferedReader br = new BufferedReader(new FileReader(name));// 创建字符流
		HashMap<String, String> hm = new HashMap<>();// 创建HashMap
		String line;
		while ((line = br.readLine()) != null) {// 将数据整行读取
			String[] arr = line.split(",");// 将读到的数据分开存取
			hm.put(arr[0], arr[1]);// 将数据放到hm集合中
		}
		br.close();
		return hm;
	}
}
