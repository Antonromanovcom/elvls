package com.antonromanov.arnote.repositoty;

import com.antonromanov.arnote.model.SearchCriteria;
import com.antonromanov.arnote.model.Quote;
import com.antonromanov.arnote.utils.WishSearchQueryCriteriaConsumer;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class WishDao implements IUserDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Quote> searchWish(List<SearchCriteria> params) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Quote> query = builder.createQuery(Quote.class);
		Root r = query.from(Quote.class);

		Predicate predicate = builder.conjunction();

		WishSearchQueryCriteriaConsumer searchConsumer = new WishSearchQueryCriteriaConsumer(predicate, builder, r);
		params.stream().forEach(searchConsumer);
		predicate = searchConsumer.getPredicate();
		query.where(predicate);

		List<Quote> result = entityManager.createQuery(query).getResultList();
		return result;
	}

}
