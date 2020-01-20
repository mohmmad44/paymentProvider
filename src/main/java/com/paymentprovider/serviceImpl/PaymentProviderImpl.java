package com.paymentprovider.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.service.PaymentProviderService;

@Service
public class PaymentProviderImpl implements PaymentProviderService {

	@Autowired
	TransactionDetailsRepository transDetalRepo;

	@Override
	public TransactionDetails findTransaction(String clientId, String orderId) {
		return transDetalRepo.findById(clientId, orderId);
	}

	@Override
	public void registerTransaction(String clientId, String orderId, Integer amount, String currency,
			String paymentMethod) {
		TransactionDetails tDetails = new TransactionDetails();
		tDetails.setClientId(clientId);
		tDetails.setOrderId(orderId);
		tDetails.setAmount(amount);
		tDetails.setCurrency(currency);
		tDetails.setPaymentMethod(paymentMethod);

		transDetalRepo.save(tDetails);
	}

	@Override
	public void authoriseTransaction(String clientId, String orderId) {
		transDetalRepo.updateRegiStatus(clientId, orderId);
	}

	@Override
	public void captureTransaction(String clientId, String orderId) {
		transDetalRepo.updateAuthStatus(clientId, orderId);
	}

	@Override
	public void reverseTransaction(String clientId, String orderId) {
		transDetalRepo.reverseTransaction(clientId, orderId);
	}

	@Override
	public TransactionDetails findPendingTransactions(String status) {
		TransactionDetails pendingtransDetails = transDetalRepo.findByStatus(status);
		return pendingtransDetails;
	}

}
