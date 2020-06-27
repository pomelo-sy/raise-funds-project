package com.raiseFunds;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shizhijian.raisefunds.RaiseFundsApplication;
import org.shizhijian.raisefunds.controller.RaiseFundsController;
import org.shizhijian.raisefunds.core.WeixinUtil;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Date;

@RunWith(value= SpringRunner.class)
@SpringBootTest(classes = {RaiseFundsApplication.class})
@Slf4j
public class TestComponents {

    @Autowired
    private WeixinUtil weixinUtil;

    @Autowired
    private RaiseFundsController raiseFundsController;

    @Autowired
    private UserService userService;

    @Test
    public void TestGetToken(){
        System.out.println(weixinUtil.getToken("1"));
    }


    @Test
    public void TestRaise(){
//        raiseFundsController.apply("011AQP5q0LhmRj1PCM5q0kkL5q0AQP5k", null,null);
    }


    @Test
    public void refreshToken(){
        weixinUtil.refreshToken();
    }

    @Test
    public void getAppid(){
        System.out.println(weixinUtil.getAppId());
    }

    @Test
    public void getopenIdByCode(){
        weixinUtil.getWebAccessTokenAndOpenId("071mNh6i2odCDE0ENz8i2AYh6i2mNh6R");
    }

    @Test
    public void testInserUser(){

        ResponseEntity<JSONObject> withJson = weixinUtil.getWithJson(URI.create("https://api.weixin.qq.com/cgi-bin/user/info?access_token=34_ij7fIssE-LZSqg0AlllDX71p_aRrb3_Mrg9pavwdOqwi-Y9RN1if_v3vmGpx4zLvvU5fqrI2rj1OCDPqS5vHoEzzuR5UL5xybbATuNr6Ldbw67keOVupL8URzedvRasXCscyMeDw9SvRNyeTCJLjAEAQXZ&openid=oCLaowcmAeHb3-wOJ9ZkoJwZdn6Y&lang=zh_CN"));
        JSONObject body = withJson.getBody();
        log.info(body.toJSONString());
        String nickName = body.getString("nickname");
        log.info("nickName: {}", nickName);

        boolean flag = userService.save(User.builder().nickName(nickName).lastLogin(new Date()).build());
        log.info(String.valueOf(flag));


    }

    @Test
    public void printUrl(){
        String url ="https%3A%2F%2Fwww.shuidichou.com%2Fcf%2Fcontribute%2F56709ec7-03dd-4b26-a3f5-d1521e8eadaf%3FuserSourceId%3DnqGmez0BXlu0odpXUArmgg%26sharedv%3D0%26shareId%3D1FDA92D2-896E-43C7-B829-EAD3F30CCFBB";
        System.out.println(URLDecoder.decode(url));
    }

    @Test
    public void testUserName(){
        log.info(userService.getById(18).getNickName());
    }
}
