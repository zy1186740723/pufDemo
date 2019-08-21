package com.gwh.pufdemo.Repository;

import com.gwh.pufdemo.DAO.ReportInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 21:56
 * @Version 1.0
 */

public interface ReportInfoRepository extends MongoRepository<ReportInfo,String>{
}
