package com.antonromanov.elvl.controller;


import com.antonromanov.elvl.dto.*;
import com.antonromanov.elvl.exceptions.BadIncomeParameterException;
import com.antonromanov.elvl.exceptions.NoDataYetException;
import com.antonromanov.elvl.model.Quote;
import com.antonromanov.elvl.service.MainService;
import com.antonromanov.elvl.utils.ControllerBase;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;


/**
 * Основной REST-контроллер приложения.
 */
@RestController
@RequestMapping("/api/quotes")
public class MainRestController extends ControllerBase {

	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger("console_logger");

	@Autowired
	MainService mainService;

	/**
	 * Найти все котировки.
	 *
	 * @param resp
	 * @return
	 */
	@GetMapping()
	public ResponseEntity<String> findAllQuotes(HttpServletResponse resp) {
		// curl --location --request GET "http://localhost:8080/api/quotes"
		return $do(s -> {
			LOGGER.info("============== FIND ALL QUOTES ============== ");
			return $prepareResponse(PayloadDTO.builder().body(mainService.findAllQuotes()).build());
		}, null, resp);
	}

	/**
	 * Получить elvls по isin.
	 *
	 * @param isin
	 * @param resp
	 * @return
	 */
	@GetMapping("/byIsin/{isin}")
	public ResponseEntity<String> getElvlsByIsin(@PathVariable(value = "isin") String isin, HttpServletResponse resp) {
		// curl --location --request GET "http://localhost:8080/api/quotes/byIsin/isin=11"
		return $do(s -> {
			LOGGER.info("============== GET ELVLS BY ISIN ============== ");
			LOGGER.info("ISIN: {}", isin);
			if (isin.isBlank() || isin.length() != 12) {
				throw new BadIncomeParameterException("Isin должен быть длиной 12 знаков и не пустой!");
			} else {
				return $prepareResponse(PayloadDTO.builder().body(mainService.findAllElvlsByIsin(isin)
						.orElseThrow(() -> new NoDataYetException("Нет еще рассчитанных elvls"))).build());
			}
		}, null, resp);
	}

	/**
	 * Запросить все elvls.
	 *
	 * @param resp
	 * @return
	 */
	@GetMapping("/elvls")
	public ResponseEntity<String> getAllElvls(HttpServletResponse resp) {
// curl --location --request GET "http://localhost:8080/api/quotes/elvls"
		return $do(s -> {
			LOGGER.info("============== ВСЕ ELVLS ============== ");
			return $prepareResponse(PayloadDTO.builder().body(mainService.findAllElvls()).build());
		}, null, resp);
	}

	/**
	 * Добавить котировку.
	 *
	 * @param dto
	 * @param resp
	 * @return
	 */
	@PostMapping()
	public ResponseEntity<String> addQuote(@RequestBody QuoteDto dto, HttpServletResponse resp) {

		return $do(s -> {
			LOGGER.info("============== ДОБАВЛЕНИЕ КОТИРОВКИ ============== :", dto);
			LOGGER.info("bid: ", dto.getBid());

			/**
			 *  Организовано примитивным способом, потому что Spring Validator крайне плохая практика:
			 *  отладка и проброс эксепшеном на удаленном сервере еще и внутри самописных валидаторов - это отдельная жопа.
			 *
			 */
			if (dto.getBid()>dto.getAsk()) throw new BadIncomeParameterException("bid должен быть меньше ask!");
			if (dto.getIsin().length()!=12) throw new BadIncomeParameterException("Ожидаемая длина isin - 12 знаков!");
			Pair<Quote, Double> pair = mainService.addQuote(dto);
			return $prepareResponse(new NewQuoteDTO(pair.getFirst(), pair.getSecond()));
		}, null,  resp);
	}
}
