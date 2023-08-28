package py.jere.agendate.controller.utils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Textual {

	private static final String WORD_SEPARATOR = "\s";

	private static final NumberFormat formatoParaguayo = NumberFormat.getInstance(new Locale("es", "PY"));

	private Textual() {
	}

	// TODO: VALIDAR QUE LAS PALABRAS CITADAS ABAJO SEAN MINUSCULA
	// 'de', 'y', 'en'
	public static String toTitleCase(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}
		return Arrays.stream(text.split(WORD_SEPARATOR)).map(
				word -> word.isEmpty() ? word : Character.toTitleCase(word.charAt(0)) + word.substring(1).toLowerCase())
				.collect(Collectors.joining(WORD_SEPARATOR));
	}

	public static boolean isBlankOrNull(String str) {
		return (str == null || "".equals(str.trim()) || str.isEmpty() || str.isBlank());
	}

	public static boolean containsIgnoreCase(String textoOriginal, String textoVerificar) {
		return textoOriginal.toLowerCase().contains(textoVerificar.toLowerCase());
	}

	public static String formatearCedulaParaguaya(String numeroDeCedulaParaguaya) throws Exception {
		try {
			Object cedula = Integer.parseInt(numeroDeCedulaParaguaya);
			return formatoParaguayo.format(cedula);
		} catch (Exception e) {
			throw e;
		}
	}

}
