package com.epeins.factory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaterialDTO {
	
		private Integer id;

	    private String materialName;

	    private String materialDisp;

	    private String materialColor;

	    private String memo;
	
}
