package com.paymentprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.service.PaymentProviderService;

public class Controller {
	
	@Autowired
	PaymentProviderService ppService;
	
	
	public String registerNewTransaction(String clientId, String orderId, Integer amount, String currency, String paymentMethod,
			String payTokenId) {
		TransactionDetails transDetails = ppService.findTransaction(clientId, orderId);
		
		if(transDetails==null && tType == "REGISTER") {
			ppService.registerTransaction(clientId, orderId, amount, currency, paymentMethod, payTokenId);
			
			return "order is successfully REGISTERED";
		}else 
			return "orderId is already exist";
			
	}
	
	public String authoriseTransaction(String clientId, String orderId, Integer amount, String currency, String paymentMethod,
			String payTokenId) {
		TransactionDetails transDetails = ppService.findTransaction(clientId, orderId);
		if(transDetails==null) {
			return "orderId is NOT exist";
						
		}else if(transDetails.getAmount()!= amount){
			return "Entered amount is Wrong";
		}else if(transDetails.getCurrency()!= currency) {
			return "Enter the correct Currency Type";
		}else if(transDetails.getPaymentMethod()!= paymentMethod) {
			return "Entered payMethod is Incorrect";
		}else if(transDetails.getPayTokenId()!= payTokenId) {
			return "Entered payTokenId is Incorrect";
		}
			return "order is successfully AUTHORISED";
	}
	
	
	
	
	
	
	
	

}
