package com.epeins.factory.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="mt_prod_info")
public class ProductInfo implements Serializable {
	
	
	@ApiModelProperty(value = "主键id 自增", name="id")
	@TableId(type=IdType.AUTO)
    private Integer id;

	@ApiModelProperty(value = "产品名称", name="prodName")
    private String prodName;

	@ApiModelProperty(value = "产品备注", name="memo")
    private String memo;

	@ApiModelProperty(value = "创建时间", name="createTime")
    private Date createTime;

	@ApiModelProperty(value = "更新时间", name="updateTime")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}