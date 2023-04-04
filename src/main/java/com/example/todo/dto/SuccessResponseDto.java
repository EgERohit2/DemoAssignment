package com.example.todo.dto;

public class SuccessResponseDto {

	private String msgKey;

	private String message;

	private Object data;

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public SuccessResponseDto(String msgKey, String message, Object data) {
		super();
		this.msgKey = msgKey;
		this.message = message;
		this.data = data;
	}

}
