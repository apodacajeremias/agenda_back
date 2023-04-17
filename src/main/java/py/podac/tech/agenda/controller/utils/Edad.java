package py.podac.tech.agenda.controller.utils;

import java.time.LocalDate;
import java.time.Period;

public class Edad {

	private Edad() {

	}

	public static int calcular(LocalDate fechaNacimiento) {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

}
