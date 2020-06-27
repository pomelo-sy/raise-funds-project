package org.shizhijian.raisefunds.core;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.shizhijian.raisefunds.dao.TokenMapper;
import org.shizhijian.raisefunds.dto.JsApiDto;
import org.shizhijian.raisefunds.pojo.Token;
import org.shizhijian.raisefunds.pojo.User;
import org.shizhijian.raisefunds.service.TokenService;
import org.shizhijian.raisefunds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component("weixinUtil")
@Slf4j
@Getter
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
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    public String getUrlWithPara(ServletRequest request) {
        HttpServletRequest r = (HttpServletRequest) request;
        String url = r.getRequestURL().toString();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        if(parameterMap != null&& parameterMap.entrySet().size() > 0){
//            url+="?";
//        }
//        StringBuffer paras = new StringBuffer("");
//        parameterMap.entrySet().forEach(t->{
//            paras.append(t.getKey()).append("=").append(t.getValue()[0]).append("&");
//        });
//        url+=paras.substring(0, paras.length()-1);;
        String path = r.getQueryString();
        if(StringUtils.isNotBlank(path)){
            url = url + "?" + path;
        }
        log.info("urlWithPara: {}", url);
        return url;
    }

    public JsApiDto getJsApiDto(ServletRequest request) {

        String url = getUrlWithPara(request);
        log.info("=================url-decoded: {}", url);
        String nonce = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        String signature = getSignature(nonce, getToken(WeixinUtil.TokenTypeEnum.jsApiToken.getValue()), timestamp, url);
        return JsApiDto.builder().urlWithPara(url).nonce(nonce).timestamp(timestamp).signature(signature).appId(appId).build();

    }

    public static void main(String[] args) {
        String s = "%3D";
        try {
            s = URLDecoder.decode(s, "UTF-8");
            System.out.println(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public enum TokenTypeEnum {
        accessToken("1"), jsApiToken("2");
        private String value;
        TokenTypeEnum(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }

    public String getToken(String tokenType){

        Token accessToken= tokenService.getById(Integer.valueOf(tokenType));
        return accessToken.getToken();
    }

    public String getSignature(String noncestr, String ticket, String timestamp, String url) {
        String str = "jsapi_ticket={jsapi_ticket}&noncestr={noncestr}&timestamp={timestamp}&url={url}"
                .replace("{jsapi_ticket}", ticket).replace("{noncestr}", noncestr)
                .replace("{timestamp}", timestamp).replace("{url}", url);
        log.info("string1: {}", str);
        return sha1(str);
    }

    public String sha1(String str) {

        MessageDigest messageDigest = null; // 此处的sha代表sha1
        try {
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {}
        byte[] cipherBytes = messageDigest.digest(str.getBytes());
        String cipherStr = Hex.encodeHexString(cipherBytes);
        return cipherStr;
    }


    public User getUserByCode(String code){
        JSONObject tokenAndOpenId = getWebAccessTokenAndOpenId(code);
        User user = null;
        if(tokenAndOpenId != null) {
            log.info("token: {}, openId: {}", tokenAndOpenId.getString("access_token"), tokenAndOpenId.getString("openid"));
            //如果已经有用户了 则更新登录时间就可以了
            user = userService.getUserByOpenId(tokenAndOpenId.getString("openid"));
            if (user != null) {
                user.setLastLogin(new Date());
            } else {
                JSONObject obj = getWeixinUserDto(tokenAndOpenId.getString("openid"));
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

    public JSONObject getWebAccessTokenAndOpenId(String code){

        log.info("getWebAccessTokenAndOpenId: code: {}, appId: {}, secretkey: {}", code, appId, appSecret);
        URI url = URI.create(getWebAccessToken.replace("{APPID}", appId).replace("{appSecret}", appSecret).replace("{code}", code));
        log.info("getWebAccessTokenAndOpenId: url: {}", url.toString());
        ResponseEntity<JSONObject> response = getWithJson(url);
        if(response.getStatusCode() == HttpStatus.OK){
            log.debug("response Body: {}", response.getBody().toJSONString());
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
            tokenService.updateById(Token.builder().token(accessToken).tokenType("1").id(1).appId("wx_raiseFunds").updateTime(new Date()).build());
        }
        String jpApiTicket = getJsApiTicketScheduled(accessToken);
        if(StringUtils.isNotBlank(jpApiTicket)){
            tokenService.updateById(Token.builder().token(jpApiTicket).tokenType("2").id(2).appId("wx_raiseFunds").updateTime(new Date()).build());
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

    public ResponseEntity<JSONObject> getWithJson(URI url){
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
