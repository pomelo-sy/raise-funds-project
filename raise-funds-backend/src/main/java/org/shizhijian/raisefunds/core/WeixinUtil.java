package org.shizhijian.raisefunds.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.shizhijian.raisefunds.dao.TokenMapper;
import org.shizhijian.raisefunds.pojo.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("weixinUtil")
public class WeixinUtil {

    @Value("${weixin.APPID}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;

    @Value("${weixin.URL.getToken}")
    private String getTokenUrl;

    @Value("${weixin.URL.getUserInfo}")
    private String getUserInfoUrl;

    @Value("${weixin.URL.getWebAccessToken}")
    private String getWebAccessToken;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenMapper tokenMapper;


    public String getToken(){
        AccessToken accessToken = tokenMapper.selectById(1);
        return accessToken.getAccessToken();
    }

    public JSONObject getWebAccessTokenAndOpenId(String code){

        URI url = URI.create(getWebAccessToken.replace("{APPID}", appId).replace("{appSecret}", appSecret).replace("{code}", code));
        ResponseEntity<JSONObject> response = getWithJson(url);
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        return null;
    }

    public JSONObject getWeixinUserDto(String openId){

        URI url = URI.create(getUserInfoUrl.replace("{access_token}", getToken()).replace("{openId}", openId));
        ResponseEntity<JSONObject> response = getWithJson(url);
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        return null;
    }

    @Scheduled(cron = " 0 0/50 * * * ?")
    public void refreshToken(){

        String accessToken = getTokenScheduled();
        if(StringUtils.isNotBlank(accessToken)){
            tokenMapper.updateById(AccessToken.builder().accessToken(accessToken).id(1).appId("wx_raiseFunds").updateTime(new Date()).build());
        }
    }

    private String getTokenScheduled(){

        URI url = URI.create(getTokenUrl.replace("{APPID}", appId).replace("{appSecret}", appSecret));
        ResponseEntity<JSONObject> response = getWithJson(url);
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody().get("access_token").toString();
        }
        return "";
    }

    private ResponseEntity<JSONObject> getWithJson(URI url){
        HttpHeaders header = new HttpHeaders();
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        header.setAccept(mediaTypes);
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(header);
        ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        return response;
    }


}
