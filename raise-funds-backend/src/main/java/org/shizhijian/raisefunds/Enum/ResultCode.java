package org.shizhijian.raisefunds.Enum;

public enum ResultCode {

	SUCCESS(200), FAIL(500);
	
	private Integer code;
	
	private ResultCode(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return this.code;
	}
}
