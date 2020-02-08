package com.paymentprovider.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

	@Test
	public void aRegisterNewTransactionTest() throws PaymentProviderException {

		System.out.println("Inside registerNewTransactionSuccessTest");

		CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		TransactionDetails transReturn = new TransactionDetails();
		when(transDetailRepo.save(any(TransactionDetails.class))).thenReturn(transReturn);
		assertEquals(Constants.REGISTER.concat(Constants.SUCCESS),
				paymentProviderService.registerNewTransaction(transDetails));

	}

	@Test
	public void aRegisterNewTransactionNegativeTest() throws PaymentProviderException {

		System.out.println("Inside registerNewTransactionSuccessTest");

		CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
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

		System.out.println("Inside registerNewTransactionSuccessTest");

		CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenThrow(NullPointerException.class);
		assertEquals(null, paymentProviderService.registerNewTransaction(transDetails));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void aRegisterNewTransactionExceptionTest2() throws PaymentProviderException {

		System.out.println("Inside registerNewTransactionSuccessTest");

		CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class)))
				.thenThrow(NoSuchElementException.class);
		assertEquals(null, paymentProviderService.registerNewTransaction(transDetails));

	}

	@Test
	public void aRegisterNewTransactionNegativeTest2() throws PaymentProviderException {

		CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
		TransactionDetails transDetails2 = new TransactionDetails();
		transDetails2.setStatus((Constants.AUTHORISE));
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetails2);
		assertEquals("order already exists in the database",
				paymentProviderService.registerNewTransaction(transDetails));

	}

	@Test
	public void authoriseTransactionTest() throws PaymentProviderException {

		CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
		transDetails.setTransactionType(Constants.AUTHORISE);
		TransactionDetails transDetailsDbMock = PaymentProviderServiceTest.ibeClientTransactionDetailsPojoObject();
		Integer transactionDetailsDb = 1;
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		when(transDetailRepo.updateRegiStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.AUTHORISE.concat(Constants.SUCCESS),
				paymentProviderService.authoriseTransaction(transDetails));

	}

	@Test
	public void authoriseTransactionNegtiveTest() throws PaymentProviderException {

		CommandLinePojo transDetails = PaymentProviderServiceTest.ibeClientCommandLinePojoObject();
		transDetails.setTransactionType(Constants.AUTHORISE);
		TransactionDetails transDetailsDbMock = PaymentProviderServiceTest.ibeClientTransactionDetailsPojoObject();
		Integer transactionDetailsDb = 1;
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		when(transDetailRepo.updateRegiStatus(transDetails.getClientId(), transDetails.getOrderId()))
				.thenReturn(transactionDetailsDb);
		assertEquals(Constants.AUTHORISE.concat(Constants.SUCCESS),
				paymentProviderService.authoriseTransaction(transDetails));

		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(null);
		assertEquals(Constants.ERROR.concat(Constants.ORDERIDERROR),
				paymentProviderService.authoriseTransaction(transDetails));

		
		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetails.setTransactionType(Constants.REGISTER);
		assertEquals(Constants.ERROR.concat(Constants.TRANSTYPEERROR),
				paymentProviderService.authoriseTransaction(transDetails));

		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetails.setTransactionType(Constants.AUTHORISE);
		transDetails.setAmount(150);
		assertEquals(Constants.ERROR.concat(Constants.AMOUNTERROR),
				paymentProviderService.authoriseTransaction(transDetails));

//		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
//		transDetails.setAmount(250);
//		transDetails.setCurrency("INR");
//		assertEquals(Constants.ERROR.concat(Constants.CURRENCYERROR),
//				paymentProviderService.authoriseTransaction(transDetails));

		Mockito.when(paymentProviderUtil.findByorder(any(CommandLinePojo.class))).thenReturn(transDetailsDbMock);
		transDetails.setAmount(250);
		transDetailsDbMock.setStatus(Constants.CAPTURED);
		assertEquals(Constants.STATUSERROR, paymentProviderService.authoriseTransaction(transDetails));

		
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
		transDetails.setOrderId("book-37847");
		transDetails.setPayMethod("CARD");
		transDetails.setPayTokenId("cc-367b9832f657b01");
		transDetails.setTransactionType("AUTHORISE");
		transDetails.setStatus("REGISTERED");
		return transDetails;
	}

}