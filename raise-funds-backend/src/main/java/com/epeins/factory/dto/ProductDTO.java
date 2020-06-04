package com.epeins.factory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {

	 private Integer id;
	 
	 private String prodName;
	 
	 private String memo;
}
