package com.antonromanov.elvl.controller;

import com.antonromanov.elvl.dto.QuoteDto;
import com.antonromanov.elvl.model.*;
import com.antonromanov.elvl.service.MainService;
import com.antonromanov.elvl.utils.ControllerBase;
import lombok.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static com.antonromanov.elvl.utils.Utils.*;

/**
 * Основной REST-контроллер приложения.
 */
@RestController
@RequestMapping("/api/quotes")
public class MainRestController extends ControllerBase {

	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger("console_logger");

	@Data
	private class DTO {
		private List<Quote> list = new ArrayList<>();
	}

	@Data
	private class DTOwithOrder {
		private List<WishDTOList> list = new ArrayList<>();
	}

	@Autowired
	MainService mainService;

	@GetMapping()
	public ResponseEntity<String> findAll(HttpServletResponse resp) {

		return $do(s -> {
			LOGGER.info("============== FILTER WISHES ============== ");
			List<Quote> quotes = mainService.findAllQuotes();
			DTO dto = new DTO();
			dto.list.addAll(quotes);
			String res = createGsonBuilder().toJson(dto);
			LOGGER.info("PAYLOAD: " + res);

			return $prepareResponse(res);

		}, null, null, null, resp);
	}

	@GetMapping("/byIsin/{isin}")
	public ResponseEntity<String> fetByIsin(@PathVariable String isin, HttpServletResponse resp) {

// curl --location --request GET "http://localhost:8080/api/quotes/byIsin/111" \ --header "Content-Type: application/json"

		return $do(s -> {
			LOGGER.info("============== FILTER WISHES ============== ");
			String res = createGsonBuilder().toJson(mainService.findAllElvlsByIsin(isin).get());
			LOGGER.info("PAYLOAD: " + res);
			return $prepareResponse(res);

		}, null, null, null, resp);
	}

	@GetMapping("/elvls")
	public ResponseEntity<String> getAllElvls(HttpServletResponse resp) {

// curl --location --request GET "http://localhost:8080/api/quotes/elvls"

		return $do(s -> {
			LOGGER.info("============== FILTER WISHES ============== ");
			String res = createGsonBuilder().toJson(mainService.findAllElvls());
			LOGGER.info("PAYLOAD: " + res);
			return $prepareResponse(res);

		}, null, null, null, resp);
	}

	@PostMapping()
	public ResponseEntity<String> addQuote(@RequestBody QuoteDto dto, HttpServletResponse resp) {

		return $do(s -> {
			LOGGER.info("============== FILTER WISHES ============== :", dto);
		//	List<Quote> quotes = mainService.findAllQuotes();
			Double d = mainService.addQuote(dto);
			DTO dto1 = new DTO();
			//dto1.list.addAll(quotes);
			String res = createGsonBuilder().toJson(dto1);
			LOGGER.info("PAYLOAD: " + res);

			return $prepareResponse(res);

		}, null, null, null, resp);
	}
}
