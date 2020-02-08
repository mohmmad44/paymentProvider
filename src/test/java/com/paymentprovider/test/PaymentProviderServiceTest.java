package com.paymentprovider.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.paymentprovider.common.Constants;
import com.paymentprovider.controller.PaymentProviderController;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.PaymentProviderException;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.serviceImpl.PaymentProviderImpl;
import com.paymentprovider.util.PaymentProviderUtil;

public class PaymentProviderServiceTest {

	@InjectMocks
	private PaymentProviderImpl paymentProviderService;

	@Mock
	private PaymentProviderUtil paymentProviderUtil;

	@Mock
	private TransactionDetailsRepository transDetailRepo;

	@Mock
	PaymentProviderException paymentProviderException;

	@Mock
	PaymentProviderController paymentProviderController;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
	TransactionDetails transDetailsDbMock = PaymentProviderServiceTest.ibeClientTransactionDetailsPojoObject();

	@Test
	public void aRegisterNewTransactionTest() throws PaymentProviderException {

		System.out.println("Inside registerNewTransactionSuccessTest");

		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		TransactionDetails transReturn = new TransactionDetails();
		when(transDetailRepo.save(any(TransactionDetails.class))).thenReturn(transReturn);
		assertEquals(Constants.REGISTER.concat(Constants.SUCCESS),
				paymentProviderService.registerNewTransaction(transDetails));

	}

	@Test
	public void aRegisterNewTransactionNegativeTest() throws PaymentProviderException {
		transDetails.setCurrency("INR");
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		TransactionDetails transReturn = new TransactionDetails();
		when(transDetailRepo.save(any(TransactionDetails.class))).thenReturn(transReturn);
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR),
				paymentProviderService.registerNewTransaction(transDetails));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void aRegisterNewTransactionExceptionTest() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.registerNewTransaction(transDetails));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void aRegisterNewTransactionExceptionTest2() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class)))
				.thenThrow(NoSuchElementException.class);
		assertEquals(null, paymentProviderService.registerNewTransaction(transDetails));

	}

	@Test
	public void aRegisterNewTransactionNegativeTest2() throws PaymentProviderException {
		TransactionDetails transDetails2 = new TransactionDetails();
		transDetails2.setStatus((Constants.AUTHORISE));
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetails2);
		assertEquals("order already exists in the database",
				paymentProviderService.registerNewTransaction(transDetails));

	}

	@Test
	public void authoriseTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.AUTHORISE);

		/*
		 * Negative Test, Db return value mock as null
		 */
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		assertEquals(Constants.ERROR.concat(Constants.ORDERIDERROR),
				paymentProviderService.authoriseTransaction(transDetails));

		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);

		/*
		 * Negative Test, command line transaction type set as REGISTER
		 */
		transDetails.setTransactionType(Constants.REGISTER);
		assertEquals(Constants.ERROR.concat(Constants.TRANSTYPEERROR),
				paymentProviderService.authoriseTransaction(transDetails));

		/*
		 * Negative Test, command line Amount value is given false
		 */
		transDetails.setTransactionType(Constants.AUTHORISE);
		transDetails.setAmount(150);
		assertEquals(Constants.ERROR.concat(Constants.AMOUNTERROR),
				paymentProviderService.authoriseTransaction(transDetails));

		/*
		 * Negative Test, Transaction status in Db is other than REGISTERED
		 */
		transDetails.setAmount(250);
		transDetailsDbMock.setStatus(Constants.CAPTURED);
		assertEquals(Constants.STATUSERROR, paymentProviderService.authoriseTransaction(transDetails));

		/*
		 * Negative Test, Given CommandLine Currency is not supported
		 */
		transDetails.setTransactionType(Constants.AUTHORISE);
		transDetails.setCurrency("INR");
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR),
				paymentProviderService.authoriseTransaction(transDetails));

		/*
		 * SUCCESSFULL AUTHORISED transaction
		 */
		transDetails.setCurrency("EUR");
		Integer transactionDetailsDb = 1;
		transDetailsDbMock.setStatus(Constants.REGISTERED);
		when(transDetailRepo.updateRegiStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.AUTHORISE.concat(Constants.SUCCESS),
				paymentProviderService.authoriseTransaction(transDetails));

	}

	@Test
	public void captureTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.CAPTURE);
		/*
		 * Negative Test, Db return value set as null
		 */
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		assertEquals(Constants.ERROR.concat(Constants.ORDERIDERROR),
				paymentProviderService.captureTransaction(transDetails));

		/*
		 * Negative Test, command line transaction type set as REGISTER
		 */
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetails.setTransactionType(Constants.REGISTER);
		assertEquals(Constants.ERROR.concat(Constants.TRANSTYPEERROR),
				paymentProviderService.captureTransaction(transDetails));

		/*
		 * Negative Test command line Amount value is given false
		 */
		transDetails.setTransactionType(Constants.CAPTURE);
		transDetails.setAmount(150);
		assertEquals(Constants.ERROR.concat(Constants.AMOUNTERROR),
				paymentProviderService.captureTransaction(transDetails));

		/*
		 * Negative Test Given CommandLine Currency is not supported
		 */
		transDetails.setAmount(250);
		transDetails.setCurrency("INR");
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR),
				paymentProviderService.captureTransaction(transDetails));

		/*
		 * Negative Test Transaction status in Db is other than REGISTERED
		 */
		transDetails.setCurrency("EUR");
		transDetailsDbMock.setStatus(Constants.REVERSED);
		assertEquals(Constants.ERROR.concat(Constants.CANCELEEDORDER),
				paymentProviderService.captureTransaction(transDetails));

		transDetailsDbMock.setStatus(Constants.CAPTURED);
		assertEquals(Constants.ERROR.concat(Constants.STATUSERROR),
				paymentProviderService.captureTransaction(transDetails));

		/*
		 * SUCCESSFULL AUTHORISED transaction
		 */
		Integer transactionDetailsDb = 1;
		transDetailsDbMock.setStatus(Constants.AUTHORISED);
		when(transDetailRepo.updateAuthStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.CAPTURE.concat(Constants.SUCCESS),
				paymentProviderService.captureTransaction(transDetails));

	}

	@Test
	public void reverseTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.REVERSE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);

		/*
		 * Negative Test command line Amount value is given false
		 */
		transDetails.setAmount(150);
		assertEquals(Constants.ERROR.concat(Constants.AMOUNTERROR),
				paymentProviderService.reverseTransaction(transDetails));

		/*
		 * Negative Test Given CommandLine Currency is not supported
		 */
		transDetails.setAmount(250);
		transDetails.setCurrency("INR");
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR),
				paymentProviderService.reverseTransaction(transDetails));

		/*
		 * Negative Test Transaction status in Db is other than REGISTERED
		 */
		transDetails.setCurrency("EUR");
		transDetailsDbMock.setStatus(Constants.REVERSED);
		assertEquals(Constants.ERROR.concat(Constants.CANCELEEDORDER),
				paymentProviderService.reverseTransaction(transDetails));

		/*
		 * SUCCESSFULL AUTHORISED transaction
		 */
		Integer transactionDetailsDb = 1;
		transDetailsDbMock.setStatus(Constants.AUTHORISED);
		when(transDetailRepo.updateAuthStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.REVERSE.concat(Constants.SUCCESS),
				paymentProviderService.reverseTransaction(transDetails));

	}

	@Test
	public void findPendingTransactions() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		List<TransactionDetails> transactionDetailsDb = new ArrayList<TransactionDetails>();
		when(transDetailRepo.findPendingTransations(transDetails.getClientId())).thenReturn(transactionDetailsDb);
		Gson gson = new Gson();
		String response = gson.toJson(transactionDetailsDb);
		assertEquals(response, paymentProviderService.findPendingTransactions(transDetails));

	}

	public static CommandLinePojo ibeClientCommandLinePojoObject() {
		CommandLinePojo transDetails = new CommandLinePojo();
		transDetails.setAmount(250);
		transDetails.setClientId("IBE");
		transDetails.setCurrency("EUR");
		transDetails.setOrderId("book-37847");
		transDetails.setPayMethod("CARD");
		transDetails.setPayTokenId("cc-367b9832f657b01");
		transDetails.setTransactionType("REGISTER");
		return transDetails;
	}

	public static TransactionDetails ibeClientTransactionDetailsPojoObject() {
		TransactionDetails transDetails = new TransactionDetails();
		transDetails.setAmount(250);
		transDetails.setClientId("IBE");
		transDetails.setCurrency("EUR");
		transDetails.setPayMethod("CARD");
		transDetails.setTransactionType("AUTHORISE");
		return transDetails;
	}

}