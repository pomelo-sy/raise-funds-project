package org.shizhijian.raisefunds.core;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.shizhijian.raisefunds.dao.TokenMapper;
import org.shizhijian.raisefunds.pojo.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Value("${weixin.URL.getJsApiTicket}")
    private String getJsApiTicket;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenMapper tokenMapper;

    public enum TokenTypeEnum {
        accessToken("1"), jsApiToken("2");
        private String value;
        TokenTypeEnum(String value){
            this.value = value;
        }
        String getValue(){
            return this.value;
        }
    }

    public String getToken(String tokenType){

        Token accessToken= tokenMapper.selectById(Integer.valueOf(tokenType));
        return accessToken.getToken();
    }

    public String getSignature(String noncestr, String ticket, String timestamp, String url) throws NoSuchAlgorithmException {
        String str = "jsapi_ticket={jsapi_ticket}&noncestr={noncestr}&timestamp={timestamp}&url={url}"
                .replace("{jsapi_ticket}", ticket).replace("{noncestr}", noncestr)
                .replace("{timestamp}", timestamp).replace("{url}", url);
        return sha1(str);
    }

    public String sha1(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA"); // 此处的sha代表sha1
        byte[] cipherBytes = messageDigest.digest(str.getBytes());
        String cipherStr = Hex.encodeHexString(cipherBytes);
        return cipherStr;
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

        URI url = URI.create(getUserInfoUrl.replace("{access_token}", getToken(TokenTypeEnum.accessToken.getValue())).replace("{openId}", openId));
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
            tokenMapper.updateById(Token.builder().token(accessToken).tokenType("1").id(1).appId("wx_raiseFunds").updateTime(new Date()).build());
        }
        String jpApiTicket = getJsApiTicketScheduled(accessToken);
        if(StringUtils.isNotBlank(jpApiTicket)){
            tokenMapper.updateById(Token.builder().token(jpApiTicket).tokenType("2").id(2).appId("wx_raiseFunds").updateTime(new Date()).build());
        }
    }

    private String getJsApiTicketScheduled(String token){
        URI url = URI.create(getJsApiTicket.replace("{access_token}", token));
        ResponseEntity<JSONObject> response = getWithJson(url);
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody().get("ticket").toString();
        }
        return "";
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
