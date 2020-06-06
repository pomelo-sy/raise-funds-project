package org.shizhijian.raisefunds.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TaskInfoDTO {

	private Integer id;

	private String taskNo;

	private Integer prodId;

	private Integer taskCount;
	
	private String prodName;

}
