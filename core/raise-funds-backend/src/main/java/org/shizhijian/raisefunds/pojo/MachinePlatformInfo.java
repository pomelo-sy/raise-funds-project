package org.shizhijian.raisefunds.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("mt_mach_plat_info")
public class MachinePlatformInfo {
	
	@ApiModelProperty(value = "主键id", name="id")
	@TableId(type=IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "机台id", name="platId")
    private String platId;
    
    @ApiModelProperty(value = "机台信息名", name="platDisp")
    private String platDisp;

    @ApiModelProperty(value = "机台号", name="platNo")
    private Integer platNo;

    @ApiModelProperty(value = "机型id", name="typeId")
    private Integer typeId;

    @TableField(exist=false)
    @ApiModelProperty(value = "机型名", name="typeName")
    private String typeName;
    
    @ApiModelProperty(value = "备注", name="memo")
    private String memo;

    @ApiModelProperty(value = "生成时间", name="createTime")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", name="updateTime")
    private Date updateTime;
    

  
}