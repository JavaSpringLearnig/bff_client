package br.com.dmtech.client.exception;

import java.util.Calendar;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomExceptionResponse {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-3")
	private Calendar dataErro;
	private HttpStatus status;
	private String path;
	private String tituloErro;
	private String msgErro;
	private String debugMsgErro;

	public CustomExceptionResponse(Calendar dataErro, HttpStatus status, String path) {
		this.dataErro = (Calendar) dataErro.clone();
		this.status = status;
		this.path = path;
	}

}