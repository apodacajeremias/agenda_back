package py.jere.agendate.controller.listeners;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import py.jere.agendate.controller.events.OnPasswordResetEvent;
import py.jere.agendate.security.config.JwtService;
import py.jere.agendate.security.token.ITokenService;
import py.jere.agendate.security.token.Token;
import py.jere.agendate.security.token.TokenType;
import py.jere.agendate.security.user.User;

@Component
public class PasswordResetListener implements ApplicationListener<OnPasswordResetEvent> {

	@Autowired
	private ITokenService service;

	@Autowired
	private JwtService jwt;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnPasswordResetEvent event) {
		try {
			this.confirmReset(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void confirmReset(OnPasswordResetEvent event) throws Exception {
		User user = event.getUser();
		String token = jwt.generateRefreshToken(user);
		Token guardar = Token.builder().user(user).token(token).type(TokenType.PASSWORD_RESET).build();
		service.registrar(guardar);
		mailSender.send(constructResetTokenEmail(event.getAppUrl(), event.getLocale(), guardar.getId(), user));
	}

	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, UUID id, User user) {
		String url = contextPath + "/auth/password/change/" + id;
		String message = messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" +"http://localhost:8080"+ url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		return email;
	}
}