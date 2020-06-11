package org.shizhijian.raisefunds.controller;

import java.util.List;

import org.shizhijian.raisefunds.dto.WeiXinParamDTO;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

public class BaseController {

	@Value("${weixin.APPID}")
	private String APPID;

	public Model getModel(WeiXinParamDTO param, Model model){

		model.addAttribute("nonce", param.getNonce());
		model.addAttribute("signature", param.getSignature());
		model.addAttribute("timestamp", param.getTimestamp());
		model.addAttribute("nonceStr", param.getNonce());
		model.addAttribute("APPID", APPID);

		return model;
	}

}
