package py.podac.tech.agenda.model.validations;

import java.util.Arrays;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UppercaseCharacterRule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import py.podac.tech.agenda.model.interfaces.ValidPassword;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public void initialize(ValidPassword arg0) {
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		PasswordValidator validator = new PasswordValidator(
				Arrays.asList(new LengthRule(8, 30), new UppercaseCharacterRule(1), new DigitCharacterRule(1)
//           ,
//           new SpecialCharacterRule(1), 
//           new NumericalSequenceRule(3,false), 
//           new AlphabeticalSequenceRule(3,false), 
//           new QwertySequenceRule(3,false),
//           new WhitespaceRule()
				));

		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(validator.getMessages(result).toString()).addConstraintViolation();
		return false;
	}
}