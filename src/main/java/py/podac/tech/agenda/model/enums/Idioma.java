package py.podac.tech.agenda.model.enums;

public enum Idioma {
	CASTELLANO("es-PY"), PORTUGUES("pt-BR"), INGLES("en-US");

	private String code;

	private Idioma(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
