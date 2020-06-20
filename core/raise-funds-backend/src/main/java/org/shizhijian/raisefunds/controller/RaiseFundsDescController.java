package org.shizhijian.raisefunds.controller;

import org.apache.commons.lang3.StringUtils;
import org.shizhijian.raisefunds.core.WeixinUtil;
import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.RaiseFundsDescService;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/raiseFundsDesc")
public class RaiseFundsDescController {

    @Autowired
    private RaiseFundsDescService raiseFundsDescService;

    @Autowired
    private UserService userService;

    @Autowired
    private WeixinUtil weixinUtil;

    @GetMapping("/inventory")
    public String inventory(String code, @RequestParam Integer raiseId, Model model){

        User currentUser = null;
        User sponsorUser = null;
        if(StringUtils.isNotEmpty(code)){
            currentUser = weixinUtil.getUserByCode(code);
        }
        RaiseFundsDesc desc = null;
        if (raiseId != null){
            desc = raiseFundsDescService.getById(raiseId);
            sponsorUser = userService.getUserByOpenId(desc.getOpenId());
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("sponsorUser", sponsorUser);
        model.addAttribute("desc", desc);
        return "main";
    }

}
