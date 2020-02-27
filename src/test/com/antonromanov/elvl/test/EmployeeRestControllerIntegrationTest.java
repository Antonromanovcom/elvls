package com.antonromanov.elvl.test;

import com.antonromanov.elvl.controller.MainRestController;
import com.antonromanov.elvl.dto.QuoteDto;
import com.antonromanov.elvl.model.Quote;
import com.antonromanov.elvl.service.MainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MainRestController.class)
public class EmployeeRestControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MainService mainService;

	@Test
	public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {

		Quote quote1 = new Quote("123456789999", 110.1, 112.5);
		Quote quote2 = new Quote("123456789999", 100.1, 112.5);
		Quote quote3 = new Quote("123456789999", 50.1, 90.5);
		Quote quote4 = new Quote("000000456789", 40.1, 112.5);

		List<Quote> allQuotes = Arrays.asList(quote1, quote2, quote3, quote4);

		given(mainService.findAllQuotes()).willReturn(allQuotes);

		mvc.perform(get("/api/quotes")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.body", hasSize(4)))
				.andExpect(jsonPath("$.body[0].bid", is(quote1.getBid())));
	}

	@Test
	public void addTest() throws Exception {
		QuoteDto quote = new QuoteDto("000000456789", 40.1, 112.5);
		Pair<Quote, Double> mockResult = Pair.of(Quote.$fromDto(quote), 40.1);

		when(mainService.addQuote(quote))
				.thenReturn(mockResult);

		mvc.perform(post("/api/quotes")
				.content(asJsonString(quote))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	private static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			System.out.println("jsonContent = " + jsonContent);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
