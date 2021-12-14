package com.example.memo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetUtils {
    public static String getNetData(String path) {
        String resultString = null;
        try {
            URL url = new URL(path);
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3 * 1000);
                connection.setReadTimeout(3 * 1000);
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // 开始读取服务器的数据
                StringBuilder builder = new StringBuilder();
                String str = bufferedReader.readLine();
                while (str != null) {
                    builder.append(str);
                    // 读取下一行数据
                    str = bufferedReader.readLine();
                }
                // 把StringBuilder转换成我们常用的类型
                resultString = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
