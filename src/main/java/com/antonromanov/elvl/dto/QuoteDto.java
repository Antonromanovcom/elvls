package com.antonromanov.elvl.dto;

import lombok.Data;

@Data
public class QuoteDto {
	private String isin;
	private Double bid;
	private Integer ask;
}
