package com.antonromanov.elvl.service;

import com.antonromanov.elvl.dto.QuoteDto;
import com.antonromanov.elvl.model.*;
import com.antonromanov.elvl.repositoty.ElvlsRepo;
import com.antonromanov.elvl.repositoty.QuoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MainServiceImpl implements MainService {

	@Autowired
	private QuoteRepo quoteRepo;
	@Autowired
	private ElvlsRepo elvlsRepo;

	@Override
	public List<Quote> findAllQuotes() {
		return quoteRepo.findAll();
	}

	@Override
	public Optional<Elvls> findAllElvlsByIsin(String isin) {
		return elvlsRepo.findByIsin(isin);
	}

	@Override
	public List<Elvls> findAllElvls() {
		return elvlsRepo.findAll();
	}

	@Override
	public Double addQuote(QuoteDto dto) {
		Double elvls =  calculateElvls((findAllElvlsByIsin(dto.getIsin())
				.orElseGet(()-> elvlsRepo.saveAndFlush(new Elvls(dto.getIsin(), dto.getBid())))), dto);
		quoteRepo.save(Quote.$fromDto(dto));
		return elvls;
	}

	private Double calculateElvls(Elvls elvls, QuoteDto dto) {
		Double newElvls = dto.getBid() >= elvls.getElvls() ?  dto.getBid() : dto.getAsk();
		elvls.setElvls(newElvls);
		elvlsRepo.save(elvls);
		return newElvls;
	}
}
