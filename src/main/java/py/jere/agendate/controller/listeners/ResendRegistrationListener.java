package py.jere.agendate.controller.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import py.jere.agendate.controller.events.OnResendRegistrationEvent;
import py.jere.agendate.security.config.JwtService;
import py.jere.agendate.security.token.ITokenService;
import py.jere.agendate.security.token.Token;
import py.jere.agendate.security.token.TokenType;
import py.jere.agendate.security.user.User;

@Component
public class ResendRegistrationListener implements ApplicationListener<OnResendRegistrationEvent> {

	@Autowired
	private ITokenService service;

	@Autowired
	private JwtService jwt;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnResendRegistrationEvent event) {
		try {
			this.confirmRegistration(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void confirmRegistration(OnResendRegistrationEvent event) throws Exception {
		User user = event.getUser();
		String token = jwt.generateRefreshToken(user);
		Token guardar = Token.builder().user(user).token(token).type(TokenType.VERIFICATION).build();
		service.registrar(guardar);

		String recipientAddress = user.getEmail();
		String subject = "Resend Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/auth/register/confirm/" + guardar.getId();
		String message = messages.getMessage("message.resendToken", null, event.getLocale());

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
		mailSender.send(email);
	}
	
}