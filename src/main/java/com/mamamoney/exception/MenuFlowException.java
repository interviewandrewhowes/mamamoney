package com.mamamoney.exception;

public class MenuFlowException extends Exception {

	private static final long serialVersionUID = 2015633643589905348L;
	
	private String message;
	
	public MenuFlowException(String message) {
		super(message);
		
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
