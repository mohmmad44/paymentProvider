package com.paymentprovider.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.paymentprovider.dto.AuthoriseDTO;
import com.paymentprovider.dto.RegisterDto;
import com.paymentprovider.model.Currency;
import com.paymentprovider.model.PaymentMethod;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.CurrencyRepository;
import com.paymentprovider.repository.PaymentMethodRepository;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.service.PaymentProviderService;

public class PaymentProviderImpl implements PaymentProviderService {

	
	
	@Autowired
	TransactionDetailsRepository transDetalRepo;

	
	@Override
	public void saveRegister(RegisterDto registerDto) {
		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setClientId(registerDto.getClientId());
		transactionDetails.setAmount(registerDto.getAmount());
		transactionDetails.setCurrency(currencyRepo.getCurrencyId(registerDto.getCurrencyId()));
		transactionDetails.setPaymentMethod(payMethRepo.getPaymentId(registerDto.getPaymentMethodId()));
		transactionDetails.setStatus("Registered");
		transDetalRepo.save(transactionDetails);
		
	}

	@Override
	public TransactionDetails getRegisterById(String clientId, String orderId) {
		TransactionDetails registerById= transDetalRepo.findById(clientId,orderId);
		if(registerById.getStatus()=="Registered") {
		return registerById;
		}else if(registerById.getStatus()=="Authorised") {
			return "";
		}
	}

	@Override
	public void updateAuthorise(AuthoriseDTO authoriseDTO) {
		String orderId=authoriseDTO.getOrderId();
		transDetalRepo.saveAuthTransactionStatus(orderId);
		
	}

	@Override
	public TransactionDetails getCaptureById(String clientId, String orderId) {
		TransactionDetails registerById= transDetalRepo.findById(clientId,orderId);
		if(registerById.getStatus()=="Authorised") {
		return registerById;
		}else if(registerById.getStatus()=="Registered") {
			return "";
		}
	}

	@Override
	public void updateCapture(AuthoriseDTO authoriseDTO) {
		String orderId=authoriseDTO.getOrderId();
		transDetalRepo.saveCapTransactionStatus(orderId);
		
	}

	@Override
	public TransactionDetails getRegisterById(String orderId) {
		return transDetalRepo.findById(orderId);;
	}
	
	
	
	
	
	

	

}
