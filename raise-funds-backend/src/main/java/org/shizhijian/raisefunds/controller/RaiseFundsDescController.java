package org.shizhijian.raisefunds.controller;

import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;
import org.shizhijian.raisefunds.service.RaiseFundsDescService;
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

    @GetMapping("/inventory")
    public String desc(@RequestParam String id, Model model){

        RaiseFundsDesc desc = raiseFundsDescService.getById(id);
        model.addAttribute("desc", desc);
        return "main";
    }

}
