package com.demo.marketpricehandler.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/* Enabling JMS configuration and creating a queues where we can publish 
 * and retrieve messages from the queue for processing.
 */
@EnableJms
@Configuration
public class JmsConfig {
	
	@Bean
	public Queue createQueue() {
		return new ActiveMQQueue("marketprice.request.queue");
	}
}
