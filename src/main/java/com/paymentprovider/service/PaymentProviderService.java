package com.paymentprovider.service;

import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {

	String registerNewTransaction(CommandLinePojo comdLinePojo);

	String authoriseTransaction(CommandLinePojo comdLinePojo);

	String captureTransaction(CommandLinePojo comdLinePojo);

	String reverseTransaction(CommandLinePojo comdLinePojo);

	String findTotalofSuccTransaction(CommandLinePojo comdLinePojo);

	TransactionDetails findByorder(CommandLinePojo comdLinePojo);

	String findPendingTransactions(CommandLinePojo comdLinePojo);

}
