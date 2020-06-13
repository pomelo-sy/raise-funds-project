package org.shizhijian.raisefunds.controller;

import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @RequestMapping("/addUser")
    public void addUser(@RequestBody User user){
        userService.saveOrUpdate(user);
    }
}
