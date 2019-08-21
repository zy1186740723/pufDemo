package com.gwh.pufdemo.DAO;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 10:26
 * @Version 1.0
 */
@Data
@Entity
public class UserInfo {

    @Id
    private String userId;

    private String username;

    private String carId;

    private String phoneNumber;


    private Integer status;

    private byte[] dataValue;
}
