package com.paymentprovider.service;

import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {

	TransactionDetails findTransaction(String clientId, String orderId);

	void registerTransaction(String clientId, String orderId, Integer amount, String currency, String paymentMethod);

	void authoriseTransaction(String clientId, String orderId);

	void captureTransaction(String clientId, String orderId);

	void reverseTransaction(String clientId, String orderId);

	TransactionDetails findPendingTransactions(String status);

}
