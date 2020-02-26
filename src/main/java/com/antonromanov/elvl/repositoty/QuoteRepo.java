package com.antonromanov.elvl.repositoty;


import com.antonromanov.elvl.model.Quote;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface QuoteRepo extends JpaRepository<Quote, Integer>{

	/*@Query(value="select w from Quote w where w.ac = false and (w.realized = false or w.realized is null) order by w.priority ASC ")
	List<Quote> getAllNotInArchive();*/
}
