package com.paymentprovider.model;

public class CommandLinePojo {

	private String clientId;
	private String orderId;
	private Integer amount;
	private String transactionType;
	private String currency;
	private String paymentMethod;
	private String payTokenId;
	
	
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPayTokenId() {
		return payTokenId;
	}
	public void setPayTokenId(String payTokenId) {
		this.payTokenId = payTokenId;
	}

	
	
	
	
}
