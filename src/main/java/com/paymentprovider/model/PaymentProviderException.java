package com.paymentprovider.model;



public class PaymentProviderException extends Throwable{

	private static final long serialVersionUID = 1L;
	private String errMessage;
	private String errCode;
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	@Override
	public String toString() {
		return "PaymentProviderException [errMessage=" + errMessage + ", errCode=" + errCode + "]";
	}
}
