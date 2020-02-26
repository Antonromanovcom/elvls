package com.antonromanov.elvl.service;

import com.antonromanov.elvl.exceptions.BadIncomeParameter;
import com.antonromanov.elvl.model.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;


public interface MainService {

	ResponseParseResult parseCsv(MultipartFile file, LocalUser localUser) throws Exception;

	void clearCounter();

	List<Quote> getAllWishes();

	List<Quote> getAllWishesWithPriority1(LocalUser user);

	int getMaxPriority(LocalUser user);
	int getMinPriority(LocalUser user);

	List<WishDTOList> getAllWishesWithGroupPriority(LocalUser user);

	void updateWish(Quote log);

	Quote updateAndFlushWish(Quote log);

	Quote addWish(Quote parseJsonToWish);

	Optional<Quote> getWishById(int id);

	Integer getSumm4All(LocalUser user);

	Integer getSumm4Prior(LocalUser user);

	Optional<Integer> getImplementedSum(LocalUser user, int period);

	Salary saveSalary(Salary salary);

	Salary getLastSalary(LocalUser localUser);

	Integer calculateImplementationPeriod(Integer summ, LocalUser localUser);

	List<Quote> getAllWishesByUserId(LocalUser user);

	Optional<List<Quote>> findAllWishesByWish(String Wish, LocalUser user);

	Optional<List<Quote>> getAllRealizedWishes(LocalUser user);

	Quote updateMonthGroup(Quote wish) throws BadIncomeParameter;

}
