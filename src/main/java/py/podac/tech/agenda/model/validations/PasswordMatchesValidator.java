package py.podac.tech.agenda.model.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import py.podac.tech.agenda.model.interfaces.PasswordMatches;
import py.podac.tech.agenda.security.user.User;

public class PasswordMatchesValidator
implements ConstraintValidator<PasswordMatches, Object> {
  
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
  }
  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){
      User user = (User) obj;
      return user.getPassword().equals(user.getMatchingPassword());
  }
}