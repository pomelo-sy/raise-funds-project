package org.shizhijian.raisefunds.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.shizhijian.raisefunds.Enum.raiseStatus;
import org.shizhijian.raisefunds.bean.RespApi;
import org.shizhijian.raisefunds.core.WeixinUtil;
import org.shizhijian.raisefunds.dto.ApplyParam;
import org.shizhijian.raisefunds.pojo.RaiseFundsDesc;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.RaiseFundsDescService;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    private HttpServletRequest request;

    @Autowired
    private RaiseFundsDescService raiseFundsDescService;

    @GetMapping("/apply")
    public String apply(String code, String openId, Model model){

        log.info("openId: {}", openId);
        log.info("code: {}", code);
        User user = null;
        //如果是从菜单点过来的没有openid，内部流转带openId
        if(StringUtils.isEmpty(openId)){
            JSONObject tokenAndOpenId = weixinUtil.getWebAccessTokenAndOpenId(code);
            if(tokenAndOpenId != null){
                log.info("token: {}, openId: {}", tokenAndOpenId.getString("access_token"), tokenAndOpenId.getString("openid"));
                //如果已经有用户了 则更新登录时间就可以了
                user = userService.getUserByOpenId(tokenAndOpenId.getString("openid"));
                if(user != null){
                    user.setLastLogin(new Date());
                    userService.saveOrUpdate(user);
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
            }
        }else{
            user = userService.getUserByOpenId(openId);
        }
        model.addAttribute("user", user);
        return "apply";
    }


    @PostMapping("/submitApply")
    @ResponseBody
    public RespApi<Boolean> submitApply(@RequestBody ApplyParam applyParam){

        log.info("submitParam: {}", applyParam);
        User sponsorUser = userService.getUserByOpenId(applyParam.getOpenId());
        if(sponsorUser != null){
            boolean save = raiseFundsDescService.save(RaiseFundsDesc.builder().sponsorUserName(applyParam.getName())
                    .openId(applyParam.getOpenId()).status(raiseStatus.init.getStatus())
                    .content(applyParam.getContent()).title(applyParam.getTitle())
                    .fundsTarget(new BigDecimal(applyParam.getTargetFunds())).createDate(new Date())
                    .build());
            if(save){
                return RespApi.OK;
            }else{
                return RespApi.FAIL;
            }

        }
        return RespApi.FAIL;
    }

    @GetMapping("/myRaiseFunds")
    public String myRaiseFunds(String code, String openId, Model model){

        User user = null;
        List<RaiseFundsDesc> raises = null;
        if(StringUtils.isEmpty(openId)){
            if(StringUtils.isEmpty(code)){
                throw new IllegalArgumentException("args error");
            }else{
                user = weixinUtil.getUserByCode(code);
                if(user == null){
                    throw new IllegalArgumentException("no userFounded code: "+ code);
                }else{
                    raises = raiseFundsDescService.findByOpenId(user.getOpenId());
                }
            }
        }else{
            raises = raiseFundsDescService.findByOpenId(openId);
        }

        String redirectServer = request.getScheme() +"://" + request.getServerName()
                /*+ ":" +request.getServerPort()*/+request.getServletPath();
        log.info(redirectServer);
        log.info(request.getContextPath());
        log.info(request.getServletPath());
        log.info(request.getRequestURI());
        log.info(request.getRequestURL().toString());
        model.addAttribute("raises", raises);
        model.addAttribute("appid", weixinUtil.getAppId());
        model.addAttribute("redirectServer", redirectServer);
        return "myRaiseFunds";
    }



}
