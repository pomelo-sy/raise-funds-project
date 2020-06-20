package org.shizhijian.raisefunds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pay")
public class PayController {

    @GetMapping("/index")
    public String index(String code, String openId, Model model){

//        model.addAttribute("","");


        return "pay";
    }
}
