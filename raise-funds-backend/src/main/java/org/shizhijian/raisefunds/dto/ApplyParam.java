package org.shizhijian.raisefunds.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyParam {

    private String name;

    private Integer userId;

    private String telephone;

    private String title;

    private String content;

    private Integer targetFunds;
}
