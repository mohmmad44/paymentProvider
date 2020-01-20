package com.paymentprovider.service;

import com.paymentprovider.dto.AuthoriseDTO;
import com.paymentprovider.dto.RegisterDto;
import com.paymentprovider.model.TransactionDetails;

public interface PaymentProviderService {



	void saveRegister(RegisterDto registerDto);

	TransactionDetails getRegisterById(String clientId, String orderId);

	void updateAuthorise(AuthoriseDTO authoriseDTO);

	TransactionDetails getCaptureById(String clientId, String orderId);

	void updateCapture(AuthoriseDTO authoriseDTO);

	TransactionDetails getRegisterById(String orderId);


}
