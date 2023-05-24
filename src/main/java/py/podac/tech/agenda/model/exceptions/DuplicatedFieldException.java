package py.podac.tech.agenda.model.exceptions;


@SuppressWarnings("serial")
public class DuplicatedFieldException extends Exception {
	public DuplicatedFieldException(String errorMessage) {
		super(errorMessage);
	}
}
