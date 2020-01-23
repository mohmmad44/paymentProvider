package com.paymentprovider.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.service.PaymentProviderService;

@Service
@Transactional
public class PaymentProviderImpl implements PaymentProviderService {

	@Autowired
	TransactionDetailsRepository transDetalRepo;

	CommandLinePojo comdLinePojo = new CommandLinePojo();
	TransactionDetails transDetails= new TransactionDetails();
	
	public TransactionDetails findByorder() {
	return transDetails = transDetalRepo.findTransaction(comdLinePojo.getClientId(),
			comdLinePojo.getOrderId());
	}

	boolean comparePojo = new Gson().toJson(comdLinePojo).equals(new Gson().toJson(transDetails));

	@Override
	public String registerNewTransaction(CommandLinePojo comdLinePojo) throws Exception {
		String tranStatus = transDetails.getStatus();

		if (transDetails == null || (tranStatus != "REVERSED" && (comparePojo != true))) {
			BeanUtils.copyProperties(transDetails, comdLinePojo);
			transDetails.setStatus("REGISTERED");
			transDetalRepo.save(transDetails);
			return "order is successfully REGISTERED";

		} else
			return "orderId already exist";
	}

	@Override
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

	@Override
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

	@Override
	public String reverseTransaction(CommandLinePojo comdLinePojo) {
		String tranStatus = transDetails.getStatus();

		if (transDetails == null) {
			return "order does NOT exist";

		} else if (comparePojo != true) {
			return "Entered values 'amount, currency, payMethod' donot match with the REGISTERED data";

		} else if (tranStatus == "REVERSED") {
			return "transaction is  already Cancelled  ";
		} else

			transDetalRepo.reverseTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
		return "order is successfully REVERSED";

	}

	@Override
	public TransactionDetails findPendingTransactions() {
		TransactionDetails pendingtransDetails = transDetalRepo.findPendingTransations(comdLinePojo.getClientId());
		return pendingtransDetails;
	}

	@Override
	public Integer findTotalofSuccTransaction(CommandLinePojo comdLinePojo) {
		//Integer total = transDetalRepo.findTotalAmont(comdLinePojo.getClientId(), comdLinePojo.getStrDate(),comdLinePojo.getEndDate());
		return 0;
	}

}
