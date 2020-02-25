package com.antonromanov.arnote.repositoty;

import com.antonromanov.arnote.model.LocalUser;
import com.antonromanov.arnote.model.Quote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Quote, Integer>{

	@Query(value="select w from Quote w where w.ac = false and (w.realized = false or w.realized is null) order by w.priority ASC ")
	List<Quote> getAllNotInArchive();


	@Query(value="select w from Quote w where w.ac = false and (w.realized = false or w.realized is null) and w.user = :user order by w.priorityGroup, w.priorityGroupOrder ASC ")
	List<Quote> getAllWithGroupOrder(@Param("user") LocalUser user);

	@Query(value="select w from Quote w where w.ac = false and (w.realized = false or w.realized is null) and w.user = :user order by w.priority ASC ")
	List<Quote> findAllByIdSorted(@Param("user") LocalUser user);



	@Query(value="select w from Quote w where w.ac = false and (w.realized = false or w.realized is null) and w.priority = 1 and  w.user = :user order by w.wish ASC ")
	List<Quote> getAllWithPriority1(@Param("user") LocalUser user);


	/**
	 * Метод для высчитывания среднего времени реализации.
	 *
	 * @param user
	 * @return
	 */
	@Query(value="select w from Quote w where w.ac = false and w.realized = true and  w.user = :user order by w.wish ASC ")
	List<Quote> getAllRealizedWishes(@Param("user") LocalUser user);



	@Query(value="select w from Quote w where w.wish = ?1")
	Optional<List<Quote>> getWishesByName(@Param("wish") String wish);

	@Modifying
	@Transactional
	@Query("delete from Quote w where w.id = ?1")
	void deleteByLongId(Long entityId);

	Optional<Quote> findById(long l);


	@Query(value="select * from arnote.wishes w where w.wish like " +
			"(concat('%',:wish,'%')) and (w.realized = false or w.realized is null ) and user_id = :userId order by w.wish", nativeQuery = true)
	Optional<List<Quote>> findAllByWishAndUser(String wish, long userId);

	// Запросить сумму всех реализованных пользователем желаний за все время
	@Query(value="select sum(p.price) from (select * from arnote.wishes w WHERE " +
			"(w.id NOT IN (311) " +
			"and w.user_id = :userId)) p" +
			" WHERE NOT p.archive AND (p.realized=true)", nativeQuery = true)
	Optional<Integer> getImplementedSum4AllPeriod(long userId);

	// Запросить сумму всех реализованных пользователем желаний за текущий месяц
	@Query(value="select sum(p.price) from (select * from arnote.wishes w WHERE " +
			"(w.id NOT IN (311) " +
			"and w.user_id = :userId " +
			"and extract(month FROM w.realization_date) = extract (month FROM CURRENT_DATE))) p" +
			" WHERE NOT p.archive AND (p.realized=true)", nativeQuery = true)
	Optional<Integer> getImplementedSum4Month(long userId);

}
