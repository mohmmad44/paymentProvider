//package com.paymentprovider.test;
//
//import java.util.List;
//import java.util.Set;
//
//import org.apache.catalina.core.ApplicationContext;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import com.paymentprovider.PaymentproviderApplication;
//import com.paymentprovider.model.PaymentProviderException;
//
//public class PaymentProviderControllerTest {
//	
//	@MockBean
//	private ApplicationArguments args;
//	
//	
//	@Before
//	public void setupMock() {
//		MockitoAnnotations.initMocks(this);
//	}
//	
//	@Autowired
//	ApplicationContext ctx;
//
//    @Test
//    public void testRun() {
//    	ApplicationRunner runner =  ctx.getBean(ApplicationRunner.class);
//        runner.run ( "-k", "arg1", "-i", "arg2");
//        
//        SpringApplication.run(PaymentproviderApplication.class, args);
//    }
//	
//	
//	@Test
//	public void paymentProviderTest() throws PaymentProviderException {
//		Mockito.when(args.getNonOptionArgs().get(0)).thenReturn("register");
//		Mockito.when(args.getOptionValues(field.getName())).thenReturn("register");
//		
//		
//	}
//	
////	ApplicationArguments aa = new ApplicationArguments() {
////		
////		@Override
////		public String[] getSourceArgs() {
////			// TODO Auto-generated method stub
////			return null;
////		}
////		
////		@Override
////		public List<String> getOptionValues(String name) {
////			// TODO Auto-generated method stub
////			return null;
////		}
////		
////		@Override
////		public Set<String> getOptionNames() {
////			// TODO Auto-generated method stub
////			return null;
////		}
////		
////		@Override
////		public List<String> getNonOptionArgs() {
////			// TODO Auto-generated method stub
////			return null;
////		}
////		
////		@Override
////		public boolean containsOption(String name) {
////			// TODO Auto-generated method stub
////			return false;
////		}
////	};
//
//}
