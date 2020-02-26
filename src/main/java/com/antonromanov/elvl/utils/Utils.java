package com.antonromanov.elvl.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Тут собраны основные утлилиты.
 */
public class Utils {

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
}
