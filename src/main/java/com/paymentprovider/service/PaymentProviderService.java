package com.paymentprovider.service;

import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.PaymentProviderException;
import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {

	String registerNewTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException;

	String authoriseTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException;

	String captureTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException;

	String reverseTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException;

	String findTotalofSuccTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException;

	TransactionDetails findByorder(CommandLinePojo comdLinePojo) throws PaymentProviderException;

	String findPendingTransactions(CommandLinePojo comdLinePojo) throws PaymentProviderException;

}
