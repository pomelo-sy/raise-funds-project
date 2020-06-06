package org.shizhijian.raisefunds.dto;

import java.util.Date;

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
