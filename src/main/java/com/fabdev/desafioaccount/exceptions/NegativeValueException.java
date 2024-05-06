package com.fabdev.desafioaccount.exceptions;

public class NegativeValueException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NegativeValueException(String message) {
		super(message);
	}
}
