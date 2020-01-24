package com.paymentprovider;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paymentprovider.model.CommandLinePojo;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.service.PaymentProviderService;

@SpringBootApplication
public class PaymentproviderApplication implements ApplicationRunner {

	@Autowired
	PaymentProviderService ppService;

	public static void main(String[] args) {
		SpringApplication.run(PaymentproviderApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		String method = args.getNonOptionArgs().get(0).toString();
		Class<CommandLinePojo> aClass = CommandLinePojo.class;
		Field[] fields = aClass.getDeclaredFields();

		JsonObject json = new JsonObject();

		for (Field field : fields) {
			field.setAccessible(true);
			String value = args.getOptionValues(field.getName()) != null ? args.getOptionValues(field.getName()).get(0)
					: null;
			json.addProperty(field.getName(), value);
		}

		Gson gson = new Gson();
		String payload = gson.toJson(json);
		System.out.println(payload);
		ObjectMapper mapper = new ObjectMapper();
		CommandLinePojo comdLinePojo = mapper.readValue(payload, CommandLinePojo.class);

		
/*
		if (comdLinePojo.getCurrency().equals("EUR") || comdLinePojo.getCurrency().equals("USD")
				|| comdLinePojo.getCurrency().equals("GBP")) {

		} else
			return "currency not supported";

		if (comdLinePojo.getCurrency().equals("EUR") || comdLinePojo.getCurrency().equals("USD")
				|| comdLinePojo.getCurrency().equals("GBP")) {

		} else
			return "currency not supported";
			
*/			

		switch (method) {
		case "register":
			String newTrans = ppService.registerNewTransaction(comdLinePojo);
			System.out.println(newTrans);
			break;

		case "authorise":
			String authorise = ppService.authoriseTransaction(comdLinePojo);
			System.out.println(authorise);
			break;

		case "capture":
			String capture = ppService.captureTransaction(comdLinePojo);
			System.out.println(capture);
			break;

		case "reverse":
			String reverse = ppService.reverseTransaction(comdLinePojo);
			System.out.println(reverse);
			break;

		case "findByOrder":
			TransactionDetails findByOrder = ppService.findByorder();
			System.out.println(findByOrder);
			break;
		case "findPending":
			TransactionDetails pendingTrans = ppService.findPendingTransactions();
			System.out.println(pendingTrans);
			break;

		case "findTotal":
			Integer findTotal = ppService.findTotalofSuccTransaction(comdLinePojo);
			System.out.println(findTotal);
			break;

		}
	}
}
