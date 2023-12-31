package py.jere.agendate.controller.utils;

import java.time.LocalDate;
import java.time.Period;

public class Edad {

	private Edad() {
	}

	public static int calcular(LocalDate fechaNacimiento) {
		return (fechaNacimiento == null) ? 0 : Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

}
