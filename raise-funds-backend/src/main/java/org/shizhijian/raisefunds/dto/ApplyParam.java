package org.shizhijian.raisefunds.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyParam {

    private String name;

    private String telephone;

    private String content;
}
