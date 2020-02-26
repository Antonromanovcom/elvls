package com.antonromanov.elvl.exceptions;

public class BadIncomeParameterException extends RuntimeException {

	private String message;

	public BadIncomeParameterException(String text) {
			this.message = text;
	}

	public String getMessage() {
		return message;
	}


}
