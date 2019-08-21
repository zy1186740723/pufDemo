package com.gwh.pufdemo.Service.PUFService;

import com.gwh.pufdemo.DAO.UserInfo;
import com.gwh.pufdemo.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**y用于调用rest接口，对于系统来说是提供了专用压缩或者解压缩的服务
 * @Author: zhangyan
 * @Date: 2019/8/19 11:43
 * @Version 1.0
 */
@Service
@Slf4j
public class RestAPIUtil {

    /**
     * http://192.168.8.188:7777/api2/compress/special/<card_id>  专用压缩
     * http://192.168.8.188:7777/api2/depress/special/<encrypted_card_id>  专用解压
     */

    @Autowired
    private  RestTemplate restTemplate;

    //private static final RestTemplate restTemplate=new RestTemplate();
    //http://192.168.8.188:7777/api2/compress/special/<card_id>
    //压缩接口路由
    private final String GET_URL_compress = "http://192.168.8.188:7777/api2/compress/common/{carId}";
    //解压缩路由
    private final String GET_URL_depress= "http://192.168.8.188:7777/api2/depress/common/{carId}";



    //private static String GET_URL = "http://192.168.8.188:7777/api2/compress/special/<card_id>";
    private static String POST_URL = "http://localhost:8080/testPost";
    private static String POST_PARAM_URL = "http://localhost:8080/testPostParam";
    private static String PUT_URL = "http://localhost:8080/testPut";
    private static String DEL_URL = "http://localhost:8080/testDel";

    /**
     * 调用服务器的PUFs专用无损压缩服务
     * @param userPlainText 用户对象
     * @return 返回被加密的身份证信息
     */
    public  String pufCompressService(UserInfo userPlainText){
        //1、从userInfo中提取需要加密的字段
        //RestTemplate restTemplate=new RestTemplate();
        userPlainText.setStatus(1);
        String carId=userPlainText.getCarId();
        String phome_number=userPlainText.getPhoneNumber();
        System.out.println(carId);
        //2、输入的是明文调用get服务，返回密文的json格式
        //ResponseEntity<UserInfo> responseEntity=restTemplate.getForEntity(GET_URL,UserInfo.class,carId);
        String res=restTemplate.getForObject(GET_URL_compress,String.class,carId);
        System.out.println(res);
        //System.out.println(responseEntity.toString());
        //3、通过json解析，转化为密文的userInfo的形式
        System.out.println(res);
        return res;
    }

    /**
     * 调用服务器的解压缩服务，
     * @param userCliperText 用户对象
     * @return 返回被解密的身份证信息
     */
    public  String pufDepress(UserInfo userCliperText) throws Exception{
        //1、从密文的数据中
        String carIdCliperText=userCliperText.getCarId();
        System.out.println(carIdCliperText);

        String res=restTemplate.getForObject(GET_URL_depress,String.class,carIdCliperText);
//        InputStream in=restTemplate.getForObject(GET_URL_depress,InputStream.class,carIdCliperText);
//
//        System.out.println("接收到返回值");
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"utf8"));
//        String line = null;
//        while((line = bufferedReader.readLine()) != null)
//        {
//
//            stringBuilder.append(System.getProperty("line.separator"));
//
//            stringBuilder.append(line);
//        }
//        String res=stringBuilder.toString();

        //res=new String(a,Charset.forName("UTF-8"));

        System.out.println(res);
        return res;
    }

    public static void main(String[] args) {
        RestAPIUtil restAPIUtil=new RestAPIUtil();
        UserInfo userInfo=new UserInfo();
        userInfo.setCarId("1234");
        userInfo.setPhoneNumber("123456");
        userInfo.setUsername("zhangyan");
        userInfo.setUserId("167");
        restAPIUtil.pufCompressService(userInfo);







    }


}
