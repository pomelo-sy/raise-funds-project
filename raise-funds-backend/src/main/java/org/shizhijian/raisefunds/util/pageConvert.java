package org.shizhijian.raisefunds.util;

import java.net.URLDecoder;

public class pageConvert {

    public static void main(String[] args) {
        String str = "https%3A%2F%2Fwww.shuidichou.com%2Fcf%2Fcontribute%2F56709ec7-03dd-4b26-a3f5-d1521e8eadaf%3Fchannel%3Dwx_charity_hy%26source%3DZkMPytFTAyc7w2MrKcF1591121864967%26forwardFrom%3D4%26sharedv%3D0%26userSourceId%3DnqGmez0BXlu0odpXUArmgg%26shareId%3DpierpTtQ52XEKydAiPW1591357925687%26shareIdV2%3DjXEssdTCZnrDw2cx2G81591357925245&response_type=code&scope=snsapi_base&connect_redirect=1";

        System.out.println(URLDecoder.decode(str));

    }
}
