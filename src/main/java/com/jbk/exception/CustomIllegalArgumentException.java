package com.jbk.exception;



public class CustomIllegalArgumentException extends RuntimeException {

	public CustomIllegalArgumentException(String msg){
		super(msg);
	}
}
