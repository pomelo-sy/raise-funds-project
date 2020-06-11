package com.raiseFunds;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shizhijian.raisefunds.RaiseFundsApplication;
import org.shizhijian.raisefunds.controller.RaiseFundsController;
import org.shizhijian.raisefunds.core.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

@RunWith(value= SpringRunner.class)
@SpringBootTest(classes = {RaiseFundsApplication.class})
public class TestComponents {

    @Autowired
    private WeixinUtil weixinUtil;

    @Autowired
    private RaiseFundsController raiseFundsController;

    @Test
    public void TestGetToken(){
        weixinUtil.getToken();
    }


    @Test
    public void TestRasie(){
        raiseFundsController.apply("011AQP5q0LhmRj1PCM5q0kkL5q0AQP5k", null);
    }


    @Test
    public void refreshToken(){
        weixinUtil.refreshToken();
    }

}
