package com.paymentprovider.util;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.paymentprovider.common.Constants;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.PaymentProviderException;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;

@Configuration
public class PaymentProviderUtil {

	@Autowired
	TransactionDetailsRepository transDetalRepo;

	@Autowired
	PaymentProviderException paymentProviderException;

	public TransactionDetails findByorder(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		try {
			return transDetalRepo.findTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
		} catch (EntityNotFoundException e) {
			paymentProviderException.setErrMessage(Constants.EntityNotFoundException);
			throw paymentProviderException;

		} catch (Exception e) {
			paymentProviderException.setErrMessage(e.getMessage());
			throw paymentProviderException;
		}
	}

}
