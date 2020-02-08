package com.paymentprovider.serviceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.paymentprovider.PaymentproviderApplication;
import com.paymentprovider.common.Constants;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.PaymentProviderException;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.service.PaymentProviderService;
import com.paymentprovider.util.PaymentProviderUtil;

@Service
@Transactional
public class PaymentProviderImpl implements PaymentProviderService {

	@Autowired
	TransactionDetailsRepository transDetalRepo;

	@Autowired
	PaymentProviderUtil paymentProviderUtil;

	Logger logger = LoggerFactory.getLogger(PaymentproviderApplication.class);

	/*
	 * find the transactions from the data base for the given clientId and orderId;
	 * 
	 * @see com.paymentprovider.service.PaymentProviderService#findByorder(com.
	 * paymentprovider.model.CommandLinePojo)
	 */

	@Override
	public TransactionDetails findByorder(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		logger.info("inside findByOrder");
		return paymentProviderUtil.findByorder(comdLinePojo);
	}

	/*
	 * method to verify only supported Currency and PaymentType
	 */

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

	/*
	 * register the transaction by satisfying the conditions i.e, if orderId not
	 * exist or orderId status should be Reversed
	 * 
	 * @see
	 * com.paymentprovider.service.PaymentProviderService#registerNewTransaction(com
	 * .paymentprovider.model.CommandLinePojo)
	 */

	@Override
	public String registerNewTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		logger.info("inside registerNewTransation");
		String response = null;
		boolean variblesCheck = variablesCheck(comdLinePojo);
		if (variblesCheck) {

			TransactionDetails transDetails = new TransactionDetails();
			try {
				TransactionDetails transDetailsDb = paymentProviderUtil.findByorder(comdLinePojo);
				if (comdLinePojo.getTransactionType().equalsIgnoreCase(Constants.REGISTER) && (null == transDetailsDb
						|| transDetailsDb.getStatus().equalsIgnoreCase(Constants.REVERSED))) {
					transDetails.setAmount(comdLinePojo.getAmount());
					transDetails.setClientId(comdLinePojo.getClientId());
					transDetails.setCurrency(comdLinePojo.getCurrency());
					transDetails.setDate(java.time.LocalDate.now());
					transDetails.setOrderId(comdLinePojo.getOrderId());
					transDetails.setPayMethod(comdLinePojo.getPayMethod());
					transDetails.setPayTokenId(comdLinePojo.getPayTokenId());
					transDetails.setTransactionType(comdLinePojo.getTransactionType());
					transDetails.setStatus(Constants.REGISTERED);
					transDetalRepo.save(transDetails);
					response = Constants.REGISTER.concat(Constants.SUCCESS);
				} else
					response = "order already exists in the database";

			} catch (NullPointerException e) {
				logger.error(e.getMessage());
				return e.getLocalizedMessage();
			} catch (Exception e) {
				logger.error(e.getMessage());
				return e.getLocalizedMessage();
			}
		} else
			response = Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR);
		return response;
	}

	/*
	 * Authorize the transaction by satisfying the conditions i.e, orderId object
	 * should not be null & status should be Registered
	 * 
	 * @see
	 * com.paymentprovider.service.PaymentProviderService#authoriseTransaction(com.
	 * paymentprovider.model.CommandLinePojo)
	 */

	@Override
	public String authoriseTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		logger.info("inside authoriseTransation()");
		String response = null;

		boolean variblesCheck = variablesCheck(comdLinePojo);
		if (variblesCheck) {
			try {
				TransactionDetails transDetails = findByorder(comdLinePojo);

				if (transDetails == null) {
					response = Constants.ERROR.concat(Constants.ORDERIDERROR);
				} else if (!(comdLinePojo.getTransactionType().equalsIgnoreCase(Constants.AUTHORISE))) {
					response = Constants.ERROR.concat(Constants.TRANSTYPEERROR);
				} else if (!(transDetails.getAmount().equals(comdLinePojo.getAmount()))) {
					response = Constants.ERROR.concat(Constants.AMOUNTERROR);
				} else if (transDetails.getStatus().equalsIgnoreCase(Constants.REGISTERED)) {
					transDetalRepo.updateRegiStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
					response = Constants.AUTHORISE.concat(Constants.SUCCESS);
				} else
					response = Constants.STATUSERROR;
			} catch (NullPointerException e) {
				logger.error(e.getMessage());
				return e.getLocalizedMessage();
			} catch (Exception e) {
				logger.error(e.getMessage());
				return e.getLocalizedMessage();
			}
		} else
			response = Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR);
		return response;
	}

	/*
	 * Capture the transaction by satisfying the conditions
	 * 
	 * @see
	 * com.paymentprovider.service.PaymentProviderService#captureTransaction(com.
	 * paymentprovider.model.CommandLinePojo)
	 */

	@Override
	public String captureTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		logger.info("inside captureTransaction()");
		String response = null;

		boolean variblesCheck = variablesCheck(comdLinePojo);
		if (variblesCheck) {
			try {
				TransactionDetails transDetails = findByorder(comdLinePojo);

				if (transDetails == null) {
					response = Constants.ERROR.concat(Constants.ORDERIDERROR);
				} else if (!(comdLinePojo.getTransactionType().equalsIgnoreCase(Constants.CAPTURE))) {
					response = Constants.ERROR.concat(Constants.TRANSTYPEERROR);
				} else if (!transDetails.getAmount().equals(comdLinePojo.getAmount())) {
					response = Constants.ERROR.concat(Constants.AMOUNTERROR);
				} else if (transDetails.getStatus().equalsIgnoreCase(Constants.REVERSED)) {
					response = Constants.ERROR.concat(Constants.CANCELEEDORDER);
				} else if (transDetails.getStatus().equalsIgnoreCase(Constants.AUTHORISED)) {
					transDetalRepo.updateAuthStatus(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
					response = Constants.CAPTURE.concat(Constants.SUCCESS);
				} else
					response = Constants.ERROR.concat(Constants.STATUSERROR);
			} catch (NullPointerException e) {
				logger.error(e.getMessage());
				return e.getLocalizedMessage();
			} catch (Exception e) {
				logger.error(e.getMessage());
				return e.getLocalizedMessage();
			}
		} else
			response = Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR);
		return response;
	}

	/*
	 * 
	 * To cancel the transaction by changing status to REVERSED
	 * com.paymentprovider.service.PaymentProviderService#reverseTransaction(com.
	 * paymentprovider.model.CommandLinePojo)
	 */
	@Override
	public String reverseTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		logger.info("inside reverseTransaction()");
		String response = null;
		try {
			TransactionDetails transDetails = findByorder(comdLinePojo);
			String tranStatus = transDetails.getStatus();

			if (!transDetails.getAmount().equals(comdLinePojo.getAmount())) {
				response = Constants.ERROR.concat(Constants.AMOUNTERROR);
			} else if (!transDetails.getCurrency().equalsIgnoreCase(comdLinePojo.getCurrency())) {
				response = Constants.ERROR.concat(Constants.CURRENCYERROR);
			} else if (tranStatus.equalsIgnoreCase(Constants.REVERSED)) {
				return Constants.ERROR.concat(Constants.CANCELEEDORDER);
			} else {
				transDetalRepo.reverseTransaction(comdLinePojo.getClientId(), comdLinePojo.getOrderId());
				return Constants.REVERSE.concat(Constants.SUCCESS);
			}
		} catch (NullPointerException e) {
			logger.error(e.getMessage());
			return e.getLocalizedMessage();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return e.getLocalizedMessage();
		}
		return response;
	}

	/*
	 * find pending transactions for a given ClientId
	 * 
	 * @see
	 * com.paymentprovider.service.PaymentProviderService#findPendingTransactions(
	 * com.paymentprovider.model.CommandLinePojo)
	 */

	@Override
	public String findPendingTransactions(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		logger.info("inside findPendingTransactions");
		String response = null;
		try {
			Gson gson = new Gson();
			response = gson.toJson(transDetalRepo.findPendingTransations(comdLinePojo.getClientId()));

		} catch (Exception e) {
			logger.error(e.getMessage());
			return e.getLocalizedMessage();
		}
		return response;
	}

	/*
	 * total amount of captured transactions for a given clientId and given time
	 * period
	 * 
	 * @see
	 * com.paymentprovider.service.PaymentProviderService#findTotalofSuccTransaction
	 * (com.paymentprovider.model.CommandLinePojo)
	 */

	@Override
	public String findTotalofSuccTransaction(CommandLinePojo comdLinePojo) throws PaymentProviderException {
		logger.info("inside findTotal");
		String response = null;
		Integer amount = 0;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.LOCALDATE);
			LocalDate strDate = LocalDate.parse(comdLinePojo.getStrDate(), formatter);
			LocalDate endDate = LocalDate.parse(comdLinePojo.getEndDate(), formatter);
			amount = transDetalRepo.findTotalAmont(comdLinePojo.getClientId(), strDate, endDate);
			response = amount != null ? amount.toString() : null;

		} catch (NullPointerException e) {
			logger.error(e.getMessage());
			return e.getLocalizedMessage();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return e.getLocalizedMessage();
		}
		return response;
	}

}
