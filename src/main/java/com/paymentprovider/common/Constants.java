package com.paymentprovider.common;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface Constants {

	public static String ORDERIDERROR = " , message='orderId not found";
	public static String PAYMENTTYPEERROR = " , message='Entered PAYMENT TYPE Does not SUPPORT";
	public static String TRANSTYPEERROR = " ,message='Entered transactionType is not correct";
	public static String AMOUNTERROR = " message='Entered amount is not correct";
	public static String CURRENCYERROR = ", message='Entered Currency Does not match";
	public static String CANCELEEDORDER = ", message='Order is Cancelled, please register again to proceed";
	public static String REGISTERED = "REGISTERED";
	public static String AUTHORISED = "AUTHORISED";
	public static String CAPTURED = "CAPTURED";
	public static String REVERSED = "REVERSED";
	public static String LOCALDATE = "yyyy-MM-dd";

	public static String REGISTER = "REGISTERE";
	public static String AUTHORISE = "AUTHORISE";
	public static String CAPTURE = "CAPTURE";
	public static String REVERSE = "REVERSE";

	public static String ERROR = "status='ERROR'";

	public static String SUCCESS = "status='SUCCESS'";
	public static String STATUSERROR = "status='ERROR', message='transaction status is not in expected state";
}
