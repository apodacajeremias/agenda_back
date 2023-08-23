package py.jere.agendate.model.exceptions;

@SuppressWarnings("serial")
public class UserAlreadyExistException extends Exception {
	public UserAlreadyExistException(String errorMessage) {
		super(errorMessage);
	}
}