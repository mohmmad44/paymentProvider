package com.paymentprovider.controller;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paymentprovider.PaymentproviderApplication;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.service.PaymentProviderService;

@Controller
public class PaymentProviderController {

	@Autowired
	PaymentProviderService ppService;

	private final Logger logger = LoggerFactory.getLogger(PaymentproviderApplication.class);

	public String paymentProvider(ApplicationArguments args) throws Exception {

		String response = "";

		try {
			String method = args.getNonOptionArgs().get(0).toString();
			Class<CommandLinePojo> aClass = CommandLinePojo.class;
			Field[] fields = aClass.getDeclaredFields();

			JsonObject json = new JsonObject();

			for (Field field : fields) {
				field.setAccessible(true);
				String value = args.getOptionValues(field.getName()) != null
						? args.getOptionValues(field.getName()).get(0)
						: null;
				json.addProperty(field.getName(), value);
			}

			Gson gson = new Gson();
			String payload = gson.toJson(json);

			ObjectMapper mapper = new ObjectMapper();
			CommandLinePojo comdLinePojo = mapper.readValue(payload, CommandLinePojo.class);

			if (!(comdLinePojo.getCurrency().equals("EUR") || comdLinePojo.getCurrency().equals("USD")
					|| comdLinePojo.getCurrency().equals("GBP"))) {
				logger.info("Input Currency format is not supported");
				response = " status='ERROR', message='Invalid Currency - " + comdLinePojo.getCurrency();
				return response;
			}

			if (!(comdLinePojo.getPayMethod().equals("CARD") || comdLinePojo.getPayMethod().equals("INVOICE")
					|| comdLinePojo.getPayMethod().equals("CASH"))) {
				logger.info("Input payMethod format is not supported");
				response = " status='ERROR', message='Invalid payment method - " + comdLinePojo.getCurrency();
				return response;
			}

			switch (method) {
			case "register":
				return ppService.registerNewTransaction(comdLinePojo);

			case "authorise":
				return ppService.authoriseTransaction(comdLinePojo);

			case "capture":
				return ppService.captureTransaction(comdLinePojo);

			case "reverse":
				return ppService.reverseTransaction(comdLinePojo);

			case "findByOrder":
				TransactionDetails transactionDetail = ppService.findByorder(comdLinePojo);
				return gson.toJson(transactionDetail);

			case "findPending":
				return ppService.findPendingTransactions(comdLinePojo);

			case "findTotal":
				return ppService.findTotalofSuccTransaction(comdLinePojo);

			}

		} catch (Exception e) {
			response = e.getLocalizedMessage();
		}
		return response;
	}

}
