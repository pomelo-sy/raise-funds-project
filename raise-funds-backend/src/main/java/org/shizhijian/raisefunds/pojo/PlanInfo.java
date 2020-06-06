package org.shizhijian.raisefunds.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName(value="pd_production_plan_info")
public class PlanInfo implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", name="id")
    @TableId(type=IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "计划时间", name="planDate")
    private Date planDate;

    @ApiModelProperty(value = "平台id", name="platId")
    private String platId;

    @ApiModelProperty(value = "类型id", name="typeId")
    private String typeId;

    @ApiModelProperty(value = "产品id", name="prodId")
    private String prodId;

    @ApiModelProperty(value = "任务id", name="taskId")
    private String taskId;

    @ApiModelProperty(value = "本日计划量", name="countToday")
    private Integer countToday;

    @ApiModelProperty(value = "是否换模", name="changeFlg")
    private String changeFlg;

    @ApiModelProperty(value = "材料id", name="materialId")
    private String materialId;
    
    @ApiModelProperty(value = "换模损耗时间", name="changeLossTime")
    private Integer changeLossTime;

    @ApiModelProperty(value = "材料损耗时间", name="waitLossTime")
    private Integer waitLossTime;

    @ApiModelProperty(value = "维修损耗时间", name="repairLossTime")
    private Integer repairLossTime;
    
    @ApiModelProperty(value = "其他损耗时间", name="otherLossTime")
    private Integer otherLossTime;
    
    @TableField(exist=false)
    @ApiModelProperty(value = "总损耗时间", name="otherLossTime")
    private Integer allLossTime;

    @ApiModelProperty(value = "备注", name="memo")
    private String memo;

    @ApiModelProperty(value = "更新时间", name="updateTime")
    private LocalDateTime updateTime;

}