package com.gwh.pufdemo.Service.Impl;

import com.gwh.pufdemo.DAO.domain.ActionType;
import com.gwh.pufdemo.DAO.ReportInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 22:06
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class reportInfoServiceImplTest {
    @Autowired
    private reportInfoServiceImpl reportInfoService;

    @Test
    public void saveReport() {
        ReportInfo reportInfo=new ReportInfo();
        reportInfo.setActionType(ActionType.UPDATE);
        reportInfo.setLocation("无锡");
        reportInfo.setOperateTime(new Date());
        reportInfo.setUsername("张言");

        Assert.assertEquals("张言",reportInfoService.saveReport(reportInfo).getUsername());

    }
}