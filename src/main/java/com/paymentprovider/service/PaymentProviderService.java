package com.paymentprovider.service;

import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {

	

	String registerNewTransaction(CommandLinePojo comdLinePojo) throws Exception;

	String authoriseTransaction(CommandLinePojo comdLinePojo);

	String captureTransaction(CommandLinePojo comdLinePojo);

	String reverseTransaction(CommandLinePojo comdLinePojo);

	TransactionDetails findPendingTransactions();

	Integer findTotalofSuccTransaction(CommandLinePojo comdLinePojo);

	TransactionDetails findByorder() throws Exception;

	

}
