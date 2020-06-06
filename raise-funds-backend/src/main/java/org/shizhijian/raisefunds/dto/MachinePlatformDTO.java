package org.shizhijian.raisefunds.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MachinePlatformDTO {
	
		private Integer id;
		
	    private String platId;
	    
	    private String platDisp;

	    private Integer platNo;

	    //对应机型表中主键
	    private Integer typeId;
	    
	    //机型的显示名字（对应机型表中的typeId）
	    private String typeName;
}
