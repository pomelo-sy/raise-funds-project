package org.shizhijian.raisefunds.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeiXinParamDTO {

    private String signature;

    private String timestamp;

    private String nonce;

    private String openid;
}
