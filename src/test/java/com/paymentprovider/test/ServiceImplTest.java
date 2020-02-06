package com.paymentprovider.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.paymentprovider.common.Constants;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.PaymentProviderException;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.repository.TransactionDetailsRepository;
import com.paymentprovider.serviceImpl.PaymentProviderImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@Rollback(false)
@Transactional
public class ServiceImplTest {

	@Autowired
	PaymentProviderImpl payImpl;

	@Autowired
	TransactionDetailsRepository transDetalRepo;

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(ServiceImplTest.class);
		ServiceImplTest test = new ServiceImplTest();
		test.registerNewTransactionSuccess();
		test.findByorderTestSuccess1();
		test.findByorderTestSuccess2();
		test.findByorderTestSuccess3();
		test.findByorderTestFailure1();
		test.findByorderTestFailure2();
		test.findByorderTestFailure3();
		test.authoriseTransactionTest1Success();
		test.captureTransactionTestSuccess();
		test.reverseTransactionTestSuccess();
		test.findTotalofSuccTransactionTestSuccess();
		test.findPendingTransactionTestSuccess();
		

	}

	@Test
	public void registerNewTransactionSuccess() {

		System.out.println("Inside registerNewTransactionSuccess");

		try {

			TransactionDetails transDetails = new TransactionDetails();

			transDetails.setAmount(250);
			transDetails.setClientId("IBE");
			transDetails.setCurrency("EUR");
			transDetails.setDate(java.time.LocalDate.now());
			transDetails.setOrderId("book-37847");
			transDetails.setPayMethod("CARD");
			transDetails.setPayTokenId("cc-367b9832f657b01");
			transDetails.setTransactionType("REGISTER");
			transDetails.setStatus(Constants.REGISTERED);

			assertTrue("Currency Format Success", (transDetails.getCurrency().equals("EUR")
					|| transDetails.getCurrency().equals("USD") || transDetails.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails.getPayMethod().equals("CARD")
					|| transDetails.getPayMethod().equals("INVOICE") || transDetails.getPayMethod().equals("CASH")));

			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("IBM");
			pojo.setOrderId("book-37747");

			TransactionDetails transDetailsDb = payImpl.findByorder(pojo);

			assertTrue(transDetails.getTransactionType().equalsIgnoreCase(Constants.REGISTER)
					&& (transDetailsDb == null || transDetailsDb.getStatus().equalsIgnoreCase(Constants.REVERSED)));

			transDetalRepo.save(transDetails);
			// when(transDetalRepo.save(Mockito.any(TransactionDetails.class))).thenAnswer(i
			// -> i.getArguments()[0]);

			TransactionDetails transDetails2 = new TransactionDetails();

			transDetails2.setAmount(150);
			transDetails2.setClientId("IBM");
			transDetails2.setCurrency("USD");
			transDetails2.setDate(java.time.LocalDate.now());
			transDetails2.setOrderId("book-37747");
			transDetails2.setPayMethod("CASH");
			transDetails2.setPayTokenId("ca-367b9732g657b01");
			transDetails2.setTransactionType("REGISTER");
			transDetails2.setStatus(Constants.REGISTERED);

			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			CommandLinePojo pojo1 = new CommandLinePojo();
			pojo1.setClientId("IBM");
			pojo1.setOrderId("book-37747");

			TransactionDetails transDetailsDb1 = payImpl.findByorder(pojo1);

			assertTrue(transDetails.getTransactionType().equalsIgnoreCase(Constants.REGISTER)
					&& (transDetailsDb1 == null || transDetailsDb1.getStatus().equalsIgnoreCase(Constants.REVERSED)));

			transDetalRepo.save(transDetails2);

			TransactionDetails transDetails3 = new TransactionDetails();

			transDetails3.setAmount(50);
			transDetails3.setClientId("MDM");
			transDetails3.setCurrency("GBP");
			transDetails3.setDate(java.time.LocalDate.now());
			transDetails3.setOrderId("book-37689");
			transDetails3.setPayMethod("INVOICE");
			transDetails3.setPayTokenId("in-367g5832i657g01");
			transDetails3.setTransactionType("REGISTER");
			transDetails3.setStatus(Constants.REGISTERED);

			assertTrue("Currency Format Success", (transDetails3.getCurrency().equals("EUR")
					|| transDetails3.getCurrency().equals("USD") || transDetails3.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails3.getPayMethod().equals("CARD")
					|| transDetails3.getPayMethod().equals("INVOICE") || transDetails3.getPayMethod().equals("CASH")));

			CommandLinePojo pojo3 = new CommandLinePojo();
			pojo3.setClientId("MDM");
			pojo3.setOrderId("book-37689");

			TransactionDetails transDetailsDb3 = payImpl.findByorder(pojo3);

			assertTrue(transDetails3.getTransactionType().equalsIgnoreCase(Constants.REGISTER)
					&& (transDetailsDb3 == null || transDetailsDb3.getStatus().equalsIgnoreCase(Constants.REVERSED)));

			transDetalRepo.save(transDetails3);

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void findByorderTestSuccess1() {
		System.out.println("Inside findByorderTest");

		try {
			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("IBE");
			pojo.setOrderId("book-37847");

			TransactionDetails checkval = payImpl.findByorder(pojo);
			assertNotNull(checkval);
		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void findByorderTestSuccess2() {
		System.out.println("Inside findByorderTest");

		try {
			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("IBM");
			pojo.setOrderId("book-37747");

			TransactionDetails checkval = payImpl.findByorder(pojo);
			assertNotNull(checkval);
		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void findByorderTestSuccess3() {
		System.out.println("Inside findByorderTest");

		try {
			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("MDM");
			pojo.setOrderId("book-37689");

			TransactionDetails checkval = payImpl.findByorder(pojo);
			assertNotNull(checkval);
		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void findByorderTestFailure1() {
		System.out.println("Inside findByorderTestFailure");

		try {
			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("KJH");
			pojo.setOrderId("book-37689");

			TransactionDetails checkval = payImpl.findByorder(pojo);
			assertNull(checkval);
		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void findByorderTestFailure2() {
		System.out.println("Inside findByorderTestFailure");

		try {
			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("asd");
			pojo.setOrderId("book-37258");

			TransactionDetails checkval = payImpl.findByorder(pojo);
			assertNull(checkval);
		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void findByorderTestFailure3() {
		System.out.println("Inside findByorderTestFailure");

		try {
			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("GTF");
			pojo.setOrderId("book-58947");

			TransactionDetails checkval = payImpl.findByorder(pojo);
			assertNull(checkval);
		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void authoriseTransactionTest1Success() {

		System.out.println("Inside authoriseTransactionTestSuccess");

		try {
			TransactionDetails transDetails = new TransactionDetails();

			transDetails.setAmount(250);
			transDetails.setClientId("IBE");
			transDetails.setCurrency("EUR");
			transDetails.setOrderId("book-37847");
			transDetails.setPayMethod("CARD");
			transDetails.setPayTokenId("cc-367b9832f657b01");
			transDetails.setTransactionType("AUTHORISE");

			assertTrue("Currency Format Success", (transDetails.getCurrency().equals("EUR")
					|| transDetails.getCurrency().equals("USD") || transDetails.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails.getPayMethod().equals("CARD")
					|| transDetails.getPayMethod().equals("INVOICE") || transDetails.getPayMethod().equals("CASH")));

			CommandLinePojo pojo = new CommandLinePojo();
			pojo.setClientId("IBE");
			pojo.setOrderId("book-37847");

			TransactionDetails transDetailsDb = payImpl.findByorder(pojo);

			assertTrue((transDetails.getTransactionType().equalsIgnoreCase(Constants.AUTHORISE))
					&& (transDetails.getAmount().equals(transDetailsDb.getAmount())
							&& (transDetails.getCurrency().equalsIgnoreCase(transDetailsDb.getCurrency())
									&& transDetailsDb.getStatus().equalsIgnoreCase(Constants.REGISTERED))));

			transDetalRepo.updateRegiStatus("IBE", "book-37847");

			TransactionDetails transDetails2 = new TransactionDetails();
			transDetails2.setAmount(150);
			transDetails2.setClientId("IBM");
			transDetails2.setCurrency("USD");
			transDetails2.setOrderId("book-37747");
			transDetails2.setPayMethod("CASH");
			transDetails2.setPayTokenId("ca-367b9732g657b01");
			transDetails2.setTransactionType("AUTHORISE");

			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			CommandLinePojo pojo1 = new CommandLinePojo();
			pojo1.setClientId("IBM");
			pojo1.setOrderId("book-37747");

			TransactionDetails transDetailsDb1 = payImpl.findByorder(pojo1);

			assertTrue((transDetails2.getTransactionType().equalsIgnoreCase(Constants.AUTHORISE))
					&& (transDetails2.getAmount().equals(transDetailsDb1.getAmount())
							&& (transDetails2.getCurrency().equalsIgnoreCase(transDetailsDb1.getCurrency())
									&& transDetailsDb1.getStatus().equalsIgnoreCase(Constants.REGISTERED))));

			transDetalRepo.updateRegiStatus("IBM", "book-37747");

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void captureTransactionTestSuccess() {

		System.out.println("Inside captureTransactionTestSuccess");

		try {
			TransactionDetails transDetails2 = new TransactionDetails();
			transDetails2.setAmount(150);
			transDetails2.setClientId("IBM");
			transDetails2.setCurrency("USD");
			transDetails2.setOrderId("book-37747");
			transDetails2.setPayMethod("CASH");
			transDetails2.setPayTokenId("ca-367b9732g657b01");
			transDetails2.setTransactionType("CAPTURE");

			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			CommandLinePojo pojo1 = new CommandLinePojo();
			pojo1.setClientId("IBM");
			pojo1.setOrderId("book-37747");

			TransactionDetails transDetailsDb = payImpl.findByorder(pojo1);

			assertTrue((transDetails2.getTransactionType().equalsIgnoreCase(Constants.CAPTURE))
					&& (transDetails2.getAmount().equals(transDetailsDb.getAmount())
							&& (transDetails2.getCurrency().equalsIgnoreCase(transDetailsDb.getCurrency())
									&& transDetailsDb.getStatus().equalsIgnoreCase(Constants.AUTHORISED))));

			transDetalRepo.updateAuthStatus("IBM", "book-37747");

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void reverseTransactionTestSuccess() {

		System.out.println("Inside reverseTransactionTestSuccess");

		try {
			TransactionDetails transDetails3 = new TransactionDetails();

			transDetails3.setAmount(50);
			transDetails3.setClientId("MDM");
			transDetails3.setCurrency("GBP");
			transDetails3.setOrderId("book-37689");
			transDetails3.setPayMethod("INVOICE");
			transDetails3.setPayTokenId("in-367g5832i657g01");
			transDetails3.setTransactionType("REVERSE");

			assertTrue("Currency Format Success", (transDetails3.getCurrency().equals("EUR")
					|| transDetails3.getCurrency().equals("USD") || transDetails3.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails3.getPayMethod().equals("CARD")
					|| transDetails3.getPayMethod().equals("INVOICE") || transDetails3.getPayMethod().equals("CASH")));

			CommandLinePojo pojo3 = new CommandLinePojo();
			pojo3.setClientId("MDM");
			pojo3.setOrderId("book-37689");

			TransactionDetails transDetailsDb = payImpl.findByorder(pojo3);

			assertTrue((transDetails3.getTransactionType().equalsIgnoreCase(Constants.REVERSE))
					&& (transDetailsDb.getAmount().equals(transDetailsDb.getAmount())
							&& (transDetailsDb.getCurrency().equalsIgnoreCase(transDetailsDb.getCurrency())
									&& (transDetailsDb.getStatus().equalsIgnoreCase(Constants.AUTHORISED)
											|| transDetailsDb.getStatus().equalsIgnoreCase(Constants.REGISTERED)))));
			transDetalRepo.reverseTransaction("MDM", "book-37689");

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	public void findPendingTransactionTestSuccess() {

		System.out.println("Inside findPendingTransactionTestSuccess");

		TransactionDetails transDetails = new TransactionDetails();

		transDetails.setAmount(250);
		transDetails.setClientId("IBE");
		transDetails.setCurrency("EUR");
		transDetails.setOrderId("book-37847");
		transDetails.setPayMethod("CARD");
		transDetails.setPayTokenId("cc-367b9832f657b01");

		CommandLinePojo pojo = new CommandLinePojo();
		pojo.setClientId("IBE");
		pojo.setOrderId("book-37847");

		List<TransactionDetails> transDetailsDb = transDetalRepo.findPendingTransations("IBE");

		for (TransactionDetails transDetailsDb1 : transDetailsDb) {
			if (transDetailsDb1.getOrderId().equalsIgnoreCase("book-37847")) {
				assertTrue((transDetailsDb1.getAmount().equals(transDetails.getAmount()))
						&& (transDetailsDb1.getCurrency().equalsIgnoreCase(transDetails.getCurrency()))
						&& (transDetailsDb1.getPayMethod().equalsIgnoreCase(transDetails.getPayMethod()))
						&& (transDetailsDb1.getPayTokenId().equalsIgnoreCase(transDetails.getPayTokenId())));

			}
		}
	}

	@Test
	public void findTotalofSuccTransactionTestSuccess() {

		System.out.println("Inside findTotalofSuccTransactionTestSuccess");

		
		try {
			Integer amount = transDetalRepo.findTotalAmont("IBM", java.time.LocalDate.now(), java.time.LocalDate.now());

			assertTrue(amount.equals(150));
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			transDetalRepo.deleteTransaction("IBE", "book-37847");
			transDetalRepo.deleteTransaction("IBM", "book-37747");
			transDetalRepo.deleteTransaction("MDM", "book-37689");
		}
		

	}

	
	
	

}
