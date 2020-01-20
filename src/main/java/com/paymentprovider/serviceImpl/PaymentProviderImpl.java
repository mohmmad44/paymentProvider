package com.paymentprovider.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.paymentprovider.controller.Controller;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.service.PaymentProviderService;

@Service
public class PaymentProviderImpl implements PaymentProviderService {

	@Autowired
	TransactionDetailsRepository transDetalRepo;

	CommandLinePojo comdLinePojo = new CommandLinePojo();

	TransactionDetails transDetails = transDetalRepo.findTransaction(comdLinePojo.getClientId(),
			comdLinePojo.getOrderId());

	boolean comparePojo = new Gson().toJson(comdLinePojo).equals(new Gson().toJson(transDetails));
	
	
	
	
	
	
	

	public String registerNewTransaction(CommandLinePojo comdLinePojo) {

		if (transDetails == null) {
			BeanUtils.copyProperties(transDetails, comdLinePojo);
			transDetails.setStatus("REGISTERED");
			transDetalRepo.save(transDetails);
			return "order is successfully REGISTERED";

		} else
			return "orderId is already exist";
	}

	public String authoriseTransaction(CommandLinePojo comdLinePojo) {
		String tranStatus = transDetails.getStatus();

		if (transDetails == null) {
			return "order does NOT exist";

		} else if (comparePojo != true) {
			return "Entered values 'amount, currency, payMethod' donot match with the REGISTERED data";

		} else if (tranStatus != "REGISTERED") {
			return "transaction is not in REGISTERED state ";
		} else if (tranStatus == "REVERSED") {
			return "transaction is not in REVERSED state ";
		} else

			transDetalRepo.updateRegiStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
		return "order is successfully AUTHORISED";

	}

	public String captureTransaction(CommandLinePojo comdLinePojo) {
		String tranStatus = transDetails.getStatus();

		if (transDetails == null) {
			return "order does NOT exist";

		} else if (comparePojo != true) {
			return "Entered values 'amount, currency, payMethod' donot match with the REGISTERED data";

		} else if (tranStatus != "AUTHORISED") {
			return "transaction is not in AUTHORISED state ";
		} else if (tranStatus == "REVERSED") {
			return "transaction is Cancelled  ";
		} else

			transDetalRepo.updateAuthStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
		return "order is successfully CAPTURED";

	}

	public String reverseTransaction(CommandLinePojo comdLinePojo) {
		String tranStatus = transDetails.getStatus();

		if (transDetails == null) {
			return "order does NOT exist";

		} else if (comparePojo != true) {
			return "Entered values 'amount, currency, payMethod' donot match with the REGISTERED data";

		} else if (tranStatus == "REVERSED") {
			return "transaction is  already Cancelled  ";
		}else

			transDetalRepo.reverseTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
		return "order is successfully REVERSED";

	}

	
	public TransactionDetails findPendingTransactions() {
		TransactionDetails pendingtransDetails = transDetalRepo.findPendingTransations(comdLinePojo.getClientId());
		return pendingtransDetails;
	}

	@Override
	public Integer findTotalofSuccTransaction(String clientId, Date begindate, Date enddate) {
		Integer total = transDetalRepo.findTotalAmont(clientId, begindate, enddate);
		return total;
	}

}
