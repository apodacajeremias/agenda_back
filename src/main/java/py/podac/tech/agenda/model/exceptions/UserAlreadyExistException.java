package py.podac.tech.agenda.model.exceptions;

@SuppressWarnings("serial")
public class UserAlreadyExistException extends Exception {
	public UserAlreadyExistException(String errorMessage) {
		super(errorMessage);
	}
}