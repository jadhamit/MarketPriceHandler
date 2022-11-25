package com.demo.marketpricehandler.consumers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.demo.marketpricehandler.dto.MarketPriceDetails;

@ExtendWith(MockitoExtension.class)
public class MarketPriceMessageConsumerTest {

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private MarketPriceMessageConsumer marketPriceMessageConsumer;
	
	@Captor
	private ArgumentCaptor <HttpEntity<MarketPriceDetails>> marketPriceDetailsCaptor;
	
	
	@Test
	public void testOnMessage_SingleMessage() {
		
		when(restTemplate.exchange(
				Mockito.eq("http://localhost:8080/publish/prices"), Mockito.eq(HttpMethod.POST) , marketPriceDetailsCaptor.capture() , Mockito.eq(String.class))).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
		
		marketPriceMessageConsumer.onMessage("106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001");
		
		assertNotNull(marketPriceDetailsCaptor.getValue());
		
		List<MarketPriceDetails> updatedMarketPriceDetails = (List<MarketPriceDetails>) marketPriceDetailsCaptor.getValue().getBody();
		
		assertTrue(updatedMarketPriceDetails.size() == 1);
		
		assertEquals(updatedMarketPriceDetails.get(0).getAskPrice(), BigDecimal.valueOf(1.099));
		assertEquals(updatedMarketPriceDetails.get(0).getBidPrice(), BigDecimal.valueOf(1.201));
		assertEquals(updatedMarketPriceDetails.get(0).getId(), "106");
	}
	
	
	@Test
	public void testOnMessage_MultiMessage() {
		
		when(restTemplate.exchange(
				Mockito.eq("http://localhost:8080/publish/prices"), Mockito.eq(HttpMethod.POST) , marketPriceDetailsCaptor.capture() , Mockito.eq(String.class))).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
		
		marketPriceMessageConsumer.onMessage("106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001 \n 107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002");
		
		assertNotNull(marketPriceDetailsCaptor.getValue());
		
		List<MarketPriceDetails> updatedMarketPriceDetails = (List<MarketPriceDetails>) marketPriceDetailsCaptor.getValue().getBody();
		
		assertTrue(updatedMarketPriceDetails.size() == 2);
		
		assertEquals(updatedMarketPriceDetails.get(0).getAskPrice(), BigDecimal.valueOf(1.099));
		assertEquals(updatedMarketPriceDetails.get(0).getBidPrice(), BigDecimal.valueOf(1.201));
		assertEquals(updatedMarketPriceDetails.get(0).getId(), "106");
		
		
		assertEquals(updatedMarketPriceDetails.get(1).getAskPrice(), BigDecimal.valueOf(119.599));
		assertEquals(updatedMarketPriceDetails.get(1).getBidPrice(), BigDecimal.valueOf(119.901));
		assertEquals(updatedMarketPriceDetails.get(1).getId(), "107");
	}
	
	
	
	
}
