package com.paymentprovider;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentproviderApplication implements ApplicationRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentproviderApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentproviderApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
      

       

        boolean containsOption = args.containsOption(" ");
       
        
                
        
        
    }
	}


