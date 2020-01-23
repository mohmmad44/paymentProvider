package com.paymentprovider;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentprovider.model.CommandLinePojo;

@SpringBootApplication
public class PaymentproviderApplication implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(PaymentproviderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PaymentproviderApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		System.out.println(args);
		String method = args.getNonOptionArgs().get(0);
		Class<CommandLinePojo> aClass = CommandLinePojo.class;
		Field[] fields = aClass.getDeclaredFields();
		
		
		StringBuffer buffer = new StringBuffer();
		
		for (Field field : fields) {
			field.setAccessible(true);
			String value = args.getOptionValues(field.getName()) != null?args.getOptionValues(field.getName()).get(0):null;
			buffer.append("'"+field.getName()+"'"+":"+value+",");
		}
		
		String json = buffer.toString();
		
		int lastCommaIndex = json.lastIndexOf(",");
		json = "{"+json.substring(0, lastCommaIndex)+"}";
		
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		CommandLinePojo readValue = mapper.readValue(json, CommandLinePojo.class);
		System.out.println(readValue.getClientId());
	}
	
	
	
}

