package com.gwh.pufdemo.Service.PUFService;

import com.gwh.pufdemo.DAO.UserInfo;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: zhangyan
 * @Date: 2019/8/21 11:01
 * @Version 1.0
 */
@Service
public class PufStringDepress {

    private static final String tempFile="E:\\java\\pufdemo\\test3.txt";
    //private String fileStoreName="E:\\java\\pufdemo\\test1.txt";
    private static final String FAR_SERVICE_DIR ="http://119.23.11.21:7777/api/depress/common";
    public String pufStringDepress(UserInfo userInfo) throws Exception{
        String res="";
        byte[] info=userInfo.getDataValue();
        //String carId=userInfo.getDataValue().toString();
        ByteArrayInputStream stream= new ByteArrayInputStream(info);



        HashMap<String, InputStream> files = new HashMap<String, InputStream>();
        files.put("file1", stream);

        try {
            String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
            URL url = new URL(FAR_SERVICE_DIR);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
            Iterator iter = files.entrySet().iterator();
            int i=0;
            while (iter.hasNext()) {
                i++;
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                InputStream val = (InputStream) entry.getValue();
                String fname = key;
                File file = new File(fname);
                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"file" + i
                        + "\";filename=\"" + key + "\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");

                byte[] data = sb.toString().getBytes();
                out.write(data);
                DataInputStream in = new DataInputStream(val);
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
                in.close();
            }
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    conn.getInputStream(), "UTF-8"));
            //获取字节流
            InputStream in=conn.getInputStream();

            //inputStream转string
            StringBuffer out2 = new StringBuffer();
            byte[] b2 = new byte[4096];
            for (int n; (n = in.read(b2)) != -1; ) {
                out2.append(new String(b2, 0, n));
            }
            System.out.println(out2);

            res= out2.toString();


//
//
//            File uploadedFile = new File(tempFile);
//            FileOutputStream fos = new FileOutputStream(uploadedFile);
//
//            byte[] b = new byte[1024];
//            int len;
//            while ((len = in.read(b)) != -1) {
//                fos.write(b, 0, len);
//            }
//            System.out.println();
//            System.out.println("保存到文件" +tempFile + "成功");
//
//            //读取tempfile中的数据
//            File tF = new File(tempFile);
//
//            InputStream is=new FileInputStream(tF);
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            int temp;
//            while ((temp = is.read()) != -1) {
//                baos.write(temp);
//            }
//            carId = baos.toString();


        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        return res;
    }




}
