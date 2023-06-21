package py.podac.tech.agenda.model.interfaces;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import py.podac.tech.agenda.model.validations.PasswordMatchesValidator;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
	
	String message() default "Passwords don't match";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}