package com.gwh.pufdemo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: zhangyan
 * @Date: 2019/8/21 0:09
 * @Version 1.0
 */
@RestController
public class RestTemplateTest {
    private static String GET_URL_compress = "http://localhost:8081/luckymoney/puf/{carId}";
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping("/test")
    public void runTest(){
        String carId="hahah";
        String res=restTemplate.getForObject(GET_URL_compress,String.class,carId);
        System.out.println(res);
    }
}
