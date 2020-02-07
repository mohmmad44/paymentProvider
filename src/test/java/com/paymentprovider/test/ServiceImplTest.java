package com.paymentprovider.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceImplTest {

	@Autowired
//	@Mock
	PaymentProviderImpl payImpl;

	@Autowired
//	@Mock
	TransactionDetailsRepository transDetalRepo;

	public static void main(String[] args) {

		Result result = JUnitCore.runClasses(ServiceImplTest.class);
	}

	public static CommandLinePojo zIbeRegisterobject() {
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

	public static CommandLinePojo zIbmRegisterobject() {
		CommandLinePojo transDetails = new CommandLinePojo();

		transDetails.setAmount(150);
		transDetails.setClientId("IBM");
		transDetails.setCurrency("USD");
		transDetails.setOrderId("book-37747");
		transDetails.setPayMethod("CASH");
		transDetails.setPayTokenId("ca-367b9732g657b01");
		transDetails.setTransactionType("REGISTER");

		return transDetails;

	}

	public static CommandLinePojo zMdmRegisterobject() {
		CommandLinePojo transDetails = new CommandLinePojo();

		transDetails.setAmount(50);
		transDetails.setClientId("MDM");
		transDetails.setCurrency("GBP");
		transDetails.setOrderId("book-37689");
		transDetails.setPayMethod("INVOICE");
		transDetails.setPayTokenId("in-367g5832i657g01");
		transDetails.setTransactionType("REGISTER");

		return transDetails;

	}

	@Test
//	@Order
	public void aRegisterNewTransactionSuccess() {

		System.out.println("Inside registerNewTransactionSuccess");

		try {

			CommandLinePojo transDetails = ServiceImplTest.zIbeRegisterobject();

			assertTrue("Currency Format Success", (transDetails.getCurrency().equals("EUR")
					|| transDetails.getCurrency().equals("USD") || transDetails.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails.getPayMethod().equals("CARD")
					|| transDetails.getPayMethod().equals("INVOICE") || transDetails.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb = payImpl.findByorder(transDetails);

			assertTrue(transDetails.getTransactionType().equalsIgnoreCase(Constants.REGISTER)
					&& (transDetailsDb == null || transDetailsDb.getStatus().equalsIgnoreCase(Constants.REVERSED)));

			// when(transDetalRepo.save(any(TransactionDetails.class))).thenReturn(new
			// TransactionDetails());

			payImpl.registerNewTransaction(transDetails);

			// assertThat(saved.getOrderId()).isSameAs(transDetails.getOrderId());

			/*
			 * 
			 */

			CommandLinePojo transDetails2 = ServiceImplTest.zIbmRegisterobject();

			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb1 = payImpl.findByorder(transDetails2);

			assertTrue(transDetails.getTransactionType().equalsIgnoreCase(Constants.REGISTER)
					&& (transDetailsDb1 == null || transDetailsDb1.getStatus().equalsIgnoreCase(Constants.REVERSED)));

			payImpl.registerNewTransaction(transDetails2);

			/*
			 * 
			 */

			CommandLinePojo transDetails3 = ServiceImplTest.zMdmRegisterobject();

			assertTrue("Currency Format Success", (transDetails3.getCurrency().equals("EUR")
					|| transDetails3.getCurrency().equals("USD") || transDetails3.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails3.getPayMethod().equals("CARD")
					|| transDetails3.getPayMethod().equals("INVOICE") || transDetails3.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb3 = payImpl.findByorder(transDetails3);

			assertTrue(transDetails3.getTransactionType().equalsIgnoreCase(Constants.REGISTER)
					&& (transDetailsDb3 == null || transDetailsDb3.getStatus().equalsIgnoreCase(Constants.REVERSED)));

			payImpl.registerNewTransaction(transDetails3);

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
//	@Order(2)
	public void bRegisterNewTransactionFailure() {

		System.out.println("Inside registerNewTransactionFailure");

		try {

			/*
			 * 
			 */

			CommandLinePojo transDetails = ServiceImplTest.zIbeRegisterobject();

			transDetails.setCurrency("INR");

			assertFalse("Currency Format Success", (transDetails.getCurrency().equals("EUR")
					|| transDetails.getCurrency().equals("USD") || transDetails.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails.getPayMethod().equals("CARD")
					|| transDetails.getPayMethod().equals("INVOICE") || transDetails.getPayMethod().equals("CASH")));

			// when(transDetalRepo.save(Mockito.any(TransactionDetails.class))).thenAnswer(i
			// -> i.getArguments()[0]);

			CommandLinePojo transDetails2 = ServiceImplTest.zIbmRegisterobject();

			transDetails2.setPayMethod("CHECK");

			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertFalse("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			/*
			 * 
			 */

			CommandLinePojo transDetails3 = ServiceImplTest.zMdmRegisterobject();

			transDetails3.setTransactionType("REVERSE");

			assertTrue("Currency Format Success", (transDetails3.getCurrency().equals("EUR")
					|| transDetails3.getCurrency().equals("USD") || transDetails3.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails3.getPayMethod().equals("CARD")
					|| transDetails3.getPayMethod().equals("INVOICE") || transDetails3.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb3 = payImpl.findByorder(transDetails3);

			assertFalse(transDetails3.getTransactionType().equalsIgnoreCase(Constants.REGISTER)
					&& (transDetailsDb3 == null || transDetailsDb3.getStatus().equalsIgnoreCase(Constants.REVERSED)));

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
//	@Order(3)
	public void cAuthoriseTransactionTestSuccess() {

		System.out.println("Inside authoriseTransactionTestSuccess");

		try {

			/*
			 * 
			 */
			CommandLinePojo transDetails = ServiceImplTest.zIbeRegisterobject();

			transDetails.setTransactionType("AUTHORISE");

			assertTrue("Currency Format Success", (transDetails.getCurrency().equals("EUR")
					|| transDetails.getCurrency().equals("USD") || transDetails.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails.getPayMethod().equals("CARD")
					|| transDetails.getPayMethod().equals("INVOICE") || transDetails.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb = payImpl.findByorder(transDetails);

			assertTrue((transDetails.getTransactionType().equalsIgnoreCase(Constants.AUTHORISE))
					&& (transDetails.getAmount().equals(transDetailsDb.getAmount())
							&& (transDetails.getCurrency().equalsIgnoreCase(transDetailsDb.getCurrency())
									&& transDetailsDb.getStatus().equalsIgnoreCase(Constants.REGISTERED))));

			payImpl.authoriseTransaction(transDetails);

			/*
			 * 
			 */

			CommandLinePojo transDetails2 = ServiceImplTest.zIbmRegisterobject();

			transDetails2.setTransactionType("AUTHORISE");

			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb1 = payImpl.findByorder(transDetails2);

			assertTrue((transDetails2.getTransactionType().equalsIgnoreCase(Constants.AUTHORISE))
					&& (transDetails2.getAmount().equals(transDetailsDb1.getAmount())
							&& (transDetails2.getCurrency().equalsIgnoreCase(transDetailsDb1.getCurrency())
									&& transDetailsDb1.getStatus().equalsIgnoreCase(Constants.REGISTERED))));

			payImpl.authoriseTransaction(transDetails2);

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/*
	 * 
	 */

	@Test
//	@Order(4)
	public void dFindByorderTestSuccess1() {
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

	/*
	 * 
	 */
	@Test
//	@Order(5)
	public void eFindByorderTestSuccess2() {
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

	/*
	 * 
	 */
	@Test
//	@Order(6)
	public void fFindByorderTestSuccess3() {
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

	/*
	 * 
	 */
	@Test
//	@Order(7)
	public void gFindByorderTestFailure1() {
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

	/*
	 * 
	 */
	@Test
//	@Order(8)
	public void hFindByorderTestFailure2() {
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

	/*
	 * 
	 */
	@Test
//	@Order(9)
	public void iFindByorderTestFailure3() {
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

	/*
	 * 
	 */
	@Test
//	@Order(10)
	public void jCaptureTransactionTestSuccess() {

		System.out.println("Inside captureTransactionTestSuccess");

		try {
			CommandLinePojo transDetails2 = ServiceImplTest.zIbmRegisterobject();
			transDetails2.setTransactionType("CAPTURE");
			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb = payImpl.findByorder(transDetails2);

			assertTrue((transDetails2.getTransactionType().equalsIgnoreCase(Constants.CAPTURE))
					&& transDetailsDb.getStatus().equalsIgnoreCase(Constants.AUTHORISED));

			payImpl.authoriseTransaction(transDetails2);

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
//	@Order(11)
	public void kReverseTransactionTestSuccess() {

		System.out.println("Inside reverseTransactionTestSuccess");

		try {
			CommandLinePojo transDetails3 = ServiceImplTest.zMdmRegisterobject()

			transDetails3.setTransactionType("REVERSE");

			assertTrue("Currency Format Success", (transDetails3.getCurrency().equals("EUR")
					|| transDetails3.getCurrency().equals("USD") || transDetails3.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails3.getPayMethod().equals("CARD")
					|| transDetails3.getPayMethod().equals("INVOICE") || transDetails3.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb = payImpl.findByorder(transDetails3);

			assertTrue((transDetails3.getTransactionType().equalsIgnoreCase(Constants.REVERSE))
					&& (transDetailsDb.getAmount().equals(transDetailsDb.getAmount())
							&& (transDetailsDb.getCurrency().equalsIgnoreCase(transDetailsDb.getCurrency())
									&& (transDetailsDb.getStatus().equalsIgnoreCase(Constants.AUTHORISED)
											|| transDetailsDb.getStatus().equalsIgnoreCase(Constants.REGISTERED)))));
			payImpl.reverseTransaction(transDetails3);

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
//	@Order(12)
	public void lReverseTransactionTestFailure() {

		System.out.println("Inside reverseTransactionTestFailure");

		try {
			CommandLinePojo transDetails3 = ServiceImplTest.zMdmRegisterobject();
			transDetails3.setTransactionType("REVERSE");

			assertTrue("Currency Format Success", (transDetails3.getCurrency().equals("EUR")
					|| transDetails3.getCurrency().equals("USD") || transDetails3.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails3.getPayMethod().equals("CARD")
					|| transDetails3.getPayMethod().equals("INVOICE") || transDetails3.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb = payImpl.findByorder(transDetails3);

			assertFalse((transDetails3.getTransactionType().equalsIgnoreCase(Constants.REVERSE))
					&& (transDetailsDb.getAmount().equals(transDetailsDb.getAmount())
							&& (transDetailsDb.getCurrency().equalsIgnoreCase(transDetailsDb.getCurrency())
									&& (transDetailsDb.getStatus().equalsIgnoreCase(Constants.AUTHORISED)
											|| transDetailsDb.getStatus().equalsIgnoreCase(Constants.REGISTERED)))));
			payImpl.reverseTransaction(transDetails3);

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
//	@Order(13)
	public void mAuthoriseTransactionsTestFailure() {

		System.out.println("Inside authoriseTransactionsTestFailure");

		try {
			CommandLinePojo transDetails = ServiceImplTest.zIbeRegisterobject();

			transDetails.setTransactionType("AUTHORISE");

			assertTrue("Currency Format Success", (transDetails.getCurrency().equals("EUR")
					|| transDetails.getCurrency().equals("USD") || transDetails.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails.getPayMethod().equals("CARD")
					|| transDetails.getPayMethod().equals("INVOICE") || transDetails.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb = payImpl.findByorder(transDetails);

			assertFalse((transDetails.getTransactionType().equalsIgnoreCase(Constants.AUTHORISE))
					&& (transDetails.getAmount().equals(transDetailsDb.getAmount())
							&& (transDetails.getCurrency().equalsIgnoreCase(transDetailsDb.getCurrency())
									&& transDetailsDb.getStatus().equalsIgnoreCase(Constants.REGISTERED))));

			CommandLinePojo transDetails2 = ServiceImplTest.zIbmRegisterobject();

			transDetails2.setTransactionType("AUTHORISE");

			assertTrue("Currency Format Success", (transDetails2.getCurrency().equals("EUR")
					|| transDetails2.getCurrency().equals("USD") || transDetails2.getCurrency().equals("GBP")));

			assertTrue("payMethod Format Success", (transDetails2.getPayMethod().equals("CARD")
					|| transDetails2.getPayMethod().equals("INVOICE") || transDetails2.getPayMethod().equals("CASH")));

			TransactionDetails transDetailsDb1 = payImpl.findByorder(transDetails2);

			assertFalse((transDetails2.getTransactionType().equalsIgnoreCase(Constants.AUTHORISE))
					&& (transDetails2.getAmount().equals(transDetailsDb1.getAmount())
							&& (transDetails2.getCurrency().equalsIgnoreCase(transDetailsDb1.getCurrency())
									&& transDetailsDb1.getStatus().equalsIgnoreCase(Constants.REGISTERED))));

		} catch (PaymentProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
//	@Order(14)
	public void nFindPendingTransactionTestSuccess() {

		System.out.println("Inside findPendingTransactionTestSuccess");

		CommandLinePojo transDetails = ServiceImplTest.zIbeRegisterobject();

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
//	@Order(15)
	public void oFindTotalofSuccTransactionTestSuccess() {

		System.out.println("Inside findTotalofSuccTransactionTestSuccess");

		try {
			Integer amount = transDetalRepo.findTotalAmont("IBM", java.time.LocalDate.now(), java.time.LocalDate.now());

			assertTrue(amount.equals(150));

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			transDetalRepo.deleteTransaction("IBE", "book-37847");
			transDetalRepo.deleteTransaction("IBM", "book-37747");
			transDetalRepo.deleteTransaction("MDM", "book-37689");
		}

	}

}
