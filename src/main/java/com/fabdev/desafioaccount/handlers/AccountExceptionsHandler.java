package com.fabdev.desafioaccount.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fabdev.desafioaccount.dtos.ErrorDto;
import com.fabdev.desafioaccount.exceptions.NegativeValueException;
import com.fabdev.desafioaccount.exceptions.NotFoundObjetException;
import com.fabdev.desafioaccount.exceptions.NotNullableFieldsException;

@ControllerAdvice
public class AccountExceptionsHandler {
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	@ExceptionHandler(NotNullableFieldsException.class)
	public ErrorDto handler(NotNullableFieldsException ex) {
		return new ErrorDto(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler(NotFoundObjetException.class)
	public ErrorDto handler(NotFoundObjetException ex) {
		return new ErrorDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
	}
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	@ExceptionHandler(NegativeValueException.class)
	public ErrorDto handler(NegativeValueException ex) {
		return new ErrorDto(ex.getMessage(), HttpStatus.CONFLICT.value());
	}

}
