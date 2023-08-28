package py.jere.agendate.controller.utils.reporte;

public enum BuilderType {
	// Se usa para descargar
	DESCARGAR("attachment"), 
	// Se usa para previsualizar
	PREVISUALIZAR("inline");

	private String type;

	private BuilderType(String type) {
		this.type = type;
	}

	public String getType() {
		return type.toLowerCase();
	}
}
