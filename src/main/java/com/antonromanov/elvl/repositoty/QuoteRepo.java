package com.antonromanov.elvl.repositoty;

import com.antonromanov.elvl.model.Quote;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface QuoteRepo extends JpaRepository<Quote, Integer>{
	List<Quote> findAllByIsin(String isin);
}
