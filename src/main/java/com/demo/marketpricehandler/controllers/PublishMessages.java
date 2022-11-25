package com.demo.marketpricehandler.controllers;

import java.util.List;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.marketpricehandler.dto.MarketPriceDetails;

@RestController
@RequestMapping("/publish")
public class PublishMessages {

	@Autowired
	private Queue queue;

	@Autowired
	private JmsTemplate jmsTemplate;

	/*
	 * We will accept the messages from UI in this API and submit the message
	 * to the queue.
	*/
	@PostMapping("/messages")
	public void sendMessages(@RequestBody String message) {
		
		jmsTemplate.convertAndSend(queue, message);
		
	}
	
	/*
	 * We will accept the updated prices send from JMS listeners and send it back to UI from this API.
	*/
	@PostMapping("/prices")
	public List<MarketPriceDetails> sendMarketPricesDetails(@RequestBody List<MarketPriceDetails> marketPriceDetails) {
		
		return marketPriceDetails;
		
	}
	
}


