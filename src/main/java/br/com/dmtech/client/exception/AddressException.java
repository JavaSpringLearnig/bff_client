package br.com.dmtech.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AddressException extends RuntimeException {

	private static final long serialVersionUID = -7112789176697114589L;

	private final HttpStatus httpStatus;

	public AddressException(String msg) {
		super(msg);
		this.httpStatus = HttpStatus.resolve(HttpStatus.BAD_REQUEST.value());
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}