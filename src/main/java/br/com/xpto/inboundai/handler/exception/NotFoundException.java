package br.com.xpto.inboundai.handler.exception;import java.io.Serial;public class NotFoundException extends RuntimeException {	@Serial	private static final long serialVersionUID = 8919243022690326512L;	public NotFoundException(String message) {		super(message);	}}