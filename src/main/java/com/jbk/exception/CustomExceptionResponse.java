package com.jbk.exception;

public class CustomExceptionResponse {

	private String path;
	private String msg;
	private String timeStamp;
	
	public CustomExceptionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomExceptionResponse(String path, String msg, String timeStamp) {
		super();
		this.path = path;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
