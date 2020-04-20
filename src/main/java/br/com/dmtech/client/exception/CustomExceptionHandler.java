package br.com.dmtech.client.exception;

import java.util.Calendar;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.netflix.hystrix.exception.HystrixRuntimeException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	@Autowired
	public CustomExceptionHandler(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(), status,
				request.getDescription(false));

		String titleError = messageSource.getMessage("error.msg.solicitacao.json.malFormada", new Object[] {},
				Locale.getDefault());

		exceptionResponse.setTituloErro(titleError);
		exceptionResponse.setMsgErro(titleError);
		exceptionResponse.setDebugMsgErro(ex.toString());

		return buildError(exceptionResponse, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(),
				HttpStatus.BAD_REQUEST, request.getDescription(false));
		String titleError = messageSource.getMessage("error.msg.payload.invalido", new Object[] {},
				Locale.getDefault());
		exceptionResponse.setTituloErro(titleError);
		exceptionResponse.setDebugMsgErro(ex.getMessage());
		StringBuilder strError = new StringBuilder();
		for (FieldError fieldErro : ex.getBindingResult().getFieldErrors()) {
			String msgUser = messageSource.getMessage(fieldErro, Locale.getDefault()) + "; ";
			strError.append(msgUser);
		}
		exceptionResponse.setMsgErro(strError.toString());
		return buildError(exceptionResponse, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(ResourceNotFoundException ex, WebRequest request) {

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(),
				ex.getHttpStatus(), request.getDescription(false));

		exceptionResponse.setTituloErro(
				messageSource.getMessage("error.msg.recurso.naoEncontrado", new Object[] {}, Locale.getDefault()));
		exceptionResponse.setMsgErro(ex.getMessage());
		exceptionResponse.setDebugMsgErro(ex.getMessage());
		return buildError(exceptionResponse, request);
	}

	@ExceptionHandler(CepInvalidException.class)
	protected ResponseEntity<Object> handlCepException(CepInvalidException ex, WebRequest request) {

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(),
				ex.getHttpStatus(), request.getDescription(false));

		String titleError = messageSource.getMessage("error.msg.solicitacao.cep.invalido", new Object[] {},
				Locale.getDefault());
		exceptionResponse.setTituloErro(titleError);
		exceptionResponse.setDebugMsgErro(ex.getMessage());
		exceptionResponse.setMsgErro(
				messageSource.getMessage("error.msg.solicitacao.cep.invalido", new Object[] {}, Locale.getDefault()));
		return buildError(exceptionResponse, request);
	}

	@ExceptionHandler(AddressException.class)
	protected ResponseEntity<Object> handlAddressException(AddressException ex, WebRequest request) {

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(),
				ex.getHttpStatus(), request.getDescription(false));

		String titleError = messageSource.getMessage("error.msg.operacao.nao.realizada", new Object[] {},
				Locale.getDefault());
		exceptionResponse.setTituloErro(titleError);
		exceptionResponse.setDebugMsgErro(ex.getMessage());
		exceptionResponse.setMsgErro(ex.getMessage());
		return buildError(exceptionResponse, request);
	}

	@ExceptionHandler(HystrixRuntimeException.class)
	public final ResponseEntity<Object> handleClientExceptions(HystrixRuntimeException ex, WebRequest request) {
		String titleError;
		String msgErro;
		HttpStatus httpStatus;

		httpStatus = HttpStatus.BAD_REQUEST;
		titleError = messageSource.getMessage("error.msg.operacao.nao.realizada", new Object[] {}, Locale.getDefault());
		msgErro = messageSource.getMessage("error.msg.proxy.descricao", new Object[] {}, Locale.getDefault());

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(), httpStatus,
				request.getDescription(false));

		exceptionResponse.setTituloErro(titleError);
		exceptionResponse.setMsgErro(msgErro);
		exceptionResponse.setDebugMsgErro(ex.getMessage());
		return buildError(exceptionResponse, request);
	}

	@ExceptionHandler(CpfFoundException.class)
	public final ResponseEntity<Object> handleCpfFoundException(CpfFoundException ex, WebRequest request) {
		String titleError;
		String msgErro;
		HttpStatus httpStatus;

		httpStatus = HttpStatus.BAD_REQUEST;
		titleError = messageSource.getMessage("error.msg.operacao.nao.realizada", new Object[] {}, Locale.getDefault());
		msgErro = messageSource.getMessage("error.msg.cpf.ja.cadastrado", new Object[] {}, Locale.getDefault());

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(), httpStatus,
				request.getDescription(false));

		exceptionResponse.setTituloErro(titleError);
		exceptionResponse.setMsgErro(msgErro);
		exceptionResponse.setDebugMsgErro(ex.getMessage());
		return buildError(exceptionResponse, request);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

		CustomExceptionResponse exceptionResponse = new CustomExceptionResponse(Calendar.getInstance(),
				HttpStatus.INTERNAL_SERVER_ERROR, request.getDescription(false));

		String titleError = messageSource.getMessage("error.msg.interno.server.error", new Object[] {},
				Locale.getDefault());
		exceptionResponse.setTituloErro(titleError);
		exceptionResponse.setMsgErro(messageSource.getMessage("error.msg.interno.server.error.descricao",
				new Object[] {}, Locale.getDefault()));
		exceptionResponse.setDebugMsgErro(ex.toString());
		return buildError(exceptionResponse, request);
	}

	private ResponseEntity<Object> buildError(CustomExceptionResponse exceptionResponse, WebRequest request) {
		return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
	}

}
