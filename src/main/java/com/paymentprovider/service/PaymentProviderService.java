package com.paymentprovider.service;

import java.util.List;

import com.paymentprovider.dto.AuthoriseDTO;
import com.paymentprovider.dto.RegisterDto;
import com.paymentprovider.model.Currency;
import com.paymentprovider.model.PaymentMethod;
import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {

	List<Currency> getCurrency();

	List<PaymentMethod> getPaymentMethod();

	void saveRegister(RegisterDto registerDto);

	TransactionDetails getRegisterById(String clientId, String orderId);

	void updateAuthorise(AuthoriseDTO authoriseDTO);

	TransactionDetails getCaptureById(String clientId, String orderId);


}
