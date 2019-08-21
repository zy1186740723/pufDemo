package com.gwh.pufdemo.DAO;

import com.gwh.pufdemo.DAO.domain.ActionType;
import lombok.Data;

import java.util.Date;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 21:51
 * @Version 1.0
 */
@Data
public class ReportInfo {

    private String username;

    private String location;

    private Date operateTime;

    private ActionType actionType;
}
