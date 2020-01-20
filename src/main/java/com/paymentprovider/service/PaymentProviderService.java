package com.paymentprovider.service;

import java.util.Date;

import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {

	TransactionDetails findTransaction(String clientId, String orderId);

	void registerTransaction(String clientId, String orderId, Integer amount, String currency, String paymentMethod,
			String payTokenId);

	void authoriseTransaction(String clientId, String orderId);

	void captureTransaction(String clientId, String orderId);

	void reverseTransaction(String clientId, String orderId);

	TransactionDetails findPendingTransactions(String status);

	Integer findTotalofSuccTransaction(String clientId, Date begindate, Date enddate);

}
