package com.paymentprovider.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

public class TransactionDetails {

	@Column(name = "client_id")
	private String clientId;

	@Id
	@Column(name = "order_id", unique = true)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "pay_tokenId")
	// @SequenceGenerator(name="pay_tokenId", sequenceName = "book-",
	// allocationSize=50)
	private String orderId;

	@Column(name = "order_date")
	private Date date;

	@Column(name = "amount")
	private Integer amount;

	@Column(name = "currency")
	private String currency;

	@Column(name = "payment_method")
	private String paymentMethod;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "pay_token_id", unique = true)
	private String payTokenId;

	@Column(name = "status")
	private String status;

	public String getPayTokenId() {
		return payTokenId;
	}

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPayTokenId(String payTokenId) {
		this.payTokenId = payTokenId;
	}

}
