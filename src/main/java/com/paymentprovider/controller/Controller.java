package com.paymentprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.service.PaymentProviderService;


public class Controller {

	@Autowired
	PaymentProviderService ppService;
	
	
	CommandLinePojo comdLinePojo =new CommandLinePojo();
	
	
	public void callSwitchMethod( String input) throws Exception {
		
		switch (input) {
		case "register":
			String newTrans= ppService.registerNewTransaction(comdLinePojo);
			System.out.println(newTrans);
			break;
			
		case "authorise":
			String authorise = ppService.authoriseTransaction(comdLinePojo);
			System.out.println(authorise);
			break;

		case "capture":
			String capture =ppService.captureTransaction(comdLinePojo);
			System.out.println(capture);
			break;

		case "reverse":
			String reverse=ppService.reverseTransaction(comdLinePojo);
			System.out.println(reverse);
			break;
			
		case "findByOrder":
			
			break;
		case "findPending":
			TransactionDetails pendingTrans= ppService.findPendingTransactions();
			System.out.println(pendingTrans);
			break;
			
			
		case "findTotal":
			Integer findTotal = ppService.findTotalofSuccTransaction(comdLinePojo);
			System.out.println(findTotal);
			break;

		
		}
	}

}
