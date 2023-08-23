package py.jere.agendate.model.exceptions;


@SuppressWarnings("serial")
public class DuplicatedFieldException extends Exception {
	public DuplicatedFieldException(String errorMessage) {
		super(errorMessage);
	}
}
