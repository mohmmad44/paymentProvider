package com.paymentprovider.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class TransactionDetails {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "order_id")
	private String orderId;
	
	
	@Column(name= "order_date")
	private Date date;

	@Column(name = "amount")
	private Integer amount;

	@Column(name = "pay_token_id", unique = true)
	private String payTokenId;

	@ManyToOne
	@JoinColumn(name = "payment_method_id")
	private PaymentMethod paymentMethod;

	@ManyToOne
	@JoinColumn(name = "curreny_id")
	private Currency currency;

	@ManyToOne
	@JoinColumn(name = "transaction_type_id")
	private TransactionType transationType;

	@Column(name = "status")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayTokenId() {
		return payTokenId;
	}

	public void setPayTokenId(String payTokenId) {
		this.payTokenId = payTokenId;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public TransactionType getTransationType() {
		return transationType;
	}

	public void setTransationType(TransactionType transationType) {
		this.transationType = transationType;
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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
