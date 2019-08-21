package com.gwh.pufdemo.Repository;

import com.gwh.pufdemo.DAO.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 10:40
 * @Version 1.0
 */
public interface UserInfoRepository extends JpaRepository<UserInfo,String> {
   UserInfo findByUsername(String name);
}
