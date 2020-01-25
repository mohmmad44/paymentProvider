package com.paymentprovider.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	TransactionDetails transDetails = new TransactionDetails();

	@Override
	public TransactionDetails findByorder(CommandLinePojo comdLinePojo) throws Exception {

		return transDetails = transDetalRepo.findTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());

	}


	@Override
	public String registerNewTransaction(CommandLinePojo comdLinePojo) throws Exception {

		try {
			TransactionDetails transDetails1 = findByorder(comdLinePojo);

			if (transDetails1 == null || transDetails1.getStatus().equalsIgnoreCase("REVERSED")) {
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
				return "register: status='SUCCESS'";

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "order already exists in the database";

	}

	@Override
	public String authoriseTransaction(CommandLinePojo comdLinePojo) {
		try {
			TransactionDetails transDetails = findByorder(comdLinePojo);
			String tranStatus = transDetails.getStatus();
			System.out.println(tranStatus);

			if (!(transDetails.getAmount().equals(comdLinePojo.getAmount()))) {
				return "Entered amount is not correct";
			} else if ((!transDetails.getCurrency().equalsIgnoreCase(comdLinePojo.getCurrency()))) {
				return "Entered Currency Does not match with record";
			} else if (tranStatus.equalsIgnoreCase("REGISTERED")) {

				int trans = transDetalRepo.updateRegiStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
				return "authorise: status='SUCCESS'";
			} else
				return "transaction is not in REGISTERED state ";

		} catch (Exception e) {
			e.printStackTrace();// logger.error ("My error message");
		}
		return "Entered order details not found";

	}

	@Override
	public String captureTransaction(CommandLinePojo comdLinePojo) {
		try {
			TransactionDetails transDetails = findByorder(comdLinePojo);

			if (transDetails == null) {
				return "orderId not found";
			} else if (!transDetails.getAmount().equals(comdLinePojo.getAmount())) {
				return "Entered amount is not correct";
			} else if (!transDetails.getCurrency().equalsIgnoreCase(comdLinePojo.getCurrency())) {
				return "Entered Currency Does not match with record";
			} else if (transDetails.getStatus().equalsIgnoreCase("REVERSED")) {
				return "Order is Cancelled, please register again to proceed";
			} else if (transDetails.getStatus().equalsIgnoreCase("AUTHORISED")) {
				transDetalRepo.updateAuthStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
				return "capture: status='SUCCESS'";
			} else
				return "transaction is not in AUTHORISED state ";

		} catch (Exception e) {
			// logger.error ("My error message");
		}
		return "Entered order details not found";

	}

	@Override
	public String reverseTransaction(CommandLinePojo comdLinePojo) {

		try {
			TransactionDetails transDetails = findByorder(comdLinePojo);
			String tranStatus = transDetails.getStatus();

			if (!transDetails.getAmount().equals(comdLinePojo.getAmount())) {
				return "Entered amount is not correct";
			} else if (!transDetails.getCurrency().equalsIgnoreCase(comdLinePojo.getCurrency())) {
				return "Entered Currency Does not match with record";
			} else if (tranStatus.equalsIgnoreCase("REVERSED")) {
				return "Order is Cancelled, please register again to proceed";
			} else {
				transDetalRepo.reverseTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
				return "reverse: status='SUCCESS'";
			}
		} catch (Exception e) {
			// logger.error ("My error message");
		}
		return "Entered order details not found";

	}

	@Override
	public List<TransactionDetails> findPendingTransactions(CommandLinePojo comdLinePojo) {
		List<TransactionDetails> pendingtransDetails = transDetalRepo.findPendingTransations(comdLinePojo.getClientId());
		return pendingtransDetails;
	}

	@Override
	public Integer findTotalofSuccTransaction(CommandLinePojo comdLinePojo) {
		Integer total = transDetalRepo.findTotalAmont(comdLinePojo.getClientId(),comdLinePojo.getStrDate(),comdLinePojo.getEndDate());
		return total;
	}

}
