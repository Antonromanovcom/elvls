package com.antonromanov.elvl.service;

import com.antonromanov.elvl.dto.QuoteDto;
import com.antonromanov.elvl.model.*;
import java.util.List;
import java.util.Optional;

public interface MainService {
	List<Quote> findAllQuotes();
	Double addQuote(QuoteDto dto);
	Optional<Elvls> findAllElvlsByIsin(String isin);
	List<Elvls> findAllElvls();
}
