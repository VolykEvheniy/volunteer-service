package com.webtutsplus.ecommerce.common;

import java.time.LocalDateTime;

public class ApiResponse {
	private final boolean success;
	private final String message;
	private Integer orderId;
	// try again
	
	public ApiResponse(boolean success, String message, Integer orderId) {
		this.success = success;
		this.message = message;
		this.orderId = orderId;
	}

	public ApiResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
	
	public String getTimestamp() {
		return LocalDateTime.now().toString();
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
