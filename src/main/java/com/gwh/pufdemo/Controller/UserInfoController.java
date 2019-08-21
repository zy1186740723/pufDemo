package com.gwh.pufdemo.Controller;

import com.gwh.pufdemo.DAO.UserInfo;
import com.gwh.pufdemo.Service.Impl.UserInfoServiceImpl;
import com.gwh.pufdemo.Service.PUFService.PufStringCompress;
import com.gwh.pufdemo.Service.PUFService.PufStringDepress;
import com.gwh.pufdemo.Service.PUFService.RestAPIUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhangyan
 * @Date: 2019/8/19 10:48
 * @Version 1.0
 */
@Controller
@Slf4j
public class UserInfoController {
    @Autowired
    private RestAPIUtil restAPIUtil;

    private static final RestTemplate restTemplate=new RestTemplate();

    private static String GET_URL = "http://localhost:8081/luckymoney/puf/{carId}";
    private static String POST_URL = "http://localhost:8080/testPost";
    private static String POST_PARAM_URL = "http://localhost:8080/testPostParam";
    private static String PUT_URL = "http://localhost:8080/testPut";
    private static String DEL_URL = "http://localhost:8080/testDel";
    @Autowired
    private UserInfoServiceImpl userInfoService;
    @Autowired
    private PufStringCompress pufStringCompress;
    @Autowired
    private PufStringDepress pufStringDepress;


    /**
     * 注册页面
     * @param model
     * @return
     */
    @RequestMapping("/register")
    public String userRegister(Model model) {
        model.addAttribute("form", new UserInfo());
        return "register";
    }

    @RequestMapping(value = "/user/registerForm",method = RequestMethod.POST)
    public ModelAndView runReister(@ModelAttribute UserInfo userInfo,
                                   Map<String,Object> map) throws Exception{

        //TODO:进行Rest请求，实现对userInfo中特定字段的加密
        //String res=restAPIUtil.pufCompressService(userInfo);
        byte[] res=pufStringCompress.pufStringComprress(userInfo);
        userInfo.setDataValue(res);
        //userInfo.setCarId(res);
        //数据库记录信息
        userInfo.setCarId("*******");
        userInfoService.register(userInfo);
        map.put("userInfo",userInfo);
        return new ModelAndView("user/result",map);

    }

    /**
     * 查看存储的所有记录
     * @param page
     * @param size
     * @param map
     * @return
     */
    @RequestMapping("/findAll")
    public ModelAndView findAll(@RequestParam(value = "page",defaultValue ="2") Integer page,
                                @RequestParam(value = "size",defaultValue = "10") Integer size,
                                Map<String,Object> map){
        PageRequest request=new PageRequest(page-1,size);
        Page<UserInfo> userInfoPage=userInfoService.findList(request);
        map.put("userInfoPage",userInfoPage);
        map.put("currentPage",page);
        map.put("size",size);
        return new ModelAndView("user/userInfo",map);
    }


    /**
     *管理员审核举报信息，需要将密文转为为明文以后查看
     * @param page
     * @param size
     * @param map
     * @return
     */
    @RequestMapping("/infoManage")
    public ModelAndView runManage(@RequestParam(value = "page",defaultValue ="2") Integer page,
                                  @RequestParam(value = "size",defaultValue = "10") Integer size,
                                  Map<String,Object> map) throws Exception{
        PageRequest request=new PageRequest(page-1,size);

        List<UserInfo> userInfoList= userInfoService.findUserInfoList();
        System.out.println("寻找用户");

        //对密文进行解密
        for (UserInfo userInfo : userInfoList) {
            System.out.println("执行解密");
            if (userInfo.getStatus()==1){
                String carId=pufStringDepress.pufStringDepress(userInfo);
                System.out.println(carId);
                userInfo.setCarId(carId);
            }

        }

        System.out.println("返回视图");
        map.put("userInfoList",userInfoList);
        map.put("currentPage",page);
        map.put("size",size);
        return new ModelAndView("user/management",map);
    }




}
