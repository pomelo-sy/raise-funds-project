package org.shizhijian.raisefunds.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.shizhijian.raisefunds.Enum.raiseStatus;
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
import java.util.List;

@Controller
@RequestMapping("/raise")
@Slf4j
public class RaiseFundsController extends BaseController{

    @Autowired
    private WeixinUtil weixinUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RaiseFundsDescService raiseFundsDescService;

    @GetMapping("/apply")
    public String apply(String code, String openId, Model model){

        log.info("openId: {}", openId);
        JSONObject tokenAndOpenId = weixinUtil.getWebAccessTokenAndOpenId(code);
        if(tokenAndOpenId != null){
            log.info("token: {}, openId: {}", tokenAndOpenId.getString("access_token"), tokenAndOpenId.getString("openid"));
            //如果已经有用户了 则更新登录时间就可以了
            User user = userService.getUserByOpenId(tokenAndOpenId.getString("openid"));
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

        raiseFundsDescService.save(RaiseFundsDesc.builder().sponsorUserName(applyParam.getName())
                .content(applyParam.getContent()).createDate(new Date())
                .fundsTarget(applyParam.getTargetFunds()).sponsorUserId(applyParam.getUserId())
                .status(raiseStatus.init.getStatus()).title(applyParam.getTitle())
                .build());
        return RespApi.OK;
    }

    @GetMapping("/myRaiseFunds")
    public String myRaiseFunds(String code, String userId, Model model){

        if(StringUtils.isNotEmpty(userId)){

            List<RaiseFundsDesc> raises = raiseFundsDescService.findByUserId(Integer.valueOf(userId));
            model.addAttribute("raises", raises);
        }else{
            User user = createUser(code);
            List<RaiseFundsDesc> raises = raiseFundsDescService.findByUserId(user.getId());
            model.addAttribute("raises", raises);
        }
        return "myRaiseFunds";
    }


    private User createUser(String code){
        JSONObject tokenAndOpenId = weixinUtil.getWebAccessTokenAndOpenId(code);
        User user = null;
        if(tokenAndOpenId != null) {
            log.info("token: {}, openId: {}", tokenAndOpenId.getString("access_token"), tokenAndOpenId.getString("openid"));
            //如果已经有用户了 则更新登录时间就可以了
            user = userService.getUserByOpenId(tokenAndOpenId.getString("openid"));
            if (user != null) {
                user.setLastLogin(new Date());
            } else {
                JSONObject obj = weixinUtil.getWeixinUserDto(tokenAndOpenId.getString("openid"));
                if (obj != null) {
                    log.info(obj.getString("nickname"));
                    user = User.builder().openId(obj.getString("openid"))
                            .nickName(obj.getString("nickname")).sex(obj.getInteger("sex"))
                            .city(obj.getString("city")).province(obj.getString("province"))
                            .country(obj.getString("country")).headImgUrl(obj.getString("headimgurl"))
                            .lastLogin(new Date()).build();
                    userService.saveOrUpdate(user);
                }
            }
        }
        return user;
    }
}
