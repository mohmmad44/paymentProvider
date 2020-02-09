package com.paymentprovider.common;

public interface Constants {
	public static final String ORDERIDERROR = " , message='orderId not found";
	public static final String PAYMENTTYPEERROR = " , message='Entered PAYMENT TYPE Does not SUPPORT";
	public static final String TRANSTYPEERROR = " ,message='Entered transactionType is not correct";
	public static final String AMOUNTERROR = " message='Entered amount is not correct";
	public static final String CURRENCYERROR = ", message='Entered Currency Does not match";
	public static final String CANCELEEDORDER = ", message='Order is Cancelled, please register again to proceed";
	public static final String REGISTERED = "REGISTERED";
	public static final String AUTHORISED = "AUTHORISED";
	public static final String CAPTURED = "CAPTURED";
	public static final String REVERSED = "REVERSED";
	public static final String LOCALDATE = "yyyy-MM-dd";

	public static final String REGISTER = "REGISTER";
	public static final String AUTHORISE = "AUTHORISE";
	public static final String CAPTURE = "CAPTURE";
	public static final String REVERSE = "REVERSE";

	public static final String ERROR = "status='ERROR'";
	public static final String EntityNotFoundException = " NO data found in the records with the given orderId";
	public static final String RollbackException = " OrderId is already exist in the records";
	public static final String PersistenceException = " Error while updating the Record";

	public static final String SUCCESS = ": status='SUCCESS'";
	public static final String STATUSERROR = "status='ERROR', message='transaction status is not in expected state";
}
