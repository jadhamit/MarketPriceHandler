package com.demo.marketpricehandler.consumers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.demo.marketpricehandler.dto.MarketPriceDetails;
import com.demo.marketpricehandler.enums.ExchangeRate;

@Component
public class MarketPriceMessageConsumer {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Logger logger = LoggerFactory.getLogger(MarketPriceMessageConsumer.class);
	
	/*
	 * We will listen to the queue specified in the destination attribute.
	 * Once we get the message on the queue this method will be called.
	 */
	@JmsListener(destination = "marketprice.request.queue")
	public void onMessage(String message) {
		logger.info("MarketPrice Message received ::{}", message);
		//List will contain the MarketPrices with Commission added or subtracted.
		List<MarketPriceDetails> marketDetailsWithCommission = new ArrayList<>();

		if (StringUtils.hasText(message)) {
			Stream.of(message.split("\n")).forEach(splittedMessage -> {
				MarketPriceDetails marketPriceDetails = new MarketPriceDetails();
				String[] messageTokens = splittedMessage.split(",");

				marketPriceDetails.setId(StringUtils.trimWhitespace(messageTokens[0]));
				marketPriceDetails.setAskPrice(deductCommission(messageTokens[2], ExchangeRate.COMMISSON_RATE.getRate()));
				marketPriceDetails.setBidPrice(addCommission(messageTokens[3], ExchangeRate.COMMISSON_RATE.getRate()));

				marketDetailsWithCommission.add(marketPriceDetails);
			});

			// Rest call to send the updated market prices

			logger.debug("Updated Market Prices::{}", marketDetailsWithCommission);

			publishNewPrices(marketDetailsWithCommission);
		}
	}

	private void publishNewPrices(List<MarketPriceDetails> marketDetailsWithCommission) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<List<MarketPriceDetails>> entity = new HttpEntity<>(marketDetailsWithCommission,headers);
	      
	    restTemplate.exchange(
	         "http://localhost:8080/publish/prices", HttpMethod.POST, entity, String.class);
	}

	private BigDecimal deductCommission(String price, double margin) {
		return BigDecimal.valueOf(Double.valueOf(price)).subtract(BigDecimal.valueOf(margin/100));
	}

	private BigDecimal addCommission(String price, double margin) {
		return BigDecimal.valueOf(Double.valueOf(price)).add(BigDecimal.valueOf(margin/100));
	}

}
