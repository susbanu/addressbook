package com.address.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(AddressNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(AddressNotFoundException ex, WebRequest request) {
		LOG.error("ResourceNotFound Exception : {}", ex);
		var errorDetail = new ErrorDetail(ex.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND);
		return buildResponseEntity(errorDetail);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	protected ResponseEntity<Object> invalidParamsExceptionHandler(ConstraintViolationException ex, WebRequest request) {
		LOG.error("ConstraintViolation Exception : {}", ex);
		List<String> errors = new ArrayList<>();
		ex.getConstraintViolations().forEach(violation -> errors.add(violation.getRootBeanClass().getName() + " "
				+ violation.getPropertyPath() + ": " + violation.getMessage()));
		var errorDetail = new ErrorDetail(String.join(",", errors), request.getDescription(false),
				HttpStatus.BAD_REQUEST);
		return buildResponseEntity(errorDetail);
	}
	
	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		LOG.error("IllegalArgument or IllegalState Exception : {}", ex);
		var errorDetail = new ErrorDetail(ex.getMessage(), request.getDescription(false), HttpStatus.CONFLICT);
		return buildResponseEntity(errorDetail);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
		LOG.error("Max file upload size error : {}", ex);
		var errorDetail = new ErrorDetail(ex.getMessage(), request.getDescription(false),
				HttpStatus.EXPECTATION_FAILED);
		return buildResponseEntity(errorDetail);
	}

	@ExceptionHandler(MongoDBException.class)
	protected ResponseEntity<Object> handleEntityNotFound(MongoDBException ex, WebRequest request) {
		LOG.error("Monogo Database Exception : {}", ex);
		var errorDetail = new ErrorDetail(ex.getMessage(), request.getDescription(false), HttpStatus.CONFLICT);
		return buildResponseEntity(errorDetail);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOG.error("Malformed request : {} ", ex);
		var errorDetail = new ErrorDetail(ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST);
		return buildResponseEntity(errorDetail);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOG.error("Method argument not valid Exception : {}", ex);
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());
		var errorDetail = new ErrorDetail(String.join(",", errors) + " Headers: " + headers.toString(),
				request.getDescription(false), HttpStatus.BAD_REQUEST);
		return buildResponseEntity(errorDetail);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		LOG.error("Request handler not found : {}", ex);
		var errorDetail = new ErrorDetail("HTTP Method: "+ex.getHttpMethod() + " Request URL: " + ex.getRequestURL(),
				request.getDescription(false), status);
		return buildResponseEntity(errorDetail);
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> unknownException(Exception ex, WebRequest request) {
		LOG.error("Internal server error : {}", ex);
		var errorDetail = new ErrorDetail(ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
		return buildResponseEntity(errorDetail);
	}
	
	private ResponseEntity<Object> buildResponseEntity(ErrorDetail errorDetail) {
		return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
	}
}
