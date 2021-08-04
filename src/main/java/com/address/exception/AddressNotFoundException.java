package com.address.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AddressNotFoundException() {
		super();
	}

	public AddressNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddressNotFoundException(String message) {
		super(message);
	}

	public AddressNotFoundException(Throwable cause) {
		super(cause);
	}
}
