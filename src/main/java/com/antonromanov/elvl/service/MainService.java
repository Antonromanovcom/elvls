package com.antonromanov.elvl.service;

import com.antonromanov.elvl.dto.QuoteDto;
import com.antonromanov.elvl.model.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.util.Pair;

public interface MainService {
	List<Quote> findAllQuotes();
	Pair<Quote, Double> addQuote(QuoteDto dto);
	Optional<Elvls> findAllElvlsByIsin(String isin);
	List<Elvls> findAllElvls();
}
