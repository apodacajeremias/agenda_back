package py.jere.agendate.controller.utils;

import java.util.Locale;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class Numerico {

	private Numerico() {

	}

	public static String escribirEnLetras(Object numero) {
		return new RuleBasedNumberFormat(new Locale("es-ES"), RuleBasedNumberFormat.SPELLOUT).format(numero);
	}

	// Se usa para recibir unos parametros que manda ion.rangeSlider.js como String
	public static Integer[] cadenaTextoAVectorNumerico(String cadena, String separador) {
		String[] cadenaTexto = cadena.split(separador);
		Integer[] vectorNumerico = new Integer[cadenaTexto.length];
		for (int i = 0; i < cadenaTexto.length; i++) {
			vectorNumerico[i] = Integer.parseInt(cadenaTexto[i]);
		}
		return vectorNumerico;
	}

}
