package com.antonromanov.elvl.dto;

import com.antonromanov.elvl.model.Quote;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class QuoteListDTO implements ResponseDTO{
	private List<Quote> list;
}
