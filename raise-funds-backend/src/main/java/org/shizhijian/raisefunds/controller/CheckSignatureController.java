package org.shizhijian.raisefunds.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/checkSig")
@Slf4j
public class CheckSignatureController {

    /*参数	描述
    signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
    timestamp	时间戳
    nonce	随机数
    echostr*/
    private static final String token = "rasie_funds";

    @GetMapping("/check")
    public String checkSig(@RequestParam String signature, @RequestParam String timestamp,
                         @RequestParam String nonce, @RequestParam String echostr){

        log.info("get message: {},{},{},{}" , signature, timestamp, nonce, echostr);
        List<String> list = new ArrayList<>();
        list.add(nonce);
        list.add(token);
        list.add(timestamp);
        list.sort((msg1, msg2) ->{
            return msg1.compareTo(msg2);
        });
        list.forEach((l)->{
            System.out.println(l);
        });



        return echostr;
    }

    public static void main(String[] args) {
        CheckSignatureController c = new CheckSignatureController();
        c.checkSig("dslkfjadk","yss","sonce","echostr");
    }
}
