package com.paymentprovider.model;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class PaymentProviderException extends Throwable{

	private static final long serialVersionUID = 1L;
	private String errMessage;
	private String errCode;
	
	
	public String getErrMessage() {
		return errMessage;
	}
	public String setErrMessage(String errMessage) {
		return this.errMessage = errMessage;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String errormessage() {
		return "PaymentProviderException [errMessage=" + errMessage +"]";
	}
}
