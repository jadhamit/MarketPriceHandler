package com.demo.marketpricehandler.dto;

import java.math.BigDecimal;


/* MaketPrice Details DTO for capturing the 
 * details
 */
public class MarketPriceDetails {
	
	private String id;
	private BigDecimal bidPrice;
	private BigDecimal askPrice;
	
	public BigDecimal getAskPrice() {
		return askPrice;
	}
	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}
	public BigDecimal getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String toString() {
		return new StringBuilder("ID:").append(this.getId()).append("| Ask Price:").append(this.getAskPrice()).append("| Bid Price:").append(this.getBidPrice()).toString();
	}

}
