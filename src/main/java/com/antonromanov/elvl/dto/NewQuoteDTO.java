package com.antonromanov.elvl.dto;

import com.antonromanov.elvl.model.Quote;
import lombok.Data;

@Data
public class NewQuoteDTO implements ResponseDTO{
	private String isin;
	private Double bid;
	private Double ask;
	private Double elvls;

	public NewQuoteDTO(Quote quote, Double elvls) {
		this.isin = quote.getIsin();
		this.bid = quote.getBid();
		this.ask = quote.getAsk();
		this.elvls = elvls;
	}
}
