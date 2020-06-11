package org.shizhijian.raisefunds.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.shizhijian.raisefunds.bean.RespApi;
import org.shizhijian.raisefunds.core.WeixinUtil;
import org.shizhijian.raisefunds.dto.ApplyParam;
import org.shizhijian.raisefunds.dto.WeiXinParamDTO;
import org.shizhijian.raisefunds.pojo.MachineTypeInfo;
import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.RaiseFundsDescService;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/raise")
@Slf4j
public class RaiseFundsController extends BaseController{

    @Autowired
    private WeixinUtil weixinUtil;

    @Autowired
    private UserService userService;

    @GetMapping("/apply")
    public String apply(@RequestParam String code, Model model){

        JSONObject tokenAndOpenId = weixinUtil.getWebAccessTokenAndOpenId(code);
        if(tokenAndOpenId != null){
            log.info("token: {}, openId: {}", tokenAndOpenId.getString("access_token"), tokenAndOpenId.getString("openid"));
            //如果已经有用户了 则更新登录时间就可以了
            User user = null;
            QueryWrapper<User> opEqual = new QueryWrapper<>();
            opEqual.eq("open_id", tokenAndOpenId.getString("openid"));
            user = userService.getOne(opEqual);
            if(user != null){
                user.setLastLogin(new Date());
            }else{
                JSONObject obj = weixinUtil.getWeixinUserDto(tokenAndOpenId.getString("openid"));
                if(obj != null){
                    log.info(obj.getString("nickname"));
                    user = User.builder().openId(obj.getString("openid"))
                            .nickName(obj.getString("nickname")).sex(obj.getInteger("sex"))
                            .city(obj.getString("city")).province(obj.getString("province"))
                            .country(obj.getString("country")).headImgUrl(obj.getString("headimgurl"))
                            .lastLogin(new Date()).build();
                    userService.saveOrUpdate(user);
                }
            }
            model.addAttribute("user", user);
        }
        return "apply";
    }

    @PostMapping("/submitApply")
    @ResponseBody
    public RespApi<Boolean> submitApply(@RequestBody ApplyParam applyParam){

        //TODO 保存申请人信息
        //TODO 保存申请单的信息
        return RespApi.OK;
//        raiseFundsDescService.save(RaiseFundsDesc.builder().content(applyParam.getContent()).sponsorUserName(applyParam.getName()))

    }

    @GetMapping("/myRaiseFunds")
    public String myRaiseFunds(@RequestParam String openid, Model model){

        log.info("openid: {}", openid);
        return "myRaiseFunds";
    }
}
