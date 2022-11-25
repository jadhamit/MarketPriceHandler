package com.demo.marketpricehandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MarketPriceHandlerApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(MarketPriceHandlerApplication.class, args);
	}
	
	
	/*Configuring Rest Template bean for making API calls*/
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
