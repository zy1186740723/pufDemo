package com.gwh.pufdemo.Service.PUFService.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangyan
 * @Date: 2019/8/20 16:25
 * @Version 1.0
 */
public class URLConnectionTest {


        public static void main(String[] args) {
            // 指定表单提交的url地址
            String url = "http://192.168.8.138:7777/api/compress/common";
            // 将上传控件之外的其他控件的数据信息存入map对象
            Map<String, String> map = new HashMap<String, String>();
            map.put("username", "test");
            map.put("password", "123456");
            // 指定要上传到服务器的文件的客户端路径
            String filePath = "E:\\java\\pufdemo\\test.jpg";

            // 获取到要上传的文件的输入流信息，通过ByteArrayOutputStream流转成byte[]
            BufferedInputStream bis = null;
            byte[] body_data = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int c = 0;
            byte[] buffer = new byte[8 * 1024];
            try {
                while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
                }
                body_data = baos.toByteArray();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 调用自定义的post数据方法，提交表单数据及上传文件
            String result = doPostSubmitBody(url, map, filePath, body_data, "utf-8");
            System.out.println(new String(result));

        }
    public static String doPostSubmitBody(String url, Map<String, String> map,
                                          String filePath, byte[] body_data, String charset) {
        // 设置三个常用字符串常量：换行、前缀、分界线（NEWLINE、PREFIX、BOUNDARY）；
        final String NEWLINE = "\r\n";
        final String PREFIX = "--";
        final String BOUNDARY = "#";
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        DataOutputStream dos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL urlObj = new URL(url);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            httpConn = (HttpURLConnection) urlObj.openConnection();
            // 调用HttpURLConnection对象setDoOutput(true)、setDoInput(true)、setRequestMethod("POST")；
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            // 设置Http请求头信息；（Accept、Connection、Accept-Encoding、Cache-Control、Content-Type、User-Agent）
            httpConn.setUseCaches(false);
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpConn.setRequestProperty("Cache-Control", "no-cache");
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            httpConn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            httpConn.connect();

            // 调用HttpURLConnection对象的getOutputStream()方法构建输出流对象；
            dos = new DataOutputStream(httpConn.getOutputStream());
            // 获取表单中上传控件之外的控件数据，写入到输出流对象（根据HttpWatch提示的流信息拼凑字符串）；
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = map.get(key);
                    dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                    dos.writeBytes("Content-Disposition: form-data; "
                            + "name=\"" + key + "\"" + NEWLINE);
                    dos.writeBytes(NEWLINE);
                    dos.writeBytes(URLEncoder.encode(value.toString(), charset));
                    // 或者写成：dos.write(value.toString().getBytes(charset));
                    dos.writeBytes(NEWLINE);
                }
            }

            // 获取表单中上传控件的数据，写入到输出流对象（根据HttpWatch提示的流信息拼凑字符串）；
            if (body_data != null && body_data.length > 0) {
                dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                String fileName = filePath.substring(filePath
                        .lastIndexOf(File.separatorChar));
                dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                        + "uploadFile" + "\"" + "; filename=\"" + fileName
                        + "\"" + NEWLINE);
                dos.writeBytes(NEWLINE);
                dos.write(body_data);
                dos.writeBytes(NEWLINE);
            }
            dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE);
            dos.flush();

            // 调用HttpURLConnection对象的getInputStream()方法构建输入流对象；
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            // 调用HttpURLConnection对象的getResponseCode()获取客户端与服务器端的连接状态码。如果是200，则执行以下操作，否则返回null；
            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
                }
            }

            //获取字节流
            InputStream in=httpConn.getInputStream();
            File uploadedFile = new File("E:\\java\\pufdemo\\test.jpg");
            FileOutputStream fos = new FileOutputStream(uploadedFile);

            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            System.out.println("保存到文件" +"E:\\java\\pufdemo\\test.jpg"+ "成功");
            // 将输入流转成字节数组，返回给客户端。
            return new String(baos.toByteArray(), charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
                bis.close();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
