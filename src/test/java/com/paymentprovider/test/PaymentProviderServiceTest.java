package com.paymentprovider.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

	/*
	 * SUCCESSFULL Variable Check
	 */
	@Test
	public void variablesCheckCurrencyTest() throws PaymentProviderException {
		transDetails.setCurrency("GBP");
		assertEquals(true, paymentProviderService.variablesCheck(transDetails));
	}

	@Test
	public void variablesCheckNegativeCurrencyTest() throws PaymentProviderException {
		transDetails.setCurrency("INR");
		assertEquals(false, paymentProviderService.variablesCheck(transDetails));
	}

	@Test
	public void variablesCheckNagativePayMethodTest() throws PaymentProviderException {
		transDetails.setPayMethod("CHECK");
		assertEquals(false, paymentProviderService.variablesCheck(transDetails));
	}

	/*
	 * SUCCESSFULL REGISTER transaction
	 */
	@Test
	public void registerNewTransactionTest() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		when(transDetailRepo.save(any(TransactionDetails.class))).thenReturn(transDetailsDbMock);
		assertEquals(Constants.REGISTER.concat(Constants.SUCCESS),
				paymentProviderService.registerNewTransaction(transDetails));
	}

	@Test
	public void registerNewTransactionNegativeCurrencyTest() throws PaymentProviderException {
		transDetails.setCurrency("INR");
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		when(transDetailRepo.save(any(TransactionDetails.class))).thenReturn(transDetailsDbMock);
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR),
				paymentProviderService.registerNewTransaction(transDetails));
	}

	@Test
	public void registerNewTransactionNegativeStatusTest() throws PaymentProviderException {
		transDetailsDbMock.setStatus((Constants.AUTHORISED));
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		assertEquals("order already exists in the database",
				paymentProviderService.registerNewTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void registerNewTransactionExceptionTest() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.registerNewTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void registerNewTransactionExceptionTest2() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class)))
				.thenThrow(NoSuchElementException.class);
		assertEquals(null, paymentProviderService.registerNewTransaction(transDetails));
	}

	/*
	 * SUCCESSFULL AUTHORISED transaction
	 */
	@Test
	public void authoriseTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.AUTHORISE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		Integer transactionDetailsDb = 1;
		transDetailsDbMock.setStatus(Constants.REGISTERED);
		when(transDetailRepo.updateRegiStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.AUTHORISE.concat(Constants.SUCCESS),
				paymentProviderService.authoriseTransaction(transDetails));
	}

	@Test
	public void authoriseDbReturnNullNegtiveTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.AUTHORISE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		assertEquals(Constants.ERROR.concat(Constants.ORDERIDERROR),
				paymentProviderService.authoriseTransaction(transDetails));
	}

	@Test
	public void authoriseFalseTransactionTypeNegtiveTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.AUTHORISE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetails.setTransactionType(Constants.REGISTER);
		assertEquals(Constants.ERROR.concat(Constants.TRANSTYPEERROR),
				paymentProviderService.authoriseTransaction(transDetails));
	}

	@Test
	public void authoriseFalseAmountNegtiveTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.AUTHORISE);
		transDetails.setAmount(150);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		assertEquals(Constants.ERROR.concat(Constants.AMOUNTERROR),
				paymentProviderService.authoriseTransaction(transDetails));
	}

	@Test
	public void authoriseTransactionNegtiveDbStatusTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.AUTHORISE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetailsDbMock.setStatus(Constants.CAPTURED);
		assertEquals(Constants.STATUSERROR, paymentProviderService.authoriseTransaction(transDetails));
	}

	@Test
	public void authoriseTransactionFalseCurrencyNegtiveTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.AUTHORISE);
		transDetails.setCurrency("INR");
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR),
				paymentProviderService.authoriseTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authoriseTransactionExceptionTest() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.authoriseTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authoriseTransactionExceptionTest2() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class)))
				.thenThrow(NoSuchElementException.class);
		assertEquals(null, paymentProviderService.authoriseTransaction(transDetails));
	}

	/*
	 * SUCCESSFULL AUTHORISED transaction
	 */
	@Test
	public void captureTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.CAPTURE);
		Integer transactionDetailsDb = 1;
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetailsDbMock.setStatus(Constants.AUTHORISED);
		when(transDetailRepo.updateAuthStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.CAPTURE.concat(Constants.SUCCESS),
				paymentProviderService.captureTransaction(transDetails));

	}

	/*
	 * Negative Test, Db return value set as null
	 */
	@Test
	public void captureTransactionNegativeDbFindTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.CAPTURE);

		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		assertEquals(Constants.ERROR.concat(Constants.ORDERIDERROR),
				paymentProviderService.captureTransaction(transDetails));
	}

	/*
	 * Negative Test, command line transaction type set as REGISTER
	 */
	@Test
	public void captureTransactionNegativeTransactionTypeTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.REGISTER);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		assertEquals(Constants.ERROR.concat(Constants.TRANSTYPEERROR),
				paymentProviderService.captureTransaction(transDetails));
	}

	/*
	 * Negative Test command line Amount value is given false
	 */
	@Test
	public void captureTransactionNegativeAmountTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.CAPTURE);
		transDetails.setAmount(150);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		assertEquals(Constants.ERROR.concat(Constants.AMOUNTERROR),
				paymentProviderService.captureTransaction(transDetails));
	}

	/*
	 * Negative Test Given CommandLine Currency is not supported
	 */
	@Test
	public void captureTransactionNegativeCurrencyTest() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetails.setCurrency("INR");
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR).concat(Constants.PAYMENTTYPEERROR),
				paymentProviderService.captureTransaction(transDetails));
	}

	/*
	 * Negative Test Transaction status in Db is other than REGISTERED
	 */
	@Test
	public void captureTransactionNegativeStatusTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.CAPTURE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetailsDbMock.setStatus(Constants.REVERSED);
		assertEquals(Constants.ERROR.concat(Constants.CANCELEEDORDER),
				paymentProviderService.captureTransaction(transDetails));
	}

	/*
	 * Negative Test Transaction status in Db is other than REGISTERED
	 */
	@Test
	public void captureTransactionNegativeCapturedStatusTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.CAPTURE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetailsDbMock.setStatus(Constants.CAPTURED);
		assertEquals(Constants.ERROR.concat(Constants.STATUSERROR),
				paymentProviderService.captureTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void captureTransactionExceptionTest() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.captureTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void captureTransactionExceptionTest2() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class)))
				.thenThrow(NoSuchElementException.class);
		assertEquals(null, paymentProviderService.captureTransaction(transDetails));
	}

	/*
	 * SUCCESSFULL AUTHORISED transaction
	 */
	@Test
	public void reverseTransactionTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.REVERSE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		Integer transactionDetailsDb = 1;
		transDetailsDbMock.setStatus(Constants.AUTHORISED);
		when(transDetailRepo.updateAuthStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.REVERSE.concat(Constants.SUCCESS),
				paymentProviderService.reverseTransaction(transDetails));
	}

	/*
	 * Negative Test command line Amount value is given false
	 */
	@Test
	public void reverseTransactionNegativeAmountTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.REVERSE);
		transDetails.setAmount(150);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		assertEquals(Constants.ERROR.concat(Constants.AMOUNTERROR),
				paymentProviderService.reverseTransaction(transDetails));
	}

	/*
	 * Negative Test Given CommandLine Currency is not supported
	 */
	@Test
	public void reverseTransactionNegtiveCurrencyTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.REVERSE);
		transDetails.setCurrency("INR");
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR),
				paymentProviderService.reverseTransaction(transDetails));
	}

	/*
	 * Negative Test Transaction status in Db is other than REGISTERED
	 */
	@Test
	public void reverseTransactionNegtiveStatusTest() throws PaymentProviderException {
		transDetails.setTransactionType(Constants.REVERSE);
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetailsDbMock.setStatus(Constants.REVERSED);
		assertEquals(Constants.ERROR.concat(Constants.CANCELEEDORDER),
				paymentProviderService.reverseTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void reverseTransactionExceptionTest() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.reverseTransaction(transDetails));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void reverseTransactionExceptionTest2() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class)))
				.thenThrow(NoSuchElementException.class);
		assertEquals(null, paymentProviderService.reverseTransaction(transDetails));
	}

	/*
	 * find Pending Transactions Test
	 */
	@Test
	public void findPendingTransactions() throws PaymentProviderException {
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		List<TransactionDetails> transactionDetailsDb = new ArrayList<TransactionDetails>();
		when(transDetailRepo.findPendingTransations(transDetails.getClientId())).thenReturn(transactionDetailsDb);
		Gson gson = new Gson();
		String response = gson.toJson(transactionDetailsDb);
		assertEquals(response, paymentProviderService.findPendingTransactions(transDetails));
	}

	
	
	@SuppressWarnings("unchecked")
	@Test
	public void findPendingTransactionsExceptionTest() throws PaymentProviderException {
		Mockito.when(transDetailRepo.findPendingTransations(transDetails.getClientId()))
				.thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.findPendingTransactions(transDetails));
	}
	
	@Test
	public void findTotalofSuccTransactionTest() throws PaymentProviderException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.LOCALDATE);
		LocalDate strDate = LocalDate.parse(transDetails.getStrDate(), formatter);
		LocalDate endDate = LocalDate.parse(transDetails.getEndDate(), formatter);
		Mockito.when(transDetailRepo.findTotalAmont(transDetails.getClientId(), strDate,endDate)).thenReturn(250.00);
		assertEquals("250.0", paymentProviderService.findTotalofSuccTransaction(transDetails));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void findTotalofSuccTransactionExceptionTest() throws PaymentProviderException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.LOCALDATE);
		LocalDate strDate = LocalDate.parse(transDetails.getStrDate(), formatter);
		LocalDate endDate = LocalDate.parse(transDetails.getEndDate(), formatter);
		Mockito.when(transDetailRepo.findTotalAmont(transDetails.getClientId(), strDate,endDate))
				.thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.findTotalofSuccTransaction(transDetails));
		
	}
	

	@SuppressWarnings("unchecked")
	@Test
	public void findTotalofSuccTransactionExceptionTest2() throws PaymentProviderException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.LOCALDATE);
		LocalDate strDate = LocalDate.parse(transDetails.getStrDate(), formatter);
		LocalDate endDate = LocalDate.parse(transDetails.getEndDate(), formatter);
		Mockito.when(transDetailRepo.findTotalAmont(transDetails.getClientId(), strDate,endDate))
				.thenThrow(NoSuchElementException.class);
		assertEquals(null, paymentProviderService.findTotalofSuccTransaction(transDetails));
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
		transDetails.setStrDate("2020-02-09");
		transDetails.setEndDate("2020-02-10");
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