package com.antonromanov.arnote.repositoty;

import com.antonromanov.arnote.model.SearchCriteria;
import com.antonromanov.arnote.model.Quote;
import java.util.List;

public interface IUserDAO {
	List<Quote> searchWish(List<SearchCriteria> params);
}
