package sendDataToServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;


import org.omg.CORBA_2_3.portable.OutputStream;

public class SendToServer {

    private static String result = null;

    //默认的url
    private static String httpUrl = "http://localhost:8080/Group8_Server";

    /*
     * data:用键值对的方式传入这个方法：
     *   需要的数据：订单号、实时地址
     *
     *      数据格式举例：
     *      key                value
     *      order_id=12344757748785
     *      real_time_address = ......
     *
     *
     * */
    public static Boolean sendDataToServer(String httpUrl,Map<String,String> data) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        ObjectMapper objectMapper = null;
        try {
            URL url = new URL(httpUrl);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("post");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setConnectTimeout(15000);

            connection.setReadTimeout(60000);

            connection.setDoOutput(true);

            connection.setDoInput(true);

            //Map集合序列化
            String jsonData = objectMapper.writeValueAsString(data);


//            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            os = connection.getOutputStream();

            os.write(jsonData.getBytes());

            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 输入流对象包装
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 遍历
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static String getResult() {
        return result;
    }

    public static void setResult(String result) {
        SendToServer.result = result;
    }

    public static String getHttpUrl() {
        return httpUrl;
    }

    public static void setHttpUrl(String httpUrl) {
        SendToServer.httpUrl = httpUrl;
    }
}
