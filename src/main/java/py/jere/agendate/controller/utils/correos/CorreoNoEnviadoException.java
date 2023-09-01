package py.jere.agendate.controller.utils.correos;

public class CorreoNoEnviadoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8075375173459131243L;

	public CorreoNoEnviadoException(String errorMessage) {
		super(errorMessage);
	}

}
