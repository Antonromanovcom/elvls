package com.antonromanov.elvl.utils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.antonromanov.elvl.dto.QuoteDto;
import com.antonromanov.elvl.model.Elvls;
import com.antonromanov.elvl.model.Quote;
import com.antonromanov.elvl.exceptions.*;
import com.antonromanov.elvl.model.WishDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * Тут собраны основные утлилиты.
 */
public class Utils {

	private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger("console_logger");

	public enum ParseType {ADD, EDIT}

	public enum OperationType {
		ADD_WISH, EDIT_WISH, DELETE_WISH, ADD_SALARY, GET_SUMS, GET_ALL_WISHES, GET_GROUP_WISHES,
		UP_PRIORITY, DOWN_PRIORITY, UP_MONTH, DOWN_MONTH, LOGIN, TOGGLE_MODE, GET_USER_INFO,
		UPDATE_USER, GET_CURRENT_USER, TOGGLE_USER_MODE
	}

	private static HashMap<Integer, String> colorClasses;


	/**
	 * Определяет лижит ли указанное время между двумя заданными.
	 *
	 * @param candidate
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean isBetween(LocalTime candidate, LocalTime start, LocalTime end) {
		return !candidate.isBefore(start) && !candidate.isAfter(end);
	}

	/**
	 * Конвертим SQL-TIME в LOCALTIME
	 *
	 * @param time
	 * @return
	 */
	public static LocalTime toLocalTime(java.sql.Time time) {
		return time.toLocalTime();
	}

	// Проверяем таймут до последнего пинга.
	public static Boolean checkTimeout(Time lastPingTime) {

		Date date = new Date();
		Time time = new Time(date.getTime());
		Boolean result = true;

		if (lastPingTime != null) { // время должно быть не ноль, иначе все наебнется
			// TODO надо еще проверить, чтобы дата была именно сегодняшняя
			LocalTime offsetTime = toLocalTime(lastPingTime).plusMinutes(15);
			result = isBetween(toLocalTime(time), toLocalTime(lastPingTime), offsetTime);
		}
		return result;

	}


	/**
	 * Проверяем ip
	 */
	public static String getIp(HttpServletRequest request) {

		String remoteAddr = "";

		// Пытаемся взять ip
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
				LOGGER.info("GETTING REQUEST FROM:  " + remoteAddr);
			}
		}

		return remoteAddr;
	}


	/**
	 * Создаем gson builder
	 */
	public static Gson createGsonBuilder() {


		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setDateFormat("dd/MM/yyyy")
				.registerTypeAdapter(java.sql.Time.class, new TimeSerializer())
				.create();

		return gson;
	}

	public static Gson createNullableGsonBuilder() {

		// Trick to get the DefaultDateTypeAdatpter instance
		// Create a first instance a Gson
		Gson gson = new GsonBuilder()
				.setDateFormat("dd/MM/yyyy")
				.create();

		// Get the date adapter
		TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);

		// Ensure the DateTypeAdapter is null safe
		TypeAdapter<Date> safeDateTypeAdapter = dateTypeAdapter.nullSafe();

		// Build the definitive safe Gson instance
		return new GsonBuilder()
				.registerTypeAdapter(Date.class, safeDateTypeAdapter)
				.create();

		//	return gson;
	}


	/**
	 * Конвертим пришедший json в нового пользака и валидируем
	 *//*
	public static LocalUser parseJsonToUserAndValidate(String json) throws Exception {

		if (JSONTemplate.fromString(json).getAsJsonObject().size() == 0) {
			throw new JsonNullException("JSON - пустой");
		}

		LocalUser localUser;
		Date currentDate = new Date();


		try {
			// ------------------ Валидация -------------------------

			if (isBlank(JSONTemplate.fromString(json).get("login").getAsString())) throw new JsonParseException(json);

			LocalUser.Role userRole;

			if (("USER".equals(JSONTemplate.fromString(json).get("userRole").getAsString())) ||
					("ADMIN".equals(JSONTemplate.fromString(json).get("userRole").getAsString()))) {
				userRole = LocalUser.Role.valueOf(JSONTemplate.fromString(json).get("userRole").getAsString());
			} else {
				userRole = LocalUser.Role.USER;
			}

			if (JSONTemplate.fromString(json).get("userCryptoMode") == null) throw new JsonParseException(json);
			if (JSONTemplate.fromString(json).get("pwd") == null) throw new JsonParseException(json);
			if (JSONTemplate.fromString(json).get("email") == null) throw new JsonParseException(json);
			if (JSONTemplate.fromString(json).get("fullname") == null) throw new JsonParseException(json);


			localUser = new LocalUser(
					JSONTemplate.fromString(json).get("login").getAsString(),
					userRole,
					JSONTemplate.fromString(json).get("pwd").getAsString(),
					JSONTemplate.fromString(json).get("userCryptoMode").getAsBoolean(),
					JSONTemplate.fromString(json).get("email").getAsString(),
					JSONTemplate.fromString(json).get("fullname").getAsString()
			);

			localUser.setCreationDate(currentDate);

		} catch (Exception e) {
			throw new JsonParseException(json);
		}
		return localUser;
	}*/


	/**
	 * Конвертим пришедший json в новую Salary
	 *//*
	public static Salary parseJsonToSalary(String json, LocalUser user) throws Exception {

		if (JSONTemplate.fromString(json).getAsJsonObject().size() == 0) {
			throw new JsonNullException("JSON - пустой");
		}

		Salary salary;
		Date currentDate = new Date();

		try {
			salary = new Salary(
					JSONTemplate.fromString(json).get("fullsalary").getAsInt(),
					JSONTemplate.fromString(json).get("residualSalary").getAsInt()
			);
			salary.setSalarydate(currentDate);
			LocalDateTime currentTimestamp = LocalDateTime.now();
			salary.setSalaryTimeStamp(currentTimestamp);

			salary.setUser(user);
		} catch (Exception e) {
			throw new JsonParseException(json);
		}
		return salary;
	}*/


	/**
	 * Конвертим пришедший json в новый WISH
	 *//*
	public static Quote parseJsonToWish(ParseType parseType, String json, LocalUser user) throws Exception {

		if (JSONTemplate.fromString(json).getAsJsonObject().size() == 0) {
			throw new JsonNullException("JSON - пустой");
		}

		Quote wishAfterParse;
		Date currentDate = new Date();

//		try {

		if (parseType == ParseType.EDIT) {
			wishAfterParse = null;
				*//*wishAfterParse = new Quote(
						JSONTemplate.fromString(json).get("id").getAsLong(),
						JSONTemplate.fromString(json).get("wish").getAsString(),
						JSONTemplate.fromString(json).get("price").getAsInt(),
						JSONTemplate.fromString(json).get("priority").getAsInt(),
						JSONTemplate.fromString(json).get("archive").getAsBoolean(),
						JSONTemplate.fromString(json).get("description").getAsString(),
						JSONTemplate.fromString(json).get("url").getAsString(),
						user);

				boolean realizedWish = JSONTemplate.fromString(json).get("realized").getAsBoolean();
				if (realizedWish) {
					wishAfterParse.setRealized(true);
					wishAfterParse.setRealizationDate(new Date());*//*
		//}

	} else

	{
		//	wishAfterParse = null;
			*//*	wishAfterParse = new Quote(
						JSONTemplate.fromString(json).get("wish").getAsString(),
						JSONTemplate.fromString(json).get("price").getAsInt(),
						JSONTemplate.fromString(json).get("priority").getAsInt(),
						JSONTemplate.fromString(json).get("archive").getAsBoolean(),
						JSONTemplate.fromString(json).get("description").getAsString(),
						JSONTemplate.fromString(json).get("url").getAsString(),
						user
				);

				wishAfterParse.setCreationDate(currentDate);*//*

	}
		*//*} catch (Exception e) {
			throw new JsonParseException(json);
		}*//*

//		return wishAfterParse;
		return null;

	}*/

	/*public static String generateRandomPassword() {

		List rules = Arrays.asList(new CharacterRule(EnglishCharacterData.UpperCase, 1),
				new CharacterRule(EnglishCharacterData.LowerCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1), new CharacterRule(EnglishCharacterData.Special, 1));

		PasswordGenerator generator = new PasswordGenerator();
		String password = generator.generatePassword(8, rules);
		return password;
	}*/

	public static String computerMonth(Integer proirity) {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();

		Locale currentLocale = Locale.getDefault();
		return Month.of((month + (proirity - 1)) > 12 ? (month + (proirity - 1)) - 12 : (month + (proirity - 1))).getDisplayName(TextStyle.FULL_STANDALONE, currentLocale);
	}

	public static int computerMonthNumber(Integer proirity) {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		return Month.of((month + (proirity - 1)) > 12 ? (month + (proirity - 1)) - 12 : (month + (proirity - 1))).getValue();
	}


	public static int getCurrentYear(Integer proirity) {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		return (month + (proirity - 1)) > 12 ? localDate.getYear() + 1 : localDate.getYear();
	}

	/*public static Double calculateElvls(Elvls elvls, QuoteDto dto) {
		return dto.getBid() >= elvls.getElvls() ?  dto.getBid() : dto.getAsk();
	}
*/
	public static WishDTO prepareWishDTO(Quote w, int maxPrior) {
		/*return WishDTO.builder()
				.id(w.getId())
//				.wish(w.getWish().length()<50 ? w.getWish() : w.getWish().substring(0, 50) + "...")
				.wish(w.getWish())
				.price(w.getPrice())
				.priority(w.getPriority())
				.ac(w.getAc())
				.description(w.getDescription())
				.url(w.getUrl())
				.priorityGroup(w.getPriorityGroup())
				.priorityGroupOrder(w.getPriorityGroupOrder())
				.month(computerMonth(w.getPriorityGroup() == null ? maxPrior : w.getPriorityGroup()))
				.build();*/
		return null;
	}

	public static String getClassColorByMonth(int month, boolean overdraft) {

		colorClasses = new HashMap<>();
		colorClasses.put(1, "label label-purple");
		colorClasses.put(2, "label label-blue");
		colorClasses.put(3, "label label-light-blue");
		colorClasses.put(4, "label label-orange");
		colorClasses.put(5, "label label-success");
		colorClasses.put(6, "label label-purple");
		colorClasses.put(7, "label label-blue");
		colorClasses.put(8, "label label-light-blue");
		colorClasses.put(9, "label label-orange");
		colorClasses.put(10, "label label-success");
		colorClasses.put(11, "label label-purple");
		colorClasses.put(12, "label label-blue");
		colorClasses.put(13, "label label-danger");

//		LOGGER.info("MONTH (getClassColorByMonth) => " + month);

		if (!overdraft) {
			if (month == 0) {
				return colorClasses.get(1);
			} else {
				return colorClasses.get(month);
			}
		} else {
			return colorClasses.get(13);
		}
	}

/*	public static String defineUserActionByMethodSignature(Signature signature) {

		String action = "UNKNOWN";

		switch (signature.getName()) {
			case "getSumm":
				action = "GET_SUM";
				break;
			case "findAll":
				action = "GET_ALL";
				break;
			case "gelAllWishes":
				action = "GET_ALL_WISHES";
				break;
			case "updateWish":
				action = "UPDATE_WISH";
				break;
			case "addWish":
				action = "ADD_NEW_WISH";
				break;
			case "deleteWish":
				action = "DELETE_WISH";
				break;
			case "getLastSalary":
				action = "GET_LAST_SALARY";
				break;
			case "addSalary":
				action = "ADD_SALARY";
				break;
			case "parseCsv":
				action = "PARSE_CSV_FILE";
				break;
			case "changePriority":
				action = "CHANGE_WISH_PRIORITY";
				break;
			case "changeMonth":
				action = "CHANGE_MONTH";
				break;
			case "addUser":
				action = "ADD_NEW_USER";
				break;
			case "deleteUser":
				action = "DELETE_USER";
				break;
			case "editUser":
				action = "EDIT_USER";
				break;
			case "toggleUserMode":
				action = "TOGGLE_USER_VIEW_MODE";
				break;
			case "getAllUsers":
				action = "GET_ALL_USERS";
				break;
			case "getCurrentUser":
				action = "GET_CURRENT_USER";
				break;
			case "returnUserPassword":
				action = "RETURN_USER_PASSWORD";
				break;
			case "resetUserPasswordByAdmin":
				action = "RETURN_USER_PASSWORD_BY_ADMIN";
				break;
		}


		return action;
	}*/

	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static int monthNameToNumber(String value) {
		int result = 0;
		switch (value) {
			case "Январь":
				result = 1;
				break;
			case "Февраль":
				result = 2;
				break;
			case "Март":
				result = 3;
				break;
			case "Апрель":
				result = 4;
				break;
			case "Май":
				result = 5;
				break;
			case "Июнь":
				result = 6;
				break;
			case "Июль":
				result = 7;
				break;
			case "Август":
				result = 8;
				break;
			case "Сентябрь":
				result = 9;
				break;
			case "Октябрь":
				result = 10;
				break;
			case "Ноябрь":
				result = 11;
				break;
			case "Декабрь":
				result = 12;
				break;
		}

		LOGGER.info("GETTING REQUEST TO CHANGE MOVE. STRING MONTH NAME PARSED TO:  " + result);

		return result;
	}

	public static int parseMonthAndCalculatePriority(String monthAndYear) throws BadIncomeParameter {

		if (Pattern.compile("[А-Яа-я]+ [0-9]+").matcher(monthAndYear).find()) {

			final Pattern pattern = Pattern.compile("([0-9]+)", Pattern.MULTILINE);
			final Matcher matcher = pattern.matcher(monthAndYear);
			String year = "";
			String month = "";

			while (matcher.find()) {
				year = matcher.group(1);
				month = monthAndYear.substring(0, matcher.start(1)).trim();
			}
			if (isBlank(year) && isInteger(year)) {
				throw new BadIncomeParameter(BadIncomeParameter.ParameterKind.WRONG_MONTH);
			} else {
				int minMonth = (YearMonth.now().getMonthValue()); // приоритет = 1
				if ((Calendar.getInstance().get(Calendar.YEAR)) == Integer.parseInt(year)) {
					return monthNameToNumber(month) - minMonth + 1; // разница между приоритетами;
				} else {

					return (12 - minMonth) + 1 + monthNameToNumber(month); // разница между приоритетами;
				}
			}

		} else {
			// Если false - то не нашли
			throw new BadIncomeParameter(BadIncomeParameter.ParameterKind.WRONG_MONTH);
		}
	}

}
