package org.shizhijian.raisefunds.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {

	private PageRequest pageRequest;
	
	private List<T> content;
}
