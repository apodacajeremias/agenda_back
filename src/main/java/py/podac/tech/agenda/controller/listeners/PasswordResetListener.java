package py.podac.tech.agenda.controller.listeners;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import py.podac.tech.agenda.controller.events.OnPasswordResetEvent;
import py.podac.tech.agenda.model.services.interfaces.IUserService;
import py.podac.tech.agenda.security.user.User;

@Component
public class PasswordResetListener implements ApplicationListener<OnPasswordResetEvent> {

	@Autowired
	private IUserService service;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnPasswordResetEvent event) {
		this.confirmReset(event);
	}

	// TODO: enviar email solamente cuando ya se defina como construir la
	// infraestructura del servicio y ya haya URL propia
	private void confirmReset(OnPasswordResetEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createPasswordResetTokenForUser(user, token);

		mailSender.send(constructResetTokenEmail(event.getAppUrl(), event.getLocale(), token, user)); // NO ENVIAR
	}

	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String subject = messages.getMessage("password.reset.subject", null, locale);
		String url = contextPath + "/user/changePassword?token=" + token;
		String message = messages.getMessage("password.reset.message", null, locale);

		return constructEmail(subject, message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject(subject);
		email.setText(body);
		return email;
	}
}