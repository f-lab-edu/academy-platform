package spring.academyPlatform.global.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {

	private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public static LocalDate parse(String dateTimeString) {
		if (dateTimeString == null || dateTimeString.isEmpty()) {
			return LocalDate.now();
		}
		return LocalDate.parse(dateTimeString, INPUT_FORMATTER);
	}

}

