package com.gwh.pufdemo.Controller;


import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 文件加密解密的操作
 * @Author: zhangyan
 * @Date: 2019/8/20 13:01
 * @Version 1.0
 */
@Controller
public class FileCompressController {

    //http://123.207.99.248:7777
    private String fileStoreName="E:\\java\\pufdemo\\test1.txt";
    private static String fileStorePATH="E:\\java\\pufdemo\\";
    private static  String filePath = "E:\\java\\pufdemo\\";
    //private static final String FAR_SERVICE_DIR = "http://localhost:8081/luckymoney/receive";//远程服务器接受文件的路由
    private static final String FAR_SERVICE_DIR ="http://119.23.11.21:7777/api/compress/common";
    private static final long yourMaxRequestSize = 10000000;
    private static  String fileName2="";
    /**
     * 定义操作页面
     * @return
     */
    @RequestMapping("/compress")
    public String FileCompress(Model model)
    {
        return "indexCompress";
    }

    @ResponseBody
    @RequestMapping("/pufDemo/upload")
    public String upload(HttpServletRequest request) throws Exception {
       //判断enctype属性是否为multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart)
            throw new IllegalArgumentException(
                    "上传内容不是有效的multipart/form-data类型.");

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        Iterator<String> iter = req.getFileNames();

        String res=null;

        while (iter.hasNext()) {
            MultipartFile file = req.getFile(iter.next());
            String fileName = file.getOriginalFilename();
            //FileItem item = (FileItem) iter.next();
            int split = fileName.lastIndexOf(".");

            InputStream is = file.getInputStream();
            fileStoreName=fileName;




                // ...

                //上传到远程服务器
                InputStream uploadedStream = is;
                HashMap<String, InputStream> files = new HashMap<String, InputStream>();
                files.put(fileName, uploadedStream);
                res=uploadToFarService(files);
                //uploadedStream.close();


            }

        return res;
    }

    public String uploadToFarService(HashMap<String, InputStream> files) {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            //获取字节流
            InputStream in=conn.getInputStream();
            File uploadedFile = new File(filePath+fileStoreName);
            FileOutputStream fos = new FileOutputStream(uploadedFile);

            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            System.out.println();
            System.out.println("保存到文件" +filePath+fileStoreName + "成功");


//            System.out.println("================控制台显示====================");
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                //进行相应的处理
//                System.out.println(line);
//            }



        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }

        return "文件经过专用无损压缩后，保存到" +filePath+fileStoreName + "成功";
    }





}
