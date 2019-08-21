package com.gwh.pufdemo.Service.Impl;

import com.gwh.pufdemo.DAO.UserInfo;
import com.gwh.pufdemo.Repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 16:33
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserInfoServiceImplTest {
    @Autowired
    private UserInfoServiceImpl userInfoService;
    @Autowired
    private UserInfoRepository repository;

    @Test
    public void findUserByNameTest() {
        UserInfo userInfo=userInfoService.findUserByName("张言");
        String carId=userInfo.getCarId();
        Assert.assertEquals("123",carId);

    }

    @Test
    public void save(){
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername("小明");
        userInfo.setUserId("3344444444");
        userInfo.setStatus(1);
        userInfo.setCarId("345");
        userInfo.setPhoneNumber("e34rr5");
        repository.save(userInfo);

    }
}