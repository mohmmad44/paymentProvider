package com.paymentprovider.serviceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.paymentprovider.PaymentproviderApplication;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.service.PaymentProviderService;

@Service
@Transactional
public class PaymentProviderImpl implements PaymentProviderService {

	@Autowired
	TransactionDetailsRepository transDetalRepo;

	Logger logger = LoggerFactory.getLogger(PaymentproviderApplication.class);

	@Override
	public TransactionDetails findByorder(CommandLinePojo comdLinePojo) {
		logger.info("inside findByOrder");
		return transDetalRepo.findTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
	}

	public boolean variablesCheck(CommandLinePojo comdLinePojo) {
		if (!(comdLinePojo.getCurrency().equals("EUR") || comdLinePojo.getCurrency().equals("USD")
				|| comdLinePojo.getCurrency().equals("GBP"))) {
			logger.info("Input Currency format is not supported");

			return false;
		}

		if (!(comdLinePojo.getPayMethod().equals("CARD") || comdLinePojo.getPayMethod().equals("INVOICE")
				|| comdLinePojo.getPayMethod().equals("CASH"))) {
			logger.info("Input payMethod format is not supported");

			return false;
		}
		return true;
	}

	// register the transaction by satisfying the conditions i.e, if orderId not
	// exist or orderId status should be Reversed
	@Override
	public String registerNewTransaction(CommandLinePojo comdLinePojo) {
		logger.info("inside registerNewTransation");
		String response = null;
		boolean variblesCheck = variablesCheck(comdLinePojo);
		if (variblesCheck) {

			TransactionDetails transDetails = new TransactionDetails();
			try {
				TransactionDetails transDetailsDb = this.findByorder(comdLinePojo);
				if (comdLinePojo.getTransactionType().equalsIgnoreCase("REGISTER")
						&& (transDetailsDb == null || transDetailsDb.getStatus().equalsIgnoreCase("REVERSED"))) {
					transDetails.setAmount(comdLinePojo.getAmount());
					transDetails.setClientId(comdLinePojo.getClientId());
					transDetails.setCurrency(comdLinePojo.getCurrency());
					transDetails.setDate(java.time.LocalDate.now());
					transDetails.setOrderId(comdLinePojo.getOrderId());
					transDetails.setPayMethod(comdLinePojo.getPayMethod());
					transDetails.setPayTokenId(comdLinePojo.getPayTokenId());
					transDetails.setTransactionType(comdLinePojo.getTransactionType());
					transDetails.setStatus("REGISTERED");
					transDetalRepo.save(transDetails);
					response = "register: status='SUCCESS'";
				} else
					response = "order already exists in the database";

			} catch (Exception e) {
				response = e.getMessage();
				logger.error(e.getMessage());
			}

		} else
			response = " status='ERROR', message='Invalid currency " + comdLinePojo.getCurrency()
					+ " or payment Method - " + comdLinePojo.getPayMethod();
		return response;
	}

	// Authorize the transaction by satisfying the conditions
	// i.e, orderId object should not be null & status should be Registered
	@Override
	public String authoriseTransaction(CommandLinePojo comdLinePojo) {
		logger.info("inside authoriseTransation()");
		String response = null;

		boolean variblesCheck = variablesCheck(comdLinePojo);
		if (variblesCheck) {
			try {
				TransactionDetails transDetails = findByorder(comdLinePojo);

				if (transDetails == null) {
					response = "status='ERROR', message='orderId not found";
				} else if (!(comdLinePojo.getTransactionType().equalsIgnoreCase("AUTHORISE"))) {
					response = "status='ERROR', message='Entered transactionType is not correct";
				} else if (!(transDetails.getAmount().equals(comdLinePojo.getAmount()))) {
					response = "status='ERROR', message='Entered amount is not correct";
				} else if ((!transDetails.getCurrency().equalsIgnoreCase(comdLinePojo.getCurrency()))) {
					response = "status='ERROR', message='Entered Currency Does not match with record";
				} else if (transDetails.getStatus().equalsIgnoreCase("REGISTERED")) {
					transDetalRepo.updateRegiStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
					response = "authorise: status='SUCCESS'";
				} else
					response = "status='ERROR', message='transaction is not in REGISTERED state ";
			} catch (Exception e) {
				response = e.getMessage();
				logger.error(e.getMessage());
			}
		} else
			response = " status='ERROR', message='Invalid currency " + comdLinePojo.getCurrency()
					+ " or payment Method - " + comdLinePojo.getPayMethod();
		return response;
	}

	// Capture the transaction by satisfying the conditions
	@Override
	public String captureTransaction(CommandLinePojo comdLinePojo) {
		logger.info("inside captureTransaction()");
		String response = null;

		boolean variblesCheck = variablesCheck(comdLinePojo);
		if (variblesCheck) {
			try {
				TransactionDetails transDetails = findByorder(comdLinePojo);

				if (transDetails == null) {
					response = "status='ERROR', message='orderId not found";
				} else if (!(comdLinePojo.getTransactionType().equalsIgnoreCase("CAPTURE"))) {
					response = "status='ERROR', message='Entered transactionType is not correct";
				} else if (!transDetails.getAmount().equals(comdLinePojo.getAmount())) {
					response = "status='ERROR', message='Entered amount is not correct";
				} else if (!transDetails.getCurrency().equalsIgnoreCase(comdLinePojo.getCurrency())) {
					response = "status='ERROR', message='Entered Currency Does not match with record";
				} else if (transDetails.getStatus().equalsIgnoreCase("REVERSED")) {
					response = "status='ERROR', message='Order is Cancelled, please register again to proceed";
				} else if (transDetails.getStatus().equalsIgnoreCase("AUTHORISED")) {
					transDetalRepo.updateAuthStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
					response = "capture: status='SUCCESS'";
				} else
					response = "status='ERROR', message='Entered order details not found";
			} catch (Exception e) {
				response = e.getMessage();
				logger.error(e.getMessage());
			}
		} else
			response = " status='ERROR', message='Invalid currency " + comdLinePojo.getCurrency()
					+ " or payment Method - " + comdLinePojo.getPayMethod();
		return response;
	}

	// To cancel the transaction by changing status to REVERSED
	@Override
	public String reverseTransaction(CommandLinePojo comdLinePojo) {
		logger.info("inside reverseTransaction()");
		String response = null;
		try {
			TransactionDetails transDetails = findByorder(comdLinePojo);
			String tranStatus = transDetails.getStatus();

			if (!transDetails.getAmount().equals(comdLinePojo.getAmount())) {
				response = "status='ERROR', message='Entered amount is not correct";
			} else if (!transDetails.getCurrency().equalsIgnoreCase(comdLinePojo.getCurrency())) {
				response = "status='ERROR', message='Entered Currency Does not match with record";
			} else if (tranStatus.equalsIgnoreCase("REVERSED")) {
				return "status='ERROR', message='Order is Cancelled, please register again to proceed";
			} else {
				transDetalRepo.reverseTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
				return "reverse: status='SUCCESS'";
			}
		} catch (Exception e) {
			response = e.getMessage();
			logger.error(e.getMessage());
		}
		return response;
	}

	@Override
	public String findPendingTransactions(CommandLinePojo comdLinePojo) {
		logger.info("inside findPendingTransactions");
		String response = null;
		try {
			Gson gson = new Gson();
			response = gson.toJson(transDetalRepo.findPendingTransations(comdLinePojo.getClientId()));

			if (response == null) {
				response = "status='ERROR', message='NO pending transactions";
			}
		} catch (Exception e) {
			response = e.getMessage();
			logger.error(e.getMessage());
		}
		return response;
	}

	@Override
	public String findTotalofSuccTransaction(CommandLinePojo comdLinePojo) {
		logger.info("inside findTotal");
		String response = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			// convert String to LocalDate
			LocalDate strDate = LocalDate.parse(comdLinePojo.getStrDate(), formatter);
			LocalDate endDate = LocalDate.parse(comdLinePojo.getEndDate(), formatter);
			response = transDetalRepo.findTotalAmont(comdLinePojo.getClientId(), strDate, endDate).toString();

		} catch (Exception e) {
			response = e.getMessage();
			logger.error(e.getMessage());
		}

		return response;
	}

}
