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
import py.podac.tech.agenda.model.entities.Usuario;
import py.podac.tech.agenda.model.services.interfaces.IUsuarioService;

@Component
public class PasswordResetListener implements ApplicationListener<OnPasswordResetEvent> {

	@Autowired
	private IUsuarioService service;

	@Autowired
	private MessageSource messages;

	@SuppressWarnings("unused")
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnPasswordResetEvent event) {
		this.confirmReset(event);
	}

	private void confirmReset(OnPasswordResetEvent event) {
		Usuario usuario = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createPasswordResetTokenForUsuario(usuario, token);

//		mailSender.send(constructResetTokenEmail(event.getAppUrl(), event.getLocale(), token, usuario)); // NO ENVIAR
	}

	@SuppressWarnings("unused")
	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, Usuario usuario) {
		String subject = messages.getMessage("password.reset.subject", null, locale);
		String url = contextPath + "/user/changePassword?token=" + token;
		String message = messages.getMessage("password.reset.message", null, locale);

		return constructEmail(subject, message + " \r\n" + url, usuario);
	}

	private SimpleMailMessage constructEmail(String subject, String body, Usuario usuario) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(usuario.getEmail());
		email.setSubject(subject);
		email.setText(body);
		return email;
	}
}