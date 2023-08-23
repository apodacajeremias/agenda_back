package py.jere.agendate.controller.listeners;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import py.jere.agendate.controller.events.OnResendRegistrationEvent;
import py.jere.agendate.model.entities.Usuario;
import py.jere.agendate.model.services.interfaces.IUsuarioService;

@Component
public class ResendRegistrationListener implements ApplicationListener<OnResendRegistrationEvent> {

	@Autowired
	private IUsuarioService service;

	@Autowired
	private MessageSource messages;

//	@Autowired
//	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnResendRegistrationEvent event) {
		this.resendConfirmationToken(event);
	}

	private void resendConfirmationToken(OnResendRegistrationEvent event) {
		Usuario usuario = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(usuario, token);

		String recipientAddress = usuario.getEmail();
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