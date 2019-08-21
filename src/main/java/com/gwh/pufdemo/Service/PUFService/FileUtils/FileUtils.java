package com.gwh.pufdemo.Service.PUFService.FileUtils;


import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: zhangyan
 * @Date: 2019/8/20 13:57
 * @Version 1.0
 */
public class FileUtils {
    public static String fileUpload(HttpServletRequest request, HttpServletResponse response)throws Exception
    {
        //允许上传的文件类型
        String fileType = "mp3,mp4,video,rmvb,pdf,txt,xml,doc,gif,png,bmp,jpeg";
        //允许上传的文件最大大小(100M,单位为byte)
        int maxSize = 1024*1024*100;
        response.addHeader("Access-Control-Allow-Origin", "*");
        //文件要保存的路径
        String savePath = request.getRealPath("/") + "save/";
        response.setContentType("text/html; charset=UTF-8");
        //检查目录
        File uploadDir = new File(savePath);
        if ( !uploadDir.exists())
        {
            uploadDir.mkdirs();
        }
        if ( !uploadDir.canWrite())
        {
            return "上传目录没有写权限！";
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024); //设置缓冲区大小，这里是1M
        factory.setRepository(uploadDir); //设置缓冲区目录

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");

        List items = upload.parseRequest((RequestContext) request);
        Iterator it = items.iterator();
        FileItem item = null;
        String fileName = "";
        String name = "";
        String extName = "";
        String newFileName = "";
        while (it.hasNext())
        {
            item = (FileItem)it.next();

            fileName = item.getName();
            if (null == fileName || "".equals(fileName))
            {
                continue;
            }

            //判断文件大小是否超限
            if (item.getSize() > maxSize)
            {
                item.delete();
                JOptionPane.showMessageDialog(null, "文件大小超过限制！应小于" + maxSize
                        / 1024 / 1024 + "M");
                return "文件大小超过限制！应小于" + maxSize;
            }

            //判断文件类型是否匹配
            //            System.getProperties().getProperty("file.separator"))
            //获取文件名称
            name = fileName.substring(fileName.lastIndexOf("\\") + 1,
                    fileName.lastIndexOf("."));
            //获取文件后缀名
            extName = fileName.substring(fileName.indexOf(".") + 1).toLowerCase().trim();

            //判断是否为允许上传的文件类型
            if ( !Arrays.<String> asList(fileType.split(",")).contains(extName))
            {
                item.delete();
                JOptionPane.showMessageDialog(null, "文件类型不正确，必须为" + fileType
                        + "的文件！");
                return "文件类型不正确，必须为" + fileType
                        + "的文件！";
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            newFileName = name + df.format(new Date()) + "." + extName;
            File uploadedFile = new File(savePath, newFileName);
            item.write(uploadedFile);
        }

        return "success";
    }

    public static void uploadFile(String fileName) {
        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL url = new URL("http://127.0.0.1:50001");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 上传文件
            File file = new File(fileName);
            StringBuilder sb = new StringBuilder();
            //用于分割参数
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"photo\";filename=\"" + fileName
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(new FileInputStream(
                    file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();
            // 定义BufferedReader输入流来读取URL的响应
//	BufferedReader reader = new BufferedReader(new InputStreamReader(
//	        conn.getInputStream()));
//	String line = null;
//	while ((line = reader.readLine()) != null) {
//	    System.out.println(line);
//	}
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }

    public void client(){
        Socket socket = null;
        OutputStream oo = null;
        FileInputStream fis = null;
        InputStream io = null;
        try {
            //1.建立socket对象实例
            socket = new Socket("127.0.0.1", 50001);
            //2.调用getOutputStream()方法，目的是建立数据缓冲区，将数据发送到服务器端
            oo = socket.getOutputStream();
            //3.创建FileInputStream对象实例，将要发送的文件写入到输出流数据缓冲区中
            File file=new File("E:\\java\\pufdemo\\test.jpg");
            fis = new FileInputStream(file);
            byte[] b=new byte[1024];
            int len;
            while((len=fis.read(b))!=-1){
                oo.write(b, 0, len);
            }
            //这行代码的意思是告诉服务器端我发送的数据完毕。
            socket.shutdownOutput();
            //4.调用getInputStream()方法：目的是接收从服务器端发送的数据
            io = socket.getInputStream();
            byte[] b1=new byte[1024];
            int len1;
            while((len1=io.read(b1))!=-1){
                String str=new String(b1, 0, len1);
                System.out.println(str);
            }
        }  catch (Exception e) {
            // 关闭各个连接
            e.printStackTrace();
        }finally{
            if(io != null){
                try {
                    io.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(fis !=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(oo != null){
                try {
                    oo.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(socket !=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void server(){
        ServerSocket ss = null;
        Socket s = null;
        InputStream is = null;
        FileOutputStream fos = null;
        OutputStream os = null;
        try {
            //1.创建ServerSocket对象的实例。
            ss = new ServerSocket(50001);
            //2.调用accept()方法来创建通信连接，期间经历了三次握手，只不过我们看不到
            s = ss.accept();
            //3.调用getInputStream()方法来接收客户端发送的文件
            is = s.getInputStream();
            //4.创建FileOutputStream()对象实例来保存从客户端发送的文件，并将文件写入到对象中
            fos = new FileOutputStream(new File("E:\\java\\pufdemo\\test.jpg"));
            byte[] b=new byte[1024];
            int len;
            while((len=is.read(b))!=-1){
                fos.write(b, 0, len);
            }
            //5.向客户端反馈信息，将信息储存在数据缓冲区中
            os = s.getOutputStream();
            os.write("你发送的视频我已经接收".getBytes());
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(os !=null){
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(is !=null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(s != null){
                try {
                    s.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(ss != null){
                try {
                    ss.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        FileUtils fileUtils=new FileUtils();
        //
        // fileUtils.client();
        FileUtils.uploadFile("E:\\java\\pufdemo\\test.jpg");
    }




}
