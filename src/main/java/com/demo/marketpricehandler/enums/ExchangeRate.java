package com.demo.marketpricehandler.enums;

/* Exchange Rate Enum we can use for maintaining 
 * the rate's in future as well.
 */
public enum ExchangeRate {
	
	COMMISSON_RATE(0.1);
	
	private double rate;
	
	private ExchangeRate(double rate) {
		this.rate = rate;
	}
	
	public double getRate() {
		return rate;
	}

}
