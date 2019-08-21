package com.gwh.pufdemo.Service;

import com.gwh.pufdemo.DAO.UserInfo;

import java.util.List;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 10:42
 * @Version 1.0
 */
public interface UserInfoService {
    UserInfo findUserByName(String name);

    List<UserInfo> findUserInfoList();

}
