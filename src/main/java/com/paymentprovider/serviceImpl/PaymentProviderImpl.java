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
	CurrencyRepository currencyRepo;
	
	@Autowired
	PaymentMethodRepository payMethRepo;
	
	@Autowired
	TransactionDetailsRepository transDetalRepo;

	@Override
	public List<Currency> getCurrency() {

		return currencyRepo.findAll();
	}

	@Override
	public List<PaymentMethod> getPaymentMethod() {
		return payMethRepo.findAll();
	}

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
		transDetalRepo.saveTransactionStatus(orderId);
		
	}
	

	

}
