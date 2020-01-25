package com.paymentprovider.model;

import java.time.LocalDate;

public class CommandLinePojo {

	private String clientId;
	private String orderId;
	private Integer amount;
	private String transactionType;
	private String currency;
	private String payMethod;
	private String payTokenId;
	private LocalDate strDate;
	private LocalDate endDate;

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

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayTokenId() {
		return payTokenId;
	}

	public void setPayTokenId(String payTokenId) {
		this.payTokenId = payTokenId;
	}

	public LocalDate getStrDate() {
		return strDate;
	}

	public void setStrDate(LocalDate strDate) {
		this.strDate = strDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

}
