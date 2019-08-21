package com.gwh.pufdemo.Service.Impl;

import com.gwh.pufdemo.DAO.ReportInfo;
import com.gwh.pufdemo.Repository.ReportInfoRepository;
import com.gwh.pufdemo.Service.reportInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 22:00
 * @Version 1.0
 */
@Service
public class reportInfoServiceImpl implements reportInfoService {
    @Autowired
    private ReportInfoRepository reportInfoRepository;

    @Transactional
    public ReportInfo saveReport(ReportInfo reportInfo){
        ReportInfo res= reportInfoRepository.save(reportInfo);

        return res;

    }
}
