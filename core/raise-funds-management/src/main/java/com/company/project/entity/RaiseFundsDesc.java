package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.vo.req.PageReqVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="t_raise_funds_desc")
public class RaiseFundsDesc extends PageReqVO implements Serializable {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private String content;

    private String title;

    private BigDecimal fundsTarget;

    private Integer sponsorUserId;

    private String openId;

    private String sponsorUserName;

    private Integer receivedUserId;

    private String receivedUserName;

    private Date createDate;

    private Integer fundsCounts;

    private Integer fundsCollected;

    private Integer status;

    private static final long serialVersionUID = 1L;
}