package com.gwh.pufdemo.Service.Impl;

import com.gwh.pufdemo.DAO.UserInfo;
import com.gwh.pufdemo.Repository.UserInfoRepository;
import com.gwh.pufdemo.Service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 10:44
 * @Version 1.0
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository repository;

    @Transactional
    public void register(UserInfo userInfo){
        repository.save(userInfo);
    }

    public Page<UserInfo> findList(Pageable pageable){
        Page<UserInfo> userInfoPage=repository.findAll(pageable);

        return new PageImpl<UserInfo>(userInfoPage.getContent(),pageable,userInfoPage.getTotalElements());
    }

    @Override
    public UserInfo findUserByName(String name) {
        UserInfo userInfo=repository.findByUsername(name);

        return userInfo;
    }

    @Override
    public List<UserInfo> findUserInfoList() {
        return  repository.findAll();

    }


    public Page<UserInfo> findMnageList(Pageable pageable){

        Page<UserInfo> userInfoPage=repository.findAll(pageable);

        return new PageImpl<UserInfo>(findUserInfoList(),pageable,userInfoPage.getTotalElements());
    }
}
