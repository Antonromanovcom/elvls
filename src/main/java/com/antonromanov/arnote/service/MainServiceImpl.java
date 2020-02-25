package com.antonromanov.arnote.service;

import au.com.bytecode.opencsv.CSVReader;
import com.antonromanov.arnote.exceptions.BadIncomeParameter;
import com.antonromanov.arnote.model.*;
import com.antonromanov.arnote.repositoty.SalaryRepository;
import com.antonromanov.arnote.repositoty.WishRepository;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static com.antonromanov.arnote.utils.Utils.*;


@Service
public class MainServiceImpl implements MainService {


    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private SalaryRepository salaryRepository;

    Integer addCount = 0; // Количество добавлений

    @Override
    public List<Quote> getAllWishes() {
        return wishRepository.findAll(Sort.by(Sort.Direction.ASC, "priority"));
    }

    @Override
    public List<Quote> getAllWishesWithPriority1(LocalUser user) {
        return wishRepository.getAllWithPriority1(user);
    }

    @Override
    public int getMaxPriority(LocalUser user) {
        List<Quote> wishDTOList = wishRepository.getAllWithGroupOrder(user);
        Comparator<Quote> comparator = Comparator.comparing( Quote::getPriorityGroup);

        if ((wishDTOList.stream().filter(wish -> wish.getPriorityGroup()!=null).max(comparator).isPresent())) {
            return (wishDTOList.stream().filter(wish -> wish.getPriorityGroup() != null).max(comparator).get().getPriorityGroup()) + 1;
        } else {
            return 1;
        }
    }

    @Override
    public int getMinPriority(LocalUser user) {
        List<Quote> wishDTOList = wishRepository.getAllWithGroupOrder(user);
        Comparator<Quote> comparator = Comparator.comparing( Quote::getPriorityGroup);

        if ((wishDTOList.stream().filter(wish -> wish.getPriorityGroup()!=null && wish.getPriorityGroup()!=0).min(comparator).isPresent())) {
            return (wishDTOList.stream().filter(wish -> wish.getPriorityGroup() != null).min(comparator).get().getPriorityGroup()) + 1;
        } else {
            return 1;
        }
    }

    private void addIteminWishDTOListForNullPriorityWishes(List<WishDTOList> wishDTOListGlobal,
                                                   List<WishDTO> wishDTOListFiltered,
                                                   int maxPrior,
                                                   LocalUser user){
        wishDTOListGlobal.add(WishDTOList.builder()
                .wishList(wishDTOListFiltered)
                .monthNumber(computerMonthNumber(maxPrior+1 > 12 ? (maxPrior+1-12) : maxPrior+1))
                .monthName(computerMonth(maxPrior))
                .year(String.valueOf(getCurrentYear(maxPrior)))
                .colspan(2)
                .sum(wishDTOListFiltered.stream().map(WishDTO::getPrice).reduce(0, ArithmeticUtils::addAndCheck))
                .overflow((wishDTOListFiltered.stream().map(WishDTO::getPrice)
                        .reduce(0, ArithmeticUtils::addAndCheck))>getLastSalary(user).getResidualSalary())
                .colorClass(getClassColorByMonth(0, (wishDTOListFiltered.stream().map(WishDTO::getPrice)
                        .reduce(0, ArithmeticUtils::addAndCheck))>getLastSalary(user).getResidualSalary()))
                .expanded(true)
                .build());
    }

    @Override
    public List<WishDTOList> getAllWishesWithGroupPriority(LocalUser user) {

        int maxPrior = getMaxPriority(user);
        List<WishDTOList> wishDTOListGlobal = new ArrayList<>();

        if (maxPrior-1 > 0){
            int currentMonth = 1;
            while (currentMonth < maxPrior){

                int finalCurrentMonth = currentMonth;
                List<WishDTO> wishDTOListFiltered = wishRepository.getAllWithGroupOrder(user)
                        .stream()
                        .filter(wish -> wish.getPriorityGroup()!=null)
                        .filter(wish -> wish.getPriorityGroup()== finalCurrentMonth)
                        .map(w -> prepareWishDTO(w, maxPrior))
                        .collect(Collectors.toList());

                wishDTOListGlobal.add(WishDTOList.builder()
                        .wishList(wishDTOListFiltered)
                        .monthNumber(computerMonthNumber(currentMonth))
                        .monthName(computerMonth(currentMonth))
                        .year(String.valueOf(getCurrentYear(currentMonth)))
                        .colspan(2)
                        .sum(wishDTOListFiltered.stream().map(WishDTO::getPrice).reduce(0, ArithmeticUtils::addAndCheck))
                        .overflow((wishDTOListFiltered.stream().map(WishDTO::getPrice).reduce(0, ArithmeticUtils::addAndCheck))>getLastSalary(user).getResidualSalary())
                        .colorClass(getClassColorByMonth(computerMonthNumber(currentMonth), (wishDTOListFiltered.stream().map(WishDTO::getPrice).reduce(0, ArithmeticUtils::addAndCheck))>getLastSalary(user).getResidualSalary()))
                        .expanded(true)
                        .build());

                currentMonth++;
            }

            List<WishDTO> wishDTOListFiltered = wishRepository.getAllWithGroupOrder(user)
                    .stream()
                    .filter(wish -> wish.getPriorityGroup()==null)
                    .map(w -> prepareWishDTO(w, maxPrior))
                    .collect(Collectors.toList());

            addIteminWishDTOListForNullPriorityWishes(wishDTOListGlobal, wishDTOListFiltered, maxPrior, user);

        } else {

            List<WishDTO> wishDTOListFiltered = wishRepository.getAllWithGroupOrder(user)
                    .stream()
                    .map(w -> prepareWishDTO(w, maxPrior))
                    .collect(Collectors.toList());

            addIteminWishDTOListForNullPriorityWishes(wishDTOListGlobal, wishDTOListFiltered, maxPrior, user);
        }

        return wishDTOListGlobal;
    }

    @Override
    public List<Quote> getAllWishesByUserId(LocalUser user) {
        return wishRepository.findAllByIdSorted(user);
    }

    @Override
    public Optional<List<Quote>> findAllWishesByWish(String wish, LocalUser user) {
        return wishRepository.findAllByWishAndUser(wish, user.getId());
    }

    @Override
    public Optional<List<Quote>> getAllRealizedWishes(LocalUser user) {
        return Optional.of(wishRepository.getAllRealizedWishes(user));
    }

    @Override
    public Quote updateMonthGroup(Quote wish) throws BadIncomeParameter {
        Quote searchedWish = wishRepository.findById(wish.getId()).orElseThrow(()->new BadIncomeParameter(BadIncomeParameter.ParameterKind.WISH_ID_SEARCH));
        wish.setPriorityGroup(searchedWish.getPriorityGroup());
        return wish;
    }

    @Override
    public void updateWish(Quote wish) {
      wishRepository.save(wish);
    }

    @Override
    public Quote updateAndFlushWish(Quote wish) {
        return wishRepository.saveAndFlush(wish);
    }

    @Override
    public Quote addWish(Quote wish) {
        return wishRepository.saveAndFlush(wish);
    }

    @Override
    public Optional<Quote> getWishById(int id) {
        return wishRepository.findById(Long.valueOf(id));
    }

    @Override
    public Integer getSumm4All(LocalUser user) {
        return wishRepository.findAllByIdSorted(user).stream().map(Quote::getPrice).reduce(0, ArithmeticUtils::addAndCheck);
    }

    @Override
    public Integer getSumm4Prior(LocalUser user) {
        return wishRepository.getAllWithPriority1(user).stream().map(Quote::getPrice).reduce(0, ArithmeticUtils::addAndCheck);
    }

    @Override
    public Optional<Integer> getImplementedSum(LocalUser user, int period) {
        if (period==1) {
            return wishRepository.getImplementedSum4AllPeriod(user.getId());
        } else {
            return wishRepository.getImplementedSum4Month(user.getId());
        }
    }

    @Override
    public Salary saveSalary(Salary salary) {
        return salaryRepository.saveAndFlush(salary);
    }

    @Override
    public Salary getLastSalary(LocalUser localUser) {
        return (salaryRepository.getLastSalary(localUser)).size()<1 ? null : (salaryRepository.getLastSalary(localUser)).get(0);
    }

    @Override
    public Integer calculateImplementationPeriod(Integer summ, LocalUser localUser) {
        return summ / (getLastSalary(localUser).getResidualSalary());
    }


    @Override
    public  ResponseParseResult parseCsv(MultipartFile file, LocalUser localUser) throws IOException {

            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), "UTF-8"), ',', '"', 1);
            List<String[]> allRows = reader.readAll();
            Pattern pattern = Pattern.compile("^\\d{1,3}\\,");

            allRows.stream()
                    .map(strings -> String.join(",", strings))
                    .filter(pattern.asPredicate())
                    .forEach(f -> {

                        Pattern p = Pattern.compile("^\\d{1,3},(.*)(?=\\,,\\d)");
                        Pattern p2 = Pattern.compile("(?:,,)(\\d.*)(р.)");
                        Matcher m = p.matcher(f);
                        Matcher m2 = p2.matcher(f);
                        String localWish = "";
                        Integer localPrice = 0;

                        if (m.find()) {
                            localWish = m.group(1);
                        }

                        if (m2.find()) {
                            localPrice = Integer.parseInt(m2.group(1).replace(",", "").trim());
                        }

                        List<Quote> wishes = wishRepository.getWishesByName(localWish).orElseGet(ArrayList::new);

                        if (wishes.size() < 1) {
                            //нету? добавляем
                            wishRepository.save(new Quote(localWish, localPrice, 1, 1, false, "from csv", "", localUser, new Date()));
                            addCount++;
                        }
                    });

        return ResponseParseResult.builder().itemsAdded(addCount).status("Ok").okMessage("Парсинг успешно выполнен").build();
    }

    @Override
    public void clearCounter() {
        addCount = 0;
    }
}
