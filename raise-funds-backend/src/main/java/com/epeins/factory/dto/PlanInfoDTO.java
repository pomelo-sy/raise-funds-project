package com.epeins.factory.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.epeins.factory.pojo.PlanInfo;
import com.epeins.factory.util.PageRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanInfoDTO{

	    private Long id;

	    private Date planDate;
	    
	    private String platId;

	    private String typeId;

	    private String prodId;

	    private String taskId;

	    private Integer countToday;

	    private String changeFlg;

	    private String materialId;
	    
	    private Integer changeLossTime;

	    private Integer waitLossTime;

	    private Integer repairLossTime;
	    
	    private Integer otherLossTime;
	    
	
}
