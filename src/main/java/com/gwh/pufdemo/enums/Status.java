package com.gwh.pufdemo.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 20:35
 * @Version 1.0
 */
@Getter
public enum Status implements CodeEnum {
    ENCRYPTED(1, "已被加密的"),
    NOTENCRYPTED(0,"未被加密的"),
    ;

    private Integer code;

    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }}