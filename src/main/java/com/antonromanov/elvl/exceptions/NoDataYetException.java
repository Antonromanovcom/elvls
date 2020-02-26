package com.antonromanov.elvl.exceptions;

public class NoDataYetException extends RuntimeException {

	private String message;

	public NoDataYetException(String message) {
			this.message = message;	// Еще нет желаний
	}

	public String getMessage() {
		return message;
	}
}
