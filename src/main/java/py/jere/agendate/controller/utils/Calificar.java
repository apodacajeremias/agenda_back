package py.jere.agendate.controller.utils;

public class Calificar {

	private Calificar() {
		//
	}

	public static synchronized double porcentaje(double puntajeLogrado, double puntajeTotal) {
		return (puntajeLogrado / puntajeTotal) * 100;
	}

	public static synchronized double puntuar(double porcentaje) {
		if (porcentaje < 60) {
			return 1d;
		} else if (porcentaje >= 60 && porcentaje < 70) {
			return 2d;
		} else if (porcentaje >= 70 && porcentaje < 80) {
			return 3d;
		} else if (porcentaje >= 80 && porcentaje < 91) {
			return 4d;
		} else if (porcentaje >= 91) {
			return 5d;
		} else {
			return 1d;
		}
	}

	public static synchronized double puntuar(double puntajeLogrado, double puntajeTotal) {
		return puntuar(porcentaje(puntajeLogrado, puntajeTotal));
	}

	public static synchronized double puntaje(double puntajeTotal, double porcentaje) {
		return puntajeTotal * (porcentaje / 100);
	}
}
