package py.podac.tech.agenda.controller.listeners;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import py.podac.tech.agenda.controller.events.OnResendRegistrationEvent;
import py.podac.tech.agenda.model.services.interfaces.IUserService;
import py.podac.tech.agenda.security.user.User;

@Component
public class ResendRegistrationListener implements ApplicationListener<OnResendRegistrationEvent> {

	@Autowired
	private IUserService service;

	@Autowired
	private MessageSource messages;

//	@Autowired
//	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnResendRegistrationEvent event) {
		this.resendConfirmationToken(event);
	}

	// TODO: enviar email solamente cuando ya se defina como construir la
	// infraestructura del servicio y ya haya URL propia
	private void resendConfirmationToken(OnResendRegistrationEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = messages.getMessage("registration.resend.subject", null, event.getLocale());
		String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
		String message = messages.getMessage("registration.resend.message", null, event.getLocale());

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
//		mailSender.send(email); //NO ENVIAR
	}
}