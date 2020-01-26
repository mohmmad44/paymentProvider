package com.paymentprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.paymentprovider.controller.PaymentProviderController;

@SpringBootApplication
public class PaymentproviderApplication implements ApplicationRunner {

	@Autowired
	PaymentProviderController controller;

	public static void main(String[] args) {
		SpringApplication.run(PaymentproviderApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String response = controller.paymentProvider(args);
		System.out.println(response);
	}

}
