package org.shizhijian.raisefunds.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsApiDto {

    private String urlWithPara;

    private String nonce;

    private String timestamp;

    private String signature;

    private String appId;
}
