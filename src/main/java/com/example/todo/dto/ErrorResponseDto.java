package com.example.todo.dto;

public class ErrorResponseDto {

	public ErrorResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ErrorResponseDto(String msgKey, String message, String error) {
		super();
		this.msgKey = msgKey;
		this.message = message;
		this.error = error;
	}

	private String msgKey;

	private String message;

	private String error;

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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "ErrorResponseDto [msgKey=" + msgKey + ", message=" + message + ", error=" + error + "]";
	}

}
