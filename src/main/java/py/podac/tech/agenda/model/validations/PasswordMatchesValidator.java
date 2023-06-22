package py.podac.tech.agenda.model.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import py.podac.tech.agenda.model.interfaces.PasswordMatches;
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.reset.PasswordResetRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		boolean isValid = false;
		if (obj instanceof User) {
			User user = (User) obj;
			isValid = user.getPassword().equals(user.getMatchingPassword());
			return isValid;
		}
		if (obj instanceof PasswordResetRequest) {
			PasswordResetRequest passwordReset = (PasswordResetRequest) obj;
			isValid = passwordReset.getPassword().equals(passwordReset.getMatchingPassword());
			return isValid;
		}
		return false;
	}
}