package org.shizhijian.raisefunds.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ResultData<T> {

	@ApiModelProperty(value = "返回标记", name="resultFlag")
	private Boolean resultFlag;
	
	@ApiModelProperty(value = "返回code", name="resultCode")
	private Integer resultCode;
	
	@ApiModelProperty(value = "返回备注", name="remark")
	private String remark;
	
	@ApiModelProperty(value = "返回对象", name="Object")
	private T resultData;
	
	
}
