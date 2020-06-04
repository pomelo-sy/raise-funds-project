package com.epeins.factory.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
	
	//当前页数
	@ApiModelProperty(value = "当前页数")
    private long pageNo;
 
    //每页显示数量
	@ApiModelProperty(value = "每页显示数量")
    private long pageSize;
 
    @ApiModelProperty(value = "总条数")
    private long totalRecord;
 
    //总页数
    @ApiModelProperty(value = "总页数")
    private long totalPage;
 
    //排序字段
//    @ApiModelProperty(value = "排序字段")
//    private String[] orderByFields;
//    
//    @ApiModelProperty(value = "默认升序:true; 降序:false")
//    private Boolean asc;

}