package py.jere.agendate.controller.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Calendario {
	private static final DateTimeFormatter YEAR_PY = DateTimeFormatter.ofPattern("yyyy", new Locale("es", "PY"));

	private Calendario() {
	}

	public static synchronized String obtenerAnho(LocalDate fecha) {
		return fecha.format(YEAR_PY);
	}

	public static synchronized String obtenerAnho(LocalDateTime fecha) {
		return fecha.format(YEAR_PY);
	}
	
	public static synchronized Date toDate(LocalDate fecha) {
		return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public static synchronized Date toDate(LocalDateTime fecha) {
		return Date.from(fecha.atZone(ZoneOffset.systemDefault()).toInstant());
	}
}
