package py.jere.agendate.controller.utils.reporte;

public enum DocumentType {
	// Se ignora totalmente el formato de la plantilla
	IGNORAR_FORMATO("IGNORAR_FORMATO"),
	// Se ignora el titulo del formato de la plantilla
	IGNORAR_TITULO("IGNORAR_TITULO"),
	// Se ignora el encabezado del formato de la plantilla
	IGNORAR_ENCABEZADO("IGNORAR_ENCABEZADO"),
	// Se ignora el pie de pagina del formato de la plantilla
	IGNORAR_PIE("IGNORAR_PIE");

	private String type;

	private DocumentType(String type) {
		this.type = type;
	}

	public String getType() {
		return type.toUpperCase();
	}
}
