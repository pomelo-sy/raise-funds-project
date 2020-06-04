package com.epeins.factory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 机型类型 eg: k1214
 * @author YongSun
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MachineTypeDTO  {
	

	private Integer id;
	
    private String typeId;

    private String typeName;

    private String memo;


}