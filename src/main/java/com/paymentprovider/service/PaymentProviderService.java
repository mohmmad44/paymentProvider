package com.paymentprovider.service;

import java.util.List;

import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {

	String registerNewTransaction(CommandLinePojo comdLinePojo) throws Exception;

	String authoriseTransaction(CommandLinePojo comdLinePojo);

	String captureTransaction(CommandLinePojo comdLinePojo);

	String reverseTransaction(CommandLinePojo comdLinePojo);

	Integer findTotalofSuccTransaction(CommandLinePojo comdLinePojo);

	TransactionDetails findByorder(CommandLinePojo comdLinePojo) throws Exception;

	List<TransactionDetails> findPendingTransactions(CommandLinePojo comdLinePojo);

}
