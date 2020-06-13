package org.shizhijian.raisefunds.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String content;

    private String name;

    private String telephone;

    private String targetFunds;

    private String openId;
}
