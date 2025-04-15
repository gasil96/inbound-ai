package br.com.xpto.inboundai.handler;import br.com.xpto.inboundai.handler.exception.BusinessException;import br.com.xpto.inboundai.handler.exception.NotFoundException;import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.ExceptionHandler;import org.springframework.web.bind.annotation.RestControllerAdvice;import org.springframework.web.context.request.WebRequest;import java.time.LocalDateTime;@RestControllerAdvicepublic class GlobalExceptionHandler {	@ExceptionHandler(Exception.class)	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {		ErrorResponse errorResponse = ErrorResponse.builder()				                              .timestamp(LocalDateTime.now())				                              .status(HttpStatus.INTERNAL_SERVER_ERROR.value())				                              .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())				                              .message(ex.getMessage())				                              .path(request.getDescription(false).replace("uri=", ""))				                              .build();		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);	}	@ExceptionHandler(MethodArgumentNotValidException.class)	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {		String message = ex.getBindingResult().getFieldErrors().stream()				                 .map(error -> String.format("Field '%s': %s", error.getField(), error.getDefaultMessage()))				                 .reduce((msg1, msg2) -> msg1 + "; " + msg2)				                 .orElse("Validation error");		ErrorResponse errorResponse = ErrorResponse.builder()				                              .timestamp(LocalDateTime.now())				                              .status(HttpStatus.BAD_REQUEST.value())				                              .error(HttpStatus.BAD_REQUEST.getReasonPhrase())				                              .message(message)				                              .path(request.getDescription(false).replace("uri=", ""))				                              .build();		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);	}	@ExceptionHandler(NotFoundException.class)	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {		ErrorResponse errorResponse = ErrorResponse.builder()				                              .timestamp(LocalDateTime.now())				                              .status(HttpStatus.NOT_FOUND.value())				                              .error(HttpStatus.NOT_FOUND.getReasonPhrase())				                              .message(ex.getMessage())				                              .path(request.getDescription(false).replace("uri=", ""))				                              .build();		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);	}	@ExceptionHandler(BusinessException.class)	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {		ErrorResponse errorResponse = ErrorResponse.builder()				                              .timestamp(LocalDateTime.now())				                              .status(HttpStatus.UNPROCESSABLE_ENTITY.value())				                              .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())				                              .message(ex.getMessage())				                              .path(request.getDescription(false).replace("uri=", ""))				                              .build();		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);	}}