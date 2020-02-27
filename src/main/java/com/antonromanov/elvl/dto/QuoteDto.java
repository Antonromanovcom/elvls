package com.antonromanov.elvl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class QuoteDto {
	private String isin;
	private Double bid;
	private Double ask;
}
