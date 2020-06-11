package org.shizhijian.raisefunds.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class pageConvert {

    public static void main(String[] args) {
        String str = "https%3A%2F%2Fwww.shuidichou.com%2Fcf%2Fcontribute%2F56709ec7-03dd-4b26-a3f5-d1521e8eadaf%3Fchannel%3Dwx_charity_hy%26source%3DZkMPytFTAyc7w2MrKcF1591121864967%26forwardFrom%3D4%26sharedv%3D0%26userSourceId%3DnqGmez0BXlu0odpXUArmgg%26shareId%3DpierpTtQ52XEKydAiPW1591357925687%26shareIdV2%3DjXEssdTCZnrDw2cx2G81591357925245&response_type=code&scope=snsapi_base&connect_redirect=1";

        System.out.println(URLDecoder.decode(str));

        String str1 = "http://39.98.152.84/raiseFunds/raise/apply?nsukey=2JKCODDrrqgt8frN6pKbGTi3n61qnsJKW7j6NXVmmNiWW8%2FBir7d%2BVvAuWqrVFwjlz6W235ItFe1XIVUOwlsLS0kd88Iewji3TuZboxMKLUZ9jcowprnBwGctFm81bzJbxCE%2FZUM0RlwNCdbH1TqjlSXkU3evncllDjnV7iUTjwV75qHg68sNJxx2nyWrE3fAEYoLCCAnc3Xi%2FxmmMpIEA%3D%3D";

        System.out.println(URLDecoder.decode(str1));
        System.out.println(URLDecoder.decode("2JKCODDrrqgt8frN6pKbGTi3n61qnsJKW7j6NXVmmNiWW8/Bir7d+VvAuWqrVFwjlz6W235ItFe1XIVUOwlsLS0kd88Iewji3TuZboxMKLUZ9jcowprnBwGctFm81bzJbxCE/ZUM0RlwNCdbH1TqjlSXkU3evncllDjnV7iUTjwV75qHg68sNJxx2nyWrE3fAEYoLCCAnc3Xi/xmmMpIEA=="));

        String str2 = "http://39.98.152.84//raiseFunds/raiseFundsDesc/inventory?id=1";
        System.out.println("str2:" + URLEncoder.encode(str2));


        String str3 = "http://39.98.152.84/raiseFunds/raise/apply";
        System.out.println(URLEncoder.encode(str3));

    }
}
