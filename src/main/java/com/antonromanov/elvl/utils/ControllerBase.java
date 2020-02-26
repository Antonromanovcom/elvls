package com.antonromanov.elvl.utils;

import com.antonromanov.elvl.dto.ResponseDTO;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import static com.antonromanov.elvl.utils.Utils.createGsonBuilder;

public class ControllerBase {

	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger("console_logger");

	protected <T,E>T $do(SomeProcess<T, E> process, E s, HttpServletResponse response){
		try {
			return process.aplly(s);
		} catch (Exception ex) {
			prepareError(ex, response);
			return null;
		}
	}

	protected ResponseEntity<String> $prepareResponse(ResponseDTO dto){
		String res = createGsonBuilder().toJson(dto);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setCacheControl("no-cache");
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(res, headers, HttpStatus.OK);
		return responseEntity;
	}

	private static void prepareError(Exception ex, HttpServletResponse response) {
		LOGGER.error(ex.getMessage(),ex);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setHeader("Content-Type","application/json;charset=UTF-8");

		try (PrintWriter pw = response.getWriter()) {
			pw.write(ex.getMessage() == null ? "Ошибка работы с api! ":ex.getMessage() );
		}catch (IOException ioe) {
			LOGGER.error("Не могу записать ошибку в Response", ex);
		}
	}

	protected interface SomeProcess<T, E> {
		T aplly(E req) throws Exception;
	}
}
